package com.bfd.controller;

import com.bfd.common.vo.Result;
import com.bfd.service.PackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: bing.shen
 * @date: 2018/8/2 15:56
 * @Description:数据包接口
 */
@RestController
@RequestMapping(PackageController.REQ_URL)
@Api(value = PackageController.REQ_URL, description = "数据包接口")
public class PackageController {

    public static final String REQ_URL = "/package";

    @Autowired
    private PackageService packageService;

    @GetMapping(value = "/queryAllPackage")
    @ApiOperation(value = "查询数据包")
    public Result<Object> queryAllPackage(@RequestParam Long businessId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), packageService.queryAllPackage(businessId));
    }

    @GetMapping(value = "/bookPage")
    @ApiOperation(value = "分页查询数据包下书籍")
    public Result<Object> bookPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize,
                                   @RequestParam Long packageId, @RequestParam int type){
        return new Result<>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),
                packageService.bookPage(pageNum, pageSize, packageId, type));
    }
}
