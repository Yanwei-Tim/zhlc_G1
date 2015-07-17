package com.zhlt.g1.util.thread;

import android.content.Context;

import com.zhlt.g1.camera.CameraData;
import com.zhlt.g1.gps.GPSBAIDUData;
import com.zhlt.g1.sensor.GSensorData;
import com.zhlt.g1.util.Codes;
import com.zhlt.g1.util.Log4jUtil;
import com.zhlt.g1.util.netty.TcpClient;
import com.zhlt.g1.util.netty.TcpClientHandler;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by user on 6/30/15.
 */
public class PCManagerThread extends Thread  {

	static Logger log = Log4jUtil.getLogger("PCManagerThread");
    private static PCManagerThread instance;
    TcpClient client;
    TcpClientHandler clientListen ;
    Context mContext;
    private PCManagerThread(Context context) {
           this.mContext = context;
           start();
    }

    public static PCManagerThread getInstance(Context context) {
        if (instance == null) {
            instance = new PCManagerThread(context);
            log.info("===========..........PCManagerThread  ==null..............................................................");
            
        }else{
        	 log.info("===========...........PCManagerThread !=null..............................................................");
             
        }
        return instance;
    }

    @Override
    public void run() {
        super.run();
        log.info("===========..........run..............................................................");
        client = new TcpClient(mContext);
    }

}
