package com.zhlt.g1app.adapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdpFrgActMain extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;// ��Ҫ��ӵ������Fragment
	private ArrayList<String> mList;

	public AdpFrgActMain(FragmentManager fm) {
		super(fm);
	}


	public AdpFrgActMain(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	public AdpFrgActMain(FragmentManager fm, ArrayList<Fragment> fragments,
			ArrayList<String> mList) {
		super(fm);
		this.fragments = fragments;
		this.mList = mList;
	}

	@Override
	public Fragment getItem(int arg0) {
		if (mList != null) {
			Bundle bundle = new Bundle();
			bundle.putInt("position", arg0);
			bundle.putString("pic", mList.get(arg0));
			fragments.get(arg0).setArguments(bundle);
		}
		return fragments.get(arg0);// ����Fragment����
	}

	@Override
	public int getCount() {
		return fragments.size();// ����Fragment�ĸ���
	}
}