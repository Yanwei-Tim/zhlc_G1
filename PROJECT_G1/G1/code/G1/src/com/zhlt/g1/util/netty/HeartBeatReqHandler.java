package com.zhlt.g1.util.netty;

import java.util.logging.Logger;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import com.zhlt.g1.util.InitUtil;
import com.zhlt.g1.util.TimeUtil;

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
 * --------- yuanpeng@gobabymobile.cn 2015-7-1 下午4:33:39 1.0 Create this moudle
 */
public class HeartBeatReqHandler extends ChannelDuplexHandler {
	private static final Logger logger = Logger
			.getLogger("com.zhlt.g1.util.netty.HeartBeatReqHandler");


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;

			if (e.state() == IdleState.READER_IDLE) {
				
				if (InitUtil.DEBUG)
					logger.info("IdleState.READER_IDLE:"+InitUtil.X);
				
				ctx.writeAndFlush(InitUtil.X);
			} else if (e.state() == IdleState.WRITER_IDLE) {
				
				if (InitUtil.DEBUG)
					logger.info("IdleState.WRITER_IDLE"+InitUtil.X);
				ctx.writeAndFlush(InitUtil.X);
			} else if (e.state() == IdleState.ALL_IDLE) {
		
				if (InitUtil.DEBUG)
					logger.info("IdleState.ALL_IDLE"+InitUtil.X);
				ctx.writeAndFlush(InitUtil.X);
			}
		}
	}
}