package com.bfd.service;

/**
 * DRM加解密Service
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年8月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface DrmEncryptService {
    
    /**
     * 在线加密返回结果
     * 
     * @param resultJson
     * @see [类、类#方法、类#成员]
     */
    public void onlineResult(String resultJson);
    
    /**
     * 离线加密返回结果
     * 
     * @param resultJson
     * @see [类、类#方法、类#成员]
     */
    public void offlineResult(String resultJson);
    
}
