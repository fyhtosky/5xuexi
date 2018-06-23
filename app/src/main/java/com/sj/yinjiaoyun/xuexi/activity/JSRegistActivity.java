package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.sj.yinjiaoyun.xuexi.http.Api;

/**
 * Created by Administrator on 2018/1/16.
 * 注册界面
 */

public class JSRegistActivity extends JSBaseActivity {

    private String BaseUrl = Api.TOKEN_REGIST;

    /**
     * 线上
     */
//    private String BaseUrl = "http://ixue.5xuexi.com/app/toRegisterPage.action";
    @NonNull
    @Override
    protected String getJsUrl() {
        return BaseUrl;
    }

    @Override
    protected void onResult(String result) {
        Toast.makeText(JSRegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(JSRegistActivity.this,LoginActivity.class));
        JSRegistActivity.this.finish();

    }
}
