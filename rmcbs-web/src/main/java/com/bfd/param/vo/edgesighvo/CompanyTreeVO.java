package com.bfd.param.vo.edgesighvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 14:57 2018/9/7
 * @Modified by:
 */
@Data
@ApiModel(value = "CompanyTreeVO",description = "客户单位平台商联动树")
public class CompanyTreeVO {

    @ApiModelProperty(value = "客户单位ID")
    private Long id;

    @ApiModelProperty(value = "客户单位名称")
    private String name;

}
