package com.zhlt.g1app.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;
import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.BaseFuncView;
import com.zhlt.g1app.basefunc.InitUtil;
import com.zhlt.g1app.basefunc.MyApplication;
import com.zhlt.g1app.basefunc.SocketUtil;
import com.zhlt.g1app.fragment.FrgCarState.MyLocationListenner;

/**
 * 
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 * 
 * File: ActHistoryTrack.java Description:历史轨迹播放
 * 
 * Title: ActHistoryTrack Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * luoyin@gobabymobile.cn 2015-6-23 下午4:34:24 1.0 Create this moudle
 */
public class ActHistoryTrack extends Activity implements OnClickListener,
		OnGetGeoCoderResultListener {

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerPoi;
	List<Marker> mMarkerList = new ArrayList<Marker>();
	private InfoWindow mInfoWindow;

	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor mCar = BitmapDescriptorFactory
			.fromResource(R.drawable.track_car);
	BitmapDescriptor mMapDot = BitmapDescriptorFactory
			.fromResource(R.drawable.map_dot);

	private TextView mTopTitle;

	private View mTitleLeft;
	protected Context context;

	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_history_track);
		context = this;
		mMapView = (MapView) findViewById(R.id.r_history_bmap_view);
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				calendar.setVisibility(View.GONE);
				mBaiduMap.hideInfoWindow();
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				calendar.setVisibility(View.GONE);
				mBaiduMap.hideInfoWindow();
			}
		});
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());
				// button.setBackgroundResource(0);
				// button.setBackgroundResource(R.drawable.popup);
				BaseFuncView.setBGResource(button, R.drawable.popup);
				OnInfoWindowClickListener listener = null;
				if (marker == mMarkerPoi) {
					button.setText("更改位置");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							LatLng ll = marker.getPosition();
							LatLng llNew = new LatLng(ll.latitude + 0.005,
									ll.longitude + 0.005);
							marker.setPosition(llNew);
							mBaiduMap.hideInfoWindow();
						}
					};
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory
							.fromView(button), ll, -47, listener);
					mBaiduMap.showInfoWindow(mInfoWindow);
				}
				for (int pInt = 0; pInt < mMarkerList.size(); pInt++) {
					if (mMarkerList.get(pInt) == marker) {

						showInfoWindow(marker.getPosition());
						mSearch.reverseGeoCode(new ReverseGeoCodeOption()
								.location(mTrackList.get(pInt)));
					}
				}

				return true;
			}
		});
		initView();

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		focusMyLoc();
	}

	/**
	 * 地图上显示布局xml窗口
	 * 
	 * @param pLatLng
	 */
	private void showInfoWindow(LatLng pLatLng) {
		View lInfoWindow = this.getLayoutInflater().inflate(
				R.layout.map_info_view, null);
		InfoWindow mInfoWindow = new InfoWindow(lInfoWindow, pLatLng, -50);
		mBaiduMap.showInfoWindow(mInfoWindow);
	}

	private CalendarPickerView calendar;

	private LinearLayout mDate;

	/**
	 * 初始化控件
	 */
	private void initView() {

		mTopTitle = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleLeft = findViewById(R.id.r_ib_title_left);
		mTopTitle.setText(getString(R.string.history_track));
		mTitleLeft.setVisibility(View.VISIBLE);
		mTitleLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goBack();
			}
		});
		mDate = (LinearLayout) findViewById(R.id.r_ll_date);
		mDate.setOnClickListener(this);
		findViewById(R.id.r_btn_play).setOnClickListener(this);
		initCalendarPickerView();
	}

	/**
	 * 初始化第三方日历控件
	 */
	private void initCalendarPickerView() {
		final Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		final Calendar lastYear = Calendar.getInstance();
		lastYear.add(Calendar.YEAR, -1);

		calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
		calendar.init(lastYear.getTime(), nextYear.getTime()) //
				.inMode(SelectionMode.SINGLE) //
				.withSelectedDate(new Date());
		calendar.setOnDateSelectedListener(new OnDateSelectedListener() {

			@Override
			public void onDateUnselected(Date date) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDateSelected(Date date) {
				// TODO Auto-generated method stub
				calendar.setVisibility(View.GONE);
				// SimpleDateFormat format=new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String lDateStr = format.format(calendar.getSelectedDate());
				((TextView) findViewById(R.id.r_tv_date)).setText(lDateStr);
				showDeviceTrack();
				showJsonTrack();
				showArrayTrack();
			}
		});

	}

	private void showArrayTrack() {
		mTrackList.clear();
		mMarkerList.clear();
		for (int i = 0; i < mLatLngs.length; i++) {
			mTrackList.add(mLatLngs[i]);
		}
		showDeviceTrack();
	}

	private void playArrayTrack() {
		mTrackList.clear();
		mMarkerList.clear();
		mcrui = 0;
		for (int i = 0; i < mLatLngs.length; i++) {
			mTrackList.add(mLatLngs[i]);
		}
		playDeviceTrack();
	}

	private void showJsonTrack() {
		new Thread() {
			@Override
			public void run() {

				String json = loadJson("http://120.24.91.146:8080/hjyPC/UserAction?act={act:%27daygps%27}");
				Log.e("LUOYINTEST", "json=" + json);
				getJsonGPSData(json, 1);
			}
		}.start();
	}

	private void playJsonTrack() {
		new Thread() {
			@Override
			public void run() {

				String json = loadJson("http://120.24.91.146:8080/hjyPC/UserAction?act={act:%27daygps%27}");
				Log.e("LUOYINTEST", "json=" + json);
				getJsonGPSData(json, 0);
			}
		}.start();
	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	private void clearOverlay(View view) {
		mBaiduMap.clear();
		// 定位
		startsensor(InitUtil.CODE5);
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	private void resetOverlay(View view) {
		// clearOverlay(null);
		// initOverlay();
		mBaiduMap.clear();
		// 路线
		startsensor(InitUtil.CODE6);
	}

	/**
	 * 响应播放历史轨迹
	 * 
	 * @param view
	 */
	private void playTrack(View view) {
		 clearOverlay(null);
		mBaiduMap.clear();
		 //initOverlay();
		Toast.makeText(this, "播放轨迹", Toast.LENGTH_LONG).show();
		startsensor(InitUtil.CODE6);
		playJsonTrack();
		playDeviceTrack();
		playArrayTrack();
	}

	private void focusingDevice(LatLng pLatLng) {
		// Point point=
		// mMapView.getLayoutDirection().toPixels();//将地图坐标转换成屏幕坐标，p.get(temp-1)获取的是一个GeoPoint
		// if(point.x < 0 || point.x > pt.x*2 || point.y < 0 || point.y >
		// pt.y*2)
		// {
		// mapController.animateTo(p.get(temp));
		// }
		Point lPoicenter = mBaiduMap.getMapStatus().targetScreen;
		Point point = mBaiduMap.getProjection().toScreenLocation(pLatLng);
		if (point.x < 0 || point.x > lPoicenter.x * 2 || point.y < 0
				|| point.y > lPoicenter.y * 2) {
			// 聚焦
			MapStatusUpdate v_mapstatusupdate = MapStatusUpdateFactory
					.newLatLng(pLatLng);
			mBaiduMap.animateMapStatus(v_mapstatusupdate);
		}
	}

	private Handler mJsonHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (mTrackList.size() > 0) {
					playDeviceTrack();
				} else {
					Toast.makeText(context, "无定位数据！", Toast.LENGTH_LONG).show();
				}

				break;
			case 1:
				if (mTrackList.size() > 0) {
					showDeviceTrack();
				} else {
					Toast.makeText(context, "无定位数据！", Toast.LENGTH_LONG).show();
				}
				break;
			}
		};
	};

	/**
	 * 获取GPS的数据
	 * 
	 * @param pdata
	 *            :JSON字串
	 * @param pwhat
	 *            :0播放轨迹，1显示轨迹
	 */
	private void getJsonGPSData(String pdata, int pwhat) {
		JSONObject dataJson;
		try {
			dataJson = new JSONObject(pdata);
			// JSONObject response=dataJson.getJSONObject("obj");
			JSONArray data = dataJson.getJSONArray("obj");
			JSONObject item;
			mTrackList.clear();
			mMarkerList.clear();
			mcrui = 0;
			for (int i = 0; i < data.length(); i++) {
				item = data.getJSONObject(i);
				Log.e("LUOYINTEST",
						"gps_latitude[" + i + "]="
								+ item.getString("gps_latitude"));
				Log.e("LUOYINTEST",
						"gps_longitude[" + i + "]="
								+ item.getString("gps_longitude"));
				LatLng trPoi = new LatLng(Double.parseDouble(item
						.getString("gps_latitude")), Double.parseDouble(item
						.getString("gps_longitude")));
				mTrackList.add(trPoi);
			}
			mJsonHandler.sendEmptyMessage(pwhat);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 响应日期选择
	 * 
	 * @param view
	 */
	private void selectDate(View view) {
		mBaiduMap.clear();
		if (calendar.getVisibility() == View.VISIBLE) {
			calendar.setVisibility(View.GONE);
		} else if (calendar.getVisibility() == View.GONE) {
			// 选择日期
			calendar.setVisibility(View.VISIBLE);
			Toast.makeText(this, "日期选择", Toast.LENGTH_LONG).show();
		}
	}

	List<LatLng> mTrackList = new ArrayList<LatLng>();
	List<Float> mbearingList = new ArrayList<Float>();

	/**
	 * 显示设备轨迹
	 */
	private void showDeviceTrack() {
		// for (int i = 0; i < mTrackList.size(); i++) {
		// //地图上画点
		// DotOptions lDotOpt = new
		// DotOptions().center(mTrackList.get(i)).color(Color.RED).radius(16).zIndex(6);
		// OverlayOptions loo = new
		// MarkerOptions().icon(mMapDot).position(mTrackList.get(i));
		// mMarkerList.add((Marker) mBaiduMap.addOverlay(loo));
		// }
		// 画轨迹线
		PolylineOptions v_polylineopt = new PolylineOptions()
				.points(mTrackList).color(Color.RED).width(7);
		mBaiduMap.addOverlay(v_polylineopt);
//		for (int i = 0; i < v_polylineopt.getPoints().size(); i++) {
//			
//		}

		// 聚焦
		MapStatusUpdate v_mapstatusupdate = MapStatusUpdateFactory
				.newLatLngZoom(mTrackList.get(0), 16);
		mBaiduMap.animateMapStatus(v_mapstatusupdate);
	}

	LatLng[] mLatLngs = { new LatLng(22.5543360000, 113.9484570000),
			new LatLng(22.5534180000, 113.9485280000),
			new LatLng(22.5528180000, 113.9485820000),
			new LatLng(22.5528180000, 113.9485820000),
			new LatLng(22.5522170000, 113.9485820000),
			new LatLng(22.5519160000, 113.9485640000),
			new LatLng(22.5519160000, 113.9475760000),
			new LatLng(22.5518660000, 113.9464800000),
			new LatLng(22.5518330000, 113.9459230000),
			new LatLng(22.5518500000, 113.9453120000),
			new LatLng(22.5529010000, 113.9454740000),
			new LatLng(22.5543200000, 113.9456720000),
			new LatLng(22.5551370000, 113.9459410000),
			new LatLng(22.5561890000, 113.9459050000),
			new LatLng(22.5572570000, 113.9459230000),
			new LatLng(22.5574570000, 113.9443240000),
			new LatLng(22.5578570000, 113.9437310000),
			new LatLng(22.5576410000, 113.9409470000),
			new LatLng(22.5577570000, 113.9388450000),
			new LatLng(22.5579910000, 113.9365450000),
			new LatLng(22.5581580000, 113.9338140000),
			new LatLng(22.5585750000, 113.9320890000),
			new LatLng(22.5588090000, 113.9303110000),
			new LatLng(22.5599100000, 113.9302390000),
			new LatLng(22.5622460000, 113.9306520000) };

	/**
	 * 播放设备轨迹
	 */
	private void playDeviceTrack() {
		MapStatusUpdate v_mapstatusupdate = MapStatusUpdateFactory
				.newLatLngZoom(mTrackList.get(0), 16);
		mBaiduMap.animateMapStatus(v_mapstatusupdate);
		if (mHandler != null) {
			mHandler.removeCallbacks(mPlayRunnable);
			mHandler = null;
		}
		mCurLaLng = new LatLng(mTrackList.get(0).latitude,
				mTrackList.get(0).longitude);
		mHandler = new Handler();
		mHandler.post(mPlayRunnable);

	}

	private Handler mHandler;
	Overlay mOverlay;
	Overlay mPolylineOverlay;
	LatLng mCurLaLng;
	int mcrui = 0;
	List<LatLng> mPolylineList = new ArrayList<LatLng>();
	/**
	 * 循环播放线程
	 */
	private Runnable mPlayRunnable = new Runnable() {

		@Override
		public void run() {
			if (ActHistoryTrack.this != null) {
				if (playFunc()) {
					mHandler.postDelayed(mPlayRunnable, 150);// 延迟调用
				}
			}
		}
	};

	/**
	 * 获取播放纬度偏移量
	 * 
	 * @param disLa
	 * @param disLo
	 * @return
	 */
	private double getLatOffset(double pLatDis, double pLonDis) {
		if (pLonDis > -0.0000001 && pLonDis < 0.000001 && pLatDis > -0.0000001
				&& pLatDis < 0.000001) {
			return 0;
		} else {
			return pLatDis / Math.sqrt(pLatDis * pLatDis + pLonDis * pLonDis);
		}
	}

	/**
	 * 获取播放经度偏移量
	 * 
	 * @param disLa
	 * @param disLo
	 * @return
	 */
	private double getLonOffset(double pLatDis, double pLonDis) {
		if (pLonDis > -0.0000001 && pLonDis < 0.000001 && pLatDis > -0.0000001
				&& pLatDis < 0.000001) {
			return 0;
		} else {
			return pLonDis / Math.sqrt(pLatDis * pLatDis + pLonDis * pLonDis);
		}
	}

	/**
	 * 播放轨迹功能
	 * 
	 * @return
	 */
	private boolean playFunc() {

		double lLonDis = mTrackList.get(mcrui + 1).longitude
				- mTrackList.get(mcrui).longitude;
		double lLatDis = mTrackList.get(mcrui + 1).latitude
				- mTrackList.get(mcrui).latitude;
		double lOffset = Math.abs(getOffset(100));
		double lLatOffset = lOffset * getLatOffset(lLatDis, lLonDis);
		double lLonOffset = lOffset * getLonOffset(lLatDis, lLonDis);
		double lDirection = Math.toDegrees(Math.atan2(lLatDis, lLonDis));

		if (Math.abs(mCurLaLng.longitude - mTrackList.get(mcrui + 1).longitude) < Math
				.abs(lLonOffset)
				|| Math.abs(mCurLaLng.latitude
						- mTrackList.get(mcrui + 1).latitude) < Math
							.abs(lLatOffset)
				|| (lLatOffset == 0 && lLonOffset == 0)) {
			// 下一个转折点
			if (mcrui < mTrackList.size() - 2)
				mcrui++;
			else {
				Toast.makeText(this, "播放完毕", Toast.LENGTH_LONG).show();
				return false;
			}
			mCurLaLng = new LatLng(mTrackList.get(mcrui).latitude,
					mTrackList.get(mcrui).longitude);
		}
		OverlayOptions loo = new MarkerOptions().icon(mCar).position(mCurLaLng)
				.anchor(0.5f, 0.5f).rotate((float) lDirection);

		if (mOverlay != null)
			mOverlay.remove();
		mOverlay = mBaiduMap.addOverlay(loo);

		List<LatLng> lPolylineList = new ArrayList<LatLng>();
		for (int i = 0; i < mcrui + 1; i++) {
			lPolylineList.add(mTrackList.get(i));
		}
		lPolylineList.add(mCurLaLng);
		PolylineOptions v_polylineopt = new PolylineOptions()
				.points(lPolylineList).color(Color.GREEN).width(7);
		if (mPolylineOverlay != null)
			mPolylineOverlay.remove();
		mPolylineOverlay = mBaiduMap.addOverlay(v_polylineopt);
		focusingDevice(mCurLaLng);

		mCurLaLng = new LatLng(mCurLaLng.latitude + lLatOffset,
				mCurLaLng.longitude + lLonOffset);
		return true;
	}

	/**
	 * 获取地图显示区域纬度均分尺寸
	 * 
	 * @param pGrade
	 *            均分等级
	 * @return
	 */
	private double getOffset(double pGrade) {
		LatLngBounds lBounds = mBaiduMap.getMapStatus().bound;
		return (lBounds.northeast.longitude - lBounds.southwest.longitude)
				/ pGrade;
	}

	public void goBack() {
		finish();
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		mCar.recycle();
		mMapDot.recycle();
		stopPlayTrack();
	}

	private void stopPlayTrack() {
		if (mHandler != null) {
			mHandler.removeCallbacks(mPlayRunnable);
			mHandler = null;
		}
	}

	/**
	 * 实际显示轨迹回调方法
	 * 
	 * @param msg
	 */
	private void showDeviceTrack(Message msg) {
		List<LatLng> trackList = new ArrayList<LatLng>();
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject(msg.obj.toString());
			Log.e("LUOYINTEST", "obj=" + obj.toString());
			// JSONArray data = obj.getJSONArray("data");
			JSONArray data = new JSONArray(obj.getString("data"));
			JSONObject item;
			Log.e("LUOYINTEST", "data=" + data.length());
			for (int i = 0; i < data.length(); i++) {
				item = data.getJSONObject(i);
				LatLng trPoi = new LatLng(Double.parseDouble(item
						.getString("latitude")), Double.parseDouble(item
						.getString("longitude")));
				trackList.add(trPoi);
				// 地图上画点
				DotOptions lDotOpt = new DotOptions().center(trPoi)
						.color(Color.GREEN).radius(8).zIndex(6);
				mBaiduMap.addOverlay(lDotOpt);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MapStatusUpdate v_mapstatusupdate = MapStatusUpdateFactory
				.newLatLngZoom(trackList.get(0), 15);
		mBaiduMap.animateMapStatus(v_mapstatusupdate);
	}

	/**
	 * 显示设备当前位置
	 * 
	 * @param msg
	 */
	private void showDevicePoi(Message msg) {
		String altitude = "";
		String longitude = "";
		String latitude = "";
		try {
			JSONObject obj = new JSONObject(msg.obj.toString());
			JSONObject data = new JSONObject(obj.getString("data"));
			altitude = data.getString("altitude");
			longitude = data.getString("longitude");
			latitude = data.getString("latitude");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Double.parseDouble(latitude);
		LatLng mPoi = new LatLng(Double.parseDouble(latitude),
				Double.parseDouble(longitude));
		OverlayOptions oomPoi = new MarkerOptions().position(mPoi).icon(mCar)
				.zIndex(5);
		mMarkerPoi = (Marker) (mBaiduMap.addOverlay(oomPoi));

		MapStatusUpdate v_mapstatusupdate = MapStatusUpdateFactory
				.newLatLngZoom(mPoi, 15);
		mBaiduMap.animateMapStatus(v_mapstatusupdate);
	}

	/**
	 * 响应直连端数据句柄
	 */
	private Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				// tv.setText(msg.obj.toString());
				break;
			case InitUtil.CODE5:
				// 显示位置
				showDevicePoi(msg);
				break;
			case InitUtil.CODE6:
				// 显示路线
				showDeviceTrack(msg);
				break;
			case 3:
				JSONObject obj = null;
				try {
					obj = new JSONObject(msg.obj.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String path = obj.optString("data");
				getphoto(path);

				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void getphoto(final String path) {

		// TODO Auto-generated method stub
		MyApplication.getInstance().getFixedThreadPool()
				.execute(new Runnable() {

					@Override
					public void run() {
						Socket request = null;
						try {

							request = new Socket("192.168.43.1", 9999);

							JSONObject obj = new JSONObject();
							obj.put("code", InitUtil.CODE12);
							obj.put("state", 1);// 1,打开，2关闭 3,log
							obj.put("source", 1);// 1 wifi手机，2服务器
							obj.put("data", path);// 返回的图片地址
							String codestr = obj.toString().trim();
							while (true) {
								// write response info

								if (codestr != null) {
									SocketUtil.writeStr2StreamVideo(
											codestr.getBytes(),
											request.getOutputStream());
									if (InitUtil.DEBUG)
										codestr = null;

								}
								// get info from request when getting a socket
								// request
								byte[] reqStr = SocketUtil
										.readStrFromStreamdatabyte(request
												.getInputStream());
								if (reqStr != null && reqStr.length > 0) {
									Bitmap bitmap = BitmapFactory
											.decodeByteArray(reqStr, 0,
													reqStr.length);
									if (bitmap != null) {
										mhandler.obtainMessage(2, bitmap)
												.sendToTarget();
										break;
									}
								} else {
								}
								Thread.sleep(1000);
							}
						} catch (IOException e) {
						} catch (Throwable e) {
						} finally {
							if (request != null) {
								try {
									// request.getInputStream().close();
									// request.getOutputStream().close();
									request.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}

					}
				});

	}

	private void startsensor(final int code) {
		// TODO Auto-generated method stub
		MyApplication.getInstance().getFixedThreadPool()
				.execute(new Runnable() {

					@Override
					public void run() {
						Socket request = null;
						try {

							request = new Socket("192.168.43.1", 9999);

							JSONObject obj = new JSONObject();
							obj.put("code", code);
							obj.put("state", 1);// 1,打开，2关闭 3,log
							obj.put("source", 1);// 1 wifi手机，2服务器
							if (code == InitUtil.CODE6) {
								JSONObject objjt = new JSONObject();
								objjt.put("start", "2015-06-01 09:09:00");// 查询历史记录的时候用得，开始时间
								objjt.put("end", "2015-07-09 09:09:00");// 结束时间
								obj.put("data", objjt); // 传递的参数
							}
							String codestr = obj.toString().trim();
							while (true) {
								// write response info

								if (codestr != null) {
									SocketUtil.writeStr2StreamVideo(
											codestr.getBytes(),
											request.getOutputStream());
									codestr = null;

								}
								// get info from request when getting a socket
								// request
								byte[] reqStr = SocketUtil
										.readStrFromStreamdatabyte(request
												.getInputStream());
								if (reqStr != null && reqStr.length > 0) {

									String result = new String(reqStr).trim();
									if (result != null && !result.equals("")
											&& !result.equals("null")) {
										if (code == InitUtil.CODE2) {
											mhandler.obtainMessage(3, result)
													.sendToTarget();
										} else {
											mhandler.obtainMessage(code, result)
													.sendToTarget();
										}
										break;
									}

									// mhandler.obtainMessage(3,reqStr).sendToTarget();
								} else {
									System.out.println("=====等于null");
								}
								Thread.sleep(1000);
							}
						} catch (IOException e) {
						} catch (Throwable e) {
						} finally {
							if (request != null) {
								try {
									// request.getInputStream().close();
									// request.getOutputStream().close();
									request.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}

					}
				});

	}

	@Override
	public void onClick(View pView) {

		// TODO Auto-generated method stub
		switch (pView.getId()) {
		case R.id.r_ll_date:
			stopPlayTrack();
			selectDate(null);
			break;
		case R.id.r_btn_play:
			playTrack(null);
			break;
		default:
			break;
		}
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(context, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		Toast.makeText(context, result.getAddress(), Toast.LENGTH_LONG).show();
		((TextView) findViewById(R.id.r_tv_loc_1)).setText(result
				.getAddressDetail().province
				+ result.getAddressDetail().city
				+ result.getAddressDetail().district);
		((TextView) findViewById(R.id.r_tv_loc_2)).setText(result
				.getAddressDetail().street
				+ result.getAddressDetail().streetNumber);
	}

	public static String loadJson(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner mListener = new MyLocationListenner();

	public void focusMyLoc() {
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(mListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// 后台运行时也可以走这里，只是不更新地图信息，以免引起FC
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).speed(location.getSpeed())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			mBaiduMap.setMyLocationData(locData);
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			mLocClient.stop();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
}
