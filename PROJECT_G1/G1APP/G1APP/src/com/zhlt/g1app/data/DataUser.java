package com.zhlt.g1app.data;

import android.text.TextUtils;

public class DataUser {

	private String mUserID="";// 用户id
	private String mUserPic=""; // 头像路径
	private String mUserName="超级大坏蛋"; // 名字
	private String mUserGender="男"; // 性别
	private String mUserPosition="广东-深圳"; // 地区
	private String mUserSign="广东深圳"; // 个性签名
	private String mUserPassword = "1234";
	private String mUserCarModel="";
	
	public String getUserCarModel() {
		return mUserCarModel;
	}

	public void setUserCarModel(String mUserCarModel) {
		this.mUserCarModel = mUserCarModel;
	}

	public String getUserPassword() {
		return mUserPassword;
	}

	public void setUserPassword(String mUserPassword) {
		this.mUserPassword = mUserPassword;
	}

	public String getUserSign() {
		return mUserSign;
	}

	public void setUserSign(String mUserSign) {
		this.mUserSign = mUserSign;
	}

	public String getUserPosition() {
		return mUserPosition;
	}

	public void setUserPosition(String mUserPosition) {
		this.mUserPosition = mUserPosition;
	}

	private String mUserPhone="138 0121 3210";

	public String getUserPhone() {
		return mUserPhone;
	}

	public void setUserPhone(String mUserPhone) {
		this.mUserPhone = mUserPhone;
	}

	public String getUserID() {
		return mUserID;
	}

	public void setUserID(String mUserId) {
		this.mUserID = mUserId;
	}

	public String getUserPic() {
		return mUserPic;
	}

	public void setUserPic(String mUserPic) {
		this.mUserPic = mUserPic;
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String mUserName) {
		this.mUserName = mUserName;
	}

	public String getUserGender() {
		return mUserGender;
	}

	public void setUserGender(String gender) {
		if (TextUtils.isEmpty(gender)) {
			return;
		}
		if ("1".equals(gender) || "男".equals(gender)) {
			this.mUserGender ="男";
		}else if ("2".equals(gender) || "女".equals(gender)) {
			this.mUserGender ="女";
		}else {
			this.mUserGender ="保密";
		}
	}

	@Override
	public String toString() {
		return "mUserGender:" + mUserGender + "mUserID:" + mUserID
				+ "mUserName:" + mUserName + "mUserPic:" + mUserPic;
	}
}
