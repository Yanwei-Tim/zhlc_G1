package com.zhlt.g1app.func;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.View;

/**
 * @Description 图片加载和缓存类
 * @author qing
 * @date 2014-2-10
 */
public class BitmapCache {

	private static BitmapCache bitmapCache;
	private Context mContext;
	private LruCache<String, Bitmap> cache;
	private Bitmap loadingBitmap;
	private ExecutorService bitmapLoadAndDisplayExecutor;

	/**
	 * 设置加载中显示的图片
	 * 
	 * @param resID
	 */
	public void setLoadingBitmap(int resID) {
		loadingBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				resID);
	}

	/**
	 * 设置加载中显示的图片
	 * 
	 * @param bitmap
	 */
	public void setLoadingBitmap(Bitmap bitmap) {
		loadingBitmap = bitmap;
	}

	/**
	 * 清空缓存
	 */
	@SuppressLint("NewApi")
	public void clean() {
		cache.evictAll();
	}

	public static BitmapCache getSingleTon(Context context) {
		if (bitmapCache == null)
			bitmapCache = new BitmapCache(context);
		return bitmapCache;
	}

	@SuppressLint("NewApi")
	private BitmapCache(Context context) {
		mContext = context;
		int cacheSize = 1024 * 1024 * ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() / 8;
		cache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
		bitmapLoadAndDisplayExecutor = Executors.newFixedThreadPool(3,
				new ThreadFactory() {

					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(r);
						t.setPriority(Thread.NORM_PRIORITY - 1);
						return t;
					}
				});
	}

	private int bitmapWidth;
	private int bitmapHeight;

	/**
	 * 设置图片显示大小
	 * 
	 * @param width
	 * @param height
	 */
	public void configBitmapSize(int width, int height) {
		bitmapWidth = width;
		bitmapHeight = height;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void loadBitmap(View view, String url, IBitmapCacheCallback callback) {
		Bitmap bitmap = null;
		bitmap = cache.get(url);
		if (bitmap != null) {
			view.setBackgroundDrawable(new BitmapDrawable(mContext
					.getResources(), bitmap));
		} else if (checkImageTask(url, view)) {
			final BitmapLoadAndDisplayTask task = new BitmapLoadAndDisplayTask(
					view, bitmapWidth, bitmapHeight, callback);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					mContext.getResources(), loadingBitmap, task);
			view.setBackgroundDrawable(asyncDrawable);
			Log.d("3dvstar", "3dvstar  loadBitmap");
			task.executeOnExecutor(bitmapLoadAndDisplayExecutor, url);
		}
	}

	private boolean checkImageTask(Object data, View view) {
		final BitmapLoadAndDisplayTask bitmapTask = getBitmapTaskFromImageView(view);
		if (bitmapTask != null) {
			final Object bitmapData = bitmapTask.data;
			if (bitmapData == null || !bitmapData.equals(data)) {
				bitmapTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private BitmapLoadAndDisplayTask getBitmapTaskFromImageView(View imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getBackground();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	public static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapLoadAndDisplayTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapLoadAndDisplayTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapLoadAndDisplayTask>(
					bitmapWorkerTask);
		}

		public BitmapLoadAndDisplayTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	private static final int MAXRETRY = 5;

	private Bitmap loadBitmap(String url, int width, int height,
			IBitmapCacheCallback callback) {
		Bitmap bitmap = null;
		String localPath = callback.getLocalPathnameByDummy(url);
		int retryCount = 0;
		while (retryCount < MAXRETRY && bitmap == null) {
			try {
				if (width == 0 || height == 0) {
					bitmap = BitmapFactory.decodeFile(localPath);
				} else {
					Options options = new Options();
					options.inJustDecodeBounds = true;
					options.inPurgeable = true;
					BitmapFactory.decodeFile(localPath, options);
					options.inSampleSize = calculateInSampleSize(options,
							width, height);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeFile(localPath, options);
				}
			} catch (Exception e) {
			}
			retryCount++;
		}

		if (bitmap == null) {
			retryCount = 0;
			byte[] data = null;
			FileOutputStream fos = null;
			while (retryCount < MAXRETRY && bitmap == null) {
				try {
					data = NetUtils.downloadImage(url, 5000);
					if (width == 0 || height == 0) {
						bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
					} else {
						Options options = new Options();
						options.inJustDecodeBounds = true;
						options.inPurgeable = true;
						BitmapFactory.decodeByteArray(data, 0, data.length,
								options);
						options.inSampleSize = calculateInSampleSize(options,
								width, height);
						options.inJustDecodeBounds = false;
						bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length, options);
					}
					File file = new File(localPath);
					if (!file.exists())
						file.createNewFile();
					fos = new FileOutputStream(file);
					fos.write(data, 0, data.length);
				} catch (Exception e) {
				} finally {
					try {
						if (fos != null)
							fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				retryCount++;
			}
		}
		if (bitmap != null) {
			bitmap = callback.processBitmap(bitmap);
		}
		return bitmap;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			final float totalPixels = width * height;

			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	private class BitmapLoadAndDisplayTask extends
			AsyncTask<Object, Void, Bitmap> {
		private Object data;
		private final WeakReference<View> viewReference;
		private final int width;
		private final int height;
		private final IBitmapCacheCallback callback;

		public BitmapLoadAndDisplayTask(View view, int width, int height,
				IBitmapCacheCallback callback) {
			this.viewReference = new WeakReference<View>(view);
			this.width = width;
			this.height = height;
			this.callback = callback;
		}

		@SuppressLint("NewApi")
		@Override
		protected Bitmap doInBackground(Object... params) {
			data = params[0];
			final String dataString = String.valueOf(data);
			Bitmap bitmap = null;
			if (bitmap == null && !isCancelled()
					&& getAttachedImageView() != null) {
				bitmap = loadBitmap(dataString, width, height, callback);
			}

			if (bitmap != null) {
				cache.put(dataString, bitmap);
			}
			return bitmap;
		}

		private View getAttachedImageView() {
			final View imageView = viewReference.get();
			final BitmapLoadAndDisplayTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);
			return this == bitmapWorkerTask ? imageView : null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Bitmap result) {
			if (isCancelled())
				result = null;
			final View view = getAttachedImageView();
			if (result != null && view != null) {
				final TransitionDrawable td = new TransitionDrawable(
						new Drawable[] { new ColorDrawable(Color.TRANSPARENT),
								new BitmapDrawable(view.getResources(), result) });
				view.setBackgroundDrawable(td);
				td.startTransition(300);
			}
		}
	}

	public interface IBitmapCacheCallback {
		/**
		 * // 由URL计算本地储存路径
		 * 
		 * @param URL
		 * @return
		 */
		String getLocalPathnameByDummy(String URL);

		Bitmap processBitmap(Bitmap bitmap);
	}
}
