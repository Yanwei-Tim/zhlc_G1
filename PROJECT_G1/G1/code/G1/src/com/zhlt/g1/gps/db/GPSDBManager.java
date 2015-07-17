/**
 * 
 */
package com.zhlt.g1.gps.db;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.zhlt.g1.db.DBOpenHelper;
import com.zhlt.g1.gps.bean.GPSVO;
import com.zhlt.g1.util.Log4jUtil;
import com.zhlt.g1.util.TimeUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @email 313044509@qq.com
 * @author kenneth 数据库操作类
 */
public class GPSDBManager {
	DBOpenHelper helper = null;
	SQLiteDatabase dbr = null,dbw=null;
	 private static final Logger logger = Logger.getLogger("GPSDBManager.class");
    private boolean Debug = false;
	// 创建构造传值对象
	public GPSDBManager(DBOpenHelper db,boolean debug) {
		helper = db;
        Debug = debug;

	}

	public void insertGPS(GPSVO gs) {
        if(Debug)
            logger.info("insertGPS /////...................................... =");
		String sql = "insert into gps(id,latitude,longitude,altitude,time,bearing,speed ,state)values(?,?,?,?,?,?,?,1)";
		try {
			dbw = helper.getReadableDatabase();
			dbw.execSQL(
					sql,
					new Object[] { gs.getId(), gs.getLatitude(),
							gs.getLongitude(), gs.getAltitude(),gs.getTime(),gs.getBearing(),gs.getSpeed() });

		} catch (Exception e) {
			e.printStackTrace();
			// Log4jUtil.getLogger().error("insertSensor:"+e.getStackTrace());
		}

	}
    public void insertGPSArray(ArrayList<GPSVO> gs) {
        if(Debug)
            logger.info("insertGPS /////...................................... =");
        String sql = "insert into gps(id,latitude,longitude,altitude,time,bearing,speed ,state)values(?,?,?,?,?,?,?,1)";
        try {
            dbw = helper.getReadableDatabase();
            for (int i = 0; i < gs.size(); i++) {
                GPSVO  m_gs  =   gs.get(i);
                dbw.execSQL(
                        sql,
                        new Object[]{m_gs.getId(), m_gs.getLatitude(),
                                m_gs.getLongitude(), m_gs.getAltitude(), m_gs.getTime(), m_gs.getBearing(), m_gs.getSpeed()});
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log4jUtil.getLogger().error("insertSensor:"+e.getStackTrace());
        }

    }
	public ArrayList<GPSVO> selectlistGPS(String start,String end ) {
		ArrayList<GPSVO> gpss = new ArrayList<GPSVO>();
		//String sql = "SELECT id,latitude,longitude,altitude,time  FROM gps Where  time >= '"+start+"' and time <= '"+end+"'  group by latitude,longitude,altitude  ORDER by time   ";
		String sql = "SELECT id,latitude,longitude,altitude,time,bearing,speed ,state  FROM gps Where  time >= '"+start+"' and time <= '"+end+"'   ORDER by time   ";
		
		try {
			dbr = helper.getWritableDatabase();
			Cursor cursor = dbr.rawQuery(sql, null);
			GPSVO g = null;
			while (cursor.moveToNext()) {
				g = new GPSVO();
				g.setId(cursor.getString(0));
				g.setLatitude(cursor.getString(1));
				g.setLongitude(cursor.getString(2));
				g.setAltitude(cursor.getString(3));
				g.setTime(cursor.getString(4));
				g.setBearing(cursor.getString(5));
				g.setSpeed(cursor.getString(6));
				g.setState(cursor.getInt(7));
				gpss.add(g);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		} 

		return gpss;
	}
	public void deletegps(){
		
	}
	public ArrayList<GPSVO> selectlistUpGPS(int state) {
		ArrayList<GPSVO> gpss = new ArrayList<GPSVO>();
		//String sql = "SELECT id,latitude,longitude,altitude,time  FROM gps Where  time >= '"+start+"' and time <= '"+end+"'  group by latitude,longitude,altitude  ORDER by time   ";
		String sql = "SELECT id,latitude,longitude,altitude,time,bearing,speed ,state  FROM gps Where  state = "+state;
		
		try {
			dbr = helper.getWritableDatabase();
			Cursor cursor = dbr.rawQuery(sql, null);
			GPSVO g = null;
			while (cursor.moveToNext()) {
				g = new GPSVO();
				g.setId(cursor.getString(0));
				g.setLatitude(cursor.getString(1));
				g.setLongitude(cursor.getString(2));
				g.setAltitude(cursor.getString(3));
				g.setTime(cursor.getString(4));
				g.setBearing(cursor.getString(5));
				g.setSpeed(cursor.getString(6));
				g.setState(cursor.getInt(7));
				gpss.add(g);
			}
            if(Debug)
			logger.info("查询 state size ="+gpss.size());
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		} 

		return gpss;
	}

	public void updatalistupgpsArray(ArrayList<GPSVO> gs) {


		try {
			dbw = helper.getReadableDatabase();
			for (int i = 0; i < gs.size(); i++) {
				String sql = "update  gps  set state = 2   where id = ?";
				dbw.execSQL(
						sql,
						new Object[] { gs.get(i).getId()});
                if(Debug)
                    logger.info(sql);
			}
            if(Debug)
          logger.info("修改 state  size ="+gs.size());
		} catch (Exception e) {
			e.printStackTrace();
			// Log4jUtil.getLogger().error("insertSensor:"+e.getStackTrace());
		}

	}
    public void updatalistupgps(GPSVO gs) {


        try {
            dbw = helper.getReadableDatabase();

            String sql = "update  gps  set state = 2   where id = ?";
            dbw.execSQL(
                    sql,
                    new Object[] { gs.getId()});
            logger.info(sql);

            logger.info("修改 state  size ="+gs);
        } catch (Exception e) {
            e.printStackTrace();
            // Log4jUtil.getLogger().error("insertSensor:"+e.getStackTrace());
        }

    }
}
