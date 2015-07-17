package com.zhlc.g1.car.guice.service;

import com.zhlc.g1.bean.CameraPhoto;
import com.zhlc.g1.bean.GPSVO;

import java.util.List;

/**
 * * Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd
 * * All rights reserved.
 * * http://www.gobabymobile.cn/
 * * File: - MainActivity.java
 * * Description: Car 接口
 * *
 * *
 * * ------------------------------- Revision History: -------------------------------------
 * * <author>                             <data>             <version>            <desc>
 * * ---------------------------------------------------------------------------------------
 * * yuanpeng@gobabymobile.cn        2015-6-26 上午11:20:02     1.0         Create this moudle
 */
public interface ICarService {
    /**
     * 一次插入多条  gps  数据
     *
     * @param gps
     * @return
     * @throws Exception
     */
    public boolean insertGpsData(List<GPSVO> gps) throws Exception;

    /**
     * 用户Car权限 认证
     *
     * @param valuses 0  imei   ,  1  key
     * @return
     * @throws Exception
     */
    public String selectuserimei(String... valuses) throws Exception;
    /**
     * 用户APP权限 认证
     *
     * @param valuses 0  imei   ,  1  key
     * @return
     * @throws Exception
     */

    public String selectuserapp(Object ... valuses) throws Exception;

    
    /**
     * 一次插入多条  gps  数据
     *
     * @param gps
     * @return
     * @throws Exception
     */
    public boolean insertCameraData(CameraPhoto info) throws Exception;
}
