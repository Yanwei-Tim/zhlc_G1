package com.zhlt.g1app.activity;

import com.zhlt.g1app.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ActService extends Activity {

	/** 标题 */
	private TextView mTitleTv;

	/** 返回 */
	private View mBackView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_service);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.service);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(mOnClickListener);
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
}
