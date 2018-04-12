package com.zhph.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.zhph.annotation.IgnoreLogin;
import com.zhph.exception.AppException;
import com.zhph.service.sys.SysResourcesService;
import com.zhph.util.CommonUtil;
import com.zhph.util.SpringContextUtils;
import com.zhph.util.StringUtil;

public class SecurityInterceptor implements HandlerInterceptor {
	private SysResourcesService sysResourcesService;

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object obj, Exception e) throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object obj, ModelAndView mv) throws Exception {
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object obj) throws Exception {
		// 设置response的头信息
		res.setCharacterEncoding("utf-8");

		HttpSession session = req.getSession();
		String url = req.getServletPath();
		if (!StringUtil.isEmpty(url)) {
			url = url.replaceAll("\\/{2,}", "/");
			url = url.replaceAll("\\/$", "");
		}

		if (obj == null) {
			return true;
		} else if (obj instanceof DefaultServletHttpRequestHandler) {
			return true;
			// 静态资源放行
		} else if (obj instanceof ResourceHttpRequestHandler) {
			return true;
			// Hessian接口请求放行
		} else if (obj instanceof HessianServiceExporter) {
			return true;
			// 如果是正常的Spring MVC请求
		} else if (obj instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) obj;

			// 如果不需要登录
			if (method.hasMethodAnnotation(IgnoreLogin.class))
				return true;
			// 后边都是需要登录的
			// 如果没有登录
			if (!CommonUtil.isAccessable(req)) {
				// 异步请求
				if (method.hasMethodAnnotation(ResponseBody.class)) {
					throw new AppException("您没有登录或会话超时，请登录！", 0);
				} else {
					req.getRequestDispatcher("/loginError").forward(req, res);
				}
				return false;
			}

			// 权限判断
			List<String> allResources = (List<String>) session.getAttribute("allResource");
			if (allResources == null) {
				if (sysResourcesService == null) {
					sysResourcesService = (SysResourcesService) SpringContextUtils.getBeanByClass(SysResourcesService.class);
				}
				allResources = sysResourcesService.findAllURI();
				session.setAttribute("allResource", allResources);
			}
			if (!allResources.contains(url))
				return true;

			List<String> resourcesUrlList = (List<String>) session.getAttribute("resourcesUrl");
			if (resourcesUrlList.contains(url))
				return true;
			else {
				throw new AppException("权限不足，请联系管理员！");
			}

		}
		return false;
	}

}
