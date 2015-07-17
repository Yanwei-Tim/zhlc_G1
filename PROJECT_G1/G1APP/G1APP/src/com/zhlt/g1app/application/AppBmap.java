package com.zhlt.g1app.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Application;
import android.os.HandlerThread;

import com.baidu.mapapi.SDKInitializer;

public class AppBmap extends Application {

	/**  所有Activity集合  */
	private List<Activity>mActList;
	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		initHandlerThread();
	}
	
	/**
	 * 添加Activity到集合里
	 */
	public  void addActivity(Activity activity){
		if (mActList == null) {
			mActList = new ArrayList<Activity>();
		}
		mActList.add(activity);
	}
	
	/**
	 * 退出APP，finish
	 */
	public  void exit(){
		if (mActList == null) {
			return;
		}
		for (int i = 0; i < mActList.size(); i++) {
			mActList.get(i).finish();
		}
	}

	private static AppBmap instance;

	public static AppBmap getInstance() {
		if (null == instance) {
			instance = new AppBmap();
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