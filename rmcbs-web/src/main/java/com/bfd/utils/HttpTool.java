package com.bfd.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSONObject;

/**
 * HTTP工具类
 * 
 * @author
 * @version [版本号, 2017年03月19日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public class HttpTool {
    
    private int timeOutMills = 60000;
    
    private String httpCharset = "UTF-8";
    
    public HttpTool() {
    }
    
    /**
     * 发送键值对http请求
     * 
     * @param strUrl
     * @param param
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public String post(String strUrl, Map<String, String> param) {
        log.info("post url:" + strUrl);
        log.info("param:" + JSONObject.toJSONString(param));
        
        PostMethod method = null;
        try {
            org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
            method = new PostMethod(strUrl);
            
            // 指定超时时间，避免在对端http服务无响应的场合造成较长等待。
            client.getHttpConnectionManager().getParams().setConnectionTimeout(timeOutMills);
            client.getHttpConnectionManager().getParams().setSoTimeout(timeOutMills);
            
            // Content-type的设置很关键，不要随便修改，否则对端可能取不出报文
            method.addRequestHeader("Content-type", "application/x-www-form-urlencoded;text/xml;charset=" + httpCharset);
            
            if (param != null && param.size() != 0) {
                Iterator<Map.Entry<String, String>> entries = param.entrySet().iterator();
                NameValuePair[] params = new NameValuePair[param.size()];
                for (int i = 0; entries.hasNext(); i++) {
                    Map.Entry<String, String> entry = entries.next();
                    params[i] = new NameValuePair(entry.getKey(), entry.getValue());
                    method.setRequestBody(params);
                }
            } else {
                method.setRequestBody(new NameValuePair[] {});
            }
            
            client.executeMethod(method);
            int statusCode = method.getStatusCode();
            log.info(String.format("HTTP Status Code:%s", statusCode));
            
            String respStr = method.getResponseBodyAsString();
            log.info(String.format("HTTP result:%s", respStr));
            
            if (HttpStatus.OK.value() != statusCode) {
                throw new RuntimeException("服务调用失败，请联系管理员！");
            }
            return respStr;
        } catch (Exception e) {
            log.info(String.format("HTTP请求失败, The URL is %s, The param is %s", strUrl, JSONObject.toJSON(param)));
            throw new RuntimeException("服务调用失败，请联系管理员！");
        } finally {
            if (null != method) {
                // 释放连接
                method.releaseConnection();
                method = null;
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        // String postKeyValue = HttpTool.postKeyValue("http://service.data.xinhua-news.cn/data/base/getToken",
        // "appId=bfd&accessKey=DE7E07D5B47B4384AEFCFB14030AD34D");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> param = new HashMap<String, String>();
        param.put("endTime", sdf.format(new Date()));
        param.put("fields", "userSeqId");
        param.put("access_token", "IlSi66AyK4JHn/my+1uFevZVyS3CmLnjsknpo6CDI9njd6DhhYHmb1K/FujAvgKC5GBuh2ffw6MpD1t8lpPZ9PgbfndsyHZyX67gKFpZ52c=");
        
        HttpTool httpTool = new HttpTool();
        String post = httpTool.post("http://service.data.xinhua-news.cn/data/userinfo/batchQuerySnUsers", param);
        
        System.out.println(post);
        System.out.println();
        
        String post2 = httpTool.post("http://service.data.xinhua-news.cn/data/userinfo/batchQuerySwUsers", param);
        System.out.println(post2);
    }
    
}