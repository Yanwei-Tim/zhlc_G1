package com.zhlt.g1app.activity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhlt.g1app.R;
import com.zhlt.g1app.application.AppBmap;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.data.DataCommon;
import com.zhlt.g1app.fragment.FrgActMain;
import com.zhlt.g1app.func.NetUtils;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: ActRegister.java Description:注册
 * 
 * Title: ActRegister Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月24日 下午2:26:54 1.0 Create this moudle
 */
public class ActRegister extends Activity implements OnClickListener {

	/** 标题 */
	private TextView mTitleTv;

	/** 跳过 */
	private TextView mPassTv;

	/** 返回 */
	private View mBackView;

	/** 确认 */
	private Button mSureIBtn;

	private Button mSendconfir;

	private HttpUtils mHttpUtils;

	private EditText mPhoneEdit;

	private EditText mConverEdit;

	private Logger mLogger = Log4jUtil.getLogger("");

	private String mConver = "1";

	private String mPhone = "1";

	private RegistereHandler mHandle;

	private int count = 60;
	private final long OFFSET = 1000;

	private View mPhoneView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_register);
		initData();
		initView();
	}

	private static class RegistereHandler extends Handler {

		private WeakReference<ActRegister> reference;

		public RegistereHandler(ActRegister register) {
			reference = new WeakReference<ActRegister>(register);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ActRegister register = reference.get();
			if (register == null) {
				return;
			}
			if (msg.what == DataCommon.Message.MSG_LOAD_FINISHED) {
				register.parseConverJson(msg.obj.toString());
			} else if (msg.what == DataCommon.Message.MSG_CONVER_SENDING) {
				if (register.count <= 0) {
					register.mSendconfir
							.setBackgroundResource(R.drawable.act_login_send);
					register.mSendconfir.setEnabled(true);
					register.count = 60;
					register.mSendconfir.setText(register.getResources()
							.getString(R.string.sendverif));
				} else {
					register.mSendconfir.setText(register.count + " s");
					sendEmptyMessageDelayed(
							DataCommon.Message.MSG_CONVER_SENDING,
							register.OFFSET);
					register.count--;
				}
			}

		}

	}

	/**
	 * 解析验证码json
	 */
	private void parseConverJson(String json) {
		mLogger.info("zzw parseConverJson "+json);
		try {
			JSONObject jsonObject = new JSONObject(json);
			mLogger.info("zzw name "+jsonObject.names().length());
			 if (jsonObject.names().length() > 1) {
					mLogger.info("zzw result "+jsonObject.getString("result").equals("4"));
				// 发送成功
				if (jsonObject.getString("result").equals("1")) {
					Toast.makeText(getApplicationContext(), "成功", 0).show();
					mConver = jsonObject.getString("code");
				} else if (jsonObject.getString("result").equals("3")) {
					Toast.makeText(getApplicationContext(), "发送短信失败", 0).show();
				}else if (jsonObject.getString("result").equals("4")) {
					Toast.makeText(getApplicationContext(), "用户已存在", 0).show();
				}
			} else {
				mLogger.info("zzw:"+jsonObject.getString("result"));
				
				// 验证码输入正确
				if (jsonObject.getString("result").equals("1")) {
					goPassword();
				} else {
					pleaseInputConver();
				}
			}

		} catch (Exception e) {
			mLogger.info("zzw parseConverJson "+e.toString());
		}
	}

	private void initData() {
		mHttpUtils = new HttpUtils();
		mHandle = new RegistereHandler(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.registerg1);
		mPassTv = (TextView) findViewById(R.id.tv_register_pass);
		mPassTv.setOnClickListener(this);
		mSureIBtn = (Button) findViewById(R.id.btn_register_sure);
		mSureIBtn.setOnClickListener(this);
		mSendconfir = (Button) findViewById(R.id.btn_register_getverif);
		mSendconfir.setOnClickListener(this);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(this);
		mPhoneEdit = (EditText) findViewById(R.id.edit_register_phone);
		mPhoneEdit.addTextChangedListener(mPhoneEditTextWatcher);
		mPhoneView = findViewById(R.id.llyt_edit_register_phone);
		mConverEdit = (EditText) findViewById(R.id.edit_register_verif);
		mConverEdit.addTextChangedListener(mConverEditTextWatcher);
	}

	private TextWatcher mConverEditTextWatcher = new TextWatcher() {

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
			mConverEdit.setBackgroundResource(R.drawable.act_login_verifi);
			mConverEdit.setHintTextColor(getResources().getColor(
					R.color.bb_gray2));
		}
	};
	private TextWatcher mPhoneEditTextWatcher = new TextWatcher() {

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
			mPhoneView.setBackgroundResource(R.drawable.act_login_verifi);
			mPhoneEdit.setHintTextColor(getResources().getColor(
					R.color.bb_gray2));
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register_sure:
			onSure();
			break;
		case R.id.tv_register_pass:
			enterMain();
			break;
		case R.id.r_ib_title_left:
			finish();
			break;
		case R.id.btn_register_getverif:
			getConver();
			break;
		}

	}

	private void getConver() {
		if (TextUtils.isEmpty(mPhoneEdit.getText().toString().trim())) {
			// Toast.makeText(getApplicationContext(),
			// getResources().getString(R.string.please_input_phone), 0)
			// .show();
			pleaseInputPhone();
			return;
		}
		mSendconfir.setEnabled(false);
		mSendconfir.setBackgroundResource(R.drawable.act_login_getverif);
		mHandle.sendEmptyMessage(DataCommon.Message.MSG_CONVER_SENDING);
		mPhone = mPhoneEdit.getText().toString().trim();
		String url = DataCommon.SEND_CONFIRE_ROOT_PATH + mPhone + "\"}";
		try {
			NetUtils.HttpRequestPost(url, mHandle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 功能：进入主界面
	 */
	private void enterMain() {
		Intent lIntent = new Intent(this, FrgActMain.class);
		startActivity(lIntent);
		finish();
		AppBmap.getInstance().exit();
	}

	/**
	 * 提示号码错误或没有输入
	 */
	private void pleaseInputPhone() {
		mPhoneEdit.setText("");
		mPhoneView.setBackgroundResource(R.drawable.common_input_wrong);
		mPhoneEdit.setHintTextColor(Color.WHITE);
		ObjectAnimator.ofFloat(mPhoneView, "alpha", 1, 0.3f, 1, 0.3f, 1)
				.setDuration(1000).start();

	}

	/**
	 * 提示验证码错误或没有输入
	 */
	private void pleaseInputConver() {
		mConverEdit.setText("");
		mConverEdit.setBackgroundResource(R.drawable.common_input_wrong);
		mConverEdit.setHintTextColor(Color.WHITE);
		ObjectAnimator.ofFloat(mConverEdit, "alpha", 1, 0, 1, 0, 1)
				.setDuration(1000).start();
	}

	/**
	 * 功能：确认成功
	 */
	public void onSure() {
		String mInputConver = mConverEdit.getText().toString().trim();
		String mInputPhone = mPhoneEdit.getText().toString().trim();
		if (TextUtils.isEmpty(mInputPhone)) {
			pleaseInputPhone();
			return;
		}
		if (TextUtils.isEmpty(mInputConver)) {
			pleaseInputConver();
			return;
		}
		String url = DataCommon.SURE_CONFIRE_ROOT_PATH + mInputPhone
				+ "\",\"checkcode\":\"" + mInputConver + "\"}";
		try {
			mLogger.info("zzw:" + url);
			NetUtils.HttpRequestPost(url, mHandle);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void goPassword() {
		Intent intent = new Intent(this, ActPassWord.class);
		intent.putExtra("phone", mPhone);
		startActivity(intent);
	}

}
