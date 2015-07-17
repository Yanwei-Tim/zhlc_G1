package com.zhlt.g1app.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdpFrgActMain extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;// ��Ҫ��ӵ������Fragment

	public AdpFrgActMain(FragmentManager fm) {
		super(fm);
	}

	/**
	 * �Զ���Ĺ��캯��
	 * 
	 * @param fm
	 * @param fragments
	 *            ArrayList<Fragment>
	 */
	public AdpFrgActMain(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);// ����Fragment����
	}

	@Override
	public int getCount() {
		return fragments.size();// ����Fragment�ĸ���
	}
}