package com.zhlt.g1app.activity;


import com.zhlt.g1app.R;
import com.zhlt.g1app.fragment.FrgActMain;
import com.zhlt.g1app.func.SceneAnimation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ActFlash extends Activity {
	private startMainHandler mStartMainHandler = new startMainHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_flash);
		// 动画效果启动
		int duration = 0; // duration是记录第一个动画播放的总时间
		ImageView animationIV;
		animationIV = (ImageView) findViewById(R.id.iv_guide_ad);
		int[] pFrameRess={R.drawable.act_guide_anim_01,R.drawable.act_guide_anim_02,R.drawable.act_guide_anim_03,
				R.drawable.act_guide_anim_04,R.drawable.act_guide_anim_05,R.drawable.act_guide_anim_06,
				R.drawable.act_guide_anim_07,R.drawable.act_guide_anim_08,R.drawable.act_guide_anim_09};
		int[] pDurations={120,120,120,120,120,120,120,120,120};
		new SceneAnimation(animationIV, pFrameRess, pDurations, mStartMainHandler);
	}

	private void enterMain() {
		Intent lIntent = new Intent(this, FrgActMain.class);
		// Intent lIntent = new Intent(this, OverlayDemo.class);
		startActivity(lIntent);
		overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private class startMainHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			enterMain();
		}
		
	}
}
