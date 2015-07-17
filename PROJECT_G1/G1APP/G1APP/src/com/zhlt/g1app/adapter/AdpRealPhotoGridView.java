package com.zhlt.g1app.adapter;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.DataUser;
import com.zhlt.g1app.func.ShowPhotoCallBack;

public class AdpRealPhotoGridView extends BaseAdapter {

	private Context mContext;
	private Logger mLogger = Log4jUtil.getLogger("");
	private boolean mIsDebug = true;
	private ArrayList<Bitmap> mPhotoLists;
	private ShowPhotoCallBack mShowPhotoCallBack;

	public AdpRealPhotoGridView(Context mContext) {
		this.mContext = mContext;
	}

	public void setPhotoListener(ShowPhotoCallBack callBack) {
		mShowPhotoCallBack = callBack;
	}

	public void setData(ArrayList<Bitmap> mPhotoLists) {
		this.mPhotoLists = mPhotoLists;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mPhotoLists == null ? 0 : mPhotoLists.size();
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

		RealPhotoHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_image, null);
			mHolder = new RealPhotoHolder(convertView);
		} else {
			mHolder = (RealPhotoHolder) convertView.getTag();
		}
		mHolder.mPhotoIv.setTag(position);
		mHolder.mPhotoIv.setOnClickListener(mOnClickListener);
		mHolder.mPhotoIv.setImageBitmap(mPhotoLists.get(position));
		return convertView;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			mLogger.info("onClick"+position+"  "+mShowPhotoCallBack+"  "+mPhotoLists.size() );
			if (mShowPhotoCallBack != null && position < mPhotoLists.size()) {
				mShowPhotoCallBack.show(mPhotoLists.get(position));
			}

		}
	};

	private class RealPhotoHolder {
		private ImageView mPhotoIv;

		public RealPhotoHolder(View view) {
			mPhotoIv = (ImageView) view.findViewById(R.id.iv_pic);
			view.setTag(this);
		}
	}

}
