package com.zhlt.g1app.basefunc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.zhlt.g1app.R;

public class BitmapHelp {

	private static BitmapHelp mBitmapHelp;
	private BitmapUtils mBitmapUtils;
	private BitmapDisplayConfig mBitmapDisplayConfig;

	public static BitmapHelp getBitmapHelp(Context context) {
		if (mBitmapHelp == null) {
			mBitmapHelp = new BitmapHelp(context);
		}
		return mBitmapHelp;
	}

	public BitmapDisplayConfig getBitmapDisplayConfig() {
		return mBitmapDisplayConfig;
	}

	public BitmapUtils getBitmapUtils() {
		return mBitmapUtils;
	}

	public void setBitmapConif(int width, int height) {
		if (mBitmapDisplayConfig != null)
			mBitmapDisplayConfig
					.setBitmapMaxSize(new BitmapSize(width, height));
	}

	private BitmapHelp(Context context) {
		mBitmapUtils = new BitmapUtils(context);
		mBitmapDisplayConfig = new BitmapDisplayConfig();
		mBitmapDisplayConfig.setLoadingDrawable(context.getResources()
				.getDrawable(R.drawable.bg_dark));
		mBitmapDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
	}
}
