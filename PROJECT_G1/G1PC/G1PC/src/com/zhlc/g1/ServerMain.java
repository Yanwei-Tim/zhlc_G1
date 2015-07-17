package com.zhlc.g1;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.zhlc.g1.car.guice.module.CarModule;
import com.zhlc.g1.car.util.HeartBeatReqHandler;
import com.zhlc.g1.car.util.TcpServerHandlerCar;
import com.zhlc.g1.car.util.TimeServerHandlerExecutePool;
import com.zhlc.g1.util.InitUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * * Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd All rights reserved.
 * http://www.gobabymobile.cn/ File: - MainActivity.java Description:程序入口 * * *
 * ------------------------------- Revision History:
 * ------------------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * --------- yuanpeng@gobabymobile.cn 2015-6-26 上午10:32:39 1.0 Create this
 * moudle
 */
public class ServerMain {

	public final static Injector injector = Guice
			.createInjector(new CarModule());
	/**
	 * 用于分配处理业务线程的线程组个数
	 */
	protected static final int BIZGROUPSIZE = Runtime.getRuntime()
			.availableProcessors() * 2; // 默认
	/**
	 * 业务出现线程大小
	 */
	protected static final int BIZTHREADSIZE = 4;

	/* 车载端 日志 */
	static Logger logger_car = Logger.getLogger("server_car_7776");
	/* APP端日志 */
	static Logger logger_app = Logger.getLogger("server_app_7777");
	ServerMain sm = this;

	private static TimeServerHandlerExecutePool fixedThreadPool = new TimeServerHandlerExecutePool(
			50, 10000);

	public static void main(String[] args) {

		try {
			//System.out.println(System.currentTimeMillis()/1000L);
			new ServerMain().runTC();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void runTC() {
		ServerBootstrap b = new ServerBootstrap();
		/*
		 * NioEventLoopGroup实际上就是个线程池,
		 * NioEventLoopGroup在后台启动了n个NioEventLoop来处理Channel事件,
		 * 每一个NioEventLoop负责处理m个Channel,
		 * NioEventLoopGroup从NioEventLoop数组里挨个取出NioEventLoop来处理Channel
		 */
		EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
		EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);
		try {

			if (InitUtil.DEBUG)
				logger_car.info("TCP  start :"  + " port:"
						+ InitUtil.PORT);

			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					if (InitUtil.DEBUG)
						logger_car.info("New connection ");
					ChannelPipeline pipeline = ch.pipeline();
					/*
					 * pipeline.addLast("frameDecoder", new
					 * LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0,
					 * 4)); pipeline.addLast("frameEncoder", new
					 * LengthFieldPrepender(4)); pipeline.addLast("decoder", new
					 * StringDecoder(CharsetUtil.UTF_8));
					 * pipeline.addLast("encoder", new
					 * 地图  潘琳琳
					 * StringEncoder(CharsetUtil.UTF_8));
					 */
					pipeline.addLast("decoder", new ObjectDecoder(
							Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
					pipeline.addLast("encoder", new ObjectEncoder());
					pipeline.addLast("readTimeoutHandler",new ReadTimeoutHandler(120));
					pipeline.addLast("HeartbeatHandler", new HeartBeatReqHandler());
					
					TcpServerHandlerCar tsh = injector
							.getInstance(TcpServerHandlerCar.class);
					tsh.msm = sm;
					pipeline.addLast(tsh);
				}
			});
			b.option(ChannelOption.SO_BACKLOG, 128);   
			b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 20000);
		    b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			b.option(ChannelOption.SO_SNDBUF, 100 * 1024);
	        b.option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024);
	        b.option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK,8 * 1024);
            b.option(ChannelOption.SO_KEEPALIVE, true);
			b.option(ChannelOption.TCP_NODELAY, true); 
			
			ChannelFuture channelFuture = b.bind(InitUtil.PORT)
					.sync();
			if (InitUtil.DEBUG)
				logger_car.info("TCP start ok");
			channelFuture.channel().closeFuture().sync();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (InitUtil.DEBUG)
				logger_car.error("TCP err");
		} finally {
			if (bossGroup != null) {
				bossGroup.shutdownGracefully();
			}
			if (workerGroup != null) {
				workerGroup.shutdownGracefully();
			}
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(1000 * 60);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (InitUtil.DEBUG)
						logger_car.info("TCP Re start ");
					runTC();
				}
			}).start();

		}
	}

}
