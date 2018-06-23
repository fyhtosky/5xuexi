package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sj.yinjiaoyun.xuexi.http.Api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/1/16.
 * 忘记密码
 */

public class ForgetPwdActivity extends JSBaseActivity {
    private String BaseUrl = Api.TOKEN_FORGET_PWD;
    private String phone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        if(intent!=null){
            phone=intent.getStringExtra(LoginActivity.PHONE);
        }
        super.onCreate(savedInstanceState);
    }



    @NonNull
    @Override
    protected String getJsUrl() {
        try {
            if(phone!=null && !TextUtils.isEmpty(phone)){
                return BaseUrl+"?userName="+ URLEncoder.encode(phone, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return BaseUrl;
    }

    /**
     * 重置密码成功之后该界面关闭
     */
    @Override
    protected void onResult(String result) {
        finish();
    }
}
