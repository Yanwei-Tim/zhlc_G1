package com.example.mytest;

import io.netty.channel.ChannelHandlerContext;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.netty.channel.ChannelHandlerContext;

public class MainActivity extends Activity implements
		TcpClientHandler.Callback, View.OnClickListener {
	TcpClientHandler clientListen;
	TcpClient client;
	String Data;

	Button RegisterBt;
	Button SendDataBt;
	/**
	 * 是否已经上线
	 */
	boolean islogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ButterKnife.inject(this);
		RegisterBt = (Button) findViewById(R.id.button1);
		SendDataBt = (Button) findViewById(R.id.button2);

		RegisterBt.setOnClickListener(this);
		SendDataBt.setOnClickListener(this);
		clientListen = new TcpClientHandler();
		clientListen.setListener(this);
	       new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				client = new TcpClient();
				client.CreatServerConnect(clientListen);
			}
		}).start();
	}

	public void RegisterCar() throws Exception {
		// 用户APP 上线
		JSONObject obj = new JSONObject();
		obj.put("code", Codes.CODE1000);
		obj.put("u_id", 1);// 1,打开，2关闭 3,log
		obj.put("source", 1);// 1 wifi手机，
		obj.put("key", "123");// 1 wifi手机，
		System.out.println("operationComplete ......................"+obj);
		client.sendMsg(obj.toString());
	}

	/*
	 * public void testTakePhone() throws Exception {
	 * 
	 * JSONObject obj=new JSONObject(); obj.put("code", Codes.CODE1001);
	 * obj.put("state",1);// 1,打开，2关闭 3,log obj.put("source", 1);//1 wifi手机，2服务器
	 * System.out.println("operationComplete ......................");
	 * client.sendMsg(obj.toString()); }
	 */
	public void testGetAllData() throws Exception {
		if (islogin) {
			JSONObject obj = new JSONObject();
			obj.put("code", Codes.CODE6001);
			obj.put("state", 1);// 1,打开，2关闭 3,log
			obj.put("source", 1);// 1 wifi手机，2服务器
			System.out.println("operationComplete ......................");
			client.sendMsg(obj.toString());
		} else {
              Toast.makeText(MainActivity.this, "请先上线", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void NettyResult(ChannelHandlerContext ctx, Object msg) {

		System.out.println("client接收到服务器返回的消息:" + msg.toString());
		JSONObject json = null;
		if (msg != null) {

			try {
				json = new JSONObject(msg.toString().trim());

				int code = json.optInt("code");
				System.out.println("code =======" + code);
				switch (code) {
				case Codes.CODE1000:
					// 上线
					int state = json.optInt("state");
					switch (state) {
					case 1:
						islogin = true;
						System.out.println("上线成功!");
						mHandler.obtainMessage(Codes.CODE1000).sendToTarget();
						break;
					case 2:
						islogin = false;
						System.out.println("上线失败!");
								
						break;
					case 3:
						islogin = false;
						System.out.println("设备不在线!");
						break;
					case 4:
						islogin = false;
						System.out.println("网络异常!");
						break;

					default:
						break;
					}
					break;
				case Codes.CODE6001:
					// 上线
					int state6001 = json.optInt("state");
					switch (state6001) {
					case 1:
						String msg6001=json.optString("msg");
						System.out.println("返回数据成功"+msg6001);
						mHandler.obtainMessage(Codes.CODE6001,msg6001).sendToTarget();
						break;
					case 2:
					
						System.out.println("返回失败!");
						break;
				

					default:
						break;
					}
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("client接收到服务器返回的消息:null");
			// startupgps(Codes.CODE1005, socket);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			try {
				RegisterCar();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.button2:
			try {
				testGetAllData();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		/*
		 * case R.id.button3: try { testTakePhone(); } catch (Exception e) {
		 * e.printStackTrace(); } break;
		 */
		default:
			break;

		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case Codes.CODE1000:
				Toast.makeText(getApplicationContext(), "连接成功",
						Toast.LENGTH_SHORT).show();
				break;
			case Codes.CODE4001:
				Toast.makeText(getApplicationContext(), "设备不在线",
						Toast.LENGTH_SHORT).show();
				break;
			case Codes.CODE6001:
				Toast.makeText(MainActivity.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case Codes.CODE1001:
				Toast.makeText(getApplicationContext(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case Codes.CODE5000:
				Toast.makeText(getApplicationContext(), "拍照成功，存储PC",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}

		;
	};
}
