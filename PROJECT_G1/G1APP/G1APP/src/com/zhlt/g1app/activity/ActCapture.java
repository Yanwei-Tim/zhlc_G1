package com.zhlt.g1app.activity;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.zhlt.g1app.R;
import com.zhlt.g1app.application.AppBmap;
import com.zhlt.g1app.basefunc.Log4jUtil;
import com.zhlt.g1app.fragment.FrgActMain;
import com.zhlt.g1app.view.CameraManager;
import com.zhlt.g1app.view.CaptureActivityHandler;
import com.zhlt.g1app.view.InactivityTimer;

/**
 * 
 * @Pamart: Initial the camera FileName:ActCapture实现回调接口
 * @description:User
 * @Time:2015-6-18
 * 
 * 
 */
public class ActCapture extends Activity implements Callback, OnClickListener {
	/** 结果码 */
	private static final int RESULT_CODE = 1;
	/** 初始化CaptureActivityAHandler对象 */
	private CaptureActivityHandler handler;
	/** boolean型值 */
	private boolean hasSurface;
	/** 映射BarcodeFormat */
	private Vector<BarcodeFormat> decodeFormats;
	/** 定义String类型characterSet */
	private String characterSet;
	/** 声明InactivityTimer类 */
	private InactivityTimer inactivityTimer;
	/** 声明媒体播放器对象 */
	private MediaPlayer mediaPlayer;
	/** 为鸣笛类型 */
	private boolean playBeep;
	/** 设定声音响定幅度数 */
	private static final float BEEP_VOLUME = 0.10f;
	/** 震动模式 */
	private boolean vibrate;
	/** 声明TextView控件 */
	private TextView textView_main_act;
	/** 声明CameraManger类 */
	CameraManager mCameraManager;
	/** 声明ImageView控件 */
	private ImageView mImageView;
	/** 声明ImageView控件 */
	private ImageView mImageView2_home_history;
	/** 获取到动画效果模式 */
	private TranslateAnimation animation;
	TextView mTitleTv;
	private View mBackView;
	private TextView mPassTv;

	/**
	 * Called when the activity is first created. 第一次创建Activity时创建周期函数 并添加函数
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		Log4jUtil.getLogger("==Acpture.java==");
		/** 第一次调用该函数时初始化CameraManger类 */
		CameraManager.init(getApplication());
		AppBmap.getInstance().addActivity(this);
		/** 方法 */
		InitView();
		hasSurface = false;
		/** 初始化InactivityTimer对象 */
		inactivityTimer = new InactivityTimer(this);
	}

	private void InitView() {
		/** 初始化 activity_capture .xml控件并为其控件添加监听事件 */
		// textView_main_act = (TextView) this.findViewById(R.id.textView1);
		// textView_main_act.setOnClickListener(this);
		mTitleTv = (TextView) findViewById(R.id.r_tv_title_text);
		mTitleTv.setText(R.string.registerg1);
		mBackView = findViewById(R.id.r_ib_title_left);
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(this);
		mImageView = (ImageView) findViewById(R.id.iv_scan_line);
		mPassTv = (TextView) findViewById(R.id.tv_capture_pass);
		mImageView2_home_history = (ImageView) findViewById(R.id.imageview_home_history);
		mImageView2_home_history.setOnClickListener(this);
		mImageView.setOnClickListener(this);
		mPassTv.setOnClickListener(this);
		hasSurface = false;
		/** 调用InactivityTimer（）构造传入当前对象 */
		inactivityTimer = new InactivityTimer(this);
		/** 动画时长 */

	}

	/**
	 * 周期方法* OnResume() OnPaunse() OnDestory()
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		animation = new TranslateAnimation(0, 0, 0, getResources()
				.getDimensionPixelOffset(R.dimen.capture_anim_height));
		animation.setDuration(1500);
		/** 重复移动 */
		animation.setRepeatCount(Animation.START_ON_FIRST_FRAME);
		/** 设置动画 */
		mImageView.startAnimation(animation);
		/** 初始化surfaceView对象 */
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		/** surfaceHolder接收surfaceView */
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		/**  */
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			/** surfaceHolder添加回调 */
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		/** 调用声频管理对象 */
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		// 设置警笛的声音函数
		// vibrate = true;
		vibrate = false;

	}

	@Override
	protected void onPause() {

		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onActivity();
		/** 关闭Camear设备 */
		CameraManager.get().closeDriver();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * FrgActMain 扫描的结果码
	 * 
	 * @param result
	 * @param barcode
	 * @return 无 解码 ——Paramter Result, Bitmap 关键 intent，bundle对象 实现 数据传递
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		/** 播放SoundAndVibrate函数 */
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(ActCapture.this, "Scan failed!", Toast.LENGTH_SHORT)
					.show();
		} else {
			Intent resultIntent = new Intent(this, FrgActMain.class);
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			bundle.putParcelable("bitmap", barcode);
			resultIntent.putExtras(bundle);
			startActivity(resultIntent);
			Toast.makeText(this, "resultString" + resultString,
					Toast.LENGTH_SHORT).show();
			System.out.println("===result" + result + barcode);
			this.setResult(RESULT_OK, resultIntent);
		}
		// 释放ActCapture对象
		ActCapture.this.finish();

	}

	/** 初始化Cmaera类 **/

	private void initCamera(SurfaceHolder surfaceHolder) {

		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}

		try {
			/** 开启设备 */
			CameraManager.get().openDriver(surfaceHolder);

			// CameraManager.get().openLight(); // 开闪光灯
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);

		}

	}

	/**
	 * 实现回调方法实现函数 从创建到销毁的过程
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	// public ViewfinderView getViewfinderView() {
	// return viewfinderView;
	// }
	public SurfaceView getViewfinderView() {
		return null;

	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		// viewfinderView.drawViewfinder();

	}

	/** 初始化声音函数 ***/
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}

	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_scan_line:
			ImageView();

			break;
		case R.id.imageview_home_history:
			IamgeView_home_history();
		default:
			break;
		case R.id.textView1:
			// Intent resultIntent = new Intent(this, FrgActMain.class);
			// startActivity(resultIntent);
			break;
		case R.id.r_ib_title_left:
			finish();
			break;
		case R.id.tv_capture_pass:
			enterMain();
			break;
		}

	}

	/**
	 * 功能：进入主界面
	 */
	private void enterMain() {
		Intent lIntent = new Intent(this, FrgActMain.class);
		// Intent lIntent = new Intent(this, OverlayDemo.class);
		startActivity(lIntent);
		// overridePendingTransition(R.anim.anim_alpha_in,
		// R.anim.anim_alpha_out);
		finish();
	}

	/**
	 * 初始化ImageView设定当前函数 动画效果
	 ***/

	public void ImageView() {
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		animation.setDuration(1000);
		animation.setRepeatCount(Animation.RESTART);
		mImageView.startAnimation(animation);
	}

	public void IamgeView_home_history() {
		Intent intent = new Intent(ActCapture.this, ActCode.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	/**
	 * 开启闪光灯 * 当需要时候可以适当添加调用 CmareManger.java
	 */

}