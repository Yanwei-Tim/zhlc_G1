package com.zhlc.app.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.zhlc.app.dao.IUserDao;
import com.zhlc.app.service.IUserService;
import com.zhlc.app.service.common.impl.BaseAppServiceImpl;
import com.zhlc.common.constants.Constants;
import com.zhlc.common.utils.DateUtil;
import com.zhlc.common.utils.StringUtil;

/**
 * @author anquan
 * @Desc: desc
 * @date 2014-12-31 下午10:31:14
 */
@Service
public class UserServiceImpl extends BaseAppServiceImpl implements IUserService {

	private static final String NAMESPACE = IUserDao.class.getName() + ".";

	/**
	 * 用户注册
	 */
	@Override
	public void sign(HashMap<String, Object> map) {
		userDao.sign(map);
	}

	/**
	 * 登录查询
	 */
	@Override
	public HashMap<String, Object> login(String user_name, String user_pass) {
		return userDao.login(user_name, user_pass);
	}

	/**
	 * 判断用户是否已注册
	 */
	@Override
	public boolean checkSign(String user_name) {
		System.out.println("checkSign start");
		String count = userDao.queryByParam(NAMESPACE, "checkSign", user_name);
		System.out.println("checkSign" + count);
		return "0".equals(count) ? false : true;
	}

	/**
	 * 用户登录错误日志
	 */
	@Override
	public void insertLoginLog(String user_name, String state) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user_name", user_name);
		param.put("state", state);

		userDao.insert(NAMESPACE, "insertLoginLog", param);
	}

	/**
	 * 用户登录错误次数
	 */
	@Override
	public int getLoginError(String user_name) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user_name", user_name);
		param.put("login_time", DateUtil.convertDateToStr(new Date(),
				DateUtil.TIME_FORMAT_DAY));

		String count = userDao.queryByMap(NAMESPACE, "getLoginError", param);
		if (StringUtil.isNotBlank(count)) {
			return Integer.parseInt(count);
		} else {
			return 0;
		}
	}

	// zzw

	@Override
	public void updateActiveCode(String user_name, String active_code) {
	 
	}

	@Override
	public HashMap<String, Object> getImeiAndCode(String activationCode) {
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		mHashMap.put(Constants.ACTIVATION_CODE, activationCode);
		return (HashMap<String, Object>) userDao.getImeiAndCode(mHashMap);
	}

	@Override
	public String getUID(String user_name) {
		HashMap<String, Object> mHashMap = new HashMap<String, Object>();
		mHashMap.put(Constants.USER_NAME, user_name);
		return userDao.getUID(mHashMap);
	}

	@Override
	public void insertBind(HashMap<String, Object> map) {
		userDao.insertBind(map);
	}
}
