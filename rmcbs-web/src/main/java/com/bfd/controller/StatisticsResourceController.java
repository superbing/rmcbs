package com.bfd.controller;

import com.bfd.common.vo.Result;
import com.bfd.param.vo.statisticsvo.ResourceCompanyParamVO;
import com.bfd.param.vo.statisticsvo.ResourceParamVO;
import com.bfd.param.vo.statisticsvo.TimeRangeParamVO;
import com.bfd.service.StatisticsResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 统计查询--资源使用查询
 *
 * @author
 * @date 2018-9-18
 */
@RestController
@RequestMapping("/statisticsResource")
@Api(value = "/statisticsResource", description = "统计查询--资源使用查询")
public class StatisticsResourceController {

    @Autowired
    StatisticsResourceService statisticsResourceService;

    @ApiOperation(value = "首页--获取数据包列表")
    @RequestMapping(value = "/getDataPackageList", method = RequestMethod.POST)
    public Object getDataPackageList(@RequestBody ResourceParamVO resourceParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getDataPackageList(resourceParamVO));
    }

    @ApiOperation(value = "首页--获取栏目列表")
    @RequestMapping(value = "/getColumnList", method = RequestMethod.POST)
    public Object getColumnList(@RequestBody ResourceParamVO resourceParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getColumnList(resourceParamVO));
    }

    @ApiOperation(value = "首页--获取个性化数据包列表")
    @RequestMapping(value = "/getPrivateDataPackageList", method = RequestMethod.POST)
    public Object getPrivateDataPackageList(@RequestBody ResourceParamVO resourceParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getPrivateDataPackageList(resourceParamVO));
    }

    @ApiOperation(value = "首页--获取个性化栏目列表")
    @RequestMapping(value = "/getPrivateColumnList", method = RequestMethod.POST)
    public Object getPrivateColumnList(@RequestBody ResourceParamVO resourceParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getPrivateColumnList(resourceParamVO));
    }

    @ApiOperation(value = "查看明细--根据数据包获取客户单位列表")
    @RequestMapping(value = "/getCompanyListByPackage", method = RequestMethod.POST)
    public Object getCompanyListByPackage(@RequestBody ResourceCompanyParamVO resourceCompanyParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getCompanyListByPackage(resourceCompanyParamVO));
    }

    @ApiOperation(value = "查看明细--根据栏目获取客户单位列表")
    @RequestMapping(value = "/getCompanyListByColumn", method = RequestMethod.POST)
    public Object getCompanyListByColumn(@RequestBody ResourceCompanyParamVO resourceCompanyParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getCompanyListByColumn(resourceCompanyParamVO));
    }

    @ApiOperation(value = "查看明细--根据个性化数据包获取客户单位列表")
    @RequestMapping(value = "/getCompanyListByPrivatePackage", method = RequestMethod.POST)
    public Object getCompanyListByPrivatePackage(@RequestBody ResourceCompanyParamVO resourceCompanyParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getCompanyListByPrivatePackage(resourceCompanyParamVO));
    }

    @ApiOperation(value = "查看明细--根据个性化栏目获取客户单位列表")
    @RequestMapping(value = "/getCompanyListByPrivateColumn", method = RequestMethod.POST)
    public Object getCompanyListByPrivateColumn(@RequestBody ResourceCompanyParamVO resourceCompanyParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getCompanyListByPrivateColumn(resourceCompanyParamVO));
    }

    @ApiOperation(value = "查看明细--根据分类id获取二级分类下拉列表")
    @RequestMapping(value = "/getDropDownList", method = RequestMethod.GET)
    public Object getDropDownList(@RequestParam long id,String type) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getDropDownList(id,type));
    }

    @ApiOperation(value = "数据包--客户单位使用明细--新增数")
    @RequestMapping(value = "/getPackageUseDetailTotal", method = RequestMethod.POST)
    public Object getPackageUseDetailTotal(@RequestBody TimeRangeParamVO timeRangeParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getPackageUseDetailTotal(timeRangeParamVO));
    }

    @ApiOperation(value = "数据包--客户单位使用明细--趋势图")
    @RequestMapping(value = "/getPackageUseDetailTrend", method = RequestMethod.POST)
    public Object getPackageUseDetailTrend(@RequestBody TimeRangeParamVO timeRangeParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getPackageUseDetailTrend(timeRangeParamVO));
    }

    @ApiOperation(value = "栏目--客户单位使用明细--新增数")
    @RequestMapping(value = "/getColumnUseDetailTotal", method = RequestMethod.POST)
    public Object getColumnUseDetailTotal(@RequestBody TimeRangeParamVO timeRangeParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getColumnUseDetailTotal(timeRangeParamVO));
    }

    @ApiOperation(value = "栏目--客户单位使用明细--趋势图")
    @RequestMapping(value = "/getColumnUseDetailTrend", method = RequestMethod.POST)
    public Object getColumnUseDetailTrend(@RequestBody TimeRangeParamVO timeRangeParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getColumnUseDetailTrend(timeRangeParamVO));
    }

    @ApiOperation(value = "个性化数据包--客户单位使用明细--新增数")
    @RequestMapping(value = "/getPrivatePackageUseDetailTotal", method = RequestMethod.POST)
    public Object getPrivatePackageUseDetailTotal(@RequestBody TimeRangeParamVO timeRangeParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getPrivatePackageUseDetailTotal(timeRangeParamVO));
    }

    @ApiOperation(value = "个性化数据包--客户单位使用明细--趋势图")
    @RequestMapping(value = "/getPrivatePackageUseDetailTrend", method = RequestMethod.POST)
    public Object getPrivatePackageUseDetailTrend(@RequestBody TimeRangeParamVO timeRangeParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getPrivatePackageUseDetailTrend(timeRangeParamVO));
    }

    @ApiOperation(value = "个性化栏目--客户单位使用明细--新增数")
    @RequestMapping(value = "/getPrivateColumnUseDetailTotal", method = RequestMethod.POST)
    public Object getPrivateColumnUseDetailTotal(@RequestBody TimeRangeParamVO timeRangeParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getPrivateColumnUseDetailTotal(timeRangeParamVO));
    }

    @ApiOperation(value = "个性化栏目--客户单位使用明细--趋势图")
    @RequestMapping(value = "/getPrivateColumnUseDetailTrend", method = RequestMethod.POST)
    public Object getPrivateColumnUseDetailTrend(@RequestBody TimeRangeParamVO timeRangeParamVO) {
        return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), statisticsResourceService
                .getPrivateColumnUseDetailTrend(timeRangeParamVO));
    }



}
