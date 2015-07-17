package com.zhlt.g1.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 
  ** Copyright (C), 2014-2015, Go Baby Mobile Corp., Ltd
 ** All rights reserved.
 ** http://www.gobabymobile.cn/
 ** File: - GenSequenceUtil.java
 ** Description:  随机生成 key
 **     
 **
 ** ------------------------------- Revision History: -------------------------------------
 ** <author>                             <data>             <version>            <desc>
 ** ---------------------------------------------------------------------------------------
 ** yuanpeng@gobabymobile.cn        2015-6-23  下午5:10:08     1.0         Create this moudle
 */
public class GenSequenceUtil {
	
	//定义一个编号的随机数，每次存放上次的编号值，防止数据重复
	private static String datedfStr="";
	private static int numCount=0;//计数标志值
	
	/**
	 * 生成注册会员的随机编号，无表面规律，该编号为12位数字串，且必须唯一，
	 * 生成规则：当前系统时间毫秒数模上1000，乘以100，再加2位随机数
	 * @return
	 */
	public static Long getMemberRandomNO(){
		Long randomNO=0L;
		Date nowDate=new Date();
		randomNO=nowDate.getTime();
		
		//当前时间的毫秒数
//		System.out.println("randomTmp=="+randomNO);
		
		//毫秒数模1000，乘以100，再加2位随机数
		Random random=new Random();
		randomNO=(randomNO/1000)*100+random.nextInt(100);
		
//		System.out.println("time1-time2=="+randomNO);
		return randomNO;
	}
	
	/**
	 * 返回完整日期格式（yyMMddHHmmss）+几位随机数的字符串
	 * @param randomBitNum  支持0-4位随机数
	 * @return
	 */
	public static String getFullDateRandomNum(int randomBit){
		//定义完整年月日时分秒字符格式
		String dateFormate="yyMMddHHmmss";
		
		SimpleDateFormat formater = new SimpleDateFormat(dateFormate);
		String fullDateStr=formater.format(new java.util.Date());
		
		//当随机数为0，而且静态变量为空字符串时，直接返回。
		if(datedfStr.equals("")){
			datedfStr=fullDateStr;
			if(randomBit==0){
				return datedfStr;
			}
		}
		
		if(randomBit==0){
			if(datedfStr.equals(fullDateStr)){
				numCount++;
				return String.valueOf(Long.parseLong(fullDateStr)+numCount);
			}else{
				//把返回值返给变量
				datedfStr=fullDateStr;
				numCount=0;
				return datedfStr;
			}
		}else{
			//当取几位随机数时
			if(datedfStr.equals(fullDateStr)){
				numCount=numCount+1;
			}else{
				datedfStr=fullDateStr;
				//不相等时，计数标志归1；
				numCount=0;
			}
			
			//根据随机位数，设置返回字符串
			if(randomBit==1){
				return String.valueOf(Long.parseLong(datedfStr)*10+numCount);
			}else if(randomBit==2){
				return String.valueOf(Long.parseLong(datedfStr)*100+numCount);
			}else if(randomBit==3){
				return String.valueOf(Long.parseLong(datedfStr)*1000+numCount);
			}else if(randomBit==4){
				return String.valueOf(Long.parseLong(datedfStr)*10000+numCount);
			}
			return null;//数值不在1-4位随机数间时，返回null;
		}
	}
	
	/**
	 * 根据源文件名返回12位随机文件名，后缀保持一致
	 * @param name
	 * @return
	 */
	public static String getRandomFileName(String originalName){
		String randomStr=String.valueOf(GenSequenceUtil.getMemberRandomNO());
		
		//截取文件名后面的后缀，并连接到前随机字符串
		String suffix = originalName.substring(originalName.lastIndexOf("."));
		return randomStr.concat(suffix);
	}
	
	/**
	 * 返回指定位数的字符串，字符串有数字、大小写字母组成
	 * @param length  指定字符串的长度
	 * @return
	 */
	public static String getCombinationStr(int length){
		String numberStr="1234567890";//数字字符串
		String lowLetterStr="abcdefghijklmnopqrstuywxyz";//小写字母字符串
		String uppLetterStr="ABCDEFGHIJKLMNOPQRSTUVWXYZ";//大写字母字符串
		String strArray[]={numberStr,lowLetterStr,uppLetterStr};
		
		//通过for循环逐一获取随机码字符
		StringBuffer strBuff=new StringBuffer(length);
		Random random=new Random();//定义随机变量
		int arrayIndex=0;
		for(int i=0;i<length;i++){
			arrayIndex=random.nextInt(3)%strArray.length;
			if(arrayIndex==0){
				strBuff.append(numberStr.charAt(random.nextInt(10)));
			}else if(arrayIndex==1){
				strBuff.append(lowLetterStr.charAt(random.nextInt(10)));
			}else if(arrayIndex==2){
				strBuff.append(uppLetterStr.charAt(random.nextInt(10)));
			}
		}
		
		return strBuff.toString();
	}
	
	/**
	 * 获取邀请码，默认为10位字符（数字、大小写字母）
	 * @return
	 */
	public static String getInviteCode(){
		return getCombinationStr(10);//默认为10为字符串
	}
	
	/**
	 * 测试方法
	 * @param args
	 */
	public static void  main(String args[]){
		try{
			
				System.out.println(GenSequenceUtil.getFullDateRandomNum(4));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
