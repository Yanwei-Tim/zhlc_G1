package com.zhlt.g1app.car;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhlt.g1app.R;
import com.zhlt.g1app.R.layout;
import com.zhlt.g1app.util.InitUtil;
import com.zhlt.g1app.util.Log4jUtil;
import com.zhlt.g1app.util.MyApplication;
import com.zhlt.g1app.util.SocketUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static Logger log = Log4jUtil.getLogger("car");
      TextView  tv=null;
      ImageView imageView1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv=(TextView)findViewById(R.id.textView1);
		imageView1=(ImageView)findViewById(R.id.imageView1);
		if (InitUtil.DEBUG)
			log.info("APP日志");
	}

	public void butonclick(View v) {
		switch (v.getId()) {
		case R.id.button1:
           //开始发送指令
			startsensor(InitUtil.CODE7);
			break;
		case R.id.button2:
	           //开始发送指令
			startsensor(InitUtil.CODE6);
				
				break;
		case R.id.button3:
	           //开始发送指令
			startsensor(InitUtil.CODE5);
				
				break;
		case R.id.button4:
	           //开始发送指令
			startsensor(InitUtil.CODE2);
				
				break;
		default:
			break;
		}
	}

	 private Handler mhandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				tv.setText(msg.obj.toString());
				break;
			case 2:
				 Bitmap  bm=(Bitmap) msg.obj;
				 Matrix matrix = new Matrix();
  				matrix.preRotate(90);
  				bm = Bitmap.createBitmap(bm, 0, 0,
  						bm.getWidth(), bm.getHeight(),
  						matrix, true);
				imageView1.setImageBitmap(bm);
				break;
			case 3:
                JSONObject obj=null;
				try {
					obj = new JSONObject(msg.obj.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
       		    String path=obj.optString("data");
				getphoto(path);
       		 
       	 
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		 
		 
	 };
	 
	 private void getphoto(final String path){

			// TODO Auto-generated method stub
			MyApplication.getInstance().getFixedThreadPool().execute(new Runnable() {
				
				@Override
				public void run() {
					Socket request = null;
					try {
						
						request = new Socket("192.168.43.1", 9999);
				
						JSONObject  obj=new JSONObject();
						obj.put("code", InitUtil.CODE12);
						obj.put("state",1);// 1,打开，2关闭 3,log
						obj.put("source", 1);//1  wifi手机，2服务器
						obj.put("data", path);//返回的图片地址
						String codestr=obj.toString().trim();
						while(true) {
							// write response info
							
						   if(codestr!=null){
							SocketUtil.writeStr2StreamVideo(codestr.getBytes(),
									request.getOutputStream());
							if (InitUtil.DEBUG)log.info("发送指令:"+codestr);
							codestr=null;
							
							}
							// get info from request when getting a socket request
							byte[] reqStr = SocketUtil.readStrFromStreamdatabyte(request
									.getInputStream());
			                 if(reqStr!=null&&reqStr.length>0){
			                	 if (InitUtil.DEBUG)log.info("接受到图片:"+reqStr.length);
			               		 Bitmap  bitmap=BitmapFactory.decodeByteArray(reqStr, 0, reqStr.length);
			            		     if(bitmap!=null){
			            			 mhandler.obtainMessage(2, bitmap).sendToTarget();
			            			 break;
			            		     }
			                 }else{
			                	 if (InitUtil.DEBUG)log.info("=====等于null");
			                 }
						    Thread.sleep(1000);
						}
					} catch (IOException e) {
						if (InitUtil.DEBUG)log.error("G-sensor：指令"+e.getStackTrace());
					} catch (Throwable e) {
						if (InitUtil.DEBUG)log.error("G-sensor：指令"+e.getStackTrace());
					} finally {
						if (request != null) {
							try {
								//request.getInputStream().close();
								//request.getOutputStream().close();
								request.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				
					
				}
			});
			
		
		 
		
	 }
	 
	 
	private void startsensor(final int code) {
		// TODO Auto-generated method stub
		MyApplication.getInstance().getFixedThreadPool().execute(new Runnable() {
			
			@Override
			public void run() {
				Socket request = null;
				try {
					
					request = new Socket("192.168.43.1", 9999);
			
					JSONObject  obj=new JSONObject();
					obj.put("code", code);
					obj.put("state",1);// 1,打开，2关闭 3,log
					obj.put("source", 1);//1  wifi手机，2服务器
					if(code==InitUtil.CODE6){
					JSONObject  objjt=new JSONObject();
					objjt.put("start", "2015-06-01 09:09:00");//查询历史记录的时候用得，开始时间
					objjt.put("end", "2015-06-09 09:09:00");//结束时间
					obj.put("data",objjt);  //传递的参数
					}
					String codestr=obj.toString().trim();
					while(true) {
						// write response info
						
					   if(codestr!=null){
						SocketUtil.writeStr2StreamVideo(codestr.getBytes(),
								request.getOutputStream());
						if (InitUtil.DEBUG)log.info("发送指令:"+codestr);
						codestr=null;
						
						}
						// get info from request when getting a socket request
						byte[] reqStr = SocketUtil.readStrFromStreamdatabyte(request
								.getInputStream());
		                 if(reqStr!=null&&reqStr.length>0){
		                	 
		                	 String result=new String(reqStr).trim();
		                	 if (InitUtil.DEBUG) log.info("接收到:"+result);
		                	 if(result!=null&&!result.equals("")&&!result.equals("null"))
		                	 {
		                		 if(code==InitUtil.CODE2){
			                		 mhandler.obtainMessage(3,result).sendToTarget();
			                	 }else{
		                	     mhandler.obtainMessage(1, result).sendToTarget();
			                	 }
		                		 break;
		                	   }
		                	 
						     //mhandler.obtainMessage(3,reqStr).sendToTarget();
		                 }else{
		                	 System.out.println("=====等于null");
		                 }
					    Thread.sleep(1000);
					}
				} catch (IOException e) {
					if (InitUtil.DEBUG)log.error("G-sensor：指令"+e.getLocalizedMessage());
				} catch (Throwable e) {
					if (InitUtil.DEBUG)log.error("G-sensor：指令"+e.getLocalizedMessage());
				} finally {
					if (request != null) {
						try {
							//request.getInputStream().close();
							//request.getOutputStream().close();
							request.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			
				
			}
		});
		
	}
}
