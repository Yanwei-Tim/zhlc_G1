package com.zhlt.g1app.basefunc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.logging.Logger;

import com.zhlt.g1app.basefunc.InitUtil;

import android.content.Context;


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
 ** yuanpeng@gobabymobile.cn        2015-7-6 下午5:37:56     1.0         Create this moudle
 */
public class APPClientNetty {
	private static final Logger logger = Logger.getLogger("TcpClient.class");


	public Channel channel;
	EventLoopGroup group = null;
   private Context mContext;
	public  APPClientNetty(Context context) {
        this.mContext = context;
		init();
	}

	/**
	 * 初始化Bootstrap
	 * 
	 * @return
	 */
	public void  init() {
		logger.info("start APP ");
		group=new NioEventLoopGroup();
		try {
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class);
		b.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("decoder", new ObjectDecoder(
						Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
				pipeline.addLast("encoder", new ObjectEncoder());
				// ch.pipeline().addLast(new PersonEncoder());
				pipeline.addLast("idleStateHandler", new IdleStateHandler(60,
						30, 0));// 心跳包 30s
								// 没有写入
								// 发送心跳
								// 60
								// 秒没有读取完
				pipeline.addLast("HeartbeatHandler", new HeartBeatReqHandler());
				pipeline.addLast("handler", new ClientHandler(mContext));
			}
		});
		b.option(ChannelOption.SO_KEEPALIVE, true);
		ChannelFuture channelFuture = b.connect(InitUtil.SERVERIP,InitUtil.SERVERPORT).sync();
		channel=channelFuture.channel();
		channel.closeFuture().sync();
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Connect err");
			e.printStackTrace();
		}finally{
			
			group.shutdownGracefully();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.info("clean group ,init()");
					init();
				}
			}).start();
		}
	}

	public void sendMsg(String msg) throws Exception {
		if (channel != null) {
			final ChannelFuture f = channel.writeAndFlush(msg).sync();
			f.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) {
					assert f == future;
					if (f.channel().isWritable()) {
						System.out
								.println("operationComplete ....no..................");
					} else {
						System.out
								.println("operationComplete .........canl.............");
					}

				}
			});
		} else {
			logger.info("消息发送失败,连接尚未建立!");
		}
	}

}
