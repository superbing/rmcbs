package com.bfd.controller;

import com.bfd.bean.ColumnBean;
import com.bfd.common.vo.Result;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;
import com.bfd.service.ColumnService;
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
 * 栏目
 *
 * @author jiang.liu
 * @date  2018-7-27
 */
@RestController
@RequestMapping("/column")
@Api(value = "/column", description = "栏目")
public class ColumnController {

    @Autowired
    ColumnService columnService;

    @ApiOperation(value = "获取栏目分级树结构")
    @RequestMapping(value = "/getColumnList", method = RequestMethod.GET)
    public Object getColumnList() {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService.getColumnList());
    }

    @ApiOperation(value = "根据id获取下一级栏目列表")
    @RequestMapping(value = "/getColumnListById", method = RequestMethod.GET)
    public Object getColumnListById(@ApiParam("数据包id")@RequestParam long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService.getPackageListByParentId(id));
    }

    @ApiOperation(value = "添加栏目分类")
    @RequestMapping(value = "/add", produces="application/json", method = RequestMethod.POST)
    public Object add(@RequestBody ColumnBean columnBean) {
        ValidatorUtils.validateEntity(columnBean);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),columnService.insert(columnBean));
    }

    @ApiOperation(value = "编辑栏目分类")
    @RequestMapping(value = "/update", produces="application/json", method = RequestMethod.POST)
    public Object update(@RequestBody ColumnBean columnBean) {
        ValidatorUtils.validateEntity(columnBean);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),columnService.update(columnBean));
    }

    @ApiOperation(value = "删除栏目分类")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(@RequestParam long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),columnService.delete(id));
    }

    @ApiOperation(value = "调整栏目顺序(上移、下移)")
    @RequestMapping(value = "/moveColumnSort", method = RequestMethod.GET)
    public Object moveColumnSort(@RequestParam long firstId,@RequestParam long secondId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),columnService.moveColumnSort
                (firstId,secondId));
    }

    @ApiOperation(value = "向栏目添加图书")
    @RequestMapping(value = "/addMetadataToColumn", produces="application/json", method = RequestMethod.POST)
    public Object addMetadataToColumn(@RequestParam long columnId, @RequestParam List<Long> metadataIds) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),columnService
                .addBookMetadata(columnId,metadataIds));
    }

    @ApiOperation(value = "从栏目里面删除单个图书")
    @RequestMapping(value = "/deleteFromColumn", method = RequestMethod.GET)
    public Object deleteFromColumn(
            @ApiParam("栏目id") @RequestParam long columnId,
            @ApiParam("图书id") @RequestParam long metadataId) {
        List<Long> metadataIds = new ArrayList<>();
        metadataIds.add(metadataId);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService
                .batchDeleteFromColumn(columnId, metadataIds));
    }

    @ApiOperation(value = "从栏目里面批量删除图书")
    @RequestMapping(value = "/batchDeleteFromColumn", method = RequestMethod.GET)
    public Object batchDeleteFromColumn(@RequestParam long columnId, @RequestParam List<Long> metadataIds) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),columnService.batchDeleteFromColumn(
                columnId, metadataIds));
    }

    @ApiOperation(value = "根据栏目id,及筛选条件查询图书列表")
    @RequestMapping(value = "/getMetadataListForPackage", method = RequestMethod.POST)
    public Object getMetadataListForPackage(@RequestBody ResourceQueryVO resourceQueryVO) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService
                .getMetadataListForColumn(resourceQueryVO));
    }

    @ApiOperation(value = "按条件查询所有图书信息列表（包含已加入栏目的数据）")
    @RequestMapping(value = "/getMetadataList", method = RequestMethod.POST)
    public Object getMetadataList(@RequestBody ResourceBookQueryVO resourceBookQueryVO) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService
                .queryMetadataList(resourceBookQueryVO));
    }

//    @ApiOperation(value = "调整图书顺序（上移、下移）")
//    @RequestMapping(value = "/moveBookSortForColumn", method = RequestMethod.GET)
//    public Object moveBookSortForColumn(
//            @ApiParam("栏目id") @RequestParam long columnId,
//            @ApiParam("移动的图书id") @RequestParam long firstBookId,
//            @ApiParam("被交换位置的图书id") @RequestParam long secondBookId) {
//        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService
//                .moveBookSortForColumn(columnId,firstBookId,secondBookId));
//    }
//
//    @ApiOperation(value = "将栏目的图书置顶")
//    @RequestMapping(value = "/setTopForColumnBook", method = RequestMethod.GET)
//    public Object setTopForColumnBook(
//            @ApiParam("栏目id") @RequestParam long columnId,
//            @ApiParam("置顶的图书id") @RequestParam long metadataId) {
//        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService
//                .setTopForColumnBook(columnId,metadataId));
//    }
//
//    @ApiOperation(value = "将栏目的图书置底")
//    @RequestMapping(value = "/setDownForColumnBook", method = RequestMethod.GET)
//    public Object setDownForColumnBook(
//            @ApiParam("栏目id") @RequestParam long columnId,
//            @ApiParam("置底的图书id") @RequestParam long metadataId) {
//        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService
//                .setDownForColumnBook(columnId,metadataId));
//    }

    @ApiOperation(value = "为某一栏目中的图书排序")
    @RequestMapping(value = "/setBookSort", method = RequestMethod.GET)
    public Object setBookSort(
            @ApiParam("栏目id") @RequestParam long columnId,
            @ApiParam("排序图书id") @RequestParam long metadataId,
            @ApiParam("排序位置") @RequestParam int sort) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService
                .setBookSort(columnId,metadataId,sort));
    }

    @ApiOperation(value = "设置皮肤模板")
    @RequestMapping(value = "/setSkinTemplate", method = RequestMethod.GET)
    public Object setSkinTemplate(
            @ApiParam("模板id") @RequestParam int templateId,
            @ApiParam("类型(仅包含PC或APP)") @RequestParam String type) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService
                .setSkinTemplate(templateId,type));
    }

    @ApiOperation(value = "获取皮肤模板")
    @RequestMapping(value = "/getSkinTemplate", method = RequestMethod.GET)
    public Object getSkinTemplate() {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), columnService
                .getSkinTemplate());
    }




}
