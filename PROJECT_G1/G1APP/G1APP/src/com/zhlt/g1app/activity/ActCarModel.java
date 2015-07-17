package com.zhlt.g1app.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpCarModelListView;
import com.zhlt.g1app.basefunc.BaseUtil;
import com.zhlt.g1app.basefunc.SharePreferUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataUser;
import com.zhlt.g1app.fragment.FrgActMain;
import com.zhlt.g1app.view.SideBar;

public class ActCarModel extends Activity implements OnClickListener {

	/** 标题 */
	private TextView mTitleTv;

	private ListView mListView;

	private AdpCarModelListView mListAdapter;

	private TextView mFinishTv;

	private SideBar mSideBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_carmodel);
		initView();
	}

	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mFinishTv = (TextView) findViewById(R.id.tv_carmodel_save);
		mTitleTv.setText(R.string.carmodel);
		mSideBar = (SideBar) findViewById(R.id.sidebar_carmodel);
		mFinishTv.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.lv_carmodel);
		mListAdapter = new AdpCarModelListView(this);
		mListView.setAdapter(mListAdapter);
		mSideBar.setListView(mListView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_carmodel_save:
			enterMain();
			break;

		default:
			break;
		}

	}

	/**
	 * 功能：进入主界面
	 */
	private void enterMain() {
		SharePreferUtil.saveUserData(getApplicationContext(), UserInfoUtil
				.getUserInfoUtil().getUserData());
		Intent lIntent = new Intent(this, FrgActMain.class);
		startActivity(lIntent);
		finish();
	}
}