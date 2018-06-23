package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.jpush.MyJPushUitl;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;


/**
 * 修改密码
 */
public class JSChangePwdActivity extends JSBaseActivity {

    private String BaseUrl = Api.TOKEN_CHANGE_PWD;
    private String url = Api.TOKEN_HOST;
    /**
     * 线上
     */
//     private String BaseUrl = "http://ixue.5xuexi.com/app/toUpdatePasswordPage.action";
//    private String url = "ixue.5xuexi.com";

    @Override
    protected String getJsUrl() {
        return BaseUrl;
    }

    /**
     * 修改密码成功之后重新登录
     */
    @Override
    protected void onResult(String result) {
        ToastUtil.showShortToast(JSChangePwdActivity.this,"密码修改成功，请重新登录");
        //退出XMpp
        if(MyApplication.xmppConnection!=null ){
            if(MyApplication.xmppConnection.isAuthenticated()){
                MyApplication.xmppConnection.disconnect();
                MyApplication.xmppConnection=null;
                MyApplication.groupsList=null;
                MyApplication.list=null;

            }
        }
        //设置登录状态
        PreferencesUtils.putSharePre(JSChangePwdActivity.this, Const.LOGIN_STATE,false);
        Intent intent=new Intent(JSChangePwdActivity.this,LoginActivity.class);
        intent.putExtra("flag",1);
        startActivity(intent);
        MyJPushUitl.stopPush(JSChangePwdActivity.this);
        MyJPushUitl.setClearNotification(JSChangePwdActivity.this);
        ActiveActUtil.getInstance().exit();
    }


    @Override
    protected void syncCookie() {
        super.syncCookie();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
            }
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除以前的cookie
            cookieManager.removeAllCookie();
            cookieManager.setCookie(url, "domain=" + url);
            cookieManager.setCookie(url, "path=/");
            cookieManager.setCookie(url, "ixuetoken=" + cookies);
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
