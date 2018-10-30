package com.bfd.controller;

import com.bfd.bean.ApiBean;
import com.bfd.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.bean.AuthInfoBean;
import com.bfd.service.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: bing.shen
 * @date: 2018/8/13 17:19
 * @Description:
 */
@RestController
@Api(description = "认证接口")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/getToken")
    @ApiOperation(value = "获取token")
    public Result<String> getToken(@RequestParam String clientId, @RequestParam String secret){
        return new Result<>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),
                authService.getToken(clientId, secret));
    }

    @GetMapping(value = "/getAuthInfo")
    @ApiOperation(value = "获取认证信息")
    public AuthInfoBean getAuthInfo(@RequestParam String token) {
        return authService.getAuthInfo(token);
    }

    @GetMapping(value = "/getApiInfo")
    @ApiOperation(value = "获取认证信息")
    public ApiBean getApiInfo(@RequestParam String url, @RequestParam String companyCode) {
        return authService.getApiInfoByUrl(url, companyCode);
    }
}
