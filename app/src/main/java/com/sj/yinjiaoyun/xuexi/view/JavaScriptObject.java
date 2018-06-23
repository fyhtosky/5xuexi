package com.sj.yinjiaoyun.xuexi.view;

import android.webkit.JavascriptInterface;

/**
 * Created by limxing on 16/10/8.
 */

public class JavaScriptObject {


    private final JavaScriptListener listener;

    //    @JavascriptInterface //sdk17版本以上加上注解
    public JavaScriptObject(JavaScriptListener listener) {
        this.listener = listener;
    }
    @JavascriptInterface
    public void fun1FromAndroid(String name) {
        listener.city(name);
    }

    @JavascriptInterface
    public void cancle() {
        listener.cancle();
    }
}
