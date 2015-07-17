package com.zhlt.g1app.activity;

import org.apache.log4j.Logger;

import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.fragment.FrgUpdatePwd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class ActConn extends Activity implements OnClickListener {
	ImageView maImageView_return;
	private EditText mEditText_Icluancher;
	private EditText mEditText_Apper;
	private Logger logUtiLogger=new Log4jUtil().getLogger("");

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		InitView();
	}

	private void InitView() {
		setContentView(R.layout.act_conn_phone);
		logUtiLogger.info("===ActConn===");
		maImageView_return = (ImageView) findViewById(R.id.r_connphone_title_left);
		maImageView_return.setVisibility(View.VISIBLE);
		maImageView_return.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.r_connphone_title_left:
		   finish();

			break;

		default:
			break;
		}

	}

	public void ReturnFance() {
		Intent intent=new Intent(this, FrgUpdatePwd.class);
		startActivity(intent);

	}
}
