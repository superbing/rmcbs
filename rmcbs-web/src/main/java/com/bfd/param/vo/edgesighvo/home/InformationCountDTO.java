package com.bfd.param.vo.edgesighvo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 14:18 2018/9/7
 * @Modified by:
 */
@Data
@ApiModel(value = "InformationCountDTO",description = "首页图书,栏目等信息展示类")
public class InformationCountDTO {

    @ApiModelProperty(value = "图书总数")
    private Long bookTotal;

    @ApiModelProperty(value = "栏目总数")
    private Long columnTotal;

    @ApiModelProperty(value = "数据包总数")
    private Long packageTotal;

    @ApiModelProperty(value = "客户单位总数")
    private Long BusinessTotal;

    @ApiModelProperty(value = "APi总数")
    private Long apiTotal;
}
