package com.zhlt.g1app.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataShare {

	private String mName;
	private String mText;
	private String mHeadPic;
	private ArrayList<String>mContentPicList;
	private List<HashMap<String, String>> mList;

	public DataShare(String mName, String mText, String mHeadPic,
			ArrayList<String> mContentPic, List<HashMap<String, String>> mList) {

		this.mName = mName;
		this.mText = mText;
		this.mHeadPic = mHeadPic;
		this.mContentPicList = mContentPic;
		this.mList = mList;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getText() {
		return mText;
	}

	public void setText(String mText) {
		this.mText = mText;
	}

	public String getHeadPic() {
		return mHeadPic;
	}

	public void setHeadPic(String mHeadPic) {
		this.mHeadPic = mHeadPic;
	}

	public ArrayList<String> getContentPic() {
		return mContentPicList;
	}

	public void setContentPic(ArrayList<String> mContentPic) {
		this.mContentPicList = mContentPic;
	}

	public List<HashMap<String, String>> getList() {
		return mList;
	}

	public void setList(List<HashMap<String, String>> mList) {
		this.mList = mList;
	}

}
