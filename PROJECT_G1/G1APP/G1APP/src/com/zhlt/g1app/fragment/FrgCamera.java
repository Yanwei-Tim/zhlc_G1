package com.zhlt.g1app.fragment;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.cyberplayer.core.BVideoView;
import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActShowPhoto;
import com.zhlt.g1app.adapter.AdpRealPhotoGridView;
import com.zhlt.g1app.application.AppBmap;
import com.zhlt.g1app.basefunc.Codes;
import com.zhlt.g1app.basefunc.InitUtil;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.basefunc.SocketUtil;
import com.zhlt.g1app.data.DataCommon;
import com.zhlt.g1app.func.NetUtils;
import com.zhlt.g1app.func.ShowPhotoCallBack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.*;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

public class FrgCamera extends FrgBase implements OnClickListener,
		BVideoView.OnPreparedListener, BVideoView.OnCompletionListener,
		BVideoView.OnErrorListener, BVideoView.OnInfoListener,
		BVideoView.OnPlayingBufferCacheListener,
		BVideoView.OnNetworkSpeedListener {

	private View mRootView;
	private ViewPager mViewPager = null;

	private TextView mImageTv;
	private ImageView mVideoIv;
	private ImageView mImageIv;
	private ImageButton mPictureIBtn;
	private AdpRealPhotoGridView mGridViewAdapter;
	private GridView mPhotoGridView;
	private static Logger mLogger = Log4jUtil.getLogger("Camera");
	private ImageButton mPlayButton;

	// 装载实时拍照的图片
	private ArrayList<Bitmap> mPhotoLists = new ArrayList<Bitmap>();

	private Runnable mRunnable = null;
	private Logger log4jUtil = Log4jUtil.getLogger("");
	private ArrayList<String> mAdvFragments = new ArrayList<String>();
	private RealHandler mHandler;
	private boolean mIsShowSuccess = false;
	private boolean mIsLoadSuccess = false;
	private Animation mPhotoShowAnim;
	private SurfaceView videoSarfaceView;
	private ImageButton mPlaybtn = null;
	private final String TAG = "FrgCamera";
	/**
	 * 您的ak
	 */
	private String AK = "BQZn8m8gC4G8r2jWSdOhgpff";
	/**
	 * 您的sk的前16位
	 */
	private String SK_full = "0T0PrP4CxeKyEhgb0ykP1K5gy1EgUzHC";
	private String SK = SK_full.substring(0, 16);
	/**
	 * 记录播放位置
	 */
	private int mLastPos = 0;

	/**
	 * 播放状态
	 */
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

	private BVideoView mVV = null;

	private EventHandler mEventHandler;
	private HandlerThread mHandlerThread;

	private final Object SYNC_Playing = new Object();

	private PowerManager.WakeLock mWakeLock = null;
	private static final String POWER_LOCK = "NewVideoViewPlayingActivity";

	private boolean mIsHwDecode = false;
	private final int EVENT_PLAY = 0;
	private final int UI_EVENT_UPDATE_CURRPOSITION = 1;
	private final int UI_EVENT_PLAY_EERRO = 2;
	boolean isrealfinish = true;
	private String mVideoSource = null;

	class EventHandler extends Handler {
		public EventHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENT_PLAY:
				/**
				 * 如果已经播放了，等待上一次播放结束
				 */
				if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
					synchronized (SYNC_Playing) {
						try {
							SYNC_Playing.wait();

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				/**
				 * 设置播放url
				 */
				mVV.setVideoPath(mVideoSource);
				/**
				 * 续播，如果需要如此
				 */
				if (mLastPos > 0) {

					mVV.seekTo(mLastPos);
					mLastPos = 0;
				}
				/**
				 * 显示或者隐藏缓冲提示
				 */
				mVV.showCacheInfo(true);
				// mVV.setCacheBufferSize(1024);

				/**
				 * 开始播放
				 */
				mVV.start();

				mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
				break;
			default:
				break;
			}
		}
	}

	Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/**
			 * 更新进度及时间
			 */
			case UI_EVENT_UPDATE_CURRPOSITION:

				// int currPosition = mVV.getCurrentPosition();
				// int duration = mVV.getDuration();
				// updateTextViewWithTimeFormat(mCurrPostion, currPosition);
				// updateTextViewWithTimeFormat(mDuration, duration);
				// mProgress.setMax(duration);
				// mProgress.setProgress(currPosition);
				// mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION,
				// 200);
				break;
			case UI_EVENT_PLAY_EERRO:

				Toast.makeText(getActivity(), "server error", Toast.LENGTH_LONG)
						.show();
				videoSarfaceView.setVisibility(View.VISIBLE);
				mPlayButton.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	};

	private TextView mVideoTv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		log4jUtil.info("zzw  real onCreateView");
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.frg_camera, null);
		}
		mHandler = new RealHandler(this);
		initView();
		initpc();
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);// 先移除
		}
		mIsPrepare = true;
		return mRootView;
	}

	private void initpc() {
		// TODO Auto-generated method stub
		registerBoradcastReceiver();
	}

	@Override
	public void onNetworkSpeedUpdate(int i) {

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onInfo(int what, int extra) {
		// TODO Auto-generated method stub
		switch (what) {
		/**
		 * 开始缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_START:
			break;
		/**
		 * 结束缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_END:
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
	 */
	@Override
	public void onPlayingBufferCache(int percent) {
		// TODO Auto-generated method stub

	}

	/**
	 * 播放出错
	 */
	@Override
	public boolean onError(int what, int extra) {
		// TODO Auto-generated method stub

		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		mUIHandler.sendEmptyMessage(UI_EVENT_PLAY_EERRO);
		mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
		return true;
	}

	/**
	 * 播放完成
	 */
	@Override
	public void onCompletion() {
		// TODO Auto-generated method stub

		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);

	}

	/**
	 * 准备播放就绪
	 */
	@Override
	public void onPrepared() {
		// TODO Auto-generated method stub

		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
		mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
	}

	private static class RealHandler extends Handler {

		private WeakReference<FrgCamera> reference;

		public RealHandler(FrgCamera fragment) {
			reference = new WeakReference<FrgCamera>(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			FrgCamera fragment = reference.get();
			if (fragment == null) {
				return;
			}
			switch (msg.what) {
			case DataCommon.Message.MSG_ADV_TEXT_lOADFINISHED:
				fragment.setAdvView();
				break;
			case DataCommon.Message.MSG_PICTURE_FINISH:
				fragment.getPhoto(msg.obj);
				break;
			case DataCommon.Message.MSG_LOAD_SOCKET_BITMAP_FINISH:
				fragment.updatePhotoGridView(msg.obj);
				break;

			}

		}
	}

	private void updatePhotoGridView(Object obj) {
		mLogger.info("updatePhotoGridView");
		Bitmap bm = (Bitmap) obj;
		Matrix matrix = new Matrix();
		matrix.preRotate(90);
		bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
				matrix, true);
		mPhotoLists.add(bm);
		mGridViewAdapter.setData(mPhotoLists);
	}

	private void getPhoto(Object obj) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String path = jsonObject.optString("data");
		if (!TextUtils.isEmpty(path)) {
			getPhotoFromPath(path);
		} else {

		}
	}

	/**
	 * 
	 * @param path
	 */
	private void getPhotoFromPath(final String path) {
		AppBmap.getInstance().getFixedThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				Socket request = null;
				try {

					request = new Socket("192.168.43.1", 9999);

					JSONObject obj = new JSONObject();
					obj.put("code", InitUtil.CODE12);
					obj.put("state", 1);// 1,打开，2关闭 3,log
					obj.put("source", 1);// 1 wifi手机，2服务器
					obj.put("data", path);// 返回的图片地址
					String codestr = obj.toString().trim();
					while (true) {
						// write response info

						if (codestr != null) {
							SocketUtil.writeStr2StreamVideo(codestr.getBytes(),
									request.getOutputStream());
							if (InitUtil.DEBUG)
								mLogger.info("发送指令:" + codestr);
							codestr = null;

						}
						// get info from request when getting a socket request
						byte[] reqStr = SocketUtil
								.readStrFromStreamdatabyte(request
										.getInputStream());
						if (reqStr != null && reqStr.length > 0) {
							if (InitUtil.DEBUG)
								mLogger.info("接受到图片:" + reqStr.length);
							Bitmap bitmap = BitmapFactory.decodeByteArray(
									reqStr, 0, reqStr.length);
							if (bitmap != null) {
								mHandler.obtainMessage(
										DataCommon.Message.MSG_LOAD_SOCKET_BITMAP_FINISH,
										bitmap).sendToTarget();
								break;
							}
						} else {
							if (InitUtil.DEBUG)
								mLogger.info("=====等于null");
						}
						Thread.sleep(1000);
					}
				} catch (IOException e) {
					if (InitUtil.DEBUG)
						mLogger.error("G-sensor：指令" + e.getStackTrace());
				} catch (Throwable e) {
					if (InitUtil.DEBUG)
						mLogger.error("G-sensor：指令" + e.getStackTrace());
				} finally {
					if (request != null) {
						try {
							// request.getInputStream().close();
							// request.getOutputStream().close();
							request.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}
		});

	}

	public void setAdvTextData(ArrayList<String> list) {
		mIsLoadSuccess = true;
		mAdvFragments = list;
		if (mIsPrepare) {
			mHandler.sendEmptyMessageDelayed(
					DataCommon.Message.MSG_ADV_TEXT_lOADFINISHED, 1000);
		}
	}

	public void setAdvView() {
		mIsShowSuccess = true;

	}

	private void initView() {

		mPhotoGridView = (GridView) mRootView.findViewById(R.id.gv_real);
		mGridViewAdapter = new AdpRealPhotoGridView(getActivity());
		mGridViewAdapter.setPhotoListener(mShowPhotoCallBack);
		mPhotoGridView.setAdapter(mGridViewAdapter);
		mVideoTv = (TextView) mRootView.findViewById(R.id.tv_real_video);
		mImageTv = (TextView) mRootView.findViewById(R.id.tv_real_image);
		mVideoIv = (ImageView) mRootView.findViewById(R.id.iv_real_video);
		mImageIv = (ImageView) mRootView.findViewById(R.id.iv_real_image);
		mPictureIBtn = (ImageButton) mRootView
				.findViewById(R.id.ibtn_real_picture);
		mVideoTv.setOnClickListener(this);
		mPictureIBtn.setOnClickListener(this);
		mImageTv.setOnClickListener(this);
		initPlayerView();
	}

	private void initPlayerView() {

		// init UI
		mPlayButton = (ImageButton) mRootView
				.findViewById(R.id.ibtn_real_startplay);
		mPlaybtn = (ImageButton) mRootView.findViewById(R.id.ibtn_real_pause);
		mPlaybtn.setImageResource(R.drawable.play_next);
		mPlayButton.setOnClickListener(this);
		mPlaybtn.setOnClickListener(this);
		mPlayButton.setVisibility(View.VISIBLE);
		videoSarfaceView = (SurfaceView) mRootView.findViewById(R.id.sfv_real);
		videoSarfaceView.setVisibility(View.VISIBLE);

		mVideoSource = "rtsp://192.168.43.1:1234"; // init play address

		/**
		 * 设置ak及sk的前16位
		 */
		BVideoView.setAKSK(AK, SK);

		/**
		 * 获取BVideoView对象
		 */
		mVV = (BVideoView) mRootView.findViewById(R.id.video_view);
		mVV.setVisibility(View.GONE);

		/**
		 * 注册listener
		 */
		mVV.setOnPreparedListener(this);
		mVV.setOnCompletionListener(this);
		mVV.setOnErrorListener(this);
		mVV.setOnInfoListener(this);
		mVV.setOnNetworkSpeedListener(this);
		/**
		 * 设置解码模式
		 */
		mVV.setDecodeMode(BVideoView.DECODE_SW); // 硬解
		/**
		 * 开启后台事件处理线程
		 */
		mHandlerThread = new HandlerThread("event handler thread",
				android.os.Process.THREAD_PRIORITY_BACKGROUND);
		mHandlerThread.start();
		mEventHandler = new EventHandler(mHandlerThread.getLooper());

	}

	private ShowPhotoCallBack mShowPhotoCallBack = new ShowPhotoCallBack() {

		@Override
		public void show(Bitmap bitmap) {
			showPhoto(bitmap);
		}
	};

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tv_real_image:
			mVideoIv.setImageResource(R.drawable.line_dark);
			mImageIv.setImageResource(R.drawable.line_blue);
			break;
		case R.id.ibtn_real_picture:
			//startsensor(InitUtil.CODE2);
			takePicture();
			break;
		case R.id.ibtn_real_startplay:
			videoSarfaceView.setVisibility(View.GONE);
			mPlayButton.setVisibility(View.GONE);
			mVV.setVisibility(View.VISIBLE);
			mEventHandler.sendEmptyMessage(EVENT_PLAY);
			break;
		case R.id.ibtn_real_pause:
			if (mVV.isPlaying()) {
				mPlaybtn.setImageResource(R.drawable.pause);
				/**
				 * 暂停播放
				 */
				mVV.pause();
				// mLastPos = mVV.getCurrentPosition();
				mVV.stopPlayback();
				videoSarfaceView.setVisibility(View.VISIBLE);
				mPlayButton.setVisibility(View.VISIBLE);
			}
			break;
	
		}

	}

	private void takePicture(){


		JSONObject obj = new JSONObject();
		try {
			obj.put("code", Codes.CODE1001);
			obj.put("source", 1);
			obj.put("key", InitUtil.KEY);
			System.out.println("send:" + obj.toString());
			Intent mIntent = new Intent();
			mIntent.setAction("com.example.mytest");
			mIntent.putExtra("msg", obj.toString());
			// 发送广播
			getActivity().sendBroadcast(mIntent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	
	}
	
	private void startsensor(final int code) {

		AppBmap.getInstance().getFixedThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				Socket request = null;
				try {

					request = new Socket("192.168.43.1", 9999);

					JSONObject obj = new JSONObject();
					obj.put("code", code);
					obj.put("state", 1);// 1,打开，2关闭 3,log
					obj.put("source", 1);// 1 wifi手机，2服务器
					if (code == InitUtil.CODE6) {
						JSONObject objjt = new JSONObject();
						objjt.put("start", "2015-06-01 09:09:00");// 查询历史记录的时候用得，开始时间
						objjt.put("end", "2015-06-09 09:09:00");// 结束时间
						obj.put("data", objjt); // 传递的参数
					}
					String codestr = obj.toString().trim();
					while (true) {
						// write response info

						if (codestr != null) {
							SocketUtil.writeStr2StreamVideo(codestr.getBytes(),
									request.getOutputStream());
							if (InitUtil.DEBUG)
								mLogger.info("发送指令:" + codestr);
							codestr = null;

						}
						// get info from request when getting a socket request
						byte[] reqStr = SocketUtil
								.readStrFromStreamdatabyte(request
										.getInputStream());
						if (reqStr != null && reqStr.length > 0) {

							String result = new String(reqStr).trim();
							if (InitUtil.DEBUG)
								mLogger.info("接收到:" + result);
							if (result != null && !result.equals("")
									&& !result.equals("null")) {
								if (code == InitUtil.CODE2) {
									mHandler.obtainMessage(
											DataCommon.Message.MSG_PICTURE_FINISH,
											result).sendToTarget();
								} else {
									mHandler.obtainMessage(1, result)
											.sendToTarget();
								}
								break;
							}

							// mhandler.obtainMessage(3,reqStr).sendToTarget();
						} else {
							System.out.println("=====等于null");
						}
						Thread.sleep(1000);
					}
				} catch (IOException e) {
					if (InitUtil.DEBUG)
						mLogger.error("G-sensor：指令" + e.getLocalizedMessage());
				} catch (Throwable e) {
					if (InitUtil.DEBUG)
						mLogger.error("G-sensor：指令" + e.getLocalizedMessage());
				} finally {
					if (request != null) {
						try {
							// request.getInputStream().close();
							// request.getOutputStream().close();
							request.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}
		});
	}

	/***
	 * 
	 */
	private void showPhoto(Bitmap bitmap) {
		mLogger.info("showPhoto");
		// Intent mIntent = new Intent(getActivity(), ActShowPhoto.class);
		// mIntent.putExtra("pic", bitmap);
		// startActivity(mIntent);
	}
	
	public void NettyResult(String msg) {
		log4jUtil.info("client接收到服务器返回的消息:" + msg.toString());
		JSONObject json = null;
		if (msg != null) {

			try {
				json = new JSONObject(msg.toString().trim());

				int code = json.optInt("code");
				log4jUtil.info("code =======" + code);
				switch (code) {
				case Codes.CODE1003:
					String data=json.optString("data");
					addimg(data);
			     	
					
					break;
				case Codes.CODE4001:
					Toast.makeText(getActivity(), "car 不在线!", Toast.LENGTH_LONG).show();
					break;
					default:
						break;
				
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("client接收到服务器返回的消息:null");
			// startupgps(Codes.CODE1005, socket);
		}
	}

	
	
	
	
	private void addimg(final String data) {
		// TODO Auto-generated method stub
		AppBmap.getInstance().getFixedThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				byte[]	b = NetUtils.downloadImage(data, 5000);
			    Bitmap	bitmap = BitmapFactory.decodeByteArray(b, 0,
					b.length);
			    mHandler.obtainMessage(DataCommon.Message.MSG_LOAD_SOCKET_BITMAP_FINISH,bitmap).sendToTarget();
				
				
			}});
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(InitUtil.ACTION_NAME)) {
				String msg = intent.getStringExtra("msg");
				NettyResult(msg);
				log4jUtil.info("result:" + msg);
				/*
				 * Toast.makeText(getActivity(), "广播" + msg, Toast.LENGTH_LONG)
				 * .show();
				 */
			}
		}

	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(InitUtil.ACTION_NAME);

		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

}
