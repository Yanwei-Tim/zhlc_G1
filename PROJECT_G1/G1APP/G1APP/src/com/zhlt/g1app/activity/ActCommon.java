package com.zhlt.g1app.activity;

import com.zhlt.g1app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: ActCommon.java Description:描述
 * 
 * Title: ActCommon Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月27日 下午3:40:17 1.0 Create this moudle
 */
public class ActCommon extends Activity {

	/** 标题 */
	private TextView mTitleTv;

	/** 返回按钮 */
	private View mAboutBack;

	/** 接受紧急求助通知 */
	private View mHelpView;

	private SeekBar mHelpSb;

	/** 接受分享轨迹通知 */
	private View mShareHistoryView;

	private SeekBar mShareHistorySb;

	/** 接受新消息通知 */
	private View mNewsView;

	private SeekBar mNewsSb;

	/** 声音 */
	private View mVoiceView;

	private SeekBar mVoiceSb;

	/** 震动 */
	private View mShockView;

	private SeekBar mShockSb;

	/** 消息字体大小 */
	private View mFontSizeView;

	/** 长度单位 */
	private View mUnitView;

	/** 大中小 */
	private RadioGroup mFontSizeRg;

	/** 公制 英制 */
	private RadioGroup mUnitRg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_common);
		initView();
	}

	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.common);
		mAboutBack = findViewById(R.id.r_ib_title_left);
		mAboutBack.setVisibility(View.VISIBLE);
		mAboutBack.setOnClickListener(mOnClickListener);

		mHelpView = findViewById(R.id.llyt_common_receiveurgentcall);
		mHelpView.setOnClickListener(mOnClickListener);
		mShareHistoryView = findViewById(R.id.llyt_common_receivehistoryshare);
		mShareHistoryView.setOnClickListener(mOnClickListener);
		mNewsView = findViewById(R.id.llyt_common_receivenews);
		mNewsView.setOnClickListener(mOnClickListener);
		mVoiceView = findViewById(R.id.llyt_common_voice);
		mVoiceView.setOnClickListener(mOnClickListener);
		mShockView = findViewById(R.id.llyt_common_shock);
		mShockView.setOnClickListener(mOnClickListener);
		mFontSizeView = findViewById(R.id.llyt_common_messagefontsize);
		mFontSizeView.setOnClickListener(mOnClickListener);
		mUnitView = findViewById(R.id.llyt_common_unit);
		mUnitView.setOnClickListener(mOnClickListener);
		mFontSizeRg = (RadioGroup) findViewById(R.id.r_rg_common_fontsize);
		mUnitRg = (RadioGroup) findViewById(R.id.r_rg_common_unit);

		mHelpSb = (SeekBar) findViewById(R.id.sb_common_receiveurgentcall);
		mShareHistorySb = (SeekBar) findViewById(R.id.sb_common_receivehistoryshare);
		mNewsSb = (SeekBar) findViewById(R.id.sb_common_receivenews);
		mVoiceSb = (SeekBar) findViewById(R.id.sb_common_voice);
		mShockSb = (SeekBar) findViewById(R.id.sb_common_shock);

		mShockSb.setOnSeekBarChangeListener(mSeekBarChangeListener);
		mHelpSb.setOnSeekBarChangeListener(mSeekBarChangeListener);
		mShareHistorySb.setOnSeekBarChangeListener(mSeekBarChangeListener);
		mNewsSb.setOnSeekBarChangeListener(mSeekBarChangeListener);
		mVoiceSb.setOnSeekBarChangeListener(mSeekBarChangeListener);

		mFontSizeRg.setOnCheckedChangeListener(mOnCheckedChangeListener);
		mUnitRg.setOnCheckedChangeListener(mOnCheckedChangeListener);

	}

	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {

			}

		}
	};

	private OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener() {

		private int startProgress;

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (seekBar.getProgress() > startProgress) {
				seekBar.setProgress(seekBar.getMax());
			} else {
				seekBar.setProgress(0);
			}

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			startProgress = seekBar.getProgress();

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * 点击开关
	 */
	private void onClickSeekBar(SeekBar mSeekBar) {
		if (mSeekBar.getProgress() == 0) {
			mSeekBar.setProgress(mSeekBar.getMax());
		} else {
			mSeekBar.setProgress(0);
		}
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;
			case R.id.llyt_common_receiveurgentcall:
				onClickSeekBar(mHelpSb);
				break;
			case R.id.llyt_common_receivehistoryshare:
				onClickSeekBar(mShareHistorySb);
				break;
			case R.id.llyt_common_receivenews:
				onClickSeekBar(mNewsSb);
				break;
			case R.id.llyt_common_voice:
				onClickSeekBar(mVoiceSb);
				break;
			case R.id.llyt_common_shock:
				onClickSeekBar(mShockSb);
				break;

			}
		}
	};
}
