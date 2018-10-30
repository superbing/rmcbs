package com.bfd.bean.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * DevinfoBean
 *
 * @author 姓名 工号
 * @version [版本号, 2018年9月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
public class DevinfoBean {

    private int num;

    private String deviceId;

    private String deviceName ;

    private String systemName;

    private String systemVersion;

    private String appVersion;

    private String deviceType;

    private String businessId;

    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

}