package com.zhlt.g1app.fragment;

import org.apache.log4j.Logger;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActConn;
import com.zhlt.g1app.basefunc.Log4jUtil;

public class FrgUpdatePwd extends Fragment implements OnClickListener {

	/** 存储空间隐藏动画 */
	private TranslateAnimation mSpaceHideAnimation;
	/** 窗口管理 */
	private PopupWindow mPopupWindow;
	/** 动画效果 */
	private TranslateAnimation mSpaceShowAnimation;
	/** 存储空间界面 */
	private View mSpaceView;
	/** 修改 */
	private Button btn_update;
	/** 连接 */
	private Button btn_conn_phone;

	/** 存储空间是否已经显示了 */
	private boolean mIsSpaceShow = false;
	private Logger log4jUtil = new Log4jUtil().getLogger("");
	private View mView;
	/** 初始化隐藏管理界面 */
	LinearLayout mLinearLayout;
	private Button btn_oranger_save;
	private Button btn_oranger_conn_phone;
	/** 隐藏按钮 */
	private TextView mtv_scremit;
	/** 显示按钮 */
	private TextView mtv_showscrmert;
	private Dialog mDialog = null;
	private boolean Canle = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.act_wifimanger, container, false);
		btn_update = (Button) v.findViewById(R.id.btn_wifi_update);
		btn_update.setOnClickListener(this);

		btn_conn_phone = (Button) v.findViewById(R.id.btn_connection_phone);
		btn_conn_phone.setOnClickListener(this);

		mtv_scremit = (TextView) v.findViewById(R.id.tv_scremit);

		mtv_showscrmert = (TextView) v.findViewById(R.id.textView1);
		log4jUtil.info("===输出===");
		/* 隐藏界面* */
		mLinearLayout = (LinearLayout) v.findViewById(R.id.LinearLayout);
		btn_oranger_save = (Button) v.findViewById(R.id.btn_oranger_save);
		btn_oranger_conn_phone = (Button) v
				.findViewById(R.id.btn_oranger_connecton);
		btn_oranger_save.setOnClickListener(this);
		btn_oranger_conn_phone.setOnClickListener(this);

		return v;

	}

	private void hidePopup() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_wifi_update:

			hidePhotoView();

			break;

		case R.id.btn_connection_phone:

			log4jUtil.info("===connPhoneface===");
			ReturnActivity();
			return;
		case R.id.btn_oranger_connecton:
			ReturnActivity();
			return;
		case R.id.btn_oranger_save:
			Toast.makeText(getActivity(), "是否需要提示框！", Toast.LENGTH_LONG).show();
			return;
	

		}

	}

	private void ReturnActivity() {

		Intent intent = new Intent(getActivity(), ActConn.class);
		startActivityForResult(intent, 0);

	}

	private void hidePhotoView() {
		btn_update.setVisibility(View.GONE);
		btn_conn_phone.setVisibility(View.GONE);
		mtv_showscrmert.setVisibility(View.GONE);
		showSpaceView();

	}

	/** 屏幕长度 */
	private int mScreenWidth;

	private void showSpaceView() {
		mLinearLayout.setVisibility(View.VISIBLE);
		mtv_scremit.setVisibility(View.VISIBLE);
		// btn_oranger_save.setVisibility(View.VISIBLE);
		// btn_oranger_conn_phone.setVisibility(View.VISIBLE);

	}

	private AnimationListener mSpaceHideAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			btn_update.setVisibility(View.GONE);
			// btn_conn_phone.setVisibility(View.GONE);
			mtv_scremit.setVisibility(View.VISIBLE);

		}
	};

}
