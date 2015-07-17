package com.zhlt.g1.BroadcastReceiver;

import org.apache.log4j.Logger;

import com.zhlt.g1.MainActivity;
import com.zhlt.g1.service.PCService;
import com.zhlt.g1.util.Log4jUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 ** Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd All rights reserved.
 * http://www.gobabymobile.cn/ File: - MainActivity.java Description:描述
 ** 
 ** 
 ** ------------------------------- Revision History:
 * ------------------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * --------- yuanpeng@gobabymobile.cn 2015-7-7 下午8:03:34 1.0 Create this moudle
 */
public class OpenBroadcastReceiver extends BroadcastReceiver {
	static Logger log = Log4jUtil.getLogger("OpenBroadcastReceiver");

	public void onReceive(Context context, Intent intent) {
		log.info("接受到广播");
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			/*
			 * Intent tIntent = new Intent(context, ServiceCar.class);
			 * log.info("广播 开机启动server"); context.startService(tIntent);
			 */
			log.info("广播 开机启动server");
			Intent newIntent = new Intent(context, MainActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// 注意，必须添加这个标记，否则启动会失败
			context.startActivity(newIntent);
		} else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
			log.info("广播 关机");
		}else if (intent.getAction().equals("stopServer")) {
			log.info("关闭server ");
			Intent tIntent = new Intent(context, PCService.class);
			context.startService(tIntent);
		}
	}
}
