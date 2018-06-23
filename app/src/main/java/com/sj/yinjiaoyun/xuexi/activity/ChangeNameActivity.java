package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  //修改用户名
 */
public class ChangeNameActivity extends MyBaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    private String mUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mUserName = PreferencesUtils.getSharePreStr(this, "username");
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
          String  name=bundle.getString("name");
            if(name!=null){
                etName.setText(name);
                etName.setSelection(name.length());
            }

        }
    }
    /**
     * 启动Activity
     * @param context
     * @param name
     */
    public static void StartActivity(Context context, String name){
        Intent intent=new Intent(context,ChangeNameActivity.class);
        intent.putExtra("name",name);
        context.startActivity(intent);
    }
    @OnClick({R.id.iv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.tv_confirm:
               if( checkName(etName.getText().toString().trim())){
                   initData();
               }

                break;
        }
    }
    //校验Editview编辑框的用户名是否合格
    private  Boolean checkName(String name){
        int length=name.length();
        if(name.equals("")){
            ToastUtil.showShortToast(ChangeNameActivity.this,"请输入用户名");
            return false;
        }
        if(length<4){
            ToastUtil.showShortToast(ChangeNameActivity.this,"用户名不能小于4个字符");
            return false;
        }
        if(length>20){
            ToastUtil.showShortToast(ChangeNameActivity.this,"用户名最大支持20个字符");
            return false;
        }
        String a="^(?!_)(?![0-9])[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";//中文、字母、数字、下划线的组合,不能以数字开头，下划线开头
        if(!name.matches(a)){
            ToastUtil.showShortToast(ChangeNameActivity.this,"用户名首字符必须为中文或字母,支持中文、字母、数字、下划线的组合");
            return false;
        }

        return true;
    }
    /**
     * 发送请求修改用户名
     */
    private void initData() {
        /**
         * type
         * 1. 头像
         * 2. 姓名：(有学籍)不可编辑
         * 3. 性别：(有学籍)不可编辑性别 1:男       2:女        3:保密
         * 4. 身份证号：(有学籍)不可编辑
         * 5. 用户名
         * 6. 民族
         * 7. 政治面貌
         * 8. 固话
         * 9. 邮政编码
         * 10. 家庭地址

         */
        HashMap<String,String> map=new HashMap<>();
        map.put("id",mUserName);
        map.put("type",String.valueOf(5));
        map.put("content",etName.getText().toString().trim());
        HttpClient.post(this, Api.MODIFY_USER_INFO , map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
                    ToastUtil.showShortToast(ChangeNameActivity.this,"用户名修改成功");
                    finish();
                }else{
                    ToastUtil.showShortToast(ChangeNameActivity.this,result.getMessage());
                }
            }

        });
    }
}
