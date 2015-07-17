package com.zhlt.g1.util.netty;

/**
 * Created by user on 6/30/15.
 */


import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;


import com.zhlt.g1.camera.bean.CameraPhoto;
import com.zhlt.g1.gps.bean.GPSVO;
import com.zhlt.g1.util.Codes;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpServerHandlerCar extends ChannelInboundHandlerAdapter {
    static Logger logger = Logger.getLogger("TcpServerHandlerCar");
    String imei = null, key = null;
    // 检查 用户
    boolean ischeck = true;
    // 是否添加了设备
    boolean isadd = false;




    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("TCP服务器11111111111111111111111111111");

        //	ctx.channel().writeAndFlush(msg).sync();
        //ctx.read();
        if(isJson(msg.toString().trim())){
            JSONObject jsonObject = new JSONObject(msg.toString().trim());
            //registertest(jsonObject,ctx);
            parseCode(jsonObject, ctx);// 解析命令



        }else{
        	
        }
      /*  ctx.write(msg);
        ctx.flush();*/
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常信息并关闭连接
        cause.printStackTrace();
        ctx.close();
    }


/*    public void registertest(JSONObject jsonObject,ChannelHandlerContext ctx){

        // 获取imei 号
        if (imei == null) {
            imei = jsonObject.optString("imei");
        }

        if (key == null) {
            key = jsonObject.optString("key");
        }
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
                .remoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();
        if (imei == null || imei.trim().equals("") || imei.equals("null") || key == null || key.trim().equals("") || key.equals("null")) {

            // 当 imei号 为null 关闭此 无效链接
            logger.error("no imei  err (imei or   key  为空)关闭:" + imei + " ip:"
                    + clientIP);
            ctx.close();

        } else {
            logger.info("while imei   =" + imei);
            if (ischeck) {
                ischeck = false;

                try {
                    // 检查 imei 号 是否有效
                    logger.info("while imei2   =" + imei);
                    Object object = icars.selectuserimei(imei, key);
                    logger.info("while imei 3   =" + imei);
                    if (object == null) {
                        logger.error("check imei  err (imei号无效):" + imei
                                + " ip:" + clientIP);
                        ctx.close();
                        return;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    ischeck = true;
                    logger.error("check imei err :" + imei);
                    e.printStackTrace();
                }
            }

            if (!isadd) {
                if (imei != null)
                    msm.addCarClient(imei, this); // 添加到在线设备中
                isadd = true;
            }

        }
    }*/
    /**
     * 解析指令
     * @throws InterruptedException
     */
    private void parseCode(JSONObject json, ChannelHandlerContext ctx) throws InterruptedException, JSONException {
        logger.info("解析指令" + json.toString());

        int cmd = json.getInt("code");
        logger.info("cmd =="+cmd);
        switch (cmd) {
            case Codes.CODE1001:
                JSONObject  obj=new JSONObject();
                obj.put("code", Codes.CODE1001);
                obj.put("state",1);// 1,打开，2关闭 3,log
                obj.put("source", 2);//1  wifi手机，2服务器
                ctx.channel().writeAndFlush(obj.toString()).sync();
                break;
            case Codes.CODE1002:
                logger.info("解Codes.CODE1002指令");
                String id = json.getString("data");

                JSONObject  codeobj=new JSONObject();
                codeobj.put("code", Codes.CODE1003);
                codeobj.put("state",1);// 1,打开，2关闭 3,log
                codeobj.put("source", 2);//1  wifi手机，2服务器
                codeobj.put("data", id);//1  wifi手机，2服务器
                ctx.channel().writeAndFlush(codeobj.toString()).sync();



                break;
            case Codes.CODE1003:
                logger.info("解CODE1003指令");

                break;
            case Codes.CODE1004:

                break;
            case Codes.CODE1005:
                //处理单个GPS
                logger.info("解CODE1005指令");
                g1close(ctx);
                break;
            case Codes.CODE1006:

                break;
    /*        case Codes.CODE10066:
                // 一次提交多条gps 记录
                String data = json.getString("data");
                List<GPSVO> lists = JSONUtil.getclassList(new GPSVO(), data);
                try {
                    boolean addon = icars.insertGpsData(lists);
                    if (addon)
                        if (InitUtil.DEBUG)
                            logger.info("添加成功!");
                        else if (InitUtil.DEBUG)
                            logger.info("添加失败!");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;*/
            case Codes.CODE1007:

                break;
            case Codes.CODE1008:

                break;
            case Codes.CODE1009:

                break;
            case Codes.CODE1010:

                break;
            case Codes.CODE1011:

                break;
            case Codes.CODE1012:
                if (imei != null)

                ctx.close();
                break;
            case Codes.CODE1013:

                break;
            case Codes.CODE1014:

                break;
            case Codes.CODE1015:

                break;
            case Codes.CODE1025:

                break;
            default:

                break;

        }

    }

    /**
     * 关闭
     * @param ctx
     */
    private void g1close(ChannelHandlerContext ctx) throws JSONException {
        // TODO Auto-generated method stub
        JSONObject obj = new JSONObject();
        obj.put("code", Codes.CODE1012);
        obj.put("source", 3);
        logger.info("发送G1 关闭指令" + obj.toString());
        if (ctx != null) {
            // ctx.write(obj.toString());
            // ctx.channel().writeAndFlush("yes, server is accepted you ,nice !"
            // + msg);
            try {
                ctx.channel().writeAndFlush(obj.toString()).sync();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
