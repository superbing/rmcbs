package com.bfd.filter;

import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.ribbon.RibbonHttpResponse;
import org.springframework.http.HttpStatus;

import com.bfd.bean.AccessLog;
import com.bfd.common.vo.Constants;
import com.bfd.utils.KafkaProducer;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import net.sf.json.JSONObject;

@Slf4j
public class PostFilter extends ZuulFilter {



	@Override
	public Object run() {
		log.info("GateWay POST FILTER.......");
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		long businessId = (Long) (ctx.get("businessId") == null ? 0L : ctx.get("businessId"));
		long apiId = ctx.get("apiId") == null ? 0L : (Long) ctx.get("apiId");
		long companyId = (Long) (ctx.get("companyId") == null ? 0L : ctx.get("companyId"));
		long apiType = ctx.get("apiType") == null ? 0L : (Long) ctx.get("apiType");
		String companyCode = (String) (ctx.get("companyCode") == null ? "" : ctx.get("companyCode"));

		// 同时满足api类型、apiId、商户id、单位id不为0并且companyCode不为空时才向Kafka发送消息
		if (apiType > 0L && apiId > 0L && companyId > 0L && businessId > 0L && !StringUtils.isEmpty(companyCode)) {
			String url = ctx.get("url") == null ? "" : (String) ctx.get("url");
			String ip = (String) ctx.get("ip");
			String inParam = getParams(request);
			long start = (Long) ctx.get("start");
			long useTime = System.currentTimeMillis() - start;
			int status = (Integer) (ctx.get("status") == null ? 0 : ctx.get("status"));
			String message = (String) (ctx.get("message") == null ? "" : ctx.get("message"));
			String accessKey = (String) (ctx.get("accessKey") == null ? "" : ctx.get("accessKey"));

			// 服务抛出异常,记录异常日志
			if (ctx.getResponseStatusCode() != HttpStatus.OK.value()) {
				log.info("服务抛出异常");
				status = 0;
				String responseBody = null;
				try {
					Object zuulResponse = ctx.get("zuulResponse");
					RibbonHttpResponse resp = (RibbonHttpResponse) zuulResponse;
					responseBody = new String(this.getBytesFromStream(resp.getBody()), "UTF-8");
					JSONObject responseJson = JSONObject.fromObject(responseBody);
					message = (String) (responseJson.get("data"));
					log.info(String.format("接口返回结果:%s", responseBody));
					resp.close();
					RequestContext.getCurrentContext().setResponseBody(responseBody);
				} catch (Exception ex) {
					message = responseBody;
				}
			}
			AccessLog log = new AccessLog();
			log.setCompanyCode(companyCode);
			log.setBusinessId(businessId);
			log.setCompanyId(companyId);
			log.setApiType(apiType);
			log.setApiId(apiId);
			log.setUrl(url);
			log.setIp(ip);
			log.setInParam(inParam);
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
			Date date = new Date();
			log.setAddTime(date.getTime());
			log.setUseTime(useTime);
			log.setStatus(status);
			log.setMessage(message);
			log.setAccessKey(accessKey);
			KafkaProducer.send(JSONObject.fromObject(log).toString());
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public String filterType() {
		return Constants.POST_FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	private static String getParams(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					map.put(paramName, paramValue);
				}
			}
		}
		return JSONObject.fromObject(map).toString();
	}

	private byte[] getBytesFromStream(InputStream is) throws Exception {
		byte[] buffer = new byte[1024];
		byte[] all = null;
		int rlegth = 0;

		while ((rlegth = is.read(buffer)) != -1) {
			if (all == null) {
				all = new byte[rlegth];
				System.arraycopy(buffer, 0, all, 0, rlegth);
			} else {
				byte[] bftmp = new byte[all.length];
				System.arraycopy(all, 0, bftmp, 0, all.length);
				all = new byte[all.length + rlegth];
				System.arraycopy(bftmp, 0, all, 0, bftmp.length);
				System.arraycopy(buffer, 0, all, bftmp.length, rlegth);
			}
		}

		return all;
	}
}
