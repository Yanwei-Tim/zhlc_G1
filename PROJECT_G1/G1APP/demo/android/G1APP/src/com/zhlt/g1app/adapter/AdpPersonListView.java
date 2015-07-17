package com.zhlt.g1app.adapter;

import java.util.HashMap;
import java.util.List;

import com.zhlt.g1app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdpPersonListView extends BaseAdapter {

	private List<HashMap<String, Object>> mList;
	private Context mContext;

	public AdpPersonListView(Context mContext,
			List<HashMap<String, Object>> mList) {
		this.mContext = mContext;
		this.mList = mList;
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

		PersonHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_xlistview, null);
			mHolder = new PersonHolder(convertView);
		} else {
			mHolder = (PersonHolder) convertView.getTag();
		}
		if (position == 0) {

			mHolder.mPicIv.setVisibility(View.VISIBLE);
			mHolder.mValueTv.setVisibility(View.GONE);
		} else {
			mHolder.mPicIv.setVisibility(View.GONE);
			mHolder.mValueTv.setVisibility(View.VISIBLE);
			mHolder.mValueTv.setText(mList.get(position).get("value")
					.toString());
		}
		mHolder.mNameTv.setText(mList.get(position).get("name").toString());

		return convertView;
	}

	private class PersonHolder {
		public TextView mNameTv;
		public TextView mValueTv;
		private ImageView mPicIv;
		public ImageButton mGoIBtn;

		public PersonHolder(View view) {
			mNameTv = (TextView) view
					.findViewById(R.id.tv_person_listitem_name);
			mValueTv = (TextView) view
					.findViewById(R.id.tv_person_listitem_value);

			mPicIv = (ImageView) view.findViewById(R.id.iv_person_listitem_pic);
			mGoIBtn = (ImageButton) view
					.findViewById(R.id.ibtn_person_listitem_go);
			view.setTag(this);
		}

	}
}
