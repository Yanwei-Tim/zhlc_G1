package com.zhlt.g1app.fragment;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActFlowUp;

public class FrgFlowup extends Fragment implements OnClickListener {

	private Button imageButton_flow;
	private ImageView needleView;
	private TextView showText;
	private Timer timer;
	// 指针旋转的角度
	private float MaxDegree = 0.1f;
	private float degree = 0.1f;
	private RotateAnimation animation;
	private boolean flag = true;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.act_flow, container, false);
		needleView = (ImageView) v.findViewById(R.id.needle);
		showText = (TextView) v.findViewById(R.id.degreeID);
		imageButton_flow=(Button)v.findViewById(R.id.imageButton1);
		imageButton_flow.setOnClickListener(this);
		// 启动定时器
		Timer timer = new Timer();
		timer.schedule(tasTimerTask, 0, 310);

		return v;

	}

	private TimerTask tasTimerTask = new TimerTask() {

		@Override
		public void run() {
			Message message1 = new Message();
			message1.what = 1;
			thandler.sendMessage(message1);

		}
	};
	private Handler thandler = new Handler() {
		public void handleMessage(Message msg1) {
			// 产生随机数0-40
			Random random = new Random();
			int a = -20 + random.nextInt(61);

			// 设置最高值
			MaxDegree = Float.parseFloat(String.valueOf(a + 20));
			// 开始转动
			// timer = new Timer();
			// // 设置每10毫秒转动一下
			// timer.schedule(new NeedleTask(), 0, 10);
			handler.sendEmptyMessage(0);
			showText.setText(String.valueOf(a));
			flag = true;
		};
	};

	public class NeedleTask extends TimerTask {

		@Override
		public void run() {
			handler.sendEmptyMessage(0);

		}

	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (degree <= MaxDegree * (3.0f)) {

				if (degree >= MaxDegree * (3.0f)) {
					timer.cancel();
				} else {
					degree += 5.0f;
					// 设置转动动画
//					animation = new RotateAnimation(degree, MaxDegree * (3.0f),
//							Animation.RELATIVE_TO_SELF, 0.9f,
//							Animation.RELATIVE_TO_SELF, 0.9f);
					animation=new RotateAnimation(degree,MaxDegree*(3.0f),
							                      Animation.RELATIVE_TO_SELF,0.9f,
							                      Animation.RELATIVE_TO_PARENT,0.9f);
				}

				// 设置动画时间1毫秒
				animation.setDuration(300);
				animation.setFillAfter(true);
				needleView.startAnimation(animation);
				flag = false;
				Log.v("1调用了", String.valueOf(degree) + "///" + MaxDegree);
			}

			if (degree >= MaxDegree * (3.0f) && flag == true) {

				if (degree <= MaxDegree * (3.0f)) {
					timer.cancel();
				} else {
					degree += -5.0f;
					animation = new RotateAnimation(degree, MaxDegree * (3.0f),
							Animation.RELATIVE_TO_SELF, 0.9f,
							Animation.RELATIVE_TO_SELF, 0.9f);
				}
				// 设置动画时间1毫秒
				animation.setDuration(300);
				animation.setFillAfter(true);
				needleView.startAnimation(animation);
				flag = true;

				Log.v("调用了2", String.valueOf(degree) + "///" + MaxDegree);

			}
			if (degree <= MaxDegree * (3.0f)) {

				if (degree >= MaxDegree * (3.0f)) {
					timer.cancel();
				} else {
					degree += 5.0f;
					// 设置转动动画
					animation = new RotateAnimation(degree, MaxDegree * (3.0f),
							Animation.RELATIVE_TO_SELF, 0.9f,
							Animation.RELATIVE_TO_SELF, 0.724f);
				}

				// 设置动画时间1毫秒
				animation.setDuration(300);
				animation.setFillAfter(true);
				needleView.startAnimation(animation);
				flag = false;
				Log.v("1调用了", String.valueOf(degree) + "///" + MaxDegree);
			}

			if (degree >= MaxDegree * (3.0f) && flag == true) {

				if (degree <= MaxDegree * (3.0f)) {
					timer.cancel();
				} else {
					degree += -5.0f;
					animation = new RotateAnimation(degree, MaxDegree * (3.0f),
							Animation.RELATIVE_TO_SELF, 0.9f,
							Animation.RELATIVE_TO_SELF, 0.724f);
				}
				// 设置动画时间1毫秒
				animation.setDuration(300);
				animation.setFillAfter(true);
				needleView.startAnimation(animation);
				flag = true;

				Log.v("调用了2", String.valueOf(degree) + "///" + MaxDegree);

			}

		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton1:
			Intent intent = new Intent(getActivity(), ActFlowUp.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
