/**
 * 
 */
package com.zhlt.g1app.basefunc;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @email 313044509@qq.com
 * @author kenneth
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	private static final String TIME1 = "yyyy_MM_dd";

	public static final String getTime() {
		Date d = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(TIME1);
		String time = formatter.format(d);
		return time;

	}

}
