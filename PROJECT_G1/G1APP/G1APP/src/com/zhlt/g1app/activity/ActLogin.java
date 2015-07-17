package com.zhlt.g1app.activity;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.zhlt.g1app.R;
import com.zhlt.g1app.application.AppBmap;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.SharePreferUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataCommon;
import com.zhlt.g1app.data.DataUser;
import com.zhlt.g1app.fragment.FrgActMain;
import com.zhlt.g1app.func.NetUtils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 * 
 * File: ActLogin.java Description:登入
 * 
 * Title: ActLogin Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月23日 下午4:06:26 1.0 Create this moudle
 */
public class ActLogin extends Activity implements OnClickListener {

	/** 确定按钮 */
	private Button mSureBtn;

	/** 立即注册 */
	private TextView mRegisterTv;

	/** 新浪微博登入按钮 */
	private ImageButton mSinaIBtn;

	/** QQ登入按钮 */
	private ImageButton mQQIBtn;

	/** 微信登入按钮 */
	private ImageButton mWeiXinIBtn;

	/** 标题 */
	private TextView mTitleTv;

	/** 跳过按钮 */
	private TextView mPassTv;

	private EditText mPhonEdit;

	private EditText mPasswordEdit;

	private Dialog mLoginDialog;

	/** 第三方友盟控制器 */
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(DataCommon.UM.DESCRIPTOR);

	/** 打印工具类 */
	private Logger mLog4jUtil = Log4jUtil.getLogger("");

	private LoginHandler mHandle;

	private View mPhoneView;

	private View mPasswordView;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		initData();
		initView();
	};

	private void initData() {
		AppBmap.getInstance().addActivity(this);
		mHandle = new LoginHandler(this);
		// 添加第三方平台到友盟控制器
		addPlatform();
	}

	private static class LoginHandler extends Handler {

		private WeakReference<ActLogin> reference;

		public LoginHandler(ActLogin register) {
			reference = new WeakReference<ActLogin>(register);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ActLogin register = reference.get();
			if (register == null) {
				return;
			}
			if (msg.what == DataCommon.Message.MSG_LOAD_FINISHED) {
				register.parseConverJson(msg.obj.toString());
			}
		}

	}

	/**
	 * 功能：添加第三方平台到友盟控制器
	 */
	private void addPlatform() {
		String appId = "1104733796";
		String appKey = "WJyzeoU2nAtNGnx2";

		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
		String WXAppId = "wx78bdaa5cf87baeb5";
		String WXAppSecret = "ac7cb5580328d4d6f5b9b45170bbb574";

		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, WXAppId,
				WXAppSecret);
		// wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	public void parseConverJson(String json) {
		hideLogining();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			if (jsonObject.getString("result").equals("1")) {
				Intent mIntent = new Intent(this, ActCapture.class);
				startActivity(mIntent);
				finish();
			} else if (jsonObject.getString("result").equals("3")) {
				pleaseInputPhone();
			} else if (jsonObject.getString("result").equals("4")) {
				pleaseInputPassword();
			} else {
				Toast.makeText(getApplicationContext(), "登入失败", 0).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 功能：初始化控件
	 */
	private void initView() {
		mSureBtn = (Button) findViewById(R.id.btn_login_sure);
		mSureBtn.setOnClickListener(this);
		mRegisterTv = (TextView) findViewById(R.id.tv_login_register);
		mRegisterTv.setOnClickListener(this);
		mSinaIBtn = (ImageButton) findViewById(R.id.ibtn_login_sina);
		mSinaIBtn.setOnClickListener(this);
		mQQIBtn = (ImageButton) findViewById(R.id.ibtn_login_qq);
		mQQIBtn.setOnClickListener(this);
		mWeiXinIBtn = (ImageButton) findViewById(R.id.ibtn_login_weixin);
		mWeiXinIBtn.setOnClickListener(this);
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.login);
		mPassTv = (TextView) findViewById(R.id.tv_login_pass);
		mPassTv.setOnClickListener(this);
		mPhonEdit = (EditText) findViewById(R.id.edit_login_phone);
		mPhonEdit.addTextChangedListener(mPhoneEditTextWatcher);
		mPhoneView = findViewById(R.id.llyt_edit_login_phone);
		mPasswordEdit = (EditText) findViewById(R.id.edit_login_password);
		mPasswordView = findViewById(R.id.llyt_login_verif);
		mPasswordEdit.addTextChangedListener(mPasswordEditTextWatcher);
	}

	/**
	 * 显示登入中
	 */
	private void showLogining() {
		if (mLoginDialog == null) {
			mLoginDialog = new Dialog(this, R.style.dialog_no_title);
			View view = getLayoutInflater().inflate(R.layout.dialog_loading,
					null);
			mLoginDialog.setContentView(view);
		}
		mLoginDialog.show();
	}

	/**
	 * 
	 */
	private void hideLogining() {

		if (mLoginDialog != null) {
			mLoginDialog.dismiss();
		}
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
			mPasswordView.setBackgroundResource(R.drawable.act_login_verifi);
			mPasswordEdit.setHintTextColor(getResources().getColor(
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
			mPhonEdit.setHintTextColor(getResources()
					.getColor(R.color.bb_gray2));
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_sure:
			checkLogin();
			break;
		case R.id.ibtn_login_qq:
			login(SHARE_MEDIA.QQ);
			break;
		case R.id.ibtn_login_sina:
			login(SHARE_MEDIA.SINA);
			break;
		case R.id.ibtn_login_weixin:
			login(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.tv_login_pass:
			enterMain();
			break;
		case R.id.tv_login_register:
			goRegister();
			break;
		}

	}

	/**
	 * 跳转到注册界面
	 */
	private void goRegister() {
		Intent lIntent = new Intent(this, ActRegister.class);
		startActivity(lIntent);
	}

	/**
	 * 功能：授权。如果授权成功，则获取用户信息 参数：platform代表当前登入的平台的信息
	 */
	private void login(final SHARE_MEDIA platform) {
		mController.doOauthVerify(ActLogin.this, platform,
				new UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						String uid = value.getString("uid");
						if (!TextUtils.isEmpty(uid)) {
							getUserInfo(platform);
						} else {
							Toast.makeText(ActLogin.this, "授权失败...",
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
					}
				});
	}

	/**
	 * 功能：获取授权平台的用户信息 参数：platform代表当前登入的平台的信息
	 */
	private void getUserInfo(SHARE_MEDIA platform) {
		mController.getPlatformInfo(ActLogin.this, platform,
				new UMDataListener() {

					@Override
					public void onStart() {

					}

					/**
					 * 成功获取用户信息，返回Map类型的用户数据
					 */
					@Override
					public void onComplete(int status, Map<String, Object> info) {
						mLog4jUtil.info("zzw" + info.toString());
						if (info != null) {
							UserInfoUtil userInfoUtil = UserInfoUtil
									.getUserInfoUtil();
							DataUser userData = new DataUser();
							if (info.containsKey("gender")) {
								userData.setUserGender(info.get("gender")
										.toString());
							}
							// 如果数据包含uid则初始化
							if (info.containsKey("uid")) {
								userData.setUserID(info.get("uid").toString());
							}

							if (info.containsKey("screen_name")) {
								userData.setUserName(info.get("screen_name")
										.toString());
							}
							if (info.containsKey("profile_image_url")) {
								userData.setUserPic(info.get(
										"profile_image_url").toString());
							}
							userInfoUtil.setUserData(userData);
							mLog4jUtil.info("zzw" + userData.toString());

							// 保存用户信息到SharePrefer
							saveUserInfo(userData);
							enterMain();
						}
					}
				});
	}

	/**
	 * 功能：保存用户信息到SharePrefer 参数： 用户信息类
	 */
	private void saveUserInfo(DataUser mUserData) {
		UserInfoUtil.getUserInfoUtil().setUserData(mUserData);
		SharePreferUtil.saveUserData(getApplicationContext(), mUserData);
	}

	/**
	 * 功能：进入主界面
	 */
	private void enterMain() {
		Intent lIntent = new Intent(this, FrgActMain.class);
		// Intent lIntent = new Intent(this, OverlayDemo.class);
		startActivity(lIntent);
		// overridePendingTransition(R.anim.anim_alpha_in,
		// R.anim.anim_alpha_out);
		// finish();
		AppBmap.getInstance().exit();
	}

	/**
	 * 提示号码错误或没有输入
	 */
	private void pleaseInputPhone() {
		mPhonEdit.setText("");
		mPhoneView.setBackgroundResource(R.drawable.common_input_wrong);
		mPhonEdit.setHintTextColor(Color.WHITE);
		ObjectAnimator.ofFloat(mPhoneView, "alpha", 1, 0.3f, 1, 0.3f, 1)
				.setDuration(1000).start();

	}

	/**
	 * 提示验证码错误或没有输入
	 */
	private void pleaseInputPassword() {
		mPasswordEdit.setText("");
		mPasswordView.setBackgroundResource(R.drawable.common_input_wrong);
		mPasswordEdit.setHintTextColor(Color.WHITE);
		ObjectAnimator.ofFloat(mPasswordView, "alpha", 1, 0, 1, 0, 1)
				.setDuration(1000).start();
	}

	/**
	 * 功能：登入成功
	 */
	public void checkLogin() {

		String userName = "\"user_name\":\"";
		String userPass = "\"user_pass\":\"";
		String type = "\"type\":\"1\"";
		String mPhone = mPhonEdit.getText().toString().trim();
		String mPassword = mPasswordEdit.getText().toString().trim();
		String url = DataCommon.LOGIN_ROOT_PATH + "data={" + userName + mPhone
				+ "\"," + userPass + mPassword + "\"," + type + "}";
		if (TextUtils.isEmpty(mPhone)) {
			pleaseInputPhone();
			return;
		}
		if (TextUtils.isEmpty(mPassword)) {
			pleaseInputPassword();
			return;
		}
		showLogining();
		try {
			NetUtils.HttpRequestPost(url, mHandle);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private long mExitTime;

	@Override
	public void onBackPressed() {

		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			super.onBackPressed();
		}
	}

}
