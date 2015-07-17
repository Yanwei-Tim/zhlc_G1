package com.zhlt.g1app.activity;

import java.lang.ref.WeakReference;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhlt.g1app.R;
import com.zhlt.g1app.application.AppBmap;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.data.DataCommon;
import com.zhlt.g1app.fragment.FrgActMain;
import com.zhlt.g1app.func.NetUtils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: ActPassWord.java Description: 填写密码
 * 
 * Title: ActPassWord Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月23日 下午4:07:33 1.0 Create this moudle
 */
public class ActPassWord extends Activity implements OnClickListener {

	/** 确定按阿牛 */
	private Button mLoginBtn;

	/** 标题 */
	private TextView mTitleTv;

	/** 跳过 */
	private TextView mPassTv;

	private EditText mPasswordEdit;

	private EditText mConfirPasswordEdit;

	private View mBackView;

	private HttpUtils mHttpUtils;
	
	private String mPhone;

	private PasswordHandler mHandle;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.act_password);
		initData();
		initView();
	};

	private void initData() {
		mHttpUtils = new HttpUtils();
		mPhone = getIntent().getStringExtra("phone");
		mHandle = new PasswordHandler(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mLoginBtn = (Button) findViewById(R.id.btn_password_sure);
		mLoginBtn.setOnClickListener(this);
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.password);
		mPassTv = (TextView) findViewById(R.id.tv_password_pass);
		mPassTv.setOnClickListener(this);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(this);
		mPasswordEdit = (EditText) findViewById(R.id.edit_smss_setpassword);
		mConfirPasswordEdit = (EditText) findViewById(R.id.edit_smss_confirepassword);
		mConfirPasswordEdit.addTextChangedListener(mPasswordEditTextWatcher);
	}
	private TextWatcher mPasswordEditTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			mConfirPasswordEdit.setBackgroundResource(R.drawable.act_login_verifi);
			mConfirPasswordEdit.setHintTextColor(getResources().getColor(
					R.color.bb_gray2));
		}
	};
	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_password_sure) {
			AppBmap.getInstance().exit();
			onLoginCorrect();
		} else if (v.getId() == R.id.tv_password_pass) {
			enterMain();
		} else if (v.getId() == R.id.r_ib_title_left) {
			finish();
		}

	}

	/**
	 * 提示验证码错误或没有输入
	 */
	private void pleaseInputConver() {
		mConfirPasswordEdit.setText("");
		mConfirPasswordEdit.setBackgroundResource(R.drawable.common_input_wrong);
		mConfirPasswordEdit.setHintTextColor(Color.WHITE);
		ObjectAnimator.ofFloat(mConfirPasswordEdit, "alpha", 1, 0, 1, 0, 1)
				.setDuration(1000).start();
	}
	
	/**
	 * 功能：登入成功
	 */
	public void onLoginCorrect() {

		String mPassword = mPasswordEdit.getText().toString().trim();
		String mConfirePassword = mConfirPasswordEdit.getText().toString()
				.trim();
		if (TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mConfirePassword)
				|| !mConfirePassword.equals(mPassword) || TextUtils.isEmpty(mPhone)) {
			pleaseInputConver();
			return;
		}
		String userName ="\"user_name\":\"";
		String userPass ="\"user_pass\":\"";
		String from ="\"car_type\":\"1\",";
		String type ="\"type\":\"1\"";
			
		String url = DataCommon.SEND_REGISTER_ROOT_PATH + "data={"+userName+mPhone+"\"," +userPass
			+mPassword+"\"," 	+from  + type +"}";
		 try {
			NetUtils.HttpRequestPost(url, mHandle);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	private class PasswordHandler extends Handler {

		private WeakReference<ActPassWord> reference;
		private Logger mLogger = Log4jUtil.getLogger("");

		public PasswordHandler(ActPassWord register) {
			reference = new WeakReference<ActPassWord>(register);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ActPassWord passwrod = reference.get();
			if (passwrod == null) {
				return;
			}
			if (msg.what == DataCommon.Message.MSG_LOAD_FINISHED) {
				mLogger.info("MSG_LOAD_FINISHED" + msg.obj);
				passwrod.parseConverJson(msg.obj.toString());
			}

		}

	}
	
	/**
	 * 解析验证码json
	 */
	private void parseConverJson(String json){
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (jsonObject.getString("result").equals("1")) {
				Intent mIntent = new Intent(ActPassWord.this, ActCapture.class);
				startActivity(mIntent);
				finish();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
 
		/**
	 * 进入主界面
	 */
	public void enterMain() {
		/** 该项引入地图界面功能界面项目修改改成进入二维码扫描界面 **/
		Intent mIntent = new Intent(this, FrgActMain.class);
		// Intent mIntent = new Intent(this, FrgActMain.class);
		startActivity(mIntent);
		finish();
	}

}
