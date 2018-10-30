package com.bfd.utils;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
public class KafkaProducer {

    private static Producer<Integer, String> producer;

    @Value("${kafka.broker.list}")
    private static String brokerList;

    @Value("${kafka.broker.list}")
    public void setBrokerList(String brokerList) {
        KafkaProducer.brokerList = brokerList;
    }

    public static void init() {
        try {
            Properties props = new Properties();
            props.put("metadata.broker.list", brokerList);
            props.put("serializer.class", "kafka.serializer.StringEncoder");
            ProducerConfig config = new ProducerConfig(props);
            log.info("brokerList=" + config.brokerList());
            log.info("props=" + config.props().toString());
            producer = new Producer<>(config);
        } catch (Exception ex) {
            log.error("初始化KAFKA异常", ex);
        }
    }

    public static void send(String topic, String msg) {
        log.info("send data to Kafka json=" + msg);
        try {
            if(producer==null){
                init();
            }
            producer.send(new KeyedMessage<>(topic, msg));
        } catch (Exception e) {
            log.error("向KAFKA发送消息异常", e);
            try {
                producer.close();
            } catch (Exception ex) {
            }
        }
    }

}
