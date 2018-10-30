package com.bfd.param.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 18:47 2018/9/7
 * @Modified by:
 */
@Data
@ApiModel(value = "TimeRangeVO",description = "时间开始结束返回对象")
public class TimeRangeVO {

    @ApiModelProperty(value = "开始时间")
    @NotNull
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull
    private String endTime;

    @ApiModelProperty(value = "时间类型")
    @NotNull
    private String timeType;

    @ApiModelProperty(value = "平台商ID,不用可以不传,如果查客户单位统计信息,")
    private long companyId;


    @ApiModelProperty(value = "后端用来判断是平台商还是客户单位的,true平台商,false客户单位")
    @JsonIgnore
    private Boolean whether;

}
