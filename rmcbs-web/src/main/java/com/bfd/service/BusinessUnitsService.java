package com.bfd.service;

import com.bfd.bean.es.DevinfoBean;
import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.BusinessUnitsVO;
import com.bfd.param.vo.CompanyVO;
import org.apache.ibatis.annotations.Param;


/**
 * @Author: chong.chen
 * @Description: 单位商务信息 service
 * @Date: Created in 14:59 2018/8/2
 * @Modified by:
 */
public interface BusinessUnitsService {


    /**
     * 更新单位商务信息
     * @param businessUnitsVO
     * @return
     */
    void update(BusinessUnitsVO businessUnitsVO);

    /**
     * 根据条件查询平台商列表
     * @param companyVO
     * @return
     */
    PageVO<BusinessUnitsVO> getCustomersList(CompanyVO companyVO);

    /**
     * 根据单位商务ID获取单位商务信息详细信息。
     * @param id
     * @return
     */
    BusinessUnitsVO getBusinessUnits(@Param(value = "id") long id);

    /**
     * 获取设备信息列表
     * @param companyVO
     * @return
     */
    PageVO<DevinfoBean> getTerminalPage(CompanyVO companyVO);

    /**
     * 根据ID清空设备数
     * @param businessId
     */
    void deleteEntryTerminal(long businessId);

}
