package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 资源管理添加图书查询条件VO
 *
 * @author 姓名 工号
 * @version [版本号, 2018年8月1日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ApiModel(value = "ResourceBookQueryVO", description = "资源管理添加图书查询条件VO")
public class ResourceBookQueryVO {

    @NotNull(message = "分类id")
    @ApiModelProperty("分类id")
    public long cid;

    @ApiModelProperty("查询开始时间")
    public String startTime;

    @ApiModelProperty("查询结束时间")
    public String endTime;

    @ApiModelProperty("图书状态")
    public int bookStatus;

    @ApiModelProperty("唯一标识(支持模糊查询)")
    public String uniqueId;

    @ApiModelProperty("ISBN(支持模糊查询)")
    public String bookIsbn;

    @ApiModelProperty("书名(支持模糊查询)")
    public String bookName;

    @ApiModelProperty("当前页")
    public int current;

    @ApiModelProperty("每页显示条数")
    public int pageSize;

    @ApiModelProperty("排序字段")
    public String sortField = "create_time";

    @ApiModelProperty("排序类型（正序：asc，倒序：desc）")
    public String sortType = "desc";

}
