package com.bfd.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: chong.chen
 * @Description: 客户单位与栏目、对应关系
 * @Date: Created in 16:11 2018/8/3
 * @Modified by:
 */
@Data
public class BusinessColumnBean {


    @ApiModelProperty("id")
    private long id;

    @ApiModelProperty(value = "通用的id")
    private long columnId;

    @ApiModelProperty(value = "客户单位id")
    private long businessId;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    private Date createTime;

}
