package com.zhlt.g1app.fragment;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.capricorn.ArcMenu;
import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.BaseFuncView;
import com.zhlt.g1app.basefunc.Codes;
import com.zhlt.g1app.basefunc.InitUtil;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.MyApplication;
import com.zhlt.g1app.basefunc.SocketUtil;
import com.zhlt.g1app.basefunc.TcpClient;
import com.zhlt.g1app.basefunc.TcpClientHandler;
import com.zhlt.g1app.basefunc.netty.NettyServerAPP;
import com.zhlt.g1app.data.DataCommon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

public class FrgCarState extends FrgBase implements OnClickListener {

	private final int AD_SCROLL_DURATION_MILLS = 5000;// 切换间隔时间，单位秒

	private View mRootView;
	private ViewPager mViewPager = null;
	private Runnable mRunnable = null;
	private Logger log4jUtil = Log4jUtil.getLogger("FrgCarState");
	private ArrayList<String> mAdvFragments = new ArrayList<String>();
	private CarHandler mHandler;
	private boolean mIsLoadSuccess = false;
	private ImageButton mNaviIBtn;
	private LinearLayout mNaviView;
	private Animation mShowAnimation;
	private Animation mHideAnimation;
	private boolean mIsInputShow = false; // 输入框显示

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerPoi;

	private InfoWindow mInfoWindow;

	private Button mLocBtn;

	private ArcMenu mArcMenu;

	TcpClientHandler clientListen;
	TcpClient client;
	/**
	 * 是否已经上线
	 */
	boolean islogin = false;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		log4jUtil.info("zzw Car onCreateView");
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.frg_car_state, container,
					false);
		}

		inittcp();

		initValiable();
		initView();
		initMapView();
		initArcStatus();
		focusMyLoc();
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);// 先移除
		}
		mIsPrepare = true;
		return mRootView;
	}

	private void inittcp() {
		registerBoradcastReceiver();
		getActivity().startService(
				new Intent(getActivity(), NettyServerAPP.class));
	}



	public void testGetAllData() throws Exception {

		JSONObject obj = new JSONObject();
		try {
			obj.put("code", Codes.CODE6001);
			obj.put("source", 1);
			obj.put("key", InitUtil.KEY);
			System.out.println("send:" + obj.toString());
			Intent mIntent = new Intent();
			mIntent.setAction("com.example.mytest");
			mIntent.putExtra("msg", obj.toString());
			// 发送广播
			getActivity().sendBroadcast(mIntent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void NettyResult(String msg) {
		log4jUtil.info("client接收到服务器返回的消息:" + msg.toString());
		JSONObject json = null;
		if (msg != null) {

			try {
				json = new JSONObject(msg.toString().trim());

				int code = json.optInt("code");
				log4jUtil.info("code =======" + code);
				switch (code) {
				case Codes.CODE4001:
					Toast.makeText(getActivity(), "car 不在线!", Toast.LENGTH_LONG).show();
					break;
				case Codes.CODE1000:
					// 上线
					int state = json.optInt("state");
					switch (state) {
					case 1:
						islogin = true;
						log4jUtil.info("上线成功!");
						mHandler.obtainMessage(Codes.CODE1000).sendToTarget();
						break;
					case 2:
						islogin = false;
						log4jUtil.info("上线失败!");

						break;
					case 3:
						islogin = false;
						log4jUtil.info("设备不在线!");
						break;
					case 4:
						islogin = false;
						log4jUtil.info("网络异常!");
						break;

					default:
						break;
					}
					break;
				case Codes.CODE6001:
					// 上线
					int state6001 = json.optInt("state");
					switch (state6001) {
					case 1:
						String msg6001 = "";
						msg6001 = json.optString("data");
						log4jUtil.info("返回数据成功" + msg6001);
						mhandler.obtainMessage(InitUtil.CODE5, msg6001)
								.sendToTarget();
						// mHandler.obtainMessage(Codes.CODE6001,
						// msg6001).sendToTarget();
						break;
					case 2:
						mHandler.sendEmptyMessage(Codes.CODE4001);
						break;

					default:
						break;
					}
					break;
				case Codes.CODE1005:
					int state1005 = json.optInt("state");
					switch (state1005) {
					case 1:
						String msg1005 = "";
						msg1005 = json.optString("data");
						log4jUtil.info("返回数据成功" + msg1005);
						mhandler.obtainMessage(InitUtil.CODE5, msg1005)
								.sendToTarget();
						// mHandler.obtainMessage(Codes.CODE6001,
						// msg6001).sendToTarget();
						break;
					case 2:
						mHandler.sendEmptyMessage(Codes.CODE4001);
						break;

					default:
						break;
					}
					
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("client接收到服务器返回的消息:null");
			// startupgps(Codes.CODE1005, socket);
		}
	}

	private static class CarHandler extends Handler {

		private WeakReference<FrgCarState> reference;

		public CarHandler(FrgCarState fragment) {
			reference = new WeakReference<FrgCarState>(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			FrgCarState fragment = reference.get();
			if (fragment == null) {
				return;
			}
			switch (msg.what) {
			case DataCommon.Message.MSG_ADV_TEXT_lOADFINISHED:
				fragment.setAdvView();
				break;

			default:
				break;
			}

		}

	}

	public void setAdvTextData(ArrayList<String> list) {

		log4jUtil.info("car setAdvTextData mIsPrepare:" + mIsPrepare);
		mAdvFragments = list;
		if (mIsPrepare) {
			mHandler.sendEmptyMessageDelayed(
					DataCommon.Message.MSG_ADV_TEXT_lOADFINISHED, 1000);
		}

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
	}

	private void setAdvView() {
		log4jUtil.info("car setAdvView");
		mIsLoadSuccess = true;
	}

	private void initView() {
		mViewPager = (ViewPager) mRootView.findViewById(R.id.vp_car);
		mNaviIBtn = (ImageButton) mRootView.findViewById(R.id.ibtn_car_navi);
		mNaviView = (LinearLayout) mRootView.findViewById(R.id.llyt_car_navi);
		mNaviIBtn.setOnClickListener(this);

		mLocBtn = (Button) mRootView.findViewById(R.id.r_btn_location);
		mLocBtn.setOnClickListener(this);

	}

	private void initArcStatus() {
		if (mArcMenu == null) {
			mArcMenu = (ArcMenu) mRootView.findViewById(R.id.r_arc_menu_state);
			for (int i = 0; i < state_default.length; i++) {
				View lItem = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_status, null);
				View bg = lItem.findViewById(R.id.r_rl_state);
				mArcItems.add(lItem);
				// bg.setBackgroundResource(0);
				// bg.setBackgroundResource(ITEM_DRAWABLES[i]);
				BaseFuncView.setBGResource(bg, state_default[i]);
				final int position = i;
				mArcMenu.addItem(lItem, new OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(getActivity(), "position:" + position,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}

	// 温度4
	private void showTempStatus(int pTemp) {
		TextView text = (TextView) mArcItems.get(4).findViewById(
				R.id.r_tv_state);
		text.setText(pTemp + "℃");
	}

	// 湿度3
	private void showHumiStatus(int pHumi) {
		TextView text = (TextView) mArcItems.get(3).findViewById(
				R.id.r_tv_state);
		text.setText(pHumi + "%");
	}

	// 紫外线2
	private void showUrayStatus(int pUray) {
		TextView text = (TextView) mArcItems.get(2).findViewById(
				R.id.r_tv_state);
		text.setText(mUrays[pUray]);
	}

	// 信号1
	private void showSignalStatus(int pSignal) {
		View bg = mArcItems.get(1).findViewById(R.id.r_rl_state);
		BaseFuncView.setBGResource(bg, state_signal[pSignal]);
	}

	// 电量0
	private void showBatteryStatus(int pBattery) {
		View bg = mArcItems.get(0).findViewById(R.id.r_rl_state);
		TextView text = (TextView) mArcItems.get(0).findViewById(
				R.id.r_tv_state);
		text.setText(pBattery + "%");
		int lNum = 0;
		if (pBattery == 100) {
			lNum = 4;
		} else {
			if (pBattery < 100) {
				lNum = 3;
			}
			if (pBattery < 80) {
				lNum = 2;
			}
			if (pBattery < 50) {
				lNum = 1;
			}
			if (pBattery < 20) {
				lNum = 0;
			}
		}
		BaseFuncView.setBGResource(bg, state_battery[lNum]);
	}

	int mBattery = 0;
	int mSignal = 0;
	int mUray = 0;
	int mHumi = 50;
	int mTemp = 60;
	//
	private String[] mUrays = { "弱", "中等", "强", "超强", };
	// 默认图片背景
	private static final int[] state_default = { R.drawable.state_battery_0,
			R.drawable.state_signal_0, R.drawable.state_uray,
			R.drawable.state_humidity, R.drawable.state_temp };
	// 信号图片
	private static final int[] state_signal = { R.drawable.state_signal_0,
			R.drawable.state_signal_1, R.drawable.state_signal_2,
			R.drawable.state_signal_3, R.drawable.state_signal_4 };
	// 电量图片
	private static final int[] state_battery = { R.drawable.state_battery_0,
			R.drawable.state_battery_1, R.drawable.state_battery_2,
			R.drawable.state_battery_3, R.drawable.state_battery_4,
			R.drawable.state_battery_5 };
	// 存储状态单元View
	private ArrayList<View> mArcItems = new ArrayList<View>();

	/**
	 * 更新状态数据
	 */
	private void changeArcStatus() {
		showBatteryStatus(mBattery % 100);
		showSignalStatus(mSignal % state_signal.length);
		showUrayStatus(mUray % mUrays.length);
		showHumiStatus(mHumi % 100);
		showTempStatus(mTemp % 100);
	}

	// private void removeView(ArcMenu pArcMenu) {
	// // 获取linearlayout子view的个数
	// int count = pArcMenu.getChildCount();
	// // 研究整个LAYOUT布局，第0位的是含add和remove两个button的layout
	// // 第count-1个是那个文字被置中的textview
	// // 因此，在remove的时候，只能操作的是0<location<count-1这个范围的
	// // 在执行每次remove时，我们从count-2的位置即textview上面的那个控件开始删除~
	// if (count - 2 > 0) {
	// // count-2>0用来判断当前linearlayout子view数多于2个，即还有我们点add增加的button
	// pArcMenu.removeViewAt(count - 2);
	// }
	// }

	private void initMapView() {
		mMapView = (MapView) mRootView.findViewById(R.id.r_bmap_view);
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
				// Button button = new Button(mRootView.getContext());
				// button.setBackgroundResource(0);
				// button.setBackgroundResource(R.drawable.popup);
				// OnInfoWindowClickListener listener = null;
				// if (marker == mMarkerPoi) {
				// button.setText("更改位置");
				// listener = new OnInfoWindowClickListener() {
				// public void onInfoWindowClick() {
				// LatLng ll = marker.getPosition();
				// LatLng llNew = new LatLng(ll.latitude + 0.005,
				// ll.longitude + 0.005);
				// marker.setPosition(llNew);
				// mBaiduMap.hideInfoWindow();
				// }
				// };
				// LatLng ll = marker.getPosition();
				// mInfoWindow = new InfoWindow(BitmapDescriptorFactory
				// .fromView(button), ll, -47, listener);
				// mBaiduMap.showInfoWindow(mInfoWindow);
				// }
				return true;
			}
		});
		
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			
			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMapStatusChangeFinish(MapStatus arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMapStatusChange(MapStatus arg0) {
				// TODO Auto-generated method stub
				/*if(mArcMenu.arcMenuCanAlpha()){
					mArcMenu.setAlpha(0.5f);				
				}*/
			}
		});
		
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				/*if(mArcMenu.arcMenuCanAlpha()){
					mArcMenu.setAlpha(0.5f);				
				}*/
			}
		});
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
	}

	private void initValiable() {
		mHandler = new CarHandler(this);
		mShowAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.show_input);
		mShowAnimation.setAnimationListener(mAnimShowListener);
		mHideAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.hide_input);
		mHideAnimation.setAnimationListener(mAnimHideListener);

	}

	private AnimationListener mAnimShowListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			log4jUtil.info("show onAnimationStart");
			mNaviView.setVisibility(View.VISIBLE);
			mIsInputShow = true;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub

		}
	};

	private AnimationListener mAnimHideListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			log4jUtil.info("hide onAnimationStart");
			mIsInputShow = false;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mNaviView.setVisibility(View.GONE);

		}
	};

	private void startADScroll() {
		log4jUtil.info("startADScroll");
		mHandler.postDelayed(mRunnable = new Runnable() {

			@Override
			public void run() {
				int currentItem = mViewPager.getCurrentItem();
				mViewPager.setCurrentItem(currentItem + 1);
				startADScroll();
			}
		}, AD_SCROLL_DURATION_MILLS);// 在UI线程上执行相关操作
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// removeView(mArcMenu);
		mMapView.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
		// mArcMenu.removeAllViews();
		startADScroll();// 在activity启动/切换到前台时,开始viewpager切换
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mHandler.removeCallbacks(mRunnable);// activity不可见/停止时，取消viewpager的切换
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 回收 bitmap 资源，需要在地图之前是否，否则地图显示可能报错
		if (mCar != null) {
			mCar.recycle();
		}
		if (mMapView != null) {
			mMapView.onDestroy();
		}

	}

	@Override
	protected void lazyLoad() {
		if (!mIsPrepare || !mIsVisiable) {
			return;
		}
		if (!mIsLoadSuccess) {
			setAdvView();
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ibtn_car_navi:
			log4jUtil.info("onClick:" + mIsInputShow);
			if (mIsInputShow) {
				mNaviView.startAnimation(mHideAnimation);
			} else {
				mNaviView.setVisibility(View.VISIBLE);
				mNaviView.startAnimation(mShowAnimation);
			}
			break;
		case R.id.r_btn_location:
			/*if (iswhile) {
				iswhile=false;
				isok=true;
				Toast.makeText(getActivity(), "开启循环定位!", Toast.LENGTH_LONG).show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						while (isok) {
						
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).start();
			}else{
				Toast.makeText(getActivity(), "关闭循环定位!", Toast.LENGTH_LONG).show();
				iswhile=true;
				isok=false;
			}*/
			getLocation(null);
			break;
		}

	}

	boolean isok = true,iswhile= true;

	// //////////////////
	public void getLocation(View view) {
		mBaiduMap.clear();
		// 定位
		// 获取感应器信息
		try {
			testGetAllData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showDeviceSensor(Message msg) {
		// TODO Auto-generated method stub
		String ltemper = "";
		String lscale = "";
		try {
			JSONObject obj = new JSONObject(msg.obj.toString());
			JSONArray data = new JSONArray(obj.getString("data"));
			ltemper = data.getJSONObject(0).getString("temperature");
			lscale = data.getJSONObject(1).getString("scale");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor mCar = BitmapDescriptorFactory
			.fromResource(R.drawable.track_car);

	private void showDevicePoi(Message msg) {
		String altitude = "";
		String longitude = "";
		String latitude = "";

		try {
			JSONArray arobj = new JSONArray(msg.obj.toString());
			System.out.println("arobj .size " + arobj.length());
			JSONObject jpsdata = (JSONObject) arobj.get(0);
			altitude = jpsdata.getString("altitude").trim();
			longitude = jpsdata.getString("longitude").trim();
			latitude = jpsdata.getString("latitude");

			JSONObject databatter = (JSONObject) arobj.get(1);
			mBattery = databatter.getInt("level");

			JSONObject dataultravioletRays = (JSONObject) arobj.get(2);
			mUray = dataultravioletRays.getInt("ultravioletRays");
			JSONObject datasignal = (JSONObject) arobj.get(3);
			mSignal = datasignal.getInt("signal");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Double.parseDouble(latitude);
		LatLng mPoi = new LatLng(Double.parseDouble(latitude),
				Double.parseDouble(longitude));
		OverlayOptions oomPoi = new MarkerOptions().position(mPoi).icon(mCar)
				.anchor(0.5f, 0.5f).zIndex(5);
		mMarkerPoi = (Marker) (mBaiduMap.addOverlay(oomPoi));

		MapStatusUpdate v_mapstatusupdate = MapStatusUpdateFactory
				.newLatLng(mPoi);
		mBaiduMap.animateMapStatus(v_mapstatusupdate);
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
				changeArcStatus();
				break;

			case InitUtil.CODE7:
				// 显示传感器信息
				showDeviceSensor(msg);
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private static Logger log = Log4jUtil.getLogger("FrgCarState");

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
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(InitUtil.ACTION_NAME)) {
				String msg = intent.getStringExtra("msg");
				NettyResult(msg);
				log4jUtil.info("result:" + msg);
				/*
				 * Toast.makeText(getActivity(), "广播" + msg, Toast.LENGTH_LONG)
				 * .show();
				 */
			}
		}

	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(InitUtil.ACTION_NAME);

		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}


	
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner mListener = new MyLocationListenner();
	public void focusMyLoc(){
		// 定位初始化
		mLocClient = new LocationClient(getActivity());
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
			//后台运行时也可以走这里，只是不更新地图信息，以免引起FC
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).speed(location.getSpeed()).latitude(location.getLatitude())
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
