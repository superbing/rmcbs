package com.bfd.bean;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chong.chen
 * @Description: 平台商/客户单位实体类
 * @Date: Created in 14:07 2018/7/27
 * @Modified by:
 */
@Data
@ApiModel(value = "CompanyBean", description = "平台商/客户单位信息表")
public class CompanyBean {
    
    @ApiModelProperty("单位id")
    private long id;
    
    @ApiModelProperty("单位名称")
    @NotNull
    private String companyName;
    
    @ApiModelProperty("单位类型")
    @NotNull
    private String companyType;
    
    /**
     * 没有对应到数据库
     */
    @ApiModelProperty("客户单位数")
    private int customerNumber;
    
    @ApiModelProperty("父节点单位id")
    @NotNull
    private long parentCompanyId;
    
    /**
     * 没有对应到数据库
     */
    @ApiModelProperty("父节点单位名称")
    private String parentCompanyName;
    
    @ApiModelProperty("注册地")
    private String registrationPlace;
    
    @ApiModelProperty("单位描述(平台说明)")
    private String companyDescription;
    
    @ApiModelProperty("商业模式")
    private String businessModel;
    
    @ApiModelProperty("状态(1：开启， 0：关闭）")
    private int status;
    
    @ApiModelProperty(value = "单位码，用于生成access_key", example = "bfd")
    private String companyCode;
    
    @ApiModelProperty("accessKey")
    private String accessKey;
    
    @JsonIgnore
    @ApiModelProperty("更新时间")
    private Date updateTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    /**
     * 是否下载，0:未下载；1:已下载；默认0未下载
     */
    @ApiModelProperty("是否下载")
    private Integer downloaded;
    
    /**
     * epub离线数据是否加密，0:未加密；1:加密中；2:加密结束；默认0未加密
     */
    @ApiModelProperty("离线数据是否加密完了")
    private Integer drmEncrypt;
    
}
