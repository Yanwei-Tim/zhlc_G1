package com.zhlt.g1app.activity;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.BaseUtil;
import com.zhlt.g1app.basefunc.UpdateManager;
import com.zhlt.g1app.fragment.FrgActMain;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: ActAboutUs.java Description:关于我们
 * 
 * Title: ActAboutUs Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月23日 下午3:24:41 1.0 Create this moudle
 */
public class ActAboutUs extends Activity {

	/** 标题 */
	private TextView mTitleTv;

	/** 返回按钮 */
	private View mAboutBack;

	/** 版本信息按钮 */
	private View mVersionInfoView;

	private View mFastView;

	private View mFeedbackView;

	private View mIntruduceView;

	private View mOperationView;

	private View mProblemView;

	private View mServiceView;

	private View mUpdateView;

	private TextView mVersionTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_aboutus);
		initView();
	}

	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.about);

		mVersionTv = (TextView) findViewById(R.id.tv_aboutus_version);
		mVersionTv.setText(BaseUtil.getVersion(getApplicationContext()));
		mAboutBack = findViewById(R.id.r_ib_title_left);
		mAboutBack.setVisibility(View.VISIBLE);
		mAboutBack.setOnClickListener(mOnClickListener);

		mVersionInfoView = findViewById(R.id.llyt_aboutus_versioninfo);
		mVersionInfoView.setOnClickListener(mOnClickListener);
		mFastView = findViewById(R.id.llyt_aboutus_fast);
		mFastView.setOnClickListener(mOnClickListener);
		mFeedbackView = findViewById(R.id.llyt_aboutus_feedback);
		mFeedbackView.setOnClickListener(mOnClickListener);
		mIntruduceView = findViewById(R.id.llyt_aboutus_intruduce);
		mIntruduceView.setOnClickListener(mOnClickListener);
		mServiceView = findViewById(R.id.llyt_aboutus_service);
		mServiceView.setOnClickListener(mOnClickListener);
		mUpdateView = findViewById(R.id.llyt_aboutus_update);
		mUpdateView.setOnClickListener(mOnClickListener);

	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;
			case R.id.llyt_aboutus_service:
				startIntent(ActService.class);
				break;

			case R.id.llyt_aboutus_fast:
				startIntent(ActFastRoom.class);
				break;

			case R.id.llyt_aboutus_intruduce:
				startIntent(ActFunction.class);
				break;

			case R.id.llyt_aboutus_feedback:
				startIntent(ActFeedback.class);
				break;
			case R.id.llyt_aboutus_update:
				isupdata();
				break;

			default:
				break;
			}
		}
	};

	private void isupdata() {
		// TODO Auto-generated method stub
		UpdateManager um = new UpdateManager(this);
		um.checkUpdate();
	}

	private void startIntent(Class class1) {
		Intent mIntent = new Intent(this, class1);
		startActivity(mIntent);
	}
}
