package com.zhlt.g1app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.zhlt.g1app.R;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd
 * All rights reserved.
 *
 * File: AdpAlbumListView.java
 * Description:相册ListView适配器
 * 
 * Title: AdpAlbumListView
 * Administrator 
 * ------------------------------- Revision History: ----------------------------
 * <author>                        <data>       <version>   <desc>
 * ------------------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn        2015年6月23日       下午4:13:31     1.0         Create this moudle
 */
public class AdpAlbumListView extends BaseAdapter {

	private Context mContext;
	private String[] mData = { "今天", "上个月" };
	private final int[] mContains = { 2, 6 };

	public AdpAlbumListView(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mData.length;
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
	public View getView(int position, View convertView, ViewGroup parent) {

		AlbumHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listitem_album, null);
			mHolder = new AlbumHolder(convertView);
		} else {
			mHolder = (AlbumHolder) convertView.getTag();
		}
		convertView.setTag(R.id.tag_gender, position);
		AdpAlbumGridView mGridViewAdapter = new AdpAlbumGridView(mContext,
				mContains[position]);
		mHolder.mAlbumGv.setAdapter(mGridViewAdapter);
		mHolder.mTimeTv.setText(mData[position]);
		return convertView;
	}

	private class AlbumHolder {
		
		/**  列表子项item的GrideView，显示多张图片  */
		public GridView mAlbumGv;
		
		/**  列表子项item的时间  */
		public TextView mTimeTv;
		
		public AlbumHolder(View view) {
			mTimeTv = (TextView) view.findViewById(R.id.tv_album_time);
			mAlbumGv = (GridView) view.findViewById(R.id.gv_album);
			view.setTag(this);
		}

	}
}
