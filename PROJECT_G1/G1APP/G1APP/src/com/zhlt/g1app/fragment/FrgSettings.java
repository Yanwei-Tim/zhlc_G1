package com.zhlt.g1app.fragment;

import org.apache.log4j.Logger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActAboutUs;
import com.zhlt.g1app.activity.ActAlbum;
import com.zhlt.g1app.activity.ActCarModel;
import com.zhlt.g1app.activity.ActCollect;
import com.zhlt.g1app.activity.ActHelp;
import com.zhlt.g1app.activity.ActHistoryTrack;
import com.zhlt.g1app.activity.ActPerson;
import com.zhlt.g1app.activity.ActShowPhoto;
import com.zhlt.g1app.activity.ActSubSet;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.CarModelData;
import com.zhlt.g1app.data.DataUser;

public class FrgSettings extends FrgBase implements OnClickListener {

	private View mRootView;
	private Logger log4jUtil = Log4jUtil.getLogger("");
	private View mPersonView;
	private View mAlbumView;
	private View mAboutView;
	private View mHelpView;
	private View mSetView;
	private View mCollectView;
	private LinearLayout linearLayout_wifiLayout;

	// 显示动画
	private TranslateAnimation mShowAnimation;
	// 头像图标
	private ImageView Set_userhead;

	private TextView mCarModelTv;

	private ImageView mCarModelIv;

	private DataUser mDataUser;
	//奔驰标志
	private ImageView mBeanTengImageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		log4jUtil.info("zzw  real onCreateView");
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.frg_setting, null);
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

	}
	//@+id/i_ll_wifi

	private void initView() {
		linearLayout_wifiLayout=(LinearLayout)mRootView.findViewById(R.id.i_ll_wifi);
		linearLayout_wifiLayout.setOnClickListener(this);
		// mPersonView = mRootView.findViewById(R.id.i_ll_person);
		// mPersonView.setOnClickListener(this);
		mAlbumView = mRootView.findViewById(R.id.i_ll_album);
		mAlbumView.setOnClickListener(this);
		// mAboutView = mRootView.findViewById(R.id.i_ll_about);
		// mAboutView.setOnClickListener(this);
		// mHelpView = mRootView.findViewById(R.id.i_ll_help);
		// mHelpView.setOnClickListener(this);
		mSetView = mRootView.findViewById(R.id.i_ll_set);
		mSetView.setOnClickListener(this);
		mCollectView = mRootView.findViewById(R.id.i_ll_collect);
		mCollectView.setOnClickListener(this);
	    mBeanTengImageView=(ImageView) mRootView.findViewById(R.id.iv_set_carmoel);
	    mBeanTengImageView.setOnClickListener(this);
		Set_userhead = (ImageView) mRootView.findViewById(R.id.iv_set_userhead);
		Set_userhead.setOnClickListener(this);

		mCarModelTv = (TextView) mRootView.findViewById(R.id.tv_set_carmodel);
		mCarModelIv = (ImageView) mRootView.findViewById(R.id.iv_set_carmoel);

		mDataUser = UserInfoUtil.getUserInfoUtil().getUserData();
		if (!TextUtils.isEmpty(mDataUser.getUserCarModel())) {

			mCarModelTv.setText(mDataUser.getUserCarModel());
			for (int i = 0; i < CarModelData.CarName.length; i++) {
				String[] mCar = CarModelData.CarName[i];
				for (int j = 0; j < mCar.length; j++) {
					if (mDataUser.getUserCarModel().equals(mCar[j])) {
						mCarModelIv.setImageResource(CarModelData.CarPic[i][j]);
					}
				}
			}
		}
	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_real_video:

			break;
		// case R.id.i_ll_person:
		// goPerson();
		// break;
		case R.id.i_ll_album:
			goAlbum();
			break;
		// case R.id.i_ll_about:
		// goAbout() ;
		// break;
		// case R.id.i_ll_help:
		// goHelp() ;
		// break;

		case R.id.i_ll_collect:
			goCollect();
			break;
		case R.id.i_ll_set:
			goSet();
			break;
		case R.id.iv_set_userhead:
			//跳转到个人信息界面
			ShowPerson();
			 
			break;
		case R.id.iv_set_carmoel:
			ShowHide();
			break;
		case R.id.i_ll_wifi:
			showWifi();
		}

	}

	private void ShowPerson() {
	 Intent intent=new Intent(getActivity(), ActPerson.class);
	 startActivity(intent);
		
	}

	private void showWifi() {
		log4jUtil.info("===System===");
		Log.d("====Seting====", "====Seting====");
	Intent  intent=new Intent(getActivity(), ActWifiManger.class);
	startActivity(intent);
	}

	private void ShowHide() {
		Intent intent = new Intent(getActivity(), ActCarModel.class);
		startActivity(intent);

	}



	/**
	 * 跳转到收藏页面
	 */
	private void goCollect() {
		Intent intent = new Intent(getActivity(), ActCollect.class);
		startActivity(intent);
	}

	/**
	 * 跳转到设置页面
	 */
	private void goSet() {
		//元程序跳转的页面
//		Intent intent = new Intent(getActivity(), ActSubSet.class);
//		startActivity(intent);
		Intent   intent= new Intent(getActivity(), SystemManger.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * 跳转到历史轨迹
	 */
	private void goHistory() {
		Intent intent = new Intent(getActivity(), ActHistoryTrack.class);
		startActivity(intent);
	}

	/**
	 * 跳转到帮助
	 */
	private void goHelp() {
		Intent intent = new Intent(getActivity(), ActHelp.class);
		startActivity(intent);
	}

	/**
	 * 跳转到关于我们
	 */
	private void goAbout() {
		Intent intent = new Intent(getActivity(), ActAboutUs.class);
		startActivity(intent);
	}

	/**
	 * 跳转到个人资料
	 */
	private void goPerson() {
		Intent intent = new Intent(getActivity(), ActPerson.class);
		startActivity(intent);
	}

	/**
	 * 跳转到相册above of 加服务器里面图片。
	 */
	private void goAlbum() {
//		Intent intent = new Intent(getActivity(), ActAlbum.class);
//		startActivity(intent);
		Intent intent =new Intent(getActivity(), ActShowPhoto.class);
		startActivity(intent);
	}

}
