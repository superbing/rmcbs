package com.bfd.controller;

import com.bfd.common.vo.Result;
import com.bfd.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    private ApiService apiService;


    @ApiOperation(value = "获取APi树")
    @RequestMapping(value = "/getApiTree", produces="application/json", method = RequestMethod.GET)
    public Object getApiTree(@ApiParam("客户单位ID") @RequestParam Long businessId) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.getApiTree(businessId));
    }


    @ApiOperation(value = "获取接口信息")
    @GetMapping(value = "/getApiDocument")
    public Result<Object> getApiDocument(@ApiParam("接口ID") @RequestParam Long apiId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.getApiDocument(apiId));
    }

}
