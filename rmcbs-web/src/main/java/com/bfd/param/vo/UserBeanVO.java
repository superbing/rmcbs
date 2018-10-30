package com.bfd.param.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: chong.chen
 * @Date: 2018/10/20 13:32
 * @Description: 用户信息返回列表
 */
@Data
@ApiModel(value = "UserBeanVO", description = "用户信息返回表")
public class UserBeanVO{
    

    @ApiModelProperty("id主键")
    private Long id;
    
    @ApiModelProperty("账号")
    private String account;
    
    @ApiModelProperty("是否可用")
    private Integer enabled;
    
    @NotNull
    @ApiModelProperty("姓名")
    private String name;
    
    @ApiModelProperty("密码")
    private String password;
    
    @JsonIgnore
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    @JsonIgnore
    @ApiModelProperty("创建人")
    private Long createUser;
    
    @ApiModelProperty("备注说明")
    private String remark;
    
    @ApiModelProperty("最后登录时间")
    private Date lastTime;
    
    @ApiModelProperty("最后登录IP")
    private String lastIp;
    
    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

}
