package com.bfd.bean;

import com.bfd.param.vo.IpRangeVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author: chong.chen
 * @Description: 单位商务信息实体类
 * @Date: Created in 20:36 2018/8/1
 * @Modified by:
 */
@Data
public class BusinessUnitsBean {

    @ApiModelProperty(value = "单位ID")
    @NotNull
    private long businessId;

    @ApiModelProperty(value = "起始实际")
    @NotNull
    private String startTime;

    @ApiModelProperty(value = "到期时间")
    @NotNull
    private String endTime;

    @ApiModelProperty(value = "终端数")
    private Long terminal;

    @ApiModelProperty(value = "终端状态，默认为0。1：true,0：false")
    private Long terminalStatus;

    @ApiModelProperty(value = "并发数")
    private Long concurrence;

    @ApiModelProperty(value = "并发状态，默认为0。1：true,0：false")
    private Long concurrenceStatus;

    @ApiModelProperty(value = "IP状态，默认为0。1：true,0：false")
    private Long ipStatus;

    @ApiModelProperty(value = "IP范围")
    private List<IpRangeVO> ipRangeList;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    private Date createTime;

}
