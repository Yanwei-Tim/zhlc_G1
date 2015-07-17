package com.zhlt.g1app.activity;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.BaseUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ActFeedback extends Activity {
	/** 标题 */
	private TextView mTitleTv;

	/** 返回按钮 */
	private View mAboutBack;

	private TextView mSendTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_feedback);
		initView();
	}

	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.feedback);

		mAboutBack = findViewById(R.id.r_ib_title_left);
		mAboutBack.setVisibility(View.VISIBLE);
		mAboutBack.setOnClickListener(mOnClickListener);
		mSendTv = (TextView) findViewById(R.id.tv_feedback_send);
		mSendTv.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;
			case R.id.tv_feedback_send:
				send();
				break;
			}
		}
	};

	/**
	 * 发送反馈信息
	 */
	private void send() {

		finish();
	}
}
