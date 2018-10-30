package com.bfd.param.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 10:13 2018/8/20
 * @Modified by:
 */
@Data
@ApiModel(value = "AddDeveloperVo", description = "平台商/客户单位信息表")
public class AddDeveloperVO {

    @ApiModelProperty("单位id")
    private Long id;

    @ApiModelProperty("单位名称")
    @NotNull
    private String companyName;

    @ApiModelProperty("单位类型")
    @NotNull
    private String companyType;

    @ApiModelProperty("注册地")
    private String registrationPlace;

    @ApiModelProperty("单位描述(平台说明)")
    private String companyDescription;

    @ApiModelProperty("商业模式")
    private String businessModel;

    @ApiModelProperty(value = "用户自定义信息")
    private String extraData;

    @ApiModelProperty("用户选择的ID集合")
    private List<Long> apiIds;



}
