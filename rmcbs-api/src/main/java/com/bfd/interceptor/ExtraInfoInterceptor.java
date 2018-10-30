package com.bfd.interceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bfd.utils.KafkaProducer;
import com.bfd.config.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * @author: bing.shen
 * @date: 2018/10/13 17:36
 * @Description:
 */
@Component
public class ExtraInfoInterceptor implements HandlerInterceptor {

    private final String preKey = "limit_extra_";


    @Value("${es.rms_extra_info.indexName}")
    private String indexName;

    @Value("${es.rms_extra_info.indexType}")
    private String indexType;

    @Value("${kafka.extra.topic}")
    private String extraTopic;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String businessId = request.getParameter("businessId");
        String columnStr = RedisUtil.get(preKey.concat(businessId));
        String devInfo = request.getParameter("devInfo");
        if (StringUtils.isNotBlank(devInfo)) {
            try {
                JSONObject devObj = JSONObject.parseObject(devInfo);
                if (StringUtils.isBlank(devObj.getString("deviceId"))) {
                    throw new RuntimeException("设备ID不能为空");
                }
            } catch (Exception e) {
                throw new RuntimeException("设备信息参数非json串");
            }
        }
        if (StringUtils.isNotBlank(columnStr)) {
            String extraInfo = request.getParameter("extraInfo");
            if (StringUtils.isNotBlank(extraInfo)) {
                JSONArray columnArr = JSONArray.parseArray(columnStr);
                Iterator<Object> iterator = columnArr.iterator();
                while (iterator.hasNext()) {
                    JSONObject columnObj = (JSONObject) iterator.next();
                    //如果是必传项
                    if (columnObj.getInteger("isRequired").equals(1)) {
                        try {
                            JSONObject extraObj = JSONObject.parseObject(extraInfo);
                            String columnValue = extraObj.getString(columnObj.getString("words"));
                            if (StringUtils.isBlank(columnValue)) {
                                throw new RuntimeException("附加参数信息:"
                                        .concat(columnObj.getString("desc"))
                                        .concat("字段为必传项"));
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException("附加参数信息非json串");
                        }
                    }
                }
            } else {
                throw new RuntimeException("附加参数信息不能为空");
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        new Thread(() -> {
            String businessId = request.getParameter("businessId");
            String devInfo = request.getParameter("devInfo");
            String extraInfo = request.getParameter("extraInfo");
            JSONObject obj;
            //PC端不传设备信息
            if (StringUtils.isNotBlank(devInfo)) {
                obj = JSONObject.parseObject(devInfo);
            } else {
                obj = new JSONObject();
            }
            obj.put("businessId", businessId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            obj.put("createTime", sdf.format(new Date()));
            obj.put("extraInfo", extraInfo);
            kafkaProducer.send(extraTopic,obj.toString());
        }).start();
    }
}
