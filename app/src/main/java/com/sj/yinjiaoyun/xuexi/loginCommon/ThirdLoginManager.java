package com.sj.yinjiaoyun.xuexi.loginCommon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.activity.BindWebActivity;
import com.sj.yinjiaoyun.xuexi.activity.MainActivity;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.CheckInfoBean;
import com.sj.yinjiaoyun.xuexi.bean.UserInfo;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.PhoneInfoUtils;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：VanHua on 2018/5/11 11:02
 * <p>
 * 邮箱：Van_Hua@qq.com
 */
public class ThirdLoginManager {

    /**
     * 第三方登录成功之后用户的信息
     */
    public static final String USER_NAME="user_name";
    public static final String UNIONID="unionid";
    public static final String ICONURL="iconurl";

    /**
     * 标识第三方登录的平台
     * 1是QQ
     * 2是微信
     */
    public static final String TYPE="type";

    private Activity activity;



    public ThirdLoginManager( Activity activity) {
        this.activity = activity;
    }

    public void WeixinLogin() {
        final UMShareAPI umShareAPI = UMShareAPI.get(activity);
        umShareAPI.setShareConfig(new UMShareConfig().isNeedAuthOnGetUserInfo(true));
        //登录成功之后获取用户信息
        umShareAPI.getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, new LoginAuthListener(activity) {
            @Override
            protected void doComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                checkHasBindInfo(share_media,map);
            }
        });
    }


    /**
     * 微信绑定的掉
     * @param loginBindListener
     */
    public void WeixinLogin(final LoginBindListener loginBindListener) {
        final UMShareAPI umShareAPI= UMShareAPI.get(activity);
        umShareAPI.setShareConfig(new UMShareConfig().isNeedAuthOnGetUserInfo(true));
        //登录成功之后获取用户信息
        umShareAPI.getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, new LoginAuthListener(activity) {
            @Override
            protected void doComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                loginBindListener.doComplete(share_media, i, map);
            }
        });
    }
    /**
     * QQ登录
     * @param loginBindListener
     */
    public void qqLogin(final LoginBindListener loginBindListener) {
        final UMShareAPI umShareAPI=UMShareAPI.get(activity);
        umShareAPI.setShareConfig(new UMShareConfig().isNeedAuthOnGetUserInfo(true));
        //登录成功之后获取用户信息
        umShareAPI.getPlatformInfo(activity, SHARE_MEDIA.QQ, new LoginAuthListener(activity) {
            @Override
            protected void doComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Logger.d("获取信息的回调："+map.toString());
                loginBindListener.doComplete(share_media, i, map);
            }
        });
    }
    /**
     * 登录
     */
    public void qqLogin() {
        final UMShareAPI umShareAPI=UMShareAPI.get(activity);
        umShareAPI.setShareConfig(new UMShareConfig().isNeedAuthOnGetUserInfo(true));
        //登录成功之后获取用户信息
        umShareAPI.getPlatformInfo(activity, SHARE_MEDIA.QQ, new LoginAuthListener(activity) {
            @Override
            protected void doComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Logger.d("获取信息的回调："+map.toString());
                checkHasBindInfo(share_media,map);
            }
        });
    }
    /**
     * 判断用户是否登录
     * 1.判断用户登录方式
     * 2.
     * @param share_media
     * @param map
     */
    private void checkHasBindInfo(SHARE_MEDIA share_media, final Map<String, String> map) {
        //表示登录的方式
        PreferencesUtils.putSharePre(MyApplication.getContext(), Const.FLAG, 3);
        Logger.d("存储用户信息："+map.toString());
        String loginType = "";
        HashMap<String,String> info=new HashMap<>();
        info.put("openid",map.get("openid"));
        info.put("unionid",map.get("unionid"));
        switch (share_media){
            case QQ:
                loginType="1";
                break;
            case WEIXIN:
                loginType="2";
                break;
        }
        info.put("type",loginType);
        info.put("name",map.get("name"));
        info.put("figureurl",map.get("iconurl"));
        info.put("fromSite","1");
        info.put("platform","2");
        final String finalLoginType = loginType;
        HttpClient.post(this, Api.CHECK_HAS_BIND_INFO, info, new CallBack<CheckInfoBean>() {
            @Override
            public void onSuccess(CheckInfoBean result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
                    //有账号则登录操作
                    saveUserInfo(finalLoginType,result.getData().getUser().getToken());
                }else {
                    //没有账号
                    Intent forIntent = new Intent(activity, BindWebActivity.class);
                    forIntent.putExtra(USER_NAME, map.get("name"));
                    forIntent.putExtra(UNIONID,map.get("unionid"));
                    forIntent.putExtra(ICONURL,map.get("iconurl"));
                    forIntent.putExtra(TYPE, finalLoginType);
                    activity.startActivity(forIntent);
                }
            }
        });
    }

    /**
     * 1.获取I学令牌的Token
     * 2.获取5学习的用户信息
     */
    private void saveUserInfo(final String loginType, String token) {
        HashMap<String,String>map=new HashMap<>();
        map.put("token",token);
        map.put("loginSystem","1");
        map.put("ip", PhoneInfoUtils.getIPAddress(MyApplication.getContext()));
        HttpClient.post(this, Api.DO_LOGIN_BY_TOKEN ,map, new CallBack<UserInfo>() {
            @Override
            public void onSuccess(UserInfo result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
                    sharedUserInfo(result);
                    //跳转操作
                    //退出XMpp
                    if(MyApplication.xmppConnection!=null ){
                        if(MyApplication.xmppConnection.isAuthenticated()){
                            MyApplication.xmppConnection.disconnect();
                            MyApplication.xmppConnection=null;
                            MyApplication.groupsList=null;
                            MyApplication.list=null;

                        }
                    }
                    //清除所有Activity
                    ActiveActUtil.getInstance().exit();
                    Intent intent=new Intent(activity,MainActivity.class);
                    activity.startActivity(intent);
                    MyApplication.isLoginSkip = true;
                }
            }

        });
    }

    /**
     * 偏好设置存储用户信息
     * @param result
     */
    private void sharedUserInfo(UserInfo result) {
        SharedPreferences.Editor  editor= MyApplication.getContext().getSharedPreferences(PreferencesUtils.QQXMPP, 0).edit();
        editor.putString(Const.TOKEN, result.getData().getUser().getToken());
        //表示登录的方式
        editor.putLong(Const.FLAG, 3);
        /**
         * 1.启动后台登录操作
         * 2.缓存所需要的数据
         */
        editor.putString("username", String.valueOf(result.getData().getUser().getEndUserId()));
        editor.putString("userImg", result.getData().getUser().getUserImg());
        editor.putString("realName", result.getData().getUser().getNickName());
        editor.putString("Name", result.getData().getUser().getNickName());
        editor.putString(Const.PASSWORD,result.getData().getUser().getPassword());
        //存储缓存
        editor.putString( Const.TOKEN, result.getData().getUser().getToken());
        editor.putBoolean(Const.LOGIN_STATE, true);
        editor.commit();
    }

}
