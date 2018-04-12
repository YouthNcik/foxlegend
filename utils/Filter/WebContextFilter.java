package com.zhph.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhph.web.WebContext;

/**
 * 用于管理 WebContext 对象的生命周期
 * 
 * @ClassName: WebContextFilter
 * @author lichangchun@zhphfinance.com
 * @date 2017年8月23日 下午4:59:02
 *
 */
public class WebContextFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
			return;
		}
		WebContext.setRequest(req);
		WebContext.setResponse(res);
		try {
			chain.doFilter(request, response);
		} finally {
			WebContext.removeRequest();
			WebContext.removeResponse();
		}

	}

	@Override
	public void destroy() {
	}
}