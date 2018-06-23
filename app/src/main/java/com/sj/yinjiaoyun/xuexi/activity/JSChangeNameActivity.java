package com.sj.yinjiaoyun.xuexi.activity;

import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;

/**
 * Created by Administrator on 2018/1/16.
 * 修改I学名称
 */

public class JSChangeNameActivity extends JSBaseActivity {
    private String BaseUrl = Api.TOKEN_CHANGE_NAME;
    private String url = Api.TOKEN_HOST;
    /**
     * 线上的
     */
//    private String BaseUrl = "http://ixue.5xuexi.com/app/toUpdateUserNamePage.action?";
//    private String url = "ixue.5xuexi.com";
    @NonNull
    @Override
    protected String getJsUrl() {
        return BaseUrl;
    }

    @Override
    protected void onResult(String result) {
        //偏好设置存储登录状态
        PreferencesUtils.putSharePre(JSChangeNameActivity.this, "realName", result);
        finish();
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
