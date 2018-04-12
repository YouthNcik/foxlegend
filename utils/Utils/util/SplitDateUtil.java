package com.zhph.util;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhph.model.cf.KeyValueForDate;
import com.zhph.model.common.RankType;
import com.zhph.model.sys.SysHoliday;

/**
 * 两个时间之间的拆分
 * @author Administrator
 *
 */
public class SplitDateUtil {
	 	private static String MS="09:00";
	    private static String YM="18:00";
	    private static String ZW="12:00";
	   @SuppressWarnings("deprecation")
	public static List<KeyValueForDate> getKeyValueForDate(String startDate,String endDate){
		List<KeyValueForDate> list=null;
		try {
			list=new ArrayList<KeyValueForDate>();
			
			String firstDay="";
			String lastDay="";
			
			Date d1=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDate);
			Date d2=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endDate);
			d1 = CalculateHoursUtil.processBeginTime(d1);
			d2 = CalculateHoursUtil.processEndTime(d2);
	
			//定义日期实列
			Calendar dd=Calendar.getInstance();
			//设置日期的起始时间
			dd.setTime(d1);
			Calendar cale=Calendar.getInstance();
			Calendar c=Calendar.getInstance();
			c.setTime(d2);
			
			
			int satrtDay=d1.getDate();
			int endDay=d2.getDate();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			KeyValueForDate keValueForDate=null;
			while (dd.getTime().before(d2)) {
			 keValueForDate=new KeyValueForDate();
			 cale.setTime(dd.getTime());
			 
			if (dd.getTime().equals(d1)) {
				System.out.println(dd.getActualMaximum(Calendar.DAY_OF_MONTH));
				cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
				lastDay=sdf.format(cale.getTime());
				keValueForDate.setStartDate(sdf.format(d1));
				keValueForDate.setEndDate(lastDay);
				}else if(dd.get(Calendar.MONTH)==d2.getMonth()&&dd.get(Calendar.YEAR)==c.get(Calendar.YEAR)){
					//取第一天
					cale.set(Calendar.DAY_OF_MONTH, 1);
					firstDay=sdf.format(cale.getTime());
					
					keValueForDate.setStartDate(firstDay);
					keValueForDate.setEndDate(sdf.format(d2));
				}else{
					//取第一天
					cale.set(Calendar.DAY_OF_MONTH, 1);
					firstDay=sdf.format(cale.getTime());
					
					cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
					lastDay=sdf.format(cale.getTime());
					
					keValueForDate.setStartDate(firstDay);
					keValueForDate.setEndDate(lastDay);
				}
				list.add(keValueForDate);
				dd.add(Calendar.MONTH,1);
			}
			
			if(endDay<satrtDay){
				keValueForDate=new KeyValueForDate();
				
				cale.setTime(d2);
				cale.set(Calendar.DAY_OF_MONTH, 1);
				firstDay=sdf.format(cale.getTime());
				
				keValueForDate.setStartDate(firstDay);
				keValueForDate.setEndDate(sdf.format(d2));
				list.add(keValueForDate);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	/**
	 * 设置分段的时间并统计时间段的天数
	 * @return 
	 */
	public static Map<String, String> segmenteDays(String startTime, String endTime, String type,
			List<SysHoliday> list) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			List<KeyValueForDate> datelist = getKeyValueForDate(startTime, endTime);
			int maxsize = datelist.size();
			int minsize = 0;
			List<KeyValueForDate> newlsit = new ArrayList<KeyValueForDate>();
			for (int i = 0; i < datelist.size(); i++) {
				KeyValueForDate keyvalue = new KeyValueForDate();
				String startDate = datelist.get(i).getStartDate();
				String endDate = datelist.get(i).getEndDate();
				// 截取时间做分类处理
				String substringByYM = endDate.substring(11, 16);

				if (i == minsize) {
					// 如果第一条的开始时间为18：00就不能设置开始时间为09：00点
					keyvalue.setStartDate(startDate);
					keyvalue.setEndDate(endDate.substring(0, 11) + YM);
					newlsit.add(keyvalue);
				} else if (maxsize == i) {
					// 如果结束的时间为18：00那么开始时间不能设置为18:00点
					keyvalue.setStartDate(startDate.substring(0, 11) + MS);
					keyvalue.setEndDate(endDate);
					newlsit.add(keyvalue);
				} else {
					if (substringByYM.equals(ZW)) {
						keyvalue.setStartDate(startDate.substring(0, 11) + MS);
						keyvalue.setEndDate(endDate.substring(0, 11) + ZW);
						newlsit.add(keyvalue);
					} else {
						keyvalue.setStartDate(startDate.substring(0, 11) + MS);
						keyvalue.setEndDate(endDate.substring(0, 11) + YM);
						newlsit.add(keyvalue);
					}
				}
			}

			// 对设置好的时间进行天数的统计
			for (KeyValueForDate keyValueForDate : newlsit) {
				Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(keyValueForDate.getStartDate());
				Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(keyValueForDate.getEndDate());
				BigDecimal totalday = new BigDecimal(0);
				String key = keyValueForDate.getStartDate().substring(0, 7);
				// 分割时间段后就按照不跨月的总天数进行
				// 若员工的职级类别为客户代表 5、团队经理 4，则请假天数为请假时间之间的差
				if (RankType.TEAM_MANAGER.getNum().equals(type) || RankType.CUSTOMER_STAND.getNum().equals(type)) {
					float leaveHours = CalculateHoursUtil.calculateHours(startDate, endDate);
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

					map.put(key, totalday.toString());
				}
				// 若员工的职级类别为行政专员 6、营业部经理 3、城市经理 2
				else if (RankType.CITY_MANAGER.getNum().equals(type) || RankType.SALES_MANAGER.getNum().equals(type)
						|| RankType.ADMIN_STAFF.getNum().equals(type)) {//// 236
																		//// others
					float leaveHours = CalculateHoursUtil.calculateHoursOthersStaff(startDate, endDate, list);
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
					map.put(key, totalday.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
