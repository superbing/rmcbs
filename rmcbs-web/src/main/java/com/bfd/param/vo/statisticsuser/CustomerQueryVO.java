package com.bfd.param.vo.statisticsuser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 14:21 2018/9/28
 * @Modified by:
 */
@ApiModel(value = "CustomerQueryVO",description = "用户使用查询客户单位返回类")
@Data
public class CustomerQueryVO {

    @ApiModelProperty(value = "客户单位ID")
    private Long id;

    @ApiModelProperty(value = "单位名称")
    private String companyName;

    @ApiModelProperty(value = "终端数与使用比例")
    private String terminalProportion;

    @ApiModelProperty(value = "限制并发数")
    private Long concurrence;

    @ApiModelProperty(value = "录入终端数")
    @JsonIgnore
    private long entryTerminal;

    @ApiModelProperty(value = "IP单位IP数")
    private Long ipNum;

    @ApiModelProperty(value = "接口数")
    private Long apiNum;

    @ApiModelProperty(value = "起始实际")
    private String startTime;

    @ApiModelProperty(value = "到期时间")
    private String endTime;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;


}
