package com.zhlc.g1.car.guice.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zhlc.g1.bean.CameraPhoto;
import com.zhlc.g1.bean.GPSVO;
import com.zhlc.g1.car.guice.service.ICarService;
import com.zhlc.g1.util.C3P0Util;
import com.zhlc.g1.util.GenSequenceUtil;
import com.zhlc.g1.util.InitUtil;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * * Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd All rights reserved.
 * http://www.gobabymobile.cn/ File: - MainActivity.java Description:描述
 * *
 * *
 * * ------------------------------- Revision History:
 * ------------------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * --------- yuanpeng@gobabymobile.cn 2015-6-26 上午11:23:54 1.0 Create this
 * moudle
 */
@Singleton
public class CarDao implements ICarService {
    static Logger logger = Logger.getLogger("CarDao");

    @Inject
    private C3P0Util c3p0;

    public Connection getConn() throws SQLException {
        return c3p0.getConnection();
    }

    @Override
    public boolean insertGpsData(List<GPSVO> listgps) throws Exception {
        if (InitUtil.DEBUG) logger.info("insert gps :" + listgps);
        Connection connection = getConn();
        Statement statemenet = connection.createStatement();
        for (GPSVO gps : listgps) {
            String sqls = "insert into gps_info(uid,g1_imei,gps_accuracy,gps_altitude,gps_bearing,gps_speed,gps_latitude,gps_longitude,gps_time) "
                    + " values('"
                    + GenSequenceUtil.getFullDateRandomNum(4)
                    + "','"
                    + gps.getImei()
                    + "','"
                    + gps.getAccuracy()
                    + "','"
                    + gps.getAltitude()
                    + "','"
                    + gps.getBearing()
                    + "','"
                    + gps.getSpeed()
                    + "','"
                    + gps.getLatitude()
                    + "','"
                    + gps.getLongitude() + "','"+gps.getTime()+"')";
            statemenet.execute(sqls);
        }
        statemenet.close();
        connection.close();

        return false;
    }

    @Override
    public String selectuserimei(String... valuses) throws Exception {
        String uid = null;
        Connection connection = getConn();
        Statement statemenet = connection.createStatement();
        String sql = "select id  from g1_user_bind  where g1_imei = '" + valuses[0] + "'  and  g1_key = '" + valuses[1] + "' ";
        ResultSet rs = statemenet.executeQuery(sql);
        if (rs.next()) {
            Object id = rs.getObject("id");
            uid = id.toString();
        }
        rs.close();
        statemenet.close();
        connection.close();
        return uid;
    }

	@Override
	public boolean insertCameraData(CameraPhoto info) throws Exception {
		// TODO Auto-generated method stub
		 if (InitUtil.DEBUG) logger.info("insert comcate " + info.toString());
	        Connection connection = getConn();
	        Statement statemenet = connection.createStatement();
	        String rand_id = GenSequenceUtil.getFullDateRandomNum(4);
	        //insert gps
	        String sqls = "insert into gps_info(uid,g1_imei,gps_accuracy,gps_altitude,gps_bearing,gps_speed,gps_latitude,gps_longitude,gps_time) "
                + " values('"
                + rand_id
                + "','"
                + info.getImei()
                + "','"
                + info.getAccuracy()
                + "','"
                + info.getAltitude()
                + "','"
                + info.getBearing()
                + "','"
                + info.getSpeed()
                + "','"
                + info.getLatitude()
                + "','"
                + info.getLongitude() + "','"+info.getTime()+"')";
           statemenet.execute(sqls);
	        
	        
           String filesqls = "insert into file_storage(uid,g1_imei,file_path,file_type,file_size,gps_id,file_up_time) "
	                    + " values('"
	                    + rand_id
	                    + "','"
	                    + info.getImei()
	                    + "','"
	                    + info.getSavePcPath()
	                    + "','"
	                    + 0
	                    + "','"
	                    + info.getFileSize()
	                    + "','"
	                    + rand_id + "',now())";
	            statemenet.execute(filesqls);
	       
	        statemenet.close();
	        connection.close();

		
		
		return true;
	}

	@Override
	public String selectuserapp(Object... valuses) throws Exception {
        String uid = null;
        Connection connection = getConn();
        Statement statemenet = connection.createStatement();
        String sql = "select id  from  g1_user_bind  where  u_id = '" + valuses[0] + "'  and  g1_key = '" + valuses[1] + "' ";
        ResultSet rs = statemenet.executeQuery(sql);
        if (rs.next()) {
            Object id = rs.getObject("id");
            uid = id.toString();
        }
        rs.close();
        statemenet.close();
        connection.close();
        return uid;
    }

}
