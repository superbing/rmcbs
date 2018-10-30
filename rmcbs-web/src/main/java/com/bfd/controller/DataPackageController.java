package com.bfd.controller;

import com.bfd.bean.DataPackageBean;
import com.bfd.common.vo.Result;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;
import com.bfd.service.DataPackageService;
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
 * 数据包
 *
 * @author jiang.liu
 * @date 2018-7-25
 */
@RestController
@RequestMapping("/dataPackage")
@Api(value = "/dataPackage", description = "数据包")
public class DataPackageController {

    @Autowired
    DataPackageService dataPackageService;

    @ApiOperation(value = "获取数据包分级树结构")
    @RequestMapping(value = "/getPackageList", method = RequestMethod.GET)
    public Object getPackageList() {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dataPackageService.getPackageList
                ());
    }

    @ApiOperation(value = "根据id获取下一级数据包列表")
    @RequestMapping(value = "/getPackageListById", method = RequestMethod.GET)
    public Object getPackageListById(@ApiParam("数据包id") @RequestParam long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dataPackageService
                .getPackageListByParentId(id));
    }

    @ApiOperation(value = "添加数据包分类")
    @RequestMapping(value = "/add", produces = "application/json", method = RequestMethod.POST)
    public Object add(@RequestBody DataPackageBean dataPackageBean) {
        ValidatorUtils.validateEntity(dataPackageBean);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dataPackageService.insert
                (dataPackageBean));
    }

    @ApiOperation(value = "编辑数据包分类")
    @RequestMapping(value = "/update", produces = "application/json", method = RequestMethod.POST)
    public Object update(@RequestBody DataPackageBean dataPackageBean) {
        ValidatorUtils.validateEntity(dataPackageBean);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dataPackageService.update
                (dataPackageBean));
    }

    @ApiOperation(value = "删除数据包分类")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(@ApiParam("数据包id") @RequestParam long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dataPackageService.delete(id));
    }

    @ApiOperation(value = "向数据包添加图书")
    @RequestMapping(value = "/addMetadataToPackage", produces = "application/json", method = RequestMethod.POST)
    public Object addMetadataToPackage(
            @ApiParam("数据包id") @RequestParam long dataPackageId,
            @ApiParam("图书id集合") @RequestParam List<Long> metadataIds) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dataPackageService
                .addBookMetadata(dataPackageId, metadataIds));
    }

    @ApiOperation(value = "从数据包里面删除单个图书")
    @RequestMapping(value = "/deleteFromDataPackage", method = RequestMethod.GET)
    public Object deleteFromDataPackage(
            @ApiParam("数据包id") @RequestParam long dataPackageId,
            @ApiParam("图书id") @RequestParam long metadataId) {
        List<Long> metadataIds = new ArrayList<>();
        metadataIds.add(metadataId);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dataPackageService
                .batchDeleteFromDataPackage(dataPackageId, metadataIds));
    }

    @ApiOperation(value = "从数据包里面批量删除图书")
    @RequestMapping(value = "/batchDeleteFromDataPackage", method = RequestMethod.GET)
    public Object batchDeleteFromDataPackage(
            @ApiParam("数据包id") @RequestParam long dataPackageId,
            @ApiParam("图书id集合") @RequestParam List<Long> metadataIds) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dataPackageService
                .batchDeleteFromDataPackage(dataPackageId, metadataIds));
    }

    @ApiOperation(value = "根据数据包id,及筛选条件查询图书列表")
    @RequestMapping(value = "/getMetadataListForPackage", method = RequestMethod.POST)
    public Object getMetadataListForPackage(@RequestBody  ResourceQueryVO resourceQueryVO) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dataPackageService
                .getMetadataListForPackage(resourceQueryVO));
    }

    @ApiOperation(value = "按条件查询所有图书信息列表（包含已加入数据包的数据）")
    @RequestMapping(value = "/getMetadataList", method = RequestMethod.POST)
    public Object getMetadataList(@RequestBody ResourceBookQueryVO resourceBookQueryVO) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dataPackageService
                .queryMetadataList(resourceBookQueryVO));
    }

}
