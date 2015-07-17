package com.zhlt.g1app.basefunc;

import java.io.File;

import org.apache.log4j.Logger;

import android.os.Environment;

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

}
