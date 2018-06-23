package com.sj.yinjiaoyun.xuexi.jsforandroid;

import android.webkit.JavascriptInterface;

/**
 * Created by Administrator on 2018/1/8.
 */

public class ForgetPwdObject {
  private JSforAndroidListener listener;

    public ForgetPwdObject(JSforAndroidListener listener) {
        this.listener = listener;
    }

    @JavascriptInterface
    public void getAppStatus(String status){
        listener.getStatus(status);
    }


}
