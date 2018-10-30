package com.bfd.param.vo.edgesighvo;

import com.bfd.common.vo.PageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 16:29 2018/8/27
 * @Modified by:
 */
@Data
@ApiModel(value = "ApiSightQO",description = "接口-平台商监控查询结果对象")
public class CompanySightDTO {


    @ApiModelProperty(value = "接口ID")
    private Long id;

    @ApiModelProperty(value = "接口名称")
    private String name;

    @ApiModelProperty(value = "接口类型地址")
    private String typeUrl;

    @ApiModelProperty(value = "地址")
    private String url;

    @ApiModelProperty(value = "接口-平台商监控查询分页结果")
    PageVO<CompanySightVO> CompanySightVOS;
}
