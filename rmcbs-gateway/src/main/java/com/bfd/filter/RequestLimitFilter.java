package com.bfd.filter;

import com.bfd.utils.ErrorResponseUtil;
import com.bfd.config.RedisUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @author: bing.shen
 * @date: 2018/9/13 17:33
 * @Description:并发数限制
 */
@Slf4j
public class RequestLimitFilter extends ZuulFilter {

    private final String preKey = "limit_set_";

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        log.info("[pre RequestLimitFilter]");

        RequestContext ctx = RequestContext.getCurrentContext();
        Long businessId = (Long)ctx.get("businessId");
        if(businessId!=null){
            //获取该客户单位设置的并发数
            String concurrency = RedisUtil.get(preKey.concat(businessId.toString()));
            if(StringUtils.isNotBlank(concurrency) && Integer.valueOf(concurrency)>0){
                String companyCode = (String) ctx.get("companyCode");
                Long count = RedisUtil.incr(companyCode);
                if(count == 1){
                    RedisUtil.expire(companyCode, 1);
                }
                if(count > Long.valueOf(concurrency)){
                    ErrorResponseUtil.setMessage(ctx, "超出访问并发数限制");
                    return null;
                }
            }

        }
        return null;
    }
}
