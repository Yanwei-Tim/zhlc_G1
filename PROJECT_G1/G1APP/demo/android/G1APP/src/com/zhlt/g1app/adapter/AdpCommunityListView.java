package com.zhlt.g1app.adapter;

import java.util.HashMap;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.BitmapHelp;
import com.zhlt.g1app.data.ShareData;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdpCommunityListView extends BaseAdapter {

	private List<ShareData> mList;
	private Context mContext;
	private BitmapUtils mBitmapUtils;
	private BitmapDisplayConfig mBitmapDisplayConfig;

	public AdpCommunityListView(Context context) {
		mContext = context;
		mBitmapUtils = BitmapHelp.getBitmapHelp(mContext).getBitmapUtils();
		mBitmapDisplayConfig = BitmapHelp.getBitmapHelp(mContext)
				.getBitmapDisplayConfig();
	}

	public void setData(List<ShareData> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ShareHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_xlistview, null);
			mHolder = new ShareHolder(convertView);
		} else {
			mHolder = (ShareHolder) convertView.getTag();
		}

		mBitmapUtils.display(mHolder.mContentIv, mList.get(position)
				.getContentPic(), mBitmapDisplayConfig, mBitmapLoadCallBack);
		mBitmapUtils.display(mHolder.mPicIv, mList.get(position).getHeadPic(),
				mBitmapDisplayConfig, mBitmapLoadCallBack);
		mHolder.mNameTv.setText(mList.get(position).getName());
		mHolder.mTextTv.setText(mList.get(position).getText());
		setCommentLayout(mHolder, position);
		return convertView;
	}

	private void setCommentLayout(ShareHolder mHolder, int position) {
		List<HashMap<String, String>> temp;
		temp = mList.get(position).getList();
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if (temp != null) {
			for (int i = 0; i < temp.size(); i++) {
				TextView name = (TextView) inflater.inflate(
						R.layout.textview_blue, null);
				name.setText(temp.get(i).get("name"));
				TextView comment = (TextView) inflater.inflate(
						R.layout.textview_dark, null);
				comment.setText(temp.get(i).get("comment"));
				mHolder.mLinearLayout.addView(name);
				mHolder.mLinearLayout.addView(comment);
			}
		}

	}

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

	private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
		final TransitionDrawable transitionDrawable = new TransitionDrawable(
				new Drawable[] { TRANSPARENT_DRAWABLE,
						new BitmapDrawable(imageView.getResources(), bitmap) });
		imageView.setImageDrawable(transitionDrawable);
		transitionDrawable.startTransition(500);
	}

	private class ShareHolder {
		public TextView mNameTv;
		public TextView mTextTv;
		public LinearLayout mLinearLayout;
		public ImageButton mPicIv;
		public ImageView mContentIv;

		public ShareHolder(View view) {
			mNameTv = (TextView) view.findViewById(R.id.tv_xlistview_name);
			mTextTv = (TextView) view.findViewById(R.id.tv_xlistview_text);
			mLinearLayout = (LinearLayout) view
					.findViewById(R.id.llyt_xlistview_comment);
			mContentIv = (ImageView) view
					.findViewById(R.id.iv_xlistview_content);
			mPicIv = (ImageButton) view.findViewById(R.id.ibtn_xlistitem_pic);
			view.setTag(this);
		}

	}

}
