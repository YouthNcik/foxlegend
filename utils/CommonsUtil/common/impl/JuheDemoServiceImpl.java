package com.zhph.service.common.impl;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.zhph.service.common.JuheDemoService;
import com.zhph.util.HttpClientHelper;

/**
 * @author lxp
 * @date 2018年3月5日 下午4:35:56
 * @parameter
 * @return
 */
@Service
public class JuheDemoServiceImpl implements JuheDemoService {

	public static final String DEF_CHARSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	// 申请的APPKEY=2a17fa253f24d5b214408d9e86941b1f 不能改动
	public static final String APPKEY = "2a17fa253f24d5b214408d9e86941b1f";

	public static final String URL_DAY = "http://v.juhe.cn/calendar/day";
	public static final String URL_MONTH = "http://v.juhe.cn/calendar/month";
	public static final String URL_YEAR = "http://v.juhe.cn/calendar/year";

	@Override
	public String getCalendarDay(String day) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("key", APPKEY);
		map.put("date", day);

		return HttpClientHelper.doPost(URL_DAY, map);
	}

	@Override
	public String getCalendarMonth(String month) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("key", APPKEY);
		map.put("year-month", month);

		return HttpClientHelper.doPost(URL_MONTH, map);
	}

	@Override
	public String getCalendarYear(String year) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("key", APPKEY);
		map.put("year", year);

		return HttpClientHelper.doPost(URL_YEAR, map);
	}

}
