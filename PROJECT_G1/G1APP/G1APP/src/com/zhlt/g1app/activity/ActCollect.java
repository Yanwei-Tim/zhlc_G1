package com.zhlt.g1app.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpCollectListView;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.data.DataCommon;
import com.zhlt.g1app.data.DataShare;
import com.zhlt.g1app.view.ViewXList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: ActCollect.java Description:收藏
 * 
 * Title: ActCollect Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月23日 下午3:24:41 1.0 Create this moudle
 */
public class ActCollect extends Activity {

	/** 收藏标题 */
	private TextView mTitleTv;

	/** 返回按钮 */
	private View mBackView;

	/** 搜索按钮 */
	private Button mSearchBtn;

	/** 打印工具类 */
	private Logger mLog4jUtil = Log4jUtil.getLogger("");

	/** 编辑按钮 */
	private TextView mEditTv;

	/** 是否正在编辑 */
	private boolean mIsEdit = false;

	/** Handler对象 */
	private CollectHandler mHandler;

	/** 收藏列表控件 */
	private ViewXList mListView;

	/** 底部功能父视图 */
	private View mBottomView;

	/** 底部分享按钮 */
	private ImageButton mShareIBtn;

	/** 删除按键 */
	private ImageButton mDeleteIBtn;

	/** 底部视图显示动画 */
	private TranslateAnimation mShowAnimation;

	/** 底部视图隐藏动画 */
	private TranslateAnimation mHideAnimation;

	/** 收藏列表适配器 */
	private AdpCollectListView mListViewAdapter;

	private String mHeadPic = "http://image.haosou.com/v?q=%E5%B8%85%E6%B0%94%E7%94%B7%E7%94%9F%E5%A4%B4%E5%83%8F&src=relation_qqimage&fromurl=http%3A%2F%2Fwww.2cto.com%2Fqq%2F201301%2F181941.html#q=%E5%B8%85%E6%B0%94%E7%94%B7%E7%94%9F%E5%A4%B4%E5%83%8F&src=relation_qqimage&fromurl=http%3A%2F%2Fwww.2cto.com%2Fqq%2F201301%2F181941.html&lightboxindex=1&id=f9fd316213ac39dfb3af5a10e215eb0b&multiple=0&itemindex=0&dataindex=1";
	private String mContentPic = "http://image.haosou.com/zv?ch=go&t1=396&t2=491#ch=go&t1=396&t2=491&lightboxindex=0&groupid=4c56fda8f09f693f4bbd9e7621bff404&itemindex=0&dataindex=120";

	private String[] mPics = { "http://112.124.110.16:8080/apk/01.png",
			"http://112.124.110.16:8080/apk/02.png",
			"http://112.124.110.16:8080/apk/03.png",
			"http://112.124.110.16:8080/apk/04.png",
			"http://112.124.110.16:8080/apk/05.png",
			"http://112.124.110.16:8080/apk/06.png", };
	/** 收藏数据集合 */
	private List<DataShare> mListShareDatas = new ArrayList<DataShare>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_collect);
		initData();
		initView();
	}

	private void initData() {
		mHandler = new CollectHandler(this);
		getCollectData();
	}

	private static class CollectHandler extends Handler {

		private WeakReference<ActCollect> reference;

		public CollectHandler(ActCollect fragment) {
			reference = new WeakReference<ActCollect>(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			ActCollect collect = reference.get();
			if (collect == null) {
				return;
			}
			switch (msg.what) {
			case DataCommon.Message.MSG_COLLECT_lOADFINISHED:
				collect.refreshListView();
				break;

			default:
				break;
			}

		}

	}

	/**
	 * 功能：获取网络收藏数据 参数：无 返回值：无
	 */
	private void getCollectData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 3; i++) {
					List<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("name", "超级大坏蛋");
					map.put("comment", "哇，好给力啊！");
					ArrayList<String> mContentPic = new ArrayList<String>();
					if (i % 2 == 0) {
						mContentPic.add(mPics[0]);
						mContentPic.add(mPics[1]);
						mContentPic.add(mPics[2]);
					} else {
						mContentPic.add(mPics[3]);
						mContentPic.add(mPics[4]);
						mContentPic.add(mPics[5]);
					}
					DataShare shareData = new DataShare("超级大好人",
							"今天又遇到了碰瓷的，请大家注意这个人！！/喷怒！", mHeadPic, mContentPic,
							mList);
					mListShareDatas.add(shareData);
					mHandler.sendEmptyMessage(DataCommon.Message.MSG_COLLECT_lOADFINISHED);
				}

			}
		}).start();
	}

	/**
	 * 功能：刷新收藏列表
	 */
	private void refreshListView() {
		mListViewAdapter.setData(mListShareDatas);
	}

	/**
	 * 功能：初始化控件
	 */
	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mEditTv = (TextView) findViewById(R.id.tv_collect_edit);
		mBottomView = findViewById(R.id.rlyt_collect_bottom);
		mShareIBtn = (ImageButton) findViewById(R.id.ibtn_collect_share);
		mDeleteIBtn = (ImageButton) findViewById(R.id.ibtn_collect_delete);
		mTitleTv.setText(R.string.collect);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(mOnClickListener);
		mListView = (ViewXList) findViewById(R.id.xListView_collect);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mListViewAdapter = new AdpCollectListView(this);
		mListView.setAdapter(mListViewAdapter);
		mEditTv.setOnClickListener(mOnClickListener);
		mDeleteIBtn.setOnClickListener(mOnClickListener);
		mShareIBtn.setOnClickListener(mOnClickListener);
	}

	/**
	 * 功能：隐藏底部栏
	 */
	private void hide() {
		if (mHideAnimation == null) {
			mHideAnimation = new TranslateAnimation(0, 0, 0, 600);
			mHideAnimation.setDuration(500);
			mHideAnimation.setFillAfter(true);
			mHideAnimation.setAnimationListener(mHideAnimationListener);
		}
		mBottomView.startAnimation(mHideAnimation);
	}

	/**
	 * 功能：显示底部栏
	 */
	private void show() {
		mBottomView.setVisibility(View.VISIBLE);
		if (mShowAnimation == null) {
			mShowAnimation = new TranslateAnimation(0, 0, 500, 0);
			mShowAnimation.setDuration(500);
			mShowAnimation.setFillAfter(true);
		}
		mBottomView.startAnimation(mShowAnimation);
	}

	/** 底部栏隐藏动画 */
	private AnimationListener mHideAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mBottomView.setVisibility(View.GONE);
		}
	};

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;
			case R.id.tv_collect_edit:
				mIsEdit = !mIsEdit;
				if (mIsEdit) {
					show();
				} else {
					hide();
				}
				mListViewAdapter.setEdit(mIsEdit);
				break;
			case R.id.ibtn_collect_share:
				share();
				break;
			case R.id.ibtn_collect_delete:
				delete();
				break;

			}
		}
	};

	/**
	 * 功能：分享
	 */
	private void share() {

	}

	/**
	 * 功能：删除选中的收藏
	 */
	private void delete() {
		List<Integer> mSelectList = mListViewAdapter.getmSelectList();
		mLog4jUtil.info("delete" + mSelectList.size() + "   "
				+ mListShareDatas.size());
		if (mSelectList.size() > mListShareDatas.size()) {
			return;
		}
		for (int i = 0; i < mSelectList.size(); i++) {
			mListShareDatas.remove(mListShareDatas.get(mSelectList.get(i)));
		}
		mLog4jUtil.info("delete" + mListShareDatas.size());
		refreshListView();
	}

}
