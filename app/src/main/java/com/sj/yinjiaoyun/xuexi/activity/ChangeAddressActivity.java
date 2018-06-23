package com.sj.yinjiaoyun.xuexi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.view.CityPicker;
import com.sj.yinjiaoyun.xuexi.view.CityPickerListener;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChangeAddressActivity extends MyBaseActivity implements CityPickerListener {

    @BindView(R.id.et_region)
    EditText etRegion;
    @BindView(R.id.et_street)
    EditText etStreet;
    private CityPicker cityPicker;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        cityPicker = new CityPicker(ChangeAddressActivity.this, this);
        mUserName = PreferencesUtils.getSharePreStr(this, "username");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.getNetworkSatte(ChangeAddressActivity.this);
    }

    @OnClick({R.id.iv_cancel, R.id.tv_confirm, R.id.et_region})
    public void onClick(View view) {
        switch (view.getId()) {
            //取消
            case R.id.iv_cancel:
                finish();
                break;
            //保存
            case R.id.tv_confirm:
                if(!TextUtils.isEmpty(etRegion.getText().toString().trim())){
                    if(!TextUtils.isEmpty(etStreet.getText().toString().trim())){
                     initData();
                     }else{
                    ToastUtil.showShortToast(ChangeAddressActivity.this,"请填写街道门牌信息");
                   }
             }else{
                    ToastUtil.showShortToast(ChangeAddressActivity.this,"请选择省市区");
                }
                break;
            //选择省市区
            case R.id.et_region:
                cityPicker.show();
                if (cityPicker.isShow()){
                    hideSoftInputView();
                    return;
                }
                break;
        }
    }

    @Override
    public void getCity(String s) {
        etRegion.setText(s);
        etRegion.setSelection(s.length());
    }
    @Override
    public void onBackPressed() {
        if (cityPicker.isShow()){
            cityPicker.close();
            return;
        }
        super.onBackPressed();
    }
    /**
     * 发送请求修改家庭地址
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
        HashMap<String,String>map=new HashMap<>();
        map.put("id",mUserName);
        map.put("type",String.valueOf(10));
        map.put("content",etRegion.getText().toString().trim()+etStreet.getText().toString().trim());
        HttpClient.post(this, Api.MODIFY_USER_INFO,map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
//                    ToastUtil.showShortToast(ChangeAddressActivity.this,"地址保存成功");
                    finish();
                }else{
                    ToastUtil.showShortToast(ChangeAddressActivity.this,result.getMessage());
                }
            }

        });
    }
    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
