package com.bfd.filter;

import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.ribbon.support.RibbonCommandContext;
import org.springframework.cloud.netflix.ribbon.support.RibbonRequestCustomizer;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/8/14 14:55
 * @Description:
 */
public class RoutingFilter extends RibbonRoutingFilter {

    private boolean useServlet31 = false;

    public RoutingFilter(ProxyRequestHelper helper, RibbonCommandFactory<?> ribbonCommandFactory, List<RibbonRequestCustomizer> requestCustomizers) {
        super(helper, ribbonCommandFactory, requestCustomizers);
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRouteHost() == null && ctx.get("serviceId") != null && ctx.sendZuulResponse();
    }

    @Override
    protected RibbonCommandContext buildCommandContext(RequestContext context) {
        HttpServletRequest request = context.getRequest();
        MultiValueMap<String, String> headers = this.helper.buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = this.helper.buildZuulRequestQueryParams(request);
        String verb = this.getVerb(request);
        InputStream requestEntity = this.getRequestBody(request);
        if (request.getContentLength() < 0 && !verb.equalsIgnoreCase("GET")) {
            context.setChunkedRequestBody();
        }

        String serviceId = (String)context.get("serviceId");
        Boolean retryable = (Boolean)context.get("retryable");
        Object loadBalancerKey = context.get("loadBalancerKey");
        String uri = this.helper.buildZuulRequestURI(request);
        uri = uri.replace("//", "/");
        long contentLength = this.useServlet31 ? request.getContentLengthLong() : (long)request.getContentLength();
        params.add("businessId", String.valueOf(RequestContext.getCurrentContext().get("businessId")));
        params.add("ip", String.valueOf(RequestContext.getCurrentContext().get("ip")));
        return new RibbonCommandContext(serviceId, verb, uri, retryable, headers, params, requestEntity, this.requestCustomizers, contentLength, loadBalancerKey);
    }
}
