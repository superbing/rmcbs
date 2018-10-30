package com.bfd.param.vo.statisticsuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 15:24 2018/9/30
 * @Modified by:
 */
@Data
@ApiModel(value = "CustomerNameVO",description = "客户单位名称返回列表")
public class CustomerNameVO {


    @ApiModelProperty(value = "客户单位ID")
    private Long id;

    @ApiModelProperty(value = "客户单位ID")
    private String companyName;

}
