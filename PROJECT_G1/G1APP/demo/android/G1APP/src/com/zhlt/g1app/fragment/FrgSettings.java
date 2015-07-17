package com.zhlt.g1app.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActPerson;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.data.CommonData;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FrgSettings extends FrgBase implements OnClickListener {

	private View mRootView;
	private ViewPager mViewPager = null;
	private TextView mVideoTv;
	private TextView mImageTv;
	private ImageView mVideoIv;
	private ImageView mImageIv;

	private Runnable mRunnable = null;
	private Logger log4jUtil = Log4jUtil.getLogger("");
	private ArrayList<String> mAdvFragments = new ArrayList<String>();
	private boolean mIsShowSuccess = false;
	private boolean mIsLoadSuccess = false;
	private View mPersonView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		log4jUtil.info("zzw  real onCreateView");
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.frg_settings, null);
		}
		initView();
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);// 先移除
		}
		mIsPrepare = true;
		return mRootView;
	}

	public void setAdvView() {
		mIsShowSuccess = true;

	}

	private void initView() {

		mPersonView = mRootView.findViewById(R.id.i_ll_setsimnumber);
          mPersonView.setOnClickListener(this);
	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		


	}
	
	/**
	 * 跳转到个人资料
	 */
	private void goPerson(){
		Intent intent = new Intent(getActivity(),ActPerson.class);
		startActivity(intent);
	}

}
