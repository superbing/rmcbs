package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 14:33 2018/8/17
 * @Modified by:
 */
@Data
@ApiModel(value = "ApiNamesVO",description = "主要存Api名称")
public class ApiNamesVO {


    /**
     * 接口id
     */
    @ApiModelProperty("id主键")
    private Long apiId;

    /**
     * 接口名称
     */
    @ApiModelProperty("接口名称")
    private String apiName;

}
