package com.zhlt.g1app.adapter;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.BitmapHelp;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.data.DataShare;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 * Copyright (C), 2015, GoBaby Mobile Corp., Ltd
 * All rights reserved.
 *
 * File: AdpCollectListView.java
 * Description:收藏ListView适配器
 * 
 * Title: AdpCollectListView
 * Administrator 
 * ------------------------------- Revision History: ----------------------------
 * <author>                        <data>       <version>   <desc>
 * ------------------------------------------------------------------------------
 * zhengzhaowei@gobabymobile.cn        2015年6月23日       下午4:15:03     1.0         Create this moudle
 */
public class AdpCollectListView extends BaseAdapter {

	
	private List<DataShare> mList;
	private Context mContext;
	private BitmapUtils mBitmapUtils;
	private BitmapDisplayConfig mBitmapDisplayConfig;
	
	/**  保存用户选择的item集合  */
	private List<Integer> mSelectList = new ArrayList<Integer>();
	private Logger mLog4jUtil = Log4jUtil.getLogger("");
	
	/**  是否正在编辑  */
	private boolean mIsEdit = false;

	public AdpCollectListView(Context context) {
		mContext = context;
		mBitmapUtils = BitmapHelp.getBitmapHelp(mContext).getBitmapUtils();
		mBitmapDisplayConfig = BitmapHelp.getBitmapHelp(mContext)
				.getBitmapDisplayConfig();
	}
	
   /**
    * 是否处于编辑状态
    * @param isEdit
    */
	
	public void setEdit(boolean isEdit){
		mIsEdit = isEdit;
		mSelectList.clear();
		notifyDataSetChanged();
	}

	/**
	 * 数据返回成功时，刷新列表
	 * @param mList
	 */
	public void setData(List<DataShare> mList) {
		this.mList = mList;
		mSelectList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}
	
	/**
	 * 获取当前选中的集合
	 * @return
	 */
	 public List<Integer> getmSelectList() {
		return mSelectList;
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

		ShareHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listitem_collect, null);
			mHolder = new ShareHolder(convertView);
		} else {
			mHolder = (ShareHolder) convertView.getTag();
		}

//		//开始加载内容的图片
//		mBitmapUtils.display(mHolder.mContentIv, mList.get(position)
//				.getContentPic(), mBitmapDisplayConfig, mBitmapLoadCallBack);
//		
//		//开始加载头像
//		mBitmapUtils.display(mHolder.mPicIv, mList.get(position).getHeadPic(),
//				mBitmapDisplayConfig, mBitmapLoadCallBack);
		
		mHolder.mNameTv.setText(mList.get(position).getName());
		mHolder.mTextTv.setText(mList.get(position).getText());
		mHolder.mCheckBox.setOnCheckedChangeListener(mOnCheckedChangeListener);
		mHolder.mCheckBox.setTag(position);
		if (position == mList.size() - 1) {
			mHolder.mLineIv.setVisibility(View.GONE);
		}else {
			mHolder.mLineIv.setVisibility(View.VISIBLE);
		}
		
		//根据编辑状态来显示列表 
		if (mIsEdit) {
			mHolder.mCheckBox.setVisibility(View.VISIBLE);
			if (mSelectList.contains(position)) {
				mHolder.mCheckBox.setChecked(true);
			}else {
				mHolder.mCheckBox.setChecked(false);
			}
		}else {
			mHolder.mCheckBox.setVisibility(View.GONE);
		}
		return convertView;
	}

	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int position = (Integer) buttonView.getTag();
			if (isChecked) {
				mSelectList.add(position);
				mLog4jUtil.info(mSelectList.toString());
			} else {
				//  移除集合中的某个item  
					for (int i = 0; i < mSelectList.size(); i++) {
                        if (position == mSelectList.get(i)) {
                        	mSelectList.remove(i);
                        	break;
						}						
					}
				}

		}
	};

	private BitmapLoadCallBack<ImageView> mBitmapLoadCallBack = new BitmapLoadCallBack<ImageView>() {

		@Override
		public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2,
				BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
			fadeInDisplay(arg0, arg2);
		}
	};
	private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(
			android.R.color.transparent);

	/**
	 * 图片加载成功时，有一个显示的动画
	 * @param imageView
	 * @param bitmap
	 */
	private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
		final TransitionDrawable transitionDrawable = new TransitionDrawable(
				new Drawable[] { TRANSPARENT_DRAWABLE,
						new BitmapDrawable(imageView.getResources(), bitmap) });
		imageView.setImageDrawable(transitionDrawable);
		transitionDrawable.startTransition(500);
	}

	private class ShareHolder {
	  
		/**  姓名  */
		public TextView mNameTv;
		
		/**  收藏的内容  */
		public TextView mTextTv;
		
		/**  头像  */
		public ImageButton mPicIv;
		
		/**  内容图片  */
		public ImageView mContentIv;
		
		/**  待删除子item  */
		private CheckBox mCheckBox;
		
		/**  横线  */
		private ImageView mLineIv;

		public ShareHolder(View view) {
			mNameTv = (TextView) view
					.findViewById(R.id.tv_collect_xlistview_name);
			mTextTv = (TextView) view
					.findViewById(R.id.tv_collect_xlistview_text);
			mContentIv = (ImageView) view
					.findViewById(R.id.iv_collect_xlistview_content);
			mPicIv = (ImageButton) view
					.findViewById(R.id.ibtn_collect_xlistitem_pic);
			mCheckBox = (CheckBox) view
					.findViewById(R.id.cb_collect_xlistitem_check);
			mLineIv= (ImageView) view.findViewById(R.id.iv_collect_listitem_line);
			view.setTag(this);
		}

	}

}
