package com.bfd.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * MetadataBean对应数据库元数据表
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年7月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ApiModel(value = "MetadataBean", description = "元数据")
public class MetadataBean {
    
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
    public String bookIsbn;//
    
    @NotNull(message = "关键词不能为空")
    @ApiModelProperty(value = "关键词", example = "马克思;法国", required = true)
    public String keywords;
    
    @NotNull(message = "内容提要不能为空")
    @ApiModelProperty(value = "内容提要", example = "资本论第一章xxxx", required = true)
    public String contentSummary;
    
    @NotNull(message = "制作者不能为空")
    @ApiModelProperty(value = "制作者", example = "人民出版社", required = true)
    public String bookMaker;
    
    @ApiModelProperty(value = "制作时间", example = "2018-07-27 10:00:00")
    public String makeTime;
    
    @ApiModelProperty(value = "版次", example = "3版")
    public String editOrder;
    
    @ApiModelProperty(value = "版次年", example = "2018")
    public String editOrderYear;
    
    @ApiModelProperty(value = "版次月", example = "10")
    public String editOrderMonth;
    
    @ApiModelProperty(value = "印次", example = "第1次")
    public String printOrder;
    
    @ApiModelProperty(value = "印次年", example = "2018")
    public String printOrderYear;
    
    @ApiModelProperty(value = "印次月", example = "10")
    public String printOrderMonth;
    
    @ApiModelProperty(value = "开本")
    public String bookSize;
    
    @ApiModelProperty(value = "开本尺寸1")
    public String bookSizeOne;
    
    @ApiModelProperty(value = "开本尺寸2")
    public String bookSizeTwo;
    
    @ApiModelProperty(value = "字数")
    public Long wordCount;
    
    @ApiModelProperty(value = "印张")
    public String printSheet;
    
    @ApiModelProperty(value = "装帧方式：平装和精装")
    public String bindingType;
    
    @ApiModelProperty(value = "页数")
    public String pagesNumber;
    
    @ApiModelProperty(value = "责编")
    public String editor;
    
    @ApiModelProperty(value = "定价")
    public String price;
    
    @ApiModelProperty(value = "中图分类")
    public String bookCategory;
    
    @ApiModelProperty(value = "作者简介")
    public String authorBrief;
    
    @ApiModelProperty(value = "引文内容")
    public String citation;
    
    @ApiModelProperty(value = "是否引进版; 1:是；0:否；默认否", example = "1")
    public String importVersion;
    
    @ApiModelProperty(value = "原书语种，只有当import_version=1时，此字段有效", example = "英语")
    public String originLanguage;
    
    @ApiModelProperty(value = "原书书名，只有当import_version=1时，此字段有效", example = "English xx")
    public String originBookName;
    
    @ApiModelProperty(value = "翻译者，只有当import_version=1时，此字段有效", example = "陈燕青")
    public String translator;
    
    @ApiModelProperty("是否已上传pdf，1:是；0:否；默认否")
    public String bookPdf;
    
    @ApiModelProperty("是否已上epub，1:是；0:否；默认否")
    public String bookEpub;
    
    @ApiModelProperty("是否已上传xml章节，1:是；0:否；默认否")
    public String bookXmlChapter;
    
    @ApiModelProperty("是否已上传xml内容，1:是；0:否；默认否")
    public String bookXml;

    @ApiModelProperty("PDF页数")
    public Integer pageNumPdf;
    
}
