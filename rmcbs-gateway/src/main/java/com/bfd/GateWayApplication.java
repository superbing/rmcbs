package com.bfd;

import java.util.Collections;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.bfd.filter.ErrorFilter;
import com.bfd.filter.PostFilter;
import com.bfd.filter.RequestLimitFilter;
import com.bfd.filter.RoutingFilter;
import com.bfd.filter.ValidateTokenFilter;

/**
 * GateWayApplication
 *
 * @author bing.shen
 * @date 2018/8/6
 */
@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients
public class GateWayApplication {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	public static void main(String[] args) {
		SpringApplication.run(GateWayApplication.class, args);
	}

	@Bean
	public ValidateTokenFilter preValidateTokenFilter() {
		return new ValidateTokenFilter();
	}

	@Bean
	public RequestLimitFilter preRequestLimitFilter() {
		return new RequestLimitFilter();
	}

	@Bean
	public RibbonRoutingFilter ribbonRoutingFilter(ProxyRequestHelper helper, RibbonCommandFactory<?> ribbonCommandFactory) {
		RoutingFilter filter = new RoutingFilter(helper, ribbonCommandFactory, Collections.emptyList());
		return filter;
	}

	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}

	@Bean
	public ErrorFilter errortFilter() {
		return new ErrorFilter();
	}

}
