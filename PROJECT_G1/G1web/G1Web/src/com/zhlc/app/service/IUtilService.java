package com.zhlc.app.service;

import java.util.HashMap;

/**
 * author：anquan <br>
 * desc： <br>
 * date： 2015-7-1 下午2:05:11<br>
 */
public interface IUtilService {

	/**
	 * 验证码保存
	 */
	public abstract boolean insertCheckCode(HashMap<String, Object> map);

	/**
	 * 查询验证码是否有效
	 */
	public abstract boolean CheckCode(HashMap<String, Object> map);

}