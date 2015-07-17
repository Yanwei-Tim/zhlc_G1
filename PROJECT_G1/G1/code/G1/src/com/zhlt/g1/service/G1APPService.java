package com.zhlt.g1.service;

import org.apache.log4j.Logger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zhlt.g1.util.Log4jUtil;
import com.zhlt.g1.util.thread.G1APPManagerThread;
import com.zhlt.g1.util.thread.PCManagerThread;

/**
 * Created by user on 6/30/15.
 */
public class G1APPService extends Service {

    private G1APPManagerThread mPCManager;
    Logger log = Log4jUtil.getLogger("G1APPService");
    @Override
    public void onCreate() {
        System.out.println("onCreat..................e");
        super.onCreate();
    }
    @Override
    public void onStart(Intent intent, int startId) {

        mPCManager = G1APPManagerThread.getInstance(this);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		flags=START_STICKY;
		log.info("APP server onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
}
