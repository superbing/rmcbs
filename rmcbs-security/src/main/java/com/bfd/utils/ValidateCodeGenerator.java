package com.bfd.utils;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:验证码接口
 */
public interface ValidateCodeGenerator {

    /**
     * 图形验证码实现方法接口
     * @param request
     * @return
     */
    ValidateCode generate(ServletWebRequest request);
}
