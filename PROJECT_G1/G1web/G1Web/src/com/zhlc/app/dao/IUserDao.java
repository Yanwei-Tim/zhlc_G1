package com.zhlc.app.dao;

import java.util.HashMap;
import java.util.Map;

import com.zhlc.common.factory.IBaseDao;

/**
 * @author anquan
 * @desc desc.<br>
 * @date 2014-12-31 上午11:18:05
 */
public interface IUserDao extends IBaseDao<HashMap<String, Object>> {

	/**
	 * 用户注册
	 * 
	 * @param map
	 * @return
	 */
	void sign(HashMap<String, Object> map);

	/**
	 * 用户登录验证查询（根据用户名查询密码）
	 */
	public HashMap<String, Object> login(String user_name, String user_pass);

	void updateActiveCode(HashMap<String, Object> map );
	
	String getUID(HashMap<String, Object> map);
	
	Map<String, Object> getImeiAndCode(HashMap<String, Object> map );

	void insertBind(HashMap<String, Object> map);
}
