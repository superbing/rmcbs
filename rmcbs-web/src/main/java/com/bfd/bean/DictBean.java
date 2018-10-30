package com.bfd.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description: 字典对象
 * @Date: Created in 11:05 2018/8/7
 * @Modified by:
 */
@Data
public class DictBean {

    @ApiModelProperty("ID")
    @JsonIgnore
    private long id;

    @ApiModelProperty("编码")
    private int code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    @JsonIgnore
    private String type;

}
