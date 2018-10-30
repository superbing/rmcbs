package com.bfd.dao.mapper;

import com.bfd.param.vo.TimeRangeVO;
import com.bfd.param.vo.edgesighvo.*;
import com.bfd.param.vo.edgesighvo.home.InvokeTotalDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 14:54 2018/8/27
 * @Modified by:
 */
@Repository
public interface LogStatisticsMapper {

    /**
     * 获取接口监控列表
     * @param apiSightQO
     * @return
     */
    List<ApiSightVO> getApiSight(ApiSightQO apiSightQO);

    /**
     * 获取接口-平台商监控列表
     * @param companySightQO
     * @return
     */
    List<CompanySightVO> getCompanySight(CompanySightQO companySightQO);

    /**
     * 获取接口-客户单位监控列表
     * @param businessSightQO
     * @return
     */
    List<BusinessSightVO> getBusinessSight(BusinessSightQO businessSightQO);

    /**
     * 获取近几天访问统计
     * @param timeRangeVO 时间范围
     * @return
     */
    InvokeTotalDTO getTheOtherDayTotal(TimeRangeVO timeRangeVO);

    /**
     * 获取平台商近几天访问统计
     * @param timeRangeVO 时间范围
     * @return
     */
    InvokeTotalDTO getTheseDaysCompanyTotal(TimeRangeVO timeRangeVO);

    /**
     * 获取客户单位近几天访问统计
     * @param timeRangeVO 时间范围
     * @return
     */
    InvokeTotalDTO getTimeRangeBusinessTotal(TimeRangeVO timeRangeVO);

}
