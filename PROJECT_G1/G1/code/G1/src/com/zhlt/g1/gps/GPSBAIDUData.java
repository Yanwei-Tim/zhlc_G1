package com.zhlt.g1.gps;

import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.telephony.TelephonyManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zhlt.g1.db.DBOpenHelper;
import com.zhlt.g1.gps.bean.GPSVO;
import com.zhlt.g1.gps.db.GPSDBManager;
import com.zhlt.g1.gps.interfaces.GPSInterface;
import com.zhlt.g1.util.GenSequenceUtil;
import com.zhlt.g1.util.InitUtil;
import com.zhlt.g1.util.Log4jUtil;
import com.zhlt.g1.util.MyApplication;
import com.zhlt.g1.util.SocketUtil;
import com.zhlt.g1.util.TimeUtil;
import com.zhlt.g1.util.netty.TcpClient;
import com.zhlt.g1.util.netty.TcpClientHandler;

import io.netty.channel.Channel;

public class GPSBAIDUData implements GPSInterface, BDLocationListener {
	public LocationClient mLocationClient;
	Handler mhandler;
	private int TIME = 5000;
	Logger log = Log4jUtil.getLogger("com.zhlt.g1.gps.GPSBAIDUData");
	DBOpenHelper helper = null;
	GPSDBManager gpsm = null;
	private static GPSBAIDUData instance;
	private DBOpenHelper dbh;
	private String imei;
	private Context mContext;
	boolean Debug = false;
	private String debugInfo = "";
	public ArrayList<String> Debugarry = new ArrayList<String>();
	private boolean isUpdateListener = true;
	private BDLocation tempBDLocation = new BDLocation();
	public ArrayList<GPSVO> gpsList = new ArrayList<GPSVO>();
	 
	HttpUtils http = new HttpUtils();
	
	private GPSBAIDUData(Context c) {
		this.mContext = c;
		dbh = DBOpenHelper.getInstance(c);
		try {
			init(c, dbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static GPSBAIDUData getInstance(Context c) {
		if (instance == null) {
			instance = new GPSBAIDUData(c);
		}
		return instance;
	}

	@Override
	public void init(Context ct, DBOpenHelper dbh) throws Exception {
		// TODO Auto-generated method stub
		helper = dbh;
		gpsm = new GPSDBManager(dbh, true);
		mLocationClient = ((MyApplication)ct.getApplicationContext()).mLocationClient;
		mLocationClient.registerLocationListener(this); // 注册监听函数
		                
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setProdName("G1");// 产品线
		// 当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。
		option.setScanSpan(1000 * 2);// 设置发起定位请求的间隔时间为2000ms
		option.setIgnoreKillProcess(true);
		option.setOpenGps(true);
		

		// option.setIsNeedAddress(true);// 返回的定位结果包含地址信息

		/*
		 * BDLocation2GpsUtil sdsd = new BDLocation2GpsUtil(); sdsd.StartUp();
		 */

		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	public void updateListener() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			switch (mLocationClient.requestLocation()) {
			case 0:
				isUpdateListener = false;
				if (Debug)
					log.info("location  successs");

				break;
			case 1:
				isUpdateListener = true;
				if (Debug)
					log.info("服务没有启动");

				break;
			case 2:
				isUpdateListener = true;
				if (Debug)
					log.info("没有监听函数");
				break;
			case 6:
				isUpdateListener = true;
				if (Debug)
					log.info("请求间隔过短");
				break;
			default:
				break;
			}

		}
	}

	@Override
	public void start(Handler handler) throws Exception {
		// TODO Auto-generated method stub
		mhandler = handler;

	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		if (mLocationClient != null) {
			mLocationClient.unRegisterLocationListener(this);
			mLocationClient.stop();
			mLocationClient = null;
		}

	}

	@Override
	public void log(boolean debug) throws Exception {
		// TODO Auto-generated method stub
		Debug = debug;

	}

	/**
	 * 历史记录GPS记录
	 * 
	 * @param code
	 */
	public void startgpslog(final int code, final String start,
			final String end, final Channel channel) {
		// TODO Auto-generated method stub
		try {
			ArrayList<GPSVO> gs = selectlistGPS(start, end);
			if (gs != null && gs.size() > 0) {
				JSONObject obj = new JSONObject();
				obj.put("code", code);
				obj.put("state", 1);
				obj.put("imei", imei);
				obj.put("source", 2);
				JSONArray objs = new JSONArray();

				for (int i = 0; i < gs.size(); i++) {
					JSONObject objdatga = new JSONObject();
					objdatga.put("longitude", gs.get(i).getLongitude());
					objdatga.put("latitude", gs.get(i).getLatitude());
					objdatga.put("altitude", gs.get(i).getAltitude());
					objdatga.put("bearing", gs.get(i).getBearing());
					objdatga.put("speed", gs.get(i).getSpeed());
					objdatga.put("time", gs.get(i).getTime());
					objdatga.put("bearing", gs.get(i).getBearing());

					objs.put(objdatga.toString());
				}
				obj.put("data", objs.toString());
				if (Debug)
					log.info("return data:" + obj.toString());
				channel.writeAndFlush(obj.toString());

			} else {
				JSONObject obj = new JSONObject();
				obj.put("code", code);
				obj.put("state", 3);
				obj.put("imei", imei);
				obj.put("data", "no data(state:3)");
				channel.writeAndFlush(obj.toString());
				if (Debug) {
					log.error("GPS no data");
					Debugarry.add("GPS no data");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			if (Debug) {
				log.error("GPS Exception e Data erro" + e.getLocalizedMessage());
				Debugarry.add("GPS Exception e Data erro :"
						+ e.getLocalizedMessage());
			}
		}
	}

	public void selectlistUpGPS(final int code, final int state,
			final Channel channel) {

		MyApplication.getInstance().getFixedThreadPool()
				.execute(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							while (true) {

								ArrayList<GPSVO> gs = selectlistUpGPS(1);
								if (gs != null && gs.size() > 0) {
									JSONObject obj = new JSONObject();
									obj.put("code", code);
									obj.put("source", 2);
									JSONArray objs = new JSONArray();
                                     
									for (int i = 0; i < gs.size(); i++) {
										JSONObject objdatga = new JSONObject();
										objdatga.put("longitude", gs.get(i)
												.getLongitude());
										objdatga.put("latitude", gs.get(i)
												.getLatitude());
										objdatga.put("altitude", gs.get(i)
												.getAltitude());
										objdatga.put("bearing", gs.get(i)
												.getBearing());
										objdatga.put("speed", gs.get(i)
												.getSpeed());
										objdatga.put("time", gs.get(i)
												.getTime());
										objdatga.put("bearing", gs.get(i)
												.getBearing());
										objdatga.put("imei", getImei());
										objs.put(objdatga.toString());
										updatalistupgps(gs.get(i));
									}
									obj.put("data", objs.toString());
									if (Debug)
										log.info("return data:"
												+ obj.toString());

									channel.writeAndFlush(obj.toString())
											.sync();
									
									log.info("time  set up :" + 30);
									Thread.sleep(1000 * 30);

								} else {
									if (Debug) {
										log.debug("GPS no data()");
										Debugarry.add("GPS NO Data");
									}
								}
								if (Debug) {
									log.info("gps set-time upload data................");
								}
								Thread.sleep(1000 * 60);
							}

						} catch (Exception e) {
							// TODO: handle exception
							if (Debug) {
								log.error("GPS Exception e Data erro"
										+ e.getLocalizedMessage());
								Debugarry.add("GPS Exception e Data erro :"
										+ e.getLocalizedMessage());
							}

						}
					}
				});

	}

	public void SendgpdDebugInfo(final int code, final Channel channel) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("code", code);
			obj.put("state", 1);
			obj.put("imei", imei);
			obj.put("source", 2);
			obj.put("data", getDubugInfo());
			channel.writeAndFlush(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void sendgpsdata(final int code, final Channel channel) {
		// TODO Auto-generated method stub
		try {

			if (getGPSVO() != null) {
				JSONObject obj = new JSONObject();
				obj.put("code", code);
				obj.put("state", 1);
				obj.put("imei", imei);
				obj.put("source", 2);
				JSONObject objdatga = new JSONObject();
				objdatga.put("longitude", getGPSVO().getLongitude());
				objdatga.put("latitude", getGPSVO().getLatitude());
				objdatga.put("altitude", getGPSVO().getAltitude());
				objdatga.put("bearing", getGPSVO().getBearing());
				objdatga.put("speed", getGPSVO().getSpeed());
				objdatga.put("time", getGPSVO().getTime());
				objdatga.put("imei", getImei());
				// String
				// result=getAddress(getLocation());
				// objdatga.put("address",result );
				obj.put("data", objdatga.toString());
				channel.writeAndFlush(obj.toString());
				if (Debug)
					log.info("写入Socket" + obj.toString());

			} else {

				JSONObject obj = new JSONObject();
				obj.put("code", code);
				obj.put("state", 3);
				obj.put("imei", imei);

				obj.put("data", "GPS mgpsvo = null ");
				channel.writeAndFlush(obj.toString());
				if (Debug)
					log.info("写入Socket" + obj.toString());

			}
		} catch (Exception e) {
			// TODO: handle exception
			if (Debug) {
				log.error("GPS Exception e Data erro" + e.getLocalizedMessage());
				Debugarry.add("GPS Exception e Data erro :+"
						+ e.getLocalizedMessage());
			}
		}
	}

	public void sendfristdata(final int code, final Channel channel) {
		// TODO Auto-generated method stub
		try {
			log.info("   6001    sendfristdata");
			if (getGPSVO() != null) {
				JSONObject obj = new JSONObject();
				obj.put("code", code);
				obj.put("state", 1);
				obj.put("imei", imei);
				obj.put("source", 2);
				JSONObject objdatga = new JSONObject();
				objdatga.put("longitude", getGPSVO().getLongitude());
				objdatga.put("latitude", getGPSVO().getLatitude());
				objdatga.put("altitude", getGPSVO().getAltitude());
				objdatga.put("bearing", getGPSVO().getBearing());
				objdatga.put("speed", getGPSVO().getSpeed());
				objdatga.put("time", getGPSVO().getTime());
				objdatga.put("imei", getImei());
				// 温度
				JSONObject temobjdatga = new JSONObject();
				temobjdatga.put("temperature", 30);
				temobjdatga.put("humidity", 20); // 湿度
				// objdatga.put("sensorY",
				// getY());
				// objdatga.put("sensorZ",
				// getZ());
				// 电池电量
				JSONObject objdatga2 = new JSONObject();
				objdatga2.put("level", 30);
				objdatga2.put("scale", 100);

				// 紫外线
				JSONObject obj3 = new JSONObject();
				obj3.put("ultravioletRays", 1);
				// 信号
				JSONObject obj4 = new JSONObject();
				obj4.put("signal", 3);

				JSONArray jsons = new JSONArray();
				jsons.put(objdatga);
				jsons.put(objdatga2);
				jsons.put(obj3);
				jsons.put(obj4);
				// String
				// result=getAddress(getLocation());
				// objdatga.put("address",result );
				obj.put("data", jsons.toString());
				channel.writeAndFlush(obj.toString());
				if (Debug)
					log.info("写入Socket" + obj.toString());

			} else {

				JSONObject obj = new JSONObject();
				obj.put("code", code);
				obj.put("state", 3);
				obj.put("imei", imei);
				obj.put("source", 2);

				obj.put("data", "GPS mgpsvo = null ");
				channel.writeAndFlush(obj.toString());
				if (Debug) {
					log.info("GPS mgpsvo = null ");
					Debugarry.add("GPS mgpsvo = null ");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			if (Debug) {
				log.error("GPS Exception e Data erro" + e.getLocalizedMessage());
				Debugarry.add("GPS Exception e Data erro :+"
						+ e.getLocalizedMessage());
			}
		}
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null) {
			log.info("location   == null ");
			return;
		}
		/*
		 * if (isUpdateListener) { updateListener(); }
		 */

		if (Debug) {
			log.info("location.getLocType()==" + location.getLocType()
					+ "      location.getLatitude()==" + location.getLatitude()
					+ "location.getLongitude()==" + location.getLongitude()
					+ "location.getRadius()==" + location.getRadius());

		}
		if (location.getLocType() == BDLocation.TypeOffLineLocationNetworkFail
				|| location.getLocType() == BDLocation.TypeNetWorkException) {
			if (mLocationClient != null && mLocationClient.isStarted()) {
				mLocationClient.requestOfflineLocation();
				log.info("location  requestOfflineLocation ");
			}

		}else{
		if(location.getLocType()!=161&&location.getLocType()!=61){
			if (mLocationClient != null && mLocationClient.isStarted()) {
			 mLocationClient.requestLocation();
			 log.info("location  requestLocation ");
			}
		}else{
			if (tempBDLocation.getLatitude() == location.getLatitude()
					&& tempBDLocation.getLongitude() == location.getLongitude()) {
				if (Debug) {
					log.info("tempBDLocation   == location ");
				}

			} else {

				GPSVO gpsvo = new GPSVO();
				gpsvo.setId(GenSequenceUtil.getCombinationStr(4));
				gpsvo.setLatitude(String.valueOf(location.getLatitude()));
				gpsvo.setLongitude(String.valueOf(location.getLongitude()));
				gpsvo.setAltitude(String.valueOf(location.getAltitude()));
				gpsvo.setBearing(String.valueOf(location.getDirection()));
				gpsvo.setSpeed(String.valueOf(location.getSpeed()));
				gpsvo.setTime(TimeUtil.getTime(TimeUtil.TIME2));

				RequestParams params = new RequestParams();
				params.addBodyParameter("latitude",String.valueOf(location.getLatitude()));
				params.addBodyParameter("longitude",String.valueOf(location.getLongitude()));
				params.addBodyParameter("coord_type",String.valueOf(3));
				//params.addBodyParameter("geotable_id",String.valueOf(113132));
				params.addBodyParameter("ak","aWUvmLQqvCGjbMP9lWrPwg54");
				params.addBodyParameter("loc_time",String.valueOf(System.currentTimeMillis()/1000L));
				params.addBodyParameter("track_name",imei);
				params.addBodyParameter("service_id","100650");
				int  direction =(int) location.getDirection();
				params.addBodyParameter("direction",location.getDirection()>0?String.valueOf(direction):"0");
				params.addBodyParameter("speed",String.valueOf(location.getSpeed()));
				params.addBodyParameter("radius",String.valueOf(location.getRadius()));
				
				
				
				http.send(HttpRequest.HttpMethod.POST,
					    "http://api.map.baidu.com/trace/v1/track/create",params,
					    new RequestCallBack<String>(){

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								// TODO Auto-generated method stub
							
								if (Debug) {
									JSONObject obj=null;
									try {
										obj = new JSONObject(responseInfo.result);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							      log.info("tempBDLocation   != location responseInfo:"+obj.toString());
								}

							}

							@Override
							public void onFailure(
									HttpException error,
									String msg) {
								// TODO Auto-generated method stub
								if (Debug) {
									log.info("tempBDLocation   != location msg:"+msg);
								}

							}
					
				});
				
				
				
				if (Debug) {
					log.info("tempBDLocation   != location ");
				}
				tempBDLocation = location;
				setLocation(gpsvo);
			}
		}
		}
	

	}

	private synchronized void setLocation(GPSVO gpsvo) {
		mgpsvo = gpsvo;
		gpsvo.setId(GenSequenceUtil.getFullDateRandomNum(4));

		if (Debug) {
			log.info("onReceiveLocation..init db: " + gpsvo);
		}
		if (InitUtil.DEBUG)
			log.info("VVVVVVVVvv2: " + gpsvo);
		// gpsList.add(gpsvo);
		gpsm.insertGPS(gpsvo);
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// handler自带方法实现定时器
			try {
				handler.postDelayed(this, TIME);
				// System.out.println("do..............");
				if (!gpsList.isEmpty()) {
					gpsm.insertGPSArray(gpsList);
					gpsList.clear();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("exception...");
			}
		}
	};
	GPSVO mgpsvo;

	public GPSVO getGPSVO() {

		return mgpsvo;
	}

	public ArrayList<String> getDubugInfo() {

		return Debugarry;
	}

	public ArrayList<GPSVO> selectlistGPS(String start, String end) {
		return gpsm.selectlistGPS(start, end);
	}

	protected void updatalistupgps(GPSVO gs) {
		// TODO Auto-generated method stub
		gpsm.updatalistupgps(gs);
	}

	public ArrayList<GPSVO> selectlistUpGPS(int state) {
		return gpsm.selectlistUpGPS(state);
	}

	public void SetImei(String imei) {
		this.imei = imei;
	}

	public String getImei() {
		return imei;
	}

}
