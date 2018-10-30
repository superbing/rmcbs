package com.bfd.param.vo.statisticsvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 资源使用查询-首页查询参数对象
 *
 * @author 姓名 工号
 * @version [版本号, 2018年9月28日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ApiModel(value = "ResourceParamVO", description = "资源使用查询-首页查询参数对象")
public class ResourceParamVO {

    @ApiModelProperty(value = "分类名称", required = false)
    private String name;

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
