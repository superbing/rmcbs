package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 19:10 2018/9/13
 * @Modified by:
 */
@Data
@ApiModel(value = "InsertApiVO",description = "批量插入用到的对象")
public class InsertApiVO {

    @ApiModelProperty(value = "客户单位ID")
    private Long businessId;

    @ApiModelProperty(value = "接口ID")
    private Long apiId;
}
