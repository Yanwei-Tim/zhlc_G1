package com.zhlt.g1.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.ypy.eventbus.EventBus;
import com.zhlt.g1.MainActivity;
import com.zhlt.g1.R;
import com.zhlt.g1.RecordActivity;
import com.zhlt.g1.camera.bean.CameraPhoto;
import com.zhlt.g1.camera.bean.CameraVideo;
import com.zhlt.g1.camera.db.CameraDBManager;
import com.zhlt.g1.camera.interfaces.CameraInterface;
import com.zhlt.g1.db.DBOpenHelper;
import com.zhlt.g1.gps.GPSBAIDUData;
import com.zhlt.g1.gps.bean.GPSVO;
import com.zhlt.g1.gps.db.GPSDBManager;
import com.zhlt.g1.util.FileUtil;
import com.zhlt.g1.util.GenSequenceUtil;
import com.zhlt.g1.util.InitUtil;
import com.zhlt.g1.util.Log4jUtil;
import com.zhlt.g1.util.MyApplication;
import com.zhlt.g1.util.TimeUtil;
import com.zhlt.g1.util.netty.TcpClient;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import static com.zhlt.g1.R.layout.activity_main;

public class CameraData   implements CameraInterface,
		MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener,
		Camera.PreviewCallback {
//	SurfaceHolder mSurfaceHolder;
	private Parameters mParameters;
	private Camera mCamera = null;
	Logger log = Log4jUtil.getLogger("com.zhlt.g1.camera.CameraData");

	CameraDBManager cdb = null;
    private static CameraData instance;
    private DBOpenHelper dbh;
    private Context mContext;
    SurfaceView surfaceview;
    private  boolean isrecord = true;
    private Channel Curchannel;
    private CameraData(Context c) {
        this.mContext = c;
        dbh = DBOpenHelper.getInstance(c);
        try {
            init(c,dbh);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static CameraData getInstance(Context c) {
        if (instance == null) {
            instance = new CameraData(c);
        }
        return instance;
    }
	@Override
	public void init(Context ct, DBOpenHelper dbh) throws Exception {
		// TODO Auto-generated method stub

        cdb = new CameraDBManager(dbh);
	}

	public void init(Activity ct, DBOpenHelper dbh,
			android.view.SurfaceView mSurfaceview)
			throws Exception {
		// TODO Auto-generated method stub
	/*	mSurfaceHolder = mSurfaceview.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//*/
		cdb = new CameraDBManager(dbh);
	}

	boolean isphotograph = true;
	GPSVO mlocation = null;

	@Override
	public void start(Handler mhandler) throws Exception {
		// TODO Auto-generated method stub

	}

	public void start(GPSVO location) throws Exception {
		if (isphotograph) {
			if (mCamera == null) {
                log.info("start2222222222");
				openCamera();
			} else {
                log.info("start11111111111111");
				mCamera.autoFocus(null);
                mCamera.unlock();
				mCamera.startPreview();
				
			}
			try {

				mCamera.takePicture(null, null, pictureCallback);
				if (InitUtil.DEBUG)
					log.info("开始拍照");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				isphotograph = true;
			}
		}
		mlocation = location;
	}

	private Camera openCamera() {
		// TODO Auto-generated method stub
		try {
			if (mCamera == null) {
				int CammeraIndex = FindBackCamera();
				if (CammeraIndex == -1) {
					CammeraIndex = FindFrontCamera();
				}
				mCamera = android.hardware.Camera.open(CammeraIndex);

			//	mCamera.setPreviewDisplay(mSurfaceHolder);

				mParameters = mCamera.getParameters();
				mParameters.setPictureFormat(PixelFormat.JPEG);// Setting
																// Picture
																// Format
				// parameters.set("rotation", 180); // Arbitrary rotation
				mCamera.setDisplayOrientation(90);
				// parameters.setPreviewSize(400, 300); // Set Photo Size
				mCamera.setParameters(mParameters); // Setting camera parameters

				mCamera.startPreview();
			} else {

				mCamera.autoFocus(null);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return mCamera;
	}


    public void StartVideo(final Channel channel) {
        CameraVideoInfo videoinfo = new CameraVideoInfo();
        videoinfo.setChannel(channel);
        videoinfo.setIsrecord(isrecord);
        if(isrecord) {
            Intent intent2 = new Intent();
            intent2.setClass(mContext, RecordActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent2);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("..............................................................00000000000000000000000000000");
            isrecord = false;
        }else {
            isrecord = true;
        }
        System.out.println("..............................................................1111111111111111111");
        EventBus.getDefault().post(videoinfo);
    }

    public void TakePhone(final GPSBAIDUData gpsdata,final Channel channel) {
        CameraVideoInfo videoinfo = new CameraVideoInfo();
        videoinfo.setIsrecord(false);
        EventBus.getDefault().post(videoinfo);
        Curchannel = channel;
                MyApplication.getInstance().getFixedThreadPool()
                        .execute(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try {
                                    start(gpsdata.getGPSVO());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
       }





    public   void  sendPhone(String id,Channel channel){

        // TODO Auto-generated method stub

        CameraPhoto cph = path(id);


        FileInputStream fis = null;
        try {
            fis = new FileInputStream(cph.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int size = 0;
        try {
            size = fis.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[size];
        try {
            fis.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cph.setpictureinfo(data);
        cph.setImei(GPSBAIDUData.getInstance(mContext).getImei());
        cph.setFileSize(size);
        channel.writeAndFlush(cph);

    }

    public void Uploadimg(String id,final  Channel channel) {
        CameraPhoto cph = path(id);
       final File  fpath = new File(cph.getPath());
        uploadfile(fpath,channel);
    }
    public void UploadVideo(String id,final  Channel channel) {
        CameraVideo cph =cdb.getvideo(id);
        final File  fpath = new File(cph.getSavePath());
        uploadfile(fpath,channel);
    }

private  void  uploadfile(final File  fpath,final  Channel channel){
    MyApplication.getInstance().getFixedThreadPool()
            .execute(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        Map map = new HashMap<String, String>();
                        map.put("act",
                                "{\"act\":\"upfile\",\"imei\":\""
                                        + GPSBAIDUData.getInstance(mContext).getImei()
                                        + "\",\"gpsid\":\""
                                        +"123" + "\"}");// uploadImg
                        Map mapfile = new HashMap<String, File>();
                        mapfile.put("file", fpath);

                        System.out.println("----------文件长度=="
                                + fpath.length());
                        String result = FileUtil.post(InitUtil.HTTPPATH
                                + "VoiceAction", map, mapfile);
                        if (result != null && !result.equals("")
                                && !result.equals("null")) {
                            JSONObject array = new JSONObject(result);
                            int state = array.optInt("state");
                            if (state == 1) {
                                System.out.println("state 1  =");

                                String obj = array.optString("obj");
                                if (obj != null && !obj.equals("")
                                        && !obj.equals("null")) {
                                    System.out.println("obj  ="
                                            + obj);
                                    String sd = obj.replace("\\", "/");
                                    String myurl = InitUtil.HTTPPATH+sd;
                                    System.out.println("myurl  ="
                                            + myurl);

                                    JSONObject myobj = new JSONObject();
                                    myobj.put("code", 1003);
                                    myobj.put("state", 1);
                                    myobj.put("source", 2);//1  wifi手机，
                                    myobj.put("imei", GPSBAIDUData.getInstance(mContext).getImei());//1  wifi手机，
                                    myobj.put("key", "123");//1  wifi手机，
                                    myobj.put("data",myurl);
                                    log.info("data:OK/....................." );
                                    channel.writeAndFlush(myobj.toString()).sync();
                                } else {
                                    System.out.println("obj  ="
                                            + obj);
                                }
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
}


	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera.setZoomChangeListener(null);
			mCamera.setFaceDetectionListener(null);
			mCamera.setErrorCallback(null);

		}
		mCamera = null;
	}

    @Override
    public void log(boolean debug) throws Exception {

    }


    @Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		// TODO Auto-generated method stub

	}

	private int FindFrontCamera() {
		int cameraCount = 0;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras(); // get cameras number

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {

				return camIdx;
			}
		}
		return -1;
	}

	private int FindBackCamera() {
		int cameraCount = 0;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras(); // get cameras number

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				return camIdx;
			}
		}
		return -1;
	}

	private String imageid;

	public synchronized String getResultdata() {
		return imageid;
	}

	public synchronized void setResultdata(String imageid) {
		this.imageid = imageid;
	}

	Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
		// @Override
		public void onPictureTaken(byte[] data, Camera camera) {
			isphotograph = true;

			if (InitUtil.DEBUG)
				log.info("拍照完成准备上传");

			try {
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
						data.length);
				Matrix matrix = new Matrix();
				matrix.preRotate(90);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), matrix, true);

				File picture = FileUtil.getimagefile();

				FileOutputStream fos = new FileOutputStream(picture.getPath()); // Get
																				// file
																				// output
																				// stream
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
				if (InitUtil.DEBUG)
					log.info("文件存放在:" + picture.getPath());
				close();

				save(picture.getAbsolutePath());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	protected void save(String absolutePath) {
		// TODO Auto-generated method stub
		CameraPhoto cp = new CameraPhoto();
		String id = GenSequenceUtil.getFullDateRandomNum(4);
		cp.setId(id);
		cp.setTime(TimeUtil.getTime(TimeUtil.TIME1));
		if (mlocation != null) {
			cp.setLatitude(""+String.valueOf(mlocation.getLatitude()));
			cp.setLongitude(""+String.valueOf(mlocation.getLongitude()));
            cp.setAltitude("" + String.valueOf(mlocation.getAltitude()));
            cp.setSpeed("" + String.valueOf(mlocation.getSpeed()));
            cp.setBearing("" + String.valueOf(mlocation.getBearing()));
		}else{
			cp.setLatitude("0.0");
			cp.setLongitude("0.0");
            cp.setAltitude("0.0");
            cp.setSpeed("0.0");
            cp.setBearing("0.0");
		}
		cp.setPath(absolutePath);
		cdb.insertphoto(cp);
        JSONObject obj = new JSONObject();
        try {
            obj.put("code", 1002);
            obj.put("state", 1);
            obj.put("source", 2);//1  wifi手机，
            obj.put("imei", GPSBAIDUData.getInstance(mContext).getImei());//1  wifi手机，
            obj.put("key", "123");//1  wifi手机，
            obj.put("data", id);
            log.info("data:拍照成功。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。+"+id );
            if(Curchannel!=null)
            Curchannel.writeAndFlush(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}
	public CameraPhoto path(String id){
		return cdb.getcameraphoto(id);
	}
}
