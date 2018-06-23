package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.jpush.MyJPushUitl;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/23.
 * 我的 账户安全 修改密码
 */
public class ChangePwdActivity extends MyBaseActivity implements View.OnFocusChangeListener {

    @BindView(R.id.itleBarView)
    TitleBarView itleBarView;
    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.cb_pwd)
    CheckBox cbPwd;
    private boolean isCheck = false;
    private String mPassword;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_changepwd);
        ButterKnife.bind(this);
        itleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }

    private void init() {

        mUserName = PreferencesUtils.getSharePreStr(this, "username");
        mPassword = PreferencesUtils.getSharePreStr(this, "pwd");
        etOldPwd.setOnFocusChangeListener(this);
        etNewPwd.setOnFocusChangeListener(this);

    }

    @OnClick({R.id.cb_pwd, R.id.bt_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_pwd:
                isCheck = !isCheck;
                if (isCheck) {
                    cbPwd.setChecked(true);
                    // 显示密码
                    etNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    cbPwd.setChecked(false);
                    // 隐藏密码
                    etNewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                //设置EditText光标的位置
                etNewPwd.setSelection(etNewPwd.length());

                break;
            case R.id.bt_submit:
                if(checkOldePwd(etOldPwd.getText().toString().trim())){
                    if(checkPwd(etNewPwd.getText().toString().trim())){
                        initData();
                    }
                }
                break;
        }
    }

    /**
     * 发送请求修改密码
     */
    private void initData() {
        HashMap<String ,String> map=new HashMap<>();
        map.put("id",mUserName);
        map.put("password",etNewPwd.getText().toString().trim());
        map.put("passwordAgain",etNewPwd.getText().toString().trim());
        map.put("oldPassword",etOldPwd.getText().toString().trim());
        HttpClient.post(this, Api.CHANGE_PWD,map,new CallBack<ReturnBean>(){

            @Override
            public void onSuccess(ReturnBean result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
                    ToastUtil.showShortToast(ChangePwdActivity.this,"密码修改成功，请重新登录");
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
                    PreferencesUtils.putSharePre(ChangePwdActivity.this, Const.LOGIN_STATE,false);
                    Intent intent=new Intent(ChangePwdActivity.this,LoginActivity.class);
                    intent.putExtra("flag",1);
                    startActivity(intent);
                    MyJPushUitl.stopPush(ChangePwdActivity.this);
                    MyJPushUitl.setClearNotification(ChangePwdActivity.this);
                    ActiveActUtil.getInstance().exit();
                }else{
                    ToastUtil.showShortToast(ChangePwdActivity.this,result.getMessage());
                }
            }
        });
    }

    /**
     * 判断就密码
     * @param pwd
     * @return
     */
    private boolean checkOldePwd(String pwd){
         if(TextUtils.isEmpty(pwd)){
             ToastUtil.showShortToast(ChangePwdActivity.this, "请输入旧密码");
             return false;
         }
         if(!mPassword.equals(pwd)){
             ToastUtil.showShortToast(ChangePwdActivity.this, "密码输入错误");
             return false;
         }
         return true;
     }
    /**
     * 校验Editview编辑框密码是否合格
     * @param pwd
     * @return
     */
    private Boolean checkPwd(String pwd) {
        int length = pwd.length();
        if (pwd.equals("")) {
            ToastUtil.showShortToast(ChangePwdActivity.this, "密码不能为空");
            return false;
        }
        if (length < 6) {
            ToastUtil.showShortToast(ChangePwdActivity.this, "密码不能小于6位");
            return false;
        }
        if (length > 20) {
            ToastUtil.showShortToast(ChangePwdActivity.this, "密码最大支持20个字符");
            return false;
        }
        if (pwd.indexOf(" ") > 0) {
            ToastUtil.showShortToast(ChangePwdActivity.this, "密码不支持空格");
            return false;
        }
        String regex = "^((?=.*?\\d)(?=.*?[A-Za-z])|(?=.*?\\d)(?=.*?[符号])|(?=.*?[A-Za-z])(?=.*?[符号]))[\\dA-Za-z符号]+$";
        if (!pwd.matches(regex)) {
            ToastUtil.showShortToast(ChangePwdActivity.this, "密码必须含有字母、数字和符号两种及以上的组合");
            return false;
        }
        if(mPassword.equals(pwd)){
            ToastUtil.showShortToast(ChangePwdActivity.this, "新密码不能和旧密码相同");
            return false;
        }
        return true;
    }

    /**
     * EditText是否获得焦点监听器
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_new_pwd:
                if (hasFocus) {
                    checkOldePwd(etOldPwd.getText().toString().trim());
                }
                break;
        }
    }

}
