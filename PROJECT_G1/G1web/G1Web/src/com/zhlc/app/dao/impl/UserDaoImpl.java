package com.zhlc.app.dao.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.zhlc.app.dao.IUserDao;
import com.zhlc.common.constants.Constants;
import com.zhlc.common.exception.BmException;
import com.zhlc.common.factory.impl.BaseDaoImpl;

/**
 * @author anquan
 * @Desc: desc
 * @date 2014-12-31 下午10:26:27
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<HashMap<String, Object>> implements
		IUserDao {

	private static final String NAMESPACE = IUserDao.class.getName() + ".";

	/**
	 * 用户注册
	 */
	@Override
	public void sign(HashMap<String, Object> map) {
		int i = 0;
		try {
			i = getSqlSession().insert(NAMESPACE + "insert", map);
		} catch (Exception e) {
			throw new BmException(Constants.common_errors_1003);
		}
		if (1 != i) {
			throw new BmException(Constants.common_errors_1003);
		}
	}

	/**
	 * 用户登录验证查询（根据用户名查询密码）
	 */
	@Override
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> login(String user_name, String user_pass) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_name", user_name);
		// map.put("user_pass", EncrypUtil.md5(user_pass));
		map.put("user_pass", user_pass);
		try {

			return (HashMap<String, Object>) getSqlSession().selectOne(
					NAMESPACE + "queryByMap", map);
		} catch (Exception e) {
			throw new BmException(Constants.common_errors_1004, e);
		}
	}

	@Override
	public void updateActiveCode(HashMap<String, Object> map) {
		int i;
		try {
			i = getSqlSession().update(NAMESPACE + "update_active_code", map);
			System.out.println("激活码插入成功");
		} catch (Exception e) {
			System.out.println("激活码插入失败");
			throw new BmException(Constants.common_errors_1003);
		}
		System.out.println("激活码" + i);
		if (1 != i) {
			throw new BmException(Constants.common_errors_1003);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> getImeiAndCode(HashMap<String, Object> map) {
         
		try {
			return (HashMap<String, Object>) getSqlSession().selectOne(
					NAMESPACE + "queryImeiByActivationCode", map);
		} catch (Exception e) {
			throw new BmException(Constants.common_errors_1004, e);
		}
	}

	@Override
	public String getUID(HashMap<String, Object> map) {

		try {
			return getSqlSession().selectOne(NAMESPACE + "queryUIdByUserName",
					map);
		} catch (Exception e) {
			throw new BmException(Constants.common_errors_1004, e);
		}
	}

	@Override
	public void insertBind(HashMap<String, Object> map) {
		int i = 0;
		try {
			i = getSqlSession().insert(NAMESPACE + "insert_user_bind", map);
		} catch (Exception e) {
			throw new BmException(Constants.common_errors_1001);
		}
		if (i != 1) {
			throw new BmException(Constants.common_errors_1001);
		}

	}

}
