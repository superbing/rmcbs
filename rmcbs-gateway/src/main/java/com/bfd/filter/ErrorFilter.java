package com.bfd.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSONObject;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.Result;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Slf4j
public class ErrorFilter extends ZuulFilter {
    
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        Result<Object> resultMap = new Result<Object>(HttpStatus.EXPECTATION_FAILED.value(), "网关发生错误，请开发者稍候再试!", null);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.println(JSONObject.toJSONString(resultMap));
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("请求api-server异常", e);
        }
        return null;
    }
    
    @Override
    public boolean shouldFilter() {
        return true;
    }
    
    @Override
    public String filterType() {
        return Constants.ERROR_FILTER_TYPE;
    }
    
    @Override
    public int filterOrder() {
        return 0;
    }
    
}
