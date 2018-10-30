package com.bfd.dao.mapper;

import java.util.Map;

import com.bfd.bean.CompanyBean;

public interface CompanyMapper {
    
    /**
     * 根据id查询客户单位
     * 
     * @param id
     * @return
     * @see [类、类#方法、类#成员]
     */
    CompanyBean getById(Long id);
    
    /**
     * 根据id查询客户单位商务信息
     * 
     * @param id
     * @return
     * @see [类、类#方法、类#成员]
     */
    Map<String, Object> getBusinessInfo(Long id);
    
}
