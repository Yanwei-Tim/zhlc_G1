package com.zhlt.g1;


import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ypy.eventbus.EventBus;
import com.zhlt.g1.camera.CameraVideoInfo;
import com.zhlt.g1.camera.bean.CameraVideo;
import com.zhlt.g1.camera.db.CameraDBManager;
import com.zhlt.g1.db.DBOpenHelper;
import com.zhlt.g1.util.FileUtil;
import com.zhlt.g1.util.GenSequenceUtil;
import com.zhlt.g1.util.InitUtil;
import com.zhlt.g1.util.MyApplication;
import com.zhlt.g1.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import io.netty.channel.Channel;


public class RecordActivity extends Activity implements SurfaceHolder.Callback {
    File fpath;

    private MediaRecorder mediarecorder;// 录制视频的类
    SurfaceView surfaceview;
    private SurfaceHolder surfaceHolder;
    String imei = "112332323";
    String key = "123";
    CameraDBManager cdb;
    CameraVideo video = new CameraVideo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(RecordActivity.this);
        setContentView(R.layout.recordlayout);
        DBOpenHelper dbh = DBOpenHelper.getInstance(this);
        cdb = new CameraDBManager(dbh);

        surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview);

        surfaceHolder = surfaceview.getHolder();// 取得holder
        surfaceHolder.addCallback(this); // holder加入回调接口
        // setType必须设置，要不出错.
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    /**
     * 定时器
     *
     * @author bcaiw
     *
     */
    class TimerThread extends TimerTask {

        /**
         * 停止录像
         */
        @Override
        public void run() {
            Log.i("TimerThread", "TimerThread, run()");

            initVideo();
            this.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(RecordActivity.this);
    }
    private void initVideo() {
        mediarecorder = new MediaRecorder();// 创建mediarecorder对象
        // 设置录制视频源为Camera(相机)
        mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
        mediarecorder
                .setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 设置录制的视频编码h263 h264
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
        mediarecorder.setVideoSize(176, 144);
        // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
        mediarecorder.setVideoFrameRate(20);
        mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
        fpath =  FileUtil.getimagefile();
        // 设置视频文件输出的路径
        mediarecorder.setOutputFile(fpath.getPath());

        try {
            // 准备录制
            mediarecorder.prepare();
            // 开始录制
            mediarecorder.start();
          video.setstarttime(TimeUtil.getTime(TimeUtil.TIME2));
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public void onEvent(CameraVideoInfo cameraVideoInfo) {

        if(cameraVideoInfo.isIsrecord()){
             Timer timer = new Timer();
        timer.schedule(new TimerThread(), 1000);
            System.out.println("开始录制...........2......................");
        }else {
            System.out.println("停止录制...........2......................");
            if (mediarecorder != null) {
                // 停止录制
                System.out.println();
                mediarecorder.stop();
                // 释放资源
                mediarecorder.release();
                mediarecorder = null;
                saveDatabase(cameraVideoInfo.getChannel());
                finish();
        }
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
        surfaceHolder = holder;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // surfaceDestroyed的时候同时对象设置为null
        surfaceview = null;
        surfaceHolder = null;
        mediarecorder = null;
    }


    protected void saveDatabase(Channel channel) {
        // TODO Auto-generated method stub

        String id = GenSequenceUtil.getFullDateRandomNum(4);
        video.setId(id);
        video.setendtime(TimeUtil.getTime(TimeUtil.TIME2));
        video.setSavePath(fpath.getAbsolutePath());
        cdb.insertvideo(video);
        if(channel!=null) {
            JSONObject resultobj = new JSONObject();
            try {
                resultobj.put("code", "");
                resultobj.put("state", 3);
                resultobj.put("imei", imei);
                resultobj.put("data", id);
                cdb.updatavideoUpload(video.getId());
                channel.writeAndFlush(resultobj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println("saveDatabase ---video id= "+id);


    }
}