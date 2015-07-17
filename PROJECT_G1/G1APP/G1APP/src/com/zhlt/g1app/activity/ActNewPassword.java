package com.zhlt.g1app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataUser;

public class ActNewPassword extends Activity {

	/** 标题 */
	private TextView mTitleTv;

	/** 取消按钮 */
	private View mCancleView;

	/** 完成按钮 */
	private View mFinishView;

	/** 账号 */
	private TextView mAccountTv;

	/** 用户信息 */
	private DataUser mDataUser;

	/** 密码编辑框 */
	private EditText mPasswordEdit;

	/** 确认密码编辑框 */
	private EditText mConverPasswordEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_password_new);
		initVariable();
		initView();
	}

	private void initVariable() {
		mDataUser = UserInfoUtil.getUserInfoUtil().getUserData();
	}

	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.changepassword);
		mCancleView = findViewById(R.id.tv_newpassword_cancle);
		mCancleView.setOnClickListener(mOnClickListener);
		mFinishView = findViewById(R.id.tv_newpassword_finish);
		mFinishView.setOnClickListener(mOnClickListener);
		mAccountTv = (TextView) findViewById(R.id.tv_newpassword_account_value);
		mAccountTv.setText(mDataUser.getUserPhone());

		mPasswordEdit = (EditText) findViewById(R.id.edit_newpassword_1);
		mConverPasswordEdit = (EditText) findViewById(R.id.edit_newpassword_conver);

	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_newpassword_cancle:
				finish();
				break;
			case R.id.tv_newpassword_finish:
				onClickFinish();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 点击完成
	 */
	private void onClickFinish() {
		String password = mPasswordEdit.getText().toString().trim();
		String converPassword = mConverPasswordEdit.getText().toString().trim();
		if (TextUtils.isEmpty(password) || TextUtils.isEmpty(converPassword)
				|| !password.equals(converPassword)) {
			return;
		}
		mDataUser.setUserPassword(password);
		finish();
	}
}
