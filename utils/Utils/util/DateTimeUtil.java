package com.zhph.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：DateTimeUtil
 * @Description：
 * @company:zhph
 * @author Administrator
 * @date: 2017-4-28 下午2:35:26
 */

public class DateTimeUtil {

    /**
     * 考勤排班用
     */
    private static final Map<Integer, String> DAYMAP = new HashMap<Integer, String>() {
        {
            put(1, "one");
            put(2, "two");
            put(3, "three");
            put(4, "four");
            put(5, "five");
            put(6, "six");
            put(7, "seven");
            put(8, "eight");
            put(9, "nine");
            put(10, "ten");
            put(11, "eleven");
            put(12, "twelve");
            put(13, "thirteen");
            put(14, "fourteen");
            put(15, "fifteen");
            put(16, "sixteen");
            put(17, "seventeen");
            put(18, "eighteen");
            put(19, "nineteen");
            put(20, "twenty");
            put(21, "twentyOne");
            put(22, "twentyTwo");
            put(23, "twentyThree");
            put(24, "twentyFour");
            put(25, "twentyFive");
            put(26, "twentySix");
            put(27, "twentySeven");
            put(28, "twentyEight");
            put(29, "twentyNine");
            put(30, "thirty");
            put(31, "thirtyOne");
        }
    };

    private static final String ZERO = "0";

    private static final String YYYYMMDD = "yyyy-MM-dd";

    public static Map<Integer, String> getDayMap() {
        return DAYMAP;
    }

    private static final Map<Integer, String> weekNameMap = new HashMap<Integer, String>() {
        {
            put(0, "日");
            put(1, "一");
            put(2, "二");
            put(3, "三");
            put(4, "四");
            put(5, "五");
            put(6, "六");
        }
    };

    public static Map<Integer, String> getWeekNameMap() {
        return weekNameMap;
    }

    /**
     * 获取当前日期yyyy-MM-dd HH:mm:ss:如2016-05-13 14:18:32
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        return df.format(new Date());
    }

    /**
     * 返回字符串类型的当天日期。
     *
     * @param space 年月日之间的间隔符号
     * @return 当天日期
     */
    public static String getDate(String space) {
        String dateStr = "";

        Calendar cal = Calendar.getInstance();
        dateStr += cal.get(Calendar.YEAR);
        dateStr += space;
        dateStr +=
                (cal.get(Calendar.MONTH) > 8) ? "" + (cal.get(Calendar.MONTH) + 1) : ZERO + (cal.get(Calendar.MONTH) + 1);
        dateStr += space;
        dateStr +=
                (cal.get(Calendar.DAY_OF_MONTH) > 9) ? "" + (cal.get(Calendar.DAY_OF_MONTH)) : ZERO
                        + (cal.get(Calendar.DAY_OF_MONTH));
        return dateStr;
    }

    /**
     * 返回字符串类型的当天时间
     *
     * @param space 小时分秒之间的间隔符号
     * @return 当天时间
     */
    public static String getTime(String space) {
        String timeStr = "";
        Calendar cal = Calendar.getInstance();
        timeStr +=
                (cal.get(Calendar.HOUR_OF_DAY) > 9) ? "" + (cal.get(Calendar.HOUR_OF_DAY)) : ZERO
                        + (cal.get(Calendar.HOUR_OF_DAY));
        timeStr += space;
        timeStr += (cal.get(Calendar.MINUTE) > 9) ? "" + (cal.get(Calendar.MINUTE)) : ZERO + (cal.get(Calendar.MINUTE));
        timeStr += space;
        timeStr += (cal.get(Calendar.SECOND) > 9) ? "" + (cal.get(Calendar.SECOND)) : ZERO + (cal.get(Calendar.SECOND));
        return timeStr;
    }

    /**
     * 返回字符串类型的当天的日期时间
     *
     * @param dateSpace 年月日之间的间隔符号
     * @param middleSpace 日期和时间之间的间隔符号
     * @param timeSpace 小时分秒之间的间隔符号
     * @return 当天日期时间
     */

    public static String getDateTime(String dateSpace, String middleSpace, String timeSpace) {
        String dateStr = "";
        Calendar cal = Calendar.getInstance();
        dateStr += cal.get(Calendar.YEAR);
        dateStr += dateSpace;
        dateStr +=
                (cal.get(Calendar.MONTH) > 8) ? "" + (cal.get(Calendar.MONTH) + 1) : ZERO + (cal.get(Calendar.MONTH) + 1);
        dateStr += dateSpace;
        dateStr +=
                (cal.get(Calendar.DAY_OF_MONTH) > 9) ? "" + (cal.get(Calendar.DAY_OF_MONTH)) : ZERO
                        + (cal.get(Calendar.DAY_OF_MONTH));

        String timeStr = "";
        // Calendar cal = Calendar.getInstance();
        timeStr +=
                (cal.get(Calendar.HOUR_OF_DAY) > 9) ? "" + (cal.get(Calendar.HOUR_OF_DAY)) : ZERO
                        + (cal.get(Calendar.HOUR_OF_DAY));
        timeStr += timeSpace;
        timeStr += (cal.get(Calendar.MINUTE) > 9) ? "" + (cal.get(Calendar.MINUTE)) : ZERO + (cal.get(Calendar.MINUTE));
        timeStr += timeSpace;
        timeStr += (cal.get(Calendar.SECOND) > 9) ? "" + (cal.get(Calendar.SECOND)) : ZERO + (cal.get(Calendar.SECOND));

        String dateTimeStr = dateStr + middleSpace + timeStr;

        return dateTimeStr;
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
        String monthstr =
                (cal.get(Calendar.MONTH) > 8) ? "" + (cal.get(Calendar.MONTH) + 1) : ZERO + (cal.get(Calendar.MONTH) + 1);
        int month = Integer.parseInt(monthstr);
        return month;
    }

    /**
     * 返回整型的月份
     *
     * @return 下一月份
     */
    public static int getNextMonth() {
        Calendar cal = Calendar.getInstance();
        String monthstr =
                (cal.get(Calendar.MONTH) > 8) ? "" + (cal.get(Calendar.MONTH) + 1) : ZERO + (cal.get(Calendar.MONTH) + 1);
        int month = Integer.parseInt(monthstr) + 1;
        return month;
    }

    /**
     * 返回整型的天
     *
     * @return 当前的日期
     */
    public static int getDay() {

        Calendar cal = Calendar.getInstance();
        String daystr =
                (cal.get(Calendar.DAY_OF_MONTH) > 9) ? "" + (cal.get(Calendar.DAY_OF_MONTH)) : ZERO
                        + (cal.get(Calendar.DAY_OF_MONTH));
        int day = Integer.parseInt(daystr);
        return day;
    }

    /**
     * 返回整型的天
     *
     * @return 下一天
     */
    public static int getNextDay() {

        Calendar cal = Calendar.getInstance();
        String daystr =
                (cal.get(Calendar.DAY_OF_MONTH) > 9) ? "" + (cal.get(Calendar.DAY_OF_MONTH)) : ZERO
                        + (cal.get(Calendar.DAY_OF_MONTH));
        int day = Integer.parseInt(daystr) + 1;
        return day;
    }

    public static Calendar getDateFromIDCard(String iDCardNum) {
        int year;
        int month;
        int day;
        int idLength = iDCardNum.length();
        Calendar cal = Calendar.getInstance();

        if (idLength == 18) {
            year = Integer.parseInt(iDCardNum.substring(6, 10));
            month = Integer.parseInt(iDCardNum.substring(10, 12));
            day = Integer.parseInt(iDCardNum.substring(12, 14));
        } else if (idLength == 15) {
            year = Integer.parseInt(iDCardNum.substring(6, 8)) + 1900;
            month = Integer.parseInt(iDCardNum.substring(8, 10));
            day = Integer.parseInt(iDCardNum.substring(10, 12));
        } else {
            return null;
        }
        cal.set(year, month, day);
        return cal;
    }

    public static int getWorkDay(String d1, String d2) {
        String formatStr = YYYYMMDD;
        java.util.Date date1 = convertStringToDate(d1, formatStr);
        java.util.Date date2 = convertStringToDate(d2, formatStr);
        return getWorkDay(date1, date2);
    }

    public static int getWorkDay(Date d1, Date d2) {
        int[] freeDays = {0, 6};// default: Sunday and Saturday are the free days.
        return getWorkDay(d1, d2, freeDays);
    }

    public static int getFreeDay(Date date, int dNum) {
        int[] freeDays = {0, 6};// default: Sunday and Saturday are the free days.
        return getFreeDay(date, dNum, freeDays);
    }

    public static int getWorkDay(Date d1, Date d2, int[] freeDays) {
        int dNum = 0;
        dNum = (int)((d2.getTime() - d1.getTime()) / 1000 / 60 / 60 / 24) + 1;

        return dNum - getFreeDay(d1, dNum, freeDays);
    }

    public static int getFreeDay(Date date, int dNum, int[] freeDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int start = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int freeNum = 0;
        for (int i = 0; i < dNum; i++) {
            for (int j = 0; j < freeDays.length; j++) {
                if ((start + i) % 7 == freeDays[j]) {
                    freeNum++;
                }
            }
        }
        return freeNum;
    }

    public static HashMap<String, Integer> geWeekDay(String d1, String d2) {
        String formatStr = YYYYMMDD;
        java.util.Date date1 = convertStringToDate(d1, formatStr);
        java.util.Date date2 = convertStringToDate(d2, formatStr);
        return geWeekDay(date1, date2);
    }

    public static HashMap<String, Integer> geWeekDay(Date d1, Date d2) {
        int dNum = 0;
        dNum = (int)((d2.getTime() - d1.getTime()) / 1000 / 60 / 60 / 24) + 1;

        return geWeekDay(d1, dNum);
    }

    public static HashMap<String, Integer> geWeekDay(Date date, int dNum) {
        Calendar cal = Calendar.getInstance();
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        cal.setTime(date);
        int mon = 0;
        int tues = 0;
        int wed = 0;
        int thurs = 0;
        int fri = 0;
        int sat = 0;
        int sun = 0;
        int start = cal.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = 0; i < dNum; i++) {
            int tmp = start + i;
            if (tmp % 7 == 1) {
                mon++;
            }
            if (tmp % 7 == 2) {
                tues++;
            }
            if (tmp % 7 == 3) {
                wed++;
            }
            if (tmp % 7 == 4) {
                thurs++;
            }
            if (tmp % 7 == 5) {
                fri++;
            }
            if (tmp % 7 == 6) {
                sat++;
            }
            if (tmp % 7 == 0) {
                sun++;
            }
        }
        hashMap.put("1", mon);
        hashMap.put("2", tues);
        hashMap.put("3", wed);
        hashMap.put("4", thurs);
        hashMap.put("5", fri);
        hashMap.put("6", sat);
        hashMap.put("7", sun);
        return hashMap;
    }

    /**
     * 日期相加
     *
     * @param date 日期
     * @param offset 天数
     * @return 返回相加后的日期
     */
    public static Date changeDay(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + offset);
        return calendar.getTime();
    }

    public static String changeDay(String datestr, int offset) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(YYYYMMDD);
        String changedatestr =
                bartDateFormat.format(changeDay(convertStringToDate(datestr, YYYYMMDD), offset)).toString();
        return changedatestr;
    }

    public static Calendar changeDay(Calendar calendar, int offset) {
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + offset);
        return calendar;
    }

    public static Date minusDay(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - offset);
        return calendar.getTime();
    }

    public static String minusDay(String datestr, int offset) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(YYYYMMDD);
        String minusdatestr =
                bartDateFormat.format(minusDay(convertStringToDate(datestr, YYYYMMDD), offset)).toString();
        return minusdatestr;
    }

    // 判断两个日期是否在同一周
    static boolean isSameWeekDates(Date date1, Date date2) {
        long diff = getMonday(date1).getTime() - getMonday(date2).getTime();
        if (Math.abs(diff) < 1000 * 60 * 60 * 24) {
            return true;
        } else {
            return false;
        }
    }

    // 获得周一的日期
    public static Date getMonday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c.getTime();
    }

    /**
     * 日期相减
     *
     * @param date 日期
     * @param date1 日期
     * @return 返回相减后的日期
     */
    public static int getDiffDate(java.util.Date date, java.util.Date date1) {
        return (int)((date.getTime() - date1.getTime()) / (24 * 3600 * 1000));
    }

    public static int getDiffDate(Calendar date, Calendar date1) {
        return getDiffDate(date.getTime(), date1.getTime());
    }

    public static int getCurrentWeek(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat(YYYYMMDD);
        int flag = 8;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(format.parse(dateStr));
            flag = cal.get(Calendar.DAY_OF_WEEK) - 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static int getMinute(String startMin, String endMin) {
        int min = 0;
        try {
            String starthourstr = startMin.substring(0, 2);
            String endhourstr = endMin.substring(0, 2);
            String startminstr = startMin.substring(3);
            String endminstr = endMin.substring(3);
            int starthour = Integer.parseInt(starthourstr);
            int endhour = Integer.parseInt(endhourstr);
            int startmin = Integer.parseInt(startminstr);
            int endmin = Integer.parseInt(endminstr);
            int tmpmin = 0;
            int tmphour = 0;
            if (endmin >= startmin) {
                tmpmin = endmin - startmin;
                tmphour = endhour - starthour;
                min = tmphour * 60 + tmpmin;
            } else {
                tmpmin = 60 + endmin - startmin;
                tmphour = endhour - 1 - starthour;
                min = tmphour * 60 + tmpmin;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return min;
    }

    public static boolean getTimeCompare(String startTime, String endTime) {
        boolean flag = false;
        try {
            String starthourstr = startTime.substring(0, 2);
            String endhourstr = endTime.substring(0, 2);
            String startminstr = startTime.substring(3);
            String endminstr = endTime.substring(3);
            int starthour = Integer.parseInt(starthourstr);
            int endhour = Integer.parseInt(endhourstr);
            int startmin = Integer.parseInt(startminstr);
            int endmin = Integer.parseInt(endminstr);

            int starttotal = starthour * 60 + startmin;
            int endtotal = endhour * 60 + endmin;
            Date date = new Date();
            int currenthour = getHour(date);
            int currentmin = getMinute(date);
            int currenttotal = currenthour * 60 + currentmin;
            if ((currenttotal >= starttotal) && (currenttotal <= endtotal)) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean isLeapYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 2, 1);
        calendar.add(Calendar.DATE, -1);
        if (calendar.get(Calendar.DAY_OF_MONTH) == 29) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 格式化日期
     *
     * @param dateStr 字符型日期
     * @param formatStr 格式
     * @return 返回日期
     */
    public static java.util.Date convertStringToDate(String dateStr, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期
     *
     * @param dateStr 字符型日期 格式
     * @return 返回日期
     */
    public static java.util.Date convertStringToDate(String dateStr) {
        return convertStringToDate(dateStr, YYYYMMDD);
    }

    /**
     * 获取本月工作日天数
     *
     * @param year 年
     * @param month 月
     * @return
     */
    public static int getCurrentMonthWorkingDays(int year, int month) {
        List<Date> dates = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
                dates.add((Date)cal.getTime().clone());
            }
            cal.add(Calendar.DATE, 1);
        }
        return dates.size();
    }

    /**
     * 获取当月休息日期
     *
     * @param year
     * @param month
     * @return
     */
    public static List<Integer> getCurrentMonthRestDays(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        List<Integer> list = new ArrayList<>();
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SUNDAY || day == Calendar.SATURDAY) {
                list.add(cal.get(Calendar.DAY_OF_MONTH));
            }
            cal.add(Calendar.DATE, 1);
        }
        return list;
    }

    /**
     * 考勤排班 获取当月周末对应XjTimeAutomated 属性
     *
     * @param year
     * @param month
     * @return
     */
    public static Map<String, Object> getWeekendDayMap(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        Map<String, Object> map = new HashMap<>();
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SUNDAY || day == Calendar.SATURDAY) {
                map.put(DAYMAP.get(cal.get(Calendar.DAY_OF_MONTH)), 1);
            }
            cal.add(Calendar.DATE, 1);
        }
        return map;
    }

    /**
     * 考勤排班 获取当月工作日对应XjTimeAutomated 属性
     *
     * @param year
     * @param month
     * @return
     */
    public static Map<String, Object> getWorkDayMap(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        Map<String, Object> map = new HashMap<>();
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day != Calendar.SUNDAY && day != Calendar.SATURDAY) {
                map.put(DAYMAP.get(cal.get(Calendar.DAY_OF_MONTH)), 1);
            }
            cal.add(Calendar.DATE, 1);
        }
        return map;
    }

    /**
     * 获取当月所有天数
     *
     * @param year 年
     * @param month 月
     * @return
     */
    public static int getCurrentMonthAllDays(int year, int month) {
        List<Date> dates = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
            dates.add((Date)cal.getTime().clone());
            cal.add(Calendar.DATE, 1);
        }
        return dates.size();
    }

    /**
     * 获取当月对应周几
     *
     * @param year
     * @param month
     * @return
     */
    public static List<Map<String, String>> getCurrentMonthAllWeekName(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        List<Map<String, String>> list = new ArrayList<>();
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
            Map<String, String> map = new HashMap<String, String>();
            map.put(DAYMAP.get(cal.get(Calendar.DAY_OF_MONTH)), weekNameMap.get(cal.get(Calendar.DAY_OF_WEEK) - 1));
            list.add(map);
            cal.add(Calendar.DATE, 1);
        }
        return list;
    }

    /**
     * 用于考勤排班统计 获取当月实际每天对应考勤天代码
     *
     * @param year
     * @param month
     * @return
     */
    public static Map<String, Object> getCurrentDayAllMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        Map<String, Object> map = new HashMap<>();
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
            map.put(DAYMAP.get(cal.get(Calendar.DAY_OF_MONTH)), 1);
            cal.add(Calendar.DATE, 1);
        }
        return map;
    }

    /**
     * 获取当月对应周几
     *
     * @param year
     * @param month
     * @return
     */
    public static Map<Integer, String> getCurrentMonthAllWeekNameBak(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        Map<Integer, String> map = new HashMap<Integer, String>();
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
            map.put(cal.get(Calendar.DAY_OF_MONTH), weekNameMap.get(cal.get(Calendar.DAY_OF_WEEK) - 1));
            cal.add(Calendar.DATE, 1);
        }
        return map;
    }

    /**
     * 以指定的格式来格式化日期
     *
     * @param date Date
     * @param format String
     * @return String
     */
    public static String convertDateToString(java.util.Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 常用的格式化日期
     *
     * @param date Date
     * @return String
     */
    public static String convertDateToString(java.util.Date date) {
        return convertDateToString(date, YYYYMMDD);
    }

    /**
     * 返回年份
     *
     * @param date 日期
     * @return 返回年份
     */
    public static int getYear(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.YEAR);
    }

    /**
     * 返回月份
     *
     * @param date 日期
     * @return 返回月份
     */
    public static int getMonth(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.MONTH) + 1;
    }

    /**
     * 返回下一月
     *
     * @param currentdate 日期
     * @return 返回月份
     */
    public static String getNextMonth(String currentdate) {
        java.util.Calendar currentCalendar = java.util.Calendar.getInstance();
        currentCalendar.setTime(convertStringToDate(currentdate));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentCalendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date = new Date(calendar.getTimeInMillis());
        String nextmonth = convertDateToString(date);
        return nextmonth;
    }

    /**
     * 返回日份
     *
     * @param date 日期
     * @return 返回日份
     */
    public static int getDay(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回小时
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.HOUR_OF_DAY);
    }

    /**
     * 返回分钟
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.MINUTE);
    }

    /**
     * 返回秒钟
     *
     * @param date 日期
     * @return 返回秒钟
     */
    public static int getSecond(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.SECOND);
    }

    /**
     * 返回毫秒
     *
     * @param date 日期
     * @return 返回毫秒
     */
    public static long getMillis(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 返回当天是星期几
     *
     * @param
     * @return 返回星期几
     */
    public static int getCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return week;
    }

    // ****************************************************************

    /**
     * 日期相加
     *
     * @param date 日期
     * @param day 天数
     * @return 返回相加后的日期
     */
    public static java.util.Date addDate(java.util.Date date, int day) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long)day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 日期相减
     *
     * @param date 日期
     * @param date1 日期
     * @return 返回相减后的日期
     */
    public static int diffDate(java.util.Date date, java.util.Date date1) {
        return (int)((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }

    public static String calcHMSSTR(String beginDate, String endDate) {

        String hmsResult = "";
        try {
            Date dateFrom;
            Date dateTo;
            DateFormat df = DateFormat.getDateTimeInstance();
            dateFrom = df.parse(beginDate);
            dateTo = df.parse(endDate);

            long lTime = (dateTo.getTime() - dateFrom.getTime()) / 1000;
            if (lTime >= 0) {
                int timeInSeconds = (int)lTime;
                int hours;
                int minutes;
                int seconds;
                hours = timeInSeconds / 3600;
                timeInSeconds = timeInSeconds - (hours * 3600);
                minutes = timeInSeconds / 60;
                timeInSeconds = timeInSeconds - (minutes * 60);
                seconds = timeInSeconds;
                if ((hours == 0) && (minutes != 0) && (seconds != 0)) {
                    hmsResult = minutes + "分钟" + seconds + "秒";
                }
                if ((hours == 0) && (minutes != 0) && (seconds == 0)) {
                    hmsResult = minutes + "分钟";
                }
                if ((hours == 0) && (minutes == 0) && (seconds != 0)) {
                    hmsResult = seconds + "秒";
                }
                if ((hours == 0) && (minutes == 0) && (seconds == 0)) {
                    hmsResult = null;
                }
                if ((hours != 0) && (minutes != 0) && (seconds != 0)) {
                    hmsResult = hours + "小时" + minutes + "分钟" + seconds + "秒";
                }
                if ((hours != 0) && (minutes == 0) && (seconds != 0)) {
                    hmsResult = hours + "小时" + seconds + "秒";
                }
                if ((hours != 0) && (minutes != 0) && (seconds == 0)) {
                    hmsResult = hours + "小时" + minutes + "分钟";
                }
                if ((hours != 0) && (minutes == 0) && (seconds == 0)) {
                    hmsResult = hours + "小时";
                }
            }
        } catch (Exception e) {
            // System.out.println("calcHMSSTRError===" + e.getMessage());
            e.printStackTrace();
        }
        return hmsResult;
    }

    /**
     * 比较前后两个时间大小
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean compareDate(String date1, String date2, String timeFormat) {
        DateFormat df = new SimpleDateFormat(timeFormat);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            return dt1.getTime() >= dt2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 月份加减
     *
     * @param date
     * @param addMonth
     * @return
     * @throws ParseException
     */
    public static String getDateAddMonth(String date, int addMonth)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        if (addMonth != 0) {
            rightNow.add(Calendar.MONTH, addMonth);
        }
        return sdf.format(rightNow.getTime());
    }

    /**
     * 获取指定时间到当月月底天数 包括当前日期
     *
     * @param date
     * @return
     */
    public static int getDaysFromDateForMonth(String date) {
        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);
        Calendar cal = Calendar.getInstance();
        int days = 0;
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, day);
        while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
            days++;
            cal.add(Calendar.DATE, 1);
        }
        return days;
    }

    /**
     * 根据日 和offset获取daymap
     * @param day
     * @param offset 1正序 -1反序
     * @return
     */
    public static List<String> getDAYMAPListForAppointDay(int day,int offset){
        List<String> list = new ArrayList<>();
        for (Map.Entry<Integer,String> entry : DAYMAP.entrySet()){
            if (offset==1){
                if (entry.getKey()>=day){
                    list.add(entry.getValue());
                }
            }else {
                if (entry.getKey()<=day){
                    list.add(entry.getValue());
                }
            }
        }
        return list;
    }
}
