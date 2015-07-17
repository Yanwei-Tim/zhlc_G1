package com.zhlt.g1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
  ** Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd
 ** All rights reserved.
 ** http://www.gobabymobile.cn/
 ** File: - DBOpenHelper.java
 ** Description: 数据库操作
 **     
 **
 ** ------------------------------- Revision History: -------------------------------------
 ** <author>                             <data>             <version>            <desc>
 ** ---------------------------------------------------------------------------------------
 ** yuanpeng@gobabymobile.cn        2015-6-23  下午7:15:35     1.0         Create this moudle
 */
public class DBOpenHelper extends SQLiteOpenHelper {
	private static final int Version = 1;
	private static final String DataName = "g1.db";
    private static DBOpenHelper instance;
	public DBOpenHelper(Context context) {
		super(context, DataName, null, Version);
		// TODO Auto-generated constructor stub
	}
    public static DBOpenHelper getInstance(Context c) {
        if (instance == null) {
            instance = new DBOpenHelper(c);
        }
        return instance;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
        System.out.println("chuangjiang...................................../////////////sss");
	     //GPS
		String gps = "create table IF NOT EXISTS gps(id VARCHAR(50) PRIMARY KEY,latitude  VARCHAR(20)  NOT NULL, longitude  VARCHAR(20)  NOT NULL,altitude VARCHAR(20)  NOT NULL ,bearing VARCHAR(20)  NOT NULL ,speed VARCHAR(20)  NOT NULL  ,time DATETIME,state INT)";
		//图
		String photo="create table IF NOT EXISTS cameraphoto(id VARCHAR(50)PRIMARY KEY,latitude  VARCHAR(20)  NOT NULL, longitude  VARCHAR(20)  NOT NULL,path VARCHAR(100)  NOT NULL  ,time DATETIME,altitude  VARCHAR(20)  NOT NULL,bearing  VARCHAR(20)  NOT NULL,speed  VARCHAR(20)  NOT NULL)";
	    //视频
        String video="create table IF NOT EXISTS videodata(id VARCHAR(50)PRIMARY KEY,path VARCHAR(100)  NOT NULL  ,starttime DATETIME,endtime DATETIME,isupload INT)";

        db.execSQL(gps);
		db.execSQL(photo);
        db.execSQL(video);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("chuangjiang......................................................");
		db.execSQL("DROP TABLE IF EXISTS " + DataName);
		onCreate(db);

	}

}
