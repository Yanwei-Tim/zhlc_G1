package com.zhlt.g1app.fragment;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.model.LatLng;
import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.InitUtil;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.MyApplication;
import com.zhlt.g1app.basefunc.SocketUtil;
import com.zhlt.g1app.data.CommonData;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
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
	private Logger log4jUtil = Log4jUtil.getLogger("");
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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mMapView.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		log4jUtil.info("zzw Car onCreateView");
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.frg_car_state, null);
		}
		initValiable();
		initView();
		initMapView();
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);// 先移除
		}
		mIsPrepare = true;
		return mRootView;
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
			case CommonData.Message.MSG_ADV_TEXT_lOADFINISHED:
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
					CommonData.Message.MSG_ADV_TEXT_lOADFINISHED, 1000);
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
				Button button = new Button(mRootView.getContext());
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

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
		// 回收 bitmap 资源
		bdB.recycle();
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
			getLocation(null);
			break;
		}

	}
	
	
	////////////////////
	public void getLocation(View view) {
		mBaiduMap.clear();
		// 定位
		startsensor(InitUtil.CODE5);
		Toast.makeText(getActivity(), "获取设备位置信息", Toast.LENGTH_LONG).show();
	}
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdB = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_markb);
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
}
