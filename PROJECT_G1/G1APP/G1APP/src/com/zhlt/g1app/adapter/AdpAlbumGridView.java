package com.zhlt.g1app.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.zhlt.g1app.R;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd
 * All rights reserved.
 *
 * File: AdpAlbumGridView.java
 * Description:相册GridView适配器
 * 
 * Title: AdpAlbumGridView
 * Administrator 
 * ------------------------------- Revision History: ----------------------------
 * <author>                        <data>       <version>   <desc>
 * ------------------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn        2015年6月23日       下午4:11:07     1.0         Create this moudle
 */
public class AdpAlbumGridView extends BaseAdapter {

	private Context mContext;
	private int  lenth = 0;

	// 上次点击的性别

	public AdpAlbumGridView(Context mContext , int lenth) {
		this.mContext = mContext;
		this.lenth = lenth;
	}

	@Override
	public int getCount() {
		return lenth;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position,  View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_image, null); 
			
		} else {
            
		}
		return convertView;
	}
 

}
