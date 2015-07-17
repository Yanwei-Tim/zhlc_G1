package com.zhlt.g1app.adapter;

import org.apache.log4j.Logger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActCity;
import com.zhlt.g1app.activity.ActPosition;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.AddressData;
import com.zhlt.g1app.data.DataUser;

public class AdpPostionListView extends BaseAdapter {

	private Context mContext;
	private Logger mLogger = Log4jUtil.getLogger("");
	private String[] mDatas = null;
	private DataUser mUserData;
	private boolean mIsDebug = true;

	// 第一次刷新的时候才初始化mLastGenderHolder,避免重复带来的错误
	private boolean mIsFirstAdapter = true;

	private boolean mIsCity;
	// 上次点击的性别
	private GenderHolder mLastGenderHolder = null;

	public AdpPostionListView(Context mContext, String[] datas, boolean isCity) {
		this.mContext = mContext;
		mDatas = datas;
		mIsCity = isCity;
		mUserData = UserInfoUtil.getUserInfoUtil().getUserData();
	}

	@Override
	public int getCount() {
		return mDatas == null ? 0 : mDatas.length;
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

		GenderHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listitem_position, null);
			mHolder = new GenderHolder(convertView);
		} else {
			mHolder = (GenderHolder) convertView.getTag();
		}

		if (position == mDatas.length - 1) {
			mHolder.mLineIv.setVisibility(View.GONE);
		} else {
			mHolder.mLineIv.setVisibility(View.VISIBLE);
		}
		mHolder.mRootView.setTag(position);
		mHolder.mRootView.setOnClickListener(mOnClickListener);
		mHolder.mGenderTv.setText(mDatas[position]);
		return convertView;
	}

	public int getCurrentItem() {
		return mCurrentItem;
	}

	private int mCurrentItem = 0;
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int i = (Integer) v.getTag();
			if (mIsCity) {
				Intent intent = new Intent();
				intent.putExtra("i", i);
				((ActCity) mContext).setResult(-1, intent);
				((ActCity) mContext).finish();
			} else {
				startCity(i);
				mCurrentItem = i;
			}
		}
	};

	private void startCity(int i) {
		Intent intent = new Intent(mContext, ActCity.class);
		intent.putExtra("position", i);
		((ActPosition) mContext).startActivityForResult(intent, 1);
	}

	private class GenderHolder {
		public View mRootView;
		public TextView mGenderTv;
		private ImageView mLineIv;

		public GenderHolder(View view) {
			mGenderTv = (TextView) view
					.findViewById(R.id.tv_position_listitem_name);
			mRootView = view.findViewById(R.id.rlyt_position_listitem);
			mLineIv = (ImageView) view
					.findViewById(R.id.iv_position_listitem_line);
			view.setTag(this);
		}

	}
}