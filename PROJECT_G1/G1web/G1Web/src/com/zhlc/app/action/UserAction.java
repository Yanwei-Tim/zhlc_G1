package com.zhlc.app.action;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zhlc.app.action.common.BaseAppAction;
import com.zhlc.common.constants.Constants;
import com.zhlc.common.utils.RandKeyUtil;
import com.zhlc.common.utils.SqlFilterUtil;
import com.zhlc.common.utils.StringUtil;

/**
 * author：anquan <br>
 * desc：用户相关处理 <br>
 * date：2015-5-4 下午03:55:13 <br>
 */
@Controller
@RequestMapping(value = "/user")
public class UserAction extends BaseAppAction {
	/**
	 * 用户注册 http://192.168.1.38:8080/G1Web/user/sign?data={"user_name":
	 * "18123672593","user_pass":"123456","car_type":"1","type":"1"} result:
	 * 1-注册成功 2-用户已注册 3-参数传递错误 4-其他异常
	 */
	@RequestMapping(value = "/sign")
	@ResponseBody
	public Map<String, Object> sign(String data) {
		System.out.println("sign:" + data);

		// String data =
		// "{\"user_name\":\"18123672593\",\"user_pass\":\"123456\",\"car_type\":\"1\",\"type\":\"1\"}";

		// ---- 参数封装 ----
		JSONObject jsonObject = JSONObject.fromObject(data);
		String user_name = jsonObject.getString("user_name");
		String user_pass = jsonObject.getString("user_pass");
		String car_type = jsonObject.getString("car_type");
		String type = jsonObject.getString("type");
		// ---- 参数封装 ----

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			LOGGER.info(user_name + " 正在注册...");
			if (StringUtil.isNotBlank(user_name)
					&& StringUtil.isNotBlank(user_pass)
					&& StringUtil.isNotBlank(car_type)
					&& StringUtil.isNotBlank(type)) {
				// 判断用户是否已注册
				System.out.println(user_name + " 判断用户是否已注册");
				if (!userService.checkSign(user_name)) {
					System.out.println(user_name + " 正在注册...");
					// 赋予参数
					HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("user_name", SqlFilterUtil.FilterSQL(user_name));
					param.put("user_pass", SqlFilterUtil.FilterSQL(user_pass));
					param.put("car_type", SqlFilterUtil.FilterSQL(car_type));
					param.put("type", SqlFilterUtil.FilterSQL(type));
					userService.sign(param);
					map.put(Constants.RESULT, 1);
					System.out.println(user_name + " 注册成功...");
				} else {
					map.put(Constants.RESULT, 2);
					System.out.println(user_name + " 已注册...");
				}
			} else {
				map.put(Constants.RESULT, 3);
				System.out.println(user_name + "参数失败");
			}
		} catch (Exception e) {
			map.put(Constants.RESULT, 4);
			System.out.println(user_name + " 注册失败");
			// throw new BmException(Constants.common_errors_1001);
		}
		return map;
	}

	/**
	 * 上传激活码到用户表 http://192.168.1.77:8080/G1Web/user/active?data={"user_name":
	 * "18123672593","activation_code":"123"} result: 1:激活成功 2:设备不存在 3:激活失败 4:用户不存在
	 */
	@RequestMapping(value = "/active")
	@ResponseBody
	public Map<String, Object> active(String data) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSONObject.fromObject(data);
			String user_name = jsonObject.getString(Constants.USER_NAME);
			String active_code = jsonObject.getString(Constants.ACTIVATION_CODE);
			System.out.println("user_name:" + user_name);
			System.out.println("active_code:" + active_code);
			if (StringUtil.isBlank(user_name)
					|| StringUtil.isBlank(active_code)) {
				map.put(Constants.RESULT, 3);
			}
			boolean isSign = userService.checkSign(SqlFilterUtil
					.FilterSQL(user_name));
			System.out.println("isSign:" + isSign);
			if (isSign) {
				String uid = userService.getUID(SqlFilterUtil
						.FilterSQL(user_name));
				System.out.println("uid:" + uid);
				if (!StringUtil.isBlank(uid)) {

					HashMap<String, Object> map2 = userService
							.getImeiAndCode(SqlFilterUtil
									.FilterSQL(active_code));
					if (map2 != null) {
						System.out.println("getImeiAndCode:" + map2.toString());
						String g1_key = RandKeyUtil.getRandomDigit(6);
						HashMap<String, Object> map3 = new HashMap<String, Object>();
						map3.put(Constants.G1_KEY, g1_key);
						map3.put(Constants.UID, uid);
						map3.put(Constants.G1_IMEI, map2.get(Constants.IMEI));
						map3.put(Constants.ACTIVATION_CODE, map2.get(Constants.ACTIVATION_CODE));
						System.out.println("insertBind:" + map3.toString());
						userService.insertBind(map3);
						map.put(Constants.RESULT, 1);
					} else {
						map.put(Constants.RESULT, 2);
					}

				} else {
					map.put(Constants.RESULT, 4);
				}
 
			} else {
				map.put(Constants.RESULT, 4);
			}
		} catch (Exception e) {
			map.put(Constants.RESULT, 3);
		}
		return map;

	}

	/**
	 * 用户登录 http://192.168.1.38:8080/G1Web/user/login?data={"user_name":
	 * "18123672593","user_pass":"123456","type":"1"} result: 1-登录成功 2-登录失败
	 * 3-用户名不存在 4-密码错误 5-当日登录错误次数超过5次 obj：
	 * {"nick_name":"baby","id":1,"user_name"
	 * :"18123672593","create_time":1435654150000,"type":"1"}
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public Map<String, Object> login(String data) {
		System.out.println("data=" + data);
		// ---- 参数封装 ----
		JSONObject jsonObject = JSONObject.fromObject(data);
		String user_name = jsonObject.getString("user_name");
		String user_pass = jsonObject.getString("user_pass");
		String type = jsonObject.getString("type");
		// ---- 参数封装 ----

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			LOGGER.info(user_name + " 正在登录...");
			if (StringUtil.isNotBlank(user_name)
					&& StringUtil.isNotBlank(user_pass)
					&& StringUtil.isNotBlank(type)) {
				// if(userService.getLoginError(user_name) < 6){
				HashMap<String, Object> ma = userService.login(SqlFilterUtil
						.FilterSQL(user_name), SqlFilterUtil
						.FilterSQL(user_pass));

				if (StringUtil.isNotBlank(ma)) {
					map.put(Constants.RESULT, 1);
					map.put("obj", ma);
				} else {
					// zzw
					boolean isUserExist = userService.checkSign(user_name);
					// 用户名存在
					if (isUserExist) {
						map.put(Constants.RESULT, 4);
					} else {
						map.put(Constants.RESULT, 3);
					}
					// /////zzw

					// 登录失败
					// 登录失败日志
					// zzw先不加 userService.insertLoginLog(user_name,"0");

					// zzw map.put(Constants.RESULT, 3);
				}
				// }
				// zzw else{
				// map.put(Constants.RESULT, 5);
				// }
			} else {
				map.put(Constants.RESULT, 2);
			}
		} catch (Exception e) {
			map.put(Constants.RESULT, 2);
		}
		return map;
	}

}
