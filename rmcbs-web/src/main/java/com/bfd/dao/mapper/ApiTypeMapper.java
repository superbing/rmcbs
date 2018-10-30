package com.bfd.dao.mapper;

import java.util.List;

import com.bfd.bean.ApiTypeBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
     * 查询有效的API类型
     * 
     * @param status
     * @return
     * @see [类、类#方法、类#成员]
     */
    List<ApiTypeBean> queryApiType(Integer status);

    /**
     * 获取所有的接口类型
     * @return
     */
    List<ApiTypeBean> getAllApiType();

    /**
     * 根据类型ID查询类型信息
     * @param status 分类状态
     * @param id 类型ID
     * @return
     */
    ApiTypeBean getTypeById(@Param(value = "status") Integer status, @Param(value = "id") long id);

}
