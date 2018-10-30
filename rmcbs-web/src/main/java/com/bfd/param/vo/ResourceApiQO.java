package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 18:35 2018/10/13
 * @Modified by:
 */
@ApiModel(value = "ResourceApiQO",description = "服务授权管理选择接口接受对象")
@Data
public class ResourceApiQO {

    @ApiModelProperty(value = "接口ID")
    private Long id;

    @ApiModelProperty(value = "接口名称")
    private String name;

    @ApiModelProperty(value = "接口使用类型")
    private Integer type;
}
