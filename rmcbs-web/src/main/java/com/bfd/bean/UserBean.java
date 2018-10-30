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
 * @Date: 2018/7/30 13:32
 * @Description:用户信息表
 */
@Data
@ApiModel(value = "UserBean", description = "用户信息表")
public class UserBean implements Serializable {
    
    private static final long serialVersionUID = 3198256594253067086L;
    
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
    
    @Override
    public String toString() {
        return "UserBean [id=" + id + ", account=" + account + ", enabled=" + enabled + ", name=" + name + ", password=" + password + ", createTime=" + createTime + ", createUser=" + createUser + ", remark=" + remark + ", lastTime=" + lastTime + ", lastIp=" + lastIp + ", roleId=" + roleId + "]";
    }
    
}
