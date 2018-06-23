package com.sj.yinjiaoyun.xuexi.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sj.yinjiaoyun.xuexi.Event.SmsEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.ImgCodeBean;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.receiver.SmsContentObserver;
import com.sj.yinjiaoyun.xuexi.utils.TimeCountUtil;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2016/8/19.
 * 找回密码页面
 */
public class FindActivity extends Activity implements HttpDemo.HttpCallBack{
    String TAG="finda";
    Boolean isVerification = false;  //判断密码明暗框的改变,默认为暗码
    EditText etPassword;
    EditText etPhone;
    EditText etVerification;//验证码
    Button verficationBtn;
    String pwd;
    String phone;
    String yanZheng;//编辑框的验证码
    Boolean phoneIsA=false;
    HttpDemo demo;
    TitleBarView titleBarView;
    private EditText etImgcode;
    private ImageView ivImgcode;
    //获取短信的内容
    private SmsContentObserver observer;
    //提取图片的验证key
    private String uuid;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_findpwd);
        //注册EventBus
        EventBus.getDefault().register(this);
        demo=new HttpDemo(this);
        init();
    }

    //初始化控件
    private void init() {
        titleBarView= (TitleBarView) findViewById(R.id.find_titleBarView);
        titleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        verficationBtn= (Button) findViewById(R.id.find_pwd_huoqu);
        etPhone = (EditText) findViewById(R.id.find_phone);
        etImgcode= (EditText) findViewById(R.id.et_imgcode);
        ivImgcode= (ImageView) findViewById(R.id.iv_Imgcode);
        etPassword = (EditText) findViewById(R.id.find_password);
        etVerification = (EditText) findViewById(R.id.find_yanzhengma);

        etImgcode.setOnFocusChangeListener(focusChangeListener);
        etPassword.setOnFocusChangeListener(focusChangeListener);
        etVerification.setOnFocusChangeListener(focusChangeListener);
        //获取图片的验证码
        getImgCode();
        observer = new SmsContentObserver(FindActivity.this, handler);
        Uri uri = Uri.parse("content://sms");
        //第二个参数： 是否监听对应服务所有URI监听  例如sms 所有uri
        getContentResolver().registerContentObserver(uri, true, observer);
    }
    View.OnFocusChangeListener focusChangeListener=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch(v.getId()){
                case R.id.et_imgcode://图片验证码
                    if(hasFocus){
                        Log.i(TAG, "onFocusChange: "+"电话");
                        checkPhone(etPhone.getText().toString());
                    }
                    break;
                case R.id.find_password://密码
                    if(hasFocus){
                        if(TextUtils.isEmpty(etImgcode.getText().toString().trim())){
                            ToastUtil.showShortToast(FindActivity.this,"请输入手机验证码");
                        }
//                       checkPassword(etPassword.getText().toString());
                    }
                    break;
                case R.id.find_yanzhengma://手机验证码
                    if(hasFocus){
                        if(TextUtils.isEmpty(etImgcode.getText().toString().trim())){
                            ToastUtil.showShortToast(FindActivity.this,"请输入图片验证码");
                        }
                        Log.i(TAG, "onFocusChange: "+"验证码");
                    }
                    break;
            }
        }
    };
    //////////////////////////////////////////////////////////////////////////////////////////
    //校验Editview编辑框的手机号码是否合格
    private boolean checkPhone(String phone){
        Log.i(TAG, "进入checkPhone: ");
        if(phone.length()==11){
            Log.i(TAG, "checkPhone: "+true);
            postHttpMsg(phone,2);//post请求码(2表示电话号码校验)
            return true;
        }else{
            Toast.makeText(this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkPhone: "+false);
            return false;
        }
    }
    // 校验Editview编辑框的 密码 是否合格
    private boolean checkPassword(String pwd){
        Log.i(TAG, "进入checkPwd: "+pwd);
        int length=pwd.length();
        String regex ="^((?=.*?\\d)(?=.*?[A-Za-z])|(?=.*?\\d)(?=.*?[符号])|(?=.*?[A-Za-z])(?=.*?[符号]))[\\dA-Za-z符号]+$";//匹配密码
        if(pwd.equals("")){
            Toast.makeText(FindActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkPwd: "+false +"请输入密码");
            return false;
        }else if(length<6){
            Toast.makeText(FindActivity.this,"密码不能小于6位",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkPwd: "+false +"密码<6");
            return false;
        }else if(length>20){
            Toast.makeText(FindActivity.this,"密码最大支持20个字符",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkPwd: "+false +"密码>20");
            return false;
        }else if(pwd.indexOf(" ")>0){
            Toast.makeText(FindActivity.this,"密码不支持空格",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkPwd: "+false +"密码有空格");
            return false;
        }else if(!pwd.matches(regex)){//正则匹配
            Toast.makeText(FindActivity.this,"密码必须含有字母、数字和符号两种及以上的组合",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "checkPwd: "+false +"正则匹配失败");
            return false;
        }else{
            Log.i(TAG, "checkPwd: "+true);
            return true;
        }
    }
    // 校验Editview编辑框的 验证码 是否合格
    private boolean checkVerification(String yanZheng){
        Log.i(TAG, "进入checkVerification: "+yanZheng);
        if(yanZheng.equals("")){
            Log.i(TAG, "checkVerification: "+false);
            Toast.makeText(FindActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            Log.i(TAG, "checkVerification: "+true);
            return true;
        }
    }

    /**
     * 监听短信的验证码拦截
     *
     * @param smsEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SmsEvent smsEvent) {
        if (smsEvent.getSmsContent() != null) {
            //设置Edittext获取焦点
            etPhone.setFocusable(false);
            etVerification.setFocusable(true);
            etVerification.setText(smsEvent.getSmsContent());
            etVerification.setSelection(smsEvent.getSmsContent().length());

        }

    }
    /**
     *  //获取网络请求 电话号码校验 是否一杯占用
     * @param msg 需要校验的电话号码 用户名
     */
    private void postHttpMsg(String msg,int reqquestCode){
        Log.i("result", "验证信息postHttpMsg: "+msg);
        if(msg.equals("")){
            return;
        }
        List<Pairs> pairsList=new ArrayList<>();
        String url= MyConfig.getURl("user/checkInfo");//验证接口
        pairsList.add(new Pairs("param",msg));
        demo.doHttpPost(url,pairsList,reqquestCode);
    }



    public void onclick(View v) {
        phone = etPhone.getText().toString();
        pwd = etPassword.getText().toString();
        yanZheng=etVerification.getText().toString();
        switch (v.getId()) {
            case R.id.find_chongzhi://密码重置f
                chongZhiPwd();
                break;
            case R.id.find_xianshi://密码明暗显示
                setPassword();
                break;
            case R.id.find_pwd_huoqu://获取验证码
                getCode();
//                getHttpVerification();
                break;
            case R.id.iv_refresh:
                etImgcode.setText("");
                etImgcode.setFocusable(true);
                getImgCode();
                break;
        }
    }

    /**
     * 获取网络请求 验证码校验 返回验证码
     */
    private void getHttpVerification(){
        Log.i(TAG, "getHttpVerification: ");
        if(!checkPhone(phone)){//
            Log.i(TAG, "getHttpVerification: "+"电话号码不可用,直接返回");
            return;
        }
        if(!phoneIsA){
            return;
        }
        //程序到这里 表示 电话都可用
        List<Pairs> pairsList=new ArrayList<>();
        String  url= MyConfig.getURl("identifycode/getIdentifycodeByPhone");
        pairsList.add(new Pairs("phone",phone));
        demo.doHttpGet(url,pairsList, MyConfig.CODE_GET_VERIFICATION);
        etVerification.setFocusable(true);//给密验证码设置焦点
        setCheckBoxIsTrue();//设置获取验证码按钮Button 倒计时
    }
    //设置获取验证码按钮Button 倒计时
    private void setCheckBoxIsTrue(){
        TimeCountUtil time=new TimeCountUtil(this,60000,1000,verficationBtn);
        time.start();
    }
    /***
     * 获取手机验证码
     */
    private void getCode() {
        if(!checkPhone(phone)){//
            Log.i(TAG, "getHttpVerification: "+"电话号码不可用,直接返回");
            return;
        }
        if(!phoneIsA){
            return;
        }
        if(TextUtils.isEmpty(etImgcode.getText().toString().trim())){
            ToastUtil.showShortToast(FindActivity.this,"请输入图片验证码");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", etPhone.getText().toString().trim());
        map.put("validateCode",etImgcode.getText().toString().trim());
        map.put("validateKey",uuid);
        map.put("smsFlag",String.valueOf(1));
        map.put("smsFrom",String.valueOf(0));
        HttpClient.postImgCode(this, Api.GET_PHONE_CODE, map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                    setCheckBoxIsTrue();
                } else {
                    ToastUtil.showShortToast(FindActivity.this, result.getMessage());
                }
            }

        });
    }

    //重置密码网络请求
    private void chongZhiPwd(){
        phone = etPhone.getText().toString();
        pwd = etPassword.getText().toString();
        yanZheng=etVerification.getText().toString();
        Log.i(TAG, "进入chongZhiPwd: ");
        if(!checkPhone(phone)){//
            Log.i(TAG, "chongZhiPwd: "+"电话号码不可用");
            return;
        }
        if(!checkPassword(pwd)){
            Log.i(TAG, "chongZhiPwd: "+"密码不可用");
            return;
        }
        if(!checkVerification(yanZheng)){
            Log.i(TAG, "chongZhiPwd: "+"验证码不可用");
            return;
        }
        String url= MyConfig.getURl("password/forgetPassword");//忘记密码接口
//        String url="http://139.196.255.175:8083/api/v2/password/forgetPassword";
        List<Pairs> list=new ArrayList<>();
        list.add(new Pairs("phone",phone));
        list.add(new Pairs("code",yanZheng));
        list.add(new Pairs("password",pwd));
        demo.doHttpPost(url,list, MyConfig.CODE_POST_FIND);
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        try {
            switch (requestCode) {
                case 1://验证码请求
                    parseVerification(msg);//解析验证码
                    break;
                case 2://电话号码请求
                    phoneIsA = parserMessage(msg, 2);
                    Log.i(TAG, "post请求码(电话号码校验)phoneIsA" + phoneIsA);
                    break;
                case 6://找回密码请求
                    parseFind(msg);
                    break;
            }
        }catch (Exception e){
            Log.e(TAG, "setMsg: "+e.toString() );
        }
    }

    private Boolean parserMessage(String msg, int i) {
        Boolean isRegist=false;
        try {
            JSONObject object=new JSONObject(msg);
            isRegist=!(object.getBoolean("success"));
            Log.i(TAG, "parserMessage: 该手机号是否注册"+isRegist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(isRegist){//表示手机号被注册
            return true;
        }else{
            Toast.makeText(FindActivity.this,"请输入一个已注册的账号",Toast.LENGTH_SHORT).show();
            etPhone.setSelection(0);
            return false;
        }
    }

    /**
     * 解析  重置密码网络请求 的信息
     */
      private void parseFind(String msg) {
          Log.i(TAG, "进入parseFind:解析 "+msg);
          try {
              JSONObject object=new JSONObject(msg);
              if(object.getBoolean("success")){
                  Toast.makeText(FindActivity.this,"密码已重置",Toast.LENGTH_SHORT).show();
                  Log.i(TAG, "parseFind: "+"重置成功");
                  FindActivity.this.finish();
              }else{
                  Log.i(TAG, "parseFind: "+"重置失败");
                  Toast.makeText(FindActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
              }
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }


    /**
     * //解析 验证码 网络请求 信息
     * @param msg json字符串样式{"state":200,"success":true,"message":"【短信已发送!】","data":{"identifycode":{"identifyCode":"407063"}}}
     */
    private void parseVerification(String msg) {
        Log.i(TAG, "进入parseVerification: "+msg);
        try {
            JSONObject object=new JSONObject(msg);
            String message_= (String) object.get("message");
            Toast.makeText(FindActivity.this,cutString(message_),Toast.LENGTH_SHORT).show();
            if(object.getBoolean("success")){
                Log.i(TAG, "parseVerification: "+"验证码成功");
            }else{
                Log.i(TAG, "parseVerification: "+cutString(message_));
                Toast.makeText(FindActivity.this,cutString(message_),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     *  设置输入框密码 明码 暗码 显示
     */
    private void setPassword(){
        pwd=etPassword.getText().toString();
        if (isVerification) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isVerification = false;
        } else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isVerification = true;
        }
        etPassword.setSelection(pwd.length());
    }


    /**
     * 截取字符串  截取【短信已发送!】中间的部分
     * @param message
     * @return
     */
    private String  cutString(String message){
        int start=message.indexOf("【");
        int end=message.indexOf("】");
        return message.substring(start+1,end);
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
                    ivImgcode.setImageBitmap(bitmap);
                }

            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(observer);
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }
}