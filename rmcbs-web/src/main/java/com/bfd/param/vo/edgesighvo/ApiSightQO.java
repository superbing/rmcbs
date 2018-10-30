package com.bfd.param.vo.edgesighvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 16:29 2018/8/27
 * @Modified by:
 */
@Data
@ApiModel(value = "ApiSightQO",description = "API接口监控查询条件对象")
public class ApiSightQO {


    @ApiModelProperty(value = "接口类型ID")
    private Long apiTypeId;

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "今天日期")
    @JsonIgnore
    private String today;

    @Min(1)
    @ApiModelProperty(value = "当前页码", required = true, example = "1")
    @NotNull
    private int current;

    @Min(1)
    @NotNull
    @ApiModelProperty(value = "每页显示条数", required = true, example = "10")
    private int pageSize;
}
