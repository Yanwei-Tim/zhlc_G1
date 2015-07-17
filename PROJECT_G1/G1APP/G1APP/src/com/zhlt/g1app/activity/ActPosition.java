package com.zhlt.g1app.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpPostionListView;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.SharePreferUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataCommon;
import com.zhlt.g1app.data.DataCity;
import com.zhlt.g1app.data.DataCounty;
import com.zhlt.g1app.data.DataProvince;
import com.zhlt.g1app.data.DataUser;
import com.zhlt.g1app.view.ViewXList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: ActPosition.java Description:地区
 * 
 * Title: ActPosition Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月23日 下午4:08:04 1.0 Create this moudle
 */
public class ActPosition extends Activity {

	/** 标题 */
	private TextView mTitleTv;

	/** 家乡 */
	private TextView mBelongTv;

	/** 返回按钮 */
	private View mBackView;

	/** 底部地区控件 */
	private View mBottomView;

	/** 隐藏的透明背景 */
	private View mTransparentView;

	/** 点击地区 */
	private View mBelongView;

	/** 定位按钮 */
	private View mLocationView;

	private TextView mLocationValeuTv;

	private ImageView mLocationNameTv;

	/** xml格式的中国省市区信息 */
	private String AddressXML;

	/** 省份列表 */
	private List<DataProvince> provinceList;

	/** 完成按钮 */
	private Button mOkBtn;

	/** 底部框显示动画 */
	private TranslateAnimation mShowAnimation;

	/** 底部框隐藏动画 */
	private TranslateAnimation mHideAnimation;

	/** Handler */
	private PostionHandler mHandler;

	/** 是否正在滚动 */
	private boolean mScrolling = false;

	/** 地区选择是否正在显示 */
	private boolean mIsPostionListShow = false;

	/** 用户数据 */
	private DataUser mDataUser;

	/** 打印 */
	private Logger mLogger = Log4jUtil.getLogger("");

	private ListView mListView;

	private AdpPostionListView mListViewAdapter;

	private ProgressBar mLocationPb;
	// /** 是否调试 */
	// private boolean mIsDebug = true;

	/** 是否从圈子跳转过来的 */
	private boolean isFromCommunity = false;

	/** 变蓝色文字的消息 */
	private final static int MSG_BLUE_COLOR = 1;

	/** 变黑色文字的消息 */
	private final static int MSG_BLACK_COLOR = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_position);
		initData();
		initView();
	}

	private static class PostionHandler extends Handler {

		private WeakReference<ActPosition> reference;

		public PostionHandler(ActPosition actPosition) {
			reference = new WeakReference<ActPosition>(actPosition);
		}

		@Override
		public void handleMessage(Message msg) {
			ActPosition actPosition = reference.get();
			if (actPosition == null) {
				return;
			}
			switch (msg.what) {
			case MSG_BLUE_COLOR:
				actPosition.setColor(actPosition.getResources().getColor(
						R.color.bluetextcolor));
				break;

			case MSG_BLACK_COLOR:
				actPosition.setColor(Color.BLACK);
				break;

			case DataCommon.Message.MSG_LOCATION_FINISHED:
				actPosition.mLocationPb.setVisibility(View.GONE);
				actPosition.mLocationNameTv.setVisibility(View.VISIBLE);
				actPosition.mLocationValeuTv.setText(msg.obj.toString());
				break;
			}

		}

	}

	private void setColor(int color) {
		mBelongTv.setTextColor(color);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mLocationView = findViewById(R.id.rlyt_position_location);
		mTitleTv.setText(R.string.position);
		if (getIntent() != null) {
			isFromCommunity = getIntent().getBooleanExtra("isFromCommunity",
					false);
			if (isFromCommunity) {
				mTitleTv.setText(R.string.change_position);
			}
		}
		mLocationPb = (ProgressBar) findViewById(R.id.pb_position_location);
		mLocationNameTv = (ImageView) findViewById(R.id.iv_position_location);
		// mBelongTv.setText(mDataUser.getUserPosition());
		// mBelongView = findViewById(R.id.rlyt_position_belong);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(mOnClickListener);
		// mBelongView.setOnClickListener(mOnClickListener);
		mLocationView.setOnClickListener(mOnClickListener);

		mLocationValeuTv = (TextView) findViewById(R.id.tv_position_here);

		// mTransparentView.setOnClickListener(mOnClickListener);
		// initPositionView();

		mListView = (ListView) findViewById(R.id.xListView_position);
		mListViewAdapter = new AdpPostionListView(this,
				com.zhlt.g1app.data.AddressData.PROVINCES, false);
		mListView.setAdapter(mListViewAdapter);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			int city = data.getIntExtra("i", -1);
			int provin = mListViewAdapter.getCurrentItem();
			String position = com.zhlt.g1app.data.AddressData.PROVINCES[provin]
					+ "-"
					+ com.zhlt.g1app.data.AddressData.CITIES[provin][city];
			// mBelongTv.setText(position);
			if (isFromCommunity) {
				SharePreferUtil.write(getApplicationContext(),
						DataCommon.SharePrefer.SharePrefer_Main_Position,
						com.zhlt.g1app.data.AddressData.CITIES[provin][city]);
			} else {
				mDataUser.setUserPosition(position);
			}
			finish();
		}
	}

	private void startCity(int i) {
		Intent intent = new Intent(this, ActCity.class);
		intent.putExtra("position", i);
		startActivity(intent);
	}

	/**
	 * 隐藏地区
	 */
	private void hide() {
		if (mHideAnimation == null) {
			mHideAnimation = new TranslateAnimation(0, 0, 0, 800);
			mHideAnimation.setDuration(500);
			mHideAnimation.setFillAfter(true);
			mHideAnimation.setAnimationListener(mHideAnimationListener);
		}
		mBottomView.startAnimation(mHideAnimation);
		if (!mHandler.hasMessages(MSG_BLACK_COLOR)) {
			mHandler.sendEmptyMessageDelayed(MSG_BLACK_COLOR, 500);
		}
		mIsPostionListShow = false;
	}

	/**
	 * 显示地区
	 */
	private void show() {

		mTransparentView.setVisibility(View.VISIBLE);
		mBottomView.setVisibility(View.VISIBLE);
		if (mShowAnimation == null) {
			mShowAnimation = new TranslateAnimation(0, 0, 500, 0);
			mShowAnimation.setDuration(500);
			mShowAnimation.setFillAfter(true);
		}
		mBottomView.startAnimation(mShowAnimation);
		if (!mHandler.hasMessages(MSG_BLUE_COLOR)) {
			mHandler.sendEmptyMessageDelayed(MSG_BLUE_COLOR, 500);
		}
		mIsPostionListShow = true;
	}

	private AnimationListener mHideAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mBelongTv.setTextColor(Color.BLACK);
			mBottomView.setVisibility(View.GONE);
			mTransparentView.setVisibility(View.GONE);
			save();
		}
	};

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;

			case R.id.rlyt_position_location:
				location();
				break;

			}
		}
	};

	private LocationClient mLocationClient;

	/**
	 * 定位
	 */
	private void location() {
		mLocationClient.start();
	}

	/**
	 * 初始化变量数据
	 */
	public void initData() {

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		mLocationClient.start();// 将开启与获取位置分开，就可以尽量的在后面的使用中获取到位
		mHandler = new PostionHandler(this);
		mDataUser = UserInfoUtil.getUserInfoUtil().getUserData();
		AddressXML = getRawAddress().toString();// 获取中国省市区信息
		try {
			analysisXML(AddressXML);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		// 初始化列表下标
	}

	public BDLocationListener myListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			if (arg0.getCity() == null) {
				int type = mLocationClient.requestLocation();
				mLogger.info("first request false" + type);
			}
			mHandler.sendMessage(mHandler.obtainMessage(
					DataCommon.Message.MSG_LOCATION_FINISHED,
					arg0.getProvince() + "-" + arg0.getCity()));

		}

	};

	/**
	 * 获取地区raw里的地址xml内容
	 * */
	public StringBuffer getRawAddress() {
		InputStream in = getResources().openRawResource(R.raw.address);
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			br.close();
			isr.close();
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return sb;
	}

	/**
	 * 解析省市区xml， 采用的是pull解析， 为什么选择pull解析：因为pull解析简单浅显易懂！
	 */
	public void analysisXML(String data) throws XmlPullParserException {
		try {
			DataProvince provinceModel = null;
			DataCity cityModel = null;
			DataCounty countyModel = null;
			List<DataCity> cityList = null;
			List<DataCounty> countyList = null;

			InputStream xmlData = new ByteArrayInputStream(
					data.getBytes("UTF-8"));
			XmlPullParserFactory factory = null;
			factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser;
			parser = factory.newPullParser();
			parser.setInput(xmlData, "utf-8");

			String province;
			String city;
			String county;

			int type = parser.getEventType();
			while (type != XmlPullParser.END_DOCUMENT) {
				String typeName = parser.getName();

				if (type == XmlPullParser.START_TAG) {
					if ("root".equals(typeName)) {
						provinceList = new ArrayList<DataProvince>();

					} else if ("province".equals(typeName)) {
						province = parser.getAttributeValue(0);// 获取标签里第一个属性,例如<city
																// name="北京市"
																// index="1">中的name属性
						provinceModel = new DataProvince();
						provinceModel.setProvince(province);
						cityList = new ArrayList<DataCity>();

					} else if ("city".equals(typeName)) {
						city = parser.getAttributeValue(0);
						cityModel = new DataCity();
						cityModel.setCity(city);
						countyList = new ArrayList<DataCounty>();

					} else if ("area".equals(typeName)) {
						county = parser.getAttributeValue(0);
						countyModel = new DataCounty();
						countyModel.setCounty(county);

					}

				} else if (type == XmlPullParser.END_TAG) {
					if ("root".equals(typeName)) {

					} else if ("province".equals(typeName)) {
						provinceModel.setCity_list(cityList);
						provinceList.add(provinceModel);

					} else if ("city".equals(typeName)) {
						cityModel.setCounty_list(countyList);
						cityList.add(cityModel);

					} else if ("area".equals(typeName)) {
						countyList.add(countyModel);
					}

				} else if (type == XmlPullParser.TEXT) {

				}

				type = parser.next();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (mIsPostionListShow) {
				hide();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 保存用户信息
	 */
	private void save() {
		mDataUser.setUserPosition(mBelongTv.getText().toString());
	}

}
