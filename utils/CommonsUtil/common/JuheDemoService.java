package com.zhph.service.common;

public interface JuheDemoService {

	// 1.获取当天的详细信息
	public String getCalendarDay(String day);

	// 1.获取当月近期假期
	public String getCalendarMonth(String month);

	// 1.获取当年的假期列表
	public String getCalendarYear(String year);

}