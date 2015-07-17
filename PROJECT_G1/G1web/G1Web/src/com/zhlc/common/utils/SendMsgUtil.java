package com.zhlc.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

/**
 * author：anquan <br>
 * desc： 发送手机短信<br>
 * date： 2015-7-1 上午9:52:18<br>
 * API： http://www.zhongguowuxian.com/Item/list.asp?id=1460
 */
public class SendMsgUtil {
	private static final Logger LOGGER = LogUtil.getDefaultInstance();
	
	public static String HTTPURL="http://sdk.zhongguowuxian.com:98/ws/BatchSend.aspx";
	public static final String CorpID="GZLKJ0004582"; //用户名 
    public static final String Pwd="qing@123"; //密码
    
    /**
     * 短信发送(可群发)
     * @param Mobile
     * --号码之间用英文逗号隔开，建议100个号码。例如：13812345678,13519876543,15812349876
     * @param Content
     * --发送内容
     * @param SendTime
     * --定时发送时间。固定14位长度字符串，比如：20060912152435代表2006年9月12日15时24分35秒，为空表示立即发送
     * @return
     *  大于等于0的数字	提交成功
		–1	账号未注册
		–2	其他错误
		–3	帐号或密码错误
		–5	余额不足，请充值
		–6	定时发送时间不是有效的时间格式
		-7	提交信息末尾未签名，请添加中文的企业签名【 】
		–8	发送内容需在1到300字之间
		-9	发送号码为空
		-10	定时时间不能小于系统当前时间
		-100 限制IP访问
     */
	public static int sendSMS(String Mobile, String Content, String SendTime){
		BufferedReader in = null;
		int inputLine = 0;
		try {
			if(StringUtil.isNotBlank(Mobile) && StringUtil.isNotBlank(Content)){
				//发送内容
				Content = URLEncoder.encode(Content.replaceAll("<br/>", ""), "GBK");
				HTTPURL = HTTPURL + "?CorpID=" + CorpID + "&Pwd=" + Pwd + "&Mobile=" + Mobile + "&Content=" + Content + "&Cell="+""+"&SendTime=" + SendTime;
				LOGGER.info("HTTPURL-----" + HTTPURL);
			
				URL url = new URL(HTTPURL);
				in = new BufferedReader(new InputStreamReader(url.openStream()));
				System.out.println("readLine:"+in.readLine());
		
				inputLine = new Integer(in.readLine()).intValue();
				System.out.println("inputLine:"+inputLine);
			}else{
				LOGGER.error("--手机号码或发送内容不符合规范--");
				System.out.println("---- 手机号码或发送内容不符合规范 ----");
				inputLine=-2;
			}

		} catch (NumberFormatException e) {
		} catch (MalformedURLException e) {
			LOGGER.error(Mobile + "--短信发送失败");
			System.out.println("---- 短信发送失败 ----");
			inputLine=-2;
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(Mobile + "--短信发送失败");
			System.out.println("---- 短信发送失败 ----");
			inputLine=-2;
		} catch (IOException e) {
			LOGGER.error(Mobile + "--短信发送失败");
			System.out.println("---- 短信发送失败 ----");
			inputLine=-2;
		}
		try {
			LOGGER.info(Mobile + "--发送短信成功--" + URLDecoder.decode(Content, "GBK") + "。返回状态码："+inputLine);
			System.out.println("---- 发送短信成功 ----");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return inputLine;
	}
	
	public static void main(String[] args) {
		
		String codeKey = RandKeyUtil.getRandomDigit(6);
		/**
		 * 短信规则：
		 * (1)同一号码间隔时间要大于1分钟
		 * (2)同一号码一天最多发5条
		 * (3)相同内容从第二次开始会被拦截
		 */
		System.out.println(sendSMS("13502839748", "你本次在中和联创申请动态服务的验证码为:" + codeKey + "。请勿将动态码告诉他人并确认该申请是由您本人操作!",""));
		
	}
}
