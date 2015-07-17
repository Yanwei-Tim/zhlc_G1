package com.zhlc.app.action;

import java.util.Date;
import java.util.HashMap;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zhlc.app.action.common.BaseAppAction;
import com.zhlc.common.constants.Constants;
import com.zhlc.common.utils.DateUtil;
import com.zhlc.common.utils.RandKeyUtil;
import com.zhlc.common.utils.SendMsgUtil;
import com.zhlc.common.utils.SqlFilterUtil;

/**
 * author：anquan <br>
 * desc： 常用请求<br>
 * date： 2015-7-1 上午11:05:35<br>
 */
@Controller
@RequestMapping(value = "/util")
public class UtilAction extends BaseAppAction {

	/**
	 * 发送短信调用
	 * 
	 * @param Mobile
	 * @param Content
	 * @param SendTime
	 * @return 
	 *         http://192.168.1.38:8080/G1Web/util/sendMsg?data={"Mobile":"18123672593"
	 *         } result: 1-成功 2-本地插入数据失败 3-发送短信失败 4-用户已存在  code：验证码
	 */
	@RequestMapping(value = "/sendMsg")
	@ResponseBody
	public HashMap<String, Object> sendMsg(String data) {

		System.out.println("---- 发送短信 ----");

		// ---- 参数封装 ----
		JSONObject jsonObject = JSONObject.fromObject(data);
		String Mobile = jsonObject.getString("Mobile");
		// ---- 参数封装 ----

		HashMap<String, Object> map = new HashMap<String, Object>();
		String codeKey = RandKeyUtil.getRandomDigit(6);
       boolean isSigned = userService.checkSign(Mobile);
       System.out.println(" sendMsg isSigned: "+isSigned);
		if (isSigned) {
			  System.out.println(" RESULT: 4" );
			map.put(Constants.RESULT, "4");
		} else {

			// 发送短信
			String Content = "你本次在中和联创申请动态服务的验证码为:" + codeKey
					+ "。请勿将动态码告诉他人并确认该申请是由您本人操作!";

			// 短信接口
			int num = SendMsgUtil.sendSMS(Mobile, Content, "");

			System.out.println(num);
			System.out.println("num >= 0 " + (num >= 0));
			// 对数据持久化操作
			if (num >= 0) {
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("mobile", Mobile);
				param.put("checkcode", codeKey);
				param.put("ip", "");
				param.put("expiretime", DateUtil.addDate(new Date(), 0, 0, 10,
						0));
				param.put("state", "1");
				System.out.println("num >= 0 ");
				boolean insert = utilService.insertCheckCode(param);
				System.out.println("insert = " + insert);
				if (insert) {
					map.put(Constants.RESULT, "1");
					System.out.println("RESULT = 1");
				} else {
					System.out.println("RESULT = 2");
					map.put(Constants.RESULT, "2");
				}
			} else {
				System.out.println("RESULT = 3");
				map.put(Constants.RESULT, "3");
			}
		}

		map.put("code", codeKey);

		return map;
	}

	/**
	 * 检查验证码是否有效
	 * 
	 * @param Mobile
	 * @param Content
	 * @param SendTime
	 * @return 
	 *         http://localhost:8080/G1Web/util/checkcode?data={"Mobile":"18123672593"
	 *         ,"checkcode":"030225"} result: 1-成功 2-验证码错误
	 */
	@RequestMapping(value = "/checkcode")
	@ResponseBody
	public HashMap<String, Object> checkcode(String data) {
		// ---- 参数封装 ----
		System.out.println("checkcode = " + data);
		JSONObject jsonObject = JSONObject.fromObject(data);
		String Mobile = jsonObject.getString("Mobile");
		String checkcode = jsonObject.getString("checkcode");
		// ---- 参数封装 ----

		HashMap<String, Object> map = new HashMap<String, Object>();
		// 对数据持久化操作
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("mobile", SqlFilterUtil.FilterSQL(Mobile));
		param.put("checkcode", SqlFilterUtil.FilterSQL(checkcode));
		param.put("createtime", DateUtil.convertDateToStr(new Date(),
				DateUtil.TIME_FORMAT_DAY));

		if (utilService.CheckCode(param)) {
			map.put(Constants.RESULT, "1");
		} else {
			map.put(Constants.RESULT, "2");
		}
		return map;
	}
}
