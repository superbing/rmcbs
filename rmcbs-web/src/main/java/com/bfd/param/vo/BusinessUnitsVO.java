package com.bfd.param.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author: chong.chen
 * @Description: 单位商务信息返回类
 * @Date: Created in 20:36 2018/8/1
 * @Modified by:
 */
@Data
public class BusinessUnitsVO {

    @ApiModelProperty(value = "单位ID")
    @NotNull
    private long id;

    @ApiModelProperty(value = "单位名称")
    @NotNull
    private String companyName;

    @ApiModelProperty(value = "客户编码")
    private String companyCode;

    @ApiModelProperty(value = "accessKey")
    private String accessKey;

    @ApiModelProperty(value = "父节点(平台商)单位id")
    @NotNull
    private long parentCompanyId;

    @ApiModelProperty(value = "父节点(平台商)单位名称")
    @NotNull
    private String parentCompanyName;

    @ApiModelProperty(value = "起始实际")
    @NotNull
    private String startTime;

    @ApiModelProperty(value = "到期时间")
    @NotNull
    private String endTime;

    @ApiModelProperty(value = "终端数")
    private long terminal;

    @ApiModelProperty(value = "终端数")
    private long entryTerminal;

    @ApiModelProperty(value = "终端状态，默认为0。1：true,0：false")
    private long terminalStatus;

    @ApiModelProperty(value = "并发数")
    private long concurrence;

    @ApiModelProperty("状态(1：开启， 0：关闭）")
    private int status;

    @ApiModelProperty(value = "并发状态，默认为0。1：true,0：false")
    private long concurrenceStatus;

    @ApiModelProperty(value = "IP状态，默认为0。1：true,0：false")
    private long ipStatus;

    @ApiModelProperty(value = "IP范围")
    private List<IpRangeVO> ipRangeList;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

}
