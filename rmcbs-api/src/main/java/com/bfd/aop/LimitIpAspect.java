package com.bfd.aop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bfd.config.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

/**
 * @author: bing.shen
 * @date: 2018/9/15 14:01
 * @Description:IP限制切面
 */
@Aspect
@Component
@Slf4j
public class LimitIpAspect {
    
    private final String preKey = "limit_ip_";
    
    @Pointcut("@annotation(com.bfd.aop.LimitIp)")
    public void methodAspect() {
    }
    
    @Before("methodAspect()")
    public void before() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getParameter("ip");
        log.info("调用IP地址:" + ip);
        String value = RedisUtil.get(preKey.concat(request.getParameter("businessId")));
        
        // 如果设置了IP限制
        if (StringUtils.isNotBlank(value)) {
            JSONArray jsonArray = JSONArray.parseArray(value);
            boolean flag = false;
            for (Iterator<?> iterator = jsonArray.iterator(); iterator.hasNext();) {
                JSONObject jsonObject = (JSONObject)iterator.next();
                if (isValidRange((String)jsonObject.get("startIp"), (String)jsonObject.get("endIp"), ip)) {
                    log.info("IP认证通过");
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new RuntimeException("此IP无权访问");
            }
        }
    }
    
    private static boolean isValidRange(String ipStart, String ipEnd, String ipToCheck) {
        try {
            long ipLo = ipToLong(InetAddress.getByName(ipStart));
            long ipHi = ipToLong(InetAddress.getByName(ipEnd));
            long ipCheck = ipToLong(InetAddress.getByName(ipToCheck));
            return (ipCheck >= ipLo && ipCheck <= ipHi);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static long ipToLong(InetAddress ip) {
        long result = 0;
        byte[] ipAdds = ip.getAddress();
        for (byte b : ipAdds) {
            result <<= 8;
            result |= b & 0xff;
        }
        return result;
    }
    
    public static void main(String[] args) {
        System.out.println(isValidRange("192.168.174.1", "192.168.174.255", "192.168.174.167"));
    }
}
