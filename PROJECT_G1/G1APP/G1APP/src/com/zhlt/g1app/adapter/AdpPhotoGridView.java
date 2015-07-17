package com.zhlt.g1app.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataUser;

public class AdpPhotoGridView extends BaseAdapter {

	private Context mContext;
	private boolean mIsDebug = true;
	private List<Integer> mSelectList = new ArrayList<Integer>();
	private Logger mLog4jUtil = Log4jUtil.getLogger("");

	// 是否正在编辑
	private boolean mIsEdit = false;

	// 上次点击的性别

	public AdpPhotoGridView(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return 15;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * 获取当前选中的集合
	 * 
	 * @return
	 */
	public List<Integer> getmSelectList() {
		return mSelectList;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoHolder mHolder;
		mLog4jUtil.info(position + "   "+mSelectList.contains(position));
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.griditem_photo, null);
			mHolder = new PhotoHolder(convertView);
		} else {
			mHolder = (PhotoHolder) convertView.getTag();
			mLog4jUtil.info(position + "  !null");
		}
		mHolder.mPhotoIBtn.setTag(mHolder);
		mHolder.mPhotoIBtn.setTag(R.id.tag_photo, position);

		if (mSelectList.contains(position)) {
			mHolder.mPhotoIv.setVisibility(View.VISIBLE);
		} else {
			mHolder.mPhotoIv.setVisibility(View.GONE);
		}
		mHolder.mPhotoIBtn.setOnClickListener(mOnClickListener);
		return convertView;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			PhotoHolder mHolder = (PhotoHolder) v.getTag();
			int position = (Integer) v.getTag(R.id.tag_photo);
			mLog4jUtil.info("onClick:" + position);
			if (mHolder != null) {
				if (mHolder.mPhotoIv.getVisibility() == View.VISIBLE) {
					mHolder.mPhotoIv.setVisibility(View.GONE);
					for (int i = 0; i < mSelectList.size(); i++) {
						if (position == mSelectList.get(i)) {
							mSelectList.remove(i);
							break;
						}
					}
				} else {
					mSelectList.add(position);
					mLog4jUtil.info("onClick:" + mSelectList.toString());
					mHolder.mPhotoIv.setVisibility(View.VISIBLE);
				}
			}
		}
	};

	private class PhotoHolder {
		public ImageButton mPhotoIBtn;
		public ImageView mPhotoIv;

		public PhotoHolder(View view) {
			mPhotoIBtn = (ImageButton) view
					.findViewById(R.id.ibtn_photo_griditem_pic);
			mPhotoIv = (ImageView) view
					.findViewById(R.id.iv_photo_griditem_state);
			view.setTag(this);
		}
	}

}
