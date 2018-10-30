package com.bfd.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: bing.shen
 * @Date: 2018/7/30 11:15
 * @Description:角色信息表
 */
@Data
@ApiModel(value = "RoleBean", description = "角色信息表")
public class RoleBean implements Serializable {

    private static final long serialVersionUID = 6410025147502229611L;

    @ApiModelProperty("id主键")
    private Long id;

    @NotNull
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("说明")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty("创建人")
    private Long createUser;

}
