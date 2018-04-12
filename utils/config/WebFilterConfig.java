package com.zhph.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zhph.filter.ContextPathFilter;
import com.zhph.filter.WebContextFilter;

@Configuration
public class WebFilterConfig {
	@Bean
	public FilterRegistrationBean contextPathFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new ContextPathFilter());
		registration.addUrlPatterns("/*");
		registration.setName("contextPathFilter");
		registration.setOrder(1);
		return registration;
	}

	@Bean
	public FilterRegistrationBean webContextFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new WebContextFilter());
		registration.addUrlPatterns("/*");
		registration.setName("webContextFilter");
		registration.setOrder(2);
		return registration;
	}
}
