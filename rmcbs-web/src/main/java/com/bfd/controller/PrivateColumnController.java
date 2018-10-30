package com.bfd.controller;

import com.bfd.bean.PrivateColumnBean;
import com.bfd.common.vo.Result;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;
import com.bfd.service.PrivateColumnService;
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
 * 个性化栏目
 *
 * @author jiang.liu
 * @date  2018-08-03
 */
@RestController
@RequestMapping("/privateColumn")
@Api(value = "/privateColumn", description = "个性化栏目")
public class PrivateColumnController {

    @Autowired
    PrivateColumnService privateColumnService;

    @ApiOperation(value = "获取个性化栏目分级树结构")
    @RequestMapping(value = "/getPrivateColumnList", method = RequestMethod.GET)
    public Object getPrivateColumnList(@ApiParam("单位id") @RequestParam long businessId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateColumnService
                .getPrivateColumnList(businessId));
    }

    @ApiOperation(value = "添加个性化栏目分类")
    @RequestMapping(value = "/add", produces="application/json", method = RequestMethod.POST)
    public Object add(@RequestBody PrivateColumnBean privateColumnBean) {
        ValidatorUtils.validateEntity(privateColumnBean);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),privateColumnService.insert(privateColumnBean));
    }

    @ApiOperation(value = "编辑个性化栏目分类")
    @RequestMapping(value = "/update", produces="application/json", method = RequestMethod.POST)
    public Object update(@RequestBody PrivateColumnBean privateColumnBean) {
        ValidatorUtils.validateEntity(privateColumnBean);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),privateColumnService.update(privateColumnBean));
    }

    @ApiOperation(value = "删除个性化栏目分类")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(@RequestParam long id) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),privateColumnService.delete(id));
    }

    @ApiOperation(value = "调整个性化栏目顺序(上移、下移)")
    @RequestMapping(value = "/moveColumnSort", method = RequestMethod.GET)
    public Object movePrivateColumnSort(@RequestParam long firstId,@RequestParam long secondId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),privateColumnService
                .movePrivateColumnSort(firstId,secondId));
    }

    @ApiOperation(value = "向个性化栏目添加图书")
    @RequestMapping(value = "/addMetadataToPrivateColumn", produces="application/json", method = RequestMethod.POST)
    public Object addMetadataToPrivateColumn(@RequestParam long privateColumnId, @RequestParam List<Long> metadataIds) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),privateColumnService
                .addBookMetadata(privateColumnId,metadataIds));
    }

    @ApiOperation(value = "从个性化栏目里面删除单个图书")
    @RequestMapping(value = "/deleteFromPrivateColumn", method = RequestMethod.GET)
    public Object deleteFromPrivateColumn(
            @ApiParam("个性化栏目id") @RequestParam long privateColumnId,
            @ApiParam("图书id") @RequestParam long metadataId) {
        List<Long> metadataIds = new ArrayList<>();
        metadataIds.add(metadataId);
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateColumnService
                .batchDeleteFromPrivateColumn(privateColumnId, metadataIds));
    }

    @ApiOperation(value = "从个性化栏目里面批量删除图书")
    @RequestMapping(value = "/batchDeleteFromPrivateColumn", method = RequestMethod.GET)
    public Object batchDeleteFromPrivateColumn(@RequestParam long privateColumnId, @RequestParam List<Long> metadataIds) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),privateColumnService
                .batchDeleteFromPrivateColumn(privateColumnId, metadataIds));
    }

    @ApiOperation(value = "根据个性化栏目id,及筛选条件查询图书列表")
    @RequestMapping(value = "/getMetadataListForPrivateColumn", method = RequestMethod.POST)
    public Object getMetadataListForPrivateColumn(@RequestBody ResourceQueryVO resourceQueryVO) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateColumnService
                .getMetadataListForPrivateColumn(resourceQueryVO));
    }

    @ApiOperation(value = "按条件查询所有图书信息列表（包含已加入个性化栏目的数据）")
    @RequestMapping(value = "/getMetadataList", method = RequestMethod.POST)
    public Object getMetadataList(@RequestBody ResourceBookQueryVO resourceBookQueryVO) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateColumnService
                .queryMetadataList(resourceBookQueryVO));
    }

//    @ApiOperation(value = "调整图书顺序（上移、下移）")
//    @RequestMapping(value = "/moveBookSortForPrivateColumn", method = RequestMethod.GET)
//    public Object moveBookSortForPrivateColumn(
//            @ApiParam("个性化栏目id") @RequestParam long privateColumnId,
//            @ApiParam("移动的图书id") @RequestParam long firstBookId,
//            @ApiParam("被交换位置的图书id") @RequestParam long secondBookId) {
//        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateColumnService
//                .moveBookSortForPrivateColumn(privateColumnId,firstBookId,secondBookId));
//    }
//
//    @ApiOperation(value = "将个性化栏目的图书置顶")
//    @RequestMapping(value = "/setTopForPrivateColumnBook", method = RequestMethod.GET)
//    public Object setTopForPrivateColumnBook(
//            @ApiParam("个性化栏目id") @RequestParam long privateColumnId,
//            @ApiParam("置顶的图书id") @RequestParam long metadataId) {
//        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateColumnService
//                .setTopForPrivateColumnBook(privateColumnId,metadataId));
//    }
//
//    @ApiOperation(value = "将个性化栏目的图书置底")
//    @RequestMapping(value = "/setDownForPrivateColumnBook", method = RequestMethod.GET)
//    public Object setDownForPrivateColumnBook(
//            @ApiParam("个性化栏目id") @RequestParam long privateColumnId,
//            @ApiParam("置底的图书id") @RequestParam long metadataId) {
//        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateColumnService
//                .setDownForPrivateColumnBook(privateColumnId,metadataId));
//    }

    @ApiOperation(value = "为某一个性化栏目中的图书排序")
    @RequestMapping(value = "/setBookSort", method = RequestMethod.GET)
    public Object setBookSort(
            @ApiParam("个性化栏目id") @RequestParam long privateColumnId,
            @ApiParam("排序图书id") @RequestParam long metadataId,
            @ApiParam("排序位置") @RequestParam int sort) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateColumnService
                .setBookSort(privateColumnId,metadataId,sort));
    }

    @ApiOperation(value = "设置个性化皮肤模板")
    @RequestMapping(value = "/setSkinColor", method = RequestMethod.GET)
    public Object setSkinColor(
            @ApiParam("颜色") @RequestParam String skinColor, @ApiParam("单位id") @RequestParam long companyId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateColumnService
                .setSkinColor(skinColor,companyId));
    }

    @ApiOperation(value = "获取个性化皮肤模板")
    @RequestMapping(value = "/getSkinColor", method = RequestMethod.GET)
    public Object getSkinColor(@ApiParam("单位id") @RequestParam long companyId) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), privateColumnService
                .getSkinColor(companyId));
    }


}
