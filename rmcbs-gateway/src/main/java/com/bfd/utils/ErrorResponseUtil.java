package com.bfd.utils;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;
import com.netflix.zuul.context.RequestContext;

import net.sf.json.JSONObject;

/**
 * @author: bing.shen
 * @date: 2018/8/24 15:55
 * @Description:
 */
public class ErrorResponseUtil {
    
    public static void setMessage(RequestContext ctx, String message) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", HttpServletResponse.SC_EXPECTATION_FAILED);
        map.put("message", message);
        map.put("data", "");
        
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(HttpServletResponse.SC_EXPECTATION_FAILED);
        ctx.setResponseBody(JSONObject.fromObject(map).toString());
        ctx.getResponse().setContentType("application/json;charset=UTF-8");
        ctx.put("status", 0);
    }
}
