package com.zhlt.g1app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhlt.g1app.R;

public class ActFlowUp extends Activity implements OnClickListener {
	private ImageView mImageViewweixin;
	private TextView mTitleTv;
	private View mBackView;
	private ImageView mImageViewplay_bao;
	private Dialog dialog;
	private LinearLayout mLinearLayout_weixin_playLayout;
	private LinearLayout mLinearLayout_iv_play_baoLayout;

	protected void onCreate(android.os.Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_paly_page);
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.charge);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(this);
		InitView();
	};

	private void InitView() {
		mImageViewweixin = (ImageView) findViewById(R.id.iv_weixin);
		mImageViewweixin.setOnClickListener(this);
		mImageViewplay_bao = (ImageView) findViewById(R.id.iv_play_bao);
		mImageViewplay_bao.setOnClickListener(this);
		mLinearLayout_weixin_playLayout=(LinearLayout)findViewById(R.id.linearlayout);
		mLinearLayout_weixin_playLayout.setOnClickListener(this);
		mLinearLayout_iv_play_baoLayout=(LinearLayout)findViewById(R.id.linearlayout2);
		mLinearLayout_iv_play_baoLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.r_ib_title_left:
			finish();
			break;
		case R.id.iv_weixin:
			Create();

		default:
			break;
		case R.id.iv_play_bao:
			Create();
			break;
		case R.id.tv_subset_password_canle:
			dialog.dismiss();
			break;
		case R.id.linearlayout:
			Create();
			break;
		case R.id.linearlayout2:
			Create();
			break;
		}

	}

	private TextView mTextView_Sure;
	private TextView mTextView_Canle;
	private EditText mInputPasswordEdit;
	/** 对话框是否正在显示 */
	private boolean mIsDialogShow = false;
	public void Create() {
		if (dialog == null) {
			dialog = new Dialog(this, R.style.dialog_no_title);
			View view = getLayoutInflater().inflate(R.layout.item_dialog_wifi,
					null);
			mInputPasswordEdit = (EditText) view
					.findViewById(R.id.edit_subset_password);
			dialog.setContentView(view);
			mTextView_Canle = (TextView) view
					.findViewById(R.id.tv_subset_password_cancle);
			mTextView_Sure = (TextView) view
					.findViewById(R.id.tv_subset_password_sure_wifi);
//			mTextView_Canle.setOnClickListener(this);
//			mTextView_Sure.setOnClickListener(this);

		}
		dialog.show();

	}
	/** 隐藏界面**/
	private void hideDialog(){
		mIsDialogShow=true;
		
		
		
	}
}
