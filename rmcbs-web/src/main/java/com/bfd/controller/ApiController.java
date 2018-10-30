package com.bfd.controller;

import com.bfd.param.vo.ApiInfoVO;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.bean.ApiInfoBean;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.Result;
import com.bfd.service.ApiService;
import com.bfd.utils.ValidatorUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: bing.shen
 * @date: 2018/8/2 15:56
 * @Description:接口管理
 */
@RestController
@RequestMapping(ApiController.REQ_URL)
@Api(value = ApiController.REQ_URL, description = "接口管理")
public class ApiController {
    
    public static final String REQ_URL = "/api";
    
    @Autowired
    private ApiService apiService;

    @Value("${api.url}")
    private String address;
    
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加接口")
    public Result<Object> add(@RequestBody ApiInfoBean apiInfoBean) {
        ValidatorUtils.validateEntity(apiInfoBean);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.addApi(apiInfoBean));
    }
    
    @PutMapping(value = "/update")
    @ApiOperation(value = "更新接口")
    public Result<Object> update(@RequestBody ApiInfoBean apiInfoBean) {
        ValidatorUtils.validateEntity(apiInfoBean);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.updateApi(apiInfoBean));
    }

    @GetMapping(value = "/updateStatus")
    @ApiOperation(value = "更新接口状态")
    public Result<Object> updateStatus(@ApiParam("接口ID") @RequestParam long id, @ApiParam("接口状态")@RequestParam int status) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.updateStatus(id,status));
    }
    
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除接口")
    public Result<Object> delete(@RequestParam Long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.deleteApi(id));
    }
    
    @GetMapping(value = "/getApiInfo")
    @ApiOperation(value = "获取接口信息")
    public Result<Object> getApiInfo(@RequestParam Long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.getApiInfo(id));
    }
    
    @PostMapping(value = "/queryApiList")
    @ApiOperation(value = "获取接口信息列表")
    public Result<Object> queryApiList(@RequestBody ApiInfoVO apiInfoVO) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.apiInfoPage(apiInfoVO));
    }
    
    @GetMapping(value = "/apiTypes")
    @ApiOperation(value = "查询api类型")
    public Result<Object> queryApiType() {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.queryApiType(Constants.ENABLE_STATUS));
    }

    @GetMapping(value = "/getApiAddress")
    @ApiOperation(value = "获取Api接口根地址")
    public Result<Object> getApiAddress() {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), address);
    }
    
}
