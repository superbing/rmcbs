package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: chong.chen
 * @date: 2018/8/9 14:42
 * @Description:API基本信息
 */
@Data
@ApiModel(value = "ApiVo", description = "API信返回VO")
public class ApiVo {

    /**
     * 接口id
     */
    @ApiModelProperty("id主键")
    private Long id;

    /**
     * 接口名称
     */
    @ApiModelProperty("接口名称")
    private String name;
    
    /**
     * api分类
     */
    @ApiModelProperty("api分类")
    private Long apiType;

    @ApiModelProperty("接口适用类型")
    private Long type;
    
    /**
     * 接口地址
     */
    @ApiModelProperty("接口地址")
    private String url;
    
    /**
     * 服务描述
     */
    @ApiModelProperty("服务描述")
    private String description;
    
    /**
     * 状态：1:启用,0:未启用
     */
    @ApiModelProperty("启用状态 1:启用,0:未启用")
    private Integer status;
    
    /**
     * 是否开启权限1:启用,0:未启用
     */
    @ApiModelProperty("开启权限 1:启用,0:未启用")
    private Integer auth;

    @ApiModelProperty(value = "是否置灰，1置灰，0不置灰")
    private Integer setAsh;

    @ApiModelProperty(value = "是否选中，1选中，0不选择")
    private Integer checked;

}
