package com.zhlt.g1app.func;

import io.netty.channel.MessageSizeEstimator.Handle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.log4j.Logger;

import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.data.DataCommon;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.widget.Toast;

public class NetUtils {

	// 无网络
	public final static int NONE = 0;

	// Wi-Fi
	public final static int WIFI = 1;

	// 3G,GPRS
	public final static int MOBILE = 2;

	private static Logger mLogger = Log4jUtil.getLogger("");

	/**
	 * * 获取当前网络状态
	 * 
	 * @param context
	 * @return
	 */

	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// Wifi网络判断
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return WIFI;
		}
		// 手机网络判断
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return MOBILE;
		}
		return NONE;
	}

	public static boolean isNetWorkConnected(Context context) {
		return getNetworkState(context) != NONE;
	}

	/**
	 * 下载一个URL
	 * 
	 * @param url
	 * @return 成功：下载完成的数据 失败：null
	 */

	public static byte[] downloadURL(String url) {
		return downloadURL(url, 7000);
	}

	public static byte[] downloadURL(String url, int timeout) {
		mLogger.info("Downloading " + url);
		InputStream inputStream = null;
		HttpURLConnection urlConn = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			URL urlStr = new URL(url);
			urlConn = (HttpURLConnection) urlStr.openConnection();
			try {
				urlConn.setConnectTimeout(timeout);
				urlConn.setReadTimeout(timeout);
			} catch (Exception e) {
				return null;
			}
			urlConn.setRequestMethod("GET");
			if (urlConn.getResponseCode() != 200) {
				return null;
			}
			inputStream = urlConn.getInputStream();
			byte b[] = new byte[1024 * 100];
			int i = 0;
			while ((i = inputStream.read(b)) != -1) {
				baos.write(b, 0, i);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (null != urlConn) {
				urlConn.disconnect();
			}
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {
			}
		}
		return baos.toByteArray();
	}

	public static byte[] downloadImage(String url, int timeout) {
		mLogger.info("Downloading " + url);
		InputStream inputStream = null;
		HttpURLConnection urlConn = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int responseCode = -1;
		try {
			URL urlStr = new URL(url);
			urlConn = (HttpURLConnection) urlStr.openConnection();
			try {
				urlConn.setConnectTimeout(timeout);
				urlConn.setReadTimeout(timeout);
			} catch (Exception e) {
				return null;
			}
			urlConn.setRequestMethod("GET");
			responseCode = urlConn.getResponseCode();
			if (responseCode != 200) {
				return null;
			}
			inputStream = urlConn.getInputStream();
			byte b[] = new byte[1024 * 100];
			int i = 0;
			while ((i = inputStream.read(b)) != -1) {
				baos.write(b, 0, i);
			}
		} catch (Exception e) {
			if (responseCode != -1 && responseCode != 200)
				return new byte[0];
			return null;
		} finally {
			if (null != urlConn) {
				urlConn.disconnect();
			}
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {
			}
		}
		return baos.toByteArray();
	}

	public static String dopost(String url, Map<String, String> parmas) {
		String result = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 4000);
		HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
		HttpPost httpPost = new HttpPost(url.replace(" ", "%20"));
		mLogger.info(url);
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		if (parmas != null) {
			Set<String> keys = parmas.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = i.next();
				pairs.add(new BasicNameValuePair(key, parmas.get(key)));
			}
		}
		try {
			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs,
					"utf-8");
			httpPost.setEntity(p_entity);
			HttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			result = NetUtils.convertStreamToString(content);

		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将流转成字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * http post请求
	 * 
	 * @param urlStr
	 *            URL地址
	 * @throws Exception
	 */
	public static void HttpRequestPost(final String urlStr, final Handler mHandle)
			throws Exception {
		new Thread(new Runnable() {

			@Override
			public void run() {
				mLogger.info("zzw:"+"HttpRequestPost : " + urlStr);
				StringBuffer buffer = new StringBuffer();
				try {
					URL url = new URL(urlStr);
					HttpURLConnection httpConn = (HttpURLConnection) url
							.openConnection();

					// 设置是否向connection输出，因为是post请求，参数要放在 http正文内，因此需要设为true
					httpConn.setDoOutput(true);

					// 是否向connection输入，默认为true
					httpConn.setDoInput(true);

					// POST请求方式
					httpConn.setRequestMethod("POST");

					// post请求不能使用缓存
					httpConn.setUseCaches(false);

					// 是否自动执行重定向,默认为true
					httpConn.setInstanceFollowRedirects(true);

					// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的意思是正文是urlencoded编码过的form参数
					// 下面我们可以看到我们对正文内容使用URLEncoder.encode 进行编码
					httpConn.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");

					// 连接，从openConnection()至此的配置必须要在connect之前完成，
					// 注意的是connection.getOutputStream会隐含的进行connect。
					httpConn.connect();

					/*
					 * DataOutputStream dos = new
					 * DataOutputStream(httpConn.getOutputStream());
					 * 
					 * //发送正文，正文内容其实跟get的URL中'?'后的参数字符串一致
					 * word=wait&tn=news&from=news String content = "filePath="
					 * + URLEncoder.encode("设计资料/设计", "utf-8");
					 * 
					 * //DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
					 * dos.writeBytes(content);
					 * 
					 * //刷新流 dos.flush();
					 * 
					 * //关闭流 dos.close();
					 */

					// 取得输入流，并使用Reader读取
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(httpConn.getInputStream()));

					String lines;
					while ((lines = reader.readLine()) != null) {
						buffer.append(lines);
					}
					reader.close();
					httpConn.disconnect();
				} catch (Exception e) {
					mLogger.info("zzw:"+e.toString());
					e.printStackTrace();
				}
				mLogger.info("zzw:"+buffer.toString());
				mHandle.sendMessage(mHandle.obtainMessage(
						DataCommon.Message.MSG_LOAD_FINISHED, buffer.toString()));
			}
		}).start();

	}
}
