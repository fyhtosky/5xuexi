package com.sj.yinjiaoyun.xuexi.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;


/**
 * Created by Administrator on 2016/8/19.
 * 注册里面的 用户协议界面
 */
@SuppressLint("SetJavaScriptEnabled")
public class ArgeementActivity extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_argeement);
        WebView wedView= (WebView) findViewById(R.id.argeement_webView);
         WebSettings wSet = wedView.getSettings();
         wSet.setJavaScriptEnabled(true);
        try {
            //设置打开的页面地址
            wedView.loadUrl("file:///android_asset/index.html");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.agreement_back://返回
                finish();
                break;
        }
    }
}
