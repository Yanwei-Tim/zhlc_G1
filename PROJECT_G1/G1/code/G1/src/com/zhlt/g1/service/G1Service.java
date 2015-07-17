/**
 * 
 */
package com.zhlt.g1.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhlt.g1.util.InitUtil;
import com.zhlt.g1.util.Log4jUtil;
import com.zhlt.g1.util.MyApplication;
import com.zhlt.g1.util.QueueDataUtil;
import com.zhlt.g1.util.SocketUtil;
import com.zhlt.g1.util.netty.TcpClient;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * 后台服务
 * 
 * @author kenneth
 * @version V1.0 Go baby mobile 深圳中和联创智能科技有限公司
 */
public class G1Service extends Service { // 回调接口的集合
	 private List<Callback> list;
	 ServerSocket server = null;
     private static final  int WIFISTATE=0,SERVERSTATE=1;
	 Logger log = Log4jUtil.getLogger("com.zhlt.g1.service.G1Service");

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return new LocalBinder();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		log.info("后台服务开启");
		list = new ArrayList<Callback>();
		//startwifi();
     /*   try {
            testTakePhone();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //startpc();
	}

	public final class LocalBinder extends Binder {
		public G1Service getService() {
			return G1Service.this;
		}
	}




    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("onStart");
        super.onStart(intent, startId);
    }
	/**
	 * 回调接口
	 * 
	 * @author Ivan Xu
	 * 
	 */
	public interface Callback {
		public void wifiresult(String person, Socket socket);

		public void pcresult(String person, Socket socket);
	}

	/**
	 * 往回调接口集合中添加一个实现类
	 * 
	 * @param callback
	 */
	public void addCallback(Callback callback) {
		list.add(callback);
	}

	boolean socketstate = true;
public  void testTakePhone() throws Exception {

    JSONObject  obj=new JSONObject();
    obj.put("code", 1002);
    obj.put("state",1);// 1,打开，2关闭 3,log
    obj.put("source", 1);//1  wifi手机，2服务器
    System.out.println("11111111111111111111111111");
  //  TcpClient.sendMsg(obj.toString());

}
	public void startpc() {
		MyApplication.getInstance().getFixedThreadPool()
				.execute(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while (socketstate) {
							Socket socket=null;
							try {
								if(InitUtil.DEBUG)log.info("访问服务器:"+InitUtil.SERVERIP+"端口:"+InitUtil.PORTSERVER);
                                System.out.println("11111111111111122222222222222222222222222222222222211111111111");
								socket = new Socket(InitUtil.SERVERIP,InitUtil.PORTSERVER);
                                System.out.println("11111111111111111111111111");
								if(socket!=null) {
                                    System.out.println("lai.e......................................");
                                  //  testTakePhone();
                                    startrunpc(socket, SERVERSTATE);

                                }
							
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								Thread.sleep(InitUtil.SERVERTIME);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
	}

	protected void startrunpc(final Socket socket, final int type)  {






		// TODO Auto-generated method stub

		MyApplication.getInstance().getFixedThreadPool()
				.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						boolean  iswhile=true;
						while (socketstate) {
							try {
								if (socket != null) {
									byte[] bts=SocketUtil
											.readStrFromStreambyte(socket);
									if(bts!=null&&bts.length>0){
									String result = new String(bts)
											.trim();
										if (InitUtil.DEBUG)
											log.info("指令:" + result);
										SocketData sd = new SocketData();
										sd.setResult(result);
										sd.setSocket(socket);
										if(type==WIFISTATE){
										handler.sendMessage(handler
												.obtainMessage(WIFISTATE, sd));
										}else if(type==SERVERSTATE){
											handler.sendMessage(handler
													.obtainMessage(SERVERSTATE, sd));
										}
										break;
									
									}else{
										log.info("已经添加过一次");
										if(iswhile){
											iswhile=false;
										if(type==SERVERSTATE){
											SocketData sd = new SocketData();
											sd.setSocket(socket);
											handler.sendMessage(handler
													.obtainMessage(SERVERSTATE, sd));
											
										}
										}
									}
									
								} else {
									break;
								}
								Thread.sleep(1000);
								log.info("等待命令");
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();

							}

						}
					}
				});
	
	}

	public void startwifi() {

		MyApplication.getInstance().getFixedThreadPool()
				.execute(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while (socketstate) {
							try {
								server = new ServerSocket(InitUtil.PORT);
								// Log4jUtil.getLogger().info("开启监听:" +
								// InitUtil.PORT);
								break;
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								// Log4jUtil.getLogger().info("监听端口错误:" +
								// e1.getStackTrace());
								try {
									Thread.sleep(1000 * 5);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						while (socketstate) {
							try {
								Socket socket = server.accept();
								log.info("wifi接受到命令，开始连接.");
								startrun(socket,WIFISTATE);
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						// handler.sendMessage(handler.obtainMessage(0,
						// "开始一步"));
					}
				});

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WIFISTATE:
				SocketData sdata = (SocketData) msg.obj;

				// 遍历集合，通知所有的实现类，即activity
				for (int i = 0; i < list.size(); i++) {
					list.get(i)
							.wifiresult(sdata.getResult(), sdata.getSocket());
				}
				break;
			case SERVERSTATE:
				SocketData sdata2 = (SocketData) msg.obj;

				// 遍历集合，通知所有的实现类，即activity
				for (int i = 0; i < list.size(); i++) {
					list.get(i)
							.pcresult(sdata2.getResult(), sdata2.getSocket());
				}
				break;

			default:
				break;
			}
		}
	};

	private void startrun(final Socket socket,final int type) {
		MyApplication.getInstance().getFixedThreadPool()
				.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						while (socketstate) {
							try {
								if (socket != null) {
									byte[] bts=SocketUtil
											.readStrFromStreambyte(socket);
									if(bts!=null&&bts.length>0){
									String result = new String(bts)
											.trim();
										if (InitUtil.DEBUG)
											log.info("指令:" + result);
										SocketData sd = new SocketData();
										sd.setResult(result);
										sd.setSocket(socket);
										if(type==WIFISTATE){
										handler.sendMessage(handler
												.obtainMessage(WIFISTATE, sd));
										}else if(type==SERVERSTATE){
											handler.sendMessage(handler
													.obtainMessage(SERVERSTATE, sd));
										}
										break;
									
									}else{
										if(type==SERVERSTATE){
											SocketData sd = new SocketData();
											sd.setSocket(socket);
											handler.sendMessage(handler
													.obtainMessage(SERVERSTATE, sd));
											break;
										}
									}
									
								} else {
									break;
								}
								Thread.sleep(500);
								log.info("等待命令");
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();

							}

						}
					}
				});
	}
     
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		log.error("后台服务关闭");
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	private class SocketData {
		private Socket socket;
		private String result;

		public Socket getSocket() {
			return socket;
		}

		public void setSocket(Socket socket) {
			this.socket = socket;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
	}
}
