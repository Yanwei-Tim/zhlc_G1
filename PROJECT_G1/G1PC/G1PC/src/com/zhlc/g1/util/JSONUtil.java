package com.zhlc.g1.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * * Copyright (C), 2014-2015, GoBaby Mobile Corp., Ltd All rights reserved.
 * http://www.gobabymobile.cn/ File: - MainActivity.java Description:描述
 * *
 * *
 * * ------------------------------- Revision History:
 * ------------------------------------- <author> <data> <version> <desc>
 * --------
 * ----------------------------------------------------------------------
 * --------- yuanpeng@gobabymobile.cn 2015-6-26 下午04:39:55 1.0 Create this
 * moudle
 */
public class JSONUtil {
    /**
     * 封装将json对象转换为java集合对象
     *
     * @param <T>
     * @param clazz
     * @param jsons
     * @return
     */
    public static <T> List<T> getclassList(T clazz, String jsons) {
        List<T> objs = null;
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(jsons);
        if (jsonArray != null) {
            objs = new ArrayList<T>();
            //List list = (List) JSONSerializer.toJava(jsonArray);
            for (Object o : jsonArray) {
                JSONObject jsonObject = JSONObject.fromObject(o.toString());
                T obj = (T) JSONObject.toBean(jsonObject, clazz.getClass());
                objs.add(obj);
            }
        }
        return objs;
    }
}
