package com.bfd.bean;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: bing.shen
 * @date: 2018/8/2 14:42
 * @Description:API基本信息
 */
@Data
@ApiModel(value = "ApiInfoBean", description = "API基本信息")
public class ApiInfoBean implements Serializable {
    
    private static final long serialVersionUID = -77584107648588272L;
    
    /**
     * 接口id
     */
    @ApiModelProperty("id主键")
    private Long id;
    
    /**
     * 接口名称
     */
    @NotNull(message = "接口名称不能为空")
    @ApiModelProperty("接口名称")
    private String name;
    
    /**
     * 
     */
    @NotNull(message = "api分类不能为空")
    @ApiModelProperty("api分类")
    private Long apiType;
    
    /**
     * 接口地址
     */
    @NotNull(message = "接口地址不能为空")
    @ApiModelProperty("接口地址")
    private String url;
    
    /**
     * 请求方法：post、get、get,post
     */
    @NotNull(message = "请求方法不能为空")
    @ApiModelProperty("请求方法")
    private String callMethod;
    
    /**
     * 服务描述
     */
    @ApiModelProperty("服务描述")
    private String description;
    
    /**
     * 输入参数，json串：[{"words":"appId","type":"string","isRequired":1,"desc":"客户id，默认传Cxinhua即可","isMust":false}]
     */
    @ApiModelProperty(value="输入参数", example="[{\"words\":\"cnmlName\",\"type\":\"string\",\"isRequired\":0,\"desc\":\"cnml文件名 cnmlName和docID至少传一个，docID优先\",\"isMust\":false}]")
    private String inParam;
    
    /**
     * 输出参数
     */
    @NotNull(message = "输出参数不能为空")
    @ApiModelProperty("输出参数")
    private String outParam;
    
    /**
     * 输出参数说明
     */
    @NotNull(message = "输出参数说明不能为空")
    @ApiModelProperty("输出参数说明")
    private String outParamDesc;
    
    /**
     * 调用示例
     */
    @ApiModelProperty("调用示例")
    private String callExample;
    
    /**
     * 状态：1:启用,0:未启用
     */
    @NotNull(message = "启用状态不能为空")
    @ApiModelProperty("启用状态")
    private Integer status;

    /**
     * 1:pc,2:app,3:通用
     */
    @NotNull(message = "接口使用类型不能为空")
    @ApiModelProperty("接口使用类型")
    private Integer type;

    /**
     * 是否开启权限1:启用,0:未启用
     */
    @NotNull(message = "开启权限不能为空")
    @ApiModelProperty("开启权限")
    private Integer auth;
    
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;
    
}
