package com.zhlt.g1app.fragment;

import java.util.ArrayList;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpFrgActMain;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

public class SystemManger extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Fragment> mFragments;
	private AdpFrgActMain mAdapter;
	private ArrayList<RadioButton> btnTitles;
	private FrgPudtSeting mFrgPudtSeting;
	private FrgSystSeting mfrgSystSeting;
	private TextView mTopTitle;
	private  TextView mTextView;
	private   View mBackView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_update_seting);
		initView();
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_wifi);
		mViewPager.setOffscreenPageLimit(0);
		mFragments = new ArrayList<Fragment>();
		mFrgPudtSeting = new FrgPudtSeting();
		mfrgSystSeting = new FrgSystSeting();
		this.setViewPager();
		// 添加title
		mFragments.add(mFrgPudtSeting);
		mFragments.add(mfrgSystSeting);
		btnTitles = new ArrayList<RadioButton>();
        mTextView=(TextView)findViewById(R.id.r_tv_title_text);
        mTextView.setText(R.string.Seting);
        mBackView=findViewById(R.id.r_ib_title_left);
        mBackView.setVisibility(View.VISIBLE);
        mBackView.setOnClickListener(mOnClickListener);
		initTitle();

	}
private OnClickListener mOnClickListener=new OnClickListener() {
	
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
	private void initTitle() {
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_wifi_chang));
		btnTitles.add((RadioButton) findViewById(R.id.r_rb_wifi_password));
		for (int i = 0; i < btnTitles.size(); i++) {
			btnTitles.get(i).setOnClickListener(
					new FooterOnClickListener(i, false));

		}

	}

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

	private class FooterOnClickListener implements OnClickListener {
		private int index;

		// public FooterOnClickListener(int index, boolean isMenubtn) {
		// this.index = index;
		// }

		public FooterOnClickListener(int index, boolean isMenuBtn) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			SystemManger.this.setCurrentPage(index);
		}
	}

	// 设置当前页面的页面ID
	public void setCurrentPage(int index) {
		mViewPager.setCurrentItem(index);

	}
}
