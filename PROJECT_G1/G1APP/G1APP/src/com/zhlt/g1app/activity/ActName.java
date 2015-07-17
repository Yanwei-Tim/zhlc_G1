package com.zhlt.g1app.activity;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataUser;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd
 * All rights reserved.
 *
 * File: ActName.java
 * Description:修改用户姓名
 * 
 * Title: ActName
 * Administrator 
 * ------------------------------- Revision History: ----------------------------
 * <author>                        <data>       <version>   <desc>
 * ------------------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn        2015年6月23日       下午4:07:11     1.0         Create this moudle
 */
public class ActName extends Activity {

	/** 标题 */
	private TextView mTitleTv;

	/** 取消按钮 */
	private TextView mCancleTv;

	/** 保存按钮 */
	private TextView mSaveTv;

	/** 清除编辑框的姓名 */
	private ImageButton mClearNameIBtn;

	/** 编辑框 */
	private EditText mNameEdit;

	/** 用户信息类 */
	private DataUser mDataUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_name);
		initData();
		initView();
	}

	/**
	 * 获取用户信息
	 */
	private void initData() {
		mDataUser = UserInfoUtil.getUserInfoUtil().getUserData();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.name);
		mCancleTv = (TextView) findViewById(R.id.tv_name_cancle);
		mSaveTv = (TextView) findViewById(R.id.tv_name_save);
		mClearNameIBtn = (ImageButton) findViewById(R.id.ibtn_name_clear);
		mNameEdit = (EditText) findViewById(R.id.edit_name);
		String name = mDataUser.getUserName();
		if (!TextUtils.isEmpty(name)) {
			mNameEdit.setText(name);
			mNameEdit.setSelection(name.length());
		}
		mSaveTv.setOnClickListener(mOnClickListener);
		mCancleTv.setOnClickListener(mOnClickListener);
		mClearNameIBtn.setOnClickListener(mOnClickListener);
		mClearNameIBtn.setOnLongClickListener(mOnLongClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_name_cancle:
				finish();
				break;
			case R.id.tv_name_save:
				saveName();
				finish();
				break;
			case R.id.ibtn_name_clear:
				onEditBack();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 回删一个字
	 */
	private void onEditBack() {

		String mEditString = mNameEdit.getText().toString().trim();
		if (!TextUtils.isEmpty(mEditString)) {
			if (mEditString.length() == 1) {
				mNameEdit.setText("");
			} else {
				mNameEdit.setText(mEditString.substring(0,
						mEditString.length() - 1));
			}
		}
		mNameEdit.setSelection(mNameEdit.getText().toString().length());
	}

	private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			mNameEdit.setText("");
			return false;
		}
	};

	/**
	 * 保存修改的名字
	 */
	private void saveName() {
		mDataUser.setUserName(mNameEdit.getText().toString().trim());
	}

}
