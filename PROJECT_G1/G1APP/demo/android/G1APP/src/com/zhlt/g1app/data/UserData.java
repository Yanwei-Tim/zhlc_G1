package com.zhlt.g1app.data;

import com.tencent.a.a.a.a.g;

import android.text.TextUtils;

public class UserData {

	private String mUserID;// 用户id
	private String mUserPic; // 头像路径
	private String mUserName; // 名字
	private String mUserGender; // 性别
	private String mUserPosition; // 地区

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
		}else {
			this.mUserGender ="女";
		}
	}

	@Override
	public String toString() {
		return "mUserGender:" + mUserGender + "mUserID:" + mUserID
				+ "mUserName:" + mUserName + "mUserPic:" + mUserPic;
	}
}
