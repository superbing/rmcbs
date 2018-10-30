package com.bfd.param.vo.serviceaudit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @Author: chong.chen
 * @Description: 接口-客户单位监控查询结果对象
 * @Date: Created in 10:10 2018/8/28
 * @Modified by:
 */
@Data
@ApiModel(value = "ServiceAuditVO",description = "服务审计返回结果对象")
public class ServiceAuditVO {


    @ApiModelProperty(value = "接口名称")
    private String name;

    @ApiModelProperty(value = "接口地址")
    private String url;

    @ApiModelProperty(value = "接口类型")
    private String apiType;

    @ApiModelProperty(value = "单位名称")
    private String companyName;

    private Long apiId;

    private String companyCode;

    private Long businessId;

    @ApiModelProperty(value = "访问IP")
    private String ip;

    @ApiModelProperty(value = "访问时间")
    private String addTime;

    @ApiModelProperty(value = "状态")
    private String message;

    @ApiModelProperty(value = "消息")
    private Integer status;

}