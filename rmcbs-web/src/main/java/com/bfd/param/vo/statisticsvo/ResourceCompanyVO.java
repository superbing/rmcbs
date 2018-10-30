package com.bfd.param.vo.statisticsvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 资源使用查询-单位对象
 *
 * @author 姓名 工号
 * @version [版本号, 2018年9月28日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ApiModel(value = "ResourceCategoryVO", description = "资源使用查询-单位对象")
public class ResourceCompanyVO {


    @ApiModelProperty("单位id")
    private long id;

    @ApiModelProperty("单位名称")
    private String companyName;

    @ApiModelProperty("父节点单位id")
    private long parentCompanyId;

    @ApiModelProperty("父节点单位名称")
    private String parentCompanyName;

    @ApiModelProperty(value = "到期时间")
    private String endTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("单位拥有分类数")
    private int categoryNumber;

    @ApiModelProperty("图书种类")
    private int bookNumber;

    @ApiModelProperty("pdf书籍数量")
    private int pdfNumber;

    @ApiModelProperty("epub书籍数量")
    private int epubNumber;
}
