/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.zhlt.g1app.view;

import com.zhlt.g1app.R;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewXListHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	// private ProgressBar mProgressBar;
	// private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	private Animation mEatAnim; // 吃豆动画
	private Animation mCatchAnim;// 追赶动画

	private Animation mRotateDownAnim;

	private ImageView point1;
	private ImageView point2;
	private ImageView point3;
	private ImageView point4;
	private ImageView hello;
	private ImageView shake;
	private int mPoint1X = 0;
	private int mPoint2X = 0;
	private int mPoint3X = 0;
	private int mFromX = 0;
	private int mPoint4X = 0;
	private boolean mIsAnimOn = false;

	private int mCurrentAnimTime = 0;
	private int mScreenWidth = 0;

	private final int ROTATE_ANIM_DURATION = 1000;
	private final int DISPPEAR_DELAY = ROTATE_ANIM_DURATION / 4;
	private final int REFERSHOVER_DELAY = 500;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
	private Handler mHandler;

	public ViewXListHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ViewXListHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 鍒濆鎯呭喌锛岃缃笅鎷夊埛鏂皏iew楂樺害涓�
		mHandler = new Handler();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		mScreenWidth = display.getWidth();
		point1 = (ImageView) mContainer
				.findViewById(R.id.xlistview_header_point1);
		point2 = (ImageView) mContainer
				.findViewById(R.id.xlistview_header_point2);
		point3 = (ImageView) mContainer
				.findViewById(R.id.xlistview_header_point3);
		point4 = (ImageView) mContainer
				.findViewById(R.id.xlistview_header_point4);
		hello = (ImageView) mContainer
				.findViewById(R.id.xlistview_header_hello);
		shake = (ImageView) mContainer
				.findViewById(R.id.xlistview_header_shake);
		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	private void initAnim() {
		if (mEatAnim == null) {
			Log.d("zzw", "initAnim");
			mEatAnim = new TranslateAnimation(0, mPoint4X - mFromX, 0, 0);
			mEatAnim.setDuration(ROTATE_ANIM_DURATION);
			mEatAnim.setFillAfter(true);
			mEatAnim.setAnimationListener(mAnimationListener);

			mCatchAnim = new TranslateAnimation(0, mScreenWidth, 0, 0);
			mCatchAnim.setDuration(ROTATE_ANIM_DURATION);
			mCatchAnim.setFillAfter(true);
			mCatchAnim.setAnimationListener(mCatchAnimationListener);

		}

	}

	private AnimationListener mCatchAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			Log.d("zzw", "onAnimationStart");
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			hello.setVisibility(View.GONE);
			shake.setVisibility(View.GONE);
		}
	};

	private AnimationListener mAnimationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			Log.d("zzw", "onAnimationStart");
			mIsAnimOn = true;
			mCurrentAnimTime = 0;
			mHandler.postDelayed(mPointRunnable, DISPPEAR_DELAY);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mArrowImageView.setVisibility(View.INVISIBLE);

		}
	};

	private Runnable mPointRunnable = new Runnable() {

		@Override
		public void run() {

			mCurrentAnimTime += DISPPEAR_DELAY;
			if (mCurrentAnimTime == DISPPEAR_DELAY) {
				point1.setVisibility(View.INVISIBLE);
			} else if (mCurrentAnimTime == 2 * DISPPEAR_DELAY) {
				point2.setVisibility(View.INVISIBLE);
			} else if (mCurrentAnimTime == 3 * DISPPEAR_DELAY) {
				point3.setVisibility(View.INVISIBLE);
			} else if (mCurrentAnimTime == 4 * DISPPEAR_DELAY) {
				point4.setVisibility(View.INVISIBLE);
			}
			if (mCurrentAnimTime < 4 * DISPPEAR_DELAY) {
				mHandler.postDelayed(mPointRunnable, DISPPEAR_DELAY);
			}

		}
	};

	private void resetAnim() {
		point1.setVisibility(View.VISIBLE);
		point2.setVisibility(View.VISIBLE);
		point3.setVisibility(View.VISIBLE);
		point4.setVisibility(View.VISIBLE);
		mArrowImageView.setVisibility(View.VISIBLE);
	}

	private void finishAnim() {
		point1.setVisibility(View.INVISIBLE);
		point2.setVisibility(View.INVISIBLE);
		point3.setVisibility(View.INVISIBLE);
		point4.setVisibility(View.INVISIBLE);
		mArrowImageView.setVisibility(View.GONE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mFromX = mArrowImageView.getLeft();

		mPoint1X = point1.getLeft();
		mPoint2X = point2.getLeft();
		mPoint3X = point3.getLeft();
		mPoint4X = point4.getLeft();
		Log.d("zzw", "onMeasure mFromX:" + mFromX + " mToX" + mPoint4X);
		initAnim();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private void refreshStart() {
		hello.setVisibility(View.VISIBLE);
		shake.setVisibility(View.VISIBLE);
		// 当吃豆动画还没完成，要手动让豆豆消失
		if (mCurrentAnimTime != 4 * DISPPEAR_DELAY) {
			finishAnim();
		}
		shake.startAnimation(mCatchAnim);
		hello.startAnimation(mCatchAnim);
	}

	public void refreshEnd() {
		mHandler.postDelayed(mRefreshOverRunnable, REFERSHOVER_DELAY);
	}

	private Runnable mRefreshOverRunnable = new Runnable() {

		@Override
		public void run() {
			resetAnim();
			hello.setVisibility(View.GONE);
			shake.setVisibility(View.GONE);
		}
	};

	public void setState(int state) {
		// if (state == mState)
		// return;

		if (state == STATE_REFRESHING) { // 鏄剧ず杩涘害
			mArrowImageView.clearAnimation();
			// mArrowImageView.setVisibility(View.INVISIBLE);
			refreshStart();
			// mProgressBar.setVisibility(View.VISIBLE);
		}
		// else { // 鏄剧ず绠ご鍥剧墖
		// mArrowImageView.setVisibility(View.VISIBLE);
		// // mProgressBar.setVisibility(View.INVISIBLE);
		// }

		switch (state) {
		case STATE_NORMAL:
			// if (mState == STATE_READY) {
			if (mIsAnimOn == false)
				mArrowImageView.startAnimation(mEatAnim);
			// }
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			// mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				// mArrowImageView.clearAnimation();
				mIsAnimOn = false;
				// mArrowImageView.startAnimation(mRotateUpAnim);
				// mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			// mHintTextView.setText(R.string.xlistview_header_hint_loading);

			break;
		default:
		}

		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getLayoutParams().height;
	}

}
