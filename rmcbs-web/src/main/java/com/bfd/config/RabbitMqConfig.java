package com.bfd.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: bing.shen
 * @date: 2018/9/4 14:27
 * @Description:
 */
@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.pdfQueueName}")
    private String pdfQueueName;

    @Value("${rabbitmq.epubQueueName}")
    private String epubQueueName;

    @Bean
    public Queue pdfQueue() {
        return new Queue(pdfQueueName);
    }

    @Bean
    public Queue epubQueue() {
        return new Queue(epubQueueName);
    }
}
