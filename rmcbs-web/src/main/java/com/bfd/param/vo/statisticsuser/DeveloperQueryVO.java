package com.bfd.param.vo.statisticsuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 14:21 2018/9/28
 * @Modified by:
 */
@ApiModel(value = "DeveloperQueryVO",description = "用户使用查询平台商返回类")
@Data
public class DeveloperQueryVO {

    @ApiModelProperty(value = "平台商ID")
    private Long id;

    @ApiModelProperty(value = "单位名称")
    private String companyName;

    @ApiModelProperty("客户单位数")
    private Long customerNumber;

    @ApiModelProperty(value = "终端数与使用比例")
    private String terminalProportion;

    @ApiModelProperty(value = "录入终端数")
    @JsonIgnore
    private long entryTerminal;

    @ApiModelProperty(value = "IP单位IP数")
    private Long ipNum;

    @ApiModelProperty(value = "接口数")
    private Long apiNum;


}
