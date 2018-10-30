package com.bfd.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.common.vo.Result;
import com.bfd.service.DrmEncryptService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * EPUB类型书籍加密后结果
 * 
 * @author xile.lu
 * @version [版本号, 2018年8月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController
@RequestMapping(value = "/drm", produces = {"application/json;charset=utf-8"})
@Api(value = "EPUB类型书籍加密后结果", description = "EPUB类型书籍加密后结果")
@Slf4j
public class DrmEncryptResultController {
    
    @Autowired
    DrmEncryptService drmEncryptService;
    
    @ApiOperation(value = "在线加密结果")
    @PostMapping("/onlineResult")
    public Object onlineResult(@RequestParam String resultJson) {
        log.info(String.format("在线加密结果返回,参数:%s", resultJson));
        drmEncryptService.onlineResult(resultJson);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "");
    }
    
    @PostMapping("/offlineResult")
    @ApiOperation(value = "离线加密结果")
    public Object offlineResult(@RequestParam String resultJson) {
        log.info(String.format("离线加密结果,参数:%s", resultJson));
        drmEncryptService.offlineResult(resultJson);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "");
    }
}
