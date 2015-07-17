package com.zhlt.g1app.basefunc.netty;

import org.apache.log4j.Logger;

import com.zhlt.g1app.basefunc.InitUtil;
import com.zhlt.g1app.basefunc.Log4jUtil;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

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
 * --------- yuanpeng@gobabymobile.cn 2015-7-6 下午3:00:40 1.0 Create this moudle
 */
public class HeartBeatReqHandler extends ChannelDuplexHandler {
	private Logger log4jUtil = Log4jUtil.getLogger("HeartBeatReqHandler");
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				ctx.writeAndFlush(InitUtil.X);
				if(InitUtil.DEBUG)log4jUtil.info(InitUtil.X);
			} else if (e.state() == IdleState.WRITER_IDLE) {
				ctx.writeAndFlush(InitUtil.X);
				if(InitUtil.DEBUG)log4jUtil.info(InitUtil.X);
			} else if (e.state() == IdleState.ALL_IDLE) {
				ctx.writeAndFlush(InitUtil.X);
				if(InitUtil.DEBUG)log4jUtil.info(InitUtil.X);
			}
		}
	}
}