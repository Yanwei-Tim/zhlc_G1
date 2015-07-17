package com.zhlt.g1.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;


import android.app.Application;
import android.os.HandlerThread;
import android.util.Log;

public class MyApplication extends Application {
	public LocationClient mLocationClient;
	public BDLocation mlocation;
	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(this.getApplicationContext());
		
		initHandlerThread();

	}

	private static MyApplication instance;

	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	private static String mHandlerName = "MY_HANDLER";
	private static TimeServerHandlerExecutePool fixedThreadPool;;

	public static TimeServerHandlerExecutePool getFixedThreadPool() {
		if (fixedThreadPool == null) {
			fixedThreadPool = new TimeServerHandlerExecutePool(50, 10000);
			//Log4jUtil.getLogger().info("初始化线程池");
		}

		return fixedThreadPool;
	}

	public void closeFixedThreadPool() {
		if (fixedThreadPool != null) {
			fixedThreadPool.close();
			//Log4jUtil.getLogger().info("关闭线程池");
		}
	}

	private static void initHandlerThread() {
		HandlerThread mHandlerThread = new HandlerThread(mHandlerName);
		mHandlerThread.start();
		

	}
	
}
