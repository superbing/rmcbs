package com.bfd.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bfd.utils.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:认证失败处理
 */
@Component("myAuthenticationFailureHandler")
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        logger.info("登录失败");
        String message = exception.getMessage();
        if(exception instanceof BadCredentialsException){
            message = "用户名或密码错误";
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Result result = new Result(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
