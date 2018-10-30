package com.bfd.param.vo.serviceaudit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: chong.chen
 * @Description: 接口-客户单位监控查询结果对象
 * @Date: Created in 10:10 2018/8/28
 * @Modified by:
 */
@Data
@ApiModel(value = "ServiceAuditQO",description = "服务审计查询条件")
public class ServiceAuditQO {


    @ApiModelProperty(value = "客户单位ID")
    private long businessId;

    @ApiModelProperty(value = "接口类型")
    private long apiType;

    @ApiModelProperty(value = "接口ID")
    private long apiId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @Min(1)
    @ApiModelProperty(value = "当前页码", required = true, example = "1")
    @NotNull
    private int current;

    @Min(1)
    @NotNull
    @ApiModelProperty(value = "每页显示条数", required = true, example = "10")
    private int pageSize;

}