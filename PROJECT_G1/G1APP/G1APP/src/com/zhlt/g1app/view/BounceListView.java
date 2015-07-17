package com.zhlt.g1app.view;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

public class BounceListView extends ListView {
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;
	private static final float SCROLL_RATIO = 0.5f;// 阻尼系数
	private Context mContext;
	private int mMaxYOverscrollDistance;
	private static final int MSG_SCROLL_BACK = 1;// 阻尼系数
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.d("zzw", mLastScrollY + " " + getScrollY());
			if (msg.what == MSG_SCROLL_BACK && mLastScrollY == getScrollY()) {
				scrollTo(0, -getScrollY());
				Log.d("zzw", "scrollTo");
				mLastScrollY = 0;
			}
		};
	};
	private int mLastScrollY;

	public BounceListView(Context context) {
		super(context);
		mContext = context;
		initBounceListView();
	}

	public BounceListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initBounceListView();
	}

	public BounceListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initBounceListView();
	}

	private void initBounceListView() {
		// get the density of the screen and do some maths with it on the max
		// overscroll distance
		// variable so that you get similar behaviors no matter what the screen
		// size

		final DisplayMetrics metrics = mContext.getResources()
				.getDisplayMetrics();
		final float density = metrics.density;
		final String dp = metrics.widthPixels + "*" + metrics.heightPixels;
		final float densityDpi = metrics.densityDpi;
		mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
		Toast.makeText(mContext, density + " " + dp + " " + densityDpi, 1)
				.show();
		// this.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

		try {
			Class<?> c = (Class<?>) Class.forName(AbsListView.class.getName());
			Field egtField = c.getDeclaredField("mEdgeGlowTop");
			Field egbBottom = c.getDeclaredField("mEdgeGlowBottom");
			egtField.setAccessible(true);
			egbBottom.setAccessible(true);
			Object egtObject = egtField.get(this); // this ָ����ListiVewʵ��
			Object egbObject = egbBottom.get(this);

			// egtObject.getClass() ʵ������һ�� EdgeEffect ������������Ҫ����
			// mGlow mEdge
			// �������������Զ���Drawable����
			Class<?> cc = (Class<?>) Class.forName(egtObject.getClass()
					.getName());
			Field mGlow = cc.getDeclaredField("mGlow");
			mGlow.setAccessible(true);
			mGlow.set(egtObject, new ColorDrawable(Color.TRANSPARENT));
			mGlow.set(egbObject, new ColorDrawable(Color.TRANSPARENT));

			Field mEdge = cc.getDeclaredField("mEdge");
			mEdge.setAccessible(true);
			mEdge.set(egtObject, new ColorDrawable(Color.TRANSPARENT));
			mEdge.set(egbObject, new ColorDrawable(Color.TRANSPARENT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP
				&& getFirstVisiblePosition() == 0) {
			mLastScrollY = getScrollY();
			Log.d("zzw", "" + getScrollY());
			if (!mHandler.hasMessages(MSG_SCROLL_BACK)) {
				mHandler.sendEmptyMessageDelayed(MSG_SCROLL_BACK, 100);
			}
		}
		return super.onTouchEvent(ev);
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// This is where the magic happens, we have replaced the incoming
		// maxOverScrollY with our own custom variable mMaxYOverscrollDistance;
		int newDeltaY = deltaY;
		int delta = (int) (deltaY * SCROLL_RATIO);
		if (delta != 0)
			newDeltaY = delta;
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX,
				mMaxYOverscrollDistance, isTouchEvent);

	}
}