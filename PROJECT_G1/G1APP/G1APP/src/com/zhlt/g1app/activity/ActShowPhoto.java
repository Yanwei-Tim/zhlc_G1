package com.zhlt.g1app.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpFrgActMain;
import com.zhlt.g1app.basefunc.BaseUtil;
import com.zhlt.g1app.basefunc.BitmapHelp;
import com.zhlt.g1app.basefunc.FileUtil;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.fragment.FrgViewPhoto;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: ActShowPhoto.java Description:图片查看
 * 
 * Title: ActShowPhoto Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月23日 下午4:08:12 1.0 Create this moudle
 */
public class ActShowPhoto extends FragmentActivity implements OnClickListener {

	private ArrayList<String> mPicList;
	private ImageView mImageIv;
	private ViewPager mViewPager;
	private TextView mIndexTv;
	private AdpFrgActMain mAdapter;
	private ArrayList<Fragment> fragments;
	private int index;
	private BitmapHelp mBitmapHelp;
	private BitmapUtils mBitmapUtils;
	private ProgressBar mPercentPb;

	private Logger mLogger = Log4jUtil.getLogger("");
	private ViewPhotoHandler mHandler;
	private final static int MSG_ANIMTION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_showphoto);
		initData();
		initView();
	}

	private void initData() {
		mPicList = getIntent().getStringArrayListExtra("list");
		index = getIntent().getIntExtra("index", 0);
		mHandler = new ViewPhotoHandler(this);
	}

	private void initView() {
		mImageIv = (ImageView) findViewById(R.id.iv_showphoto);
		mIndexTv = (TextView) findViewById(R.id.tv_showphoto_index);
		mPercentPb = (ProgressBar) findViewById(R.id.pb_showphoto);
		mPercentPb.setActivated(false);
		if (mPicList != null) {

			mLogger.info(mPicList.toString());
			if (mPicList.size() == 1) {
				mBitmapHelp = BitmapHelp.getBitmapHelp(this);
				mBitmapUtils = mBitmapHelp.getBitmapUtils();
				loadImage(mPicList.get(0));
				mImageIv.setOnClickListener(this);
			} else {
				mPercentPb.setVisibility(View.INVISIBLE);
				mIndexTv.setVisibility(View.VISIBLE);
				initViewPager();
			}
		}
	}

	private static class ViewPhotoHandler extends Handler {

		private WeakReference<ActShowPhoto> reference;

		public ViewPhotoHandler(ActShowPhoto register) {
			reference = new WeakReference<ActShowPhoto>(register);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ActShowPhoto photo = reference.get();
			if (photo == null) {
				return;
			}
			if (msg.what == MSG_ANIMTION) {
				float scale = (Float) msg.obj;
				photo.startScale(scale);
			}

		}

	}

	private void initViewPager() {
		mImageIv.setVisibility(View.INVISIBLE);
		mViewPager = (ViewPager) findViewById(R.id.vp_showphoto);
		mViewPager.setVisibility(View.VISIBLE);
		fragments = new ArrayList<Fragment>();
		for (int i = 0; i < mPicList.size(); i++) {
			FrgViewPhoto viewPhoto = new FrgViewPhoto();
			fragments.add(viewPhoto);
		}

		mAdapter = new AdpFrgActMain(getSupportFragmentManager(), fragments,
				mPicList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(index);
		mIndexTv.setText((index + 1) + "/" + mPicList.size());
		mViewPager.setOnPageChangeListener(mPageChangeListener);
	}

	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			mIndexTv.setText((arg0 + 1) + "/" + mPicList.size());
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};
	private AnimatorSet mScale;

	private void loadImage(String mUrl) {
		if (!TextUtils.isEmpty(mUrl)) {
			final Bitmap mBitmap = FileUtil.getBitmapFromUrl(mUrl);
			if (mBitmap != null) {
				mImageIv.setImageBitmap(mBitmap);
			}
			mImageIv.setAlpha(0.4f);
			mBitmapUtils.display(mImageIv, mUrl,
					new BitmapLoadCallBack<View>() {

						public void onLoading(
								View container,
								String uri,
								com.lidroid.xutils.bitmap.BitmapDisplayConfig config,
								long total, long current) {
							mLogger.info("onLoading" + uri);
							// mPercentPb.setMax((int) total);
							mPercentPb.setProgress((int) current);
						}

						@Override
						public void onLoadCompleted(View arg0, String arg1,
								Bitmap arg2, BitmapDisplayConfig arg3,
								BitmapLoadFrom arg4) {
							mLogger.info("onLoadCompleted" + arg1);
							mPercentPb.setVisibility(View.GONE);
							mImageIv.setImageBitmap(arg2);
							int mScreenWidth = BaseUtil
									.getDisplayWidth(ActShowPhoto.this);
							float scale = mScreenWidth * 1.0f / arg2.getWidth();
							if (mBitmap != null) {
								scale = mScreenWidth * 1.0f
										/ mBitmap.getWidth();
							}
							mLogger.info("scale " + scale);
							mHandler.sendMessage(mHandler.obtainMessage(
									MSG_ANIMTION, scale));
						}

						@Override
						public void onLoadFailed(View arg0, String arg1,
								Drawable arg2) {
							mLogger.info("onLoadFailed" + arg1);
							mImageIv.setImageResource(R.drawable.common_load_fail);
							Toast.makeText(ActShowPhoto.this, "加载失败", 0).show();
						}
					});
		}
		// }
	}

	private void startScale(float scale) {
		mImageIv.setAlpha(1.0f);
		mScale = new AnimatorSet();
		mScale.setDuration(500);
		mScale.playTogether(ObjectAnimator.ofFloat(mImageIv, "ScaleX", scale),
				ObjectAnimator.ofFloat(mImageIv, "ScaleY", scale));
		mScale.start();
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_showphoto) {
			finish();
		}

	}
}
