package com.zhlt.g1app.basefunc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;
import android.os.HandlerThread;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
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
	private static ExecutorService fixedThreadPool;;

	public static ExecutorService getFixedThreadPool() {
		if (fixedThreadPool == null) {
			fixedThreadPool = Executors.newFixedThreadPool(8);
			// Log4jUtil.getLogger().info("初始化线程池");
		}

		return fixedThreadPool;
	}

	public void closeFixedThreadPool() {
		if (fixedThreadPool != null) {
			fixedThreadPool.shutdown();
			// Log4jUtil.getLogger().info("关闭线程池");
		}
	}

	private static void initHandlerThread() {
		HandlerThread mHandlerThread = new HandlerThread(mHandlerName);
		mHandlerThread.start();
		fixedThreadPool = Executors.newFixedThreadPool(5);

	}

}
