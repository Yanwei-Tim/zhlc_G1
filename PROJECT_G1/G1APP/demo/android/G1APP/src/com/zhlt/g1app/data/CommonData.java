package com.zhlt.g1app.data;

public class CommonData {

	public static class Message {

		public static final int MSG_ADV_TEXT_lOADFINISHED = 1;
		public static final int MSG_ADV_TEXT_LOADFAIL = 2;
		public static final int MSG_ADV_IMAGE_lOADFINISHED = 5;
		public static final int MSG_ADV_IMAGE_LOADFAIL = 6;
		public static final int MSG_SHARE_LOADFAIL = 3;
		public static final int MSG_SHARE_lOADFINISHED = 4;
	}
	
	public static class SharePrefer{
		public static final String SharePrefer_User_Name="shareprefer_login";
		public static final String SharePrefer_User_UserID="id";
		public static final String SharePrefer_User_UserName="name";
		public static final String SharePrefer_User_UserPic="pic";
		public static final String SharePrefer_User_UserGender="gender";
	}
	
	//友盟
	public static class UM{
		public static final String DESCRIPTOR = "com.umeng.share";
		
		private static final String TIPS = "请移步官方网站 ";
		private static final String END_TIPS = ", 查看相关说明.";
		public static final String TENCENT_OPEN_URL = TIPS + "http://wiki.connect.qq.com/android_sdk使用说明" 
														+ END_TIPS;
		public static final String PERMISSION_URL = TIPS + "http://wiki.connect.qq.com/openapi权限申请" 
														+ END_TIPS;
		
		public static final String SOCIAL_LINK = "http://www.umeng.com/social";
		public static final String SOCIAL_TITLE = "友盟社会化组件帮助应用快速整合分享功能";
		public static final String SOCIAL_IMAGE = "http://www.umeng.com/images/pic/banner_module_social.png";
		
		public static final String SOCIAL_CONTENT = "友盟社会化组件（SDK）让移动应用快速整合社交分享功能，我们简化了社交平台的接入，为开发者提供坚实的基础服务：（一）支持各大主流社交平台，" + 
													"（二）支持图片、文字、gif动图、音频、视频；@好友，关注官方微博等功能" +
													"（三）提供详尽的后台用户社交行为分析。http://www.umeng.com/social";
		
	}
}
