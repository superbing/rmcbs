package com.bfd.param.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 *  @Author: chong.chen
 *  @Description: 单位查询条件VO
 *  @Date: 15:58 2018/8/7
 */
@Data
@ApiModel(value = "CompanyVO", description = "单位查询条件VO")
public class CompanyVO {

    @ApiModelProperty(value = "平台商ID", required = false)
    private String companyId;

    @ApiModelProperty(value = "单位名称", required = false)
    private String companyName;

    @ApiModelProperty(value = "客户编码")
    private String companyCode;
    
    @Min(1)
    @ApiModelProperty(value = "当前页码", required = false, example = "1")
    private int current = 1;
    
    @Min(1)
    @ApiModelProperty(value = "每页显示条数", required = false, example = "10")
    private int pageSize = 10;

    @ApiModelProperty(value = "客户单位/平台商标注（请无视它）", required = false)
    @JsonIgnore
    private int isDevelopers;

    @ApiModelProperty("排序字段")
    public String sortField;

    @ApiModelProperty("排序类型（正序：asc，倒序：desc）")
    public String sortType;
    
}
