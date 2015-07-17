package com.zhlt.g1app.fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActHistoryTrack;
import com.zhlt.g1app.activity.ActPosition;
import com.zhlt.g1app.adapter.AdpFrgActMain;
import com.zhlt.g1app.application.AppBmap;
import com.zhlt.g1app.basefunc.SharePreferUtil;
import com.zhlt.g1app.basefunc.UpdateManager;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataCommon;
import com.zhlt.g1app.func.CommentCallBack;
import com.zhlt.g1app.func.IOnKeyboardStateChangedListener;
import com.zhlt.g1app.view.KeyboardListenRelativeLayout;
import com.zhlt.g1app.view.ViewXList;

/**
 * 
 ** Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd All rights reserved.
 * http://www.gobabymobile.cn/ File: - FrgActMain.java Description:描述
 ** 
 **
 ** ------------------------------- Revision History:
 * ------------------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * --------- yuanpeng@gobabymobile.cn 2015-6-29 上午10:13:13 1.0 Create this
 * moudle
 */
public class FrgActMain extends FragmentActivity implements OnClickListener,
		CommentCallBack {
	private ViewPager mPager;
	private ArrayList<Fragment> mFragments;
	private AdpFrgActMain mAdapter;
	private ArrayList<RadioButton> btnTitles;
	private ArrayList<String> mAdvImageLists = new ArrayList<String>();

	private FrgCarState mFrgCarState;
	private FrgCamera mFrgCamera;
	private FrgCommunity mFrgCommunity;
	private FrgSettings mFrgSettings;
	private TextView mTopTitle;
	private Button mPositionBtn;
	private View mTitleLeft;
	private InputMethodManager imm;
	private TextView mCommentEdit;
	private View mCommentView;
	private Button mCommentSendBtn;
	private KeyboardListenRelativeLayout activityRootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.frgact_main);
		AppBmap.getInstance().addActivity(this);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.initView();
		// getAdvImageData();
		// getAdvTextData();
		this.setListener();
		this.setCurrentPage(0);

		DisplayMetrics outMetrics = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		Log.d("zzw", outMetrics.widthPixels + " " + outMetrics.heightPixels);

		isupdata();
	}

	private void getAdvImageData() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				mAdvImageLists
						.add("http://image.haosou.com/zv?ch=go&t1=396&t2=491#ch=go&t1=396&t2=491&lightboxindex=0&groupid=4c56fda8f09f693f4bbd9e7621bff404&itemindex=0&dataindex=120");
			}
		}).start();
	}

	private void getAdvTextData() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				mAdvImageLists.add("13名密切接触者失联");
				mFrgCarState.setAdvTextData(mAdvImageLists);
			}
		}).start();
	}

	private void initView() {
		mPager = (ViewPager) findViewById(R.id.r_vps_frg_act_main);
		mPager.setOffscreenPageLimit(0);
		mCommentView = findViewById(R.id.llyt_main_comment);
		mCommentEdit = (EditText) findViewById(R.id.edit_main_comment);
		mCommentSendBtn = (Button) findViewById(R.id.btn_main_comment_send);
		mCommentSendBtn.setOnClickListener(this);

		activityRootView = (KeyboardListenRelativeLayout) findViewById(R.id.rlyt_main);

		activityRootView
				.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {

					public void onKeyboardStateChanged(int state) {
						switch (state) {
						case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE: // 软键盘隐藏
							hideComment();
							break;
						case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW: // 软键盘显示

							break;
						default:
							break;
						}
					}
				});

		mFragments = new ArrayList<Fragment>();
		mFrgCarState = new FrgCarState();
		mFrgCamera = new FrgCamera();
		mFrgCommunity = new FrgCommunity();
		mFrgCommunity.setCommentCallBack(this);
		mFrgSettings = new FrgSettings();
		mFragments.add(mFrgCarState);
		mFragments.add(mFrgCamera);
		mFragments.add(mFrgCommunity);
		mFragments.add(mFrgSettings);
		this.setViewPager();

		btnTitles = new ArrayList<RadioButton>();
		mTopTitle = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleLeft = findViewById(R.id.r_ib_title_left);
		mPositionBtn = (Button) findViewById(R.id.btn_title_right);
		mPositionBtn.setOnClickListener(this);
		initTitle();
	}

	private void setViewPager() {
		mAdapter = new AdpFrgActMain(getSupportFragmentManager(), mFragments);
		mPager.setAdapter(mAdapter);
		mPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				// if(arg0.getId()==) {
				// return true;
				// }
				return false;
			}
		});
	}

	/**
	 * 显示底部评论框
	 */
	public void showComment() {
		mCommentView.setVisibility(View.VISIBLE);
		mCommentEdit.requestFocus();
		showInput();
	}

	/**
	 * 获取输入的内容
	 */
	public String getComment() {
		return mCommentEdit.getText().toString().trim();
	}

	/**
	 * 隐藏底部评论框
	 */
	public void hideComment() {
		mCommentView.setVisibility(View.GONE);
		hideInput();
	}

	/**
	 * 显示系统键盘
	 */
	private void showInput() {
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mCommentEdit, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 隐藏系统键盘
	 */
	private void hideInput() {
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(
					mCommentEdit.getApplicationWindowToken(), 0);

		}
	}

	private void setListener() {
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int id) {
				setHeadandFoot(id);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * 初始化几个用来显示title的RadioButton
	 */
	private void initTitle() {
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_car_state));
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_camera));
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_community));
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_settings));
		for (int i = 0; i < btnTitles.size(); i++) {
			btnTitles.get(i).setOnClickListener(
					new FooterOnClickListener(i, false));
		}
	}

	private int[] mTitles = { R.string.car_state, R.string.camera,
			R.string.community, R.string.settings };

	/**
	 * 根据id设置切换页面和底部菜单
	 * 
	 * @param currentId
	 */
	private void setCurrentPage(int pCurrentId) {
		// 如果当前页面在圈子，再点击圈子的话，圈子回到第一页
		if (pCurrentId == 2) {
			if (mTopTitle.getText().toString().equals(getString(mTitles[2]))) {
				mFrgCommunity.backToTop();
			}
		}
		mPager.setCurrentItem(pCurrentId);
		setHeadandFoot(pCurrentId);
	}

	private void setHeadandFoot(int pCurrentId) {
		btnTitles.get(pCurrentId).setChecked(true);
		mTopTitle.setText(getString(mTitles[pCurrentId]));
		initTitleRight(pCurrentId);
	}

	private void startHistoryTrack() {
		Intent lIntent = new Intent(FrgActMain.this, ActHistoryTrack.class);
		startActivity(lIntent);
		// overridePendingTransition(R.anim.anim_alpha_in,
		// R.anim.anim_alpha_out);
		// finish();
	}

	private void initTitleRight(int pWhich) {
		ImageView mTitleRight = (ImageView) FrgActMain.this
				.findViewById(R.id.r_ib_title_right);
		switch (pWhich) {
		case 0:
			mPositionBtn.setVisibility(View.GONE);
			mTitleRight
					.setImageResource(R.drawable.selector_frgact_main_title_track);
			mTitleRight.setVisibility(View.VISIBLE);
			mTitleRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					startHistoryTrack();
				}
			});
			break;
		case 1:
			mPositionBtn.setVisibility(View.GONE);
			mTitleRight
					.setImageResource(R.drawable.selector_frgact_main_title_add);
			mTitleRight.setVisibility(View.VISIBLE);
			mTitleRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(FrgActMain.this, "添加。。。", Toast.LENGTH_LONG)
							.show();
				}
			});
			break;
		case 2:
			mPositionBtn.setVisibility(View.VISIBLE);
			mTitleRight.setVisibility(View.GONE);
			break;
		case 3:
			mPositionBtn.setVisibility(View.GONE);
			mTitleRight.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private long mExitTime;

	/**
	 * 重写OnClickListener的响应函数，主要目的就是实现点击title时，pager会跟着响应切换
	 * 
	 * @author tdx
	 */
	private class FooterOnClickListener implements OnClickListener {
		private int index;

		public FooterOnClickListener(int index, boolean isMenubtn) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			FrgActMain.this.setCurrentPage(index);
		}
	}

	private void isupdata() {
		// TODO Auto-generated method stub
		UpdateManager um = new UpdateManager(FrgActMain.this);
		um.checkUpdate();
		UserInfoUtil.getUserInfoUtil().setUserData(SharePreferUtil.getUserData(getApplicationContext()));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_main_comment_send:
			hideComment();
			mFrgCommunity.onCommentSuccess(getComment());
			mCommentEdit.setText("");
			break;

		case R.id.btn_title_right:
			goPosition();
			break;

		default:
			break;
		}

	}

	/**
	 * 跳转到地区选择
	 */
	private void goPosition() {
		if (mCommentView.getVisibility() == View.VISIBLE) {
			hideComment();
		} else {
			Intent intent = new Intent(this, ActPosition.class);
			intent.putExtra("isFromCommunity", true);
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		mPositionBtn.setText(SharePreferUtil.getString(getApplicationContext(),
				DataCommon.SharePrefer.SharePrefer_Main_Position, "深圳"));
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			finish();
			super.onBackPressed();
		}
	}
}
