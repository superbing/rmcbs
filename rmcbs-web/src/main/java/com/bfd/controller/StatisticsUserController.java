package com.bfd.controller;

import com.bfd.bean.CompanyBean;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.common.vo.Result;
import com.bfd.param.vo.ApiInfoVO;
import com.bfd.param.vo.CompanyVO;
import com.bfd.param.vo.statisticsuser.UserQueryQO;
import com.bfd.service.*;
import com.bfd.param.vo.TimeRangeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 统计查询--用户使用查询
 *
 * @author jiang.liu
 * @date 2018-9-14
 */
@RestController
@RequestMapping("/statisticsUser")
@Api(value = "/statisticsUser", description = "统计查询--用户使用查询")
public class StatisticsUserController {

    @Autowired
    StatisticsUserService statisticsUserService;

    @Autowired
    CompanyService companyService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private BusinessUnitsService businessUnitsService;

    @Autowired
    ApiService apiService;

    @ApiOperation(value = "根据ID获取客户单位选择的所有接口")
    @RequestMapping(value = "getCustomersName{id}" ,method = RequestMethod.GET)
    public Object getCustomersName(@ApiParam("平台商ID") @PathVariable("id") long id){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),statisticsUserService.getCustomersName(id));
    }

    @ApiOperation(value = "根据平台商ID获取客户单位名称列表")
    @RequestMapping(value = "getApiById{id}" ,method = RequestMethod.GET)
    public Object getApiById(@ApiParam("平台商ID") @PathVariable("id") long id){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),resourceService.getApiById(id));
    }

    @ApiOperation(value = "根据ID设备信息列表")
    @RequestMapping(value = "getTerminalAccount" ,method = RequestMethod.POST)
    public Object getTerminalPage(@RequestBody CompanyVO companyVO){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),businessUnitsService.getTerminalPage(companyVO));
    }

    @ApiOperation(value = "获取平台商数据调用统计")
    @PostMapping("/getDeveloperQueryPage")
    public Object getDeveloperQueryPage(@RequestBody UserQueryQO userQueryQO){
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),statisticsUserService.getDeveloperQueryPage(userQueryQO));
    }

    @ApiOperation(value = "获取客户单位数据调用统计")
    @PostMapping("/getCustomerQueryPage")
    public Object getCustomerQueryPage(@RequestBody UserQueryQO userQueryQO){
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),statisticsUserService.getCustomerQueryPage(userQueryQO));
    }


    @ApiOperation(value = "获取平台商数据调用统计")
    @PostMapping("/getCompanyTotal")
    public Object getCompanyTotal(@RequestBody TimeRangeVO timeRangeVO){
        timeRangeVO.setWhether(Constants.TRUE);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),statisticsUserService.getTimeRangeTotal(timeRangeVO));
    }

    @ApiOperation(value = "获取平台商图形展示统计")
    @PostMapping("/getGraphicalCompanyTotal")
    public Object getGraphicalCompanyTotal(@RequestBody TimeRangeVO timeRangeVO){
        timeRangeVO.setWhether(Constants.TRUE);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),statisticsUserService.getGraphicalTotal(timeRangeVO));
    }

    @ApiOperation(value = "获取客户单位数据调用统计")
    @PostMapping("/getBusinessTotal")
    public Object getBusinessTotal(@RequestBody TimeRangeVO timeRangeVO){
        timeRangeVO.setWhether(Constants.FLASE);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),statisticsUserService.getTimeRangeTotal(timeRangeVO));
    }

    @ApiOperation(value = "获取客户单位图形展示统计")
    @PostMapping("/getGraphicalBusinessTotal")
    public Object getGraphicalBusinessTotal(@RequestBody TimeRangeVO timeRangeVO){
        timeRangeVO.setWhether(Constants.FLASE);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),statisticsUserService.getGraphicalTotal(timeRangeVO));
    }

}
