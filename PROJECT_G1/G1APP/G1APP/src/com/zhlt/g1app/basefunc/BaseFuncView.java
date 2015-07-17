package com.zhlt.g1app.basefunc;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;


public class BaseFuncView {

	@SuppressWarnings("deprecation")
	public static void setBGResource(View pView, int pRsid){
		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;

		InputStream is = pView.getContext().getResources().openRawResource(pRsid);

		Bitmap mBgBitmap = BitmapFactory.decodeStream(is, null, opt);
		pView.setBackgroundDrawable(new BitmapDrawable(mBgBitmap));
	}
}
