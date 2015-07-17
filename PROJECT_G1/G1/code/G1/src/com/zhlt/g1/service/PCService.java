package com.zhlt.g1.service;

import org.apache.log4j.Logger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zhlt.g1.util.Log4jUtil;
import com.zhlt.g1.util.thread.PCManagerThread;

/**
 * Created by user on 6/30/15.
 */
public class PCService extends Service {
	Logger log = Log4jUtil.getLogger("PCService");
    private PCManagerThread mPCManager;

    @Override
    public void onCreate() {
    	log.info("===========..........PCService  onCreate..............................................................");
         super.onCreate();
    }
    @Override
    public void onStart(Intent intent, int startId) {
    	log.info("===========..........PCService  onStart..............................................................");
           mPCManager = PCManagerThread.getInstance(this);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		flags=START_STICKY;
		log.info("===========..........PCService  onStartCommand..............................................................");
        return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		
		log.info("===========..........PCService  onDestroy..............................................................");
		super.onDestroy();
	}
	

}
