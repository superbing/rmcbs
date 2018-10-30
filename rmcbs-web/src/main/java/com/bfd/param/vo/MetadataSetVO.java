package com.bfd.param.vo;

import com.bfd.bean.ColumnBean;
import com.bfd.bean.DataPackageBean;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @Author: chong.chen
 * @Description: 元数据设置VO
 * @Date: Created in 16:00 2018/8/7
 * @Modified by:
 */
@Data
@ApiModel(value = "MetadataSetVO",description = "元数据设置VO")
public class MetadataSetVO {


    @ApiModelProperty(value = "元数据ID")
    private Long id;

    @ApiModelProperty(value = "唯一标识")
    public String uniqueId;

    @ApiModelProperty(value = "书名")
    public String bookName;

    @ApiModelProperty(value = "出版时间")
    public String publishDate;

    @ApiModelProperty(value = "ISBN(International Standard Book Number)")
    public String bookIsbn;

    @ApiModelProperty(value = "作者")
    public String author;

    @ApiModelProperty(value = "出版社")
    public String press;

    @ApiModelProperty(value = "数据包名称集合")
    public List<DataPackageBean> packageList = Lists.newArrayList();

    @ApiModelProperty(value = "栏目名称集合")
    public List<ColumnBean> columnList = Lists.newArrayList();

    @ApiModelProperty("是否已上传pdf，1:是；0:否；默认否")
    public String bookPdf;

    @ApiModelProperty("是否已上epub，1:是；0:否；默认否")
    public String bookEpub;

    @ApiModelProperty("是否已上传xml章节，1:是；0:否；默认否")
    public String bookXmlChapter;

    @ApiModelProperty("是否已上传xml内容，1:是；0:否；默认否")
    public String bookXml;

}
