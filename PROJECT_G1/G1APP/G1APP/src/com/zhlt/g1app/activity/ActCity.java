package com.zhlt.g1app.activity;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpPostionListView;
import com.zhlt.g1app.view.ViewXList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ActCity extends Activity {

	private ListView mListView;
	private AdpPostionListView mListViewAdapter;
	private int position = 0;
	private TextView mTitleTv;
	private View mBackView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_city);
		position = getIntent().getIntExtra("position", 0);
		initView();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_city);
		mListViewAdapter = new AdpPostionListView(this,
				com.zhlt.g1app.data.AddressData.CITIES[position],true);
		mListView.setAdapter(mListViewAdapter);
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText("城市");
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
			}
		}
	};
}
