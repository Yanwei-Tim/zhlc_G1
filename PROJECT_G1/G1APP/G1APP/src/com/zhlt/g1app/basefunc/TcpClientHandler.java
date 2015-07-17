package com.zhlt.g1app.basefunc;


import org.json.JSONObject;

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
        try {
//            RegisterCar(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RegisterCar(ChannelHandlerContext ctx) throws Exception {
        // 用户APP 上线
        JSONObject obj = new JSONObject();
        obj.put("code", Codes.CODE1000);
        obj.put("u_id", 1);// 1,打开，2关闭 3,log
        obj.put("source", 1);// 1 wifi手机，
        obj.put("key", "123");// 1 wifi手机，
        ctx.channel().writeAndFlush(obj.toString());
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("client接收到服务器返回的消息:"+msg);
        mcallback.NettyResult(ctx,msg);
    }


}
