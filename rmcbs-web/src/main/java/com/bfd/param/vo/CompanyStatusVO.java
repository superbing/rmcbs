package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

/**
 * @Author: chong.chen
 * @Description: 平台商/客户单位状态修改类
 * @Date: Created in 15:45 2018/8xi/16
 * @Modified by:
 */
@Data
@ApiModel(value = "CompanyStatusVO", description = "平台商/客户单位状态修改类")
public class CompanyStatusVO {

    @ApiModelProperty(value = "ID")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "父节点单位id")
    @NotNull
    private Long parentCompanyId;

    @ApiModelProperty("状态(1：开启， 0：关闭）")
    @NotNull
    private int status;

}
