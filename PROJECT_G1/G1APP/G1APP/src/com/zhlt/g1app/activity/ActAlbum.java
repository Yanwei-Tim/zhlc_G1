package com.zhlt.g1app.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpAlbumGridView;
import com.zhlt.g1app.adapter.AdpAlbumListView;
import com.zhlt.g1app.view.ViewXList;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 * 
 * File: ActAlbum.java Description:相册
 * 
 * Title: ActSubSet Administrator ------------------------------- Revision
 * History: ---------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn 2015年6月23日 下午3:24:41 1.0 Create this moudle
 */
public class ActAlbum extends Activity {

	/** 标题 */
	private TextView mTitleTv;

	/** 相册列表 */
	private ViewXList mListView;

	/** 相册ListView适配器 */
	private AdpAlbumListView mAlbumListAdapter;

	/** 返回按钮 */
	private View mAubumBackView;
	/** 相册数据的封装 */
	ArrayList<String> mPicArrayList;
	private int index;

	AdpAlbumGridView mAdpAlbumGridView;
	Bitmap bp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_album);
		initData();
		initView();
	}

	/**
	 * 功能:初始化数据 参数：无 返回值：无
	 */
	private void initData() {
		mAlbumListAdapter = new AdpAlbumListView(this);

	}

	private float ScanleWidth;
	private float ScanleHeight;
	Context mContext;

	private void initView() {

		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.album);
		mListView = (ViewXList) findViewById(R.id.xListView_album);
		mListView.setAdapter(mAlbumListAdapter);
		// 设置列表上下拉不刷新
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mAubumBackView = findViewById(R.id.r_ib_title_left);
		mAubumBackView.setVisibility(View.VISIBLE);
		mAubumBackView.setOnClickListener(mOnClickListener);

	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.r_ib_title_left:
				finish();
				break;
			// 相册返回事件
			case R.id.xListView_album:

			default:
				break;
			}
		}
	};
}
