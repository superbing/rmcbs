package com.bfd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.common.vo.Result;
import com.bfd.param.vo.BusinessUnitsVO;
import com.bfd.param.vo.CompanyVO;
import com.bfd.service.BusinessUnitsService;
import com.bfd.utils.ValidatorUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @Author: chong.chen
 * @Description: 单位商务信息管理
 * @Date: Created in 18:18 2018/8/2
 * @Modified by:
 */
@RestController
@RequestMapping(value = "/business")
@Api(value = "/business",description = "单位商务信息管理")
public class BusinessUnitsController {

    @Autowired
    private BusinessUnitsService businessUnitsService;

    @ApiOperation(value = "修改单位商务信息(注意校验IP地址)")
    @RequestMapping(value = "/update", produces="application/json", method = RequestMethod.PUT)
    public Object update(@RequestBody BusinessUnitsVO businessUnitsVO) {
        ValidatorUtils.validateEntity(businessUnitsVO);
        businessUnitsService.update(businessUnitsVO);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),"");
    }

    @ApiOperation(value = "获取单位商务信息列表")
    @RequestMapping(value = "/getBusinessList" ,method = RequestMethod.POST)
    public Object getBusinessList(@RequestBody CompanyVO companyVO){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),businessUnitsService.getCustomersList(companyVO));
    }

    @ApiOperation(value = "根据ID获取单位商务信息")
    @RequestMapping(value = "getBusinessById{id}" ,method = RequestMethod.GET)
    public Object getCompanyById(@ApiParam("客户单位ID") @PathVariable("id") long id){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),businessUnitsService.getBusinessUnits(id));
    }

    @ApiOperation(value = "根据ID设备信息列表")
    @RequestMapping(value = "getTerminalAccount" ,method = RequestMethod.POST)
    public Object getTerminalPage(@RequestBody CompanyVO companyVO){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),businessUnitsService.getTerminalPage(companyVO));
    }

    @ApiOperation(value = "根据ID清空设备数")
    @RequestMapping(value = "deleteEntryTerminal{id}" ,method = RequestMethod.DELETE)
    public Object deleteEntryTerminal(@ApiParam("客户单位ID") @PathVariable("id") long id){
        businessUnitsService.deleteEntryTerminal(id);
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),' ');
    }

}
