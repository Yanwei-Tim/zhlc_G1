/**
 * 
 */
package com.zhlt.g1.gps;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.zhlt.g1.db.DBOpenHelper;
import com.zhlt.g1.gps.bean.GPSVO;
import com.zhlt.g1.gps.db.GPSDBManager;
import com.zhlt.g1.gps.interfaces.GPSInterface;
import com.zhlt.g1.util.GenSequenceUtil;
import com.zhlt.g1.util.InitUtil;
import com.zhlt.g1.util.Log4jUtil;
import com.zhlt.g1.util.MyApplication;
import com.zhlt.g1.util.TimeUtil;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * 
 * @author kenneth
 * @version V1.0 Go baby mobile 深圳中和联创智能科技有限公司
 */
public class GPSData implements GPSInterface {
	android.location.LocationManager locationManager = null;
	Location currentLocation = null;
	String currentProvider = null;
	Logger log = Log4jUtil.getLogger("com.zhlt.g1.gps.GPSData");
	DBOpenHelper helper = null;
	GPSDBManager gpsm = null;
	Handler mhandler;
	Context may = null;
	boolean isdebug = false;
	static GPSData instance;

	public static GPSData getInstance(Context c) {
		if (instance == null) {
			instance = new GPSData();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zhlt.g1.interfaces.G1Interface#init()
	 */
	@Override
	public void init(Context ay, DBOpenHelper db) throws Exception {
		// TODO Auto-generated method stub
		helper = db;
		may = ay;
		gpsm = new GPSDBManager(helper, isdebug);
		// 获取到LocationManager对象
		locationManager = (android.location.LocationManager) ay
				.getSystemService(ay.LOCATION_SERVICE);
		// 根据设置的Criteria对象，获取最符合此标准的provider对象
		currentProvider = android.location.LocationManager.GPS_PROVIDER;
		Log.d("Location", "currentProvider: " + currentProvider);
		// 根据当前provider对象获取最后一次位置信息
		currentLocation = locationManager.getLastKnownLocation(currentProvider);
		// 如果位置信息为null，则请求更新位置信息
		if (currentLocation == null) {

			// TODO Auto-generated method stub
			locationManager.requestLocationUpdates(currentProvider, 0, 0,
					locationListener);
		}

		// 直到获得最后一次位置信息为止，如果未获得最后一次位置信息，则显示默认经纬度
		// 每隔10秒获取一次位置信息
		MyApplication.getInstance().getFixedThreadPool()
				.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(1000 * 15);
						} catch (InterruptedException e) {
							if (InitUtil.DEBUG)
								log.info(e.getMessage());
						}
						while (true) {
							currentLocation = locationManager
									.getLastKnownLocation(currentProvider);
							if (currentLocation != null) {

								setLocation(currentLocation);

							}else{
								if (InitUtil.DEBUG)
									log.info("定位失败");
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								if (InitUtil.DEBUG)
									log.info(e.getMessage());
							}
						}
					}
				});
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zhlt.g1.interfaces.G1Interface#start()
	 */
	@Override
	public void start(Handler handler) throws Exception {

		mhandler = handler;
		// TODO Auto-generated method stub
		// if(getLocation()!=null){
		locationManager.requestLocationUpdates(
				android.location.LocationManager.GPS_PROVIDER, 1000, 1,
				locationListenerGPS);
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zhlt.g1.interfaces.G1Interface#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		if (locationListener != null)
			locationManager.removeUpdates(locationListener);
		if (locationListenerGPS != null)
			locationManager.removeUpdates(locationListenerGPS);

	}

	@Override
	public void log(boolean debug) throws Exception {

	}

	// 创建位置监听器
	private LocationListener locationListener = new LocationListener() {
		// 位置发生改变时调用
		@Override
		public void onLocationChanged(Location location) {
			if (InitUtil.DEBUG)
				log.info("onLocationChanged");
			if (InitUtil.DEBUG)
				log.info("onLocationChanged Latitude" + location.getLatitude());
			if (InitUtil.DEBUG)
				log.info("onLocationChanged location" + location.getLongitude());
			setLocation(location);
		}

		// provider失效时调用
		@Override
		public void onProviderDisabled(String provider) {
			if (InitUtil.DEBUG)
				log.info("onProviderDisabled");
		}

		// provider启用时调用
		@Override
		public void onProviderEnabled(String provider) {
			if (InitUtil.DEBUG)
				log.info("onProviderEnabled");
		}

		// 状态改变时调用
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			if (InitUtil.DEBUG)
				log.info("onStatusChanged");
		}

	};
	// 位置监听
	private LocationListener locationListenerGPS = new LocationListener() {

		/**
		 * 位置信息变化时触发
		 */
		public void onLocationChanged(Location location) {
			setLocation(location);

			// Log.i(TAG, "时间：" + location.getTime());
			// Log.i(TAG, "经度：" + location.getLongitude());
			// Log.i(TAG, "纬度：" + location.getLatitude());
			// Log.i(TAG, "海拔：" + location.getAltitude());
		}

		/**
		 * GPS状态变化时触发
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			// GPS状态为可见时
			case LocationProvider.AVAILABLE:
				// Log.i(TAG, "当前GPS状态为可见状态");
				break;
			// GPS状态为服务区外时
			case LocationProvider.OUT_OF_SERVICE:
				// Log.i(TAG, "当前GPS状态为服务区外状态");
				break;
			// GPS状态为暂停服务时
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				// Log.i(TAG, "当前GPS状态为暂停服务状态");
				break;
			}
		}

		/**
		 * GPS开启时触发
		 */
		public void onProviderEnabled(String provider) {
			Location location = locationManager.getLastKnownLocation(provider);
			setLocation(location);
		}

		/**
		 * GPS禁用时触发
		 */
		public void onProviderDisabled(String provider) {
			setLocation(null);
		}

	};
	GPSVO mlocation;

	private void setLocation(Location location) {
		if (location != null) {
			location.setAltitude(0);
			String address = getAddress(location);
			if (InitUtil.DEBUG)
				log.info("address:" + address);

			GPSVO gpsvo = new GPSVO();
			gpsvo.setId(GenSequenceUtil.getFullDateRandomNum(4));
			gpsvo.setLatitude(String.valueOf(location.getLatitude()));
			gpsvo.setLongitude(String.valueOf(location.getLongitude()));
			gpsvo.setAltitude(String.valueOf(location.getAltitude()));
			gpsvo.setBearing(String.valueOf(location.getBearing()));
			gpsvo.setSpeed(String.valueOf(location.getSpeed()));
			gpsvo.setTime(TimeUtil.getTime(TimeUtil.TIME2));

			if (InitUtil.DEBUG)
				log.info("VVVVVVVVvv1: " + gpsvo);
			gpsm.insertGPS(gpsvo);
			mlocation = gpsvo;
			if (mhandler != null)
				mhandler.obtainMessage(1, gpsvo).sendToTarget();
		}
	}

	public GPSVO getLocation() {

		return mlocation;
	}

	public String getAddress(Location l) {
		String result = null;
		if (l != null) {
			List<Address> listadd = getAddressbyGeoPoint(l);
			if (listadd != null && listadd.size() > 0) {
				result = parseAddr(listadd.get(0));
			}

		}
		return result;
	}

	private String parseAddr(Address address) {

		return address.getAddressLine(0) + address.getAddressLine(1)
				+ address.getAddressLine(2) + address.getFeatureName();
	}

	// 获取地址信息

	private List<Address> getAddressbyGeoPoint(Location location) {

		List<Address> result = null;

		// 先将 Location 转换为 GeoPoint

		// GeoPoint gp =getGeoByLocation(location);

		try {

			if (location != null) {

				// 获取 Geocoder ，通过 Geocoder 就可以拿到地址信息

				Geocoder gc = new Geocoder(may, Locale.CHINA);

				result = gc.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return result;

	}

	public ArrayList<GPSVO> selectlistGPS(String start, String end) {
		return gpsm.selectlistGPS(start, end);
	}

}
