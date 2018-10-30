package com.bfd.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 个性化皮肤模板实体类
 * @author jiang.liu
 * @date 2018-8-22
 */
@Data
@ApiModel(value = "PrivateTemplateBean", description = "个性化皮肤模板表")
public class PrivateTemplateBean {

    @ApiModelProperty("主键id")
    private int id;

    @NotNull
    @ApiModelProperty("皮肤颜色")
    private String skin;

    @NotNull
    @ApiModelProperty("单位id")
    private long companyId;
}
