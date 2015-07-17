/**
 * 
 */
package com.zhlt.g1.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @email  313044509@qq.com
 * @author kenneth
 *
 */
public class QueueDataUtil {


	private static ArrayBlockingQueue<byte[]> queues = new ArrayBlockingQueue<byte[]>(
			20);

	public synchronized static void setqueues(byte[] b) {
		if (b != null && b.length > 0) {
			try {
				queues.put(b);
			} catch (IllegalStateException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}

	public synchronized static byte[] getqueues() {

		try {
			return queues.poll(1,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public synchronized static void close() {
		queues.clear();

	}

	public synchronized static int size() {
		return queues.size();
	}

}
