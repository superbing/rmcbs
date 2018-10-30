package com.bfd.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfd.bean.CompanyBean;
import com.bfd.dao.mapper.CompanyMapper;
import com.bfd.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
    
    @Autowired
    CompanyMapper companyMapper;
    
    @Override
    public Map<String, Object> getById(Long id) {
        CompanyBean companyBean = companyMapper.getById(id);
        if (companyBean != null) {
            Map<String, Object> businessInfoMap = companyMapper.getBusinessInfo(id);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("companyName", companyBean.getCompanyName());
            result.put("companyCode", companyBean.getCompanyCode());
            result.put("accessKey", companyBean.getAccessKey());
            result.put("createTime", companyBean.getCreateTime());
            result.putAll(businessInfoMap);
            return result;
        } else {
            throw new RuntimeException("客户单位不存在！");
        }
        
    }
    
}
