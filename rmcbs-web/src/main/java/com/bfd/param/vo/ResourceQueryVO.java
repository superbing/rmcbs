package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 数据包图书列表查询条件VO
 *
 * @author 姓名 工号
 * @version [版本号, 2018年8月1日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ApiModel(value = "ResourceQueryVO", description = "数据包图书列表查询条件VO")
public class ResourceQueryVO {

    @NotNull(message = "分类id")
    @ApiModelProperty("分类id")
    public long cid;

    @ApiModelProperty("查询开始时间")
    public String startTime;

    @ApiModelProperty("查询结束时间")
    public String endTime;

    @ApiModelProperty("作者")
    public String author;

    @ApiModelProperty("书名(支持模糊查询)")
    public String bookName;

    @ApiModelProperty("唯一标识(支持模糊查询)")
    public String uniqueId;

    @ApiModelProperty("ISBN(支持模糊查询)")
    public String bookIsbn;

    @ApiModelProperty("排序字段")
    public String sortField = "create_time";

    @ApiModelProperty("排序类型（正序：asc，倒序：desc）")
    public String sortType = "desc";

}

