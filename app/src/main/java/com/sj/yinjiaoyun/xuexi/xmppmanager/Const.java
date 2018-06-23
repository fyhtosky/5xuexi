package com.sj.yinjiaoyun.xuexi.xmppmanager;


import com.sj.yinjiaoyun.xuexi.http.Api;

public class Const {
	/**
	 * IM服务器
	 */
	//线上服务器
	public static final String XMPP_HOST = Api.XMPP_HOST;
	public static final int XMPP_PORT = 5280;
	
	/**
	 * 登录状态广播
	 */
	public static final String ACTION_IS_LOGIN_SUCCESS = "com.android.qq.is_login_success";
	/**
	 * 消息记录操作广播
	 */
	public static final String ACTION_MSG_OPER= "com.android.qq.msgoper";
	/**
	 * 添加好友请求广播
	 */
	public static final String ACTION_ADDFRIEND= "com.android.qq.addfriend";
	/**
	 * 更新聊天界面的广播
	 */
	public  static final String ACTION_UPDATE_CHEAT_LOG="com.anrdois.qq.chat_log";
	
	//静态地图API
	public static  final String LOCATION_URL_S = "http://api.map.baidu.com/staticimage?width=320&height=240&zoom=17&center=";
	public static  final String LOCATION_URL_L = "http://api.map.baidu.com/staticimage?width=480&height=800&zoom=17&center=";
	
	public static final String MSG_TYPE_TEXT="msg_type_text";//文本消息
	public static final String MSG_TYPE_IMG="msg_type_img";//图片
	public static final String MSG_TYPE_LOCATION="msg_type_location";//位置
	
	public static final String MSG_TYPE_ADD_FRIEND="msg_type_add_friend";//添加好友
	public static final String MSG_TYPE_ADD_FRIEND_SUCCESS="msg_type_add_friend_success";//同意添加好友

	public static final String SPLIT="卍";
	public static final String TYPE="type";

	public static final int NOTIFY_ID=0x90;
	//传递群聊的id
	public static final String GROUP_ID="group_id";
	//传递群人员的总数
	public static  final String  GROUP_TOTAL="group_total";
	//群主的名称
	public static final String GROUP_NAME="group_name";
	//传递私聊的id
	public static final String CHAT_ID="chat_id";
	//私聊的名称
	public static final String CHAT_NAME="chat_name";
	//私聊图片的url
	public static final String CHAT_LOGO="chat_logo";
	//@功能的数据携带
    public static final String CHOOSE_REPLY="chat_reply";
	public static final String CHOOSE_JID="choose_jid";
	//是否登陆过
	public static final String LOGIN_STATE="login_state";
	//标示是否是第一次安装
	public static final String ISFIRST="isFirsts";
	//关闭Service的广播
	public static final String STOP_SELF="stop_self";
    //标示CkeckBox是否选中
	public static final String IS_CHKECK="is_check";
	//监听短信获取验证码再控件显示
	public static final int MSG_RECEIVED_CODE=1;




	//获取服务器存储的token
	public static final String TOKEN="token";
    //表示登录的方式1是账号登录2是学号登录
	public static final String FLAG="flag";
	//存储院校的id
	public static final String SCHOOL_ID="school_id";
	//存储登录的用户名
	public static final String LOGIN_NAME="login_name";
	//存储登录的密码
	public static final String PASSWORD="password";
	//表示Token是否失效
    public static final String IS_TOKEN_LOST="is_token_lost";

}
