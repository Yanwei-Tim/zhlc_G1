package com.zhlt.g1app.activity;

import com.zhlt.g1app.R;
import com.zhlt.g1app.fragment.FrgActMain;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActPassWord extends Activity implements OnClickListener {

	private Button mLoginBtn;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.act_password);
		initView();
	};

	private void initView() {
		mLoginBtn = (Button) findViewById(R.id.btn_login_sure);
		mLoginBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_login_sure) {
			onLoginCorrect();
		}

	}

	/**
	 * 登入成功
	 */
	public void onLoginCorrect() {
		Intent mIntent = new Intent(this, FrgActMain.class);
		startActivity(mIntent);
//		overridePendingTransition(R.anim.anim_alpha_in, 0);
		finish();
	}

}
