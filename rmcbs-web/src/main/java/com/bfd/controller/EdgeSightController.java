package com.bfd.controller;

import com.bfd.bean.ApiInfoBean;
import com.bfd.bean.ApiTypeBean;
import com.bfd.common.vo.PageVO;
import com.bfd.common.vo.Result;
import com.bfd.enums.EnabledEnum;
import com.bfd.param.vo.edgesighvo.*;
import com.bfd.param.vo.serviceaudit.ServiceAuditQO;
import com.bfd.param.vo.serviceaudit.ServiceAuditVO;
import com.bfd.service.ApiService;
import com.bfd.service.CompanyService;
import com.bfd.service.EdgeSightService;
import com.bfd.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 14:53 2018/8/27
 * @Modified by:
 */
@RestController
@RequestMapping(value = "/edgeSight", produces = {"application/json;charset=utf-8"})
@Api(value = "/edgeSight", description = "服务监控")
public class EdgeSightController {

    @Autowired
    EdgeSightService edgeSightService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private CompanyService companyService;

    @ApiOperation(value = "获取接口监控列表")
    @RequestMapping(value = "/getApiSight", method = RequestMethod.POST)
    public Object getApiSight(@RequestBody ApiSightQO apiSightQO) {
        ValidatorUtils.validateEntity(apiSightQO);
        PageVO<ApiSightVO> result = edgeSightService.getApiSight(apiSightQO);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
    }

    @ApiOperation(value = "获取接口-平台商列表")
    @RequestMapping(value = "/getCompanySight", method = RequestMethod.POST)
    public Object getCompanySight(@RequestBody CompanySightQO companySightQO) {
        ValidatorUtils.validateEntity(companySightQO);
        CompanySightDTO result = edgeSightService.getCompanySight(companySightQO);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
    }

    @ApiOperation(value = "获取接口-客户单位列表")
    @RequestMapping(value = "/getBusinessSight", method = RequestMethod.POST)
    public Object getBusinessSight(@RequestBody BusinessSightQO businessSightQO) {
        ValidatorUtils.validateEntity(businessSightQO);
        BusinessSightDTO result = edgeSightService.getBusinessSight(businessSightQO);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
    }


    @ApiOperation(value = "获取接口-客户单位列表")
    @PostMapping("/getDayCount")
    public Object getDayCount(@RequestBody DayCountSightQO dayCountSightQO) {
        ValidatorUtils.validateEntity(dayCountSightQO);
        DayCountSightDTO result = edgeSightService.getDayCount(dayCountSightQO);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
    }

    @ApiOperation(value = "获取当天接口统计")
    @PostMapping("/getDayApiCount")
    public Object getDayApiCount(@RequestBody DayCountSightQO dayCountSightQO) {
        ValidatorUtils.validateEntity(dayCountSightQO);
        DayCountSightDTO result = edgeSightService.getDayApiCount(dayCountSightQO);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
    }

    @ApiOperation(value = "获取服务审计")
    @PostMapping("/getServiceAudit")
    public Object getServiceAudit(@RequestBody ServiceAuditQO serviceAuditQO) {
        ValidatorUtils.validateEntity(serviceAuditQO);
        PageVO<ServiceAuditVO> result = edgeSightService.getServiceAudit(serviceAuditQO);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
    }

    @ApiOperation(value = "查询所有可用的Api")
    @GetMapping("/queryApi")
    public Object queryApi() {
        List<ApiInfoBean> result = apiService.queryApi();
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
    }

    @ApiOperation(value = "查询所有的Api类型")
    @GetMapping("/queryApiType")
    public Object queryApiType() {
        List<ApiTypeBean> result = apiService.queryApiType(EnabledEnum.OPEN.getKey());
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
    }

    @ApiOperation(value = "查询所有客户单位")
    @GetMapping("/getCompanyTree")
    public Object getCompanyTree() {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.getCompanyTree());
    }

}
