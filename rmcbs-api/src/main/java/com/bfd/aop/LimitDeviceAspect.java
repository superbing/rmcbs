package com.bfd.aop;

import com.alibaba.fastjson.JSONObject;
import com.bfd.utils.KafkaProducer;
import com.bfd.config.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

/**
 * @author: bing.shen
 * @date: 2018/9/15 14:01
 * @Description:设备信息限制切面
 */
@Aspect
@Component
@Slf4j
public class LimitDeviceAspect {

    private final String limitDevicePreKey = "limit_device_";

    private final String deviceSetPreKey = "device_set_";

    private final String luaStr = new StringBuffer()
            .append("local n = tonumber(ARGV[1]) ")
            .append("if not n or n == 0 then return 0 end ")
            .append("local vals = redis.call('HMGET', KEYS[1], 'Total', 'Booked');")
            .append("local total = tonumber(vals[1]) local blocked =tonumber(vals[2]) ")
            .append("if not total or not blocked then return 2 end ")
            .append("if blocked + n <= total then redis.call('HINCRBY', KEYS[1], 'Booked', n) ")
            .append("return n end return 0;").toString();


    @Value("${es.rms_device_info.indexName}")
    private String indexName;

    @Value("${es.rms_device_info.indexType}")
    private String indexType;

    @Value("${kafka.device.topic}")
    private String deviceTopic;

    @Value("${epub.key.dir}")
    private String keyDir;

    @Pointcut("@annotation(com.bfd.aop.LimitDevice)")
    public void methodAspect() {
    }

    @Before("methodAspect()")
    public void before() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String devInfo = request.getParameter("devInfo");
        if (StringUtils.isBlank(devInfo)) {
            throw new RuntimeException("设备信息不能为空！");
        }
        String businessId = request.getParameter("businessId");
        String limitDeviceKey = limitDevicePreKey.concat(businessId);
        String value = RedisUtil.hget(limitDeviceKey, "Total");
        JSONObject obj = JSONObject.parseObject(devInfo);
        String deviceId = obj.getString("deviceId");
        obj.put("businessId", businessId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        obj.put("createTime", sdf.format(System.currentTimeMillis()));
        // 如果设置了设备限制
        if (StringUtils.isNotBlank(value)) {
            String deviceSetKey = deviceSetPreKey.concat(businessId);
            log.info("调用设备信息:" + devInfo);
            Long i = RedisUtil.sadd(deviceSetKey, deviceId);
            //如果set集合中不存在则返回为1
            if (i == 1L) {
                // 设备id不在ES设备列表里面，判断ES设备数是否超过设置上限（调用redis接口，返回1则插入es并且验证通过，返回0则验证不通过）
                int k = Integer.valueOf(RedisUtil.eval(luaStr, 1, limitDeviceKey, "1").toString());
                log.info("调用redis接口，返回结果：" + i);
                if (k > 0) {
                    KafkaProducer.send(deviceTopic,obj.toString());
                } else {
                    RedisUtil.srem(deviceSetKey, deviceId);
                    throw new RuntimeException("设备数超出限制！");
                }
            } else {
                KafkaProducer.send(deviceTopic,obj.toString());
            }
        } else {
            KafkaProducer.send(deviceTopic,obj.toString());
        }
    }
}
