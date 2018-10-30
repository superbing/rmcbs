package com.bfd.controller;

import com.bfd.aop.LimitDevice;
import com.bfd.common.vo.Result;
import com.bfd.enums.ClientTypeEnum;
import com.bfd.service.ColumnService;
import com.bfd.aop.LimitIp;
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
 * @Description:栏目接口
 */
@RestController
@RequestMapping(ColumnController.REQ_URL)
@Api(value = ColumnController.REQ_URL, description = "栏目接口")
public class ColumnController {

    public static final String REQ_URL = "/column";

    @Autowired
    private ColumnService columnService;

    @GetMapping(value = "/queryPcPublicColumn")
    @ApiOperation(value = "查询PC公共栏目")
    @LimitIp
    public Result<Object> queryPcPublicColumn(@RequestParam Long businessId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService.queryPublicColumn(businessId));
    }

    @GetMapping(value = "/queryAppPublicColumn")
    @ApiOperation(value = "查询APP公共栏目")
    @LimitDevice
    public Result<Object> queryAppPublicColumn(@RequestParam Long businessId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService.queryPublicColumn(businessId));
    }

    @GetMapping(value = "/queryPcPrivateColumn")
    @ApiOperation(value = "查询PC个性化栏目")
    @LimitIp
    public Result<Object> queryPcPrivateColumn(@RequestParam Long businessId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService.queryPrivateColumn(businessId));
    }

    @GetMapping(value = "/queryAppPrivateColumn")
    @ApiOperation(value = "查询APP个性化栏目")
    @LimitDevice
    public Result<Object> queryAppPrivateColumn(@RequestParam Long businessId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService.queryPrivateColumn(businessId));
    }

    @GetMapping(value = "/bookPcPage")
    @ApiOperation(value = "分页查询PC栏目下书籍")
    @LimitIp
    public Result<Object> bookPcPage(@RequestParam int pageNum, @RequestParam int pageSize,
                            @RequestParam Long columnId, @RequestParam int columnType, @RequestParam Long parentId,
                            String order){
        return new Result<>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),
                columnService.bookPcPage(pageNum, pageSize, columnId, columnType, parentId, order));
    }

    @GetMapping(value = "/bookAppPage")
    @ApiOperation(value = "分页查询APP栏目下书籍")
    @LimitDevice
    public Result<Object> bookAppPage(@RequestParam int pageNum, @RequestParam int pageSize,
                                      @RequestParam Long columnId, @RequestParam int columnType, @RequestParam Long parentId,
                                      String order){
        return new Result<>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),
                columnService.bookAppPage(pageNum, pageSize, columnId, columnType, parentId, order));
    }

    @GetMapping(value = "/getPcTemplate")
    @ApiOperation(value = "获取PC模板ID")
    @LimitIp
    public Result<Object> getPcTemplate(@RequestParam Long businessId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                columnService.getTemplate(businessId));
    }
}
