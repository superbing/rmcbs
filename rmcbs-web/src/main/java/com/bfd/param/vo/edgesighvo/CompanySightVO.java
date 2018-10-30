package com.bfd.param.vo.edgesighvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 16:29 2018/8/27
 * @Modified by:
 */
@Data
@ApiModel(value = "CompanySightVO",description = "接口-平台商监控值对象")
public class CompanySightVO {


    @ApiModelProperty(value = "平台商ID")
    private Long id;

    @ApiModelProperty(value = "平台商名称")
    private String companyName;

    @ApiModelProperty(value = "访问次数")
    private String accessCount;

    @ApiModelProperty(value = "访问时间")
    private String accessDay;

}
