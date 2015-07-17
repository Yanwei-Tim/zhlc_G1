package com.zhlc.app.service.impl;

import java.util.HashMap;
import org.springframework.stereotype.Service;
import com.zhlc.app.dao.IUtilDao;
import com.zhlc.app.service.IUtilService;
import com.zhlc.app.service.common.impl.BaseAppServiceImpl;

/**
 * author：anquan <br>
 * desc：常用工具包 <br>
 * (1)短信
 * date： 2015-7-1 下午1:47:31<br>
 */
@Service
public class UtilServiceImpl extends BaseAppServiceImpl implements IUtilService{
	
	private static final String NAMESPACE = IUtilDao.class.getName() + ".";
	
	@Override
	public boolean insertCheckCode(HashMap<String, Object> map){
		return utilDao.insert(NAMESPACE, "insertCheckCode", map);
	}
	
	@Override
	public boolean CheckCode(HashMap<String, Object> map){
		String count = utilDao.queryByMap(NAMESPACE, "queryByMap", map);
		
		return "0".equals(count) ? false : true;
	}
}
