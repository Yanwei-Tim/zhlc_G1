package com.zhlc.g1.car.util;
import java.util.Date;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.zhlc.g1.util.InitUtil;


/**
 * 
 ** Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd
 ** All rights reserved.
 ** http://www.gobabymobile.cn/
 ** File: - MainActivity.java
 ** Description:  服务器端心跳连接
 **     
 **
 ** ------------------------------- Revision History: -------------------------------------
 ** <author>                             <data>             <version>            <desc>
 ** ---------------------------------------------------------------------------------------
 ** yuanpeng@gobabymobile.cn        2015-7-1 下午02:33:56     1.0         Create this moudle
 */
public class HeartBeatReqHandler extends ChannelDuplexHandler {
	static Logger logger_car = Logger.getLogger("HeartBeatReqHandler");
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		if(msg.toString().trim().length()<3){
			ctx.writeAndFlush(InitUtil.SERVERRRESULT);
			logger_car.info("xintiao : "+msg.toString());
		}else{
			ctx.fireChannelRead(msg);
		}
		
	}
}
