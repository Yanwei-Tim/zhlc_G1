package com.zhlc.common.exception;

import org.springframework.dao.DataAccessException;

import com.zhlc.common.utils.StringUtil;

/**
 * @author anquan
 * @Time 2014-9-12 下午04:52:07
 * @Desc 自定义DAO层的异常类
 */
public class BmException extends DataAccessException {
	
	private static final long serialVersionUID = -485825256677060522L;

	/**
	 * 普通异常信息
	 * @param message
	 */
	public BmException(String message) {
		super(StringUtil.getResourceMessage(message));
	}
	/**
	 * 带替换参数的异常信息
	 * @param message
	 * @param args
	 */
	public BmException(String message,String[] args) {
		super(StringUtil.getResourceMessage(message, args));
	}
	
	/**
	 * 普通异常与引发这个异常的类
	 * @param message
	 * @param e
	 */
	public BmException(String message,Exception e){
		super(StringUtil.getResourceMessage(message) + " " + e.getCause());
	}
	/**
	 * 带替换参数的异常信息与引发这个异常的类
	 * @param message
	 * @param args
	 * @param e
	 */
	public BmException(String message,String[] args,Exception e){
		super(StringUtil.getResourceMessage(message, args) + " " + e.getCause());
	}
}
