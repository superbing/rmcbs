package com.bfd.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bfd.bean.BusinessUnitsBean;
import com.bfd.bean.CompanyBean;
import com.bfd.bean.IpRangeBean;
import com.bfd.bean.es.DevinfoBean;
import com.bfd.common.exception.RmcbsException;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.BusinessUnitsMapper;
import com.bfd.dao.mapper.CompanyMapper;
import com.bfd.param.vo.BusinessUnitsVO;
import com.bfd.param.vo.CompanyVO;
import com.bfd.param.vo.IpRangeVO;
import com.bfd.service.BusinessUnitsService;
import com.bfd.config.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import redis.clients.jedis.Jedis;

/**
 * @Author: chong.chen
 * @Description: 单位商务信息实现
 * @Date: Created in 15:06 2018/8/2
 * @Modified by:
 */
@Service
public class BusinessUnitsServiceImpl implements BusinessUnitsService {

    @Autowired  
    private BusinessUnitsMapper businessUnitsMapper;


    @Value("${es.rms_device_info.indexName}")
    private String indexName;

    @Value("${es.rms_device_info.indexType}")
    private String indexType;

    @Value("${es.max.total}")
    private Long maxTotal;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void update(BusinessUnitsVO businessUnitsVO) {

        //获取jedis实例
        Map<String, String> map = Maps.newHashMap();
        //制造对象
        BusinessUnitsBean businessUnitsBean = new BusinessUnitsBean();
        businessUnitsBean.setBusinessId(businessUnitsVO.getId());
        businessUnitsBean.setStartTime(businessUnitsVO.getStartTime());
        businessUnitsBean.setEndTime(businessUnitsVO.getEndTime());
        businessUnitsBean.setConcurrence(businessUnitsVO.getConcurrence());
        businessUnitsBean.setConcurrenceStatus(businessUnitsVO.getConcurrenceStatus());
        businessUnitsBean.setTerminal(businessUnitsVO.getTerminal());
        businessUnitsBean.setTerminalStatus(businessUnitsVO.getTerminalStatus());
        businessUnitsBean.setIpStatus(businessUnitsVO.getIpStatus());

        if(businessUnitsVO.getConcurrenceStatus() != Constants.ZERO){
            RedisUtil.set(Constants.LIMIT_SET+String.valueOf(businessUnitsVO.getId()), String.valueOf(businessUnitsVO.getConcurrence()));
        }
        //根据ID查询客户单位设置信息
        BusinessUnitsBean business = businessUnitsMapper.getBusinessById(businessUnitsVO.getId());
        CompanyBean companyBean = companyMapper.getCompanyById(businessUnitsVO.getId());
        String deviceKey = Constants.LIMIT_DEVICE.concat(String.valueOf(businessUnitsVO.getId()));
        String ipKey = Constants.LIMIT_IP.concat(String.valueOf(businessUnitsVO.getId()));
        //先判断是新增还是删除,如果为空则是新增
        if(business == null){
            //新增如果没开,就不存
            if (businessUnitsVO.getTerminalStatus() != Constants.ZERO){
                map.put(Constants.TOTAL, String.valueOf(businessUnitsVO.getTerminal()));
                map.put(Constants.BOOKED, Constants.ZERO_STRING);
                RedisUtil.hmset(deviceKey, map);
            }
            businessUnitsMapper.insert(businessUnitsBean);
        }else{

            //先判断终端数是否变化
            if(businessUnitsVO.getTerminal() != business.getTerminal() ){

                if(businessUnitsVO.getTerminal() < business.getTerminal() ){
                    //如果终端数调小,则提示清空终端数
                    throw new RmcbsException("终端数的设置不能减少,如需调整请清空终端数,然后再更改.");
                }
                //如果关闭了,将key删除
                if(businessUnitsVO.getTerminalStatus() == Constants.ZERO){
                    //判断key是否存在
                    if(RedisUtil.exists(String.valueOf(businessUnitsVO.getId()))){
                        RedisUtil.del(deviceKey);
                    }
                }else {
                    List<String> booked = RedisUtil.hmget(String.valueOf(businessUnitsVO.getId()),Constants.BOOKED);
                    map.put(Constants.TOTAL, String.valueOf(businessUnitsVO.getTerminal()));
                    if(booked == null || booked.get(Constants.ZERO) == null){
                        map.put(Constants.BOOKED, Constants.ZERO_STRING);
                    }else {
                        map.put(Constants.BOOKED, booked.get(Constants.ZERO));
                    }
                    RedisUtil.hmset(deviceKey, map);
                }
            }
            //不为空则是更新
            businessUnitsMapper.update(businessUnitsBean);
        }

        //判断开关状态,如果关闭则删除
        if(businessUnitsVO.getIpStatus() == Constants.ZERO){
            //判断key是否存在
            if(RedisUtil.exists(ipKey)){
                RedisUtil.del(ipKey);
            }
        }else {
            //将Ip范围添加到redis
            String ipJson = JSON.toJSONString(businessUnitsVO.getIpRangeList());
            RedisUtil.set(ipKey,ipJson);
        }
        //先看看是否有Ip范围
        if(businessUnitsVO.getIpRangeList() !=null && businessUnitsVO.getIpRangeList().size() > Constants.ZERO){
            //开始修改Ip范围，先将以前的IP范围删了
            List<BusinessUnitsBean> businessUnitsBeans = Lists.newArrayList();
            businessUnitsBeans.add(businessUnitsBean);
            businessUnitsMapper.deleteIp(businessUnitsBeans);
            //变成ipRangeBean，再添加
            List<IpRangeBean> ipRangeBeanList = becomeIpRangeBean(businessUnitsVO.getIpRangeList(),businessUnitsVO.getId());
            businessUnitsMapper.insertIp(ipRangeBeanList);

        }
    }


    @Override
    public PageVO<BusinessUnitsVO> getCustomersList(CompanyVO companyVO) {

        List<BusinessUnitsVO> businessUnitsVOS = Lists.newArrayList();
        //分页查询
        PageHelper.startPage(companyVO.getCurrent(),companyVO.getPageSize());
        //默认排序
        if (companyVO.getSortField() == null || companyVO.getSortField().equals("")) {
            companyVO.setSortField("createTime");
        }
        if (companyVO.getSortType() == null || companyVO.getSortType().equals("")) {
            companyVO.setSortType("desc");
        }
        //如果传入的字段不包含在排序字段串里面,证明传入排序数据有误
        if(!Constants.SORT_CUSTOMER.contains(companyVO.getSortField()) || !Constants.SORT.contains(companyVO.getSortType())){
            throw new RmcbsException("传入的排序变量有误");
        }
        List<BusinessUnitsVO> customerList = businessUnitsMapper.getCustomersList(companyVO);
        //通过PageHelper获取PageInfo返回对象
        PageInfo<BusinessUnitsVO> pageInfo = new PageInfo<BusinessUnitsVO>(customerList);
        for (BusinessUnitsVO businessUnitsVO: pageInfo.getList()){
            int entryTerminal = getTerminalAccount(businessUnitsVO.getId());
            businessUnitsVO.setEntryTerminal(entryTerminal);
            businessUnitsVOS.add(businessUnitsVO);
        }
        return new PageVO<BusinessUnitsVO>(companyVO.getCurrent(), companyVO.getPageSize(), pageInfo.getTotal(),businessUnitsVOS);
    }

    @Override
    public BusinessUnitsVO getBusinessUnits(long id) {

        //根据ID查询出单位商务信息
        BusinessUnitsVO businessUnitsVO = businessUnitsMapper.getBusiness(id);
        //获取IP范围
        List<IpRangeBean> ipRangeBeanList = businessUnitsMapper.getIpRangeList(id);
        //若果获取范围不为0,将其填入单位商务信息对象中
        if(businessUnitsVO != null && ipRangeBeanList != null && ipRangeBeanList.size() > Constants.ZERO){
            //去掉无用属性，再填入
            businessUnitsVO.setIpRangeList(becomeIpRangeVo(ipRangeBeanList));
        }
        return businessUnitsVO;
    }

    private List<IpRangeVO> becomeIpRangeVo(List<IpRangeBean> ipRangeBeanList){
        List<IpRangeVO> ipRangeVOList = Lists.newArrayList();
        for (IpRangeBean ipRangeBean:ipRangeBeanList){
            IpRangeVO ipRangeVO = new IpRangeVO();
            ipRangeVO.setStartIp(ipRangeBean.getStartIp());
            ipRangeVO.setEndIp(ipRangeBean.getEndIp());
            ipRangeVOList.add(ipRangeVO);
        }
        return ipRangeVOList;
    }
    private List<IpRangeBean> becomeIpRangeBean(List<IpRangeVO> ipRangeVOList,long businessId){
        List<IpRangeBean> ipRangeBeanList = Lists.newArrayList();
        for (IpRangeVO ipRangeVO:ipRangeVOList){
            IpRangeBean ipRangeBean = new IpRangeBean();
            ipRangeBean.setBusinessId(businessId);
            ipRangeBean.setStartIp(ipRangeVO.getStartIp());
            ipRangeBean.setEndIp(ipRangeVO.getEndIp());
            ipRangeBeanList.add(ipRangeBean);
        }
        return ipRangeBeanList;
    }

    /**
     * 获取终端数
     * @param companyVO 客户单位Id
     * @return
     */
    @Override
    public PageVO<DevinfoBean> getTerminalPage(CompanyVO companyVO){

        List<DevinfoBean> devinfoBeans = Lists.newArrayList();

        //分页从ES中获取
        Pageable pageable = PageRequest.of(companyVO.getCurrent()-1,companyVO.getPageSize());

        // 创建查询语句
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName)
                .withTypes(indexType)
                .withQuery(boolQuery()
                        .must(termQuery(Constants.BUSINESSID,companyVO.getCompanyId())))
                .withPageable(pageable)
                .build();
        Page<JSONObject> jsonObjects = elasticsearchTemplate.queryForPage(searchQuery, JSONObject.class);
        int i = 1;
        for (JSONObject jsonObject : jsonObjects){
            DevinfoBean devinfoBean = JSONObject.toJavaObject(jsonObject,DevinfoBean.class);
            devinfoBean.setNum(i);
            i++;
            devinfoBeans.add(devinfoBean);
        }
        long total = jsonObjects.getTotalElements();
        if(jsonObjects.getTotalElements() > maxTotal){
            total = maxTotal;
        }
        //封装返回前端的对象
        PageVO<DevinfoBean> pageVO = new
                PageVO<DevinfoBean>(companyVO.getCurrent(),companyVO.getPageSize(),total,devinfoBeans);

        return pageVO;
    }

    /**
     * 获取终端数
     * @param businessId 客户单位Id
     * @return
     */
    private int getTerminalAccount(long businessId){

        int result = 0;
        // 创建查询语句
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName)
                .withTypes(indexType)
                .withQuery(boolQuery()
                        .must(termQuery(Constants.BUSINESSID,businessId)))
                .build();
        List<JSONObject> jsonObjects = elasticsearchTemplate.queryForList(searchQuery, JSONObject.class);

        if(jsonObjects != null && jsonObjects.size() > Constants.ZERO){
            result = jsonObjects.size();
        }
        return result;
    }

    @Override
    public void deleteEntryTerminal(long businessId) {

        DeleteQuery deleteQuery = new DeleteQuery();
        deleteQuery.setIndex(indexName);
        deleteQuery.setType(indexType);
        deleteQuery.setQuery(boolQuery()
                .must(termQuery(Constants.BUSINESSID, businessId)));
        elasticsearchTemplate.delete(deleteQuery);

        List<String> total = RedisUtil.hmget(String.valueOf(businessId),Constants.TOTAL);
        Map<String, String> map = Maps.newHashMap();
        map.put(Constants.TOTAL, String.valueOf(Constants.ZERO));
        map.put(Constants.BOOKED, String.valueOf(Constants.ZERO));
        RedisUtil.hmset(String.valueOf(businessId), map);

        //将终端数的数量置为空和状态关闭
        BusinessUnitsBean businessUnitsBean = new BusinessUnitsBean();
        businessUnitsBean.setBusinessId(businessId);
        businessUnitsBean.setTerminal((long)Constants.ZERO);
        businessUnitsMapper.update(businessUnitsBean);


    }



}
