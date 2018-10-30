package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 16:44 2018/9/14
 * @Modified by:
 */
@Data
@ApiModel(value = "ChangePasswordQO",description = "修改密码接受对象")
public class ChangePasswordQO {

    @ApiModelProperty(value = "用户ID")
    private long id;

    @ApiModelProperty(value = "原密码")
    @NotNull
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @NotNull
    private String newPassword;

}
