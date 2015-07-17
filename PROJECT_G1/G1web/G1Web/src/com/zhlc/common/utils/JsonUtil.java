package com.zhlc.common.utils;

import java.util.HashMap;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JavaIdentifierTransformer;


public class JsonUtil {
	/**
	 * 对象转化成json(过滤)
	 * @param obj
	 * @param param 过滤指定属性，不转成json
	 * @return String
	 * @date:2012-12-7
	 */
	public static String objectToJson(Object obj,String... params) {
		//判断是否需要过滤属性
		if(!StringUtil.isBlank(params)){
			JsonConfig jsonConfig = new JsonConfig(); 
			jsonConfig.setExcludes(params);  
			return JSONObject.fromObject(obj,jsonConfig).toString();
		}
		return JSONObject.fromObject(obj).toString();
	}
	
	/**
	 * 数组转化成json(过滤)
	 * @param list
	 * @param params
	 * @return
	 */
	public static String arrayToJson(List<?> list,String... params) {
		//判断是否需要过滤属性
		if(!StringUtil.isBlank(params)){
			JsonConfig jsonConfig = new JsonConfig(); 
			jsonConfig.setExcludes(params);  
			return JSONArray.fromObject(list,jsonConfig).toString();
		}
		return JSONArray.fromObject(list).toString();
	}
	
	/**
	 * 数组转json(不过滤)
	 * @param array
	 * @return String
	 * @date:2012-12-7
	 */
	public static String arrayToJson(Object[] array) {
		return JSONArray.fromObject(array).toString();
	}
	
	/**
	 * 普通json转Object
	 * @param jsonString
	 * @param classes
	 * @return Object
	 * @date:Mar 4, 2014
	 */
	public static Object jsonToObject(String jsonString, Class<?> classes) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return JSONObject.toBean(jsonObject, classes);
	}
	/**
	 * 首字母大写json转Object
	 * @param jsonString
	 * @param classes
	 * @return Object
	 * @date:Mar 4, 2014
	 */
	public static Object jsonCapsToObject(String jsonString, Class<?> classes) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		JsonConfig config = new JsonConfig();
		config.setJavaIdentifierTransformer(new JavaIdentifierTransformer() {
			@Override
			public String transformToJavaIdentifier(String str) {
				char[] chars = str.toCharArray();
				chars[0] = Character.toLowerCase(chars[0]);
				return new String(chars);
			}

		});
		config.setRootClass(classes);
		return JSONObject.toBean(jsonObject, config);
	}
	/**
	 * json转数组
	 * @param jsonString
	 * @param classes
	 * @return Object
	 * @date:2012-12-7
	 */
	public static Object jsonToArray(String jsonString, Class<?> classes) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return JSONArray.toArray(jsonArray, classes);
	}
	/**
	 * json转list
	 * @param jsonString
	 * @param classes
	 * @return Object
	 * @date:2012-12-7
	 */
	public static Object jsonToList(String jsonString, Class<?> classes){
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return JSONArray.toCollection(jsonArray, classes);
	}
	
	public static void main(String[] args){
		String item = "{'roomId':'1','winResult':'5','bankerId':null,'bankerChips':0,'bankerWinOrLoseChips':0,'settlementTime':'2014-03-04 14:35:56','systemDrawMoney':0,'cardResoure':1,'playersWinOrLoseLogList':[]}";
		JsonUtil.jsonToObject(item, HashMap.class);
	}
}
