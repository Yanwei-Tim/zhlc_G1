package com.zhlc.g1.car.util;

/**
 * 
 ** Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd
 ** All rights reserved.
 ** http://www.gobabymobile.cn/
 ** File: - MainActivity.java
 ** Description:描述
 **     
 **
 ** ------------------------------- Revision History: -------------------------------------
 ** <author>                             <data>             <version>            <desc>
 ** ---------------------------------------------------------------------------------------
 ** duanpengfei@gobabymobile.cn        2015-6-29 上午11:42:26     1.0         Create this moudle
 */

import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.zhlc.g1.ServerMain;
import com.zhlc.g1.bean.CameraPhoto;
import com.zhlc.g1.bean.GPSVO;
import com.zhlc.g1.car.guice.service.ICarService;
import com.zhlc.g1.util.Codes;
import com.zhlc.g1.util.DeviceAppAdapter;
import com.zhlc.g1.util.InitUtil;
import com.zhlc.g1.util.JSONUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpServerHandlerCar extends ChannelInboundHandlerAdapter {
	static Logger logger = Logger.getLogger("TcpServerHandlerCar");
	String imei = null, key = null;
	int u_id = 0;
	// 检查 用户
	boolean ischeck = true;
	// 是否添加了设备
	boolean isadd = false;
	/**
	 * 是否已经添加了未激活设备
	 */
	boolean ispendingDevices = false;

	public ServerMain msm = null;
	int source = 0;

	@Inject
	ICarService icars;

	public static boolean isJson(String value) {
		try {
			new JSONObject(value);
		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		logger.info("TCP=" + msg.toString());

		// ctx.channel().writeAndFlush(msg).sync();
		// ctx.read();
		if (isJson(msg.toString().trim())) {
			JSONObject jsonObject = new JSONObject(msg.toString().trim());
			int source = jsonObject.optInt("source");
			switch (source) {
			case 1:
				if (ischeck) {
					registerapp(jsonObject, ctx);
				} else {
					parseCode(jsonObject, ctx);// 解析命令
				}
				break;
			case 2:
				if (ischeck) {
					int code = jsonObject.getInt("code");
					// 未激活，等待激活状态
					if (code == Codes.CODE1015) {
						activation(jsonObject, ctx);
					} else {
						// 已经激活 上线
						registercar(jsonObject, ctx);
					}
				} else {
					parseCode(jsonObject, ctx);// 解析命令
				}
				break;
			default:
				break;
			}

		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		if (InitUtil.DEBUG)
			logger.error("server err  close imei:" + imei + "   key:" + key);
		// 打印异常信息并关闭连接
		if (imei != null) {
			switch (source) {
			case 1:
				if (InitUtil.DEBUG)
					logger.error("delete  APP   =" + key);
				DeviceAppAdapter.delAPPClient(key);
				break;
			case 2:
				if (InitUtil.DEBUG)
					logger.error("delete  car   =" + key);
				DeviceAppAdapter.delCarClient(key);
				break;
			default:
				break;
			}
		}
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * car 注册上线
	 * 
	 * @param jsonObject
	 * @param ctx
	 */
	public void registercar(JSONObject jsonObject, ChannelHandlerContext ctx) {

		// 获取imei 号
		if (imei == null) {
			imei = jsonObject.optString("imei");
		}

		if (key == null) {

			key = jsonObject.optString("key");
			logger.info("key   =" + key);
		}

		source = jsonObject.optInt("source");
		logger.info("source   =" + source);

		InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
				.remoteAddress();
		String clientIP = insocket.getAddress().getHostAddress();
		if (imei == null || imei.trim().equals("") || imei.equals("null")
				|| key == null || key.trim().equals("") || key.equals("null")) {

			// 当 imei号 为null 关闭此 无效链接
			logger.error("no imei  err (imei or   key  is  null):" + imei
					+ " ip:" + clientIP);
			ctx.close();

		} else {
			if (ischeck) {
				ischeck = false;
				try {
					// 检查 imei 号 是否有效
					logger.info("while key   =" + key);
					Object object = icars.selectuserimei(imei, key);
					logger.info("while imei 3   =" + imei);
					if (object == null) {
						logger.error("check imei  err (imei  NO):" + imei
								+ " ip:" + clientIP);
						ctx.close();
						return;
					} else {
						// 有效用户
						if (!isadd) {
							if (key != null) {
								// 添加到在线设备中
								if (!DeviceAppAdapter.getActiveCarClientMap()
										.containsKey(key)) {
									DeviceAppAdapter.addCarClient(key, ctx);
								} else {
									DeviceAppAdapter.delCarClient(key);
									DeviceAppAdapter.addCarClient(key, ctx);
								}
								isadd = true;
							}
						}
						JSONObject obj = new JSONObject();
						obj.put("code", 1000);
						obj.put("state", 1);
						obj.put("source", 3);
						obj.put("msg", "car Go online!");
						logger.info("car Go online!" + obj.toString());
						ctx.writeAndFlush(obj.toString());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ischeck = true;
					logger.error("check imei err :" + imei);
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * app注册上线
	 * 
	 * @param jsonObject
	 * @param ctx
	 */
	public void registerapp(JSONObject jsonObject, ChannelHandlerContext ctx) {

		// 获取imei 号
		if (u_id == 0) {
			u_id = jsonObject.optInt("u_id");
			logger.info("u_id   =" + u_id);
		}

		if (key == null) {

			key = jsonObject.optString("key");
			logger.info("key   =" + key);
		}

		source = jsonObject.optInt("source");
		logger.info("source   =" + source);

		InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
				.remoteAddress();
		String clientIP = insocket.getAddress().getHostAddress();
		if (u_id <= 0 || key == null || key.trim().equals("")
				|| key.equals("null")) {

			// 当 imei号 为null 关闭此 无效链接
			logger.error("no u_id  err (u_id or   key is  null):" + u_id
					+ " ip:" + clientIP);
			ctx.close();

		} else {
			if (ischeck) {
				ischeck = false;
				try {
					// 检查 imei 号 是否有效
					logger.info("while uid   =" + u_id);
					Object object = icars.selectuserapp(u_id, key);
					if (object == null) {
						logger.error("check uid  err (uid  NO):" + u_id
								+ " ip:" + clientIP);
						ctx.close();
						return;
					} else {
						// 有效用户
						if (!isadd) {
							if (key != null) {
								// 添加到在线设备中
								if (!DeviceAppAdapter.getActiveAPPClientMap()
										.containsKey(key)) {
									DeviceAppAdapter.addAPPClient(key, ctx);
								} else {

									DeviceAppAdapter.delAPPClient(key);
									DeviceAppAdapter.addAPPClient(key, ctx);
								}
								isadd = true;
							}
						}
						JSONObject obj = new JSONObject();
						obj.put("code", 1000);
						obj.put("state", 1);
						obj.put("source", 3);
						obj.put("msg", "APP Go OnLine ok");
						logger.info("APP Go online OK!" + obj.toString());
						ctx.writeAndFlush(obj.toString());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ischeck = true;
					logger.error("check u_id err :" + u_id);
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 解析指令
	 * 
	 * @throws InterruptedException
	 */
	private void parseCode(JSONObject json, ChannelHandlerContext ctx) {
		try {

			logger.info(" zai xian APP: "
					+ DeviceAppAdapter.getActiveAPPClientMap().size() + " Car:"
					+ DeviceAppAdapter.getActiveCarClientMap().size()
					+ " code :" + json.toString());
			ChannelHandlerContext adapterctx = DeviceAppAdapter
					.getActiveCarClientMap().get(key);

			int cmd = json.getInt("code");
			switch (cmd) {
			case CodeCar.CODE1001:
				logger.info("CODE1001 ==");

				if (adapterctx != null) {
					JSONObject obj = new JSONObject();
					obj.put("code", CodeCar.CODE1001);
					obj.put("state", 1);// 1,打开，2关闭 3,log
					obj.put("source", 3);// 1 wifi手机，2服务器

					Channel chls = checkchannel(adapterctx.channel());
					if (chls != null)
						chls.writeAndFlush(obj.toString()).sync();
				} else {
					JSONObject noobj = new JSONObject();
					noobj.put("code", CodeCar.CODE4001);
					noobj.put("state", 1);// 1,打开，2关闭 3,log
					noobj.put("source", 3);// 1 wifi手机，2服务器

					Channel chls = checkchannel(ctx.channel());
					if (chls != null)
						chls.writeAndFlush(noobj.toString()).sync();
				}

				break;
			case CodeCar.CODE1002:
				logger.info("CodeCar.CODE1002");
				String id = json.getString("data");

				JSONObject codeobj = new JSONObject();
				codeobj.put("code", CodeCar.CODE1003);
				codeobj.put("state", 1);// 1,打开，2关闭 3,log
				codeobj.put("source", 3);// 1 wifi手机，2服务器
				codeobj.put("data", id);// 1 wifi手机，2服务器

				Channel chls = checkchannel(ctx.channel());
				if (chls != null)
					chls.writeAndFlush(codeobj.toString()).sync();

				break;
			case CodeCar.CODE1003:
				logger.info("CODE1003");

				break;
			case CodeCar.CODE1004:

				break;
			case CodeCar.CODE1005:
				// 处理单个GPS
				logger.info("CODE1005");
				logger.info("CODE1001 ==");
				ChannelHandlerContext gpsadapterctx = DeviceAppAdapter
						.getActiveCarClientMap().get(key);

				if (gpsadapterctx != null) {
					JSONObject obj = new JSONObject();
					obj.put("code", CodeCar.CODE1005);
					obj.put("state", 1);// 1,打开，2关闭 3,log
					obj.put("source", 3);// 1 wifi手机，2服务器

					Channel chls1 = checkchannel(gpsadapterctx.channel());
					if (chls1 != null)
						chls1.writeAndFlush(obj.toString()).sync();

				} else {

					JSONObject noobj = new JSONObject();
					noobj.put("code", 4001);
					noobj.put("state", 1);// 1,打开，2关闭 3,log
					noobj.put("source", 3);// 1 wifi手机，2服务器

					Channel chls2 = checkchannel(ctx.channel());
					if (chls2 != null)
						chls2.writeAndFlush(noobj.toString()).sync();
				}
				// g1close(ctx);
				break;
			case CodeCar.CODE1006:

				break;
			case CodeCar.CODE10066:
				// 一次提交多条gps 记录
				String data = json.getString("data");
				List<GPSVO> lists = JSONUtil.getclassList(new GPSVO(), data);
				try {
					boolean addon = icars.insertGpsData(lists);
					if (addon)
						if (InitUtil.DEBUG)
							logger.info("up  http  ok!");
						else if (InitUtil.DEBUG)
							logger.info("up  http  NO!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case CodeCar.CODE1007:

				break;
			case CodeCar.CODE1008:

				break;
			case CodeCar.CODE1009:

				break;
			case CodeCar.CODE1010:

				break;
			case CodeCar.CODE1011:

				break;
			case CodeCar.CODE1012:

				ctx.close();
				break;
			case CodeCar.CODE1013:

				break;
			case CodeCar.CODE1014:
				if (source == 1) {
					JSONObject myobj = new JSONObject();
					myobj.put("code", CodeCar.CODE1014);
					myobj.put("state", 1);// 1,打开，2关闭 3,log
					myobj.put("source", 3);// 1 wifi手机，2服务器

					Channel chls3 = checkchannel(ctx.channel());
					if (chls3 != null)
						chls3.writeAndFlush(myobj.toString()).sync();
				} else {
					logger.info("car CODE1014");

				}

				break;
			case CodeCar.CODE1015:
				if (source == 1) {
					JSONObject myobj = new JSONObject();
					myobj.put("code", cmd);
					myobj.put("state", 1);// 1,打开，2关闭 3,log
					myobj.put("source", 3);// 1 wifi手机，2服务器

					Channel chls4 = checkchannel(ctx.channel());
					if (chls4 != null)
						chls4.writeAndFlush(myobj.toString()).sync();
				} else {
					logger.info("car CODE1014");

				}
				break;
			case CodeCar.CODE1025:

				break;
			case CodeCar.CODE6001:
				logger.info("CODE6001 ==");
				switch (source) {
				case 1:
					ChannelHandlerContext caradapter = DeviceAppAdapter
							.getActiveCarClientMap().get(key);
					if (caradapter != null) {

						Channel chls5 = checkchannel(caradapter.channel());
						if (chls5 != null) {
							JSONObject obj = new JSONObject();
							obj.put("code", cmd);
							obj.put("state", 1);// 1,打开，2关闭 3,log
							obj.put("source", 3);// 1 wifi手机，2服务器
							chls5.writeAndFlush(obj.toString()).sync();
							/*
							 * Channel chls6=checkchannel(ctx.channel());
							 * if(chls6!=null){ String str=
							 * DeviceAppAdapter.getActiveAPPClientMapData
							 * ().get(key+"6001");
							 * logger.info("CODE6001上次 =="+str);
							 * if(str!=null&&!str
							 * .equals("")&&!str.equals("null"))
							 * chls6.writeAndFlush(str).sync(); }
							 */
						} else {
							JSONObject noobj = new JSONObject();
							noobj.put("code", CodeCar.CODE4001);
							noobj.put("state", 1);// 1,打开，2关闭 3,log
							noobj.put("source", 3);// 1 wifi手机，2服务器
							noobj.put("msg", "Car NO Line");// 1 wifi手机，2服务器

							Channel chls6 = checkchannel(ctx.channel());
							if (chls6 != null) {
								chls6.writeAndFlush(noobj.toString()).sync();
							}
						}
					} else {

						JSONObject noobj = new JSONObject();
						noobj.put("code", CodeCar.CODE4001);
						noobj.put("state", 1);// 1,打开，2关闭 3,log
						noobj.put("source", 3);// 1 wifi手机，2服务器
						noobj.put("msg", "Car NO Line");// 1 wifi手机，2服务器

						Channel chls6 = checkchannel(ctx.channel());
						if (chls6 != null)
							chls6.writeAndFlush(noobj.toString()).sync();
					}
					break;
				case 2:
					ChannelHandlerContext appadapter = DeviceAppAdapter
							.getActiveAPPClientMap().get(key);
					// DeviceAppAdapter.getActiveAPPClientMapData().put(key+"6001",
					// json.toString());
					if (appadapter != null) {
						logger.info("car up 6001 data  ==: " + json.toString());

						Channel chls7 = checkchannel(appadapter.channel());
						if (chls7 != null) {
							chls7.writeAndFlush(json.toString()).sync();
							logger.info("car to app  6001 data  ==: "
									+ json.toString());
						}

					}
					break;
				}

				break;
			default:

				break;

			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("==err");
			e.printStackTrace();
		}
	}

	/**
	 * 当前连接状态
	 * 
	 * @param chl
	 * @return
	 */
	private Channel checkchannel(Channel chl) {
		boolean isWritable2 = chl.isWritable();
		boolean isActive2 = chl.isActive();
		boolean isOpen2 = chl.isOpen();
		boolean isRegistered2 = chl.isRegistered();
		logger.info("isWritable:" + isWritable2 + "  isActive:" + isActive2
				+ "  isOpen:" + isOpen2 + "  isRegistered:" + isRegistered2);
		if (isWritable2 && isActive2 && isOpen2 && isRegistered2) {
			return chl;
		}
		return null;
	}

	/**
	 * 关闭
	 * 
	 * @param ctx
	 */
	private void g1close(ChannelHandlerContext ctx) {
		// TODO Auto-generated method stub
		JSONObject obj = new JSONObject();
		obj.put("code", Codes.CODE1012);
		obj.put("source", 3);
		obj.put("msg", "亲，你的账号在别的地方登录了！");
		if (ctx != null) {
			// ctx.write(obj.toString());
			// ctx.channel().writeAndFlush("yes, server is accepted you ,nice !"
			// + msg);
			try {
				if (ctx.channel().isActive()) {
					ctx.channel().writeAndFlush(obj.toString()).sync();

					if (InitUtil.DEBUG)
						logger.info("submite  close " + obj.toString());
				} else {
					ctx.close().sync();
					if (InitUtil.DEBUG)
						logger.info("submite  close   no  clicent ");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    /**
     * 设备未激活
     * @param obj
     * @param ctx
     */
	private void activation(JSONObject obj, ChannelHandlerContext ctx) {
		if (!ispendingDevices) {
			ispendingDevices = true;
			imei = obj.optString("imei");
			DeviceAppAdapter.addpendingDevice(imei, ctx);
		}
		JSONObject  json=new JSONObject();
		json.put("result", Codes.CODE4005);
		
		ctx.writeAndFlush(json);
		
	}
}