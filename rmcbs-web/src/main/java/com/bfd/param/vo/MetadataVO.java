package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * MetadataVO值对象
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年7月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ApiModel(value = "MetadataVO", description = "元数据查询条件VO")
public class MetadataVO {
    

    @ApiModelProperty("查询开始时间")
    public String startTime;

    @ApiModelProperty("查询结束时间")
    public String endTime;

    @ApiModelProperty(value = "图书状态", required = false, example = "0")
    private String BookStatus;

    @ApiModelProperty(value = "唯一标识", required = false, example = "B_2018_001")
    private String uniqueId;

    @ApiModelProperty(value = "书籍名称", required = false, example = "中国共产党宣言")
    private String bookName;

    @ApiModelProperty(value = "ISBN")
    private String bookIsbn;

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

    @ApiModelProperty(value = "PDF上传状态")
    public String bookPdf;

}
