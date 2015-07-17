package com.zhlt.g1app.activity;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.BaseUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.fragment.FrgActMain;

public class ActLanguage extends Activity {
	/** 标题 */
	private TextView mTitleTv;

	/** 返回按钮 */
	private View mAboutBack;

	private TextView mSendTv;

	private View mSimplifiedView;

	private View mTraditionalView;

	private View mEnglishView;

	private View mSimplifiedSelectView;

	private View mTraditionalSelectView;

	private View mEnglishSelectView;

	private View mLastSelectView;

	private View mBackView;

	private final int Simplified = 1;
	private final int Traditional = 2;
	private final int English = 3;

	private Integer language;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_language);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mSimplifiedView = findViewById(R.id.rlyt_language_simplified);
		mTraditionalView = findViewById(R.id.rlyt_language_traditional);
		mEnglishView = findViewById(R.id.rlyt_language_english);
		mSimplifiedSelectView = findViewById(R.id.ibtn_language_simplified_select);
		mTraditionalSelectView = findViewById(R.id.ibtn_language_traditional_select);
		mEnglishSelectView = findViewById(R.id.ibtn_language_english_select);
		getLanguage();
		mTitleTv.setText(R.string.language);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(mOnClickListener);
		mSimplifiedView.setOnClickListener(mOnClickListener);
		mTraditionalView.setOnClickListener(mOnClickListener);
		mEnglishView.setOnClickListener(mOnClickListener);
	}

	private void getLanguage() {
		language = Integer.valueOf(BaseUtil.getAppLanguage(this));
		setCurrentLanguage(language);
	}

	/**
	 * 设置当前的性别
	 */
	private void setCurrentLanguage(int i) {
		if (i == 1) {
			if (mLastSelectView != null) {
				mLastSelectView.setVisibility(View.GONE);
			}
			mSimplifiedSelectView.setVisibility(View.VISIBLE);
			mLastSelectView = mSimplifiedSelectView;
		} else if (i == 2) {
			if (mLastSelectView != null) {
				mLastSelectView.setVisibility(View.GONE);
			}
			mTraditionalSelectView.setVisibility(View.VISIBLE);
			mLastSelectView = mTraditionalSelectView;
		} else {
			if (mLastSelectView  != null) {
				mLastSelectView.setVisibility(View.GONE);
			}
			mEnglishSelectView.setVisibility(View.VISIBLE);
			mLastSelectView = mEnglishSelectView;
		}
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;
			case R.id.rlyt_language_simplified:
				setCurrentLanguage(Simplified);
				setLang(Locale.SIMPLIFIED_CHINESE);
				break;
			case R.id.rlyt_language_traditional:
				setCurrentLanguage(Traditional);
				setLang(Locale.TRADITIONAL_CHINESE);
				break;
			case R.id.rlyt_language_english:
				setCurrentLanguage(English);
				setLang(Locale.ENGLISH);
				break;
			}
		}
	};

	// // 方法二，通过语言码，getLanguage()方法可以获得对应语言码
	// // public String getLanguage ()
	// // Returns the language code for this Locale or the empty string if no
	// language was set.
	// // if
	// (curLocale.getLanguage().equals(Locale.SIMPLIFIED_CHINESE.getLanguage()))
	// {
	// // setLang(Locale.ENGLISH);
	// // } else {
	// // setLang(Locale.SIMPLIFIED_CHINESE);
	// // }
	// }
	// });
	// }
	//
	private void setLang(Locale l) {
		// 获得res资源对象
		Resources resources = getResources();
		// 获得设置对象
		Configuration config = resources.getConfiguration();
		// 获得屏幕参数：主要是分辨率，像素等。
		DisplayMetrics dm = resources.getDisplayMetrics();
		// 语言
		config.locale = l;
		resources.updateConfiguration(config, dm);

		// 刷新activity才能马上奏效
		startActivity(new Intent().setClass(ActLanguage.this, FrgActMain.class));
		finish();
	}

}
