package com.bfd.controller;

import com.bfd.bean.CompanyBean;
import com.bfd.common.vo.PageVO;
import com.bfd.common.vo.Result;
import com.bfd.param.vo.AddDeveloperVO;
import com.bfd.param.vo.ApiInfoVO;
import com.bfd.param.vo.CompanyStatusVO;
import com.bfd.param.vo.CompanyVO;
import com.bfd.service.ApiService;
import com.bfd.service.CompanyService;
import com.bfd.service.ResourceService;
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
 * @Description: 平台商管理
 * @Date: Created in 14:38 2018/7/27
 * @Modified by:
 */
@RestController
@RequestMapping(value = "/developer")
@Api(value = "/developer",description = "平台商管理")
public class DeveloperController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private ResourceService resourceService;

    @ApiOperation(value = "获取平台商分页列表")
    @PostMapping(value = "/getDevelopersList")
    public Object getDevelopersList(@RequestBody CompanyVO companyVO){
        PageVO<CompanyBean> result = companyService.getDevelopersList(companyVO);
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),result);
    }

    @ApiOperation(value = "获取所有平台商列表")
    @RequestMapping(value = "/getAllDevelopersList" ,method = RequestMethod.GET)
    public Object getAllDevelopersList(){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),companyService.getAllDevelopersList());
    }

    @ApiOperation(value = "根据ID获取平台商信息")
    @RequestMapping(value = "getCompanyById{id}" ,method = RequestMethod.GET)
    public Object getCompanyById(@ApiParam("平台商/客户单位ID") @PathVariable("id") long id){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),companyService.getCompanyById(id));
    }

    @ApiOperation(value = "添加平台商信息")
    @RequestMapping(value = "/addDevelopers", produces="application/json", method = RequestMethod.POST)
    public Object addDevelopers(@RequestBody AddDeveloperVO addDeveloperVO) {
        ValidatorUtils.validateEntity(addDeveloperVO);
        return new Result<Long>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),companyService.addDeveloper(addDeveloperVO));
    }

    @ApiOperation(value = "编辑平台商信息")
    @RequestMapping(value = "/update", produces="application/json", method = RequestMethod.PUT)
    public Object updateDeveloper(@RequestBody AddDeveloperVO addDeveloperVO) {
        ValidatorUtils.validateEntity(addDeveloperVO);
        return new Result<Integer>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.updateDeveloper(addDeveloperVO));
    }

    @ApiOperation(value = "更改状平台商状态")
    @RequestMapping(value = "/updateStatus",produces = "application/json",method = RequestMethod.PUT)
    public Object updateStatus(@RequestBody CompanyStatusVO companyStatusVO){
        ValidatorUtils.validateEntity(companyStatusVO);
        return new Result<>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),companyService.updateStatus(companyStatusVO));
    }

    @ApiOperation(value = "删除平台商信息")
    @RequestMapping(value = "/delete{id}", produces="application/json", method = RequestMethod.DELETE)
    public Object delete(@PathVariable("id") long id) {
        return new Result<Boolean>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.deleteDeveloper(id));
    }

    @PostMapping(value = "/queryApiList")
    @ApiOperation(value = "获取接口信息列表")
    public Object queryApiList(@RequestBody ApiInfoVO apiInfoVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), apiService.getApiList(apiInfoVO));
    }

    @ApiOperation(value = "添加接口")
    @PostMapping(value = "/addApi")
    public Object addApi(@ApiParam("平台商ID") @RequestParam long businessId,@ApiParam("添加接口ID集合")@RequestParam List<Long> apiIds){
        return  new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),resourceService.AddApi(businessId,apiIds));
    }

    @ApiOperation(value = "删除接口")
    @DeleteMapping(value = "/deleteOne")
    public Object deleteOne(@ApiParam(value = "平台商ID") @RequestParam long id,@ApiParam(value = "接口ID") @RequestParam long apiId){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),resourceService.deleteOne(id,apiId));
    }

    @ApiOperation(value = "根据ID获取平台商选择的所有接口")
    @RequestMapping(value = "getApiById{id}" ,method = RequestMethod.GET)
    public Object getApiById(@ApiParam("平台商ID") @PathVariable("id") long id){
        return new Result<Object>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),resourceService.getApiById(id));
    }

}
