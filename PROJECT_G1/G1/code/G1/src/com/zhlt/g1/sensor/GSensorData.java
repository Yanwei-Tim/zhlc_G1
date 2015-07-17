package com.zhlt.g1.sensor;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Handler;

import com.zhlt.g1.db.DBOpenHelper;
import com.zhlt.g1.gps.GPSBAIDUData;
import com.zhlt.g1.sensor.interfaces.SensorInterfaces;
import com.zhlt.g1.util.InitUtil;
import com.zhlt.g1.util.Log4jUtil;
import com.zhlt.g1.util.MyApplication;
import com.zhlt.g1.util.SocketUtil;

import java.net.Socket;

import io.netty.channel.Channel;

public class GSensorData implements SensorInterfaces {
	private SensorManager sensorMgr;
	Sensor sensor = null;
	private float x, y, z;
	Logger log = Log4jUtil.getLogger("com.zhlt.g1.sensor.GSensorData");
	SensorEventListener lsn = null;
     private int level,scale;
    private static GSensorData instance;
    private DBOpenHelper dbh;
    private Context mContext;
    private  String imei;
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}



    public  GSensorData(){

    }
    private GSensorData(Context c) {
        this.mContext = c;
        dbh = DBOpenHelper.getInstance(c);
        imei=  GPSBAIDUData.getInstance(mContext).getImei();
         try {
            init(c,dbh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static GSensorData getInstance(Context c) {
        if (instance == null) {
            instance = new GSensorData(c);
        }
        return instance;
    }



    @Override
	public void init(Context ct, DBOpenHelper dbh) throws Exception {
		// TODO Auto-generated method stub
		mContext = ct;
		sensorMgr = (SensorManager) ct.getSystemService(ct.SENSOR_SERVICE);
		sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (InitUtil.DEBUG)
			log.info("初始化G-sensor");
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(mBroadcastReceiver, filter);
	}

	@Override
	public void start(Handler mhandler) throws Exception {
		// TODO Auto-generated method stub
		// 注册listener，第三个参数是检测的精确度

		// TODO Auto-generated method stub
		lsn = new SensorEventListener() {
			public void onSensorChanged(SensorEvent e) {

				x = e.values[SensorManager.DATA_X];
				y = e.values[SensorManager.DATA_Y];
				z = e.values[SensorManager.DATA_Z];

			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};
		sensorMgr
				.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);

	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		sensorMgr.unregisterListener(lsn);
        mContext.unregisterReceiver(mBroadcastReceiver);
	}

    @Override
    public void log(boolean debug) throws Exception {

    }

    /**
     * sensor接口
     *
     * @param code
     */
    public void startsensor(final int code, final Channel channel) {
        // TODO Auto-generated method stub
        try {
            if (getX() == 0 && getY() == 0 && getZ() == 0) {
                start(null);
            }
            MyApplication.getInstance().getFixedThreadPool()
                    .execute(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {

                                while (true) {
                                    if (getLevel() != 0) {
                                        JSONObject obj = new JSONObject();
                                        obj.put("code", code);
                                        obj.put("state", 1);
                                        obj.put("imei", imei);
                                        // 温度
                                        JSONObject objdatga = new JSONObject();
                                        objdatga.put("temperature", 30);
                                        objdatga.put("humidity", 20);
                                        // objdatga.put("sensorY",
                                        // getY());
                                        // objdatga.put("sensorZ",
                                        // getZ());
                                        // 电池电量
                                        JSONObject objdatga2 = new JSONObject();
                                        objdatga2.put("level", getLevel());
                                        objdatga2.put("scale", getScale());
                                        
                                        //紫外线
                                        JSONObject  obj3=new JSONObject();
                                        obj3.put("ultravioletRays", 1);
                                        //信号 
                                        JSONObject  obj4=new JSONObject();
                                        obj4.put("signal", 3);
                                        
                                        JSONArray jsons = new JSONArray();
                                        jsons.put(objdatga);
                                        jsons.put(objdatga2);
                                        jsons.put(obj3);
                                        jsons.put(obj4);

                                        obj.put("data", jsons.toString());
                                        channel.writeAndFlush(obj.toString()).sync();
                                        if (InitUtil.DEBUG)
                                            log.info("写入Socket"
                                                    + obj.toString());
                                        break;
                                    }
                                    Thread.sleep(500);
                                    if (InitUtil.DEBUG)
                                        log.info("等待500");
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                                if (InitUtil.DEBUG)
                                    log.error("返回数据错误"
                                            + e.getLocalizedMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				// int status = intent.getIntExtra("status", 0);
				// int health = intent.getIntExtra("health", 0);
				// boolean present = intent.getBooleanExtra("present", false);
				 level = intent.getIntExtra("level", 0);
				 scale = intent.getIntExtra("scale", 0);
                System.out.println("DAIN LIANG level    ="+level);
				// int icon_small = intent.getIntExtra("icon-small", 0);
				// int plugged = intent.getIntExtra("plugged", 0);
				// int voltage = intent.getIntExtra("voltage", 0);
				// int temperature = intent.getIntExtra("temperature", 0);
				// String technology = intent.getStringExtra("technology");

			}
		}
	};

}
