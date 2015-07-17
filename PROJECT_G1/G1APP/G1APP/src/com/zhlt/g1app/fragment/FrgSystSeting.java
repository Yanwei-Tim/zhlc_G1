package com.zhlt.g1app.fragment;

import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActAboutUs;
import com.zhlt.g1app.activity.ActLanguage;

import android.R.menu;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class FrgSystSeting extends Fragment implements OnClickListener {
	private LinearLayout mLinearLayout;
	private LinearLayout mLinearLayout_Notice;
	private LinearLayout mLinearLayout_Warming;
	private LinearLayout mLinearLayout_4G_Net;
	private LinearLayout linearLayout_Above_Wifi;
	private LinearLayout linearLayout_About_UsLayout;
	private Button btn_Clean_RecleyButton;
	private SeekBar mSeekBar_Warming;
	private SeekBar mSeekBar_WIFI_NET;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.act_update_system_seting, container,
				false);
		mLinearLayout = (LinearLayout) v.findViewById(R.id.llyt_common_offline);
		mLinearLayout.setOnClickListener(this);
		mLinearLayout_Notice = (LinearLayout) v
				.findViewById(R.id.llyt_common_Notice);
		mLinearLayout_Notice.setOnClickListener(this);
		mLinearLayout_Warming = (LinearLayout) v
				.findViewById(R.id.llyt_update_Warming);
		mLinearLayout_Warming.setOnClickListener(this);
		mLinearLayout_4G_Net = (LinearLayout) v
				.findViewById(R.id.llyt_common_4GConnection);
		mLinearLayout_4G_Net.setOnClickListener(this);
		linearLayout_Above_Wifi = (LinearLayout) v
				.findViewById(R.id.llyt_common_WiFiNet);
		linearLayout_Above_Wifi.setOnClickListener(this);
		linearLayout_About_UsLayout = (LinearLayout) v
				.findViewById(R.id.System_aboutUs);
		linearLayout_About_UsLayout.setOnClickListener(this);
		btn_Clean_RecleyButton=(Button)v.findViewById(R.id.btn_Clean_Recley);
		btn_Clean_RecleyButton.setOnClickListener(this);
		mSeekBar_Warming=(SeekBar)v.findViewById(R.id.sb_common_four_net);
		mSeekBar_WIFI_NET=(SeekBar)v.findViewById(R.id.sb_common_WIFI_Net);
		mSeekBar_Warming.setOnSeekBarChangeListener(WarmingOnSeekBarChangeListener);
		mSeekBar_WIFI_NET.setOnSeekBarChangeListener(WarmingOnSeekBarChangeListener);
		return v;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llyt_common_offline:
			ReturnLuangure();

			break;
		case R.id.llyt_common_Notice:
			Toast.makeText(getActivity(), "通知管理界面", Toast.LENGTH_SHORT).show();
			return;
		case R.id.llyt_update_Warming:
			Toast.makeText(getActivity(), "通知提醒界面", Toast.LENGTH_SHORT).show();
			return;
		case R.id.llyt_common_4GConnection:
			Toast.makeText(getActivity(), "4GConnection", Toast.LENGTH_SHORT).show();
			return;
		case R.id.llyt_common_WiFiNet:
			Toast.makeText(getActivity(), "WiFINET", Toast.LENGTH_SHORT).show();
			return;
		case R.id.System_aboutUs:
			ReturnAboutUs();
			Toast.makeText(getActivity(), "关于我们", Toast.LENGTH_SHORT).show();
			return;
		case R.id.btn_Clean_Recley:
			Toast.makeText(getActivity(), "清空缓存", Toast.LENGTH_SHORT).show();
			return;
		default:
			break;
		}

	}

	
	private OnSeekBarChangeListener WarmingOnSeekBarChangeListener=new OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if(seekBar.getProgress()>50){
				seekBar.setProgress(100);
			}else{
				seekBar.setProgress(0);
			}
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			
		}
	};

	private void ReturnAboutUs() {
		Intent intent = new Intent(getActivity(), ActAboutUs.class);
		startActivity(intent);

	}

	private void ReturnLuangure() {
		Intent intent = new Intent(getActivity(), ActLanguage.class);
		startActivity(intent);

	}
}
