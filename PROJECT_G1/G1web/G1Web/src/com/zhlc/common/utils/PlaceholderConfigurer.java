package com.zhlc.common.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * author：anquan <br>
 * desc： 配置文件解加密<br>
 * date： 2015-7-1 下午5:44:45<br>
 */
public class PlaceholderConfigurer extends PropertyPlaceholderConfigurer{
	
	    private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};
	    
	    @Override
	    protected String convertProperty(String propertyName, String propertyValue){
	        //如果在加密属性名单中发现该属性
	        if (isEncryptProp(propertyName)) {
	            String decryptValue = EncrypUtil.decode(propertyValue);
	            System.out.println(decryptValue);
	            return decryptValue;
	        }else {
	            return propertyValue;
	        }
	    }
	    
	    private boolean isEncryptProp(String propertyName){
	        for (String encryptName : encryptPropNames) {
	            if (encryptName.equals(propertyName)) {
	                return true;
	            }
	        }
	        return false;
	    }
}
