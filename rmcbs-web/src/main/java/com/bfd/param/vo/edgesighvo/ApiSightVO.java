package com.bfd.param.vo.edgesighvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description: API接口监控返回页面实体类
 * @Date: Created in 15:57 2018/8/27
 * @Modified by:
 */
@Data
@ApiModel(value = "ApiSightVO",description = "API接口监控返回页面实体类")
public class ApiSightVO {

    @ApiModelProperty("APi接口ID")
    private Long id;

    @ApiModelProperty("APi接口名称")
    private String name;

    @ApiModelProperty("APi接口地址")
    private String url;

    @ApiModelProperty("APi接口接口类型")
    private String typeName;

    @ApiModelProperty("平均响应时间")
    private String avgTime;

    @ApiModelProperty("访问总次数")
    private String totalCount;

    @ApiModelProperty("当天访问数")
    private String accessCount;
}
