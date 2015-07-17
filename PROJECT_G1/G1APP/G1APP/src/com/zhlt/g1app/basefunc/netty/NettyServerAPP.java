package com.zhlt.g1app.basefunc.netty;

import org.apache.log4j.Logger;

import com.zhlt.g1app.basefunc.Log4jUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
 * --------- yuanpeng@gobabymobile.cn 2015-7-6 下午5:33:22 1.0 Create this moudle
 */
public class NettyServerAPP extends Service  {

	private APPManagerThread mPCManager;
	private Logger log4jUtil = Log4jUtil.getLogger("NettyServerAPP");
	@Override
	public void onCreate() {
		log4jUtil.info("onCreat..................");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {

		mPCManager = APPManagerThread.getInstance(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		log4jUtil.info("server onDestroy");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		flags=START_STICKY;
		log4jUtil.info("server onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

}
