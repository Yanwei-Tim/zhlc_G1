package com.zhlc.app.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anquan
 * @Desc: desc
 * @date 2014-12-31 下午11:12:26
 */
public interface IUserService {

	void sign(HashMap<String, Object> map);

	public HashMap<String, Object> login(String userName, String passWord);

	boolean checkSign(String user_name);

	void updateActiveCode(String userName, String active_code);

	void insertLoginLog(String user_name, String state);

	int getLoginError(String user_name);
	
	String getUID(String userName);
	
	HashMap<String, Object> getImeiAndCode(String activation_code );

	void insertBind(HashMap<String, Object> map);
}