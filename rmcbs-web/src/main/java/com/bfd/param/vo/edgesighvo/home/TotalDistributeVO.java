package com.bfd.param.vo.edgesighvo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 15:30 2018/9/10
 * @Modified by:
 */
@Data
@ApiModel(value = "TotalDistributeDTO",description = "调用总次数统计")
public class TotalDistributeVO {

    @ApiModelProperty(value = "序列号")
    private int num;

    @ApiModelProperty(value = "时间")
    private String time;

    @ApiModelProperty(value = "调用总次数")
    private long total;

    @ApiModelProperty(value = "调用成功次数")
    private long successTotal;

    @ApiModelProperty(value = "调用失败次数")
    private long failTotal;

}
