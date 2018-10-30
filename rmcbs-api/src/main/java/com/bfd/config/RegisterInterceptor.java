package com.bfd.config;

import com.bfd.interceptor.ExtraInfoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: bing.shen
 * @date: 2018/10/13 17:30
 * @Description:
 */
@Configuration
public class RegisterInterceptor implements WebMvcConfigurer {

    @Autowired
    private ExtraInfoInterceptor extraInfoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*registry.addInterceptor(extraInfoInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/document/*");*/
    }
}
