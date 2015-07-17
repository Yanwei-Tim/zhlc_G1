package com.zhlt.g1app.basefunc;

import com.zhlt.g1app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DialogView {

	private Activity mActivity;
	private OnClickListener mOnClickListener;
	private Dialog dialog;
	private View contentView;
	private TextView title;
	private TextView cancleTv;
	private TextView sureTv;

	public DialogView(Activity context) {
		mActivity = context;
		initView();
	}

	public void setOnClickListener(OnClickListener mOnClickListener) {
		cancleTv.setOnClickListener(mOnClickListener);
		sureTv.setOnClickListener(mOnClickListener);
	}

	private void initView() {
		dialog = new Dialog(mActivity, R.style.dialog_no_title);
		contentView = LayoutInflater.from(mActivity).inflate(
				R.layout.dialog_upgrade, null);
		title = (TextView) contentView.findViewById(R.id.tv_dialog_title);
		cancleTv = (TextView) contentView.findViewById(R.id.tv_dialog_cancle);
		sureTv = (TextView) contentView.findViewById(R.id.tv_dialog_sure);
		dialog.setContentView(contentView);
	}
	
	public void setCanceledOnTouchOutside(boolean isTouch){
		dialog.setCanceledOnTouchOutside(isTouch);
	}

	public void setTitle(String text) {
		title.setText(text);
	}

	public void setLeft(String text) {
		cancleTv.setText(text);
	}

	public void setRight(String text) {
		sureTv.setText(text);
	}

	public void show() {
		dialog.show();
	}

	public void dismiss() {
		dialog.dismiss();
	}
}
