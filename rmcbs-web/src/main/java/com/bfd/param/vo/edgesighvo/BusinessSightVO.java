package com.bfd.param.vo.edgesighvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 10:10 2018/8/28
 * @Modified by:
 */
@Data
@ApiModel(value = "BusinessSightVO",description = "接口-客户单位监控值对象")
public class BusinessSightVO {


    @ApiModelProperty(value = "客户单位ID")
    private Long id;

    @ApiModelProperty(value = "客户单位Key")
    private String accessKey;

    @ApiModelProperty(value = "平台商名称")
    private String companyName;

    @ApiModelProperty(value = "访问次数")
    private Integer accessCount;

    @ApiModelProperty(value = "访问时间")
    private String accessDay;

}
