package com.bfd.param.vo.statisticsvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 资源使用查询-分类对象
 *
 * @author 姓名 工号
 * @version [版本号, 2018年9月28日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ApiModel(value = "ResourceCategoryVO", description = "资源使用查询-分类对象")
public class ResourceCategoryVO {

    @ApiModelProperty("分类id")
    private long id;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("分类别名")
    private String aliasName;

    @ApiModelProperty("父节点id")
    private long parentId;

    @ApiModelProperty("使用类型")
    private int type;

    @ApiModelProperty("使用类型名称")
    private String typeName;

    @ApiModelProperty("排序字段")
    private int sort;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("客户单位数")
    private int companyNumber;

    @ApiModelProperty("子节点个数")
    private int childrenNumber;

    @ApiModelProperty("图书种类")
    private int bookNumber;

    @ApiModelProperty("pdf书籍数量")
    private int pdfNumber;

    @ApiModelProperty("epub书籍数量")
    private int epubNumber;


}
