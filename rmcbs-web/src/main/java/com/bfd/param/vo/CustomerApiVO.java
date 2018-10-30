package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 14:15 2018/8/9
 * @Modified by:
 */
@Data
@ApiModel(value = "CustomerApiVO", description = "客户单位选择列表VO")
public class CustomerApiVO {

    @ApiModelProperty(value = "上级平台商选择接口集合")
    private List<Long> developerApiList;

    @ApiModelProperty(value = "客户单位选择接口集合")
    private List<Long> customerApiList;
}
