package com.bfd.controller;

import com.bfd.aop.LimitDevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.service.CheckDrmBookService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * DRM图书验证
 *
 * @author admin
 * @date 2018-9-05
 */
@RestController
@RequestMapping("/checkdrmbook")
@Api(value = "/checkdrmbook", description = "DRM图书验证")
@Slf4j
public class CheckDrmBook {
    
    @Autowired
    private CheckDrmBookService checkDrmBookService;
    
    /**
     * 验证DRM图书
     *
     * @param businessId 客户单位id 52
     * @param bookId 图书id B-2018-0003
     * devInfo 设备相关信息 { "deviceName": "iPhone6Plus", "systemName": "iOS", "systemVersion": "11.4.1", "deviceId":
     *            "51BAD1BF-7B46-42E6-A724-85E788D7327F", "appVersion": "1.0", "deviceType": "iPhone6Plus" }
     * @return
     */
    @RequestMapping("/checkBook")
    @ApiOperation(value = "验证drm图书")
    @LimitDevice
    public Object checkBook(@RequestParam Long businessId, @RequestParam String bookId,
        @RequestParam String token) {
        log.info(String.format("checkBook param: businessId:%s, bookId:%s, token:%s", businessId, bookId, token));
        return checkDrmBookService.checkDrmBook(bookId, businessId, token);
    }
    
}
