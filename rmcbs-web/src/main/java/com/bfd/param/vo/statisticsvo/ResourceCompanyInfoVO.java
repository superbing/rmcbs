package com.bfd.param.vo.statisticsvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资源使用查询-客户单位展示对象
 *
 * @author 姓名 工号
 * @version [版本号, 2018年9月28日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ApiModel(value = "ResourceCompanyInfoVO", description = "资源使用查询-客户单位展示对象")
public class ResourceCompanyInfoVO {

    @ApiModelProperty(value = "单位ID")
    private long id;

    @ApiModelProperty(value = "单位名称")
    private String companyName;

    @ApiModelProperty(value = "客户编码")
    private String companyCode;

    @ApiModelProperty(value = "accessKey")
    private String accessKey;

    @ApiModelProperty(value = "父节点(平台商)单位id")
    private long parentCompanyId;

    @ApiModelProperty(value = "父节点(平台商)单位名称")
    private String parentCompanyName;

    @ApiModelProperty(value = "起始实际")
    private String startTime;

    @ApiModelProperty(value = "到期时间")
    private String endTime;

    @ApiModelProperty("状态(1：开启， 0：关闭）")
    private int status;

    @ApiModelProperty("二级分类个数")
    private int childrenNumber;

    @ApiModelProperty("图书种类")
    private int bookNumber;

    @ApiModelProperty("pdf书籍数量")
    private int pdfNumber;

    @ApiModelProperty("epub书籍数量")
    private int epubNumber;


}