package com.bfd.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:验证码异常
 */
public class ValidateCodeException extends AuthenticationException  {

    /**
     * 实现一个父类的构造方法
     * @param msg
     */
    public ValidateCodeException(String msg) {
        super(msg);
    }
}
