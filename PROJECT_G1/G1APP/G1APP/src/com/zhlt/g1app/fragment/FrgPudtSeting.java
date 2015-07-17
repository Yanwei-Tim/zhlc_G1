package com.zhlt.g1app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActAppStore;

public class FrgPudtSeting extends Fragment implements OnClickListener {
	private ImageButton mImageButton;
	private LinearLayout mLinearLayout_Save_Manger;
	private LinearLayout mLinearLayout_xiazaiLayout;
	private static final boolean CurrenStae = false;
	SeekBar mSeekBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.act_product_seting, container,
				false);
		// mImageButton=(ImageButton) view.findViewById(R.id.back_button);
		// mImageButton.setOnClickListener(this);
		mLinearLayout_Save_Manger = (LinearLayout) view
				.findViewById(R.id.llyt_Save_Manger);
		mLinearLayout_Save_Manger.setOnClickListener(this);
		mLinearLayout_xiazaiLayout = (LinearLayout) view
				.findViewById(R.id.llyt_common_voice);
		mLinearLayout_xiazaiLayout.setOnClickListener(this);
		mSeekBar = (SeekBar) view.findViewById(R.id.sb_common_voice);
		mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
		return view;
	}
  private OnSeekBarChangeListener mSeekBarChangeListener=new OnSeekBarChangeListener() {
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
			if (seekBar.getProgress() > 50) {
				seekBar.setProgress(100);
			} else {
				seekBar.setProgress(0);
			}
		}
		
	
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	 if(seekBar.getProgress()>=0){
		 seekBar.setProgress(100);
	 }else {
		seekBar.getProgress();
	}

	
		
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		
	}
};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			ReturnAppStore();

			break;
		case R.id.llyt_Save_Manger:
			ReturnAppStore();
			break;
		case R.id.llyt_common_voice:
			Toast.makeText(getActivity(), "连接管理", Toast.LENGTH_SHORT).show();
		default:
			break;
		}

	}

	

	private void ReturnAppStore() {
		Intent intent = new Intent(getActivity(), ActAppStore.class);
		startActivityForResult(intent, 0);

	}

}
