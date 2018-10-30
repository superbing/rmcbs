package com.bfd.controller;

import com.bfd.bean.DictBean;
import com.bfd.common.vo.Result;
import com.bfd.param.vo.CityVO;
import com.bfd.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 11:38 2018/8/7
 * @Modified by:
 */
@RestController
@RequestMapping(value = "/dict")
@Api(value = "/dict",description = "字典")
public class DictController {


    @Autowired
    private DictService dictService;

    @GetMapping(value = "/getDict{type}")
    @ApiOperation(value = "根据类型查询字典信息")
    public Object getDict(@ApiParam(value = "图书状态：book，省市:city,单位类型：company_type，是否可用：enable" +
            "责任方式：responsible ，出版社：press，语种：language，装帧形式：binding_type") @PathVariable("type") String type) {
        List<DictBean> dictBeans = dictService.getDict(type);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dictBeans);
    }

    @GetMapping(value = "/getAuthorList")
    @ApiOperation(value = "查询图书作者列表")
    public Object getDict() {
        List<String> authorList = dictService.getAuthorList();
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), authorList);
    }

    @GetMapping(value = "/getCityTree")
    @ApiOperation(value = "获取省市列表")
    public Object getCityTree() {
        List<CityVO> cityTree = dictService.getCityTree();
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), cityTree);
    }

}
