package com.bfd.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.bfd.bean.ApiTypeBean;

/**
 * ApiTypeMapper
 * 
 * @author xile.lu
 * @version [版本号, 2018年8月8日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public interface ApiTypeMapper {
    
    /**
     * 根据类型ID查询类型信息
     * 
     * @param status 分类状态
     * @param id 类型ID
     * @return
     */
    ApiTypeBean getTypeById(@Param(value = "status") Integer status, @Param(value = "id") long id);
    
}
