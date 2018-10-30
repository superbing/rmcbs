package com.bfd.dao.mapper;

import com.bfd.bean.BusinessApiBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description: 客户接口mapper
 * @Date: Created in 9:54 2018/8/6
 * @Modified by:
 */
@Repository
public interface BusinessApiMapper {


    /**
     * 根据客户ID获取ID集合
     * @param businessId
     * @return
     */
    List<BusinessApiBean> getById(@Param(value = "businessId") Long businessId);
}
