package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 15:42 2018/9/28
 * @Modified by:
 */
@Data
@ApiModel(value = "ResourceRecordVO",description = "资源授权是否选择过栏目或数据包选项标注")
public class ResourceRecordVO {

    @ApiModelProperty(value = "栏目数量")
    private Long columnNum;

    @ApiModelProperty(value = "数包数量")
    private Long packageNum;
}
