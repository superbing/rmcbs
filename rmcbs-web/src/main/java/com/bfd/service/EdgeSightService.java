package com.bfd.service;

import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.edgesighvo.*;
import com.bfd.param.vo.serviceaudit.ServiceAuditQO;
import com.bfd.param.vo.serviceaudit.ServiceAuditVO;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 16:57 2018/8/27
 * @Modified by:
 */
public interface EdgeSightService {

    /**
     * 获取接口监控列表
     * @param apiSightQO
     * @return
     */
    PageVO<ApiSightVO> getApiSight(ApiSightQO apiSightQO);

    /**
     * 获取接口-平台商监控列表
     * @param companySightQO
     * @return
     */
    CompanySightDTO getCompanySight(CompanySightQO companySightQO);

    /**
     * 获取接口-客户单位监控列表
     * @param businessSightQO
     * @return
     */
    BusinessSightDTO getBusinessSight(BusinessSightQO businessSightQO);

    /**
     * 获取客户单位接口的统计
     * @param businessSightQO
     * @return
     */
    DayCountSightDTO getDayCount(DayCountSightQO businessSightQO);

    /**
     * 获取当天接口的统计
     * @param dayCountSightQO
     * @return Service audit
     */
    DayCountSightDTO getDayApiCount(DayCountSightQO dayCountSightQO);

    /**
     * 获取服务审计列表
     * @param serviceAuditQO
     * @return
     */
    PageVO<ServiceAuditVO> getServiceAudit(ServiceAuditQO serviceAuditQO);
}
