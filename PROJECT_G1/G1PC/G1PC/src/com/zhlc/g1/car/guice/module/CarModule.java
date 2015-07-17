package com.zhlc.g1.car.guice.module;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.zhlc.g1.car.guice.dao.CarDao;
import com.zhlc.g1.car.guice.service.ICarService;

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
 * * yuanpeng@gobabymobile.cn        2015-6-26 上午11:25:56     1.0         Create this moudle
 */
public class CarModule implements Module {

    @Override
    public void configure(Binder bind) {
        // TODO Auto-generated method stub
        bind.bind(ICarService.class).to(CarDao.class);
       
    }

}
