package com.zhph.interceptor;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zhph.model.sys.SysUser;

/**
 * 实现Spring MVC中Controller接口访问信息日志输出
 * 
 * @author lxp
 *
 */

public class LoggerShowInterceptor implements HandlerInterceptor {

	public static final Logger logger = LogManager.getLogger(LoggerShowInterceptor.class);

	private SysUser sysUser;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		sysUser = (SysUser) request.getSession().getAttribute("onlineUser");
		if (sysUser != null) {
			long startTime = System.currentTimeMillis();
			request.setAttribute("startTime", startTime);
			if (handler instanceof HandlerMethod) {

				Enumeration<String> headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements()) {
					String key = (String) headerNames.nextElement();
					String value = request.getHeader(key);
					System.out.println(key + "=" + value);
				}
				HandlerMethod h = (HandlerMethod) handler;
				logger.info("==>  |字符集编码：" + Charset.defaultCharset().name());
				logger.info("==>  |ActionId：" + request.getRequestURI());
				logger.info("==>  |SessionId：" + request.getSession().getId());
				logger.info("==>  |登录用户：" + sysUser.getFullName() + "/" + sysUser.getUserName());
				logger.info("==>  |ClassId：" + h.getBean().getClass().getName());
				logger.info("==>  |执行方法体：" + h.getMethod().getName());
				logger.info("==>  |Params：" + getParamString(request.getParameterMap()));
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

		if (sysUser != null) {
			long startTime = (Long) request.getAttribute("startTime");
			long endTime = System.currentTimeMillis();
			long executeTime = endTime - startTime;
			if (handler instanceof HandlerMethod) {
				if (modelAndView != null) {
					logger.info("==>  |转向地址：" + modelAndView.getViewName());
				} else {
					logger.info("==>  |转向地址：NULL（AJAX请求...）");
				}
				logger.info("==>  |The total time consuming：" + executeTime + "ms");
				logger.info("==>  |------------------------" + new Date() + "-------------------------");
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {

	}

	private String getParamString(Map<String, String[]> map) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String[]> e : map.entrySet()) {
			sb.append(e.getKey()).append("=");
			String[] value = e.getValue();
			if (value == null) {
				sb.append("null").append("\t");
			} else {
				if (value.length == 1) {
					sb.append(value[0]).append("\t");
				} else {
					sb.append(Arrays.toString(value)).append("\t");
				}
			}
		}
		return sb.toString();
	}
}