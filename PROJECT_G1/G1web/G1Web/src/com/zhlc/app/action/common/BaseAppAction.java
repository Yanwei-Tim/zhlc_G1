package com.zhlc.app.action.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.zhlc.app.service.IUserService;
import com.zhlc.app.service.IUtilService;
import com.zhlc.common.utils.LogUtil;

public class BaseAppAction {
	protected static final Logger LOGGER = LogUtil.getDefaultInstance();
	
	@Autowired
	protected IUtilService utilService;//工具(发短信)
	@Autowired
	protected IUserService userService;//用户
}
