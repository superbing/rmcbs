package com.bfd.param.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: chong.chen
 * @Description:
 * @Date: Created in 10:35 2018/8/21
 * @Modified by:
 */
@Data
@ApiModel(value = "ResourceVO",description = "资源授权管理保存选择的")
public class ResourceVO {


    @ApiModelProperty(value = "客户单位ID")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "接口ID集合")
    private List<Long> apiIds;

    @ApiModelProperty(value = "栏目ID集合")
    private List<Long> columnIds;

    @ApiModelProperty(value = "数据包ID集合")
    private List<Long> packageIds;

}
