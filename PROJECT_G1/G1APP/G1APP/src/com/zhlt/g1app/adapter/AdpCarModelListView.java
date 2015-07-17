package com.zhlt.g1app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.CarModelData;
import com.zhlt.g1app.data.DataUser;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd All rights reserved.
 *
 * File: AdpAlbumListView.java Description:相册ListView适配器
 * 
 * Title: AdpAlbumListView Administrator -------------------------------
 * Revision History: ---------------------------- <author> <data> <version>
 * <desc>
 * ------------------------------------------------------------------------
 * ------ zhengzhaowei@gobabymobile.cn 2015年6月23日 下午4:13:31 1.0 Create this
 * moudle
 */
public class AdpCarModelListView extends BaseAdapter {

	private Context mContext;
	private DataUser mDataUser;

	public AdpCarModelListView(Context mContext) {
		this.mContext = mContext;
		mDataUser = UserInfoUtil.getUserInfoUtil().getUserData();
	}

	@Override
	public int getCount() {
		return CarModelData.CarWord.length;
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

		CarModelHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listitem_carmodel, null);
			mHolder = new CarModelHolder(convertView);
		} else {
			mHolder = (CarModelHolder) convertView.getTag();
		}
		convertView.setTag(R.id.tag_gender, position);
		addView(mHolder.mContainer, position);
		mHolder.mNameTv.setText(CarModelData.CarWord[position]);
		return convertView;
	}

	private void addView(LinearLayout layout, int position) {

		if (layout.getChildCount() > 0) {

			if (layout.getChildCount() <= CarModelData.CarPic[position].length) {
				getItem(layout, position);
				if (layout.getChildCount() < CarModelData.CarPic[position].length) {
					addItemView(layout, position, layout.getChildCount());
				}
			} else {
				getItem(layout, position);
				layout.removeViews(CarModelData.CarPic[position].length,
						layout.getChildCount()
								- CarModelData.CarPic[position].length);
			}
			return;
		}
		addItemView(layout, position, 0);
	}

	public void getItem(LinearLayout layout, int position) {
		for (int i = 0; i < layout.getChildCount(); i++) {
			LinearLayout view = (LinearLayout) layout.getChildAt(i);
			RelativeLayout relativeLayout = (RelativeLayout) view.getChildAt(0);
			ImageView iv = (ImageView) relativeLayout.getChildAt(0);
			TextView name = (TextView) relativeLayout.getChildAt(1);
			iv.setImageResource(CarModelData.CarPic[position][i]);
			name.setText(CarModelData.CarName[position][i]);
			view.setOnClickListener(mClickListener);
			view.setTag(CarModelData.CarName[position][i]);
		}
	}

	private void addItemView(LinearLayout layout, int position, int start) {
		for (int i = 0; i < CarModelData.CarPic[position].length; i++) {
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.item_carmodel, null);
			ImageView iv = (ImageView) view
					.findViewById(R.id.iv_carmodel_item_car_image);
			TextView name = (TextView) view
					.findViewById(R.id.tv_carmodel_item_car_name);
			if (i == CarModelData.CarPic[position].length - 1) {
				View line = view.findViewById(R.id.iv_carmodel_item_line);
				line.setVisibility(view.INVISIBLE);
			}
			iv.setImageResource(CarModelData.CarPic[position][i]);
			name.setText(CarModelData.CarName[position][i]);
			view.setOnClickListener(mClickListener);
			view.setTag(CarModelData.CarName[position][i]);
			layout.addView(view);
		}
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String name = (String) v.getTag();
			mDataUser.setUserCarModel(name);
		}
	};

	private class CarModelHolder {

		/**   */
		public LinearLayout mContainer;

		/** 列表子项item */
		public TextView mNameTv;

		public CarModelHolder(View view) {
			mNameTv = (TextView) view.findViewById(R.id.tv_carmodel_item_name);
			mContainer = (LinearLayout) view
					.findViewById(R.id.llyt_carmodel_item);
			view.setTag(this);
		}

	}

	public int getPositionForSection(char c) {
		for (int i = 0; i < CarModelData.CarWord.length; i++) {
			if (CarModelData.CarWord[i].contains(c + "")) {
				return i;
			}
		}
		return 0;
	}
}
