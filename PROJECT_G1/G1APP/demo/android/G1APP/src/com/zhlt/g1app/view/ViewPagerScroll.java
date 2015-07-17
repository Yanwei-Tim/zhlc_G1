package com.zhlt.g1app.view;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ViewPagerScroll extends ViewPager {

	private boolean isCanScroll = true;

	public ViewPagerScroll(Context context) {
		super(context);
	}

	public ViewPagerScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (isCanScroll) {
			return super.onTouchEvent(arg0);
		} else {
			return false;
		}

	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (isCanScroll) {
			return super.onInterceptTouchEvent(arg0);
		} else {
			return false;
		}

	}

	@Override
	protected boolean canScroll(View arg0, boolean arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		if(arg0.getClass().getName().equals("com.baidu.mapapi.map.MapView")) {    
            return true;    
        } 
		return super.canScroll(arg0, arg1, arg2, arg3, arg4);
	}
	
}