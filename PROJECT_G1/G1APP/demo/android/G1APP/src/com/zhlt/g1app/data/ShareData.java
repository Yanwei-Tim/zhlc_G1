package com.zhlt.g1app.data;

import java.util.HashMap;
import java.util.List;

public class ShareData {

	private String mName;
	private String mText;
	private String mHeadPic;
	private String mContentPic;
	private List<HashMap<String, String>> mList;

	public ShareData(String mName, String mText, String mHeadPic,
			String mContentPic, List<HashMap<String, String>> mList) {

		this.mName = mName;
		this.mText = mText;
		this.mHeadPic = mHeadPic;
		this.mContentPic = mContentPic;
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

	public String getContentPic() {
		return mContentPic;
	}

	public void setContentPic(String mContentPic) {
		this.mContentPic = mContentPic;
	}

	public List<HashMap<String, String>> getList() {
		return mList;
	}

	public void setList(List<HashMap<String, String>> mList) {
		this.mList = mList;
	}

}
