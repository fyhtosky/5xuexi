package com.sj.yinjiaoyun.xuexi.http;

/**
 * Created by wanzhiying on 2017/3/6.
 */
public class Api {
    public static  final String AESKEY="abcdefgabcdefg11";
    /**
     * I学令牌的服务器
     */
    //线上I学令牌
    public static  String TOKEN_HOST="http://ixue.5xuexi.com";
    //测试I学令牌
//    public static  String TOKEN_HOST="http://139.196.254.188:8080";

     //文波的
//    public static  String TOKEN_HOST="http://192.168.1.176";

    /**
     * IM服务器
     */
    //线上服务器
//	public static  String XMPP_HOST = "139.224.32.120";
    //测试的服务器
    public static  String XMPP_HOST = "139.196.155.123";

    /**
     *
     * 下载版本包URI
     */
    public static String DOWN_APK_URL;
    /**
     * 接口服务器
     */
       public static  String Host="http://soa.5xuexi.com";//正式环境
//    public static  String  Host="http://139.196.255.175:8083";//测试环境
     //文波测试环境
//    public static  String Host="http://192.168.1.176:8083";
     //小平测试环境
//     public static  String Host="http://192.168.0.118:80";
      //小葵的测试环境
//     public static  String Host="http://192.168.1.125";



    /**
     * 上传图片库
     */
    //线上
//    public static String PHOTO_HOST="http://picroom.5xuexi.com";
    //测试的
    public static String PHOTO_HOST="http://139.196.255.175:8086";
    /**
     * 上传图片的接口
     */
    public static  String UPLOAD_PHOTO=PHOTO_HOST+"/picroom/imPicWithBase.action";

    /**
     * 获取接口地址、
     * 校验版本信息、
     * 提示是否升级
     */
    public static  String GET_APP_URI_CONFIG=Host+"/api/v2/app/getAppStaticProperties.action";

    /**
     * 最近会话列表
     */
    public static final String GET_RECENT_CHAT_LIST=Host+"/api/im/findRecentlyMsg.action";
    /**
     * 获取群列表
     */
   public static final String GET_GROUP_LIST=Host+"/api/im/groupChat/findMyGroupsByEndUserId.action";
    /**
     * 保存聊天记录
     */
    public static final String SAVE_CHAT_LOG=Host+"/api/im/msg/saveMsg.action";
    /**
     * 获取聊天信息
     */
    public static final String GET_CHAT_MESSAGE=Host+"/api/im/findChatJidMsg.action";
    /**
     * 群 -成员列表<br>
     */
    public static final String POST_GROUP_MEMBERS=Host+"/api/im/groupChat/findGroupMembersByGroupId.action";

    /**
     * 群 –基本信息<br>
     */
    public static final String GET_GROUP_INFO=Host+"/api/im/groupChat/getGroupChatInfo.action";
    /**
     * IM获取个人基本信息
     */
    public static final String GET_PERSONAL_INFO=Host+"/api/im/getUserInfo.action";
    /**
     * 清除会话计数
     */
    public static final String CLEAR_SESSION_COUNT=Host+"/api/im/clearSessionCount.action";

    /**
     * 消息免打扰
     */
    public static final String SET_NOTDISTURB=Host+"/api/im/setNotDisturb.action";
    /**
     * 收藏消息
     */
    public static final String COLLECT_MESSAGE=Host+"/api/im /addTigaseMsgEnshrine.action";
    /**
     * 获取收藏消息
     */
    public static final String GET_COLLECT_MESSAGE_LIST=Host+"/api/im /findTigaseMsgEnshrineByPg.action";
    /**
     * 删除收藏消息
     */
    public static final String DELETE_COLLECT_MESSAGE=Host+"/api/im /deleteMsgEnshrine.action";
    /**
    * 修改密码
    */
   public static final String CHANGE_PWD=Host+"/api/v2/password/modifierPassword.action";
    /**
     * 获取验证码
     */
    public static final String GET_PHONE_CODE=Host+"/api/v2/comm/sendSmscode.action";
   /**
    * 判断手机号码的有效性
    */
   public static final String CHECH_PHONE_NUMBER=Host+"/api/v2/user/checkMyMobile.action";
    /**
    * 修改手机号码
    */
  public static final String UPDATE_PHONE_NUMBER=Host+"/api/v2/user/updateMyMobile.action";
    /**
     * 更新用户信息
     */
    public static final String MODIFY_USER_INFO=Host+"/api/v2/user/updateMyBaseInfo.action";
    /**
     * 获取个人信息
     */

    public static final String GET_USER_INFO=Host+"/api/v2/user/findUserById.action";
    /**
     * 公共上传图片
     */
    public static final String COMMON_UPLOAD_PHOTO="http://139.196.255.175:8086/picroom/commPicWithBase.action";

    /**
     * 获取图片验证码
     */
    public static final String GET_CODE_IMG=Host+"/api/v2/sms/createValidateCodeImg.action";
    /**
     * 我的专业列表的界面
     */
    public static final String FIND_MY_PRODUCT_FOR_CHOOSE=Host+"/api/v2/product/findMyProductForChoose.action";
    /**
     * 选择专业方向的
     */
    public static final String FIND_DIRECTION_ENROLLPLAY=Host+"/api/v2/myProduct/findDirectionByEnrollPlanId.action";
    /**
     * 保存选择专业
     */
    public static final String ADD_USER_DIRECTION=Host+"/api/v2/product/addEndUserDirection.action";
    /**
     * 我的课程列表
     */
    public static final String FIND_COURSE_LIST=Host+"/api/v2/myProduct/findMyCourseScheduleVOByPar.action";
    /**
     * 我的专业可选择的课程
     */
    public static final String FIND_COURSE_OPTIONAL=Host+"/api/v2/myProduct/findMyTeachingPlanVOByPar.action";
    /**
     * 保存选择专业的课程
     */
    public static final String ADD_COURSE_SCHEDULES=Host+"/api/v2/myProduct/addCourseSchedules.action";
    /**
     *
     * 获取视频地址
     */
    public static final String GET_VIDEO_ADDRESS=Host+"/api/v2/learn/getCoursewareVideoUrl.action";

    /**
     * 基本信息
     * 根据ltoken 登录
     */
    public static final String  DO_LOGIN_BY_TOKEN=Host+"/api/v2/passport/doLoginByItoken.action";


    /**
     * I学令牌
     * 忘记密码界面所需要的url
     */
    public static final String TOKEN_FORGET_PWD=TOKEN_HOST+"/app/toForgetPasswordPage.action";

    /**
     * I学令牌
     * 修改密码所需要的url
     */
    public static final String TOKEN_CHANGE_PWD=TOKEN_HOST+"/app/toUpdatePasswordPage.action";

    /**
     * I学令牌
     * 修改手机号所需要的url
     */
    public static final String TOKEN_CHANGE_PHONE=TOKEN_HOST+"/app/toUpdatePhonePage.action";

    /**
     * I学令牌
     * 用户注册界面
     */
    public static final String TOKEN_REGIST=TOKEN_HOST+"/app/toRegisterPage.action";

    /**
     * I学令牌
     * 修改用户名称
     */
    public static final String TOKEN_CHANGE_NAME=TOKEN_HOST+"/app/toUpdateUserNamePage.action";

    /**
     * I学令牌
     * 修改用户ID
     */
   public static final String TOKEN_CHENGAE_ID_CARD=TOKEN_HOST+"/app/toUpdateCardPage.action";

    /**
     * I学令牌
     * 第三方账号登录check(有无账号)
     */
    public static final String CHECK_HAS_BIND_INFO=TOKEN_HOST+"/api/user/checkHasBindInfo.action";

    /**
     * I学令牌
     * 绑定H5界面
     */
    public static final String CHECH_BIND_LOGIN_PAGE=TOKEN_HOST+"/tlogin/checkBindLoginPage.action";

    /**
     * I学令牌
     * 解除和绑定QQ 、微信
     */
    public static final String UNBIND_THIRD_LOGIN=TOKEN_HOST+"/api/user/unbindThirdLoginForApi.action";

    /**
     * I学令牌
     * 绑定QQ、微信
     */
    public static final String BIND_THIRD_LOGIN=TOKEN_HOST+"/bindloginInfoForApi.action";
}
