/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhlt.g1app.view;

import java.util.Vector;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.zhlt.g1app.R;
import com.zhlt.g1app.activity.ActCapture;




/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 *  对所有获取到信息进行处理
 *  关键字：Handler
 *  @decrption:
 */
public final class CaptureActivityHandler extends Handler {
   /** 标记 */
	private static final String TAG = CaptureActivityHandler.class
			.getSimpleName();
 /** 文件名ActCapture */
	private final ActCapture activity;
	/** 解码线程 */
	private final DecodeThread decodeThread;
	private State state;
   /** 枚举 */
	private enum State {
		PREVIEW, SUCCESS, DONE
	}
  /** 处理解码 */
	public CaptureActivityHandler(ActCapture activity,
			Vector<BarcodeFormat> decodeFormats, String characterSet) {
		this.activity = activity;
		/** 调用解码函数 */
		decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
				new ViewfinderResultPointCallback(null));
		/**启动线程 */
		decodeThread.start();
		state = State.SUCCESS;
		/**开始捕捉信息和预览效果  */
		CameraManager.get().startPreview();
		/** 释放 */
		restartPreviewAndDecode();
	}
 /** 关键字：Message
  *  返回值：无
  *  参数：message
  *  说明：通过message更新handler信息*/
	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case R.id.auto_focus:
		
			// Log.d(TAG, "Got auto-focus message");
			// When one auto focus pass finishes, start another. This is the
			// closest thing to
			// continuous AF. It does seem to hunt a bit, but I'm not sure what
			// else to do.
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
//				CameraManager.get().requestAutoFocus(this, R.id)
			}
			break;
		case R.id.restart_preview:
			Log.d(TAG, "Got restart preview message");
			restartPreviewAndDecode();
			break;
		case R.id.decode_succeeded:
			Log.d(TAG, "Got decode succeeded message");
			state = State.SUCCESS;
			Bundle bundle = message.getData();

			/***********************************************************************/
			Bitmap barcode = bundle == null ? null : (Bitmap) bundle
					.getParcelable(DecodeThread.BARCODE_BITMAP);

			activity.handleDecode((Result) message.obj, barcode);
			/***********************************************************************/
			break;
		case R.id.decode_failed:
			// We're decoding as fast as possible, so when one decode fails,
			// start another.
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			break;
		case R.id.return_scan_result:
			Log.d(TAG, "Got return scan result message");
			activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
			activity.finish();
			break;
		case R.id.launch_product_query:
			Log.d(TAG, "Got product query message");
			String url = (String) message.obj;
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			activity.startActivity(intent);
			break;
		}
	}
  /** 文件名：同步函数 
   *  关键字：decodethreader
   *  返回值：无
   *  释放cmaremanger预览
   *  中断操作*/
	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
		quit.sendToTarget();
		try {
			decodeThread.join();
		} catch (InterruptedException e) {
			// continue
		}

		// Be absolutely sure we don't send any queued up messages
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
	}
  /**  重置操作*/
	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			//activity.drawViewfinder();
		}
	}

}
