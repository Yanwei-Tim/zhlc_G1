package com.zhlt.g1app.basefunc;

import com.zhlt.g1app.data.CommonData;
import com.zhlt.g1app.data.UserData;

import android.R.menu;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

public class SharePreferUtil {

	/**
	 * 从sharepreference读取用户数据，
	 * 
	 * @return false = 未登入
	 */
	public static UserData getUserData(Context context) {
		UserData mUserData = null;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				CommonData.SharePrefer.SharePrefer_User_Name, 0);
		String id = sharedPreferences.getString(
				CommonData.SharePrefer.SharePrefer_User_UserID, "");
		if (!TextUtils.isEmpty(id)) {
			mUserData = new UserData();
			mUserData.setUserGender(sharedPreferences.getString(
					CommonData.SharePrefer.SharePrefer_User_UserGender, ""));
			mUserData.setUserID(sharedPreferences.getString(
					CommonData.SharePrefer.SharePrefer_User_UserID, ""));
			mUserData.setUserPic(sharedPreferences.getString(
					CommonData.SharePrefer.SharePrefer_User_UserPic, ""));
			mUserData.setUserName(sharedPreferences.getString(
					CommonData.SharePrefer.SharePrefer_User_UserName, ""));
		}
		return mUserData;
	}

	/**
	 * 从sharepreference保存用户数据，
	 * 
	 * 　
	 */
	public static void saveUserData(Context context, UserData mUserData) {

		if (mUserData == null) {
			return;
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				CommonData.SharePrefer.SharePrefer_User_Name, 0);
		Editor mEditor = sharedPreferences.edit();
		mEditor.putString(CommonData.SharePrefer.SharePrefer_User_UserGender,
				mUserData.getUserGender());
		mEditor.putString(CommonData.SharePrefer.SharePrefer_User_UserID,
				mUserData.getUserID());
		mEditor.putString(CommonData.SharePrefer.SharePrefer_User_UserPic,
				mUserData.getUserPic());
		mEditor.putString(CommonData.SharePrefer.SharePrefer_User_UserName,
				mUserData.getUserName());
		mEditor.commit();
	}

}
