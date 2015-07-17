package com.zhlt.g1app.basefunc;

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
import io.netty.util.CharsetUtil;

public class TcpClient {
    public static String HOST = "121.41.16.21";
    public static int PORT = 7776;

    public  Bootstrap bootstrap  ;
    public  Channel channel ;



    public void CreatServerConnect(TcpClientHandler  client){
        bootstrap = getBootstrap(client);
        channel = getChannel(HOST,PORT);
    }
    /**
     * 初始化Bootstrap
     * @return
     */
    public  Bootstrap getBootstrap(final  TcpClientHandler client ){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder",
                        new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                pipeline.addLast("encoder",new ObjectEncoder());
                pipeline.addLast("handler",client);
            }
        });
        b.option(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }

    public  final Channel getChannel(String host,int port){
        Channel channel = null;
        try {
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (Exception e) {
            System.out.println("连接Server(IP[%s],PORT");
            return null;
        }
        return channel;
    }

    public  void sendMsg(String msg) throws Exception {
        if(channel!=null){
            ChannelFuture f =channel.writeAndFlush(msg).sync();
        }
        else{
            System.out.println ("消息发送失败,连接尚未建立!");
        }
    }


}