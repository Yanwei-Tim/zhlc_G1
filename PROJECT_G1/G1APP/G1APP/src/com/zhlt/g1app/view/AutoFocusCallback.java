/*
 * Copyright (C) 2010 ZXing authors
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

import java.util.ArrayList;
import java.util.List;

import com.zhlt.g1app.basefunc.Log4jUtil;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
/***实现焦点回调函数接口**/

/**
 * Callback interface used to notify on completion of camera auto focus.
 *
 * <p>Devices that do not support auto-focus will receive a "fake"
 * callback to this interface. If your application needs auto-focus and
 * should not be installed on devices <em>without</em> auto-focus, you must
 * declare that your app uses the
 * {@code android.hardware.camera.autofocus} feature, in the
 * <a href="{@docRoot}guide/topics/manifest/uses-feature-element.html">&lt;uses-feature></a>
 * manifest element.</p>
 *
 * @see #autoFocus(AutoFocusCallback)
 */

final class AutoFocusCallback implements Camera.AutoFocusCallback {

  private static final String TAG = AutoFocusCallback.class.getSimpleName();
   //自动区间对焦
  private static final long AUTOFOCUS_INTERVAL_MS = 1500L;
  //焦点处理者
  private Handler autoFocusHandler;
  //焦点信息处理者
  private int autoFocusMessage;

  void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
    this.autoFocusHandler = autoFocusHandler;
    this.autoFocusMessage = autoFocusMessage;
  }
/***自动聚焦***/
  @SuppressLint("NewApi")
public void onAutoFocus(boolean success, Camera camera) {
	  if(success){
		  Log4jUtil.getLogger("对焦成功");
		  
	  }else{
		  Camera.Parameters parameters=camera.getParameters();
		  if(parameters.getMaxNumFocusAreas()>0){
			 Log4jUtil.getLogger("支持测光区域");
			 List<Camera.Area>meteringAreas=new ArrayList<Camera.Area>();
			 Rect areaRect1=new Rect(-100, -100, 100, 100);
			 //设置宽度到60%
			 meteringAreas.add(new Camera.Area(areaRect1, 600));
			 //在图像的右上角指定一个区域
			 Rect areaRect2=new Rect(800, -1000, 1000, -800);
			 meteringAreas.add(new Camera.Area(areaRect1, 400));
			 //设置感光角度
			 parameters.setMeteringAreas(meteringAreas);
			 //设置焦度
			 parameters.setFocusAreas(meteringAreas);
		  }
//		  camera.setParameters(parameters);
		  Log4jUtil.getLogger("对焦失败");
	  }
	  
	  
    if (autoFocusHandler != null) {
      Message message = autoFocusHandler.obtainMessage(autoFocusMessage, success);
      autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
      autoFocusHandler = null;
    } else {
      Log.d(TAG, "Got auto-focus callback, but no handler for it");
    }
  }

}
