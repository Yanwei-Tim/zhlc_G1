package com.zhlt.g1.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


import android.os.Environment;

public class FileUtil {

	public static File getimagefile() {
	/*	boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		String str = "";
		if (sdCardExist) {
			str = Environment.getExternalStorageDirectory().getPath();// ��ȡ��Ŀ¼
		}*/
		String str=Environment.getExternalStorageDirectory().getPath();
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
		//Log4jUtil.getLogger().info("图片地址"+file.getPath());
		return file;
	}

	public static File getvideofile() {
		String str=Environment.getExternalStorageDirectory().getPath();
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
		//File file = new File(file1.getPath() + "/zhltvideo"
		//		+ System.currentTimeMillis() + ".h264");
		//Log4jUtil.getLogger().info("录像地址"+file.getPath());
		return file;
	}
	public static String getlog4jfile() {
		String str=Environment.getExternalStorageDirectory().getPath();
		File file1 = new File(str + "/zhlt");
		if (!file1.exists() && !file1.isDirectory()) {
			file1.mkdirs();
		}
		File file2 = new File(str + "/zhlt/log");
		if (!file2.exists() && !file2.isDirectory()) {
			file2.mkdirs();
		}
		File file = new File(file2.getPath() + "/G1log"
				+ TimeUtil.getTime(TimeUtil.TIME1)+".log");
		return file.getAbsolutePath();
	}

    public static String post(String actionUrl,Map<String, String> params,Map<String, File> files) throws IOException {

        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(200 * 1000);
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);
        conn.setRequestMethod("POST"); // Post方式
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);
        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\""
                    + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }
        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: multipart/form-data; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }
        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        // 得到响应码
        boolean success = conn.getResponseCode()==200;
        String data = "";
        if(success){
            InputStream in = conn.getInputStream();
            InputStreamReader isReader = new InputStreamReader(in);
            BufferedReader bufReader = new BufferedReader(isReader);
            String line = null;

            while ((line = bufReader.readLine()) != null)
                data += line;

            outStream.close();
            conn.disconnect();
            System.out.println("----data="+data);
        }
        return data;
    }
}
