package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 16:41 2018/8/2
 * @Modified by:
 */
@Data
@ApiModel(value = "IpRangeVO", description = "IP范围VO")
public class IpRangeVO {

    @ApiModelProperty(value = "起始IP")
    private String startIp;

    @ApiModelProperty(value = "终止IP")
    private String endIp;
}
