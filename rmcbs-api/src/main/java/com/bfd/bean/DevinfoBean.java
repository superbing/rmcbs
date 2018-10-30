package com.bfd.bean;

import lombok.Data;

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

    private String deviceId;

    private String deviceName ;

    private String systemName;

    private String systemVersion;

    private String appVersion;

    private String deviceType;

    private String businessId;

}