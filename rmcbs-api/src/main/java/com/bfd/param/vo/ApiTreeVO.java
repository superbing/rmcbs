package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 14:19 2018/8/17
 * @Modified by:
 */
@Data
@ApiModel(value = "ApiTreeVO",description = "接口文档中树的返回类")
public class ApiTreeVO {

    @ApiModelProperty(value = "Api类型ID")
    private Long id;

    @ApiModelProperty(value = "Api类型名称")
    private String typeName;

    @ApiModelProperty(value = "该类型下的Api集合")
    private List<ApiNamesVO> apiVOList;

}
