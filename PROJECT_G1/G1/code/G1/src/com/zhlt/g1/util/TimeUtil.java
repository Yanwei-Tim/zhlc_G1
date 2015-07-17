/**
 * 
 */
package com.zhlt.g1.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @email 313044509@qq.com
 * @author kenneth
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {
	@SuppressWarnings("unused")
	public static final String TIME1 = "yyyy_MM_dd";
	public static final String TIME2 = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME3 = "yyyy-MM-dd ";

	public static final String getTime(String t) {
		Date d = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(t);
		String time = formatter.format(d);
		return time;

	}
	public static final  String getday(int day){
		String daystr="";
		SimpleDateFormat formatter = new SimpleDateFormat(TIME3);
		daystr = formatter.format(getNextDay(day));
		return daystr+"00:00:00";
	}
	public static Date getNextDay(int day) {  
        Calendar calendar = Calendar.getInstance();  
        Date  date= new Date();
        calendar.setTime(new Date());  
        calendar.add(Calendar.DAY_OF_MONTH, -day);  
        date = calendar.getTime();  
        return date;  
    } 
	public static void main(String[] args) {
		getday(1);
	}
}
