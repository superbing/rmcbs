package com.bfd.controller;

import com.bfd.bean.PrivateDataPackageBean;
import com.bfd.common.vo.Result;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;
import com.bfd.service.PrivateDataPackageService;
import com.bfd.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 个性化数据包
 *
 * @author jiang.liu
 * @date 2018-08-03
 */
@RestController
@RequestMapping("/privateDataPackage")
@Api(value = "/privateDataPackage", description = "个性化数据包")
public class PrivateDataPackageController {

    @Autowired
    PrivateDataPackageService privateDataPackageService;

    @ApiOperation(value = "获取某个单位的个性化数据包分级树结构")
    @RequestMapping(value = "/getPrivatePackageList", method = RequestMethod.GET)
    public Object getPrivatePackageList(@ApiParam("单位id") @RequestParam long businessId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateDataPackageService
                .getPrivatePackageList(businessId));
    }

    @ApiOperation(value = "添加个性化数据包分类")
    @RequestMapping(value = "/add", produces = "application/json", method = RequestMethod.POST)
    public Object add(@RequestBody PrivateDataPackageBean privateDataPackageBean) {
        ValidatorUtils.validateEntity(privateDataPackageBean);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateDataPackageService.insert
                (privateDataPackageBean));
    }

    @ApiOperation(value = "编辑个性化数据包分类")
    @RequestMapping(value = "/update", produces = "application/json", method = RequestMethod.POST)
    public Object update(@RequestBody PrivateDataPackageBean privateDataPackageBean) {
        ValidatorUtils.validateEntity(privateDataPackageBean);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateDataPackageService.update
                (privateDataPackageBean));
    }

    @ApiOperation(value = "删除个性化数据包分类")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(@ApiParam("个性化数据包id") @RequestParam long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateDataPackageService.delete(id));
    }

    @ApiOperation(value = "向个性化数据包添加图书")
    @RequestMapping(value = "/addBookForPrivateDataPackage", produces = "application/json", method = RequestMethod.POST)
    public Object addBookForPrivateDataPackage(
            @ApiParam("个性化数据包id") @RequestParam long privateDataPackageId,
            @ApiParam("图书id集合") @RequestParam List<Long> metadataIds) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateDataPackageService
                .addBookForPrivateDataPackage(privateDataPackageId, metadataIds));
    }

    @ApiOperation(value = "从个性化数据包里面删除单个图书")
    @RequestMapping(value = "/deleteFromPrivateDataPackage", method = RequestMethod.GET)
    public Object deleteFromPrivateDataPackage(
            @ApiParam("个性化数据包id") @RequestParam long privateDataPackageId,
            @ApiParam("图书id") @RequestParam long metadataId) {
        List<Long> metadataIds = new ArrayList<>();
        metadataIds.add(metadataId);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateDataPackageService
                .batchDeleteFromPrivateDataPackage(privateDataPackageId, metadataIds));
    }

    @ApiOperation(value = "从个性化数据包里面批量删除图书")
    @RequestMapping(value = "/batchDeleteFromPrivateDataPackage", method = RequestMethod.GET)
    public Object batchDeleteFromPrivateDataPackage(
            @ApiParam("个性化数据包id") @RequestParam long privateDataPackageId,
            @ApiParam("图书id集合") @RequestParam List<Long> metadataIds) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateDataPackageService
                .batchDeleteFromPrivateDataPackage(privateDataPackageId, metadataIds));
    }

    @ApiOperation(value = "根据个性化数据包id,及筛选条件查询图书列表")
    @RequestMapping(value = "/getMetadataListForPrivatePackage", method = RequestMethod.POST)
    public Object getMetadataListForPrivatePackage(@RequestBody ResourceQueryVO resourceQueryVO) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateDataPackageService
                .getMetadataListForPrivatePackage(resourceQueryVO));
    }

    @ApiOperation(value = "按条件查询所有图书信息列表（包含已加入个性化数据包的数据）")
    @RequestMapping(value = "/getMetadataList", method = RequestMethod.POST)
    public Object getMetadataList(@RequestBody ResourceBookQueryVO resourceBookQueryVO) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateDataPackageService
                .queryMetadataList(resourceBookQueryVO));
    }




}
