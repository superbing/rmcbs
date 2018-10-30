package com.bfd.param.vo.edgesighvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 16:29 2018/8/27
 * @Modified by:
 */
@Data
@ApiModel(value = "CompanySightQO",description = "接口-平台商监控查询条件对象")
public class CompanySightQO {


    @ApiModelProperty(value = "平台商ID")
    private Long companyId;

    @ApiModelProperty(value = "接口ID")
    @NotNull
    private Long apiId;

    @ApiModelProperty(value = "起始实际",example = "2018-01-01")
    private String startTime;

    @ApiModelProperty(value = "到期时间",example = "2019-01-01")
    private String endTime;

    @Min(1)
    @ApiModelProperty(value = "当前页码", required = true, example = "1")
    @NotNull
    private int current;

    @Min(1)
    @NotNull
    @ApiModelProperty(value = "每页显示条数", required = true, example = "10")
    private int pageSize;
}
