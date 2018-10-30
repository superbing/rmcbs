package com.bfd.dao.mapper;

import com.bfd.bean.BusinessUnitsBean;
import com.bfd.bean.CompanyBean;
import com.bfd.bean.IpRangeBean;
import com.bfd.param.vo.BusinessUnitsVO;
import com.bfd.param.vo.CompanyVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description: 单位商务信息 mapper
 * @Date: Created in 10:34 2018/8/2
 * @Modified by:
 */
@Repository
public interface BusinessUnitsMapper {

    /**
     * 新增单位商务信息
     * @param businessUnitsBean
     * @return
     */
    int insert(BusinessUnitsBean businessUnitsBean);

    /**
     * 批量新增IP范围
     * @param ipRangeBeans
     * @return
     */
    int insertIp(@Param(value = "list") List<IpRangeBean> ipRangeBeans);

    /**
     * 更新单位商务信息
     * @param businessUnitsBean
     * @return
     */
    int update(BusinessUnitsBean businessUnitsBean);

    /**
     * 更新IP范围
     * @param ipRangeBean
     * @return
     */
    int updateIp(IpRangeBean ipRangeBean);

    /**
     * 批量删除单位商务信息(此方法是删除平台商或客户单位时使用)
     * @param CompanyBean
     * @return
     */
    boolean delete(@Param(value = "ids") List<CompanyBean> CompanyBean);

    /**
     * 批量删除IP范围
     * @param businessUnitsBeans
     * @return
     */
    boolean deleteIp(@Param(value = "ids") List<BusinessUnitsBean> businessUnitsBeans);

    /**
     * 根据条件查询商务信息列表
     * @param companyVO
     * @return
     */
    List<BusinessUnitsVO> getCustomersList(CompanyVO companyVO);

    /**
     * 根据ID查询单条商务信息
     * @param id
     * @return
     */
    BusinessUnitsVO getBusiness(@Param(value = "id") long id);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    BusinessUnitsBean getBusinessById (@Param(value = "id") long id);

    /**
     * 根据单位商务信息ID获取IP范围
     * @param id
     * @return
     */
    List<IpRangeBean> getIpRangeList(Long id);



}
