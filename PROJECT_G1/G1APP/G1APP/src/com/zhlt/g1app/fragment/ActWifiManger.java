package com.zhlt.g1app.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TabWidget;
import android.widget.TextView;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpFrgActMain;

public class ActWifiManger extends FragmentActivity {

	private ViewPager mViewPager;
	private ArrayList<Fragment> mFragments;
	private AdpFrgActMain mAdapter;
	private ArrayList<RadioButton> btnTitles;
	private FrgFlowup mFrgFlowup;
	private FrgUpdatePwd mFrgUpdatePsw;
	private FrgResting mFrgRest;
	private TextView mTopTitle;
	private TextView mTextView;
	private View mBackView;
	TabWidget tab_line;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_wifi_mian);
		initView();
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_wifi);
		mViewPager.setOffscreenPageLimit(0);
	//	tab_line=(TabWidget)findViewById(R.id.tv_line);

       
		mFragments = new ArrayList<Fragment>();
		mFrgFlowup = new FrgFlowup();
		mFrgUpdatePsw = new FrgUpdatePwd();
	//	mFrgRest = new FrgResting();
		mFragments.add(mFrgFlowup);
		mFragments.add(mFrgUpdatePsw);
		//mFragments.add(mFrgRest);
		this.setViewPager();
		btnTitles = new ArrayList<RadioButton>();
		mTextView = (TextView) findViewById(R.id.r_tv_title_text);
		mTextView.setText(R.string.wifi);
		
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(mOnClickListener);
		
		initTitle();
	}

	/**
	 * 初始化几个用来显示title的RadioButton
	 */
	private void initTitle() {
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_wifi_chang));
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_wifi_password));
		//btnTitles.add((RadioButton) findViewById(R.id.tv_line));
		for (int i = 0; i < btnTitles.size(); i++) {
			btnTitles.get(i).setOnClickListener(
					new FooterOnClickListener(i, false));
			
		}
	
	
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;

			default:
				break;
			}

		}
	};

	private void setViewPager() {
		mAdapter = new AdpFrgActMain(getSupportFragmentManager(), mFragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				btnTitles.get(arg0).setChecked(true);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 根据id设置切换页面和底部菜单
	 * 
	 * @param currentId
	 */
	public void setCurrentPage(int pCurrentId) {
		mViewPager.setCurrentItem(pCurrentId);
	}

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
			ActWifiManger.this.setCurrentPage(index);
		}
	}

}
