package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.CheckInfoBean;
import com.sj.yinjiaoyun.xuexi.bean.JsCallBackBean;
import com.sj.yinjiaoyun.xuexi.bean.JsReturnBean;
import com.sj.yinjiaoyun.xuexi.bean.UserInfo;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.loginCommon.ThirdLoginManager;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.CXAESUtil;
import com.sj.yinjiaoyun.xuexi.utils.PhoneInfoUtils;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 作者：VanHua on 2018/5/8 14:40
 * <p>
 * 邮箱：1373516909@qq.com
 * 进入绑定H5页面  for api（移动android - ios）
 */
public class BindWebActivity  extends JSBaseActivity {
    private String BaseUrl = Api.CHECH_BIND_LOGIN_PAGE;
    private String unionid="";
    private String name="";
    private String iconurl="";
    //表示第三方平台 1为QQ;2为微信
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        if(intent!=null){
            unionid=intent.getStringExtra(ThirdLoginManager.UNIONID);
            type=intent.getStringExtra(ThirdLoginManager.TYPE);
            name=intent.getStringExtra(ThirdLoginManager.USER_NAME);
            iconurl=intent.getStringExtra(ThirdLoginManager.ICONURL);
        }
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected String getJsUrl() {
        String key=Api.AESKEY;
        if(unionid!=null && !TextUtils.isEmpty(unionid)){
            String token= CXAESUtil.encryptString(key,type+"_"+unionid);
            String fromSite="1";
            String platform="2";
            String tokenType=type;
            try {
                return BaseUrl+"?ltoken="+token+"&fromSite="+fromSite+"&platform="+platform+"&tokenType="+tokenType
                        +"&name="+ URLEncoder.encode(name, "utf-8")+"&figureurl="+iconurl;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return BaseUrl;
    }

    @Override
    protected void onResult(String result) {

    }

    @Override
    public void getStatus(String status) {
        Logger.d("回调的结果："+status);
        JsCallBackBean jsCallBackBean= new Gson().fromJson(status, JsCallBackBean.class);
        if(jsCallBackBean.getStatus()==200){
            switch (type){
                case "1":
                    ToastUtil.showShortToast(MyApplication.getContext(),"QQ账号绑定成功");
                    break;
                case "2":
                    ToastUtil.showShortToast(MyApplication.getContext(),"微信账号绑定成功");
                    break;

            }
            saveUserInfo(jsCallBackBean.getData().getData().getUser().getToken());
        }else {
            ToastUtil.showShortToast(BindWebActivity.this,jsCallBackBean.getData().getMessage());
        }
    }

    /**
     * 登录操作
     * @param token
     */
    private void saveUserInfo(String token) {
        HashMap<String,String> map=new HashMap<>();
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
                    startActivity(new Intent(BindWebActivity.this, MainActivity.class));
                    finish();
                    MyApplication.isLoginSkip = true;
                    //清除所有Activity
                    ActiveActUtil.getInstance().exit();

                }
            }

        });
    }

    /**
     * 存储用户的基本信息
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
