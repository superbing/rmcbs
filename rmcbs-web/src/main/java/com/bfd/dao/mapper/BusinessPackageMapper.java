package com.bfd.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description: 客户数据包 mapper
 * @Date: Created in 9:54 2018/8/6
 * @Modified by:
 */
@Repository
public interface BusinessPackageMapper {

    /**
     * 新增
     * @param businessId
     * @param ids
     * @return
     */
    int insert(@Param(value = "businessId")Long businessId, @Param(value = "ids") List<Long> ids);

    /**
     * 根据客户ID批量删除
     * @param businessList
     * @return
     */
    int delete(@Param(value = "ids") List<Long> businessList);

    /**
     * 根据客户ID获取ID集合
     * @param businessId
     * @return
     */
    List<Long> getById(@Param(value = "businessId")Long businessId);
}
