package com.zhlt.g1app.basefunc.netty;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhlt.g1app.basefunc.Codes;
import com.zhlt.g1app.basefunc.InitUtil;
import com.zhlt.g1app.basefunc.Log4jUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 ** Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd All rights reserved.
 * http://www.gobabymobile.cn/ File: - MainActivity.java Description:描述
 ** 
 ** 
 ** ------------------------------- Revision History:
 * ------------------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * --------- yuanpeng@gobabymobile.cn 2015-7-6 下午3:01:27 1.0 Create this moudle
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
	static ClientHandler ch;
	ChannelHandlerContext mctx = null;
	Context mct = null;
	private static Logger log4jUtil = Log4jUtil.getLogger("ClientHandler");
	public ClientHandler(Context ct) {
		mct = ct;
		ch = this;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		registerAPP(ctx);
		mctx = ctx;
	}

	public void registerAPP(ChannelHandlerContext ctx) throws Exception {
		// 用户APP 上线
		JSONObject obj = new JSONObject();
		obj.put("code", Codes.CODE1000);
		obj.put("u_id", InitUtil.UID);// 1,打开，2关闭 3,log
		obj.put("source", 1);// 1 wifi手机，
		obj.put("key", InitUtil.KEY);// 1 wifi手机，
		ctx.channel().writeAndFlush(obj.toString());
		log4jUtil.info("=======register APP");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		log4jUtil.info("server break:" + msg.toString());
		try {
			new JSONObject(msg.toString());
		} catch (JSONException e) {
			// TODO: handle exception
			log4jUtil.info("xintiao APP :" + msg.toString());
			return;
		}
		
		Intent mIntent = new Intent(InitUtil.ACTION_NAME);
		mIntent.putExtra("msg", msg.toString());
		// 发送广播
		mct.sendBroadcast(mIntent);

		mctx = ctx;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		log4jUtil.error("err  exceptionCaught");
		ctx.close();
	}

	public void sendmsg(String string) {
		// TODO Auto-generated method stub
		if(mctx==null||mctx.channel()==null)return;
		boolean isWritable = mctx.channel().isWritable();
		boolean isActive = mctx.channel().isActive();
		boolean isOpen = mctx.channel().isOpen();
		boolean isRegistered = mctx.channel().isRegistered();
		log4jUtil.info("isWritable:" + isWritable + "  isActive:"
				+ isActive + "  isOpen:" + isOpen + "  isRegistered:"
				+ isRegistered);
		if(isWritable&&isActive&&isOpen&&isRegistered)
		{
			mctx.writeAndFlush(string);
		
		}else{
			JSONObject obj = new JSONObject();
			try {
				obj.put("code", Codes.CODE4001);
				obj.put("source", 1);
				obj.put("msg", "亲，暂时连接不上!");
				Intent mIntent = new Intent(InitUtil.ACTION_NAME);
				mIntent.putExtra("msg", obj.toString());
				// 发送广播
				mct.sendBroadcast(mIntent);
				log4jUtil.info(obj.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public static class ClientHandlerBroadcastReceiver extends
			BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals("com.example.mytest")) {
				String msg = intent.getStringExtra("msg");
				ch.sendmsg(msg);
				log4jUtil.info("sendmsg:"+msg);
			}
		}

	}

}