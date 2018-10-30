package com.bfd.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.common.vo.Result;
import com.bfd.param.vo.TimeRangeVO;
import com.bfd.service.HomeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 14:53 2018/9/07
 * @Modified by:
 */
@RestController
@RequestMapping(value = "/home", produces = {"application/json;charset=utf-8"})
@Api(value = "/home", description = "首页")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @ApiOperation(value = "查询书,栏目数据包等总数")
    @GetMapping("/getAllInformation")
    public Object getAllInformation(){
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),homeService.getAllInformation());
    }

    @ApiOperation(value = "获取数据调用统计")
    @PostMapping("/getTheOtherDayTotal")
    public Object getTheOtherDayTotal(@RequestBody TimeRangeVO timeRangeVO){
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),homeService.getTimeRangeTotal(timeRangeVO));
    }


    @ApiOperation(value = "获取图形展示统计")
    @PostMapping("/getGraphicalTotal")
    public Object getGraphicalTotal(@RequestBody TimeRangeVO timeRangeVO){
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),homeService.getGraphicalTotal(timeRangeVO));
    }
}
