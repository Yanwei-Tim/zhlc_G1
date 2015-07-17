package com.zhlt.g1app.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.baidu.cyberplayer.core.BVideoView;
import com.zhlt.g1app.R;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.data.CommonData;

import android.os.*;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

public class FrgCamera extends FrgBase implements OnClickListener,BVideoView.OnPreparedListener, BVideoView.OnCompletionListener,
        BVideoView.OnErrorListener, BVideoView.OnInfoListener, BVideoView.OnPlayingBufferCacheListener,BVideoView.OnNetworkSpeedListener {

	private View mRootView;
	private ViewPager mViewPager = null;
	
	private TextView mImageTv;
	private ImageView mVideoIv;
	private ImageView mImageIv;
    private ImageButton mPlayButton;

	private Runnable mRunnable = null;
	private Logger log4jUtil = Log4jUtil.getLogger("");
	private ArrayList<String> mAdvFragments = new ArrayList<String>();
	private RealHandler mHandler;
	private boolean mIsShowSuccess = false;
	private boolean mIsLoadSuccess = false;
    private  SurfaceView videoSarfaceView;
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
    private  enum PLAYER_STATUS {
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
    boolean isrealfinish =true;
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
                    //   mVV.setCacheBufferSize(1024);

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

                    //   int currPosition = mVV.getCurrentPosition();
                    //    int duration = mVV.getDuration();
                    //     updateTextViewWithTimeFormat(mCurrPostion, currPosition);
                    //    updateTextViewWithTimeFormat(mDuration, duration);
                    //   mProgress.setMax(duration);
                    //      mProgress.setProgress(currPosition);
                    //     mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
                    break;
                case UI_EVENT_PLAY_EERRO:

                    Toast.makeText(getActivity(),"server error",Toast.LENGTH_LONG).show();
                    videoSarfaceView.setVisibility(View.VISIBLE);
                    mPlayButton.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		log4jUtil.info("zzw  real onCreateView");
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.frg_camera, null);
		}
		mHandler = new RealHandler(this);
		initView();
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);// 先移除
		}
		mIsPrepare = true;
		return mRootView;
	}



    @Override
    public void onNetworkSpeedUpdate(int i) {

    }

    @Override
    public boolean onInfo(int what, int extra) {
        // TODO Auto-generated method stub
        switch(what){
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
			case CommonData.Message.MSG_ADV_TEXT_lOADFINISHED:
				fragment.setAdvView();
				break;

			default:
				break;
			}

		}
	}

	public void setAdvTextData(ArrayList<String> list) {
		mIsLoadSuccess = true;
		mAdvFragments = list;
		if (mIsPrepare) {
			mHandler.sendEmptyMessageDelayed(
					CommonData.Message.MSG_ADV_TEXT_lOADFINISHED, 1000);
		}
	}

	public void setAdvView() {
		mIsShowSuccess = true;

	}

	private void initView() {

		
		mImageTv = (TextView) mRootView.findViewById(R.id.tv_real_image);
		mVideoIv = (ImageView) mRootView.findViewById(R.id.iv_real_video);
		mImageIv = (ImageView) mRootView.findViewById(R.id.iv_real_image);
		
		mImageTv.setOnClickListener(this);
        initPlayerView();
	}
    private  void initPlayerView(){

        //init UI
        mPlayButton = (ImageButton) mRootView.findViewById(R.id.ibtn_real_startplay);
        mPlaybtn = (ImageButton) mRootView.findViewById(R.id.ibtn_real_pause);
        mPlaybtn.setImageResource(R.drawable.play_next);
        mPlayButton.setOnClickListener(this);
        mPlaybtn.setOnClickListener(this);
        mPlayButton.setVisibility(View.VISIBLE);
        videoSarfaceView = (SurfaceView) mRootView.findViewById(R.id.sfv_real);
        videoSarfaceView.setVisibility(View.VISIBLE);

        mVideoSource = "rtsp://192.168.43.1:1234"; //init play address

        /**
         * 设置ak及sk的前16位
         */
        BVideoView.setAKSK(AK, SK);

        /**
         *获取BVideoView对象
         */
        mVV = (BVideoView)mRootView.findViewById(R.id.video_view);
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
        mVV.setDecodeMode(BVideoView.DECODE_SW); //硬解
        /**
         * 开启后台事件处理线程
         */
        mHandlerThread = new HandlerThread("event handler thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());

    }

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
                //  mLastPos = mVV.getCurrentPosition();
                        mVV.stopPlayback();
                        videoSarfaceView.setVisibility(View.VISIBLE);
                        mPlayButton.setVisibility(View.VISIBLE);
                    }
                break;
		}

	}

}
