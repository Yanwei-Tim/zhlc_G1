package com.zhlt.g1app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zhlt.g1app.R;
import com.zhlt.g1app.adapter.AdpPersonListView;
import com.zhlt.g1app.basefunc.UserInfoUtil;
import com.zhlt.g1app.data.UserData;
import com.zhlt.g1app.view.ViewXList;

import android.app.Activity;
import android.os.Bundle;

public class ActPerson extends Activity{

	private ViewXList mListView;
	private AdpPersonListView mListAdapter;
	private List<HashMap<String, Object>> mList = new ArrayList<HashMap<String,Object>>();
	private UserData mUserData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_person);
		getListData();
		initView();
	}
	
	private void initView(){
		mListView = (ViewXList)findViewById(R.id.xListView_person);
		mListAdapter = new AdpPersonListView(getApplicationContext(), mList);
		mListView.setAdapter(mListAdapter);
	}
	
	/**
	 * 初始化个人资料
	 */
	private void getListData(){
		mUserData = UserInfoUtil.getUserInfoUtil().getUserData();
		HashMap<String, Object>mpicMap = new HashMap<String, Object>();
		mpicMap.put("头像", mUserData.getUserPic());
		HashMap<String, Object>mNameMap = new HashMap<String, Object>();
		mNameMap.put("名字", mUserData.getUserName());
		HashMap<String, Object>mGenderMap = new HashMap<String, Object>();
		mGenderMap.put("性别", mUserData.getUserGender());
		HashMap<String, Object>mPostionMap = new HashMap<String, Object>();
		mPostionMap.put("地区", "广东 深圳");
		HashMap<String, Object>mPhoneMap = new HashMap<String, Object>();
		mPhoneMap.put("绑定手机", "138 0121 3210");
		HashMap<String, Object>mSingMap = new HashMap<String, Object>();
		mSingMap.put("个性签名", "hello");
		mList.add(mpicMap);
		mList.add(mNameMap);
		mList.add(mGenderMap);
		mList.add(mPostionMap);
		mList.add(mPhoneMap);
		mList.add(mSingMap);
		
	}
}
