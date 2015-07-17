package com.zhlt.g1app.activity;

import com.zhlt.g1app.R;
import com.zhlt.g1app.application.AppBmap;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.fragment.FrgActMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ActCode extends Activity implements OnClickListener {
	// 设定扫描码的结果
	private final static int SCANNIN_GREQUEST_CODE = 1;
	// 定义编辑框
	private EditText et_code;
	// 定义按钮功能
	private Button btn_login_sure;
	// ibtn_real_add
	private ImageButton btn_return;
	private View mBackView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_edt_sequence);
		InitView();

	}

	private void InitView() {
		btn_login_sure = (Button) findViewById(R.id.btn_login_sure);
		et_code = (EditText) findViewById(R.id.et_sequence_number);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(this);
		btn_login_sure.setOnClickListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 复写onActivityResult方法
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			// 如果结果码相同
			if (resultCode == RESULT_OK) {
				// 将获取到的绑定数据展示在TextView上面
				Bundle bundle = data.getExtras();
				et_code.getText().append(bundle.getString("result"));
				et_code.setText(bundle.getString("result").toString().trim());
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_sure:
			goCarModel();
			break;
		case R.id.r_ib_title_left:
			Log4jUtil.getLogger("===Log===");
			Log.i("msg", "===msg===");
			Intent intent1 = new Intent(ActCode.this, ActLogin.class);
			startActivity(intent1);

		default:
			break;
		}
	}

	/**
	 * 功能：进入主界面
	 */
	private void enterMain() {
		Intent lIntent = new Intent(this, FrgActMain.class);
		// Intent lIntent = new Intent(this, OverlayDemo.class);
		startActivity(lIntent);
		// overridePendingTransition(R.anim.anim_alpha_in,
		// R.anim.anim_alpha_out);
		finish();
	}

	/**
	 * 功能：注册车牌
	 */
	private void goCarModel() {
		Intent lIntent = new Intent(this, ActCarModel.class);
		startActivity(lIntent);
		finish();
		AppBmap.getInstance().exit();
	}
}
