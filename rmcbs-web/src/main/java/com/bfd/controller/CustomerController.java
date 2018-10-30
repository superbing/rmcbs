package com.bfd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bfd.bean.CompanyBean;
import com.bfd.common.vo.PageVO;
import com.bfd.common.vo.Result;
import com.bfd.param.vo.CompanyStatusVO;
import com.bfd.param.vo.CompanyVO;
import com.bfd.service.CompanyService;
import com.bfd.utils.ValidatorUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @Author: chong.chen
 * @Description: 客户单位管理
 * @Date: Created in 14:38 2018/7/27
 * @Modified by:
 */
@RestController
@RequestMapping(value = "/customer")
@Api(value = "/customer", description = "客户单位管理")
public class CustomerController {
    
    @Autowired
    private CompanyService companyService;
    
    @ApiOperation(value = "获取所有平台商列表")
    @RequestMapping(value = "/getAllDevelopersList", method = RequestMethod.GET)
    public Object getAllDevelopersList() {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.getAllDevelopersList());
    }
    
    @ApiOperation(value = "获取客户单位分页列表")
    @RequestMapping(value = "/getCustomerList", method = RequestMethod.POST)
    public Object getCustomerList(@RequestBody CompanyVO companyVO) {
        PageVO<CompanyBean> result = companyService.getCustomerList(companyVO);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
    }
    
    @ApiOperation(value = "根据ID获取客户单位信息")
    @RequestMapping(value = "getCustomerId{id}", method = RequestMethod.GET)
    public Object getCustomerId(@ApiParam("客户单位ID") @PathVariable("id") long id) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.getCompanyById(id));
    }

    @ApiOperation(value = "根据ID获取客户单位的平台商自定义字段")
    @RequestMapping(value = "getExtraData/{id}", method = RequestMethod.GET)
    public Object getExtraData(@ApiParam("ID") @PathVariable("id") long id) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.getExtraData(id));
    }
    
    @ApiOperation(value = "添加客户单位信息")
    @RequestMapping(value = "/addCustomer", produces = "application/json", method = RequestMethod.POST)
    public Object addCustomer(@RequestBody CompanyBean companyBean) {
        ValidatorUtils.validateEntity(companyBean);
        return new Result<Long>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.addCustomer(companyBean));
    }
    
    @ApiOperation(value = "编辑客户单位信息")
    @RequestMapping(value = "/update", produces = "application/json", method = RequestMethod.PUT)
    public Object update(@RequestBody CompanyBean companyBean) {
        ValidatorUtils.validateEntity(companyBean);
        return new Result<Integer>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.updateCustomer(companyBean));
    }
    
    @ApiOperation(value = "更改客户单位状态")
    @RequestMapping(value = "/updateStatus", produces = "application/json", method = RequestMethod.PUT)
    public Object updateStatus(@RequestBody CompanyStatusVO companyStatusVO) {
        ValidatorUtils.validateEntity(companyStatusVO);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.updateStatus(companyStatusVO));
    }
    
    @ApiOperation(value = "删除客户单位信息")
    @RequestMapping(value = "/delete{id}", produces = "application/json", method = RequestMethod.DELETE)
    public Object delete(@PathVariable("id") long id) {
        return new Result<Boolean>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.deleteCustomer(id));
    }

    @ApiOperation(value = "重置AccessKey")
    @RequestMapping(value = "/resetAccessKey{id}", produces = "application/json", method = RequestMethod.PATCH)
    public Object resetAccessKey(@PathVariable("id") long id) {
        return new Result<Integer>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), companyService.resetAccessKey(id));
    }
    
    @ApiOperation(value = "分类查询客户单位")
    @PostMapping(value = "/getCustomerByType")
    public Object getCustomerByType(@RequestBody CompanyVO companyVO) {
        ValidatorUtils.validateEntity(companyVO);
        PageVO<CompanyBean> pageVo = companyService.getCompany(companyVO);
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), pageVo);
    }
    
}
