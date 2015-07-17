package com.zhlc.common.utils;

/**
 * author：anquan <br>
 * desc：SQL过滤器 <br>
 * date： 2015-7-2 上午11:52:25<br>
 */
public class SqlFilterUtil {
	
	public static String FilterSQL(String str){
		return str.toLowerCase().replaceAll("'", "")
		   .replaceAll("/", "")
		   .replaceAll("&", "")
		   .replaceAll("<", "")
		   .replaceAll(">", "")
		   .replaceAll("=", "")
		   .replaceAll(";", "")
		   .replaceAll(",", "")
		   .replaceAll("\\+", "")
		   .replaceAll("-", "")
		   .replaceAll("%", "")
		   .replaceAll("\\*", "")
		   .replaceAll("and", "")
		   .replaceAll("or", "")
		   .replaceAll("like", "")
		   .replaceAll("exec", "")
		   .replaceAll("insert", "")
		   .replaceAll("delete", "")
		   .replaceAll("update", "")
		   .replaceAll("select", "")
		   .replaceAll("create", "")
		   .replaceAll("drop", "")
		   .replaceAll("truncate", "")
		   .replaceAll("declare", "")
		   ;
    }
	
	public static void main(String[] args) {
		String str = "18123672593+-/=.>,<";
		System.out.println(FilterSQL(str));
	}
}
