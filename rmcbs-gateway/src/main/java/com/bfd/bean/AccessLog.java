package com.bfd.bean;

import lombok.Data;

@Data
public class AccessLog {
    
    /**
     * 客户单位id
     */
    private Long businessId;
    
    /**
     * 父ID
     */
    private Long companyId;
    
    /**
     * 客户单位编码
     */
    private String companyCode;
    
    /**
     * apiId
     */
    private long apiId;
    
    /**
     * 调用url
     */
    private String url;
    
    /**
     * 接口分类
     */
    private long apiType;
    
    /**
     * 接口入参
     */
    private String inParam;
    
    /**
     * 接口调用耗时，单位毫秒
     */
    private long useTime;
    
    /**
     * 调用ip
     */
    private String ip;
    
    /**
     * 入库时间
     */
    private long addTime;
    
    private int status;
    
    private String message;
    
    private String accessKey;
    
}
