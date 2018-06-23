package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class ChangeIdCardActivity extends MyBaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_id_card);
        ButterKnife.bind(this);
        init();
    }
    private void init() {
        mUserName = PreferencesUtils.getSharePreStr(this, "username");
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String  idCard=bundle.getString("IdCard");
            if(idCard!=null){
                etName.setText(idCard);
                etName.setSelection(idCard.length());
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.getNetworkSatte(ChangeIdCardActivity.this);
    }

    @OnClick({R.id.iv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.tv_confirm:
                if(!TextUtils.isEmpty(etName.getText().toString().trim())){
                    if(etName.getText().toString().trim().length()==18){
                        initData();
                    }else{
                        ToastUtil.showShortToast(ChangeIdCardActivity.this,"请输入有效身份证号");
                    }

                }else{
                    ToastUtil.showShortToast(ChangeIdCardActivity.this,"请输入身份证号");
                }
                break;
        }
    }

    /**
     * 启动Activity
     * @param context
     * @param card
     */
    public static void StartActivity(Context context,String card){
        Intent intent=new Intent(context,ChangeIdCardActivity.class);
        intent.putExtra("IdCard",card);
        context.startActivity(intent);
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
        map.put("type",String.valueOf(4));
        map.put("content",etName.getText().toString().trim());
        HttpClient.post(this, Api.MODIFY_USER_INFO , map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
//                    ToastUtil.showShortToast(ChangeIdCardActivity.this,"身份证号码修改成功");
                    finish();
                }else{
                    ToastUtil.showShortToast(ChangeIdCardActivity.this,result.getMessage());
                }
            }

        });
    }
}
