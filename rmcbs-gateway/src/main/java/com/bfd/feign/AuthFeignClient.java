package com.bfd.feign;

import com.bfd.bean.ApiBean;
import com.bfd.bean.AuthInfoBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: bing.shen
 * @date: 2018/8/13 18:34
 * @Description:
 */
@FeignClient("auth")
public interface AuthFeignClient {

    /**
     * getAuthInfo
     * @param token
     * @return
     */
    @RequestMapping(value = "/getAuthInfo", method = RequestMethod.GET)
    AuthInfoBean getAuthInfo(@RequestParam("token") String token);

    /**
     * getApiInfo
     * @param url
     * @param companyCode
     * @return
     */
    @RequestMapping(value = "/getApiInfo", method = RequestMethod.GET)
    ApiBean getApiInfo(@RequestParam("url") String url, @RequestParam("companyCode") String companyCode);
}
