package com.zhlt.g1app.activity;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.UserInfoUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: ActGender.java Description:性别修改
 * 
 * Title: ActGender Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月23日 下午3:24:41 1.0 Create this moudle
 */
public class ActGender extends Activity {

	/** 标题 */
	private TextView mTitleTv;

	/** 返回 */
	private View mBackView;

	/** 男 */
	private View mManView;

	/** 男三角 */
	private View mManSelectView;

	/** 女 */
	private View mWomanView;

	/** 女三角 */
	private View mWomanSelectView;

	/** 保密 */
	private View mSecretView;

	/** 保密三角 */
	private View mSecretSelectView;

	/** 第一次显示的三角 */
	private View mLastSelectView;

	/** 代表男 */
	private final int MAN = 1;
	
	/** 代表女 */
	private final int WOMAN = 2;
	
	/** 代表保密 */
	private final int SECRET = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_gender);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mManView = findViewById(R.id.rlyt_gender_man);
		mWomanView = findViewById(R.id.rlyt_gender_woman);
		mSecretView = findViewById(R.id.rlyt_gender_serect);
		mManSelectView = findViewById(R.id.ibtn_gender_man_select);
		mWomanSelectView = findViewById(R.id.ibtn_gender_woman_select);
		mSecretSelectView = findViewById(R.id.ibtn_gender_secret_select);
		mLastSelectView = mManSelectView;
		mManSelectView.setVisibility(View.VISIBLE);
		mTitleTv.setText(R.string.gender);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(mOnClickListener);
		mManView.setOnClickListener(mOnClickListener);
		mWomanView.setOnClickListener(mOnClickListener);
		mSecretView.setOnClickListener(mOnClickListener);
	}

	/**
	 * 设置当前的性别
	 */
	private void setCurrentSex(int i) {
		if (i == 1) {
			mLastSelectView.setVisibility(View.GONE);
			mManSelectView.setVisibility(View.VISIBLE);
			mLastSelectView = mManSelectView;
			UserInfoUtil.getUserInfoUtil().getUserData().setUserGender("男");
		} else if (i == 2) {
			mLastSelectView.setVisibility(View.GONE);
			mWomanSelectView.setVisibility(View.VISIBLE);
			mLastSelectView = mWomanSelectView;
			UserInfoUtil.getUserInfoUtil().getUserData().setUserGender("女");
		} else {
			mLastSelectView.setVisibility(View.GONE);
			mSecretSelectView.setVisibility(View.VISIBLE);
			mLastSelectView = mSecretSelectView;
			UserInfoUtil.getUserInfoUtil().getUserData().setUserGender("保密");
		}
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;
			case R.id.rlyt_gender_man:
				setCurrentSex(MAN);
				break;
			case R.id.rlyt_gender_woman:
				setCurrentSex(WOMAN);
				break;
			case R.id.rlyt_gender_serect:
				setCurrentSex(SECRET);
				break;
			}
		}
	};

}
