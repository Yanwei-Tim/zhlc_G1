package com.zhlt.g1.util.netty;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.zhlt.g1.camera.bean.CameraPhoto;
import com.zhlt.g1.util.InitUtil;
import com.zhlt.g1.util.MyApplication;
import com.zhlt.g1.util.PersonDecoder;
import com.zhlt.g1.util.PersonEncoder;

import java.util.Objects;
import java.util.logging.Logger;

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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;
/**
 * 
  ** Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd
 ** All rights reserved.
 ** http://www.gobabymobile.cn/
 ** File: - TcpClient.java
 ** Description:描述
 **     
 **
 ** ------------------------------- Revision History: -------------------------------------
 ** <author>                             <data>             <version>            <desc>
 ** ---------------------------------------------------------------------------------------
 ** yuanpeng@gobabymobile.cn        2015-7-10  下午2:32:49     1.0         Create this moudle
 */
public class TcpClient {
	private static final Logger logger = Logger.getLogger("TcpClient.class");


	public Channel channel;
	EventLoopGroup group = null;
    private Context mContext;
	public  TcpClient(Context mcontext) {
        this.mContext = mcontext;
        logger.info("===========........TcpClient..............................................................");
		init();
	}

	/**
	 * 初始化Bootstrap
	 * 
	 * @return
	 */
	public void  init() {
		if(InitUtil.DEBUG)  logger.info("===========............开启连接..............................................................");
        
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
				pipeline.addLast("idleStateHandler", new IdleStateHandler(180,
						60, 0));
				pipeline.addLast("readTimeoutHandler",new ReadTimeoutHandler(120));
				pipeline.addLast("HeartbeatHandler", new HeartBeatReqHandler());
				pipeline.addLast("handler", new NettyClientHandler(mContext));
			}
		});
		b.option(ChannelOption.SO_BACKLOG, 128);   
		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 20*1000);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.option(ChannelOption.TCP_NODELAY, true); 
		ChannelFuture channelFuture = b.connect(InitUtil.SERVERIP, InitUtil.PORTSERVER).sync();
		channel=channelFuture.channel();
		channel.closeFuture().sync();
		
		} catch (Exception e) {
			// TODO: handle exception
			if(InitUtil.DEBUG)  logger.info("===========......异常...");
	        
			e.printStackTrace();
		}finally{
			
			group.shutdownGracefully();
			channel=null;
			group=null;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(1000*30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(InitUtil.DEBUG)logger.info("清除，重新连接");
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