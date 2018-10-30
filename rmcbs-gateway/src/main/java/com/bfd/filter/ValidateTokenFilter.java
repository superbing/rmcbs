package com.bfd.filter;

import com.alibaba.fastjson.JSONObject;
import com.bfd.bean.ApiBean;
import com.bfd.bean.AuthInfoBean;
import com.bfd.bean.CompanyBean;
import com.bfd.feign.AuthFeignClient;
import com.bfd.utils.ErrorResponseUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: bing.shen
 * @date: 2018/8/13 18:21
 * @Description:
 */
@Slf4j
public class ValidateTokenFilter extends ZuulFilter {

    @Autowired
    private AuthFeignClient authFeignClient;

    @Value("${auth.path}")
    private String getTokenPath;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        log.info("[pre ValidateTokenFilter]");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getParameter("token");
        // 调用ip
        String ip = this.getRemoteAddr(request);
        ctx.put("ip", ip);
        // 请求开始时间, 用于记录接口调用时间
        ctx.put("start", System.currentTimeMillis());
        ctx.put("status", 1);
        // 请求url
        String servletPath = request.getServletPath();
        ctx.put("url", servletPath);
        log.info(String.format("servletPath:%s", servletPath));

        if (StringUtils.isBlank(token) && !getTokenPath.equals(servletPath)) {
            ErrorResponseUtil.setMessage(ctx, "token不能为空");
            return null;
        }

        if (!getTokenPath.equals(servletPath)) {
            AuthInfoBean authInfoBean = authFeignClient.getAuthInfo(token);
            if (HttpStatus.INTERNAL_SERVER_ERROR.value() == authInfoBean.getCode()) {
                ErrorResponseUtil.setMessage(ctx, authInfoBean.getMessage());
                return null;
            }
            if (HttpStatus.UNAUTHORIZED.value() == authInfoBean.getCode()) {
                ErrorResponseUtil.setMessage(ctx, authInfoBean.getMessage());
            }
            Long apiId = 0L;
            Long apiType = 0L;
            ApiBean apiBean = null;
            JSONObject authenticatedObject = authInfoBean.getAuthenticatedObject();
            if (authenticatedObject != null) {
                apiBean = authenticatedObject.getObject(servletPath, ApiBean.class);
            } else {
                ErrorResponseUtil.setMessage(ctx, "无可用接口");
            }
            if (apiBean != null) {
                if (apiBean.getBusinessId() == null) {
                    ErrorResponseUtil.setMessage(ctx, "无此接口访问权限");
                }
                apiId = apiBean.getApiId();
                apiType = apiBean.getApiType();
            } else {
                ErrorResponseUtil.setMessage(ctx, "无此接口");
            }
            CompanyBean companyBean = authInfoBean.getCompanyBean();
            log.info(String.format("AuthInfoBean->CompanyBean:%s", companyBean));
            ctx.put("apiId", apiId);
            ctx.put("apiType", apiType);
            ctx.put("businessId", companyBean.getBusinessId());
            ctx.put("companyCode", companyBean.getCompanyCode());
            ctx.put("companyId", companyBean.getCompanyId());
            ctx.put("accessKey", companyBean.getAccessKey());
        }
        // 如果是获取token接口:url->/auth/getToken
        else {
            String clientId = request.getParameter("clientId");
            String accessKey = request.getParameter("secret");
            ApiBean apiBean = authFeignClient.getApiInfo(servletPath, clientId);
            ctx.put("companyCode", clientId);
            ctx.put("accessKey", accessKey);
            ctx.put("businessId", apiBean.getBusinessId());
            ctx.put("companyId", apiBean.getCompanyId());
            ctx.put("apiId", apiBean.getApiId());
            ctx.put("apiType", apiBean.getApiType());
        }
        return null;
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
