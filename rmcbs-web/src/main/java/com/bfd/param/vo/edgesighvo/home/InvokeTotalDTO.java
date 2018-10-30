package com.bfd.param.vo.edgesighvo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 17:59 2018/9/7
 * @Modified by:
 */
@Data
@ApiModel(value = "InvokeTotalDTO",description = "调用信息统计类")
public class InvokeTotalDTO {

    @ApiModelProperty(value = "调用总次数")
    private long invokeTotal;

    @ApiModelProperty(value = "调用成功次数")
    private long invokeSuccessTotal;

    @ApiModelProperty(value = "调用失败次数")
    private long invokeFailTotal;
}
