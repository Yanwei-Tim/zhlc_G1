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

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

final class PreviewCallback implements Camera.PreviewCallback {
/** 预览回调接口
 *   关键字：PreviewCallback of interface
 *   return:无*/
  private static final String TAG = PreviewCallback.class.getSimpleName();
/**创建摄像机管理类  */
  private final CameraConfigurationManager configManager;
 /** 设定用户回调判断值*/ 
  private final boolean useOneShotPreviewCallback;
  /** 预览处理 */
  private Handler previewHandler;
  /** 预览信息 */
  private int previewMessage;
/** 添加预览回调参数值 */
  PreviewCallback(CameraConfigurationManager configManager, boolean useOneShotPreviewCallback) {
    this.configManager = configManager;
    this.useOneShotPreviewCallback = useOneShotPreviewCallback;
  }
 /** 设定预览处理预览信息
  * return：无
  *  */
  void setHandler(Handler previewHandler, int previewMessage) {
    this.previewHandler = previewHandler;
    this.previewMessage = previewMessage;
  }
/** 设定预览模式
 * return:无 */
  public void onPreviewFrame(byte[] data, Camera camera) {
    Point cameraResolution = configManager.getCameraResolution();
    if (!useOneShotPreviewCallback) {
      camera.setPreviewCallback(null);
    }
    if (previewHandler != null) {
      Message message = previewHandler.obtainMessage(previewMessage, cameraResolution.x,
          cameraResolution.y, data);
      message.sendToTarget();
      previewHandler = null;
    } else {
      Log.d(TAG, "Got preview callback, but no handler for it");
    }
  }

}
