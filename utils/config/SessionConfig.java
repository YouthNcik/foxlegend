package com.zhph.config;

import com.zhph.web.WebSessionListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

@Configuration
// maxInactiveIntervalInSeconds为SpringSession的过期时间（单位：秒）
@EnableRedisHttpSession(redisNamespace = "new-salary-20170119",maxInactiveIntervalInSeconds=600)
public class SessionConfig {
	@Bean
	public FilterRegistrationBean springSessionRepositoryFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new DelegatingFilterProxy());
		registration.addUrlPatterns("/*");
		registration.setName("springSessionRepositoryFilter");
		registration.setOrder(2);
		return registration;
	}

	@Bean
	public SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapterRegistration() {
		List<HttpSessionListener> listeners = new ArrayList<HttpSessionListener>();
		listeners.add(new WebSessionListener());
		SessionEventHttpSessionListenerAdapter adapter = new SessionEventHttpSessionListenerAdapter(listeners);
		return adapter;
	}
}
