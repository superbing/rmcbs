package com.bfd.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 个性化数据包实体类
 * @author jiang.liu
 * @date 2018-08-02
 */
@Data
@ApiModel(value = "PrivateDataPackageBean", description = "个性化数据包基础信息表")
public class PrivateDataPackageBean {

    @ApiModelProperty("个性化数据包id")
    private long id;

    @NotNull
    @ApiModelProperty("单位id")
    private long businessId;

    @NotNull
    @ApiModelProperty("个性化数据包名称")
    private String name;

    @ApiModelProperty("个性化数据包别名")
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

