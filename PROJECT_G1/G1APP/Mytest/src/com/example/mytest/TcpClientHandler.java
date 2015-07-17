package com.example.mytest;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class TcpClientHandler extends ChannelInboundHandlerAdapter {


    private Callback mcallback = null;

    public void setListener(Callback listener) {
        mcallback = listener;
    }
    /**
     * 回调接口
     *
     * @author Ivan Xu
     *
     */
    public interface Callback {

        public  void NettyResult(ChannelHandlerContext ctx, Object msg);
    }
    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
    	 System.out.println("channelActive:.......................");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    	 System.out.println("client接收到服务器返回的消息:"+msg);
        mcallback.NettyResult(ctx,msg);
    }

 /*   @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
           {
        //messageReceived方法,名称很别扭，像是一个内部方法.
        logger.info("client接收到服务器返回的消息:"+msg);
        mcallback.NettyResult(ctx,msg);
    }*/

}
