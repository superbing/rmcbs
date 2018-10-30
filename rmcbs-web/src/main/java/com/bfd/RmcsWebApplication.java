package com.bfd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * RmcsWebApplication启动类
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年7月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@SpringBootApplication
@EnableAsync
public class RmcsWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(RmcsWebApplication.class, args);
    }
}
