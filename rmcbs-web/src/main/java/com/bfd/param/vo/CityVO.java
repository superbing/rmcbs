package com.bfd.param.vo;

import com.bfd.bean.CityBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 10:59 2018/9/4
 * @Modified by:
 */
@Data
@ApiModel(value = "CityVO",description = "城市返回对象")
public class CityVO {


    @ApiModelProperty(value = "唯一主键ID")
    private Integer id;

    @ApiModelProperty(value = "省市名称")
    private String name;

    @ApiModelProperty(value = "省下面的市")
    private List<CityBean> children;
}
