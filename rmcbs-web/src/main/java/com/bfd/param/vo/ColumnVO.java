package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: chong.chen
 * @date: 2018/8/9 14:42
 * @Description: 栏目树设置选中展示
 */
@Data
@ApiModel(value = "ColumnVO", description = "栏目树设置选中展示")
public class ColumnVO {

    @ApiModelProperty("栏目id")
    private long id;

    @NotNull
    @ApiModelProperty("栏目名称")
    private String name;

    @ApiModelProperty("栏目别名")
    private String aliasName;

    @NotNull
    @ApiModelProperty("父节点id")
    private long parentId;

    @ApiModelProperty("使用类型")
    private int type;

    @ApiModelProperty("排序字段")
    private int sort;

    @ApiModelProperty(value = "是否选中，1选中，0不选择")
    private Integer checked;

    @ApiModelProperty(value = "图书数量")
    private Integer bookNumber;

}


