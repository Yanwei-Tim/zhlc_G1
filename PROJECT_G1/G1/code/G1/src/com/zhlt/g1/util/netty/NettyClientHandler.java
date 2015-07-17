package com.zhlt.g1.util.netty;

import android.app.DownloadManager;
import android.content.Context;

import com.zhlt.g1.camera.CameraData;
import com.zhlt.g1.gps.GPSBAIDUData;
import com.zhlt.g1.sensor.GSensorData;
import com.zhlt.g1.util.Codes;
import com.zhlt.g1.util.InitUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger("TcpClientHandler.class");
    Context mContext;
    public NettyClientHandler(Context context) {
        this.mContext = context;

    }

    public  void RegisterCar(ChannelHandlerContext ctx) throws Exception {

        JSONObject obj = new JSONObject();
		obj.put("code", Codes.CODE1000);
		
		obj.put("source", 2);// 1 wifi手机，
		obj.put("imei", GPSBAIDUData.getInstance(mContext).getImei());// 1
																		// wifi手机，
		obj.put("key", InitUtil.KEY);// 1 wifi手机，
        
        
        System.out.println("operationComplete ......................");
        ctx.channel().writeAndFlush(obj.toString());
        
        GPSBAIDUData.getInstance(mContext).selectlistUpGPS(Codes.CODE10066, 1, ctx.channel());
    }


    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        logger.info("channelActive:.......................");
        try {
            RegisterCar(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


		
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        System.out.println("client接收到服务器返回的消息:" + msg);
        JSONObject json = null;
     //队列处理。。 等待。。。  另外一个线程处理消息。。。。。。。。。。
        if (msg != null ) {

            try {
                json = new JSONObject(msg.toString().trim());

                int code = json.optInt("code");
                System.out.println("code ======="+code);
                switch (code) {
                    case Codes.CODE1000:
                        // 上线
                        try {
                            RegisterCar(ctx);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Codes.CODE1001:
                        // 实时照相
                        System.out.println("CODE1001 =======");
                        CameraData.getInstance(mContext).TakePhone( GPSBAIDUData.getInstance(mContext),ctx.channel());
                        GPSBAIDUData.getInstance(mContext).log(true);
                        // startcamera(code, socket);
                        break;
                    case Codes.CODE1004:
                        CameraData.getInstance(mContext).StartVideo(ctx.channel());
                        // 实时录像
                        break;
                    case Codes.CODE10004:
                        String videoid = json.optString("data");
                        System.out.println("CODE10004 =======+"+videoid);
                        CameraData.getInstance(mContext).UploadVideo(videoid,ctx.channel());
                        break;
                    case Codes.CODE1005:
                        // 实时GPS地址
                        System.out.println("CODE1005 =======");
                        GPSBAIDUData.getInstance(mContext).sendgpsdata(code, ctx.channel());
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
                        CameraData.getInstance(mContext).Uploadimg(id,ctx.channel());
                        break;

                    case Codes.CODE1012:
                        // sensor数据
                        break;
                    case Codes.CODE6001:
                        // sensor数据
                        GPSBAIDUData.getInstance(mContext).sendfristdata(code, ctx.channel());
                        //  closeSocket(ctx.channel());
                        break;
                    case Codes.CODE9001:
                        // sensor数据
                        GPSBAIDUData.getInstance(mContext).SendgpdDebugInfo(code, ctx.channel());
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
            System.out.println ("client接收到服务器返回的消息:null");
        }

    }


}
