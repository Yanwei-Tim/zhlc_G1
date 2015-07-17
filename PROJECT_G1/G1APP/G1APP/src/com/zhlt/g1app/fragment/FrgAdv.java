package com.zhlt.g1app.fragment;

import com.zhlt.g1app.R;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FrgAdv extends Fragment {

	private View mRootView;
	private TextView mAdvTv;
	private ImageView mAdvIv;
	private String mAdvString = "";
	private int res = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.layout_adv, null);
		}
		res = getArguments().getInt("pic");
		mAdvString = getArguments().getString("text");
		initView();
		return mRootView;
	}

	public void setText(String adv) {
		if (mAdvTv != null) {
			mAdvTv.setText(adv);
		}
		mAdvString = adv;
	}

	public void setImage(int res) {
		if (mAdvIv != null) {
			mAdvIv.setImageResource(res);
		}
		this.res = res;
		Log.d("zzw", "setImage:" + res);
	}

	private void initView() {
		mAdvTv = (TextView) mRootView.findViewById(R.id.tv_adv_text);
		if (!TextUtils.isEmpty(mAdvString)) {
			mAdvTv.setText(mAdvString);
		}
		mAdvIv = (ImageView) mRootView.findViewById(R.id.tv_adv_image);
		if (res != 0) {
			mAdvIv.setImageResource(res);
		}
	}
}
