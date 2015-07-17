package com.zhlt.g1app.adapter;

import java.util.ArrayList;

import com.zhlt.g1app.fragment.FrgAdv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class AdpFragmentPager extends FragmentPagerAdapter {
	private ArrayList<String> mPicLists;// 图片
	private ArrayList<String> mTextLists;// 文字

	public AdpFragmentPager(FragmentManager fm) {
		super(fm);
	}

	public AdpFragmentPager(FragmentManager fm, ArrayList<String> textList,
			ArrayList<String> imageList) {
		super(fm);
		this.mTextLists = textList;
		this.mPicLists = imageList;
	}

	public void setTextData(ArrayList<String> list) {
		this.mTextLists = list;
		notifyDataSetChanged();
	}

	public void setImageData(ArrayList<String> list) {
		this.mPicLists = list;
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int arg0) {

		FrgAdv fragment = new FrgAdv();
		Bundle bundle = new Bundle();
		if (mTextLists != null&&mTextLists.size()>0) {
			int res = arg0 % mTextLists.size();
			bundle.putString("text", mTextLists.get(res));
			fragment.setArguments(bundle);
		} else {
			if(mPicLists!=null&&mPicLists.size()>0){
			int res = arg0 % mPicLists.size();
			bundle.putSerializable("pic", mPicLists.get(res));
			fragment.setArguments(bundle);
			}
		}
		return fragment;
	}

	@Override
	public int getCount() {
		if (mPicLists == null && mTextLists == null) {
			return 0;
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}