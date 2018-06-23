package com.sj.yinjiaoyun.xuexi.entry;

import android.util.Log;

import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.PhoneInfoUtils;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/10/28.
 * 登录、退出记录用户登录状态信息接口
 */
public  class LoginStatusToHttp implements HttpDemo.HttpCallBack {

    private static List<Pairs> pairsList;
    static HttpDemo demo;
    //标示用户登录状态
    private static boolean loginState=false;
    static String url= MyConfig.getURl("user/updateUserLoginStatus");
    static String TAG="abcde";
    public LoginStatusToHttp() {
        Log.i(TAG, "LoginStatusToHttp: ");
        demo=new HttpDemo(this);
    }

    /**
     * 安卓用户上线
     * @param userId  用户id
     */
    public  void userUpLine(String userId){
        Log.i(TAG, "userUpLine: id="+userId);
        loginState=PreferencesUtils.getSharePreBoolean(MyApplication.getContext(), Const.LOGIN_STATE);
        //未登录则不登陆IM服务器
        if(!loginState){
            return;
        }
        if(userId==null){
            return;
        }
        pairsList=new ArrayList<>();
        pairsList.add(new Pairs("userId",userId));
        pairsList.add(new Pairs("loginStatus","1"));//登录状态 0 ：下线 1：上线
        pairsList.add(new Pairs("userType","0"));//用户类型 0：前台用户 1：后台用户
        pairsList.add(new Pairs("loginSystem","1"));//登录来源 1:android;2:ios
        pairsList.add(new Pairs("ip", PhoneInfoUtils.getIPAddress(MyApplication.getContext())));
//        pairsList.add(new Pairs("appPhone",PhoneInfoUtils.getNativePhoneNumber(MyApplication.getContext())));
        demo.doHttpPost(url,pairsList,1);
    }

    /**
     * 安卓用户下线
     * @param userId   用户id
     */
    public  void userUnderLine(String userId){
        Log.i(TAG, "userUnderLine: id="+userId);
        loginState=PreferencesUtils.getSharePreBoolean(MyApplication.getContext(), Const.LOGIN_STATE);
        //未登录则不登陆IM服务器
        if(!loginState){
            return;
        }
        pairsList=new ArrayList<>();
        pairsList.add(new Pairs("userId",userId));
        pairsList.add(new Pairs("loginStatus","0"));//登录状态  0 ：下线 1：上线
        pairsList.add(new Pairs("userType","0"));//用户类型 0：前台用户 1：后台用户
        pairsList.add(new Pairs("loginSystem","1"));//登录来源 1:android;2:ios
        pairsList.add(new Pairs("ip", PhoneInfoUtils.getIPAddress(MyApplication.getContext())));
//        pairsList.add(new Pairs("appPhone",PhoneInfoUtils.getNativePhoneNumber(MyApplication.getContext())));
        demo.doHttpPost(url,pairsList,1);
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+requestCode+":"+msg);
    }

}
