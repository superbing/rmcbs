package com.bfd.controller;

import com.bfd.aop.LimitDevice;
import com.bfd.aop.LimitIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.common.vo.Result;
import com.bfd.service.CompanyService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/company")
@Api(value = "company", description = "获取单位信息")
public class CompanyController {
    
    @Autowired
    CompanyService companyService;
    
    @RequestMapping("/getPcCompanyInfo")
    @LimitIp
    public Result<Object> getPcCompanyInfo(@RequestParam Long businessId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.getById(businessId));
    }

    @RequestMapping("/getAppCompanyInfo")
    @LimitDevice
    public Result<Object> getAppCompanyInfo(@RequestParam Long businessId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.getById(businessId));
    }
    
}
