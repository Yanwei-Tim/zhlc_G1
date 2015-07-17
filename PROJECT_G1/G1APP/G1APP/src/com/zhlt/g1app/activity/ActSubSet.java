package com.zhlt.g1app.activity;

import org.apache.log4j.Logger;

import com.zhlt.g1app.R;
import com.zhlt.g1app.application.AppBmap;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.fragment.ActWifiManger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: ActSubSet.java Description:设置
 * 
 * Title: ActSubSet Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月23日 下午3:24:41 1.0 Create this moudle
 */
public class ActSubSet extends Activity {
	private Logger log4jUtil = new Log4jUtil().getLogger("");

	/** 标题 */
	private TextView mTitleTv;

	/** 返回按钮 */
	/**
	 * 
	 */
	private View mBackView;

	/** 摄像头 */
	private View mConnectView;

	/** APP存储管理 */
	private View mAppStoreView;

	/** 离线地图 */
	private View mOfflineMapView;

	/** 语言 */
	private View mLanguageView;

	/** 自动下载拍照文件 */
	private View mAutoDownloadView;

	/** 注销按钮 */
	private View mLoginOutView;

	/** 注销对话框 */
	private View mLoginOutDialogView;

	/** 取消注销 */
	private TextView mLoginoutCancleTv;

	/** 确定注销 */
	private TextView mLoginoutSureTv;

	/** 模拟对话框 */
	private TextView mDialogTv;

	private View mDialogOutSideBg;

	/** 自动下载拍照文件开关 */
	private SeekBar mAutoDownCSb;

	/** 对话框是否正在显示 */
	private boolean mIsDialogShow = false;

	private View mWifiView;

	private View mCommonView;

	private Dialog mDialog;

	private View mChangePasswordView;

	private View mInputPasswordCancleView;

	private View mInputPasswordSureView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_subset);
		AppBmap.getInstance().addActivity(this);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.set);
		mCommonView = findViewById(R.id.llyt_subset_common);
		mChangePasswordView = findViewById(R.id.llyt_subset_password);
		mWifiView = findViewById(R.id.llyt_subset_wifi);
		mDialogTv = (TextView) findViewById(R.id.tv_subset_dialog);
		mDialogOutSideBg = findViewById(R.id.iv_subset_dialog_outside);
		mLoginOutView = findViewById(R.id.llyt_subset_loginout);
		mLoginOutDialogView = findViewById(R.id.rlyt_subset_loginout);
		mLoginoutCancleTv = (TextView) findViewById(R.id.tv_subset_loginout_cancle);
		mLoginoutSureTv = (TextView) findViewById(R.id.tv_subset_loginout_sure);
		mConnectView = findViewById(R.id.llyt_subset_camera);
		mAppStoreView = findViewById(R.id.llyt_subset_appstore);
		mOfflineMapView = findViewById(R.id.llyt_subset_offlinemap);
		mLanguageView = findViewById(R.id.llyt_subset_language);
		// mAutoDownloadView = findViewById(R.id.llyt_subset_auto);
		mBackView = findViewById(R.id.r_ib_title_left);
		// mAutoDownCSb = (SeekBar) findViewById(R.id.sb_subset);
		// mAutoDownCSb.setOnSeekBarChangeListener(mSeekBarChangeListener);
		// mAutoDownCSb.setMax(2);
		mBackView.setVisibility(View.VISIBLE);
		mLoginOutView.setOnClickListener(mOnClickListener);
		mConnectView.setOnClickListener(mOnClickListener);
		mAppStoreView.setOnClickListener(mOnClickListener);
		mChangePasswordView.setOnClickListener(mOnClickListener);
		mCommonView.setOnClickListener(mOnClickListener);
		mOfflineMapView.setOnClickListener(mOnClickListener);
		mLanguageView.setOnClickListener(mOnClickListener);
		// mAutoDownloadView.setOnClickListener(mOnClickListener);
		mDialogOutSideBg.setOnClickListener(mOnClickListener);
		mBackView.setOnClickListener(mOnClickListener);
		mWifiView.setOnClickListener(mOnClickListener);
		mLoginoutCancleTv.setOnClickListener(mOnClickListener);
		mLoginoutSureTv.setOnClickListener(mOnClickListener);

	}

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

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;

			case R.id.llyt_subset_common:
				goCommon();
				break;

			case R.id.tv_subset_password_cancle:
				mDialog.dismiss();
				break;

			case R.id.tv_subset_password_sure:
				onPasswordSure();
				break;

			case R.id.llyt_subset_password:
				showPasswordDialog();
				break;

			case R.id.llyt_subset_camera:
				if (!mIsDialogShow) {
					showDialog();
				} else {
					hideDialog();
				}
				break;
			case R.id.llyt_subset_appstore:
				goAppStore();
				break;
			// case R.id.llyt_subset_auto:
			// onClickAutoDownload();
			// break;
			case R.id.llyt_subset_offlinemap:

				break;
			case R.id.llyt_subset_language:
				goLanguage();
				break;
			case R.id.iv_subset_dialog_outside:
				hideDialog();
				break;
			case R.id.llyt_subset_loginout:
				showLoginOutDialog();
				break;
			case R.id.tv_subset_loginout_sure:
				hideDialog();
				loginOut();
				break;
			case R.id.tv_subset_loginout_cancle:
				hideDialog();
				break;
			case R.id.llyt_subset_wifi:
				goWifi();
				break;
			}
		}

	};

	private EditText mInputPasswordEdit;

	private InputMethodManager imm;

	/**
	 * 验证原密码，确认后调转到修改密码界面
	 */
	private void onPasswordSure() {
		// 输入原密码正确
		if (UserInfoUtil.getUserInfoUtil().getUserData().getUserPassword()
				.equals(mInputPasswordEdit.getText().toString().trim())) {
			mDialog.dismiss();
			goNewPassword();
		}

	}

	protected void goLanguage() {
		Intent intent = new Intent(this, ActLanguage.class);
		startActivity(intent);
	}

	private void goNewPassword() {
		Intent intent = new Intent(this, ActNewPassword.class);
		startActivity(intent);
	}

	private void showPasswordDialog() {

		if (mDialog == null) {
			mDialog = new Dialog(this, R.style.dialog_no_title);
			View view = getLayoutInflater().inflate(
					R.layout.item_dialog_password, null);
			mInputPasswordEdit = (EditText) view
					.findViewById(R.id.edit_subset_password);
			mDialog.setContentView(view);
			mInputPasswordCancleView = view
					.findViewById(R.id.tv_subset_password_cancle);
			mInputPasswordSureView = view
					.findViewById(R.id.tv_subset_password_sure);
			mInputPasswordCancleView.setOnClickListener(mOnClickListener);
			mInputPasswordSureView.setOnClickListener(mOnClickListener);
		}
		mDialog.show();
		showInput();
	}

	/**
	 * 显示系统键盘
	 */
	private void showInput() {
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 进入通用
	 */
	private void goCommon() {
		Intent intent = new Intent(this, ActCommon.class);
		startActivity(intent);
	}

	/**
	 * 进入Wifi
	 */
	private void goWifi() {
		log4jUtil.info("===goWifi()of Success===");
		Toast.makeText(this, "===IntentWifiManger===", Toast.LENGTH_LONG)
				.show();
		Intent intent = new Intent(ActSubSet.this, ActWifiManger.class);
		startActivityForResult(intent, 0);

		// this.finish();
	}

	/**
	 * 注销成功
	 */
	private void loginOut() {
		Intent intent = new Intent(this, ActLogin.class);
		startActivity(intent);
		AppBmap.getInstance().exit();
	}

	/**
	 * 显示注销对话框
	 */
	private void showLoginOutDialog() {
		mIsDialogShow = true;
		mLoginOutDialogView.setVisibility(View.VISIBLE);
		mDialogOutSideBg.setVisibility(View.VISIBLE);
	}

	/**
	 * 点击自动下载开关
	 */
	private void onClickAutoDownload() {
		if (mAutoDownCSb.getProgress() == 0) {
			mAutoDownCSb.setProgress(mAutoDownCSb.getMax());
		} else {
			mAutoDownCSb.setProgress(0);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (mIsDialogShow) {
				hideDialog();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 跳转到APP存储管理
	 */
	private void goAppStore() {
		Intent intent = new Intent(this, ActAppStore.class);
		startActivity(intent);
	}

	/**
	 * 显示模拟对话框
	 */
	private void showDialog() {
		mDialogTv.setVisibility(View.VISIBLE);
		mDialogOutSideBg.setVisibility(View.VISIBLE);
		mIsDialogShow = true;
	}

	/**
	 * 隐藏模拟对话框
	 */
	private void hideDialog() {
		mIsDialogShow = false;
		mDialogTv.setVisibility(View.GONE);
		mDialogOutSideBg.setVisibility(View.GONE);
		mLoginOutDialogView.setVisibility(View.GONE);
	}
}
