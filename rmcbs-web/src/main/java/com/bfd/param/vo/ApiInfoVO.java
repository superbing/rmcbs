package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 10:32 2018/8/9
 * @Modified by:
 */
@Data
@ApiModel(value = "ApiInfoVO", description = "接口查询条件VO")
public class ApiInfoVO {

    @ApiModelProperty(value = "平台商/客户单位ID", required = false)
    private Long companyId;

    @ApiModelProperty(value = "接口ID", required = false)
    private Long id;

    @ApiModelProperty(value = "接口名称", required = false)
    private String name;

    @ApiModelProperty(value = "api分类", required = false)
    private Long apiType;

    /**
     * 1:pc,2:app,3:通用
     */
    @ApiModelProperty(value = "接口使用类型")
    private Integer type;

    @ApiModelProperty(value = "接口地址", required = false)
    private String url;

    @ApiModelProperty(value = "接口状态", required = false)
    private Long status;

    @Min(1)
    @ApiModelProperty(value = "当前页码", required = false, example = "1")
    private int current = 1;

    @Min(1)
    @ApiModelProperty(value = "每页显示条数", required = false, example = "10")
    private int pageSize = 10;
}
