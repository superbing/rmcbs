package com.bfd.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: chong.chen
 * @Description: 接口访问统计实体类
 * @Date: Created in 14:43 2018/8/27
 * @Modified by:
 */
@Data
@ApiModel(value = "LogStatistics",description = "接口访问统计实体类")
public class LogStatistics {

    @ApiModelProperty(value = "唯一ID")
    private Long id;

    @ApiModelProperty(value = "接口ID")
    private Long apiId;

    @ApiModelProperty("平台商ID")
    private Long companyId;

    @ApiModelProperty("客户单位ID")
    private Long businessId;

    @ApiModelProperty("统计日期")
    private Date accessDay;

    @ApiModelProperty("当天访问总次数")
    private int accessCount;

    @ApiModelProperty("成功总次数")
    private int accessSuccessCount;

    @ApiModelProperty("响应总时间")
    private int useTime;

    @ApiModelProperty("最后更新时间")
    private Date updateTime;



}
