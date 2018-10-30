package com.bfd.service.impl;

import java.math.BigInteger;
import java.util.*;

import com.bfd.bean.BusinessApiBean;
import com.bfd.common.exception.RmcbsException;
import com.bfd.dao.mapper.*;
import com.bfd.param.vo.AddDeveloperVO;
import com.bfd.param.vo.CompanyStatusVO;
import com.bfd.param.vo.InsertApiVO;
import com.bfd.param.vo.edgesighvo.CompanyTreeDTO;
import com.bfd.service.ResourceService;
import com.bfd.config.RedisUtil;
import com.bfd.utils.SpringSecurityUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bfd.bean.BusinessUnitsBean;
import com.bfd.bean.CompanyBean;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.enums.EnabledEnum;
import com.bfd.param.vo.CompanyVO;
import com.bfd.service.CompanyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import redis.clients.jedis.Jedis;

/**
 * @Author: chong.chen
 * @Description: 平台商/客户单位ServiceImpl
 * @Date: Created in 14:32 2018/7/27
 * @Modified by:
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    
    @Autowired
    private CompanyMapper companyMapper;
    
    @Autowired
    private BusinessUnitsMapper businessUnitsMapper;
    
    @Autowired
    private BusinessApiMapper businessApiMapper;
    
    @Autowired
    private BusinessColumnMapper businessColumnMapper;
    
    @Autowired
    private BusinessPackageMapper businessPackageMapper;

    public static final Object object = new Object();

    @Autowired
    SequenceMapper sequenceMapper;

    @Autowired
    private ResourceService resourceService;
    
    @Override
    public PageVO<CompanyBean> getDevelopersList(CompanyVO companyVO) {
        // 平台商标记
        companyVO.setIsDevelopers(Constants.DEVELOPER);
        // 分页查询平台商
        PageHelper.startPage(companyVO.getCurrent(), companyVO.getPageSize());
        //默认排序
        if (companyVO.getSortField() == null || companyVO.getSortField().equals("")) {
            companyVO.setSortField("createTime");
        }
        if (companyVO.getSortType() == null || companyVO.getSortType().equals("")) {
            companyVO.setSortType("desc");
        }
        //如果传入的字段不包含在排序字段串里面,证明传入排序数据有误
        if(!Constants.SORT_DEVELOPER.contains(companyVO.getSortField()) || !Constants.SORT.contains(companyVO.getSortType())){
            throw new RmcbsException("传入的排序变量有误");
        }
        List<CompanyBean> developersList = companyMapper.getDevelopersPage(companyVO);
        // 通过PageHelper获取PageInfo返回对象
        PageInfo<CompanyBean> pageInfo = new PageInfo<CompanyBean>(developersList);
        return new PageVO<CompanyBean>(companyVO.getCurrent(), companyVO.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
    }
    
    @Override
    public List<CompanyBean> getAllDevelopersList() {
        CompanyVO companyVO = new CompanyVO();
        // 打上搜索平台商标注
        companyVO.setIsDevelopers(Constants.DEVELOPER);
        return companyMapper.getDevelopersList(companyVO);
    }
    
    @Override
    public PageVO<CompanyBean> getCustomerList(CompanyVO companyVO) {
        // 平台商标记，客户单位parent_company_id字段不为0
        companyVO.setIsDevelopers(Constants.DEVELOPER);
        // 分页查询出客户单位
        PageHelper.startPage(companyVO.getCurrent(), companyVO.getPageSize());
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
        List<CompanyBean> customerList = companyMapper.getCustomerList(companyVO);
        // 通过PageHelper获取PageInfo返回对象
        PageInfo<CompanyBean> pageInfo = new PageInfo<CompanyBean>(customerList);
        return new PageVO<CompanyBean>(companyVO.getCurrent(), companyVO.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<CompanyTreeDTO> getCompanyTree() {
        List<CompanyTreeDTO> companyTree = companyMapper.getCompanyTree();
        return companyTree;
    }
    
    @Override
    public CompanyBean getCompanyById(long id) {
        
        CompanyBean companyBean = companyMapper.getCompanyById(id);
        if (companyBean != null && companyBean.getParentCompanyId() == (long)Constants.DEVELOPER) {
            companyBean.setCustomerNumber(companyMapper.getCustomerById(id).size());
        }
        //判断是否是客户单位
        if(companyBean.getParentCompanyId() != Constants.ZERO){
            //查询其平台商信息
            CompanyBean parentBean = companyMapper.getCompanyById(companyBean.getParentCompanyId());
            //添加其用户自定义的信息
            companyBean.setParentExtraData(parentBean.getExtraData());
        }
        
        return companyBean;
    }

    @Override
    public String getExtraData(long id) {
        //查询其平台商信息
        CompanyBean parentBean = companyMapper.getCompanyById(id);
        return parentBean.getExtraData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addCustomer(CompanyBean companyBean) {

        companyBean.setCreateUser(String.valueOf(SpringSecurityUtil.getCurrentUserId()));
        companyBean.setUpdateUser(String.valueOf(SpringSecurityUtil.getCurrentUserId()));

        List<CompanyBean> verification = companyMapper.getCustomerByName(companyBean.getCompanyName());

        if (verification != null && verification.size() > Constants.ZERO){
           throw new RmcbsException("客户单位名称已存在，请重新命名。");
        }

        companyBean.setCompanyCode(getCompanyCode());
        
        // 生成accessKey
        String accessKey = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        companyBean.setAccessKey(accessKey);
        
        companyBean.setStatus(Constants.ENABLE_STATUS);
        long result = companyMapper.insert(companyBean);

        //为客户单位添加他平台商选择的接口
        List<BusinessApiBean> businessApiBeans = businessApiMapper.getById(companyBean.getParentCompanyId());
        if( businessApiBeans != null && businessApiBeans.size() > Constants.ZERO){
            List<Long> apiIds = Lists.newArrayList();
            businessApiBeans.forEach(businessApiBean -> apiIds.add(businessApiBean.getApiId()));
            businessApiMapper.insert(companyBean.getId(),apiIds);
        }
        if(StringUtils.isNotBlank(companyBean.getExtraData())){
            //查询其平台商信息
            CompanyBean parentBean = companyMapper.getCompanyById(companyBean.getParentCompanyId());
            //拼接存储到redis中的数据类型
            String redisExtraData = "";
            if(StringUtils.isNotBlank(parentBean.getExtraData())){
                redisExtraData = parentBean.getExtraData().substring(0,parentBean.getExtraData().length()-1)+","
                        +companyBean.getExtraData().substring(1,companyBean.getExtraData().length());
            }
            RedisUtil.set(Constants.LIMIT_EXTRA+String.valueOf(companyBean.getId()),redisExtraData);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addDeveloper(AddDeveloperVO addDeveloperVO) {

        List<CompanyBean> verification = companyMapper.getDeveloperByName(addDeveloperVO.getCompanyName());

        if (verification != null && verification.size() > Constants.ZERO){
            throw new RmcbsException("平台商名称已存在，请重新命名");
        }
        //生成CompanyBean对象
        CompanyBean companyBean = new CompanyBean();
        companyBean.setCompanyName(addDeveloperVO.getCompanyName());
        companyBean.setCompanyType(addDeveloperVO.getCompanyType());
        companyBean.setBusinessModel(addDeveloperVO.getBusinessModel());
        companyBean.setCompanyDescription(addDeveloperVO.getCompanyDescription());
        companyBean.setRegistrationPlace(addDeveloperVO.getRegistrationPlace());
        companyBean.setExtraData(addDeveloperVO.getExtraData());
        companyBean.setCreateUser(String.valueOf(SpringSecurityUtil.getCurrentUserId()));
        companyBean.setUpdateUser(String.valueOf(SpringSecurityUtil.getCurrentUserId()));

        // 生成accessKey
        String accessKey = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        companyBean.setAccessKey(accessKey);
        //设置默认状态值
        companyBean.setStatus(Constants.ENABLE_STATUS);
        //设置默认父值
        companyBean.setParentCompanyId(Constants.PARENT_COMPANY_ID);
        //返回生成的ID
        long result = companyMapper.insert(companyBean);

        //判断是否没选择过，不为null则先删除
        if(businessApiMapper.getById(companyBean.getId()) != null){
            //先变为集合
            List<Long> longs = Lists.newArrayList();
            longs.add(companyBean.getId());
            businessApiMapper.delete(longs);
        }
        //添加用户选择过得ID,判断用户ID是否选择
        if(addDeveloperVO.getApiIds() != null && addDeveloperVO.getApiIds().size() > Constants.ZERO){
            businessApiMapper.insert(companyBean.getId(),addDeveloperVO.getApiIds());
        }
        return Constants.ZERO;
    }

    /**
     * 活动CompanyCode自增方法
     * @return
     */
    private String getCompanyCode() {
        synchronized (object) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(Constants.NAME, Constants.COMPANYCODE);

            this.sequenceMapper.getNextVal(param);
            BigInteger uniqueId = (BigInteger)param.get(Constants.NEXT_VAL);
            String result = uniqueId.toString();
            return result;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomer(CompanyBean companyBean) {

        companyBean.setUpdateUser(String.valueOf(SpringSecurityUtil.getCurrentUserId()));

        List<CompanyBean> verification = companyMapper.getCustomerByName(companyBean.getCompanyName());

        if (verification != null && verification.size() > Constants.ZERO){
            if (verification.get(Constants.ZERO).getId() != companyBean.getId()){
                throw new RmcbsException("客户单位名称已存在，请重新命名。");
            }
        }

        //判断平台商是否更改
        CompanyBean parent = companyMapper.getCompanyById(companyBean.getId());
        //如果不等证明平台商修改了
        if (parent.getParentCompanyId() != companyBean.getParentCompanyId()){
            //获取旧客户单位选择的集合
            List<BusinessApiBean> apiBeans = businessApiMapper.getById(parent.getParentCompanyId());
            if(apiBeans != null && apiBeans.size() > Constants.ZERO){
                //生成apiID集合
                List<Long> apiIds = Lists.newArrayList();
                apiBeans.forEach(businessApiBean -> apiIds.add(businessApiBean.getApiId()));
                //先删除他以前平台商选择的接口
                businessApiMapper.deleteBusinessApi(companyBean.getId(),apiIds);
            }
            //为客户单位添加他平台商选择的接口
            List<BusinessApiBean> businessApiBeans = businessApiMapper.getById(companyBean.getParentCompanyId());
            if( businessApiBeans != null && businessApiBeans.size() > Constants.ZERO){
                List<Long> apiIds = Lists.newArrayList();
                //为客户单位添加他自己选择的接口
                List<BusinessApiBean> mySelf = businessApiMapper.getById(companyBean.getId());
                //将自己选择的接口制造成Map集合
                Map<Long,BusinessApiBean> mySelfMap =Maps.newHashMap();
                mySelf.forEach(businessApiBean -> mySelfMap.put(businessApiBean.getApiId(),businessApiBean));
                //制作需要添加的接口ID集合
                for (BusinessApiBean businessApiBean : businessApiBeans){
                    if(!mySelfMap.containsKey(businessApiBean.getApiId())){
                        apiIds.add(businessApiBean.getApiId());
                    }
                }
                businessApiMapper.insert(companyBean.getId(),apiIds);
            }
        }
        if(StringUtils.isNotBlank(companyBean.getExtraData())){
            //查询其平台商信息
            CompanyBean parentBean = companyMapper.getCompanyById(companyBean.getParentCompanyId());
            //拼接存储到redis中的数据类型
            String redisExtraData = "";
            if(StringUtils.isNotBlank(parentBean.getExtraData())){
                redisExtraData = parentBean.getExtraData().substring(0,parentBean.getExtraData().length()-2)+","
                        +companyBean.getExtraData().substring(2,companyBean.getExtraData().length());
            }
            RedisUtil.set(Constants.LIMIT_EXTRA+String.valueOf(companyBean.getId()),redisExtraData);
        }else {
            //判断key是否存在
            if(RedisUtil.exists(Constants.LIMIT_EXTRA+String.valueOf(companyBean.getId()))){
                RedisUtil.del(Constants.LIMIT_EXTRA+String.valueOf(companyBean.getId()));
            }
        }
        //更改客户单位信息
        return companyMapper.update(companyBean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resetAccessKey(long id) {
        CompanyBean companyBean = new CompanyBean();
        companyBean.setId(id);
        // 生成accessKey
        String accessKey = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        companyBean.setAccessKey(accessKey);
        //更改客户单位信息
        return companyMapper.update(companyBean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDeveloper(AddDeveloperVO addDeveloperVO) {

        List<CompanyBean> verification = companyMapper.getDeveloperByName(addDeveloperVO.getCompanyName());

        if (verification != null && verification.size() > Constants.ZERO){
            if (verification.get(Constants.ZERO).getId() != addDeveloperVO.getId()){
                throw new RmcbsException("平台商名称已存在，请重新命名");
            }
        }

        //生成CompanyBean对象
        CompanyBean companyBean = new CompanyBean();
        companyBean.setId(addDeveloperVO.getId());
        companyBean.setCompanyName(addDeveloperVO.getCompanyName());
        companyBean.setCompanyType(addDeveloperVO.getCompanyType());
        companyBean.setBusinessModel(addDeveloperVO.getBusinessModel());
        companyBean.setCompanyDescription(addDeveloperVO.getCompanyDescription());
        companyBean.setRegistrationPlace(addDeveloperVO.getRegistrationPlace());
        companyBean.setExtraData(addDeveloperVO.getExtraData());
        companyBean.setUpdateUser(String.valueOf(SpringSecurityUtil.getCurrentUserId()));
        //更新接口
        //判断是否没选择过，不为null则先删除
        if(businessApiMapper.getById(companyBean.getId()) != null){
            //先变为集合
            List<Long> longs = Lists.newArrayList();
            longs.add(companyBean.getId());
            businessApiMapper.delete(longs);
        }
        //判断是否选择了接口
        if(addDeveloperVO.getApiIds() != null && addDeveloperVO.getApiIds().size() > Constants.ZERO){

            //添加用户选择过得ID
            businessApiMapper.insert(companyBean.getId(),addDeveloperVO.getApiIds());
            List<CompanyBean> companyBeans = companyMapper.getCustomerById(addDeveloperVO.getId());
            //判断是否为空
            if(companyBeans != null && companyBeans.size() > Constants.ZERO){
                //生成companyBean的id集合
                List<Long> businessIds = Lists.newArrayList();
                //再添加
                List<InsertApiVO> insertApiVOList = Lists.newArrayList();
                for(CompanyBean companyBean1 : companyBeans){
                    for(Long apiId :addDeveloperVO.getApiIds()){
                        InsertApiVO insertApiVO = new InsertApiVO();
                        insertApiVO.setApiId(apiId);
                        insertApiVO.setBusinessId(companyBean1.getId());
                        insertApiVOList.add(insertApiVO);
                        businessIds.add(companyBean1.getId());
                    }
                }
                //先删除
                businessApiMapper.deleteChildren(businessIds,addDeveloperVO.getApiIds());
                //再添加
                businessApiMapper.insertChildren(insertApiVOList);

                //add by bing.shen 更新权限缓存
                for(Long businessId : businessIds){
                    resourceService.cacheAuth(businessId);
                }
            }
        }
        //查询原本信息
        CompanyBean bean = companyMapper.getCompanyById(companyBean.getId());
        //如果不为空
        if(StringUtils.isNotBlank(companyBean.getExtraData())){
            if(StringUtils.isNotBlank(bean.getExtraData())){
                if(!bean.getExtraData().equals(companyBean.getExtraData())){
                    //更改其下面所有客户单位redis中的数据
                    List<CompanyBean> companyBeanList = companyMapper.getCustomerById(companyBean.getId());
                    if(companyBeanList != null && companyBeanList.size() > Constants.ZERO){
                        //遍历,按个改
                        for(CompanyBean customer : companyBeanList){
                            String redisExtraData = "";
                            if(StringUtils.isNotBlank(customer.getExtraData())){
                                redisExtraData = companyBean.getExtraData().substring(0,companyBean.getExtraData().length()-1)+","
                                        +customer.getExtraData().substring(1,customer.getExtraData().length());
                            }
                            RedisUtil.set(Constants.LIMIT_EXTRA+String.valueOf(customer.getId()),redisExtraData);
                        }
                    }
                }
                //如果为空
            }else{
                //判断以前是否有,如果有那么气客户单位存入redis的信息也得改
                if(bean.getExtraData() != null && bean.getExtraData().length() > Constants.ZERO){
                    //更改其下面所有客户单位redis中的数据
                    List<CompanyBean> companyBeanList = companyMapper.getCustomerById(companyBean.getId());
                    if(companyBeanList != null && companyBeanList.size() > Constants.ZERO){
                        //遍历,按个改
                        for(CompanyBean customer : companyBeanList){
                            if(StringUtils.isNotBlank(customer.getExtraData())){
                                RedisUtil.set(Constants.LIMIT_EXTRA+String.valueOf(customer.getId()),customer.getExtraData());
                            }
                        }
                    }
                }
            }


        }
        //更新平台商
        return companyMapper.update(companyBean);
    }
    
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateStatus(CompanyStatusVO companyStatusVO) {

        Long  createUser  = SpringSecurityUtil.getCurrentUserId();
        
        // 更改id的集合
        List<Long> ids = Lists.newArrayList();
        // 判断对象类型,是否是平台商
        if (companyStatusVO.getParentCompanyId() == Constants.ZERO) {
            // 获取他的所有客户单位
            List<CompanyBean> companyBeans = companyMapper.getCustomerById(companyStatusVO.getId());
            companyBeans.forEach((companyBean1) -> ids.add(companyBean1.getId()));
            ids.add(companyStatusVO.getId());
        } else {
            ids.add(companyStatusVO.getId());
        }
        // 判断传过来的状态，若果是开启状态，就关闭
        if (companyStatusVO.getStatus() == EnabledEnum.OPEN.getKey()) {
            return companyMapper.updateStatus(EnabledEnum.CLOSE.getKey(),createUser, ids);
            // 如果关闭就开启
        } else {
            //判断是否是客户单位
            if (companyStatusVO.getParentCompanyId() != Constants.ZERO) {
                //判断他的平台商是否关闭
                if(companyMapper.getParentStatus(companyStatusVO.getId()) == Constants.ZERO){
                    throw new RmcbsException("该客户单位所属平台商已关闭");
                }
            }
            return companyMapper.updateStatus(EnabledEnum.OPEN.getKey(),createUser, ids);
        }
    }
    
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteDeveloper(long id) {
        
        // 获取他的所有客户单位
        List<CompanyBean> companyBeans = companyMapper.getCustomerById(id);
        if(companyBeans != null && companyBeans.size() > Constants.ZERO){
            // 删除所有单位商务信息
            deleteBusiness(companyBeans);
            // 删除所有用户单位与栏目、接口、数据包的关系
            deleteResource(companyBeans);
            // 删除所有客户单位
            companyMapper.deleteCustomers(companyBeans);
        }

        // 删除平台商与接口关系
        List<Long> longs = Lists.newArrayList();
        longs.add(id);
        businessApiMapper.delete(longs);
        
        return companyMapper.delete(id);
    }
    
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteCustomer(long id) {
        
        CompanyBean companyBean = getCompanyById(id);
        // 制作companyBean的List
        List<CompanyBean> companyBeans = Lists.newArrayList();
        companyBeans.add(companyBean);
        // 删除单位商务信息
        deleteBusiness(companyBeans);
        // 删除用户单位与栏目、接口、数据包的关系
        deleteResource(companyBeans);
        
        return companyMapper.delete(id);
    }
    
    /**
     * 删除单位商务信息
     * 
     * @param companyBeans
     */
    private void deleteBusiness(List<CompanyBean> companyBeans) {
        
        // 先判断是否查出来
        if (companyBeans == null || companyBeans.size() == Constants.ZERO) {
            return;
        }
        // 删除单位商务信息表
        businessUnitsMapper.delete(companyBeans);
        // 删除Ip范围表
        List<BusinessUnitsBean> businessUnitsBeans = Lists.newArrayList();
        for (CompanyBean companyBean : companyBeans) {
            BusinessUnitsBean businessUnitsBean = new BusinessUnitsBean();
            businessUnitsBean.setBusinessId(companyBean.getId());
            businessUnitsBeans.add(businessUnitsBean);
        }
        businessUnitsMapper.deleteIp(businessUnitsBeans);
    }
    
    /**
     * 删除用户单位与栏目、接口、数据包的关系
     * 
     * @param companyBeans
     */
    private void deleteResource(List<CompanyBean> companyBeans) {
        
        // 先判断是否查出来
        if (companyBeans == null || companyBeans.size() == Constants.ZERO) {
            return;
        }
        List<Long> longs = Lists.newArrayList();
        // 制作ID的List集合
        companyBeans.forEach((companyBean) -> longs.add(companyBean.getId()));
        // 删除与接口的关系
        businessApiMapper.delete(longs);
        // 删除与栏目的关系
        businessColumnMapper.delete(longs);
        // 删除与数据包的关系
        businessPackageMapper.delete(longs);
    }
    
    @Override
    public PageVO<CompanyBean> getCompany(CompanyVO companyVO) {
        
        // 分页查询出单位
        PageHelper.startPage(companyVO.getCurrent(), companyVO.getPageSize());
        List<CompanyBean> companyList = companyMapper.getCompany(companyVO);
        // 通过PageHelper获取PageInfo返回对象
        PageInfo<CompanyBean> pageInfo = new PageInfo<CompanyBean>(companyList);
        return new PageVO<CompanyBean>(companyVO.getCurrent(), companyVO.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
    }
    
}
