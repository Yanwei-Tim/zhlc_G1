package com.zhlc.g1.bean;
/**
 * * Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd
 * * All rights reserved.
 * * http://www.gobabymobile.cn/
 * * File: - MainActivity.java
 * * Description:描述
 * *
 * *
 * * ------------------------------- Revision History: -------------------------------------
 * * <author>                             <data>             <version>            <desc>
 * * ---------------------------------------------------------------------------------------
 * * yuanpeng@gobabymobile.cn        2015-6-26 下午04:01:47     1.0         Create this moudle
 */


import java.io.Serializable;

/**
 *
 ** Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd
 ** All rights reserved.
 ** http://www.gobabymobile.cn/
 ** File: - MainActivity.java
 ** Description:  GPS Bean
 **
 **
 ** ------------------------------- Revision History: -------------------------------------
 ** <author>                             <data>             <version>            <desc>
 ** ---------------------------------------------------------------------------------------
 ** yuanpeng@gobabymobile.cn        2015-6-26 上午11:25:56     1.0         Create this moudle
 */
public class GPSVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String latitude;
    private String longitude;
    private String time;
    private String altitude;
    private String bearing;
    private String speed;
    private String imei;
    private String key;
    private String accuracy;

    public GPSVO() {
    }

    public GPSVO(String id, String latitude, String longitude, String time,
                 String altitude, String bearing, String speed, String imei,
                 String key, String accuracy) {
        super();
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.altitude = altitude;
        this.bearing = bearing;
        this.speed = speed;
        this.imei = imei;
        this.key = key;
        this.accuracy = accuracy;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "GPSVO [accuracy=" + accuracy + ", altitude=" + altitude
                + ", bearing=" + bearing + ", id=" + id + ", imei=" + imei
                + ", key=" + key + ", latitude=" + latitude + ", longitude="
                + longitude + ", speed=" + speed + ", time=" + time + "]";
    }

}
