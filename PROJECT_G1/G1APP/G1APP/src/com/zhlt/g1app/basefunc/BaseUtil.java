package com.zhlt.g1app.basefunc;

import com.zhlt.g1app.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class BaseUtil {

	public static String getVersion(Context context)// 获取版本号
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return context.getString(R.string.unkown);
		}
	}

	/**
	 * 获取屏幕分辨率
	 * @param context
	 * @return 长+宽
	 */
	public static int getDisplayWidth(Activity context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics.widthPixels  ;
	}
	
	
	public static String getAppLanguage(Activity context) {
		return SharePreferUtil.getString(context, "app_language", "1");
	}
	
	
	

}
