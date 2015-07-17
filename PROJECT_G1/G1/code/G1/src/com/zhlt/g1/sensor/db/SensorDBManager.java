package com.zhlt.g1.sensor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhlt.g1.db.DBOpenHelper;
import com.zhlt.g1.sensor.bean.GSensorVO;

public class SensorDBManager {
	DBOpenHelper helper = null;
	SQLiteDatabase db = null;

	// 创建构造传值对象
	public SensorDBManager(Context context) {
		helper = new DBOpenHelper(context);
	}

	public void insertSensor(GSensorVO gvo) {
		String sql = "insert into g_sensor(id,sensorX,sensorY,sensorZ,time)values(?,?,?,?,?)";
		try {
			db = helper.getReadableDatabase();
			db.execSQL(
					sql,
					new Object[] { gvo.getId(), gvo.getSensorX(),
							gvo.getSensorY(), gvo.getSensorZ(), gvo.getTime() });

		} catch (Exception e) {
			e.printStackTrace();
			// Log4jUtil.getLogger().error("insertSensor:"+e.getStackTrace());
		} finally {
			if (db != null)
				db.close();
		}

	}
}
