package com.zhlt.g1app.basefunc;

import java.io.File;

import org.apache.log4j.Logger;

import com.zhlt.g1app.data.DataCommon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class FileUtil {
	private static Logger gLogger = Log4jUtil.getLogger("FileUtil");

	public static File getimagefile() {
		/*
		 * boolean sdCardExist = Environment.getExternalStorageState().equals(
		 * android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ���� String str =
		 * ""; if (sdCardExist) { str =
		 * Environment.getExternalStorageDirectory().getPath();// ��ȡ��Ŀ¼ }
		 */
		String str = Environment.getExternalStorageDirectory().getPath();
		File file1 = new File(str + "/zhlt");
		if (!file1.exists() && !file1.isDirectory()) {
			file1.mkdirs();
		}
		File file2 = new File(str + "/zhlt/img");
		if (!file2.exists() && !file2.isDirectory()) {
			file2.mkdirs();
		}

		File file = new File(file2.getPath() + "/zhltvoid"
				+ System.currentTimeMillis() + ".jpg");
		if (InitUtil.DEBUG)
			gLogger.info("图片地址" + file.getPath());
		return file;
	}

	public static Bitmap getBitmapFromUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		File file = new File(DataCommon.Image_Community_Path
				+ url.substring(url.lastIndexOf("/") + 1));
		if (file.exists()) {
			Log.d("zzw",
					DataCommon.Image_Community_Path
							+ url.substring(url.lastIndexOf("/") + 1)
							+ " exists");
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		return null;

	}

	public static File getvideofile() {
		String str = Environment.getExternalStorageDirectory().getPath();
		File file1 = new File(str + "/zhlt");
		if (!file1.exists() && !file1.isDirectory()) {
			file1.mkdirs();
		}
		File file2 = new File(str + "/zhlt/video");
		if (!file2.exists() && !file2.isDirectory()) {
			file2.mkdirs();
		}
		File file = new File(file2.getPath() + "/zhltvoid"
				+ System.currentTimeMillis() + ".3gp");
		// File file = new File(file1.getPath() + "/zhltvideo"
		// + System.currentTimeMillis() + ".h264");
		if (InitUtil.DEBUG)
			gLogger.info("录像地址" + file.getPath());
		return file;
	}

	public static String getlog4jfile() {
		String str = Environment.getExternalStorageDirectory().getPath();
		File file1 = new File(str + "/zhlt");
		if (!file1.exists() && !file1.isDirectory()) {
			file1.mkdirs();
		}
		File file2 = new File(str + "/zhlt/log");
		if (!file2.exists() && !file2.isDirectory()) {
			file2.mkdirs();
		}

		File file = new File(file2.getPath() + "/G1_APPlog"
				+ TimeUtil.getTime() + ".log");

		return file.getAbsolutePath();
	}

	/**
	 * 根据url得到本地的图片路径
	 * 
	 * @param url
	 * @return
	 */
	public static String getImageLocalPath(String imageURL) {
		if (TextUtils.isEmpty(imageURL)) {
			return "";
		}
		File cacheDir = new File(DataCommon.Image_Community_Path);
		if (!cacheDir.exists() && !cacheDir.isDirectory()) {
			cacheDir.mkdirs();
		}

		return DataCommon.Image_Community_Path
				+ imageURL.substring(imageURL.lastIndexOf("/") + 1);
	}

}
