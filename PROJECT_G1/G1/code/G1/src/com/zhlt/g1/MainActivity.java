package com.zhlt.g1;

import java.util.ArrayList;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.rtsp.RtspServer;
import net.majorkernelpanic.streaming.video.VideoQuality;
import org.apache.log4j.Logger;
import com.zhlt.g1.camera.CameraData;
import com.zhlt.g1.db.DBOpenHelper;
import com.zhlt.g1.gps.GPSBAIDUData;
import com.zhlt.g1.gps.GPSData;
import com.zhlt.g1.gps.bean.GPSVO;
import com.zhlt.g1.sensor.GSensorData;
import com.zhlt.g1.service.G1APPService;
import com.zhlt.g1.service.G1Service;
import com.zhlt.g1.service.PCService;
import com.zhlt.g1.util.InitUtil;
import com.zhlt.g1.util.Log4jUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;


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
 * --------- yuanpeng@gobabymobile.cn 2015-6-10 下午2:05:35 1.0 Create this moudle
 */
public class MainActivity extends Activity{
	DBOpenHelper helper = null;
	TextView tv = null;
	Logger log = Log4jUtil.getLogger("com.zhlt.g1.MainActivity");
	GSensorData gdata = null;
    GPSBAIDUData gpsdata = null;
	GPSData gpsdata2 = null;
	CameraData camerad = null;
	SurfaceView surface_camera;
	TelephonyManager tm = null;
	String imei = null;
	boolean  isread=true;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		setContentView(R.layout.activity_main);
		surface_camera = (SurfaceView) findViewById(R.id.surface_camera);
		tm = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
		init();
		// ---------------------------------------------------------------------------------s
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
				.edit();
		editor.putString(RtspServer.KEY_PORT, String.valueOf(1234));
		editor.commit();
		SessionBuilder.getInstance().setSurfaceView(surface_camera)
				.setPreviewOrientation(90).setContext(getApplicationContext())
				.setAudioEncoder(SessionBuilder.AUDIO_NONE)
				.setVideoQuality(new VideoQuality(320, 240, 20, 250000))// 设置分辨率
				.setVideoEncoder(SessionBuilder.VIDEO_H264);

		this.startService(new Intent(this, RtspServer.class));
        this.startService(new Intent(this, PCService.class));
        //this.startService(new Intent(this, G1APPService.class));
	}



	private void init() {
       gpsdata =GPSBAIDUData.getInstance(getApplicationContext());
       // gpsdata2=GPSData.getInstance(getApplicationContext());
        try {
          gpsdata.log(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        // wmu.open();
        try {
            if (imei == null) {
               imei = tm.getDeviceId();
               gpsdata.SetImei(InitUtil.NAME);
            	if (InitUtil.DEBUG)
        			log.info("imei:"+imei);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (InitUtil.DEBUG)
                log.info("初始化错误G-Sensor:" + e.getStackTrace());
        }

    }
	public void onclickbut(View v){
		switch (v.getId()) {
		case R.id.button1:
			finish();
			Intent mIntent = new Intent();
			mIntent.setAction("stopServer");
			// 发送广播
			sendBroadcast(mIntent);
			break;

		default:
			break;
		}
	}

}
