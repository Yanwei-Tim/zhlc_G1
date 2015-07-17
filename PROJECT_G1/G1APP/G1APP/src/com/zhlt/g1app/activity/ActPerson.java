package com.zhlt.g1app.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: ActPerson.java Description:用户信息
 * 
 * Title: ActPerson Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月23日 下午4:07:55 1.0 Create this moudle
 */
public class ActPerson extends Activity {

	/** 用户信息字段名称和数据 */
	private List<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();

	/** 用户数据 */
	private DataUser mUserData;

	/** 打印 */
	private Logger mLogger = Log4jUtil.getLogger("");

	/** 是否调试 */
	private boolean mIsDebug = true;

	/** 标题 */
	private TextView mTitleTv;

	/** 底部弹出框 */
	private View mBottonView;

	/** 底部显示动画 */
	private TranslateAnimation mShowAnimation;

	/** 底部是否已经显示了 */
	private boolean mIsPhotoShow;

	/** 底部隐藏动画 */
	private TranslateAnimation mHideAnimation;

	/** 头像父布局 */
	private View mHeadView;

	/** 大头贴 */
	private ImageView mHeadIv;

	/** 名字 */
	private View mNameView;

	/** 名字内容 */
	private TextView mNameTv;

	/** 性别 */
	private View mSexView;

	/** 性别内容 */
	private TextView mSexTv;

	/** 地区 */
	private View mPositionView;

	/** 地区内容 */
	private TextView mPositionTv;

	/** 绑定手机 */
	private View mPhoneView;

	/** 手机内容 */
	private TextView mPhoneTv;

	/** 个性签名 */
	private View mSignView;

	/** 个性签名内容 */
	private TextView mSignTv;

	private View mDialogOutSideBg;

	/** 拍照按钮 */
	private Button mImageBtn;

	/** 手机相册选择按钮 */
	private Button mAlbumBtn;

	/** 取消按钮 */
	private Button mCancleBtn;

	private final int CASE_CAMERA = 1;

	private View mBackView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_person);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mBottonView = findViewById(R.id.person_bottom);
		mHeadIv = (ImageView) findViewById(R.id.iv_person_head);
		mDialogOutSideBg = findViewById(R.id.iv_person_dialog_outside);
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mNameTv = (TextView) findViewById(R.id.tv_person_name_value);
		mSexTv = (TextView) findViewById(R.id.tv_person_sex_value);
		mPositionTv = (TextView) findViewById(R.id.tv_person_position_value);
		mPhoneTv = (TextView) findViewById(R.id.tv_person_phone_value);
		mSignTv = (TextView) findViewById(R.id.tv_person_sign_value);
		mTitleTv.setText(R.string.person);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mHeadView = findViewById(R.id.rlyt_person_head);
		mNameView = findViewById(R.id.rlyt_person_name);
		mSexView = findViewById(R.id.rlyt_person_sex);
		mPositionView = findViewById(R.id.rlyt_person_position);
		mPhoneView = findViewById(R.id.rlyt_person_phone);
		mSignView = findViewById(R.id.rlyt_person_sign);

		mImageBtn = (Button) findViewById(R.id.btn_person_image);
		mAlbumBtn = (Button) findViewById(R.id.btn_person_album);
		mCancleBtn = (Button) findViewById(R.id.btn_person_cancle);
		mImageBtn.setOnClickListener(mOnClickListener);
		mAlbumBtn.setOnClickListener(mOnClickListener);
		mCancleBtn.setOnClickListener(mOnClickListener);
		mBackView.setOnClickListener(mOnClickListener);
		mSignView.setOnClickListener(mOnClickListener);
		mHeadView.setOnClickListener(mOnClickListener);
		mNameView.setOnClickListener(mOnClickListener);
		mSexView.setOnClickListener(mOnClickListener);
		mPositionView.setOnClickListener(mOnClickListener);
		mPhoneView.setOnClickListener(mOnClickListener);
		mDialogOutSideBg.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				if (mIsPhotoShow) {
					hide();
				} else {
					finish();
				}
				break;
			case R.id.iv_person_dialog_outside:
				hide();
				break;
			case R.id.btn_person_image:
				startImage();
				hide();
				break;
			case R.id.btn_person_cancle:
				hide();
				break;
			case R.id.btn_person_album:
				startAlbum();
				hide();
				break;
			case R.id.rlyt_person_head:
				show();
				break;
			case R.id.rlyt_person_name:
				startActivity(ActName.class);
				break;
			case R.id.rlyt_person_sex:
				startActivity(ActGender.class);
				break;
			case R.id.rlyt_person_position:
				startActivity(ActPosition.class);
				break;
			case R.id.rlyt_person_phone:

				break;
			case R.id.rlyt_person_sign:
				startActivity(ActSign.class);
				break;

			}
		}
	};

	private int SELECT_PIC = 2;

	private Bitmap mBitmap;

	/**
	 * 启动系统拍照
	 */

	private void startActivity(@SuppressWarnings("rawtypes") Class class1) {
		Intent intent = new Intent(this, class1);
		startActivity(intent);
	}

	private void startImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CASE_CAMERA);
	}

	/**
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */

	private void startAlbum() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/jpeg");
		startActivityForResult(intent, SELECT_PIC);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == Activity.RESULT_OK) {
				if (requestCode == CASE_CAMERA) {
					Bundle extras = data.getExtras();
					Bitmap bitmap = (Bitmap) extras.get("data");
					isUploadPhoto(bitmap);
				} else if (requestCode == SELECT_PIC) {
					Uri imageFileUri = data.getData();// 获取选择图片的URI
					isUploadPhoto(imageFileUri);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新头像
	 * 
	 * @param obj
	 */
	private void isUploadPhoto(Object obj) {
		setImage(obj);
	}

	/**
	 * 设置头像
	 */
	public void setImage(Object bm) {

		if (bm instanceof Bitmap) {
			mBitmap = Bitmap.createScaledBitmap((Bitmap) bm, 50, 50, true);
		} else if (bm instanceof Uri) {
			Bitmap bmp;
			try {
				bmp = MediaStore.Images.Media.getBitmap(getContentResolver(),
						(Uri) bm);
				mBitmap = Bitmap.createScaledBitmap(bmp, 50, 50, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mHeadIv.setImageBitmap(mBitmap);
	}

	/**
	 * 初始化个人资料
	 */
	private void getListData() {
		mList.clear();
		mUserData = UserInfoUtil.getUserInfoUtil().getUserData();
		if (mIsDebug) {
			mLogger.info("zzw getListData:" + mUserData.getUserGender());
		}
		mNameTv.setText(mUserData.getUserName());
		mSexTv.setText(mUserData.getUserGender());
		mPositionTv.setText(mUserData.getUserPosition());
		mPhoneTv.setText(mUserData.getUserPhone());
		mSignTv.setText(mUserData.getUserSign());
	}

	/**
	 * 隐藏底部框
	 */
	private void hide() {
		mIsPhotoShow = false;
		if (mHideAnimation == null) {
			mHideAnimation = new TranslateAnimation(0, 0, 0, 600);
			mHideAnimation.setDuration(500);
			mHideAnimation.setFillAfter(true);
			mHideAnimation.setAnimationListener(mHideAnimationListener);
		}
		mBottonView.startAnimation(mHideAnimation);

	}

	/**
	 * 显示底部框动画
	 */
	private void show() {
		showBottomView();
		if (mShowAnimation == null) {
			mShowAnimation = new TranslateAnimation(0, 0, 500, 0);
			mShowAnimation.setDuration(500);
			mShowAnimation.setFillAfter(true);
		}
		mDialogOutSideBg.setVisibility(View.VISIBLE);
		mBottonView.startAnimation(mShowAnimation);
		mIsPhotoShow = true;
	}

	private AnimationListener mHideAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mDialogOutSideBg.setVisibility(View.GONE);
			hideBottomView();
		}
	};

	private void hideBottomView() {
		mImageBtn.setVisibility(View.GONE);
		mAlbumBtn.setVisibility(View.GONE);
		mCancleBtn.setVisibility(View.GONE);
		mBottonView.setVisibility(View.GONE);
	}

	private void showBottomView() {
		mImageBtn.setVisibility(View.VISIBLE);
		mAlbumBtn.setVisibility(View.VISIBLE);
		mCancleBtn.setVisibility(View.VISIBLE);
		mBottonView.setVisibility(View.VISIBLE);
	}

	/**
	 * 每次获取最新的用户信息
	 */
	@Override
	protected void onResume() {
		getListData();
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (mIsPhotoShow) {
				hide();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
