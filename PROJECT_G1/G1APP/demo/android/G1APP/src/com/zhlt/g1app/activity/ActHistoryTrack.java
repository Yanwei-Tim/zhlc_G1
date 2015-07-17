package com.zhlt.g1app.activity;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;
import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.InitUtil;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.MyApplication;
import com.zhlt.g1app.basefunc.SocketUtil;
import com.zhlt.g1app.fragment.FrgCamera;
import com.zhlt.g1app.fragment.FrgCarState;
import com.zhlt.g1app.fragment.FrgCommunity;
import com.zhlt.g1app.fragment.FrgSettings;

/**
 * 演示覆盖物的用法
 */
public class ActHistoryTrack extends Activity {
	private static Logger log = Log4jUtil.getLogger("ActHistoryTrack");

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerPoi;

	private InfoWindow mInfoWindow;

	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdB = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markb);

	private TextView mTopTitle;

	private Button mTitleLeft;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_history_track);
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
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
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
				return true;
			}
		});
		initView();
	}

	private CalendarPickerView calendar;

	private TextView mDate;
	private void initView() {

		mTopTitle = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleLeft = (Button) findViewById(R.id.r_ib_title_left);
		mTopTitle.setText(getString(R.string.car_state));
		mTitleLeft.setVisibility(View.VISIBLE);
		mTitleLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goBack();
			}
		});
		mDate = (TextView) findViewById(R.id.r_tv_date);
		mDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectDate(null);
			}
		});
		initCalendarPickerView();
	}
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
			}
		});

	}
	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
		// 定位
		startsensor(InitUtil.CODE5);
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		// clearOverlay(null);
		// initOverlay();
		mBaiduMap.clear();
		// 路线
		startsensor(InitUtil.CODE6);
	}
	
	public void playTrack(View view) {
		// clearOverlay(null);
		mBaiduMap.clear();
		// initOverlay();
		Toast.makeText(this, "播放轨迹", Toast.LENGTH_LONG).show();
		startsensor(InitUtil.CODE6);
	}
	public void selectDate(View view) {
		//选择日期
		calendar.setVisibility(View.VISIBLE);
		Toast.makeText(this, "sasd", Toast.LENGTH_LONG).show();
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
		bdB.recycle();
	}

	// /////////////////
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
				Log.e("LUOYINTEST",
						"latitude[" + i + "]=" + item.getString("latitude"));
				Log.e("LUOYINTEST",
						"longitude[" + i + "]=" + item.getString("longitude"));
				// LatLng trPoi = new
				// LatLng(Double.parseDouble(item.getString("latitude"))+Math.random()/6000,
				// Double.parseDouble(item.getString("longitude"))+Math.random()/6000);
				LatLng trPoi = new LatLng(Double.parseDouble(item
						.getString("latitude")), Double.parseDouble(item
						.getString("longitude")));
				trackList.add(trPoi);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PolylineOptions v_polylineopt = new PolylineOptions().points(trackList)
				.color(Color.BLUE).width(7);
		mBaiduMap.addOverlay(v_polylineopt);
		MapStatusUpdate v_mapstatusupdate = MapStatusUpdateFactory
				.newLatLngZoom(trackList.get(0), 15);
		mBaiduMap.animateMapStatus(v_mapstatusupdate);
	}

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
		OverlayOptions oomPoi = new MarkerOptions().position(mPoi).icon(bdB)
				.zIndex(5);
		mMarkerPoi = (Marker) (mBaiduMap.addOverlay(oomPoi));

		MapStatusUpdate v_mapstatusupdate = MapStatusUpdateFactory
				.newLatLngZoom(mPoi, 15);
		mBaiduMap.animateMapStatus(v_mapstatusupdate);
		// mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(mPoi,
		// 11));
	}

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
										log.info("发送指令:" + codestr);
									codestr = null;

								}
								// get info from request when getting a socket
								// request
								byte[] reqStr = SocketUtil
										.readStrFromStreamdatabyte(request
												.getInputStream());
								if (reqStr != null && reqStr.length > 0) {
									if (InitUtil.DEBUG)
										log.info("接受到图片:" + reqStr.length);
									Bitmap bitmap = BitmapFactory
											.decodeByteArray(reqStr, 0,
													reqStr.length);
									if (bitmap != null) {
										mhandler.obtainMessage(2, bitmap)
												.sendToTarget();
										break;
									}
								} else {
									if (InitUtil.DEBUG)
										log.info("=====等于null");
								}
								Thread.sleep(1000);
							}
						} catch (IOException e) {
							if (InitUtil.DEBUG)
								log.error("G-sensor：指令" + e.getStackTrace());
						} catch (Throwable e) {
							if (InitUtil.DEBUG)
								log.error("G-sensor：指令" + e.getStackTrace());
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
									if (InitUtil.DEBUG)
										log.info("发送指令:" + codestr);
									codestr = null;

								}
								// get info from request when getting a socket
								// request
								byte[] reqStr = SocketUtil
										.readStrFromStreamdatabyte(request
												.getInputStream());
								if (reqStr != null && reqStr.length > 0) {

									String result = new String(reqStr).trim();
									if (InitUtil.DEBUG)
										log.info("接收到:" + result);
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
							if (InitUtil.DEBUG)
								log.error("G-sensor：指令"
										+ e.getLocalizedMessage());
						} catch (Throwable e) {
							if (InitUtil.DEBUG)
								log.error("G-sensor：指令"
										+ e.getLocalizedMessage());
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

}
