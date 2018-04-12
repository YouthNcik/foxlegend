package com.zhph.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateUtil {
	public static final String YEAR = "YYYY";
	public static final String MM = "MM";
	public static final String DD = "DD";
	public static final String HH = "HH";
	public static final String MI = "MI";
	public static final String SS = "SS";
	private static String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };

	private static SimpleDateFormat normalDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
	private static DateFormat commonDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat emdhmsyDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

	/**
	 * @功能描述:将日期转换为字符串
	 * @param date
	 * @return
	 * @创建时间 2008-7-13
	 * @author Administrator
	 */
	public static String parseDateFormat(Date date, String formate) {
		try {
			SimpleDateFormat sdf = getSimpleDateFormat(formate);
			return sdf.format(date);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * @功能描述:將字符串轉換為日期
	 * @param dateStr
	 * @return
	 * @创建时间 2008-7-13
	 * @author Administrator
	 */
	public static Date parseStringToDate(String dateStr, String formate) throws Exception {
		try {
			SimpleDateFormat sdf = getSimpleDateFormat(formate);
			return sdf.parse(dateStr);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * @功能描述: 计算两个日期之间的时间差 传入的对象为两个日期,字符串和时间对象即可
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 * @创建时间 2008-7-12
	 * @author Administrator
	 */
	public static String dateDiff(Object dateFrom, Object dateTo) throws Exception {
		try {

			Date date1 = getDateByObject(dateFrom);
			Date date2 = getDateByObject(dateTo);
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(date2);
			cal2.setTime(date1);
			float a = ((float) (cal1.getTimeInMillis() - cal2.getTimeInMillis()) / 86400) / 1000;
			int b = (int) a;
			return "" + Math.abs(((b == a) ? b : b + 1));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return "";
	}

	/**
	 * 日期相减
	 *
	 * @param date
	 *            日期
	 * @param date1
	 *            日期
	 * @return 返回相减后的日期
	 */
	public static int getDiffDate(java.util.Date date, java.util.Date date1) {
		return (int) ((date.getTime() - date1.getTime()) / (24 * 3600 * 1000));
	}

	/**
	 * @功能描述:根据传入参数产生日期对象
	 * @param obj
	 * @return
	 * @throws Exception
	 * @创建时间 Oct 20, 2008
	 * @author Administrator
	 */
	public static Date getDateByObject(Object obj) throws Exception {
		if (null == obj) {
			return normalDateFormat.parse(getCurrentDate(null));
		}

		if (obj instanceof java.util.Date) {
			return normalDateFormat.parse(normalDateFormat.format(obj));
		}
		if (obj instanceof String) {
			return normalDateFormat.parse((String) obj);
		}
		return null;
	}

	/**
	 *
	 * @功能描述：将分钟转换成小时
	 * @param minute
	 *            分钟数
	 * @return
	 * @throws @创建时间
	 * @author zhh
	 */
	public static String minuteConvertHour(long minute) {
		int minuteInt = (int) minute;
		int h = (int) Math.floor(minute / 60);
		int m = minuteInt - h * 60;
		StringBuffer sb = new StringBuffer();
		if (h > 0) {
			sb.append(h);
			sb.append("小时");
		}
		if (m > 0) {
			sb.append(m);
			sb.append("分钟");
		}
		return sb.toString();
	}

	/**
	 * @功能描述:获取当前时间
	 * @return
	 * @创建时间 2008-7-26
	 * @author beedoor
	 */
	public static String getCurrentDate(String format) {
		Date impTime = new Date();
		SimpleDateFormat sdf;
		try {
			sdf = getSimpleDateFormat(format);
			return sdf.format(impTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @功能描述:获取当前时间(日始终是1)
	 * @return
	 * @创建时间 2008-7-26
	 * @author beedoor
	 */
	public static String getCurrentDate2(String format) {
		Date impTime = new Date();

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf;
		calendar.setTime(impTime);
		calendar.set(Calendar.DATE, 1);
		try {
			sdf = getSimpleDateFormat(format);
			return sdf.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @功能描述:获取指定格式的字符串
	 * @return
	 * @创建时间 2008-7-26
	 * @author beedoor
	 */
	public static String getFormatDateStr(Object obj, String format) throws Exception {
		Date d = null;
		SimpleDateFormat sdf = getSimpleDateFormat(format);
		if (obj instanceof String) {
			d = sdf.parse(obj.toString());
		} else if (obj instanceof Date) {
			d = (Date) obj;

		}
		if (null != d) {
			return sdf.format(d);
		}

		return "";
	}

	/**
	 * 功能描述:返回日期格式化对象
	 *
	 * @param format
	 * @return
	 * @创建时间 Jul 28, 2008
	 * @author Administrator
	 */
	public static SimpleDateFormat getSimpleDateFormat(String format) throws Exception {
		try {
			return new SimpleDateFormat(format);
		} catch (Exception ex) {
			return normalDateFormat;
		}
	}

	/**
	 * @功能描述:返回日期中的指定部分--YYYY 年份，MM 月份，DD 天，HH 小时 MI 分 SS 秒
	 * @param date
	 * @param part
	 * @return
	 * @创建时间 Jul 28, 2008
	 * @author Administrator
	 */
	public static int getDatePart(Date date, String part) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (part.equals(YEAR)) {
			return cal.get(Calendar.YEAR);
		} else if (part.equals(MM)) {
			return cal.get(Calendar.MONTH);
		} else if (part.equals(DD)) {
			return cal.get(Calendar.DAY_OF_MONTH);
		} else if (part.equals(HH)) {
			return cal.get(Calendar.HOUR_OF_DAY);
		} else if (part.equals(MI)) {
			return cal.get(Calendar.MINUTE);
		} else if (part.equals(SS)) {
			return cal.get(Calendar.SECOND);
		}

		return 0;
	}

	public static String getSimpleDateString(String date) {
		try {
			Date date22 = getDateByObject(date);
			return simpleDateFormat.format(date22);
		} catch (Exception ex) {

		}
		return "";
	}

	public static Timestamp transferDateTimeStamp(Date date) {
		return new Timestamp(date.getTime());
	}

	// 日期转化为大小写
	public static String dataToUpper(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) + 1;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		return numToUpper(year) + "年" + monthToUppder(month) + "月" + dayToUppder(day) + "日";
	}

	// 将数字转化为大写
	public static String numToUpper(int num) {
		// String u[] = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
		String u[] = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		char[] str = String.valueOf(num).toCharArray();
		String rstr = "";
		for (int i = 0; i < str.length; i++) {
			rstr = rstr + u[Integer.parseInt(str[i] + "")];
		}
		return rstr;
	}

	// 月转化为大写
	public static String monthToUppder(int month) {
		if (month < 10) {
			return numToUpper(month);
		} else if (month == 10) {
			return "十";
		} else {
			return "十" + numToUpper(month - 10);
		}
	}

	// 日转化为大写
	public static String dayToUppder(int day) {
		if (day < 20) {
			return monthToUppder(day);
		} else {
			char[] str = String.valueOf(day).toCharArray();
			if (str[1] == '0') {
				return numToUpper(Integer.parseInt(str[0] + "")) + "十";
			} else {
				return numToUpper(Integer.parseInt(str[0] + "")) + "十" + numToUpper(Integer.parseInt(str[1] + ""));
			}
		}
	}

	// yyyyMM 返回相隔月数
	public static int getBetweenMonth(Date start, Date end) {
		if (start.after(end)) {
			Date t = start;
			start = end;
			end = t;
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(end);
		Calendar temp = Calendar.getInstance();
		temp.setTime(end);
		temp.add(Calendar.DATE, 1);

		int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

		if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
			return year * 12 + month + 1;
		} else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
			return year * 12 + month;
		} else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
			return year * 12 + month;
		} else {
			return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
		}
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	// 获取当前日期的前一天日期
	public static String getLastDate(String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		SimpleDateFormat lformat = new SimpleDateFormat(format);
		return lformat.format(calendar.getTime());
	}

	// 比较2个日期的大小
	public static int compare_date(Date from, Date to) {
		if (from.getTime() > to.getTime()) {
			return 1;
		} else if (from.getTime() < to.getTime()) {
			return -1;
		} else {
			return 0;
		}
	}

	public static int getYear(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.YEAR);
	}

	public static int getMonth(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.MONTH) + 1;
	}

	public static int getDay(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.DAY_OF_MONTH);
	}

	public static java.util.Date convertStringToDate(String dateStr) {
		return convertStringToDate(dateStr, "yyyy-MM-dd");
	}

	public static java.util.Date convertStringToDate(String dateStr, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getAddMonth(String dateStr, int offset) {
		String calcRslt = "";
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(dateStr));
			cal.add(Calendar.MONTH, offset);// 月份加1

			calcRslt = commonDateFormat.format(cal.getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return calcRslt;
	}

	public static Date getAddMonth(Date date, int offset) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, offset);// 月份加1
			return cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] getMonthAry(String beginDate, String endDate) {
		String[] monthAry = null;
		try {
			int count = getMonthCount(beginDate + "-01", endDate + "-01");
			count = count + 1;
			monthAry = new String[count];
			for (int i = 0; i < count; i++) {
				String monthStr = getAddMonth(beginDate + "-01", i);
				monthAry[i] = monthStr.substring(0, monthStr.length() - 3);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return monthAry;
	}

	public static String getReduceOneMonth(String dateStr) {
		String calcRslt = "";
		try {
			int year = getYear(convertStringToDate(dateStr));
			int month = getMonth(convertStringToDate(dateStr));
			int day = getDay(convertStringToDate(dateStr));
			String dayStr = "";
			if (day < 10) {
				dayStr = "0" + day;
			} else {
				dayStr = day + "";
			}
			if (month == 1) {
				month = 12;
				calcRslt = (year - 1) + "-" + month + "-" + dayStr;
			} else {
				calcRslt = year + "-" + (month - 1) + "-" + dayStr;
				if ((month - 1) < 10) {
					calcRslt = year + "-0" + (month - 1) + "-" + dayStr;
				} else {
					calcRslt = year + "-" + (month - 1) + "-" + dayStr;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calcRslt;
	}

	// 计算当月最后一天,返回字符串
	public static int getLastDay(String dateStr) {
		int day = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(dateStr);
			Calendar lastDate = Calendar.getInstance();
			lastDate.setTime(date);
			lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
			lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
			lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

			String str = sdf.format(lastDate.getTime());
			String dayStr = str.substring(8);
			day = Integer.parseInt(dayStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}

	// 返回工资年月的最后一天
	public static String getGzymLastDay(String gzYm) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(gzYm + "-01");
			Calendar lastDate = Calendar.getInstance();
			lastDate.setTime(date);
			lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
			String str = sdf.format(lastDate.getTime());
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 计算当月第一天,返回字符串
	public static String getFirstDate(String dateStr) {
		String str = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(dateStr);
			Calendar firstDate = Calendar.getInstance();
			firstDate.setTime(date);
			firstDate.set(Calendar.DATE, 1);// 设为当前月的1号

			str = sdf.format(firstDate.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getAllYearMonthDay(String dateStr) {
		String calcRslt = "";
		try {
			int year = getYear(convertStringToDate(dateStr));
			int month = getMonth(convertStringToDate(dateStr));
			int day = getDay(convertStringToDate(dateStr));
			String monthStr = "";
			if (month < 10) {
				monthStr = "0" + month;
			} else {
				monthStr = month + "";
			}
			String dayStr = "";
			if (day < 10) {
				dayStr = "0" + day;
			} else {
				dayStr = day + "";
			}
			calcRslt = year + "-" + monthStr + "-" + dayStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calcRslt;
	}

	public static String changeDay(String datestr, int offset) {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String changedatestr = bartDateFormat.format(changeDay(convertStringToDate(datestr, "yyyy-MM-dd"), offset)).toString();
		return changedatestr;
	}

	public static Date changeDay(Date date, int offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) + offset));
		return calendar.getTime();
	}

	public static String getChineseDate(String dateStr) {
		Date date = convertStringToDate(dateStr);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) + 1;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		return year + "年" + month + "月" + day + "日";
	}

	/**
	 * 取得上个月的最后一天
	 *
	 * @param
	 * @return String
	 */
	public static String getOnOneMonthEnd() {
		String result = "";
		int year = getYear();
		int month = getMonth();
		if (month == 1) {
			month = 12;
			year = year - 1;
		} else {
			month = month - 1;
		}
		String ymd = year + "-" + month + "-" + "01";
		Date date = convertStringToDate(ymd);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		result = parseDateFormat(calendar.getTime(), "yyyy-MM-dd");
		return result;
	}

	/**
	 * 获取当前时间.月份减一
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		return format.format(calendar.getTime());
	}

	/**
	 * 取得上个月份的第一天
	 *
	 * @param
	 * @return String
	 */
	public static String getOnOneMonthBegin() {
		String result = "";
		int year = getYear();
		int month = getMonth();
		if (month == 1) {
			month = 12;
			year = year - 1;
		} else {
			month = month - 1;
		}
		if (month < 10) {
			result = year + "-0" + month + "-" + "01";
		} else {
			result = year + "-" + month + "-" + "01";
		}
		return result;
	}

	/**
	 * 返回整型的年份
	 *
	 * @return 当前的年份
	 */
	public static int getYear() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 返回整型的月份
	 *
	 * @return 当前的月份
	 */
	public static int getMonth() {
		Calendar cal = Calendar.getInstance();
		String monthstr = (cal.get(Calendar.MONTH) > 8) ? "" + (cal.get(Calendar.MONTH) + 1) : "0" + (cal.get(Calendar.MONTH) + 1);
		int month = Integer.parseInt(monthstr);
		return month;
	}

	/**
	 * 根据一定格式转换字符串的日期格式
	 *
	 * @return 当前的月份
	 */
	public static String getEmdhmsyDate(String dateStr) {
		String newDate = "";
		try {
			Date d = emdhmsyDateFormat.parse(dateStr);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			newDate = formatter.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newDate;
	}

	/**
	 * 将日期向后延后几天
	 *
	 * @param dateStr
	 * @param day
	 * @return
	 * @author CWW
	 */
	public static String dateAddDay(String dateStr, int day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String newDate = "";
		try {
			Date d = new Date(sdf.parse(dateStr).getTime() + 24 * 3600 * 1000L * day);
			newDate = sdf.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newDate;
	}

	/**
	 * 将日期提前几天
	 *
	 * @param dateStr
	 * @param day
	 * @return
	 * @author CWW
	 */
	public static String dateSubDay(String dateStr, int day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String newDate = "";
		try {
			Date d = new Date(sdf.parse(dateStr).getTime() - 24 * 3600 * 1000L * day);
			newDate = sdf.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newDate;
	}

	/**
	 * 判断指定日期是否为当月
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isOnMonth(String date) {
		try {
			String toDate = getCurrentDate("yyyy-MM");
			toDate = toDate.substring(toDate.lastIndexOf("-") + 1);
			if (date == null) {
				return false;
			}
			if (date.substring(5, 7).equals(toDate)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取两日期相差几个月
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 * @author cww
	 */
	public static int getMonthCount(String startDate, String endDate) {
		int monthCount = 0;
		int flag = 0;
		try {
			Calendar calendarDate1 = Calendar.getInstance();
			calendarDate1.setTime(DateFormat.getDateInstance().parse(startDate));

			Calendar calendarDate2 = Calendar.getInstance();
			calendarDate2.setTime(DateFormat.getDateInstance().parse(endDate));

			if (calendarDate2.equals(calendarDate1)) {
				return 0;
			}
			if (calendarDate1.after(calendarDate2)) {
				Calendar temp = calendarDate1;
				calendarDate1 = calendarDate2;
				calendarDate2 = temp;
			}
			if (calendarDate2.get(Calendar.DAY_OF_MONTH) < calendarDate1.get(Calendar.DAY_OF_MONTH)) {
				flag = 1;
			}
			if (calendarDate2.get(Calendar.YEAR) > calendarDate1.get(Calendar.YEAR)) {
				monthCount = ((calendarDate2.get(Calendar.YEAR) - calendarDate1.get(Calendar.YEAR)) * 12 + calendarDate2.get(Calendar.MONTH) - flag) - calendarDate1.get(Calendar.MONTH);
			} else {
				monthCount = calendarDate2.get(Calendar.MONTH) - calendarDate1.get(Calendar.MONTH) - flag;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return monthCount;
	}

	public static int getMonthCountBy30(String startDate, String endDate) {
		int monthCount = 0;
		// int flag = 0;
		try {
			Calendar calendarDate1 = Calendar.getInstance();
			calendarDate1.setTime(DateFormat.getDateInstance().parse(startDate));

			Calendar calendarDate2 = Calendar.getInstance();
			calendarDate2.setTime(DateFormat.getDateInstance().parse(endDate));

			if (calendarDate2.equals(calendarDate1)) {
				return 0;
			}
			if (calendarDate1.after(calendarDate2)) {
				Calendar temp = calendarDate1;
				calendarDate1 = calendarDate2;
				calendarDate2 = temp;
			}

			long time1 = calendarDate1.getTimeInMillis();
			long time2 = calendarDate2.getTimeInMillis();

			long between_days = (time2 - time1) / (1000 * 3600 * 24);

			monthCount = Integer.parseInt(String.valueOf(between_days)) / 30;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return monthCount;
	}

	/**
	 * private static List<String> parseStringToList(String target, String flag)
	 * { StringBuilder sb = new StringBuilder(target); int st = 0; int ed =
	 * sb.indexOf(flag); List<String> list = new LinkedList<String>(); while (st
	 * >= 0 && ed >= 0) { list.add(sb.substring(st, ed)); st = ed + 1; ed =
	 * sb.indexOf(flag, st); } if (list.size() > 0) { list.add(sb.substring(st,
	 * sb.length())); } return list; }
	 **/

	/**
	 * 比较指定两日期如果str1晚于str2则return true;
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean secondCompare(String str1, String str2) {
		boolean bea = false;
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1;
		java.util.Date date0;
		int flag = 0;
		try {
			date1 = sdf_d.parse(str1);
			date0 = sdf_d.parse(str2);
			flag = date0.compareTo(date1);
			if (flag == 0 || flag == -1) {
				bea = true;
			}
		} catch (ParseException e) {
			bea = false;
		}
		return bea;
	}

	/**
	 * 获得自然月的最后一天日期
	 *
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getLastDayOfMonth(Date date) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		return getFormatDateStr(cal.getTime(), "yyyy-MM-dd");
	}

	/***
	 * 前count个月
	 * 
	 * @param yearMon
	 *            'yyyy-mm'
	 * @param count
	 * @return
	 * @author zhanchunliang
	 * @throws Exception
	 */
	public static String getSubMonth(String yearMon, Integer count) {
		String result = "";
		if (count > 0) {
			Integer mon = Integer.parseInt(yearMon.substring(5, 7)) - count;
			if (mon > 0) {
				if (mon > 9) {
					result = yearMon.substring(0, 4) + "-" + mon.toString();
				} else {
					result = yearMon.substring(0, 4) + "-0" + mon.toString();
				}
			} else if (mon == 0) {
				Integer year = Integer.parseInt(yearMon.substring(0, 4)) - 1;
				result = year.toString() + "-12";
			} else {
				Integer year = Integer.parseInt(yearMon.substring(0, 4)) - 1;
				Integer month = 12 + Integer.parseInt(yearMon.substring(5, 7)) - count;
				if (month > 9) {
					result = year.toString() + "-" + month.toString();
				} else {
					result = year.toString() + "-0" + month.toString();
				}
			}
		} else {
			Integer mon = Integer.parseInt(yearMon.substring(5, 7)) - count;
			if (mon <= 12) {
				if (mon > 9) {
					result = yearMon.substring(0, 4) + "-" + mon.toString();
				} else {
					result = yearMon.substring(0, 4) + "-0" + mon.toString();
				}
			} else {
				Integer year = Integer.parseInt(yearMon.substring(0, 4)) + 1;
				Integer month = Integer.parseInt(yearMon.substring(5, 7)) - count - 12;
				if (month > 9) {
					result = year.toString() + "-" + month.toString();
				} else {
					result = year.toString() + "-0" + month.toString();
				}
			}
		}
		return result;
	}

	/**
	 * 得到两日期相差几个月
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getYearMonthCount(String beginDate, String endDate) throws Exception {
		int monthCount = 0;

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

		Date beginDate1 = fmt.parse(beginDate);
		Calendar beginCal = Calendar.getInstance();
		beginCal.setTime(beginDate1);
		int beginYear = beginCal.get(Calendar.YEAR);
		int beginMonth = beginCal.get(Calendar.MONTH) + 1;
		int beginDay = beginCal.get(Calendar.DAY_OF_MONTH);

		Date endDate1 = fmt.parse(endDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate1);
		int endYear = endCal.get(Calendar.YEAR);
		int endMonth = endCal.get(Calendar.MONTH) + 1;
		int endDay = endCal.get(Calendar.DAY_OF_MONTH);

		monthCount = (endYear - beginYear) * 12 + (endMonth - beginMonth);

		if (monthCount == 0) {
			if (beginDay > endDay) {
				monthCount = monthCount - 1;
			} else if (beginDay < endDay) {
				monthCount = monthCount + 1;
			}
		} else {
			if (beginDay < endDay) {
				monthCount = monthCount + 1;
			}
		}
		return monthCount;
	}

	/**
	 * 得到两日期相差几个月 包括当前月
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getYearMonthCountBak(String beginDate, String endDate) throws Exception {
		int monthCount;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM");
		Date beginDate1 = fmt.parse(beginDate);
		Calendar beginCal = Calendar.getInstance();
		beginCal.setTime(beginDate1);
		int beginYear = beginCal.get(Calendar.YEAR);
		int beginMonth = beginCal.get(Calendar.MONTH) + 1;
		Date endDate1 = fmt.parse(endDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate1);
		int endYear = endCal.get(Calendar.YEAR);
		int endMonth = endCal.get(Calendar.MONTH) + 1;
		monthCount = (endYear - beginYear) * 12 + (endMonth - beginMonth);
		return monthCount;
	}

	/**
	 * 得到两日期相差几个月 包括当前月
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getYearMonthCountBak(Date beginDate, Date endDate) throws Exception {
		int monthCount;
		Calendar beginCal = Calendar.getInstance();
		beginCal.setTime(beginDate);
		int beginYear = beginCal.get(Calendar.YEAR);
		int beginMonth = beginCal.get(Calendar.MONTH) + 1;
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int endYear = endCal.get(Calendar.YEAR);
		int endMonth = endCal.get(Calendar.MONTH) + 1;
		monthCount = (endYear - beginYear) * 12 + (endMonth - beginMonth);
		return monthCount;
	}

	public static long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	public static long getQuot(Date date1, Date date2) {
		long quot = 0;
		quot = date1.getTime() - date2.getTime();
		quot = quot / 1000 / 60 / 60 / 24;
		return quot;
	}

	/**
	 * 日期相减获取自然月月数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getMonthNum(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return (cal2.get(1) - cal1.get(1)) * 12 + (cal2.get(2) - cal1.get(2));
	}

	public static Object twoMonthsAgo(String dateString) {
		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
		Date date = null;

		try {
			date = yyyyMM.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance();
		if (c != null) {
			c.setTime(date);
			c.add(Calendar.MONTH, -2);
		}
		return yyyyMM.format(c.getTime());
	}

	public static String getAddMonth2(String dateStr, int offset) {
		String calcRslt = "";
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM");
			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(dateStr));
			cal.add(Calendar.MONTH, offset);// 月份加1

			calcRslt = commonDateFormat.format(cal.getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return calcRslt;
	}

	public static Object oneMonthsAgo(String dateString) {
		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
		Date date = null;

		try {
			date = yyyyMM.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance();
		if (c != null) {
			c.setTime(date);
			c.add(Calendar.MONTH, -1);
		}
		return yyyyMM.format(c.getTime());
	}

	/**
	 * 根据时间获取星期几
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(String date) {
		Date dt = convertStringToDate(date, "yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}

	/**
	 * 根据入职时间和当前时间判断信贷员工是否处在保护期 TRUE代表在保护期
	 * 
	 * @param gzYm
	 *            工资年月
	 * @param hireTime
	 *            入职时间
	 * @return
	 */
	public static boolean checkProtection(String gzYm, String hireTime) {
		boolean flag = true;
		try {
			Date gzYmDate = new SimpleDateFormat("yyyy-MM").parse(gzYm);
			Date hiredate = new SimpleDateFormat("yyyy-MM-dd").parse(hireTime);
			Calendar t1 = Calendar.getInstance();
			t1.setTime(gzYmDate);
			int actualMaximum = t1.getActualMaximum(Calendar.DAY_OF_MONTH);
			t1.set(t1.get(Calendar.YEAR), t1.get(Calendar.MONTH), actualMaximum);
			// t1为工资年月的最后一天
			Calendar t2 = Calendar.getInstance();
			t2.setTime(hiredate);
			Calendar t3 = Calendar.getInstance();
			int month = t2.get(Calendar.MONTH);
			int year = t2.get(Calendar.YEAR);
			t3.set(year, month, 15);
			// 判断入职日期是否在15号之前（含15号）
			int c = t2.compareTo(t3);
			// 得到保护期的时间
			if (c == 1) {
				// 当月、下个月、下下个月和下下下个月为该员工的保护期
				Calendar t4 = Calendar.getInstance();
				// 也就是保护期的时间为当前的入职时间加3个月
				t4.setTime(hiredate);
				t4.add(Calendar.MONTH, 3);
				// 去t4加了月份后，在这个月的最后一天。
				t4.set(t4.get(Calendar.YEAR), t4.get(Calendar.MONTH), t4.getActualMaximum(Calendar.DAY_OF_MONTH));
				// 工资年月的最后一天和保护期时间比较，看是否员工是否处在保护期内
				int k = t1.getTime().compareTo(t4.getTime());
				// 当工资年月的最后一天小于等于保护期最后一天的时候，那么这个人就还在保护期
				if (k == 1) {
					flag = false;
				}
			} else {
				// 当月、下个月、下下个月即为保护期
				Calendar t5 = Calendar.getInstance();
				// 保护期的时间为当前的入职时间加2个月
				t5.setTime(hiredate);
				t5.add(Calendar.MONTH, 2);
				t5.set(t5.get(Calendar.YEAR), t5.get(Calendar.MONTH), t5.getActualMaximum(Calendar.DAY_OF_MONTH));
				// 当前时间和保护期时间比较，看是否员工是否处在保护期内
				int k = t1.getTime().compareTo(t5.getTime());
				// 当工资年月的最后一天小于等于保护期最后一天的时候，那么这个人就还在保护期
				if (k == 1) {
					flag = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 根据开业时间和工资年月判断营业部开业时间是否大于6个月 (小于6个月返回false，大于6个月返回true)
	 * 
	 * @param openDate
	 *            时间格式：YYYY-MM-DD
	 * @param gzYm
	 *            时间格式：YYYY-MM
	 */
	public static boolean judgeOpenTimeThanMonth(String openDate, String gzYm) {
		boolean flag = false;
		try {
			openDate.substring(0, 7);
			Date t1 = new SimpleDateFormat("yyyy-MM").parse(openDate.substring(0, 7));
			Date t2 = new SimpleDateFormat("yyyy-MM").parse(gzYm);
			Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(t1);
			int beginYear = beginCal.get(Calendar.YEAR);
			int beginMonth = beginCal.get(Calendar.MONTH);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(t2);
			int endYear = endCal.get(Calendar.YEAR);
			int endMonth = endCal.get(Calendar.MONTH) + 1;
			int monthCount = (endYear - beginYear) * 12 + (endMonth - beginMonth);
			if (monthCount <= 6) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 根据判断是否未入职日期
	 * 
	 * @param entryDate
	 * @return
	 */
	public static Map<String, String> getBecomeDate(String entryDate) throws Exception {
		// 用来装载返回参数
		Map<String, String> map = new HashMap<>();
		// 定义日期格式
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 转换入职日期格式
		Date entrydate = format.parse(entryDate);
		// 获取日历对象
		Calendar cal = Calendar.getInstance();
		// 用来获取当前年份
		int year = cal.get(Calendar.YEAR);
		// 获取当前月份
		int month = cal.get(Calendar.MONTH) + 1;
		cal.setTime(entrydate);
		// 判断日期是否为星期六或者星期天
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			// 如果是，放入map
			map.put("success", "false");
			map.put("message", "入职日期不能为周末");
		} else {
			// 否则,判断是否为这个月的最后一个工作日
			// 当天
			int now = cal.get(Calendar.DAY_OF_MONTH);
			// 最后一天
			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			// 不能是这个月的最后一天
			if (lastDay == now) {
				map.put("success", "false");
				map.put("message", "入职日期不能为这个月的最后一个工作日");
			} else {
				// 如果不是最后一天,判断入职不能是（最后一天为周六或者周日）的前一天/两天
				// 这个月的最后一天
				String lastDateOne = year + "" + month + "" + (lastDay);
				Date date = format.parse(lastDateOne);
				Calendar lastDateCal = Calendar.getInstance();
				lastDateCal.setTime(date);
				// 这个月的倒数第二天
				String lastDateTwo = year + "" + month + "" + (lastDay - 1);
				Date dateTwo = format.parse(lastDateTwo);
				Calendar lastDateCalTwo = Calendar.getInstance();
				lastDateCalTwo.setTime(dateTwo);
				// 这个月的倒数第三天
				String lastDateThree = year + "" + month + "" + (lastDay - 2);
				Date dateThree = format.parse(lastDateThree);
				Calendar lastDateCalThree = Calendar.getInstance();
				lastDateCalThree.setTime(dateThree);
				// 最后一天如果是星期六,那么入职日期不能是最后一天的前一天
				if (lastDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					if (now == lastDateCalTwo.getActualMaximum(Calendar.DAY_OF_MONTH)) {
						map.put("success", "false");
						map.put("message", "入职日期不能为月末周六的前一天");
					} else {
						map.put("success", "true");
						map.put("message", "成功");
						Map<String, String> becomeDate = getAllDate(cal);
						map.put("becomeDate", becomeDate.get("becomeDate"));
						map.put("startDate", format.format(cal.getTime()));
						Map<String, Date> endDate = getEndDate(cal);
						map.put("endDate", format.format(endDate.get("endDate")));
					}
					// 如果最后一天是星期天,那么入职日期不能是最后一天的前两天
				} else if (lastDateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					if (now == lastDateCalThree.getActualMaximum(Calendar.DAY_OF_MONTH)) {
						map.put("success", "false");
						map.put("message", "入职日期不能为月末周日的前两天");
					} else {
						map.put("success", "true");
						map.put("message", "成功");
						Map<String, String> becomeDate = getAllDate(cal);
						map.put("becomeDate", becomeDate.get("becomeDate"));
						map.put("startDate", format.format(cal.getTime()));
						Map<String, Date> endDate = getEndDate(cal);
						map.put("endDate", format.format(endDate.get("endDate")));
					}
				}
			}
		}
		return map;
	}

	/**
	 * 获得转正日期
	 * 
	 * @param cal传入的入职日期
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getAllDate(Calendar cal) throws Exception {
		// 用来装载返回参数
		Map<String, String> map = new HashMap<>();
		// 定义日期格式
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 转正日期
		cal.add(Calendar.MONTH, 3);
		String becomeDate = format.format(cal.getTime());
		map.put("becomeDate", becomeDate);
		return map;
	}

	/**
	 * 获得合同截止日期
	 *
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Date> getEndDate(Calendar cal) throws Exception {
		// 用来装载返回参数
		Map<String, Date> map = new HashMap<>();
		// 定义日期格式
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int year = cal.get(Calendar.YEAR);
		// 从。。开始
		String bDateFrom = year + "-05-31";
		// 到。。结束
		String bDateTo = year + "-12-01";
		Date from = format.parse(bDateFrom);
		Date to = format.parse(bDateTo);
		Calendar after = Calendar.getInstance();
		after.setTime(from);
		Calendar before = Calendar.getInstance();
		before.setTime(to);
		// 如果是从6月1日开始到12月1日（不包括当天）,那么合同截至日期则为三年后的12月1日，否则为三年后的6月1日
		if (cal.after(after) && cal.before(before)) {
			String s = (cal.get(Calendar.YEAR) + 3) + "-12-01";
			map.put("endDate", format.parse((cal.get(Calendar.YEAR) + 3) + "-12-01"));
		} else {
			map.put("endDate", format.parse((cal.get(Calendar.YEAR) + 3) + "-06-01"));
		}
		return map;
	}

	/**
	 * 获取传时间的当年的第一天日期(字符串)
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String getFirstDayStrOfTheYear(Date date, String format) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return getFormatDateStr(cal.getTime(), format);
	}

	/**
	 * 获取传时间的当年的最后一天日期(字符串)
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String getLastDayStrOfTheYear(Date date, String format) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		cal.add(Calendar.YEAR, 1);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return getFormatDateStr(cal.getTime(), format);
	}

}
