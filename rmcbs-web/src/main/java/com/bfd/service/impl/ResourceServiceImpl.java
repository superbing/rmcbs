package com.bfd.service.impl;

import java.util.List;
import java.util.Map;

import com.bfd.dao.mapper.*;
import com.bfd.param.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bfd.bean.AuthBean;
import com.bfd.bean.BusinessApiBean;
import com.bfd.bean.ColumnBean;
import com.bfd.bean.CompanyBean;
import com.bfd.bean.DataPackageBean;
import com.bfd.common.vo.Constants;
import com.bfd.enums.CheckedEnum;
import com.bfd.enums.SetAshEnum;
import com.bfd.service.ColumnService;
import com.bfd.service.CompanyService;
import com.bfd.service.DataPackageService;
import com.bfd.service.ResourceService;
import com.bfd.config.RedisUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 11:41 2018/8/3
 * @Modified by:
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private final String preKey = "auth_";

    @Autowired
    private ApiInfoMapper apiInfoMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private BusinessApiMapper businessApiMapper;

    @Autowired
    private BusinessColumnMapper businessColumnMapper;

    @Autowired
    private BusinessPackageMapper businessPackageMapper;


    @Autowired
    DataPackageService dataPackageService;

    @Autowired
    ColumnService columnService;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ColumnMapper columnMapper;

    @Autowired
    private DataPackageMapper dataPackageMapper;

    @Override
    public List<ResourceRecordVO> getCustomerRecord(Long id) {
        return resourceMapper.getCustomerRecord(id);
    }

    @Override
    public List<ApiVo> getApiInfoList(ResourceApiQO resourceApiQO) {

        //获取该客户单位信息
        CompanyBean companyBean = companyService.getCompanyById(resourceApiQO.getId());

        //获取所有接口
        List<ApiVo> apiVos = apiInfoMapper.getApiList(resourceApiQO);

        //根据他的上级ID即平台商ID获取选择接口集合
        List<BusinessApiBean> developerApis = businessApiMapper.getById(companyBean.getParentCompanyId());

        //制作映射
        Map<Long,BusinessApiBean> developerMap = Maps.newHashMap();
        developerApis.forEach((developer)->developerMap.put(developer.getApiId(),developer));

        //获取该客户单位所选择的接口
        List<BusinessApiBean> customerApis = businessApiMapper.getById(resourceApiQO.getId());

        //制作映射
        Map<Long,BusinessApiBean> customerMap = Maps.newHashMap();
        customerApis.forEach((customer)->customerMap.put(customer.getApiId(),customer));

        //分别制作三个List，方便返回结果排序
        List<ApiVo> developer = Lists.newArrayList();
        List<ApiVo> customer = Lists.newArrayList();

        //没选择的接口集合
        List<ApiVo> noChecked = Lists.newArrayList();
        for (ApiVo apiVo:apiVos) {

            //是否是平台商选中的接口
            if(developerMap.containsKey(apiVo.getId())){

                //如果选中，置灰
                apiVo.setSetAsh(SetAshEnum.OPEN.getKey());

                //选中
                apiVo.setChecked(CheckedEnum.OPEN.getKey());
                developer.add(apiVo);

                //判断是否是客户单位选中的接口
            } else if(customerMap.containsKey(apiVo.getId())){

                //如果是则选中
                apiVo.setChecked(CheckedEnum.OPEN.getKey());
                apiVo.setSetAsh(SetAshEnum.CLOSE.getKey());
                customer.add(apiVo);

                //没选中，也没置灰
            }else {
                apiVo.setSetAsh(SetAshEnum.CLOSE.getKey());
                apiVo.setChecked(CheckedEnum.CLOSE.getKey());
                noChecked.add(apiVo);
            }
        }

        //制作返回结果集合
        List<ApiVo> result = Lists.newArrayList();
        result.addAll(developer);
        result.addAll(customer);
        result.addAll(noChecked);
        return result;
    }

    @Override
    public List<ApiVo> getApiById(Long id) {

        //根据ID即平台商ID获取选择接口集合
        List<ApiVo> apiBeans = apiInfoMapper.getApiById(id);
        return apiBeans;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int AddApi(long businessId, List<Long> ids) {

        //判断是否没选择过，不为null则先删除
        if(businessApiMapper.getById(businessId) != null){

            //先变为集合
            List<Long> longs = Lists.newArrayList();
            longs.add(businessId);
            businessApiMapper.delete(longs);
        }

        //添加用户选择过得ID
        return businessApiMapper.insert(businessId,ids);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int AddColumn(long businessId, List<Long> ids) {

        //判断是否没选择过，不为null则先删除
        if(businessColumnMapper.getById(businessId) != null){

            //先变为集合
            List<Long> longs = Lists.newArrayList();
            longs.add(businessId);
            businessColumnMapper.delete(longs);
        }

        //添加用户选择过得ID
        return businessColumnMapper.insert(businessId,ids);
    }

    @Override
    public List<ColumnVO> getColumnList(Long businessId) {

        //设置返回结果
        List<ColumnVO> columnVOS = Lists.newArrayList();

        //获取栏目分级树结构
        List<ColumnBean> list = columnService.getColumnList();

        //获取选择栏目的ID集合
        List<Long> columnIds = businessColumnMapper.getById(businessId);

        //制作映射
        Map<Long,Long> columnIdMap = Maps.newHashMap();
        columnIds.forEach((id)->columnIdMap.put(id,id));

        //获取一级栏目ID集合
        List<Long> columnOneLevel = columnMapper.getColumnOneLevel();

        //转换对象，并设置是否标注
        for (ColumnBean columnBean:list) {
            ColumnVO columnVO = new ColumnVO();
            columnVO.setId(columnBean.getId());
            columnVO.setName(columnBean.getName());
            columnVO.setAliasName(columnBean.getAliasName());
            columnVO.setParentId(columnBean.getParentId());
            columnVO.setSort(columnBean.getSort());
            columnVO.setType(columnBean.getType());
            columnVO.setBookNumber(columnBean.getBookNumber());

            //一级栏目不标注
            if(!columnOneLevel.contains(columnBean.getId())){
                if(columnIdMap.containsKey(columnBean.getId())){
                    columnVO.setChecked(CheckedEnum.OPEN.getKey());
                }else {
                    columnVO.setChecked(CheckedEnum.CLOSE.getKey());
                }
            }else{
                columnVO.setChecked(CheckedEnum.CLOSE.getKey());
            }
            columnVOS.add(columnVO);
        }

        return columnVOS;
    }

    @Override
    public List<ColumnVO> showColumnList(Long businessId) {

        //设置返回结果
        List<ColumnVO> columnVOS = Lists.newArrayList();

        //获取栏目分级树结构
        List<ColumnBean> list = columnService.getColumnList();

        //获取选择栏目的ID集合
        List<Long> columnIds = businessColumnMapper.getById(businessId);

        //制作映射
        Map<Long,Long> columnIdMap = Maps.newHashMap();
        columnIds.forEach((id)->columnIdMap.put(id,id));

        //设置选择栏目的父ID集合
        List<Long> checkParentIds = Lists.newArrayList();

        //转换对象，并设置是否标注
        for (ColumnBean columnBean:list) {

            //只有它选择的才给他展示
            if(columnIdMap.containsKey(columnBean.getId())){
                ColumnVO columnVO = new ColumnVO();
                columnVO.setId(columnBean.getId());
                columnVO.setName(columnBean.getName());
                columnVO.setAliasName(columnBean.getAliasName());
                columnVO.setParentId(columnBean.getParentId());
                columnVO.setType(columnBean.getType());

                //将其填入父ID集合
                checkParentIds.add(columnBean.getParentId());
                columnVO.setSort(columnBean.getSort());
                columnVO.setChecked(CheckedEnum.OPEN.getKey());
                columnVO.setBookNumber(columnBean.getBookNumber());
                columnVOS.add(columnVO);
            }
        }
        return columnVOS;
    }

    /**
     * 获取所选择栏目的父集合
     * @param list 所有栏目集合
     * @param checkParentIds 选择的父类集合
     * @return
     */
    private List<ColumnVO> getColumnParentList(List<ColumnBean> list,List<Long> checkParentIds){

        //制造返回对象
        List<ColumnVO> columnVOS = Lists.newArrayList();
        //制造父类ID集合
        List<Long> parents = Lists.newArrayList();

        //遍历父类ID的集合
        for(long id:checkParentIds){
            getColumnParents(list,parents,id);
        }
        //将所有栏目集合整成一个Map
        Map<Long,ColumnBean> columnsMap = Maps.newHashMap();
        list.forEach((columnBean) -> columnsMap.put(columnBean.getId(),columnBean));
        for (Long id:parents) {
                ColumnBean columnBean = columnsMap.get(id);
                ColumnVO columnVO = new ColumnVO();
                columnVO.setId(columnBean.getId());
                columnVO.setName(columnBean.getName());
                columnVO.setAliasName(columnBean.getAliasName());
                columnVO.setParentId(columnBean.getParentId());
                columnVO.setSort(columnBean.getSort());
                columnVOS.add(columnVO);
                columnVO.setBookNumber(columnBean.getBookNumber());
        }
        return columnVOS;
    }

    /**
     * 递归获取父ID的集合
     * @param list  所有栏目集
     * @param parents 父ID集合
     * @param parentId 父ID
     * @return
     */
    private void getColumnParents(List<ColumnBean> list,List<Long> parents,long parentId){

        for(ColumnBean columnBean:list){
            //如果栏目ID，是父ID,并且不再父ID集合中（去重）
            if (columnBean.getId() == parentId && !parents.contains(columnBean.getId())){
                //将该ID放入ID集合中
                parents.add(columnBean.getId());
                //再判断该ID是否是顶级栏目（顶级栏目为0），如果不是继续递归
                if(columnBean.getParentId() != Constants.ZERO){
                    getColumnParents(list,parents,columnBean.getId());
                }
            }
        }
    }



    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int AddPackage(long businessId, List<Long> ids) {
        //判断是否没选择过，不为null则先删除
        if(businessPackageMapper.getById(businessId) != null){
            //先变为集合
            List<Long> longs = Lists.newArrayList();
            longs.add(businessId);
            businessPackageMapper.delete(longs);
        }
        //添加用户选择过得ID
        return businessPackageMapper.insert(businessId,ids);
    }

    @Override
    public List<DataPackageVO> showPackageList(Long businessId) {
        //设置返回结果
        List<DataPackageVO> dataPackageVOS = Lists.newArrayList();
        //获取栏目分级树结构
        List<DataPackageBean> dataPackageBeans = dataPackageService.getPackageList();
        //获取选择数据包的ID集合
        List<Long> dataPackageIds = businessPackageMapper.getById(businessId);
        //制作映射
        Map<Long,Long> dataPackageIdMap = Maps.newHashMap();
        dataPackageIds.forEach((id)->dataPackageIdMap.put(id,id));
        //设置选择栏目的父ID集合
        List<Long> checkParentIds = Lists.newArrayList();
        //转换对象，并设置是否标注
        for (DataPackageBean dataPackageBean:dataPackageBeans) {
            //只有选择的才展示
            if(dataPackageIdMap.containsKey(dataPackageBean.getId())){
                DataPackageVO dataPackageVO = new DataPackageVO();
                dataPackageVO.setId(dataPackageBean.getId());
                dataPackageVO.setName(dataPackageBean.getName());
                dataPackageVO.setAliasName(dataPackageBean.getAliasName());
                dataPackageVO.setParentId(dataPackageBean.getParentId());
                dataPackageVO.setType(dataPackageBean.getType());
                //将其填入父ID集合
                checkParentIds.add(dataPackageBean.getParentId());
                dataPackageVO.setSort(dataPackageBean.getSort());
                dataPackageVO.setChecked(CheckedEnum.OPEN.getKey());
                dataPackageVO.setBookNumber(dataPackageBean.getBookNumber());
                dataPackageVOS.add(dataPackageVO);
            }
        }
        return dataPackageVOS;
    }


    @Override
    public List<DataPackageVO> getPackageList(Long businessId) {

        //设置返回结果
        List<DataPackageVO> dataPackageVOS = Lists.newArrayList();

        //获取栏目分级树结构
        List<DataPackageBean> dataPackageBeans = dataPackageService.getPackageList();

        //获取选择数据包的ID集合
        List<Long> dataPackageIds = businessPackageMapper.getById(businessId);

        //制作映射
        Map<Long,Long> dataPackageIdMap = Maps.newHashMap();
        dataPackageIds.forEach((id)->dataPackageIdMap.put(id,id));

        //获取所有一级数据包
        List<Long> packageOneLevel = dataPackageMapper.getPackageOneLevel();

        //转换对象，并设置是否标注
        for (DataPackageBean dataPackageBean:dataPackageBeans) {
            DataPackageVO dataPackageVO = new DataPackageVO();
            dataPackageVO.setId(dataPackageBean.getId());
            dataPackageVO.setName(dataPackageBean.getName());
            dataPackageVO.setAliasName(dataPackageBean.getAliasName());
            dataPackageVO.setParentId(dataPackageBean.getParentId());
            dataPackageVO.setSort(dataPackageBean.getSort());
            dataPackageVO.setType(dataPackageBean.getType());
            //一级数据包不标注
            if(!packageOneLevel.contains(dataPackageBean.getId())){
                if(dataPackageIdMap.containsKey(dataPackageBean.getId())){
                    dataPackageVO.setChecked(CheckedEnum.OPEN.getKey());
                }else {
                    dataPackageVO.setChecked(CheckedEnum.CLOSE.getKey());
                }
            }else{
                dataPackageVO.setChecked(CheckedEnum.CLOSE.getKey());
            }
            dataPackageVOS.add(dataPackageVO);
            dataPackageVO.setBookNumber(dataPackageBean.getBookNumber());
        }
        return dataPackageVOS;
    }

    /**
     * 获取所选择数据包的父集合
     * @param list 所有数据包集合
     * @param checkParentIds 选择的父类集合
     * @return
     */
    private List<DataPackageVO> getPackageParentList(List<DataPackageBean> list,List<Long> checkParentIds){

        //制造返回对象
        List<DataPackageVO> dataPackageVOS = Lists.newArrayList();
        //制造父类ID集合
        List<Long> parents = Lists.newArrayList();

        //遍历父类ID的集合
        for(long id:checkParentIds){
            getPackageParents(list,parents,id);
        }
        //制作所有数据包的Map集合
        Map<Long,DataPackageBean> packagesMap = Maps.newHashMap();
        list.forEach(dataPackageBean -> packagesMap.put(dataPackageBean.getId(),dataPackageBean));
        for (Long id:parents) {
                DataPackageBean dataPackageBean = packagesMap.get(id);
                DataPackageVO dataPackageVO = new DataPackageVO();
                dataPackageVO.setId(dataPackageBean.getId());
                dataPackageVO.setName(dataPackageBean.getName());
                dataPackageVO.setAliasName(dataPackageBean.getAliasName());
                dataPackageVO.setParentId(dataPackageBean.getParentId());
                dataPackageVO.setSort(dataPackageBean.getSort());
                dataPackageVOS.add(dataPackageVO);
                dataPackageVO.setBookNumber(dataPackageBean.getBookNumber());
        }

        return dataPackageVOS;
    }

    /**
     * 递归获取父ID的集合
     * @param list  所有数据包集
     * @param parents 父ID集合
     * @param parentId 父ID
     * @return
     */
    private void getPackageParents(List<DataPackageBean> list,List<Long> parents,long parentId){

        for(DataPackageBean dataPackageBean:list){
            //如果栏目ID，是父ID,并且不再父ID集合中（去重）
            if (dataPackageBean.getId() == parentId && !parents.contains(dataPackageBean.getId())){
                //将该ID放入ID集合中
                parents.add(dataPackageBean.getId());
                //再判断该ID是否是顶级栏目（顶级栏目为0），如果不是继续递归
                if(dataPackageBean.getParentId() != Constants.ZERO){
                    getPackageParents(list,parents,dataPackageBean.getId());
                }
            }
        }
    }

    @Override
    public boolean deleteOne(Long id, Long apiId) {
        return businessApiMapper.deleteOne(id,apiId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addResource(ResourceVO resourceVO) {

        //添加接口
        //判断是否没选择过，不为null则先删除
        List<BusinessApiBean> list = businessApiMapper.getById(resourceVO.getId());

        if(list != null && resourceVO.getApiIds() != null){
            //先变为集合
            List<Long> longs = Lists.newArrayList();
            longs.add(resourceVO.getId());
            int i = businessApiMapper.delete(longs);

            //添加用户选择过得ID
            businessApiMapper.insert(resourceVO.getId(),resourceVO.getApiIds());
        }


        //添加栏目
        //判断是否没选择过，不为null则先删除
        if(businessColumnMapper.getById(resourceVO.getId()) != null && resourceVO.getColumnIds() != null){
            //先变为集合
            List<Long> longs1 = Lists.newArrayList();
            longs1.add(resourceVO.getId());
            businessColumnMapper.delete(longs1);

            //添加用户选择过得ID
            businessColumnMapper.insert(resourceVO.getId(),resourceVO.getColumnIds());
        }

        //添加数据包
        //判断是否没选择过，不为null则先删除
        if(businessPackageMapper.getById(resourceVO.getId()) != null && resourceVO.getPackageIds() != null){
            //先变为集合
            List<Long> longs2 = Lists.newArrayList();
            longs2.add(resourceVO.getId());
            businessPackageMapper.delete(longs2);

            //添加用户选择过得ID
            businessPackageMapper.insert(resourceVO.getId(),resourceVO.getPackageIds());
        }

        //add by bing.shen 更新权限缓存
        this.cacheAuth(resourceVO.getId());
    }

    @Override
    public void cacheAuth(Long businessId){
        List<AuthBean> list = businessApiMapper.queryAuth(businessId);
        if(!CollectionUtils.isEmpty(list)){
            JSONObject jsonObject = new JSONObject();
            if(!CollectionUtils.isEmpty(list)){
                for(AuthBean authBean : list){
                    jsonObject.put(authBean.getUrl(), authBean);
                }
            }
            RedisUtil.set(preKey.concat(businessId.toString()), jsonObject.toJSONString());
        }
    }
}
