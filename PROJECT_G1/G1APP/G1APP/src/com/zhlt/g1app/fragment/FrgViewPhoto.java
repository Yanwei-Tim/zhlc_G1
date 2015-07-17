package com.zhlt.g1app.fragment;

import java.lang.ref.WeakReference;
import org.apache.log4j.Logger;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.BaseUtil;
import com.zhlt.g1app.basefunc.BitmapHelp;
import com.zhlt.g1app.basefunc.FileUtil;
import com.zhlt.g1app.basefunc.Log4jUtil;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FrgViewPhoto extends Fragment {

	private View mRootView;
	private ImageView mImageIv;
	private BitmapHelp mBitmapHelp;
	private BitmapUtils mBitmapUtils;
	private String mUrl = "";
	private ProgressBar mPercentPb;
	private Logger mLogger = Log4jUtil.getLogger("");
	private AnimatorSet mScale;
	private ViewPhotoHandler mHandler;
	private final static int MSG_ANIMTION = 1;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.viewpager_item_showphoto,
					null);
		}
		initData();
		initView();
		loadImage();
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);// 先移除
		}
		return mRootView;
	}

	private void initData() {
		mUrl = getArguments().getString("pic");
		mLogger.info(mUrl);
		mBitmapHelp = BitmapHelp.getBitmapHelp(getActivity());
		mBitmapUtils = mBitmapHelp.getBitmapUtils();
		mHandler = new ViewPhotoHandler(this);
	}

	private static class ViewPhotoHandler extends Handler {

		private WeakReference<FrgViewPhoto> reference;

		public ViewPhotoHandler(FrgViewPhoto register) {
			reference = new WeakReference<FrgViewPhoto>(register);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			FrgViewPhoto photo = reference.get();
			if (photo == null) {
				return;
			}
			if (msg.what == MSG_ANIMTION) {
				float scale = (Float) msg.obj;
				photo.startScale(scale);
			}

		}

	}

	private void initView() {

		mImageIv = (ImageView) mRootView.findViewById(R.id.iv_showphoto_item);
		mPercentPb = (ProgressBar) mRootView
				.findViewById(R.id.pb_showphoto_item);
		mLogger.info("onLoading" + mUrl);

	}

	private void loadImage() {
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
						}

						@Override
						public void onLoadCompleted(View arg0, String arg1,
								Bitmap arg2, BitmapDisplayConfig arg3,
								BitmapLoadFrom arg4) {
							mLogger.info("onLoadCompleted" + arg1);
							mImageIv.setImageBitmap(arg2);
							int mScreenWidth = BaseUtil
									.getDisplayWidth(getActivity());
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
							Toast.makeText(getActivity(), "加载失败", 0).show();
						}
					});
		}
		// }
	}

	private void startScale(float scale) {
		mPercentPb.setVisibility(View.GONE);
		mImageIv.setAlpha(1.0f);
		mScale = new AnimatorSet();
		mScale.setDuration(500);
		mScale.playTogether(ObjectAnimator.ofFloat(mImageIv, "ScaleX", scale),
				ObjectAnimator.ofFloat(mImageIv, "ScaleY", scale));
		mScale.start();
	}
}
