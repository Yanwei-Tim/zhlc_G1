package com.zhlt.g1.util.thread;

import android.content.Context;
import com.zhlt.g1.camera.CameraData;
import com.zhlt.g1.gps.GPSBAIDUData;
import com.zhlt.g1.sensor.GSensorData;
import com.zhlt.g1.util.Codes;
import com.zhlt.g1.util.InitUtil;
import com.zhlt.g1.util.SocketUtil;
import com.zhlt.g1.util.netty.TcpClient;
import com.zhlt.g1.util.netty.TcpClientHandler;
import com.zhlt.g1.util.netty.TcpServerHandlerCar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Created by user on 6/30/15.
 */
public class G1APPManagerThread extends Thread implements TcpClientHandler.Callback {


    private static G1APPManagerThread instance;

    Context mContext;

    //............................
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2; // 默认
    /**
     * 业务出现线程大小
     */
    protected static final int BIZTHREADSIZE = 4;
    private static final String IP = "192.168.1.30";
    private static final int PORT = 7778;
    /*
     * NioEventLoopGroup实际上就是个线程池,
     * NioEventLoopGroup在后台启动了n个NioEventLoop来处理Channel事件,
     * 每一个NioEventLoop负责处理m个Channel,
     * NioEventLoopGroup从NioEventLoop数组里挨个取出NioEventLoop来处理Channel
     */
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);
    private G1APPManagerThread(Context context) {
        this.mContext = context;
        start();
    }

    public static G1APPManagerThread getInstance(Context context) {
        if (instance == null) {
            instance = new G1APPManagerThread(context);
        }
        return instance;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("..........................sdsd................................................");
        try {
            buildConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
     void buildConnect() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder",
                        new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                pipeline.addLast("encoder",new ObjectEncoder());


                TcpClientHandler tsh = new TcpClientHandler();
                tsh.setListener(G1APPManagerThread.this);
                pipeline.addLast(tsh);
            }
        });
        b.bind(IP, PORT).sync();
        if (InitUtil.DEBUG)
            System.out.println("TCP服务器已启动");
    }

    @Override
    public void NettyResult(ChannelHandlerContext ctx, Object msg) {
        System.out.println ("client接收到服务器返回的消息:"+msg);
        JSONObject json = null;
        if (msg != null ) {
            try {
                json = new JSONObject(msg.toString().trim());
                int code = json.optInt("code");
            switch (code) {
                case Codes.CODE1004:
                    // 实时录像
                    break;
                case Codes.CODE1001:
                    // 实时照相
                    System.out.println("laizhaoxiang le ................................");

                      CameraData.getInstance(mContext).TakePhone( GPSBAIDUData.getInstance(mContext),ctx.channel());
                    break;
                case Codes.CODE1005:
                    // 实时GPS地址
                    GPSBAIDUData.getInstance(mContext).sendgpsdata(code,ctx.channel());
                    break;
                case Codes.CODE1006:
                    // GPS历史记录
                    JSONObject day = json.optJSONObject("data");
                    String start = day.optString("start");
                    String end = day.optString("end");
                    GPSBAIDUData.getInstance(mContext).startgpslog(code, start, end, ctx.channel());
                    break;
                case Codes.CODE1007:
                    // sensor数据

                    GSensorData.getInstance(mContext).startsensor(code, ctx.channel());
                    break;
                case Codes.CODE1003:
                    String id = json.optString("data");
                    System.out.println("CODE1003 =======+"+id);
                    CameraData.getInstance(mContext).sendPhone(id,ctx.channel());
                    break;
                case Codes.CODE1012:
                    // sensor数据
                    closeSocket(ctx.channel());
                    break;
                case Codes.CODE6001:
                    // sensor数据
                  //  closeSocket(ctx.channel());
                    break;

                default:
                    System.out.println("APP不在线。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
                    break;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }else{
            System.out.println ("client接收到服务器返回的消息:..null");
        }
}

    private void closeSocket(Channel channel) {
        // TODO Auto-generated method stub
        JSONObject obj = new JSONObject();
        try {
            obj.put("code", Codes.CODE1012);
            obj.put("imei", GPSBAIDUData.getInstance(mContext).getImei());
            channel.writeAndFlush(obj.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
