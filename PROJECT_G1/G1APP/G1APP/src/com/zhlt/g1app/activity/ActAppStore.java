package com.zhlt.g1app.activity;

import org.apache.log4j.Logger;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpPhotoGridView;
import com.zhlt.g1app.basefunc.Log4jUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
*
* Copyright (C), 2015, GoBaby Mobile Corp., Ltd
* All rights reserved.
*
* File: ActAppStore.java
* Description:储存空间
* 
* Title: ActAppStore
* Administrator 
* ------------------------------- Revision History: ----------------------------
* <author>                        <data>       <version>   <desc>
* ------------------------------------------------------------------------------
* zhengzhaowei@gobabymobile.cn        2015年6月23日       下午3:24:41     1.0         Create this moudle
*/
public class ActAppStore extends Activity {

	/**  标题  */
	private TextView mTitleTv;
	
	/**  返回按钮  */
	private View mStoreBackView;

	/**  照片显示动画  */
	private TranslateAnimation mPhotoShowAnimation;
	
	/**  存储空间显示动画  */
	private TranslateAnimation mSpaceShowAnimation;

	/**  存储空间隐藏动画  */
	private TranslateAnimation mSpaceHideAnimation;
	
	/**  照片隐藏动画  */
	private TranslateAnimation mPhotoHideAnimation;

	/**  存储空间界面  */
	private View mSpaceView;
	
	/**  存储空间照片控件  */
	private View mSpacePhotoView;
	
	/**  存储空间视频控件  */
	private View mSpaceFilmView;
	
	/**  存储空间返回按钮  */
	private Button mSpaceBackBtn;
	
	/**  照片界面是否已经显示了  */
	private boolean mIsPhotoShow = false;
	
	/**  存储空间是否已经显示了  */
	private boolean mIsSpaceShow = false;
	
	/**  查看存储空间按钮  */
	private Button mLookSpaceBtn;

	/**  照片界面  */
	private View mPhotoView;
	
	/**  照片中的GridView控件  */
	private GridView mPhotoGv;
	
	/**  照片按钮  */
	private ImageButton mPhotoIBtn;
	
	/**  照片删除按钮  */
	private ImageButton mPhotoDeleteIBtn;
	
	/**  照片返回按钮  */
	private Button mPhotoBackBtn;

	/**  照片GridView适配器  */
	private AdpPhotoGridView mPhotoGridViewAdapter;
	
	/**   屏幕长度  */
	private int mScreenWidth;

	/**   下拉选择按钮  */
	private Button mDropBtn;

	/**   打印工具类  */
	private Logger mLogger = Log4jUtil.getLogger("");

	/**  容量弹出框  */
	private PopupWindow mPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_appstore);
		initData();
		initView();
	}

	/**
	 * 功能:初始化数据
	 */
	private void initData() {

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		mScreenWidth = displayMetrics.widthPixels;

	}
	
	/**
	 * 功能:初始化控件
	 */
	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.appstore);
		mStoreBackView = findViewById(R.id.r_ib_title_left);

		mPhotoView = findViewById(R.id.rlyt_photo);
		mSpaceView = findViewById(R.id.item_space);

		mStoreBackView.setVisibility(View.VISIBLE);
		mStoreBackView.setOnClickListener(mOnClickListener);
		mLookSpaceBtn = (Button) findViewById(R.id.btn_appstore_lookspace);
		mLookSpaceBtn.setOnClickListener(mOnClickListener);
		mDropBtn = (Button) findViewById(R.id.btn_appstore_ava);
		mDropBtn.setOnClickListener(mOnClickListener);

		initSpaceView();
		initPhotoView();
	}

	/**
	 * 功能：初始化储存空中的控件
	 */
	private void initSpaceView() {
		mSpacePhotoView = findViewById(R.id.llyt_space_photo);
		mSpaceFilmView = findViewById(R.id.llyt_space_film);
		mSpaceBackBtn = (Button) findViewById(R.id.btn_space_back);
		mSpacePhotoView.setOnClickListener(mOnClickListener);
		mSpaceFilmView.setOnClickListener(mOnClickListener);
		mSpaceBackBtn.setOnClickListener(mOnClickListener);
	}

	/**
	 * 功能：初始化照片界面中的控件
	 */
	private void initPhotoView() {
		mPhotoGv = (GridView) findViewById(R.id.gv_photo);
		mPhotoGridViewAdapter = new AdpPhotoGridView(this);
		mPhotoBackBtn = (Button) findViewById(R.id.btn_photo_back);
		mPhotoGv.setAdapter(mPhotoGridViewAdapter);
		mPhotoBackBtn.setOnClickListener(mOnClickListener);
	}

	/**
	 * 功能：虽然图片界面已经消失，但是触摸还是在图片界面，所以强制隐藏所有的控件
	 */
	private void hidePhotoView() {
		mPhotoView.setVisibility(View.GONE);
		mPhotoGv.setVisibility(View.GONE);
		mPhotoBackBtn.setVisibility(View.GONE);
	}

	/**
	 * 功能：显示图片界面
	 */
	private void showPhotoView() {
		mPhotoView.setVisibility(View.VISIBLE);
		mPhotoGv.setVisibility(View.VISIBLE);
		mPhotoBackBtn.setVisibility(View.VISIBLE);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;
			case R.id.btn_appstore_lookspace:
				if (!mIsSpaceShow) {
					hidePopup();
					showSpace();
				} else {
					hideSpace();
				}
				break;
			case R.id.llyt_space_photo:
				mLogger.info("photo");
				if (!mIsPhotoShow) {
					showPhoto();
				} else {
					hidePhoto();
				}
				break;
			case R.id.btn_photo_back:
				mLogger.info("btn_photo_back");
				hidePhoto();
				break;
			case R.id.btn_space_back:
				mLogger.info("btn_space_back");
				hideSpace();
				break;
			case R.id.btn_appstore_ava:
				showPopup();
				break;
			}
		}
	};

	/**
	 * 显示下拉框
	 */
	@SuppressLint("InflateParams")
	private void showPopup() {
		if (mPopupWindow == null) {
			View contentView = getLayoutInflater().inflate(
					R.layout.item_popup_space, null);
			mPopupWindow = new PopupWindow(contentView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable(
					getResources()));
			mPopupWindow.setAnimationStyle(R.style.popup_anim);
		}
		mPopupWindow.showAsDropDown(mDropBtn);
	}

	private void hidePopup() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * 显示存储空间界面
	 */
	private void showSpace() {
		mIsSpaceShow = true;
		showSpaceView();
		if (mSpaceShowAnimation == null) {
			mSpaceShowAnimation = new TranslateAnimation(mScreenWidth, 0, 0, 0);
			mSpaceShowAnimation.setDuration(500);
			mSpaceShowAnimation.setFillAfter(true);
		}
		mSpaceView.startAnimation(mSpaceShowAnimation);
	}

	/**
	 * 显示存储空间界面
	 */
	private void showSpaceView() {
		mSpaceView.setVisibility(View.VISIBLE);
		mSpaceBackBtn.setVisibility(View.VISIBLE);
		mSpaceFilmView.setVisibility(View.VISIBLE);
		mSpacePhotoView.setVisibility(View.VISIBLE);
	}

	private void hideSpaceView() {
		mSpaceView.setVisibility(View.GONE);
		mSpaceBackBtn.setVisibility(View.GONE);
		mSpaceFilmView.setVisibility(View.GONE);
		mSpacePhotoView.setVisibility(View.GONE);
	}

	/**
	 * 隐藏存储空间界面
	 */
	private void hideSpace() {
		mLogger.info("hideSpace");
		mIsSpaceShow = false;
		if (mSpaceHideAnimation == null) {
			mSpaceHideAnimation = new TranslateAnimation(0, mScreenWidth, 0, 0);
			mSpaceHideAnimation.setDuration(500);
			mSpaceHideAnimation.setFillAfter(true);
		}
		mSpaceHideAnimation.setAnimationListener(mSpaceHideAnimationListener);
		mSpaceView.startAnimation(mSpaceHideAnimation);
	}

	/**
	 * 显示存储空间界面
	 */
	private void showPhoto() {
		mLogger.info("showPhoto");
		mIsPhotoShow = true;
		showPhotoView();
		if (mPhotoShowAnimation == null) {
			mPhotoShowAnimation = new TranslateAnimation(mScreenWidth, 0, 0, 0);
			mPhotoShowAnimation.setDuration(500);
			mPhotoShowAnimation.setFillAfter(true);
		}
		mPhotoView.startAnimation(mPhotoShowAnimation);
	}

	/**
	 * 功能：按下返回键时，如果照片界面或者存储空间界面还在的时候，不让back退出
	 * 参数：int keyCode, KeyEvent event
	 * 返回值：super.onKeyDown(keyCode, event);
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		mLogger.info(mIsSpaceShow + "    " + mIsPhotoShow);
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (mIsPhotoShow) {
				hidePhoto();
				return true;
			} else if (mIsSpaceShow) {
				hideSpace();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 功能：隐藏照片界面
	 */
	private void hidePhoto() {
		mLogger.info("hidePhoto");
		mIsPhotoShow = false;
		if (mPhotoHideAnimation == null) {
			mPhotoHideAnimation = new TranslateAnimation(0, mScreenWidth, 0, 0);
			mPhotoHideAnimation.setDuration(500);
			mPhotoHideAnimation.setFillAfter(true);
			mPhotoHideAnimation
					.setAnimationListener(mPhotoHideAnimationListener);
		}
		mPhotoView.startAnimation(mPhotoHideAnimation);
	}

	/**  储存空间界面隐藏动画  */
	private AnimationListener mSpaceHideAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			hideSpaceView();
		}
	};
	
	/**  照片界面隐藏动画  */
	private AnimationListener mPhotoHideAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			hidePhotoView();
		}
	};

}
