package com.bfd.service;

import com.bfd.bean.CompanyBean;
import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.TimeRangeVO;
import com.bfd.param.vo.edgesighvo.home.InvokeTotalDTO;
import com.bfd.param.vo.edgesighvo.home.TotalDistributeDTO;
import com.bfd.param.vo.statisticsuser.CustomerQueryVO;
import com.bfd.param.vo.statisticsuser.DeveloperQueryVO;
import com.bfd.param.vo.statisticsuser.UserQueryQO;

import java.util.List;

/**
 * 统计查询--用户使用查询service服务层
 *
 * @author jiang.liu
 * @date 2018-09-14
 */
public interface StatisticsUserService {


    /**
     * 根据平台商ID获取旗下客户单位名称
     * @param id
     * @return
     */
    List<CompanyBean> getCustomersName(long id);

    /**
     * 获取平台旗下策略统计分页
     * @param userQueryQO
     * @return
     */
    PageVO<DeveloperQueryVO> getDeveloperQueryPage(UserQueryQO userQueryQO);

    /**
     * 获取客户单位的策略统计分页
     * @param userQueryQO
     * @return
     */
    PageVO<CustomerQueryVO> getCustomerQueryPage(UserQueryQO userQueryQO);

    /**
     * 获取平台商or客户单位时间范围的统计
     * @param timeRangeVO
     * @return
     */
    InvokeTotalDTO getTimeRangeTotal(TimeRangeVO timeRangeVO);

    /**
     * 获取平台商or客户单位时间范围一天各时间调用统计
     * @param timeRangeVO
     * @return
     */
    TotalDistributeDTO getGraphicalTotal(TimeRangeVO timeRangeVO);

}
