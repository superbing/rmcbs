package com.bfd.controller;

import com.bfd.common.vo.Result;
import com.bfd.param.vo.*;
import com.bfd.service.*;
import com.bfd.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description: 资源授权管理
 * @Date: Created in 10:50 2018/8/3
 * @Modified by:
 */
@RestController
@RequestMapping(value = ResourceController.RESOURCE_URL)
@Api(value = ResourceController.RESOURCE_URL ,description = "资源授权管理")
public class ResourceController {

    public static final String RESOURCE_URL = "/resource";

    @Autowired
    private BusinessUnitsService businessUnitsService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    DataPackageService dataPackageService;

    @Autowired
    ColumnService columnService;

    @PostMapping(value = "/getUnitList")
    @ApiOperation(value = "获取单位列表")
    public Object getUnitList(@RequestBody CompanyVO companyVO){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),businessUnitsService.getCustomersList(companyVO));
    }

    @ApiOperation(value = "根据api名称模糊查询")
    @RequestMapping(value = "/getApiNameList" ,method = RequestMethod.POST)
    public Object getApiNameList(@RequestBody ResourceApiQO resourceApiQO){
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),resourceService.getApiInfoList(resourceApiQO));
    }

    @ApiOperation(value = "获取接口列表")
    @RequestMapping(value = "/getApiList" ,method = RequestMethod.POST)
    public Object getApiList(@RequestBody ResourceApiQO resourceApiQO){
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),resourceService.getApiInfoList(resourceApiQO));
    }

    @ApiOperation(value = "获取客户单位是否选择栏目或者数据包")
    @RequestMapping(value = "/getCustomerRecord" ,method = RequestMethod.GET)
    public Object getCustomerRecord(@ApiParam("单位ID") @RequestParam("id") long id){
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),resourceService.getCustomerRecord(id));
    }

    @ApiOperation(value = "获取栏目分级树结构")
    @GetMapping(value = "/getColumnList{id}")
    public Object getColumnList(@ApiParam("单位ID") @PathVariable("id") long id){
        List<ColumnVO> columnVOS = resourceService.getColumnList(id);
        return  new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),columnVOS);
    }

    @ApiOperation(value = "展示客户单位栏目分级树结构")
    @GetMapping(value = "/showColumnList{id}")
    public Object showColumnList(@ApiParam("单位ID") @PathVariable("id") long id){
        List<ColumnVO> columnVOS = resourceService.showColumnList(id);
        return  new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),columnVOS);
    }

    @ApiOperation(value = "获取数据包分级树结构")
    @GetMapping(value = "/getPackageList{id}")
    public Object getPackageList(@ApiParam("单位ID") @PathVariable("id") long id){
        List<DataPackageVO> dataPackageVOS = resourceService.getPackageList(id);
        return  new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),dataPackageVOS);
    }

    @ApiOperation(value = "展示客户单位数据包分级树结构")
    @GetMapping(value = "/showPackageList{id}")
    public Object showPackageList(@ApiParam("单位ID") @PathVariable("id") long id){
        List<DataPackageVO> dataPackageVOS = resourceService.showPackageList(id);
        return  new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),dataPackageVOS);
    }

    @ApiOperation(value = "根据ID获取客户单位选择的所有接口")
    @RequestMapping(value = "getApiById{id}" ,method = RequestMethod.GET)
    public Object getApiById(@ApiParam("平台商ID") @PathVariable("id") long id){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),resourceService.getApiById(id));
    }

    @ApiOperation(value = "添加客户数据包")
    @PostMapping(value = "/addResource")
    public Object addResource(@RequestBody ResourceVO resourceVO){
        ValidatorUtils.validateEntity(resourceVO);
        resourceService.addResource(resourceVO);
        return  new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),"");
    }


}
