package com.bfd.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 11:00 2018/9/4
 * @Modified by:
 */
@Data
@ApiModel(value = "CityBean",description = "省市对象")
public class CityBean {


    @ApiModelProperty(value = "唯一主键ID")
    private Integer id;

    @ApiModelProperty(value = "省市名称")
    private String name;

    @ApiModelProperty(value = "父ID")
    private Integer parentId;


}
