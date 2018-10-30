package com.bfd.param.vo.statisticsuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 10:10 2018/9/29
 * @Modified by:
 */
@Data
@ApiModel(value = "UserQueryQO",description = "用户使用查询条件类")
public class UserQueryQO {

    @ApiModelProperty(value = "单位id")
    private String id;

    @ApiModelProperty(value = "单位名称")
    private String companyName;

    @Min(1)
    @ApiModelProperty(value = "当前页码", required = true, example = "1")
    @NotNull
    private int current;

    @Min(1)
    @NotNull
    @ApiModelProperty(value = "每页显示条数", required = true, example = "10")
    private int pageSize;

}