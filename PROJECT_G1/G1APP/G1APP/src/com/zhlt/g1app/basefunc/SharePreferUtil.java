package com.zhlt.g1app.basefunc;

import java.util.Set;

import com.zhlt.g1app.data.DataCommon;
import com.zhlt.g1app.data.DataUser;

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
	public static DataUser getUserData(Context context) {
		DataUser mUserData = null;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DataCommon.SharePrefer.SharePrefer_User_Name, 0);
		String id = sharedPreferences.getString(
				DataCommon.SharePrefer.SharePrefer_User_UserID, "");
		if (!TextUtils.isEmpty(id)) {
			mUserData = new DataUser();
			mUserData.setUserGender(sharedPreferences.getString(
					DataCommon.SharePrefer.SharePrefer_User_UserGender, ""));
			mUserData.setUserID(sharedPreferences.getString(
					DataCommon.SharePrefer.SharePrefer_User_UserID, ""));
			mUserData.setUserPic(sharedPreferences.getString(
					DataCommon.SharePrefer.SharePrefer_User_UserPic, ""));
			mUserData.setUserName(sharedPreferences.getString(
					DataCommon.SharePrefer.SharePrefer_User_UserName, ""));
		}
		return mUserData;
	}

	/**
	 * 从sharepreference保存用户数据，
	 * 
	 * 　
	 */
	public static void saveUserData(Context context, DataUser mUserData) {

		if (mUserData == null) {
			return;
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DataCommon.SharePrefer.SharePrefer_User_Name, 0);
		Editor mEditor = sharedPreferences.edit();
		mEditor.putString(DataCommon.SharePrefer.SharePrefer_User_UserGender,
				mUserData.getUserGender());
		mEditor.putString(DataCommon.SharePrefer.SharePrefer_User_UserID,
				mUserData.getUserID());
		mEditor.putString(DataCommon.SharePrefer.SharePrefer_User_UserPic,
				mUserData.getUserPic());
		mEditor.putString(DataCommon.SharePrefer.SharePrefer_User_UserName,
				mUserData.getUserName());
		mEditor.putString(DataCommon.SharePrefer.SharePrefer_User_UserCarModel,
				mUserData.getUserCarModel());
		mEditor.commit();
	}

	public static void write(Context context, String name, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DataCommon.SharePrefer.SharePrefer_Main_Name, 0);
		Editor mEditor = sharedPreferences.edit();
		mEditor.putString(name, value);
		mEditor.commit();
	}

	public static String getString(Context context, String name, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DataCommon.SharePrefer.SharePrefer_Main_Name, 0);
		return sharedPreferences.getString(name, value);
	}

}
