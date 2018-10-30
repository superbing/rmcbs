package com.bfd.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: bing.shen
 * @date: 2018/8/2 14:46
 * @Description:API参数信息
 */
@Data
@ApiModel(value = "ApiParamBean", description = "API参数信息")
public class ApiParamBean implements Serializable {

    private static final long serialVersionUID = -41344598044989528L;

    /**
     * 主键
     */
    @ApiModelProperty("API参数主键")
    private Long id;

    /**
     * API主键
     */
    @ApiModelProperty("API信息主键")
    private Long apiId;

    /**
     * 参数名称
     */
    @ApiModelProperty("参数名称")
    private String name;

    /**
     * 1:String,2:int,3:Long
     */
    @ApiModelProperty("参数类型1:String,2:int,3:Long")
    private int type;

    /**
     * 是否必填
     */
    @ApiModelProperty("是否必填1:必填，0:非必填")
    private int isRequired;

    /**
     * 说明
     */
    @ApiModelProperty("说明")
    private String remark;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;
}
