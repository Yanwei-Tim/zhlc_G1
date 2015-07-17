package com.zhlt.g1app.basefunc;

import com.zhlt.g1app.data.UserData;

public class UserInfoUtil {

	private UserData mUserData;
	private static UserInfoUtil mUserInfoUtil;

	public static UserInfoUtil getUserInfoUtil() {
		if (mUserInfoUtil == null) {
			mUserInfoUtil = new UserInfoUtil();
		}
		return mUserInfoUtil;
	}

	private UserInfoUtil() {
		if (mUserData == null) {
			mUserData = new UserData();
		}
	}

	public UserData getUserData() {
		return mUserData;
	}

	public void setUserData(UserData userData) {
		if (userData == null) {
			return;
		}
		if (mUserData == null) {
			mUserData = new UserData();
		}
		mUserData.setUserGender(userData.getUserGender());
		mUserData.setUserID(userData.getUserID());
		mUserData.setUserName(userData.getUserName());
		mUserData.setUserPic(userData.getUserPic());
	}

}
