package com.bfd.service;

import com.bfd.param.vo.TimeRangeVO;
import com.bfd.param.vo.edgesighvo.home.InformationCountDTO;
import com.bfd.param.vo.edgesighvo.home.InvokeTotalDTO;
import com.bfd.param.vo.edgesighvo.home.TotalDistributeDTO;
import com.bfd.param.vo.edgesighvo.home.TotalDistributeVO;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 11:38 2018/9/7
 * @Modified by:
 */
public interface HomeService {

    /**
     * 获取图书,栏目等的统计
     * @return
     */
    InformationCountDTO getAllInformation();


    /**
     * 获取时间范围的统计
     * @param timeRangeVO
     * @return
     */
    InvokeTotalDTO getTimeRangeTotal(TimeRangeVO timeRangeVO);


    /**
     * 获取时间范围一天各时间调用统计
     * @param timeRangeVO
     * @return
     */
    TotalDistributeDTO getGraphicalTotal(TimeRangeVO timeRangeVO);



}
