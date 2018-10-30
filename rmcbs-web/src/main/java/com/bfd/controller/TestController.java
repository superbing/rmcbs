package com.bfd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;

@RestController
public class TestController {
    
    @RequestMapping("test")
    public Object test(@RequestParam(name = "docIds") String docIds) {
        JSONArray parseArray = JSONArray.parseArray(docIds);
        return parseArray;
    }
}
