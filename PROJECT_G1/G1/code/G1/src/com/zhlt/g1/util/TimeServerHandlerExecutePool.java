package com.zhlt.g1.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
 * --------- yuanpeng@gobabymobile.cn 2015-7-8 上午10:09:46 1.0 Create this moudle
 */
public class TimeServerHandlerExecutePool {
	private ExecutorService executor;

	public TimeServerHandlerExecutePool(int maxPoolSize, int queuSize) {
		executor = new ThreadPoolExecutor(Runtime.getRuntime()
				.availableProcessors(), maxPoolSize, 120l, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(queuSize));
	}

	public void execute(Runnable task) {
		executor.execute(task);
	}

	public void close() {
		executor.shutdown();
		executor = null;
	}
}