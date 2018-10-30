package com.bfd.param.vo.edgesighvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 10:10 2018/8/28
 * @Modified by:
 */
@Data
@ApiModel(value = "DayCountSightQO",description = "当天访问数条件对象")
public class DayCountSightQO {


    @ApiModelProperty(value = "客户单位ID")
    private Long BusinessId;

    @ApiModelProperty(value = "接口ID")
    @NotNull
    private Long apiId;

    @ApiModelProperty(value = "访问时间")
    private String accessDay;

    @Min(1)
    @ApiModelProperty(value = "当前页码", required = true, example = "1")
    @NotNull
    private int current;

    @Min(1)
    @NotNull
    @ApiModelProperty(value = "每页显示条数", required = true, example = "10")
    private int pageSize;

}
