package com.zhlc.app.dto;

import java.util.Date;

/**
 * @author anquan
 * @desc desc.<br>
 * @date 2014-12-31 ����11:12:29
 */
public class UserDTO {
	private String id;
	private String userName;
	private String passWord;
	private Date createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
