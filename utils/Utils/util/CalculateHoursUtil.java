package com.zhph.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhph.model.common.RankType;
import com.zhph.model.sys.SysHoliday;


public class CalculateHoursUtil {
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // 这里的格式可以自己设置
    
    // 设置上班时间：该处时间可以根据实际情况进行调整
    public static final int abh = 9;// 上午上班时间,小时
    public static final int abm = 00;// 上午上班时间,分钟
    public static final int aeh = 12;// 上午下班时间，小时
    public static final int aem = 0;// 上午下班时间，分钟
    public static final int pbh = 13;// 下午上班时间，小时
    public static final int pbm = 00;// 下午上班时间，分钟
    public static final int peh = 18;// 下午下班时间，小时
    public static final int pem = 00;// 下午下班时间，分钟
    public static final float h1 = abh + (float)abm / 60;
    public static final float h2 = aeh + (float)aem / 60;
    public static final float h3 = pbh + (float)pbm / 60;
    public static final float h4 = peh + (float)pem / 60;
    public static final float hoursPerDay = h2 - h1 + (h4 - h3);// 每天上班小时数
    public static final int daysPerWeek = 7;// 每周工作天数 客户经理和团队经理
    public static final int daysPerweekByOthers = 5;// 除开客户经理和团队经理之外的人
    public static final long milsecPerDay = 1000 * 60 * 60 * 24;// 每天的毫秒数
    public static final float hoursPerWeek = hoursPerDay * daysPerWeek;// 每星期小时数
    public static final float hoursPerWeekbyOthers = hoursPerDay * daysPerweekByOthers;// 每星期小时数 othsers
    public static void main(String[] args) {
		System.out.println((float)56/60);
	}
    /**
     * 客户经理团队对经理的请假时间计算
     * @date 2016-12-14 下午3:28:35
     * @throws
     */
    public static float calculateHours(Date beginTime, Date endTime) {
        // 对输入的字符串形式的时间进行转换
        Date t1 = beginTime;// 真实开始时间
        Date t2 = endTime;// 真实结束时间
        //对时间进行预处理
        t1 = processBeginTime(t1);
        t2 = processEndTime(t2);
        // 若开始时间晚于结束时间，返回0
        if (t1.getTime() > t2.getTime()) {
            return 0;
        }
        // 开始时间到结束时间的完整星期数
        int weekCount = (int)((t2.getTime() - t1.getTime()) / (milsecPerDay * 7));
        float totalHours = 0;
        totalHours += weekCount * hoursPerWeek;
        // 调整结束时间，使开始时间和结束时间在一个星期的周期之内
        t2.setTime(t2.getTime() - weekCount * 7 * milsecPerDay);
        int dayCounts = 0;// 记录开始时间和结束时间之间工作日天数
        // 调整开始时间，使得开始时间和结束时间在同一天，或者相邻的工作日内。
        while (t1.getTime() <= t2.getTime()) {
            Date temp = new Date(t1.getTime() + milsecPerDay);
            temp = processBeginTime(temp);
            temp.setHours(t1.getHours());
            temp.setMinutes(t1.getMinutes());
            if (temp.getTime() > t2.getTime()) {
                break;
            } else {
                t1 = temp;
                dayCounts++;
            }
        }
        totalHours += dayCounts * hoursPerDay;
        float hh1 = t1.getHours() + (float)t1.getMinutes() / 60;
        float hh2 = t2.getHours() + (float)t2.getMinutes() / 60;
        // 处理开始结束是同一天的情况
        if (t1.getDay() == t2.getDay()) {
            float tt = 0;
            tt = hh2 - hh1;
            if (hh1 >= h1 && hh1 <= h2 && hh2 >= h3) {
                tt = tt - (h3 - h2);
            }
            totalHours += tt;
        } else {
            // 处理开始结束不是同一天的情况
            float tt1 = h4 - hh1;
            float tt2 = hh2 - h1;
            if (hh1 <= h2) {
                tt1 = tt1 - (h3 - h2);
            }
            if (hh2 >= h3) {
                tt2 = tt2 - (h3 - h2);
            }
            totalHours += (tt1 + tt2);
        }
        return totalHours;
    }
    
    // 调整结束时间，使开始时间和结束时间在一个星期的周期之内
    @SuppressWarnings("unused")
    private static String printDate(Date t) {
        String str;
        String xingqi = null;
        switch (t.getDay()) {
            case 0:
                xingqi = "星期天";
                break;
            case 1:
                xingqi = "星期一";
                break;
            case 2:
                xingqi = "星期二";
                break;
            case 3:
                xingqi = "星期三";
                break;
            case 4:
                xingqi = "星期四";
                break;
            case 5:
                xingqi = "星期五";
                break;
            case 6:
                xingqi = "星期六";
                break;
            default:
                break;
        }
        str = format.format(t) + "  " + xingqi;
        return str;
    }

    // 对结束时间进行预处理
    public static Date processEndTime(Date t) {
    	float h = t.getHours() + (float)t.getMinutes() / 60;
        if (h > h3) {//13点后为下午下班时间
        	t.setHours(peh);
        	t.setMinutes(pem);
        } else if (h > h1 && h <= h3) {// 若开始时间介于中午休息时间及上午上班时间内，都设置为上午下班时间,及半天
        	t.setHours(aeh);
        	t.setMinutes(aem);
        } else if (t.getHours() <= h1) {// 若开始时间早于上午上班时间结束时间设置为前一天下班时间
        	t.setTime(t.getTime() - milsecPerDay);
        	t.setHours(peh);
        	t.setMinutes(pem);
        }
        return t;
    }
    
    // 对开始时间进行预处理
    public static Date processBeginTime(Date t) {
        float h = t.getHours() + (float)t.getMinutes() / 60;
        // 若结束时间晚于下午下班时间，将其设置为下午下班时间
        if (h >= h4) {//18点后为第二天9点
        	t.setTime(t.getTime() + milsecPerDay);
        	t.setHours(abh);
        	t.setMinutes(abm);
        } else if (h >= h2 && h < h4) {// 若开始时间介于中午休息时间及下午上班时间内，都设置为下午上班时间,及半天
            	t.setHours(pbh);
            	t.setMinutes(pbm);
        } else if (t.getHours() < h2) {// 若开始时间早于上午下班时间，将hour设置为上午上班时间
            	t.setHours(abh);
            	t.setMinutes(abm);
        }
        return t;
    }
    
    /**
     * 职级类别为行政专员、营业部经理、城市经理请假时间的计算方法
     */
    public static float calculateHoursByOthsers(Date beginTime, Date endTime) {
        // 对输入的字符串形式的时间进行转换
        Date t1 = beginTime;// 真实开始时间
        Date t2 = endTime;// 真实结束时间
        // 对时间进行预处理
        t1 = processBeginTimeByothers(t1);
        t2 = processEndTimeByothers(t2);
        // 若开始时间晚于结束时间，返回0
        if (t1.getTime() > t2.getTime()) {
            return 0;
        }
        // 开始时间到结束时间的完整星期数
        int weekCount = (int)((t2.getTime() - t1.getTime()) / (milsecPerDay * 7));
        float totalHours = 0;
        totalHours += weekCount * hoursPerWeekbyOthers;
        // 调整结束时间，使开始时间和结束时间在一个星期的周期之内
        t2.setTime(t2.getTime() - weekCount * 7 * milsecPerDay);
        int dayCounts = 0;// 记录开始时间和结束时间之间工作日天数
        // 调整开始时间，使得开始时间和结束时间在同一天，或者相邻的工作日内。
        while (t1.getTime() <= t2.getTime()) {
            Date temp = new Date(t1.getTime() + milsecPerDay);
            temp = processBeginTimeByothers(temp);
            temp.setHours(t1.getHours());
            temp.setMinutes(t1.getMinutes());
            if (temp.getTime() > t2.getTime()) {
                break;
            } else {
                t1 = temp;
                dayCounts++;
            }
        }
        totalHours += dayCounts * hoursPerDay;
        float hh1 = t1.getHours() + (float)t1.getMinutes() / 60;
        float hh2 = t2.getHours() + (float)t2.getMinutes() / 60;
        // 处理开始结束是同一天的情况
        if (t1.getDay() == t2.getDay()) {
            float tt = 0;
            tt = hh2 - hh1;
            if (hh1 >= h1 && hh1 <= h2 && hh2 >= h3) {
                tt = tt - (h3 - h2);
            }
            totalHours += tt;
        } else {
            // 处理开始结束不是同一天的情况
            float tt1 = h4 - hh1;
            float tt2 = hh2 - h1;
            if (hh1 <= h2) {
                tt1 = tt1 - (h3 - h2);
            }
            if (hh2 >= h3) {
                tt2 = tt2 - (h3 - h2);
            }
            totalHours += (tt1 + tt2);
        }
        return totalHours;
    }
    
    // 对结束时间进行预处理
    private static Date processEndTimeByothers(Date t) {
        float h = t.getHours() + (float)t.getMinutes() / 60;
        // 若结束时间晚于下午下班时间，将其设置为下午下班时间
        if (h >= h4) {
            t.setHours(peh);
            t.setMinutes(pem);
        } else {
            // 若结束时间介于中午休息时间，那么设置为上午下班时间
            if (h >= h2 && h <= h3) {
                t.setHours(aeh);
                t.setMinutes(aem);
            } else {
                // 若结束时间早于上午上班时间，日期向前推一天，并将时间设置为下午下班时间
                if (t.getHours() <= h1) {
                    t.setTime(t.getTime() - milsecPerDay);
                    t.setHours(peh);
                    t.setMinutes(pem);
                }
            }
        }
        // 若开始时间是周六，那么后移两天下周一的下午下班时间
        if (t.getDay() == 6) {
            t.setTime(t.getTime() + milsecPerDay * 2);
            t.setHours(abh);
            t.setMinutes(abm);
        }
        // 若开始时间是周末，那么将开始时间向后推移到最近的工作日的上午上班时间
        if (t.getDay() == 0) {
            t.setTime(t.getTime() + milsecPerDay * 1);
            t.setHours(abh);
            t.setMinutes(abm);
        }
        return t;
    }
    
    // 对开始时间进行预处理
    private static Date processBeginTimeByothers(Date t) {
        float h = t.getHours() + (float)t.getMinutes() / 60;
        // 若开始时间晚于下午下班时间，将开始时间向后推一天
        if (h >= h4) {
            t.setTime(t.getTime() + milsecPerDay);
            t.setHours(abh);
            t.setMinutes(abm);
        } else {
            // 若开始时间介于中午休息时间，那么设置为下午上班时间
            if (h >= h2 && h <= h3) {
                t.setHours(pbh);
                t.setMinutes(pbm);
            } else {
                // 若开始时间早于上午上班时间，将hour设置为上午上班时间
                if (t.getHours() <= h1) {
                    t.setHours(abh);
                    t.setMinutes(abm);
                }
            }
        }
        // 若开始时间是周六，那么后移两天下周一的下午下班时间
        if (t.getDay() == 6) {
            t.setTime(t.getTime() + milsecPerDay * 2);
            t.setHours(abh);
            t.setMinutes(abm);
        }
        // 若开始时间是周末，那么将开始时间向后推移到最近的工作日的上午上班时间
        if (t.getDay() == 0) {
            t.setTime(t.getTime() + milsecPerDay * 1);
            t.setHours(abh);
            t.setMinutes(abm);
        }
        return t;
    }
    
    /**
     * 对遇到周六周末进行处理
     */
    public static void setTimeday(Date t)
        throws Exception {
            // 若开始时间是周六，那么后移两天下周一的下午下班时间
            if (t.getDay() == 6) {
                t.setTime(t.getTime() + milsecPerDay * 2);
                t.setHours(abh);
                t.setMinutes(abm);
            }
            // 若开始时间是周末，那么将开始时间向后推移到最近的工作日的上午上班时间
            if (t.getDay() == 0) {
                t.setTime(t.getTime() + milsecPerDay * 1);
                t.setHours(abh);
                t.setMinutes(abm);
            }
    }
    
    /**
     * 将字符串形式的时间转换成Date形式的时间
     * 
     * @param time
     * @return
     */
    private static Date stringToDate(String time) {
        try {
            return format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 计算跨月的请休假 客户代表和团队经理
     */
	public Map<String, Object> calculateHoursBydifferentMonth(String startTime, String endTime, String rankType,
        List<SysHoliday> list) {
        Date t1 = stringToDate(startTime + ":00");
        Date t2 = stringToDate(endTime + ":00");
        Map<String, Object> maps = new HashMap<String, Object>();
        Map<String, String> keymap=new HashMap<>();
        keymap.put("key", endTime + ":00");
        BigDecimal dayinlastMonth = new BigDecimal("0");
        BigDecimal dayinnextMonth = new BigDecimal("0");
        // t1的开始时间
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("t1", t1);
        while (t1.getMonth() != t2.getMonth()) {
            // 每次加一天
            Date temp = new Date(t1.getTime() + milsecPerDay);
            temp.setHours(t1.getHours());
            temp.setMinutes(t1.getMinutes());
            // 当t1循坏加一天后得到的月份等于结束时间月份之后跳出循坏
            if (temp.getMonth() == t2.getMonth()) {
                map.put("temp", temp);
                break;
            } else {
                t1 = temp;
            }
        }
        Date t3=(Date)map.get("temp");
        Date t1Bymap = (Date)map.get("t1");
        String value = keymap.entrySet().iterator().next().getValue();
        Date t2Bymap = stringToDate(value);
        if (t3.getHours() == abh) {
            // 向前移一天
            Date t4 = new Date(t3.getTime() - milsecPerDay);
            
            // 设置为下午下班时间
            t4.setHours(peh);
            t4.setMinutes(pem);
            // 已知t1-t4; t3-t2;
            float beforMonthHours = calculateHours(t1Bymap, t4);
            float afterMonthHours = calculateHours(t3, t2);
            
            float aTime = calculateHoursOthersStaff(t1Bymap, t4, list);
            float bTime = calculateHoursOthersStaff(t3, t2Bymap, list);
            // 4 5 客户代表 团队经理
            if (RankType.TEAM_MANAGER.getNum().equals(rankType) || RankType.CUSTOMER_STAND.getNum().equals(rankType)) {
                dayinlastMonth = getTotalDay(beforMonthHours);
                dayinnextMonth = getTotalDay(afterMonthHours);
                maps.put("startTimecorspMonth", dayinlastMonth);
                maps.put("endTimecorspMonth", dayinnextMonth);
            }else  if (RankType.CITY_MANAGER.getNum().equals(rankType) || RankType.SALES_MANAGER.getNum().equals(rankType) || RankType.ADMIN_STAFF.getNum().equals(rankType) ) {//// 236 others
                dayinlastMonth = getTotalDay(aTime);
                dayinnextMonth = getTotalDay(bTime);
                maps.put("startTimecorspMonth", dayinlastMonth);
                maps.put("endTimecorspMonth", dayinnextMonth);
            }else{
            	dayinlastMonth = getTotalDay(aTime);
            	dayinnextMonth = getTotalDay(bTime);
            	maps.put("startTimecorspMonth", dayinlastMonth);
            	maps.put("endTimecorspMonth", dayinnextMonth);
            }
        } else {
            // 向前移一天
            Date t4 = new Date(t3.getTime() - milsecPerDay);
            // t3设置为上午上班时间
            t3.setHours(abh);
            t3.setMinutes(abm);
            // t4设置为下午上班时间
            t4.setHours(peh);
            t4.setMinutes(pem);
            float beforMonthHours = calculateHours(t1Bymap, t4);
            float afterMonthHours = calculateHours(t3, t2);
            float aTime = calculateHoursOthersStaff(t1Bymap, t4, list);
            float bTime = calculateHoursOthersStaff(t3, t2Bymap, list);
            // 4 5 客户经理 团队代表
            if (RankType.TEAM_MANAGER.getNum().equals(rankType) || RankType.CUSTOMER_STAND.getNum().equals(rankType)) {
                dayinlastMonth = getTotalDay(beforMonthHours);
                dayinnextMonth = getTotalDay(afterMonthHours);
                maps.put("startTimecorspMonth", dayinlastMonth);
                maps.put("endTimecorspMonth", dayinnextMonth);
            }else  if (RankType.CITY_MANAGER.getNum().equals(rankType) || RankType.SALES_MANAGER.getNum().equals(rankType) || RankType.ADMIN_STAFF.getNum().equals(rankType) ) {//// 236 others
                dayinlastMonth = getTotalDay(aTime);
                dayinnextMonth = getTotalDay(bTime);
                maps.put("startTimecorspMonth", dayinlastMonth);
                maps.put("endTimecorspMonth", dayinnextMonth);
            }else{
            	dayinlastMonth = getTotalDay(aTime);
            	dayinnextMonth = getTotalDay(bTime);
            	maps.put("startTimecorspMonth", dayinlastMonth);
            	maps.put("endTimecorspMonth", dayinnextMonth);
            	
            }
        }
        return maps;
    }
    
    /**
     * 请假时间转化为天数
     * 
     * @throws
     */
    public BigDecimal getTotalDay(float leaveHours) {
        BigDecimal totalday = new BigDecimal(0);
        try {
            BigDecimal remainder = new BigDecimal(leaveHours % 8);
            Double merchant = new Double(leaveHours / 8);
            BigDecimal t1 = new BigDecimal(Math.floor(merchant));
            BigDecimal t2 = new BigDecimal(0.5);
            int checkT2 = remainder.intValue();
            if (checkT2 == 3 || checkT2 == 5) {
                totalday = t1.add(t2);
            } else {
                totalday = t1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalday;
    }
    
    /**
     * 计算行政人员的节假日
     */
    public static float calculateHoursOthersStaff(Date beginTime, Date endTime, List<SysHoliday> list) {
    	float h = beginTime.getHours() + (float)beginTime.getMinutes() / 60;
        if (h >= h4) {//18点后为第二天9点
        	beginTime.setTime(beginTime.getTime() + milsecPerDay);
        	beginTime.setHours(abh);
        	beginTime.setMinutes(abm);
        } else if (h >= h2 && h < h4) {// 若开始时间介于中午休息时间及下午上班时间内，都设置为下午上班时间,及半天
            	beginTime.setHours(pbh);
            	beginTime.setMinutes(pbm);
        } else if (beginTime.getHours() < h2) {// 若开始时间早于上午下班时间，将hour设置为上午上班时间
            	beginTime.setHours(abh);
            	beginTime.setMinutes(abm);
        }
    	h = endTime.getHours() + (float)endTime.getMinutes() / 60;
        if (h > h3) {//13点后为下午下班时间
        	endTime.setHours(peh);
        	endTime.setMinutes(pem);
        } else if (h > h1 && h <= h3) {// 若开始时间介于中午休息时间及上午上班时间内，都设置为上午下班时间,及半天
        	endTime.setHours(aeh);
        	endTime.setMinutes(aem);
        } else if (endTime.getHours() <= h1) {// 若开始时间早于上午上班时间结束时间设置为前一天下班时间
        	endTime.setTime(endTime.getTime() - milsecPerDay);
        	endTime.setHours(peh);
        	endTime.setMinutes(pem);
        }
        
        
        float totalHours = 0;
        // 存在节假日
        if (list != null && list.size() > 0) {
            Date t1 = beginTime;
            Date t2 = endTime;
            // 对开始和结束时间进行预处理
            t1 = pretreatmentBylistAndBegin(beginTime, list);
            t2 = pretreatmentBylistAndEnd(endTime, list);
            // 若开始时间晚于结束时间，返回0
            if (t1.getTime() > t2.getTime()) {
                return 0;
            }
            int dayCounts = 0;// 记录开始时间和结束时间之间工作日天数
            // 调整开始时间，使得开始时间和结束时间在同一天，或者相邻的工作日内。
            while (t1.getTime() <= t2.getTime()) {
                Date temp = new Date(t1.getTime() + milsecPerDay);
                temp = pretreatmentBylistAndBegin(temp, list);
                temp.setHours(t1.getHours());
                temp.setMinutes(t1.getMinutes());
                if (temp.getTime() > t2.getTime()) {
                    break;
                } else {
                    t1 = temp;
                    dayCounts++;
                }
            }
            totalHours += dayCounts * hoursPerDay;
            float hh1 = t1.getHours() + (float)t1.getMinutes() / 60;
            float hh2 = t2.getHours() + (float)t2.getMinutes() / 60;
            // 处理开始结束是同一天的情况
            if (t1.getDay() == t2.getDay()) {
                float tt = 0;
                tt = hh2 - hh1;
                if (hh1 >= h1 && hh1 <= h2 && hh2 >= h3) {
                    tt = tt - (h3 - h2);
                }
                totalHours += tt;
            } else {
                // 处理开始结束不是同一天的情况
                float tt1 = h4 - hh1;
                float tt2 = hh2 - h1;
                if (hh1 <= h2) {
                    tt1 = tt1 - (h3 - h2);
                }
                if (hh2 >= h3) {
                    tt2 = tt2 - (h3 - h2);
                }
                totalHours += (tt1 + tt2);
            }

        } else {
            // 没节假日录入，就只排除双休日
            totalHours = calculateHoursByOthsers(beginTime, endTime);
        }
        return totalHours;
    }
    
    /**
     * 结束时间进行预处理
     * @throws
     */
    private static Date pretreatmentBylistAndEnd(Date t, List<SysHoliday> list) {
    	int key = 0;
        for (int i = 0; i < list.size(); i++) {
            String offToWork = list.get(i).getOffLaterDate();
            // 结束时间不在节假日里面但是在换休假里面
			if (!StringUtil.isEmpty(offToWork)) {
                String[] split = offToWork.split(";");
				for (String string : split) {//这里原来的调班时间为时间段判断，现在为独立的多个天，需要对每个天进行独立判断
					Date t1 = stringToDate(string + " 09:00");
					Date t2 = stringToDate(string + " 18:00");
					// 不在节假日内但是在换休假里面
					if ((t.compareTo(t1) == 0 || t.compareTo(t1) == 1)
							&& (t.compareTo(t2) == 0 || t.compareTo(t2) == -1)) {
						key = 1;
						break;
					}
				}
            }
            // 结束时间在节假日里面
			String startHoliday = DateUtil.parseDateFormat(list.get(i).getHolidayStartDate(), "yyyy-MM-dd");
			String endHoliday = DateUtil.parseDateFormat(list.get(i).getHolidayEndDate(), "yyyy-MM-dd");
            if (startHoliday != null && endHoliday != null) {
                Date t3 = stringToDate(startHoliday + " 09:00");
                Date t4 = stringToDate(endHoliday + " 18:00");
                // 开始时间在节假日内
				if ((t.compareTo(t3) == 1 || t.compareTo(t3) == 0) && (t.compareTo(t4) == -1 || t.compareTo(t4) == 0)) {
					key = 2;
					break;
				}
            }
        }
        // 不属于特别处理的数据直接跳过周六周末即可
        if (key == 0) {
            // 周六向前移动一天的下班时间
            if (t.getDay() == 6) {
                t.setTime(t.getTime() - milsecPerDay * 1);
                t.setHours(peh);
                t.setMinutes(pem);
            }
            if (t.getDay() == 0) {
                t.setTime(t.getTime() - milsecPerDay * 2);
                t.setHours(peh);
                t.setMinutes(pem);
            }
        }
        // 备注上班时间，就算是周六周末也不进行跳天处理
//        if (key == 1) {
            // System.out.println("特别备注时间不做任何处理！");
//        }
        // 节假日时间，请假结束时间向前移
        if (key == 2) {
            for (int i = 0; i < list.size(); i++) {
				String startHoliday = DateUtil.parseDateFormat(list.get(i).getHolidayStartDate(), "yyyy-MM-dd");
				String endHoliday = DateUtil.parseDateFormat(list.get(i).getHolidayEndDate(), "yyyy-MM-dd");
                Date t3 = stringToDate(startHoliday + " 09:00");
                Date t4 = stringToDate(endHoliday + " 18:00");
                if ((t.compareTo(t3) == 0 || t.compareTo(t3) == 1) && (t.compareTo(t4) == -1 || t.compareTo(t4) == 0)) {
                    // 那么把结束时间向前移到节假日的前一天的下班时间
                    int day=DateUtil.getDiffDate(t, t3);
                    t.setTime(t.getTime() - (milsecPerDay * (day + 1)));
                    t.setHours(peh);
                    t.setMinutes(pem);
                    // 如果节假日开始放的前一天为周六
					if (t.getDay() == 6 || t.getDay() == 0) {
                        String offToWork = list.get(i).getOffLaterDate();
        				if (!StringUtil.isEmpty(offToWork)) {
                            String[] split = offToWork.split(";");
							for (String string : split) {// 循环遍历，确认不在调班日
								if (t.getDay() != 6 && t.getDay() != 0) {//不在周六和周日，则不在循环
									break;
								}
								Date t5 = stringToDate(string + " 09:00");
								Date t6 = stringToDate(string + " 18:00");
								// 如果这天在调斑里面记录了那么就不跳开周六这天
								if ((t.compareTo(t5) == 0 || t.compareTo(t5) == 1)
										&& (t.compareTo(t6) == -1 || t.compareTo(t6) == 0)) {
									// 不做处理 占位
									break;
								} else {//跳一天，如果周六调休，则可能周天
	                                t.setTime(t.getTime() - milsecPerDay * 1);
									t.setHours(abh);
									t.setMinutes(abm);
								}
							}
							
							if (t.getDay() == 6 || t.getDay() == 0) {//再次处理,如果计算完后,还在周末的话
								t.setTime(t.getTime() - milsecPerDay * (t.getDay() == 0 ? 2 : 1));//周六跳一天，周日跳两天
								t.setHours(abh);
								t.setMinutes(abm);
							}
                        } else {
							t.setTime(t.getTime() - milsecPerDay * (t.getDay() == 0 ? 2 : 1));//周六跳一天，周日跳两天
                            t.setHours(peh);
                            t.setMinutes(pem);
                        }
                    }
                }
            }
        }
        return t;
    }
    
    /**
     * 开始时间处理
     */
    private static Date pretreatmentBylistAndBegin(Date t, List<SysHoliday> list) {
		// 判断请休假的开始时间存不存在节假日里面
		int key = 0;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String offToWork = list.get(i).getOffLaterDate();
				if (!StringUtil.isEmpty(offToWork)) {
					String[] split = offToWork.split(";");
					for (String string : split) {//这里原来的调班时间为时间段判断，现在为独立的多个天，需要对每个天进行独立判断
						Date t1 = stringToDate(string + " 09:00");
						Date t2 = stringToDate(string + " 18:00");
						// 不在节假日内但是在换休假里面
						if ((t.compareTo(t1) == 0 || t.compareTo(t1) == 1)
								&& (t.compareTo(t2) == 0 || t.compareTo(t2) == -1)) {
							key = 1;
							break;
						}
					}
				}
				String startHoliday = DateUtil.parseDateFormat(list.get(i).getHolidayStartDate(), "yyyy-MM-dd");
				String endHoliday = DateUtil.parseDateFormat(list.get(i).getHolidayEndDate(), "yyyy-MM-dd");
				if (startHoliday != null && endHoliday != null) {
					Date t3 = stringToDate(startHoliday + " 09:00");
					Date t4 = stringToDate(endHoliday + " 18:00");
					// 开始时间在节假日内
					if ((t.compareTo(t3) == 1 || t.compareTo(t3) == 0)
							&& (t.compareTo(t4) == -1 || t.compareTo(t4) == 0)) {
						key = 2;
						break;
					}
				}
			}//end for 
		}
		
		if (key == 2) {//开始时间在节假日中，跳完节假日后，需要判断新的开始时间为周末时，周末为调班的情况（调班只对周末进行）
			for (int i = 0; i < list.size(); i++) {
				String startHoliday = DateUtil.parseDateFormat(list.get(i).getHolidayStartDate(), "yyyy-MM-dd");
				String endHoliday = DateUtil.parseDateFormat(list.get(i).getHolidayEndDate(), "yyyy-MM-dd");
				Date t3 = stringToDate(startHoliday + " 09:00");
				Date t4 = stringToDate(endHoliday + " 18:00");
				/**
				 * 如果请假开始时间在某个节假日的之间
				 */
				if ((t.compareTo(t3) == 1 || t.compareTo(t3) == 0) && (t.compareTo(t4) == -1 || t.compareTo(t4) == 0)) {
					int day = DateUtil.getDiffDate(t4, t);
					t.setTime(t.getTime() + milsecPerDay * (day + 1));
					t.setHours(abh);
					t.setMinutes(abm);
					// 跳开节假日的t 这天，是否为周六周末，不是就不管是就要判断需不需要跳
					if (t.getDay() == 6 || t.getDay() == 0) {
						String offToWork = list.get(i).getOffLaterDate();
						// 如果换休假不为空就要判断 为空就直接处理可能存在的周六周末
						if (!StringUtil.isEmpty(offToWork)) {
							String[] split = offToWork.split(";");
							for (String string : split) {// 循环遍历，确认不在调班日
								if (t.getDay() != 6 && t.getDay() != 0) {//不在周六和周日，则不在循环
									break;
								}
								Date t5 = stringToDate(string + " 09:00");
								Date t6 = stringToDate(string + " 18:00");
								// 如果这天在调斑里面记录了那么就不跳开周六这天
								if ((t.compareTo(t5) == 0 || t.compareTo(t5) == 1)
										&& (t.compareTo(t6) == -1 || t.compareTo(t6) == 0)) {
									// 不做处理 占位
									break;
								} else {//跳一天，如果周六调休，则可能周天
									t.setTime(t.getTime() + milsecPerDay * 1);
									t.setHours(abh);
									t.setMinutes(abm);
								}
							}
							
							if (t.getDay() == 6 || t.getDay() == 0) {//再次处理,如果计算完后,还在周末的话
								t.setTime(t.getTime() + milsecPerDay * (t.getDay() == 6 ? 2 : 1));//周六跳两天，周日跳一天
								t.setHours(abh);
								t.setMinutes(abm);
							}

							
						} else {//未配置调班时间
							t.setTime(t.getTime() + milsecPerDay * (t.getDay() == 6 ? 2 : 1));//周六跳两天，周日跳一天
							t.setHours(abh);
							t.setMinutes(abm);
						}
					}
				}
			}//end for
		}
		// 不在节假日里面但是在调斑里面，那么
		if (key == 1) {
			// System.out.println("规定上班日不做任何跳天处理");
		}
		// 普通的处理
		if (key == 0) {
			if (t.getDay() == 6) {
				t.setTime(t.getTime() + milsecPerDay * 2);
				t.setHours(abh);
				t.setMinutes(abm);
			}
			if (t.getDay() == 0) {
				t.setTime(t.getTime() + milsecPerDay * 1);
				t.setHours(abh);
				t.setMinutes(abm);
			}
		}
		return t;
	}
}
