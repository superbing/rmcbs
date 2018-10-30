package com.bfd.param.vo.edgesighvo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 15:30 2018/9/10
 * @Modified by:
 */
@Data
@ApiModel(value = "TotalDistributeDTO",description = "调用总次数统计")
public class TotalDistributeDTO {


    @ApiModelProperty(value = "调用失败次数")
    private List<TotalDistributeVO> distributeTotal;

    @ApiModelProperty(value = "时(1开启,0关闭)")
    private int hour;

    @ApiModelProperty(value = "天(1开启,0关闭)")
    private int day;

    @ApiModelProperty(value = "周(1开启,0关闭)")
    private int week;

    @ApiModelProperty(value = "月(1开启,0关闭)")
    private int month;
}
