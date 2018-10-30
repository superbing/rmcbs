package com.bfd.param.vo.statisticsvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 * @Date:
 * @Modified by:
 */
@Data
@ApiModel(value = "TimeRangeParamVO",description = "时间开始结束参数对象")
public class TimeRangeParamVO {

    @ApiModelProperty(value = "开始时间")
    @NotNull
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull
    private String endTime;

    @ApiModelProperty(value = "时间类型")
    @NotNull
    private String timeType;

    @ApiModelProperty(value = "分类id")
    @NotNull
    private long id;

    @ApiModelProperty(value = "分类id")
    private String queryType;

}
