package com.zhlt.g1app.basefunc;

import com.zhlt.g1app.data.DataUser;

public class UserInfoUtil {

	private DataUser mUserData;
	private static UserInfoUtil mUserInfoUtil;

	public static UserInfoUtil getUserInfoUtil() {
		if (mUserInfoUtil == null) {
			mUserInfoUtil = new UserInfoUtil();
		}
		return mUserInfoUtil;
	}

	private UserInfoUtil() {
		if (mUserData == null) {
			mUserData = new DataUser();
		}
	}

	public DataUser getUserData() {
		return mUserData;
	}

	public void setUserData(DataUser userData) {
		if (userData == null) {
			return;
		}
		if (mUserData == null) {
			mUserData = new DataUser();
		}
		mUserData.setUserGender(userData.getUserGender());
		mUserData.setUserID(userData.getUserID());
		mUserData.setUserName(userData.getUserName());
		mUserData.setUserPic(userData.getUserPic());
	}

}
