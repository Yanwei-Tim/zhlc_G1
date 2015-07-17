package com.zhlt.g1app.basefunc.netty;


import android.content.Context;


/**
 * 
 ** Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd
 ** All rights reserved.
 ** http://www.gobabymobile.cn/
 ** File: - MainActivity.java
 ** Description:描述
 **     
 **
 ** ------------------------------- Revision History: -------------------------------------
 ** <author>                             <data>             <version>            <desc>
 ** ---------------------------------------------------------------------------------------
 ** yuanpeng@gobabymobile.cn        2015-7-6 下午5:34:30     1.0         Create this moudle
 */
public class APPManagerThread extends Thread{


    private static APPManagerThread instance;
    APPClientNetty client;
  
    Context mContext;
    private APPManagerThread(Context context) {
           this.mContext = context;
           start();
    }

    public static APPManagerThread getInstance(Context context) {
        if (instance == null) {
            instance = new APPManagerThread(context);
        }
        return instance;
    }


    @Override
    public void run() {
        super.run();
       client = new APPClientNetty(mContext);
    }

}
