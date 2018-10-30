package com.bfd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.common.vo.Result;
import com.bfd.param.vo.CompanyVO;
import com.bfd.service.ApiService;
import com.bfd.service.BusinessUnitsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @Author: chong.chen
 * @Description: API文档
 * @Date: Created in 14:36 2018/8/8
 * @Modified by:
 */
@RestController
@RequestMapping(value = "/document")
@Api(value = "/document",description = "API文档")
public class ApiDocumentController {


    @Autowired
    private BusinessUnitsService businessUnitsService;

    @Autowired
    private ApiService apiService;


    @PostMapping(value = "/getUnitList")
    @ApiOperation(value = "获取单位列表")
    public Object getUnitList(@RequestBody CompanyVO companyVO){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),businessUnitsService.getCustomersList(companyVO));
    }

    @ApiOperation(value = "获取APi树")
    @RequestMapping(value = "/getApiTree", produces="application/json", method = RequestMethod.GET)
    public Object getApiTree(@ApiParam("客户单位ID") @RequestParam Long id) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.getApiTree(id));
    }


    @ApiOperation(value = "获取接口信息")
    @GetMapping(value = "/getApiDocument")
    public Result<Object> getApiDocument(@ApiParam("接口ID") @RequestParam Long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.getApiDocument(id));
    }

}
