package com.bfd.service.impl;

import java.util.List;
import java.util.Map;

import com.bfd.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bfd.bean.ApiInfoBean;
import com.bfd.bean.ApiTypeBean;
import com.bfd.bean.BusinessApiBean;
import com.bfd.common.exception.RmcbsException;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.ApiInfoMapper;
import com.bfd.dao.mapper.ApiTypeMapper;
import com.bfd.dao.mapper.BusinessApiMapper;
import com.bfd.enums.CheckedEnum;
import com.bfd.enums.EnabledEnum;
import com.bfd.enums.SetAshEnum;
import com.bfd.param.vo.ApiInfoVO;
import com.bfd.param.vo.ApiTreeVO;
import com.bfd.param.vo.ApiVo;
import com.bfd.service.ApiService;
import com.bfd.utils.SpringSecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import redis.clients.jedis.Jedis;

/**
 * @author: bing.shen
 * @date: 2018/8/2 16:09
 * @Description:
 */
@Service
public class ApiServiceImpl implements ApiService {
    
    @Autowired
    private ApiInfoMapper apiInfoMapper;
    
    @Autowired
    ApiTypeMapper apiTypeMapper;

    @Autowired
    BusinessApiMapper businessApiMapper;

    @Autowired
    ResourceService resourceService;

    @Value("${api.url}")
    private String address;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addApi(ApiInfoBean apiInfoBean) {
         Long userId = SpringSecurityUtil.getCurrentUserId();
         apiInfoBean.setCreateUser(userId);
         ApiInfoBean infoBean = apiInfoMapper.findByName(apiInfoBean.getName());
         if (infoBean != null){
             throw new RmcbsException("接口名称与已存在,请修改接口名称");
         }
        ApiInfoBean apiUrl = apiInfoMapper.findByUrl(apiInfoBean.getUrl());
        if (apiUrl != null){
            throw new RmcbsException("接口地址与已存在,请修改接口地址");
        }
         apiInfoMapper.insert(apiInfoBean);
         return true;
    }

    @Override
    public List<ApiTreeVO> getApiTree(long id) {

        //根据客户单位ID获取他所选择的接口
        List<BusinessApiBean> businessApiBeans = businessApiMapper.getById(id);
        //创建返回结果
        List<ApiTreeVO> apiTreeVOS = Lists.newArrayList();
        //该客户单位选择了接口
        if(businessApiBeans != null && businessApiBeans.size() > Constants.ZERO){
            //制作id集合
            List<Long> apiIds = Lists.newArrayList();
            businessApiBeans.forEach(businessApiBean->apiIds.add(businessApiBean.getApiId()));
            //返回结果
            apiTreeVOS = apiInfoMapper.getApiTree(apiIds);
            //客户单位没选择接口
        }else{
            List<Long> apiIds = null;
            //返回结果
            apiTreeVOS = apiInfoMapper.getApiTree(apiIds);
            //将接口类型下的ID都去掉
            apiTreeVOS.forEach(apiTreeVO -> apiTreeVO.setApiVOList(null));
        }

        return apiTreeVOS;
    }

    @Override
    public ApiInfoBean getApiInfo(Long id) {
        ApiInfoBean apiInfoBean = apiInfoMapper.findById(id);
        if (apiInfoBean == null){
            return new ApiInfoBean();
        }
        return apiInfoBean;
    }

    @Override
    public ApiInfoBean getApiDocument(Long id) {
        ApiInfoBean apiInfoBean = apiInfoMapper.findById(id);
        if (apiInfoBean == null){
            return new ApiInfoBean();
        }
        ApiTypeBean apiTypeBean = apiTypeMapper.getTypeById(EnabledEnum.OPEN.getKey(),apiInfoBean.getApiType());
        String uri = apiInfoBean.getUrl();
        apiInfoBean.setUrl(address+apiTypeBean.getUrl()+uri);
        return apiInfoBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApi(ApiInfoBean apiInfoBean) {
        ApiInfoBean infoBean = apiInfoMapper.findByName(apiInfoBean.getName());
        if (infoBean != null && !infoBean.getId().equals(apiInfoBean.getId())){
            throw new RmcbsException("接口名称与已存在,请修改接口名称");
        }
        ApiInfoBean apiUrl = apiInfoMapper.findByUrl(apiInfoBean.getUrl());
        if (apiUrl != null && !apiUrl.getId().equals(apiInfoBean.getId())){
            throw new RmcbsException("接口地址与已存在,请修改接口地址");
        }
        apiInfoMapper.update(apiInfoBean);
        return true;
    }

    @Override
    public boolean updateStatus(long id, int status) {
        boolean result = apiInfoMapper.updateStatus(id,status);
        //查询出所有用这个接口的客户单位集合
        List<Long> businessIds = businessApiMapper.getByApiId(id);
        //将授权信息重新存一遍
        for(Long businessId : businessIds){
            resourceService.cacheAuth(businessId);
        }
        return result;
    }

    @Override
    public PageVO<ApiInfoBean> apiInfoPage(ApiInfoVO apiInfoVO) {
        PageHelper.startPage(apiInfoVO.getCurrent(), apiInfoVO.getPageSize());
        List<ApiInfoBean> list = apiInfoMapper.queryList(apiInfoVO);
        PageInfo<ApiInfoBean> pageInfo = new PageInfo<>(list);
        return new PageVO<>(apiInfoVO.getCurrent(), apiInfoVO.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
    }


    @Override
    public PageVO<ApiVo> getApiList(ApiInfoVO apiInfoVO) {
        PageHelper.startPage(apiInfoVO.getCurrent(), apiInfoVO.getPageSize());
        List<ApiInfoBean> list = apiInfoMapper.queryList(apiInfoVO);
        //返回结果集合
        List<ApiVo>  apiVoList = Lists.newArrayList();
        //根据平台商ID查询出接口 信息
        List<BusinessApiBean> developerApis = businessApiMapper.getById(apiInfoVO.getCompanyId());
        //制作映射
        Map<Long,BusinessApiBean> developerMap = Maps.newHashMap();
        developerApis.forEach((developer)->developerMap.put(developer.getApiId(),developer));
        for (ApiInfoBean apiInfoBean:list) {
            ApiVo apiVo = new ApiVo();
            apiVo.setId(apiInfoBean.getId());
            apiVo.setName(apiInfoBean.getName());
            apiVo.setUrl(apiInfoBean.getUrl());
            apiVo.setApiType(apiInfoBean.getApiType());
            apiVo.setDescription(apiInfoBean.getDescription());
            apiVo.setAuth(apiInfoBean.getAuth());
            apiVo.setStatus(apiInfoBean.getStatus());
            //判断接口是否被选中
            if(developerMap.containsKey(apiInfoBean.getId())){
                //如果被选中
                apiVo.setChecked(CheckedEnum.OPEN.getKey());
                //如果没被选中
            }else {
                apiVo.setChecked(CheckedEnum.CLOSE.getKey());
            }
            apiVo.setSetAsh(SetAshEnum.CLOSE.getKey());
            apiVoList.add(apiVo);
        }
        PageInfo<ApiInfoBean> pageInfo = new PageInfo<>(list);
        return new PageVO<>(apiInfoVO.getCurrent(), apiInfoVO.getPageSize(), pageInfo.getTotal(), apiVoList);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteApi(Long id) {
        apiInfoMapper.delete(id);
        return true;
    }
    
    @Override
    public List<ApiTypeBean> queryApiType(Integer status) {
        return apiTypeMapper.queryApiType(status);
    }

    @Override
    public List<ApiInfoBean> queryApi() {
        return apiInfoMapper.queryAllApi();
    }

    @Override
    public List<ApiInfoBean> getApiByType(long type) {
        return apiInfoMapper.getApiByType(type);
    }
}
