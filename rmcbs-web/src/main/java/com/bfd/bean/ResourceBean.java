package com.bfd.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: bing.shen
 * @Date: 2018/7/30 14:30
 * @Description:
 */
@Data
@ApiModel(value = "ResourceBean", description = "资源信息表")
public class ResourceBean implements Serializable {

    private static final long serialVersionUID = 2011217752854447372L;

    @ApiModelProperty("id主键")
    private Long id;

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("父ID")
    private Long parentId;

    @ApiModelProperty("子菜单")
    private List<ResourceBean> list;

    @ApiModelProperty("是否有权限(1:无权限,0:有权限)")
    private int flag;
}
