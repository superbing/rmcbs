package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;


/**
 * @Author: chong.chen
 * @Description: 元数据设置条件查询VO
 * @Date: Created in 16:00 2018/8/7
 * @Modified by:
 */
@Data
@ApiModel(value = "MetadataTermsVO",description = "元数据设置条件查询VO")
public class MetadataTermsVO {


    @ApiModelProperty(value = "唯一标识")
    public String uniqueId;

    @ApiModelProperty(value = "书名")
    public String bookName;

    @ApiModelProperty(value = "ISBN")
    public String bookIsbn;

    @ApiModelProperty(value = "数据包名称")
    public String packageName;

    @ApiModelProperty(value = "栏目名称")
    public String columnName;

    @ApiModelProperty(value = "格式状态")
    public String BookStatus;

    @Min(1)
    @ApiModelProperty(value = "当前页码", required = false, example = "1")
    private int current = 1;

    @Min(1)
    @ApiModelProperty(value = "每页显示条数", required = false, example = "10")
    private int pageSize = 10;

    @ApiModelProperty("排序字段")
    public String sortField = "create_time";

    @ApiModelProperty("排序类型（正序：asc，倒序：desc）")
    public String sortType = "desc";

}
