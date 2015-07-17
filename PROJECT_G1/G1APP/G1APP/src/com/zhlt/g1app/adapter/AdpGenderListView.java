package com.zhlt.g1app.adapter;

import org.apache.log4j.Logger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataUser;

public class AdpGenderListView extends BaseAdapter {

	private Context mContext;
	private Logger mLogger = Log4jUtil.getLogger("");
	private final String[] mGenders = { "男", "女", "保密" };
	private DataUser mUserData;
	private boolean mIsDebug = true;

	// 第一次刷新的时候才初始化mLastGenderHolder,避免重复带来的错误
	private boolean mIsFirstAdapter = true;

	// 上次点击的性别
	private GenderHolder mLastGenderHolder = null;

	public AdpGenderListView(Context mContext) {
		this.mContext = mContext;
		mUserData = UserInfoUtil.getUserInfoUtil().getUserData();
	}

	@Override
	public int getCount() {
		return mGenders.length;
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
					R.layout.listitem_gender, null);
			mHolder = new GenderHolder(convertView);
		} else {
			mHolder = (GenderHolder) convertView.getTag();
		}
		if (mUserData.getUserGender().equals(mGenders[position])) {
			mHolder.mGenderIBtn.setVisibility(View.VISIBLE);
			if (mIsFirstAdapter) {
				mLastGenderHolder = mHolder;
				mIsFirstAdapter = false;
			}
		} else {
			mHolder.mGenderIBtn.setVisibility(View.GONE);
		}

		if (position == mGenders.length - 1) {
			mHolder.mLineIv.setVisibility(View.GONE);
		} else {
			mHolder.mLineIv.setVisibility(View.VISIBLE);
		}
		convertView.setTag(R.id.tag_gender, position);
		mHolder.mGenderTv.setText(mGenders[position]);
		convertView.setOnClickListener(mOnClickListener);
		return convertView;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag(R.id.tag_gender);
			if (mIsDebug) {
				mLogger.info("zzw startActivity:" + position);
			}
			if (mLastGenderHolder == (GenderHolder) v.getTag()) {
				return;
			}
			if (mLastGenderHolder != null) {
				mLastGenderHolder.mGenderIBtn.setVisibility(View.GONE);
			}
			mLastGenderHolder = (GenderHolder) v.getTag();
			mLastGenderHolder.mGenderIBtn.setVisibility(View.VISIBLE);
			mUserData.setUserGender(mGenders[position]);
			if (mIsDebug) {
				mLogger.info("zzw:" + mUserData.getUserGender());
			}
		}
	};

	private class GenderHolder {
		public ImageButton mGenderIBtn;
		public TextView mGenderTv;
		private ImageView mLineIv;

		public GenderHolder(View view) {
			mGenderTv = (TextView) view
					.findViewById(R.id.tv_gender_listitem_name);
			mGenderIBtn = (ImageButton) view
					.findViewById(R.id.ibtn_gender_listitem_select);
			mLineIv = (ImageView) view
					.findViewById(R.id.iv_gender_listitem_line);
			view.setTag(this);
		}

	}
}