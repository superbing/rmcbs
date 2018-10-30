package com.bfd.param.vo.statisticsvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * 资源使用查询-查询客户单位参数对象
 *
 * @author 姓名 工号
 * @version [版本号, 2018年9月28日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ApiModel(value = "ResourceCompanyParamVO", description = "资源使用查询-查询客户单位参数对象")
public class ResourceCompanyParamVO {

    @ApiModelProperty(value = "分类id", required = true)
    private long cid;

    @ApiModelProperty(value = "二级分类id", required = false)
    private long sid;

    @JsonIgnore
    @ApiModelProperty(value = "二级分类id集合", required = false)
    private List<Long> list;

    @ApiModelProperty(value = "平台商id", required = false)
    private String businessId;

    @ApiModelProperty(value = "客户单位名称", required = false)
    private String companyName;

    @ApiModelProperty("排序字段")
    public String sortField = "create_time";

    @ApiModelProperty("排序类型（正序：asc，倒序：desc）")
    public String sortType = "desc";

    @Min(1)
    @ApiModelProperty(value = "当前页码", required = false, example = "1")
    private int current = 1;

    @Min(1)
    @ApiModelProperty(value = "每页显示条数", required = false, example = "10")
    private int pageSize = 10;

}
