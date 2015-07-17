package com.zhlc.app.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.zhlc.app.dao.IUserDao;
import com.zhlc.app.dao.IUtilDao;
import com.zhlc.app.service.common.IBaseAppService;

public class BaseAppServiceImpl implements IBaseAppService{

	@Autowired
	protected IUtilDao utilDao;//工具包
	@Autowired
	protected IUserDao userDao;//用户信息
}
