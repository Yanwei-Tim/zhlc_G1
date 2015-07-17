package com.zhlc.common.exception;

import org.springframework.dao.DataAccessException;

import com.zhlc.common.utils.StringUtil;

/**
 * @author anquan
 * @Time 2014-9-12 下午05:34:06
 * @Desc  自定义Validate层的异常类
 */
public class ValidateException extends DataAccessException{

	private static final long serialVersionUID = -485825256677060522L;

	/**
	 * 普通异常信息
	 * @param message
	 */
	public ValidateException(String message){
		super(StringUtil.getResourceMessage(message));
	}
	/**
	 * 带替换参数的异常信息
	 * @param message
	 * @param args
	 */
	public ValidateException(String message,String[] args){
		super(StringUtil.getResourceMessage(message, args));
	}
	
	/**
	 * 普通异常与引发这个异常的类
	 * @param message
	 * @param e
	 */
	public ValidateException(String message,Exception e){
		super(StringUtil.getResourceMessage(message) + " " + e.getCause());
	}
	/**
	 * 带替换参数的异常信息与引发这个异常的类
	 * @param message
	 * @param args
	 * @param e
	 */
	public ValidateException(String message,String[] args,Exception e){
		super(StringUtil.getResourceMessage(message, args) + " " + e.getCause());
	}
}
