package com.zhph.service.common;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
	public Object doLogin(HttpServletRequest req) throws Exception;
}
