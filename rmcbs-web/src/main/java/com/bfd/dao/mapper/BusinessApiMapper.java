package com.bfd.dao.mapper;

import com.bfd.bean.AuthBean;
import com.bfd.bean.BusinessApiBean;
import com.bfd.param.vo.InsertApiVO;
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
     * 新增
     * @param businessId
     * @param ids
     * @return
     */
    int insert(@Param(value = "businessId")Long businessId, @Param(value = "ids") List<Long> ids);

    /**
     * 批量新增客户单位和平台商选择的接口
     * @param insertApiVOS
     * @return
     */
    int insertChildren(@Param(value = "insertApiVOS") List<InsertApiVO> insertApiVOS);

    /**
     * 根据客户ID批量删除
     * @param businessList
     * @return
     */
    int delete(@Param(value = "ids") List<Long> businessList);


    /**
     * 删除平台商给客户单位选择的接口ID
     * @param businessIds
     * @param apiIds
     * @return
     */
    int deleteChildren(@Param(value = "businessIds") List<Long> businessIds,@Param(value = "apiIds") List<Long> apiIds);

    /**
     * 根据平台商ID和接口ID单独删除关系
     * @param id
     * @param apiId
     * @return
     */
    boolean deleteOne(@Param(value = "id") Long id, @Param(value = "apiId") Long apiId);

    /**
     * 根据客户单位ID和接口ID集合删除关系
     * @param businessId
     * @param apiIds
     * @return
     */
    boolean deleteBusinessApi(@Param(value = "businessId") Long businessId, @Param(value = "apiIds") List<Long> apiIds);

    /**
     * 根据客户ID获取ID集合
     * @param businessId
     * @return
     */
    List<BusinessApiBean> getById(@Param(value = "businessId")Long businessId);

    /**
     * 根据客户ID查询权限列表
     * @param businessId
     * @return
     */
    List<AuthBean> queryAuth(@Param(value = "businessId")Long businessId);


    /**
     * 根据Apiid获取客户单位集合
     * @param apiId
     * @return
     */
    List<Long> getByApiId(@Param(value = "apiId")Long apiId);
}
