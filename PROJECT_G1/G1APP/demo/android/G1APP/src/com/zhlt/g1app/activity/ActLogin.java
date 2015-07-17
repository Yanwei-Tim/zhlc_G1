package com.zhlt.g1app.activity;

import java.util.Map;

import org.apache.log4j.Logger;

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
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.SharePreferUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.CommonData;
import com.zhlt.g1app.data.UserData;
import com.zhlt.g1app.fragment.FrgActMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class ActLogin extends Activity implements OnClickListener {

	private Button mSureBtn;
	private ImageButton mSinaIBtn;
	private ImageButton mQQIBtn;
	private ImageButton mWeiXinIBtn;

	// 友盟
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(CommonData.UM.DESCRIPTOR);

	private Logger mLog4jUtil = Log4jUtil.getLogger("");

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.act_login);
		addQZoneQQPlatform();
		initView();
	};

	private void addQZoneQQPlatform() {
		String appId = "100424468";
		String appKey = "c7394704798a158208a74ab60104f0ba";
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
		UMWXHandler wxCircleHandler = new UMWXHandler(this,WXAppId,WXAppSecret);
//		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	private void initView() {
		mSureBtn = (Button) findViewById(R.id.btn_login_sure);
		mSureBtn.setOnClickListener(this);
		mSinaIBtn = (ImageButton) findViewById(R.id.ibtn_login_sina);
		mSinaIBtn.setOnClickListener(this);
		mQQIBtn = (ImageButton) findViewById(R.id.ibtn_login_qq);
		mQQIBtn.setOnClickListener(this);
		mWeiXinIBtn = (ImageButton) findViewById(R.id.ibtn_login_weixin);
		mWeiXinIBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_sure:
			onLoginCorrect();
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
		}

	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
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
	 * 获取授权平台的用户信息</br>
	 */
	private void getUserInfo(SHARE_MEDIA platform) {
		mController.getPlatformInfo(ActLogin.this, platform,
				new UMDataListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						mLog4jUtil.info("zzw" + info.toString());
						if (info != null) {
							UserInfoUtil userInfoUtil = UserInfoUtil
									.getUserInfoUtil();
							UserData userData = new UserData();
							if (info.containsKey("gender")) {
								userData.setUserGender(info.get("gender")
										.toString());
							}
							// 如果数据包含UId则初始化
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
							saveUserInfo(userData);
							enterMain();
						}
					}
				});
	}

	private void saveUserInfo(UserData mUserData) {
		UserInfoUtil.getUserInfoUtil().setUserData(mUserData);
		SharePreferUtil.saveUserData(getApplicationContext(), mUserData);
	}

	/**
	 * 进入主界面
	 */
	private void enterMain() {
		Intent lIntent = new Intent(this, ActFlash.class);
		// Intent lIntent = new Intent(this, OverlayDemo.class);
		startActivity(lIntent);
		// overridePendingTransition(R.anim.anim_alpha_in,
		// R.anim.anim_alpha_out);
		finish();
	}

	/**
	 * 登入成功
	 */
	public void onLoginCorrect() {
		Intent mIntent = new Intent(this, ActPassWord.class);
		startActivity(mIntent);
		// overridePendingTransition(R.anim.anim_alpha_in, 0);
		finish();
	}

}
