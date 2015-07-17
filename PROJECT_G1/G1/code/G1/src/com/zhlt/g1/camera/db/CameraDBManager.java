package com.zhlt.g1.camera.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhlt.g1.camera.CameraVideoInfo;
import com.zhlt.g1.camera.bean.CameraPhoto;
import com.zhlt.g1.camera.bean.CameraVideo;
import com.zhlt.g1.db.DBOpenHelper;
import com.zhlt.g1.gps.bean.GPSVO;

public class CameraDBManager {
	DBOpenHelper helper = null;
	SQLiteDatabase db = null;

	// 创建构造传值对象
	public CameraDBManager(DBOpenHelper db) {
		helper = db;
	}

	public void insertphoto(CameraPhoto gs) {

		String sql = "insert into cameraphoto(id,latitude,longitude,path,time,altitude,bearing,speed)values(?,?,?,?,?,?,?,?)";
		try {
			db = helper.getReadableDatabase();
			db.execSQL(
					sql,
					new Object[] { gs.getId(), gs.getLatitude(),
							gs.getLongitude(), gs.getPath(),gs.getTime(),gs.getAltitude() ,gs.getBearing() ,gs.getSpeed()  });

		} catch (Exception e) {
			e.printStackTrace();
			// Log4jUtil.getLogger().error("insertSensor:"+e.getStackTrace());
		} finally {
			if (db != null) {
				db.close();
				db = null;
			}
		}

	}

	public CameraPhoto getcameraphoto(String id) {
		CameraPhoto g = null;
        System.out.println("insert id   ============================  "+id);
		String sql = "select id,latitude,longitude,path,time,altitude,bearing,speed  from cameraphoto where  id = '"+id+"'";
		try {
			db = helper.getWritableDatabase();
			Cursor cursor = db.rawQuery(sql,null);
			
			while (cursor.moveToNext()) {
				g = new CameraPhoto();
				g.setId(cursor.getString(0));
				g.setLatitude(cursor.getString(1));
				g.setLongitude(cursor.getString(2));
				g.setPath(cursor.getString(3));
				g.setTime(cursor.getString(4));
                g.setAltitude(cursor.getString(5));
                g.setBearing(cursor.getString(6));
                g.setSpeed(cursor.getString(7));
			
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if (db != null) {
				db.close();
				db = null;
			}
		}

		return g;
	}
    public void insertvideo(CameraVideo cameraVideo) {
        String sql = "insert into videodata(id,path,starttime,endtime,isupload)values(?,?,?,?,?)";
        try {
            db = helper.getReadableDatabase();
            db.execSQL(
                    sql,
                    new Object[] { cameraVideo.getId(),cameraVideo.getSavePath(),cameraVideo.getstarttime(),cameraVideo.getendtime(),cameraVideo.isIsrecord() });

        } catch (Exception e) {
            e.printStackTrace();
            // Log4jUtil.getLogger().error("insertSensor:"+e.getStackTrace());
        } finally {
            if (db != null) {
                db.close();
                db = null;
            }
        }

    }

    public CameraVideo getvideo(String id) {
        CameraVideo g = null;
        System.out.println("insert id   ============================  "+id);
        String sql = "select id,path,starttime,endtime,isupload  from videodata where  id = '"+id+"'";
        try {
            db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql,null);

            while (cursor.moveToNext()) {
                g = new CameraVideo();
                g.setId(cursor.getString(0));
                g.setSavePath(cursor.getString(1));
                g.setstarttime(cursor.getString(2));
                g.setendtime(cursor.getString(3));
                g.setIsrecord(cursor.getInt(4));

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.close();
                db = null;
            }
        }

        return g;
    }
    public void updatavideoUpload(String id) {

        try {
            db = helper.getReadableDatabase();

            String sql = "update  videodata  set isupload = 1   where id = ?";
            db.execSQL(
                    sql,
                    new Object[] { id });



        } catch (Exception e) {
            e.printStackTrace();
            // Log4jUtil.getLogger().error("insertSensor:"+e.getStackTrace());
        }

    }
}
