package com.zhlt.g1app.activity;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataUser;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
 
/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd
 * All rights reserved.
 *
 * File: ActSign.java
 * Description:个性签名
 * 
 * Title: ActSign
 * Administrator 
 * ------------------------------- Revision History: ----------------------------
 * <author>                        <data>       <version>   <desc>
 * ------------------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn        2015年6月23日       下午4:08:21     1.0         Create this moudle
 */
public class ActSign extends Activity {

	/**  标题  */
	private TextView mTextView;
	
	/**  返回按钮  */
	private View mBackView;
	
	/**  用户数据  */
	private DataUser mDataUser;
	
	/**  编辑框  */
	private EditText mSignEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_sign);
		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData(){
		mDataUser = UserInfoUtil.getUserInfoUtil().getUserData();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		mSignEdit =(EditText)findViewById(R.id.edit_sign);
		mTextView = (TextView) findViewById(R.id.r_tv_title_text);
		mTextView.setText(R.string.sign);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				saveSign();
				finish();
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			 saveSign();
		}
		return super.onKeyDown(keyCode, event);
	}
	

	/**
	 * 保存个性签名
	 */
	private void saveSign() {
		mDataUser.setUserSign(mSignEdit.getText().toString().trim());
	}
}