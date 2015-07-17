package com.zhlt.g1app.func;


import java.io.InputStream;

import com.zhlt.g1app.basefunc.BaseFuncView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class SceneAnimation {
	private ImageView mImageView;
	private int[] mFrameRess;
	private int[] mDurations;

	private int mLastFrameNo;
	
	private Handler mHandler;
	
	public SceneAnimation(ImageView pImageView, int[] pFrameRess,
			int[] pDurations, Handler pHandler) {
		mImageView = pImageView;
		mFrameRess = pFrameRess;
		mDurations = pDurations;
		mLastFrameNo = pFrameRess.length - 1;
		mHandler = pHandler;
		BaseFuncView.setBGResource(mImageView,mFrameRess[0]);
		play(1);
	}

	private void play(final int pFrameNo) {
		mImageView.postDelayed(new Runnable() {
			public void run() {
				BaseFuncView.setBGResource(mImageView,mFrameRess[pFrameNo]);
				if (pFrameNo == mLastFrameNo)
				//	play(0);
					mHandler.sendEmptyMessage(0);
				else
					play(pFrameNo + 1);
			}
		}, mDurations[pFrameNo]);
	}
}
