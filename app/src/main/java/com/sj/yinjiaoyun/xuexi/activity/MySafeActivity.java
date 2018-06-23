package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.view.MeView;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/2.
 * 主页- 我fragment- 我的账户 页面
 */
public class MySafeActivity extends MyBaseActivity {


    @BindView(R.id.mysafe_titleBarView)
    TitleBarView mysafeTitleBarView;
    @BindView(R.id.myaccount_phone)
    MeView myaccountPhone;
    @BindView(R.id.myaccount_username)
    MeView myaccountUsername;
    @BindView(R.id.myaccount_idCard)
    MeView myaccountIdCard;
    @BindView(R.id.myaccount_password)
    MeView myaccountPassword;
    private String endUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myaccount);
        ButterKnife.bind(this);
        mysafeTitleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        endUserId = PreferencesUtils.getSharePreStr(this, "username");

    }

    @Override
    protected void onResume() {
        super.onResume();
        setDateRequest();
    }

    private void setDateRequest() {
        String params = "?id=" + endUserId;
        HttpClient.get(this, Api.GET_USER_INFO + params, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
                //显示用户名
                if (result.getData().getUser().getUserName() != null) {
                    myaccountUsername.setValuesForMark(result.getData().getUser().getUserName());
                }
                //显示手机号
                if (result.getData().getUser().getPhone() != null) {
                    myaccountPhone.setValuesForMark(result.getData().getUser().getPhone());
                }
                //显示身份证号码
                if (result.getData().getUser().getIdCard() != null) {
                    myaccountIdCard.setValuesForMark(result.getData().getUser().getIdCard());
                }
                //显示密码
                if(result.getData().getUser().getPassword()!=null){
                    myaccountPassword.setValuesForMark(result.getData().getUser().getPassword().substring(0,6));
                    myaccountPassword.getEtMark().setTransformationMethod(PasswordTransformationMethod
                            .getInstance());  //以密文显示，以.代替
                }
            }


        });
    }

    public void onclick(View v) {
        switch (v.getId()) {
            //修改用户名
            case R.id.myaccount_username:
//               ChangeNameActivity.StartActivity(MySafeActivity.this,name);
                startActivity(new Intent(MySafeActivity.this, JSChangeNameActivity.class));
                break;
            case R.id.myaccount_password://修改登录密码
//                Intent intent = new Intent(this, ChangePwdActivity.class);
//                startActivity(intent);
                startActivity(new Intent(MySafeActivity.this, JSChangePwdActivity.class));
                break;
            case R.id.myaccount_phone://修改手机号码
//                Intent intent1 = new Intent(this, ChangePhoneActivity.class);
//                startActivity(intent1);
                startActivity(new Intent(MySafeActivity.this, JSChangePhoneActivity.class));
                break;
            case R.id.myaccount_idCard://修改生份证号码
                startActivity(new Intent(MySafeActivity.this, JSChangeIdCardActivity.class));
                break;
        }
    }
}
