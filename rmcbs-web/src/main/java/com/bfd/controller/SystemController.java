package com.bfd.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: bing.shen
 * @date: 2018/7/30 14:04
 * @Description :
 */

@RestController
@RequestMapping(SystemController.REQ_URL)
@Api(value = SystemController.REQ_URL, description = "系统管理")
public class SystemController {

    public static final String REQ_URL = "/sys";


}
