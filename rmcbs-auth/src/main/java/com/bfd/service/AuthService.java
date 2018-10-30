package com.bfd.service;

import com.bfd.bean.ApiBean;
import com.bfd.bean.AuthInfoBean;

/**
 * @author: bing.shen
 * @date: 2018/8/13 15:28
 * @Description:
 */
public interface AuthService {

    /**
     * 获取token
     * @param clientId
     * @param secret
     * @return
     * @throws Exception
     */
    String getToken(String clientId, String secret);

    /**
     * 获取认证信息
     * @param token
     * @return
     */
    AuthInfoBean getAuthInfo(String token);

    /**
     * 通过url获取API信息
     * @param url
     * @param companyCode
     * @return
     */
    ApiBean getApiInfoByUrl(String url, String companyCode);

}