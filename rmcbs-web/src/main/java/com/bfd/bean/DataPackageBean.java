package com.bfd.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 数据包实体类
 * @author jiang.liu
 * @date 2018-7-25
 */
@Data
@ApiModel(value = "DataPackageBean", description = "数据包基础信息表")
public class DataPackageBean {

    @ApiModelProperty("数据包id")
    private long id;

    @NotNull
    @ApiModelProperty("数据包名称")
    private String name;

    @ApiModelProperty("数据包别名")
    private String aliasName;

    @NotNull
    @ApiModelProperty("父节点id")
    private long parentId;

    @ApiModelProperty("使用类型")
    private int type;

    @ApiModelProperty("排序字段")
    private int sort;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建人")
    private long createUser;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("更新人")
    private long updateUser;

    private int bookNumber;

}
