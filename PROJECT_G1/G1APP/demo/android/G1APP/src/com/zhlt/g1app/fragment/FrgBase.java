package com.zhlt.g1app.fragment;

import android.support.v4.app.Fragment;

public abstract class FrgBase extends Fragment {

	public boolean mIsVisiable = false;
	public boolean mIsPrepare = false;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {

		if (isVisibleToUser) {
			mIsVisiable = true;
			lazyLoad();
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	protected abstract void lazyLoad();

}
