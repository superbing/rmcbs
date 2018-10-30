package com.bfd.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: chong.chen
 * @Description: IP范围实体类
 * @Date: Created in 10:24 2018/8/2
 * @Modified by:
 */
@Data
public class IpRangeBean {

    @ApiModelProperty(value = "IP范围ID")
    private long id;

    @ApiModelProperty(value = "商务信息ID")
    private long businessId;

    @ApiModelProperty(value = "起始IP")
    private String startIp;

    @ApiModelProperty(value = "终止IP")
    private String endIp;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    private Date createTime;

}
