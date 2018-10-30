package com.bfd.service.impl;

import com.bfd.bean.ApiInfoBean;
import com.bfd.bean.ApiTypeBean;
import com.bfd.bean.BusinessApiBean;
import com.bfd.common.vo.Constants;
import com.bfd.dao.mapper.ApiInfoMapper;
import com.bfd.dao.mapper.ApiTypeMapper;
import com.bfd.dao.mapper.BusinessApiMapper;
import com.bfd.enums.EnabledEnum;
import com.bfd.param.vo.ApiTreeVO;
import com.bfd.service.ApiService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Value("${api.url}")
    private String address;


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

}
