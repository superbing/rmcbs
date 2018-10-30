package com.bfd.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 公共皮肤模板实体类
 * @author jiang.liu
 * @date 2018-8-22
 */
@Data
@ApiModel(value = "PublicTemplateBean", description = "公共皮肤模板表")
public class PublicTemplateBean {

    @ApiModelProperty("主键id")
    private int id;

    @NotNull
    @ApiModelProperty("模板id")
    private int templateId;

    @ApiModelProperty("客户端类型")
    private String type;

}
