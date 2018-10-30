package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * BookVO
 *
 * @author 姓名 工号
 * @version [版本号, 2018年8月1日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ApiModel(value = "BookVO", description = "元数据VO")
public class BookVO {

    public long id;

    @NotNull(message = "唯一标识不能为空")
    @ApiModelProperty(value = "唯一标识", example = "B_2018_001", required = true)
    public String uniqueId;

    @NotNull(message = "书名不能为空")
    @ApiModelProperty(value = "书名", example = "马克思列名主义书籍", required = true)
    public String bookName;

    @NotNull(message = "作者不能为空")
    @ApiModelProperty(value = "作者", example = "陈燕青", required = true)
    public String author;

    @NotNull(message = "责任方式不能为空")
    @ApiModelProperty(value = "责任方式", example = "字典项", required = true)
    public String responsible;

    @NotNull(message = "出版社不能为空")
    @ApiModelProperty(value = "出版社", example = "人民出版社;百分点出版社", required = true)
    public String press;

    @NotNull(message = "出版地不能为空")
    @ApiModelProperty(value = "出版地", example = "字典项, 省会城市", required = true)
    public String publishPlace;

    @NotNull(message = "出版时间不能为空")
    @ApiModelProperty(value = "出版时间", example = "2018-07-27", required = true)
    public String publishDate;

    @NotNull(message = "语种不能为空")
    @ApiModelProperty(value = "语种", example = "字典项zh-CN", required = true)
    public String language;

    @NotNull(message = "ISBN不能为空")
    @ApiModelProperty(value = "ISBN(International Standard Book Number)", example = "ISBN", required = true)
    public String bookIsbn;

    @ApiModelProperty(value = "bookPdf", required = true)
    public String bookPdf;

    @ApiModelProperty(value = "bookEpub", required = true)
    public String bookEpub;

    @ApiModelProperty(value = "bookXml", required = true)
    public String bookXml;

    @ApiModelProperty(value = "bookType", example = "PDF EPUB XML", required = true)
    public String bookType;

    @ApiModelProperty(value = "sort", required = true)
    public int sort;

    @ApiModelProperty(value = "是否选中状态", example = "true", required = true)
    public boolean checked;

}
