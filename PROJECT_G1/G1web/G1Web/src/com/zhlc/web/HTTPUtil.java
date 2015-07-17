package com.zhlc.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author：anquan <br>
 * desc： <br>
 * date： 2015-7-3 下午4:17:34<br>
 */
public class HTTPUtil {
	
	 /** 
     * http post请求 
     * @param urlStr URL地址 
     * @throws Exception 
     */  
    private static String HttpRequestPost(String urlStr) throws Exception{  
        StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(urlStr);  
			HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();  
			  
			//设置是否向connection输出，因为是post请求，参数要放在 http正文内，因此需要设为true  
			httpConn.setDoOutput(true);  
			  
			//是否向connection输入，默认为true  
			httpConn.setDoInput(true);  
			  
			//POST请求方式  
			httpConn.setRequestMethod("POST");  
			  
			//post请求不能使用缓存  
			httpConn.setUseCaches(false);  
			  
			//是否自动执行重定向,默认为true  
			httpConn.setInstanceFollowRedirects(true);  
			  
			//配置本次连接的Content-type，配置为application/x-www-form-urlencoded的意思是正文是urlencoded编码过的form参数  
			//下面我们可以看到我们对正文内容使用URLEncoder.encode 进行编码  
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
			  
			//连接，从openConnection()至此的配置必须要在connect之前完成， 注意的是connection.getOutputStream会隐含的进行connect。  
			httpConn.connect();  
			
			/*
			DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());  
			  
			//发送正文，正文内容其实跟get的URL中'?'后的参数字符串一致 word=wait&tn=news&from=news  
			String content = "filePath=" + URLEncoder.encode("设计资料/设计", "utf-8");  
			  
			//DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面  
			dos.writeBytes(content);   
			  
			//刷新流  
			dos.flush();  
			  
			//关闭流  
			dos.close();  
			*/
			  
			// 取得输入流，并使用Reader读取  
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));  
			  
			String lines; 
			while ((lines = reader.readLine()) != null) {  
				buffer.append(lines);
			}  
			reader.close();  
			httpConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return buffer.toString();
    }
    
	public static void main(String[] args) {
		try {
			System.out.println(HttpRequestPost("http://192.168.1.77:8080/G1Web/util/sendMsg?data={\"Mobile\":\"18123672593\"}"));
			//httpRequestPost("http://192.168.1.38:8080/G1Web/user/sign?data={\"user_name\":\"18123672593\",\"user_pass\":\"123456\",\"car_type\":\"1\",\"type\":\"1\"}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
