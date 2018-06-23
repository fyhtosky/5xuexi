package com.sj.yinjiaoyun.xuexi.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.SmsEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.ImgCodeBean;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.receiver.SmsContentObserver;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.TimeCountUtil;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePhoneActivity extends MyBaseActivity implements View.OnFocusChangeListener {


    @BindView(R.id.itleBarView)
    TitleBarView itleBarView;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.bt_code)
    Button btCode;
    @BindView(R.id.et_Imcode)
    EditText etImcode;
    @BindView(R.id.iv_Imcode)
    ImageView ivImcode;
    private String mUserName;
    //获取短信的内容
    private SmsContentObserver observer;
    //提取图片的验证key
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
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
        //注册EventBus
        EventBus.getDefault().register(this);
        mUserName = PreferencesUtils.getSharePreStr(this, "username");
        etImcode.setOnFocusChangeListener(this);
        etCode.setOnFocusChangeListener(this);
        //获取图片验证码
        getImgCode();
        observer = new SmsContentObserver(ChangePhoneActivity.this, new Handler());
        Uri uri = Uri.parse("content://sms");
        //第二个参数： 是否监听对应服务所有URI监听  例如sms 所有uri
//        监听ContentProvider的数据改变
        getContentResolver().registerContentObserver(uri, true, observer);

    }

    private boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast(ChangePhoneActivity.this, "请输入手机号码");
            return false;
        }
        String regex = "^[1][3-8][0-9]{9}$";
        if (!phone.matches(regex)) {
            ToastUtil.showShortToast(ChangePhoneActivity.this, "请输入有效的手机号码");
            return false;
        }
        return true;
    }


    /**
     * 监听短信的验证码拦截
     * @param smsEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SmsEvent smsEvent) {
        if (smsEvent.getSmsContent() != null) {
            //设置Edittext获取焦点
            etPhone.setFocusable(false);
            etCode.setFocusable(true);
            etCode.setText(smsEvent.getSmsContent());
            etCode.setSelection(smsEvent.getSmsContent().length());

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(observer);
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.bt_code, R.id.bt_submit,R.id.iv_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_code:
                if (checkPhone(etPhone.getText().toString().trim())) {
                    //发送网络请求判断手机号码的有效性
                    if(!TextUtils.isEmpty(etImcode.getText().toString().trim())){
                        checkPhoneNumber();
                    }else{
                        ToastUtil.showShortToast(ChangePhoneActivity.this,"请输入图片验证码");
                    }

                }
                break;
            case R.id.bt_submit:
                if (checkPhone(etPhone.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(etCode.getText().toString())) {
                        initData();
                    } else {
                        ToastUtil.showShortToast(ChangePhoneActivity.this, "请输入手机验证码");
                    }
                }
                break;
            //刷新图片验证码
            case R.id.iv_refresh:
                getImgCode();
                etImcode.setText("");
                etImcode.setFocusable(true);
                break;
        }
    }

    /**
     * 修改手机号
     */
    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", mUserName);
        map.put("phone", etPhone.getText().toString().trim());
        map.put("smsCode", etCode.getText().toString().trim());
        HttpClient.post(this, Api.UPDATE_PHONE_NUMBER, map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                    ToastUtil.showShortToast(ChangePhoneActivity.this, "手机号码修改成功");
                    finish();
                } else {
                    ToastUtil.showShortToast(ChangePhoneActivity.this, result.getMessage());
                }
            }

        });
    }

    /**
     * 发送网络请求获取号码有效性
     */
    private void checkPhoneNumber() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", mUserName);
        map.put("phone", etPhone.getText().toString().trim());
        HttpClient.post(this, Api.CHECH_PHONE_NUMBER, map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                    //获取验证码
                    getCode();
                } else {
                    ToastUtil.showShortToast(ChangePhoneActivity.this, result.getMessage());
                }
            }
        });
    }

    /***
     * 获取手机验证码
     */
    private void getCode() {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", etPhone.getText().toString().trim());
        map.put("validateCode",etImcode.getText().toString().trim());
        map.put("validateKey",uuid);
        map.put("smsFlag",String.valueOf(2));
        map.put("smsFrom",String.valueOf(0));
        HttpClient.postImgCode(this, Api.GET_PHONE_CODE, map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                    setButtonForTime();
                } else {
                    ToastUtil.showShortToast(ChangePhoneActivity.this, result.getMessage());
                }
            }

        });
    }

    //设置获取验证码按钮Button 倒计时
    private void setButtonForTime() {
        TimeCountUtil time = new TimeCountUtil(this, 60000, 1000, btCode);
        time.start();
    }
         /**
          * 获取图片验证码
         */
     private void getImgCode(){
         HttpClient.get(this, Api.GET_CODE_IMG, new CallBack<ImgCodeBean>() {
             @Override
             public void onSuccess(ImgCodeBean result) {
                 if(result==null){
                     return;
                 }
                 if (result.isSuccess()){
                     uuid=result.getData().getUuid();
                     String base64Data=result.getData().getImage();
                     //将Base64转成Bitmap
                     byte[] bytes= Base64.decode(base64Data, Base64.DEFAULT);
                     Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                     ivImcode.setImageBitmap(bitmap);
                 }

             }

         });
      }
    /**
     * 从字符串中截取连续6位数字组合 ([0-9]{6})截取六位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
     *
     * @param content 短信内容
     * @return 截取得到的6位动态密码
     */
    public String getDynamicPwd(String content) {
        // 此正则表达式验证六位数字的短信验证码
        Pattern pattern = Pattern.compile("(?<![0-9])([0-9]{6})(?![0-9])");
        Matcher matcher = pattern.matcher(content);
        String dynamicPwd = "";
        while (matcher.find()) {
            dynamicPwd = matcher.group();
            Logger.d("getDynamicPwd: find pwd=" + dynamicPwd);
        }
        return dynamicPwd;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_Imcode:
                if (hasFocus) {
                    checkPhone(etPhone.getText().toString().trim());
                }
                break;
            case R.id.et_code:
                if (hasFocus) {
                   if(TextUtils.isEmpty(etImcode.getText().toString().trim())){
                       ToastUtil.showShortToast(ChangePhoneActivity.this,"请输入图片验证码");
                   }
                }
                break;
        }
    }


}
