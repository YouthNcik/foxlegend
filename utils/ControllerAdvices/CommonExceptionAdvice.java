package com.zhph.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * 全局异常处理
 * 
 * @ClassName: CommonExceptionAdvice
 * @author lichangchun@zhphfinance.com
 * @date 2017年8月23日 下午5:14:23
 *
 */
@ControllerAdvice
public class CommonExceptionAdvice {
	private Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ModelAndView handleMissingServletRequestParameterException(HttpServletRequest req, HttpServletResponse res, HandlerMethod method, MissingServletRequestParameterException e) {
		logger.error("缺少请求参数", e);
		return doHandle(req, res, method, 400, "缺少请求参数：" + e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ModelAndView handleHttpMessageNotReadableException(HttpServletRequest req, HttpServletResponse res, HandlerMethod method, HttpMessageNotReadableException e) {
		logger.error("参数解析失败", e);
		return doHandle(req, res, method, 400, "参数解析失败：" + e.getMessage());
	}

	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ModelAndView handleHttpRequestMethodNotSupportedException(HttpServletRequest req, HttpServletResponse res, HandlerMethod method, HttpRequestMethodNotSupportedException e) {
		logger.error("不支持当前请求方法", e);
		return doHandle(req, res, method, 405, "不支持当前请求方法：" + e.getMessage());
	}

	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ModelAndView handleHttpMediaTypeNotSupportedException(HttpServletRequest req, HttpServletResponse res, HandlerMethod method, HttpMediaTypeNotSupportedException e) {
		logger.error("不支持当前媒体类型", e);
		return doHandle(req, res, method, 415, "不支持当前媒体类型：" + e.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ModelAndView handleException(HttpServletRequest req, HttpServletResponse res, HandlerMethod method, DataIntegrityViolationException e) {
		logger.error("操作数据库出现异常", e);
		return doHandle(req, res, method, 500, "操作数据库出现异常：" + e.getMessage());
	}

	@ExceptionHandler(AppException.class)
	public ModelAndView handleAppException(HttpServletRequest req, HttpServletResponse res, HandlerMethod method, AppException e) {
		logger.error("[" + e.getStatus() + "]" + e.getMessage(), e);
		return doHandle(req, res, method, e.getStatus(), e.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(HttpServletRequest req, HttpServletResponse res, HandlerMethod method, Exception e) {
		logger.error("[500]" + e.getMessage(), e);
		return doHandle(req, res, method, 500, "系统异常：" + e.getMessage());
	}

	private ModelAndView doHandle(HttpServletRequest req, HttpServletResponse res, HandlerMethod handler, Integer code, String msg) {
		// 对于异步请求
		if (handler.hasMethodAnnotation(ResponseBody.class)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", code);
			map.put("msg", msg);
			ModelAndView modelAndView = new ModelAndView();
			MappingJackson2JsonView view = new MappingJackson2JsonView();
			view.setAttributesMap(map);
			modelAndView.setView(view);
			return modelAndView;
		} else {
			req.setAttribute("msg", "[" + code + "]" + msg);
			ModelAndView modelAndView = new ModelAndView("/pages/common/error");
			return modelAndView;
		}
	}
}
