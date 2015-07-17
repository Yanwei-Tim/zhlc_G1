package com.zhlc.common.utils;

import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;

/**
 * @author anquan
 * @Time 2014-9-12 下午03:34:06
 * @Desc 字符串处理工具
 */
public class StringUtil { 
    /**
     * 判断对象是否为空
     * @param obj
     * @return 
     */
    public static boolean isBlank(Object obj){
        return obj == null || "".equals(obj) || "null".equals(obj);
    }
    /**
     * 判断对象是不为空
     * @param obj
     * @return 
     */
    public static boolean isNotBlank(Object obj){
    	return obj != null && !"".equals(obj) && !"null".equals(obj);
    }
	/**
	 * 
	 * 功能说明：判断系统运行的平台
	 * 参数及返回值: 
	 * @return
	 */
	public static boolean isWindowsPlatform() {
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows")) {
			return true;
		}
		return false;
	}
    /**
     * 获取国际化消息
     * @param path
     * @param code
     * @param args
     * @return 
     */
    public static String getResourceMessage(String code,String... args){
        ResourceBundle bundle = ResourceBundle.getBundle("I18N/commonErrors", Locale.getDefault());
        try {
            String message = bundle.getString(code);
            if(!StringUtils.isBlank(message)){
                if(null != args && args.length > 0){
                    for(int i = 0;i < args.length; i++){
                        message = message.replace("{" + i + "}", args[i]);
                    }
                }
            }
            return message;
        }catch (Exception e) {
            return code;
        }
    }
}
