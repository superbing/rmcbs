package com.bfd.param.vo.edgesighvo;

import com.bfd.common.vo.PageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description: 接口-客户单位监控查询结果对象
 * @Date: Created in 10:10 2018/8/28
 * @Modified by:
 */
@Data
@ApiModel(value = "BusinessSightDTO",description = "接口-客户单位监控查询结果对象")
public class BusinessSightDTO {


    @ApiModelProperty(value = "接口ID")
    private Long id;

    @ApiModelProperty(value = "接口名称")
    private String name;

    @ApiModelProperty(value = "接口类型地址")
    private String typeUrl;

    @ApiModelProperty(value = "地址")
    private String url;

    @ApiModelProperty(value = "接口-客户单位监控查询分页结果")
    PageVO<BusinessSightVO> businessSightVOS;
}
