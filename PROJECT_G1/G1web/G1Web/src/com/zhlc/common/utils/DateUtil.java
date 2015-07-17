package com.zhlc.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {

	/**
	 * 日期格式化字符串->	yyyyMMddHHmmss
	 */
	public static final String TIME_FORMAT_HOUR = "yyyyMMddHHmmss";
	
	/**
	 * 日期格式化字符串 到秒	yyyy-MM-dd HH:mm:ss
	 */
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 日期格式化字符串 到天	yyyy-MM-dd
	 */
	public static final String TIME_FORMAT_DAY = "yyyy-MM-dd";
	
	/**
	 * 日期格式化字符串 到毫秒	yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final String TIME_FORMAT_MSEL = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/**
	 * oracle中日期格式化字符串	yyyy-mm-dd hh24:mi:ss
	 */
	public static final String TIME_FORMAT_ORACLE = "yyyy-mm-dd hh24:mi:ss";
	/**
	 * oracle中日期格式化字符串	HH:mm
	 */
	public static final String TIME_FORMAT_HHMM = "HH:mm";
	/**
	 * oracle中日期格式化字符串	yyyy-MM-dd HH
	 */
	public static final String TIME_FORMAT_HOUR_ONLY = "yyyy-MM-dd HH";
	/**
	 * oracle中日期格式化字符串	yyyy-MM-dd HH
	 */
	public static final String TIME_FORMAT_MIN_ONLY = "yyyy-MM-dd HH:mm";
	
	/**
	 * 返回系统时间
	 *  
	 * @return long
	 * @author:何志鸣 
	 * @date:2012-12-6
	 */
	public static long getcurrentTime(){
		return System.currentTimeMillis();
	}
	/**
	 * 获取服务器当前日期
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}
	
	/**
	 * 获取服务器当前时间的字符串 ,格式 ：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentDateStr(){
		
		return convertDateToStr(getCurrentDate(), TIME_FORMAT);
	}
	/**
	 * 获取当前时间到指定hour时间的字符串
	 * @param hour		指定hour,-1指前一小时，+1指后一小时
	 * @return String
	 * @author:何志鸣 
	 * @date:2013-3-7
	 */
	public static String getCurrentToHour(int hour,String format){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, hour); // 得到提前hour小时
		Date oneHourBefore = calendar.getTime();
		return convertDateToStr(oneHourBefore,format);
	}
	/**
	 * 得到当天的23:59:59秒的指定格式的字符串值
	 *  
	 * @param format
	 * @return String
	 * @author:何志鸣 
	 * @date:2013-1-16
	 */
	public static String getCurrentLastesStr(String format){
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentDate());
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		convertDateToStr(getCurrentDate(), format);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return sdf.format(cal.getTime());
	}
	/**
	 * 获取指定日期的23:59:59秒的指定格式的date
	 *  
	 * @param date
	 * @param format
	 * @return String
	 * @author:何志鸣 
	 * @date:2013-1-16
	 */
	public static Date getCurrentLastesStr(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}
	/**
	 * 获取指定天数的2013-01-01 23:59:59秒的指定格式的date
	 */
	public static String getLastesDayStr(int date){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		// 获取昨天的时间
		return convertDateToStr(cal.getTime(),TIME_FORMAT);
	}
	/**
	 * 获取当天的零点的日期,00:00:01
	 *  
	 * @return Date
	 * @author:何志鸣 
	 * @date:2013-1-16
	 */
	public static Date getCurrentFirstStr(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 1);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	/**
	 * 获取服务器当前时间的字符串 ,格式：指定
	 *  
	 * @param format
	 * @return String
	 * @author:何志鸣 
	 * @date:2013-1-6
	 */
	public static String getCurrentDateStr(String format){
		
		return convertDateToStr(getCurrentDate(), format);
	}
	
	/**
	 * 增加天数
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDate(Date date, int days) {
		if (date == null) return date;
		Locale loc = Locale.getDefault();
		GregorianCalendar cal = new GregorianCalendar(loc);
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		cal.set(year, month, day + days);
		return cal.getTime();
	}
	/**
	 * 增加天/小时/分钟/秒
	 *  
	 * @param date			时间
	 * @param days			天
	 * @param hours			小时
	 * @param mins			分钟
	 * @param second		秒
	 * @return Date
	 * @author:何志鸣 
	 * @date:Dec 26, 2013
	 */
	public static Date addDate(Date date, int days,int hours,int mins,int second) {
		if (date == null) return date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		cal.add(Calendar.HOUR, hours);
		cal.add(Calendar.MINUTE, mins);
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}
	/**
	 * 增加天/小时/分钟/秒
	 *  
	 * @param date			时间
	 * @param days			天
	 * @param hours			小时
	 * @param mins			分钟
	 * @param second		秒
	 * @return Date
	 * @author:何志鸣 
	 * @date:Dec 26, 2013
	 */
	public static Date addDate(String date, int days,int hours,int mins,int second) {
		if(StringUtil.isBlank(date)){
			throw new IllegalArgumentException("DateUtil.addDate()方法非法参数值：" + date);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(convertStrToDate(date,TIME_FORMAT));
		cal.add(Calendar.DAY_OF_MONTH, days);
		cal.add(Calendar.HOUR, hours);
		cal.add(Calendar.MINUTE, mins);
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}
	/**
	 * 获取前几天或后几天的日期
	 *  
	 * @param days
	 * @return Date
	 * @author:何志鸣 
	 * @date:2013-1-16
	 */
	public static Date addDate(int days){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
	
	/**
	 * 获取前一天所处的周的星期一
	 *  
	 * @return Date
	 * @author:何志鸣 
	 * @date:2013-3-12
	 */
	public static Date getWeekStar(int week){
		Calendar cal = new GregorianCalendar();  
		cal.add(Calendar.DAY_OF_WEEK, week);
		cal.setFirstDayOfWeek(Calendar.MONDAY);  
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
		return cal.getTime();
	}
	/**
	 * 获取前一天所处的周的星期天
	 *  
	 * @param day
	 * @return Date
	 * @author:何志鸣 
	 * @date:2013-3-12
	 */
	public static Date getWeekEnd(int week){
		Calendar cal = new GregorianCalendar(); 
		cal.add(Calendar.DAY_OF_WEEK, week);
		cal.setFirstDayOfWeek(Calendar.MONDAY);  
		cal.set(Calendar.HOUR_OF_DAY, 23);  
		cal.set(Calendar.MINUTE, 59);  
		cal.set(Calendar.SECOND, 59);  
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);  
		return cal.getTime();
	}
	/**
	 * 获取前一天所处的月头
	 *  
	 * @param day
	 * @return Date
	 * @author:何志鸣 
	 * @date:2013-3-12
	 */
	public static Date getMonthStar(int month){
		Calendar cal = new GregorianCalendar(); 
		cal.add(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);  
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		return cal.getTime();
	}
	/**
	 * 获取前一天所处的月尾
	 *  
	 * @param day
	 * @return Date
	 * @author:何志鸣 
	 * @date:2013-3-12
	 */
	public static Date getMonthEnd(int month){
		Calendar cal = new GregorianCalendar(); 
		cal.add(Calendar.MONTH, 1 + month);
		cal.set(Calendar.DAY_OF_MONTH, 0); 
		cal.set(Calendar.HOUR_OF_DAY, 23);  
		cal.set(Calendar.MINUTE, 59);  
		cal.set(Calendar.SECOND, 59);  
		return cal.getTime();
	}
	
	/**
	 * 将字符串转换为日期格式 
	 * @param dateStr
	 * @param dateFormat
	 * @return
	 */
	public static Date convertStrToDate(String dateStr, String dateFormat) {
		if (dateStr == null || dateStr.equals("")) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(dateStr);
		}
		catch (Exception e) {
			throw new RuntimeException("DateUtil.convertStrToDate():" + e.getMessage());
		}
	}
	
	/**
	 * 将日期转换为字符串格式
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String convertDateToStr(Date date, String dateFormat) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}
	
	/**
	 * 给一日期增加一时间
	 * @param timePart HH,mm,ss 
	 * @param number 要增加的时间
	 * @param date 日期对象
	 * @return
	 */
	public static Date addTime(String timePart, int number, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (timePart.equals("HH")) {
			cal.add(Calendar.HOUR, number);
		}
		else if (timePart.equals("mm")) {
			cal.add(Calendar.MINUTE, number);
		}
		else if (timePart.equals("ss")) {
			cal.add(Calendar.SECOND, number);
		}
		else {
			throw new IllegalArgumentException("DateUtil.addDate()方法非法参数值：" + timePart);
		}
		return cal.getTime();
	}
	/**
	 * 得到上月时间最后一天
	 * 返回 yyyy-MM-dd HH:mm:ss 
	 */
	public static String getLastMonthTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(cal.get(Calendar.MONTH), -1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(cal.getTime());
	}
	
	/**
	 * 
	 * 清除指定的时间
	 * @param date
	 * @param timePart HH,mm,ss 
	 * @return
	 */
	public static Date clearTime(Date date,String timePart){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (timePart.equals("HH")) {
			cal.clear(Calendar.HOUR);
		}
		else if (timePart.equals("mm")) {
			cal.clear(Calendar.MINUTE);
		}
		else if (timePart.equals("ss")) {
			cal.clear(Calendar.SECOND);
		}
		else {
			throw new IllegalArgumentException("DateUtil.addDate()方法非法参数值：" + timePart);
		}
		return cal.getTime();
	}
	public static void main(String[] args){
		//上周一
//		System.out.println(DateUtil.convertDateToStr(getWeekStar(-3), TIME_FORMAT));
//		//上周日
//		System.out.println(DateUtil.convertDateToStr(getWeekEnd(-3), TIME_FORMAT));
//	      // 3.当前月的第一天  
//        System.out.println("当前月的第一天：" + convertDateToStr(getMonthStar(35),TIME_FORMAT));  
//        // 4.当前月的最后一天  
//        System.out.println("当前月的最后一天：" + convertDateToStr(getMonthEnd(35),TIME_FORMAT));  
//        System.out.println("获取前2天的时间：" + getLastesDayStr(-2));
//        System.out.println(24*60*60*1000);
        
        Date d = addTime("mm", -5, getCurrentDate());
        System.out.println(convertDateToStr(d, TIME_FORMAT_MIN_ONLY) + ":00");
        
      //当前时间  date
      		Date currDate = DateUtil.getCurrentDate();
      		//当前全日期
        		String currTime = DateUtil.convertDateToStr(currDate,DateUtil.TIME_FORMAT_MIN_ONLY) + ":00";
        		//当前时分秒
        		String c1 = DateUtil.convertDateToStr(currDate,DateUtil.TIME_FORMAT_HHMM) + ":00";
        		//当前后推5分钟时分秒
        		String c2 = DateUtil.convertDateToStr(DateUtil.addTime("mm", -5,currDate) , DateUtil.TIME_FORMAT_HHMM) + ":00";
        		//当前后推5分钟全日期
        		String currFiveLastTime = DateUtil.convertDateToStr(DateUtil.addTime("mm", -5,currDate) , DateUtil.TIME_FORMAT_MIN_ONLY) + ":00";
        		//前一天
        		String oneDayTime = DateUtil.convertDateToStr(DateUtil.addDate(currDate,-1,0,0,0), DateUtil.TIME_FORMAT_MIN_ONLY) + ":00";
        		//前10天
        		String tenDayTime = DateUtil.convertDateToStr(DateUtil.addDate(currDate,-10,0,-5,0), DateUtil.TIME_FORMAT_MIN_ONLY) + ":00";
      		System.out.println(currTime);
      		System.out.println(currFiveLastTime);
      		System.out.println(c1);
      		System.out.println(c2);
      		System.out.println(oneDayTime);
      		System.out.println(tenDayTime);
	}
}
