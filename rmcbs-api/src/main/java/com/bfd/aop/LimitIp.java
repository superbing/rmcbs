package com.bfd.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: bing.shen
 * @date: 2018/9/15 13:55
 * @Description:限制PC端访问IP(只有PC端使用的接口会用到)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitIp {
}
