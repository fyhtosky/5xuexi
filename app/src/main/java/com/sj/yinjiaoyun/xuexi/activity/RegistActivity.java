package com.sj.yinjiaoyun.xuexi.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.sj.yinjiaoyun.xuexi.db.DbOperatorLogin;
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
 * 注册页面（普通登录前注册）
 */
public class RegistActivity extends Activity implements HttpDemo.HttpCallBack{

    String TAG="regist";
    EditText etPhone;//电话
    EditText etName;//名字
    EditText etPassword;//密码
    EditText etVerification;//验证码
    Button btnZhuce;//注册按钮
    Button btnYanZhen;//获取验证码
    TitleBarView titleBarView;

    Boolean isVerification = false;  //判断密码明暗框的改变,false表示默认为暗码
    String phone;//电话编辑框的信息
    String name;//名字编辑框的信息
    String pwd;//密码编辑框的信息
    HttpDemo demo;//网络请求操作类

    String verification;//编辑框里面用户输入的验证码
    List<Pairs> pairsList;
    boolean phoneIsA=false;//默认电话号码不可用
    boolean nameIsA=false;//默认名字不可用
    boolean pwdIsA=false;//默认密码不可用
    boolean verificationIsA=false;//默认验证码不可用


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
        setContentView(R.layout.layout_register);
        //注册EventBus
        EventBus.getDefault().register(this);


        demo=new HttpDemo(this);
        init();
    }

    //初始化控件
    private void init() {
        btnYanZhen= (Button) findViewById(R.id.register_huoqu);
        btnZhuce= (Button) findViewById(R.id.regist_zhuce);
        etPhone = (EditText) findViewById(R.id.regist_dianhua);
        etName = (EditText) findViewById(R.id.regist_yonghuming);
        etPassword = (EditText) findViewById(R.id.regist_mima);
        etImgcode= (EditText) findViewById(R.id.et_imgcode);
        ivImgcode= (ImageView) findViewById(R.id.iv_Imgcode);
        etVerification = (EditText) findViewById(R.id.regist_yanzhengma);
        titleBarView= (TitleBarView) findViewById(R.id.register_titleBarView);
        titleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etPhone.setOnFocusChangeListener(focusChangeListener);
        etName.setOnFocusChangeListener(focusChangeListener);
        etPassword.setOnFocusChangeListener(focusChangeListener);
        etVerification.setOnFocusChangeListener(focusChangeListener);
        etImgcode.setOnFocusChangeListener(focusChangeListener);
        //获取图片的验证码
        getImgCode();
        observer = new SmsContentObserver(RegistActivity.this, handler);
        Uri uri = Uri.parse("content://sms");
        //第二个参数： 是否监听对应服务所有URI监听  例如sms 所有uri
        getContentResolver().registerContentObserver(uri, true, observer);
    }
    View.OnFocusChangeListener focusChangeListener=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch(v.getId()){
                case R.id.regist_dianhua://电话
                    if(!hasFocus){
                        Log.i(TAG, "onFocusChange: "+"电话");
                        checkPhone(etPhone.getText().toString());
                    }
                    break;
                case R.id.regist_yonghuming://用户名
                    if(!hasFocus){
                        Log.i(TAG, "onFocusChange: "+"用户名");
                        checkName(etName.getText().toString());
                    }
                    break;
                case R.id.regist_mima://密码
                    if(!hasFocus){
                        Log.i(TAG, "onFocusChange: "+"密码"+etPassword.getText().toString());
                        checkPwd(etPassword.getText().toString());
                    }
                    break;
                case R.id.regist_yanzhengma://验证码
                    if(!hasFocus){
                        Log.i(TAG, "onFocusChange: "+"验证码");
                    }
                    break;
                case R.id.et_imgcode:
                    if(!hasFocus){
                        if(TextUtils.isEmpty(etImgcode.getText().toString().trim())){
                            ToastUtil.showShortToast(RegistActivity.this,"请输入图片验证码");
                        }
                    }
                    break;
            }
        }
    };


    public void onclick(View v) {
        phone = etPhone.getText().toString();
        name = etName.getText().toString();
        pwd = etPassword.getText().toString();
        verification=etVerification.getText().toString();
        switch (v.getId()) {
            case R.id.regist_mingma://明暗码状态改变
                setPassword();//设置输入框密码明码暗码显示
                break;
            case R.id.register_huoqu://获取验证码
//                getHttpVerification();//获取网络请求验证码
                getCode();
                break;
            case R.id.regist_zhuce://注册
                userResgister();
                break;
            case R.id.regist_argeement://注册协议
                startActivity(new Intent(this, ArgeementActivity.class));
                break;
            case R.id.iv_refresh:
                etImgcode.setText("");
                etImgcode.setFocusable(true);
                getImgCode();
                break;
        }
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
     * 用户注册
     */
    private void userResgister(){
        Log.i(TAG, "进入userResgister: ");
        /////////////////校验电话号码///////////////
        Log.i(TAG, "进入checkPhone: ");
         if(!checkPhone(phone)){
             return;
         }
         if(!phoneIsA){
            return;
         }
        /////////////////校验用户名///////////////
       if(!checkName(name)){
           return;
        }
        if(!nameIsA){
            return;
        }
        /////////////////校验密码///////////////
        Log.i(TAG, "进入checkPwd: "+pwd);
        if(!checkPwd(pwd)){
            return;
        }
        ////////验证码////////////////////////////////////////////////////////////////////////////////
        if(verification.equals("")){
            Toast.makeText(RegistActivity.this,"验证码为空",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, "注册校验:验证码 ");
            //程序到这里表示phone name pwd verification都可用
            Log.i(TAG, "开始网络注册:用户名 "+name+" 密码:"+pwd+" 验证码:"+":"+verification+" 电话:"+phone);
            pairsList=new ArrayList<>();
            String url= MyConfig.getURl("user/register.action");
            pairsList.add(new Pairs("userName",name));
            pairsList.add(new Pairs("password",pwd));
            pairsList.add(new Pairs("code",verification));
            pairsList.add(new Pairs("phone",phone));
            demo.doHttpPost(url,pairsList,4);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
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
    //校验Editview编辑框的用户名是否合格
    private  Boolean checkName(String name){
        Log.i(TAG, "进入checkName: ");
        int length=name.length();
        if(name.equals("")){
            Toast.makeText(RegistActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(length<4){
            Toast.makeText(RegistActivity.this,"用户名不能小于4个字符",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(length>20){
            Toast.makeText(RegistActivity.this,"用户名最大支持20个字符",Toast.LENGTH_SHORT).show();
            return false;
        }
        String a="^(?!_)(?![0-9])[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";//中文、字母、数字、下划线的组合,不能以数字开头，下划线开头
        if(!name.matches(a)){
            Toast.makeText(RegistActivity.this,"用户名首字符必须为中文或字母,支持中文、字母、数字、下划线的组合",Toast.LENGTH_LONG).show();
            return false;
        }
        postHttpMsg(name, MyConfig.CODE_POST_NAME);//post请求码(3表示用户名校验)
        Log.i(TAG, "checkName: "+"请求网络");
        return true;
    }
    //校验Editview编辑框密码是否合格
    private Boolean checkPwd(String pwd){
        Log.i(TAG, "进入checkPwd: "+pwd);
        pwdIsA=false;
        int length=pwd.length();
        if(pwd.equals("")){
            Toast.makeText(RegistActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(length<6){
            Toast.makeText(RegistActivity.this,"密码不能小于6位",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(length>20){
            Toast.makeText(RegistActivity.this,"密码最大支持20个字符",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd.indexOf(" ")>0){
            Toast.makeText(RegistActivity.this,"密码不支持空格",Toast.LENGTH_SHORT).show();
            return false;
        }
        String regex ="^((?=.*?\\d)(?=.*?[A-Za-z])|(?=.*?\\d)(?=.*?[符号])|(?=.*?[A-Za-z])(?=.*?[符号]))[\\dA-Za-z符号]+$";
        if(!pwd.matches(regex)){
            Toast.makeText(RegistActivity.this,"密码必须含有字母、数字和符号两种及以上的组合",Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.i(TAG, "checkPwd: "+"密码可用");
        pwdIsA=true;
        return true;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////

    //设置获取验证码按钮Button 倒计时
    private void setButtonForTime(){
        TimeCountUtil time=new TimeCountUtil(this,60000,1000,btnYanZhen);
        time.start();
    }

    /***
     * 获取手机验证码
     */
    private void getCode() {
        if(!checkPhone(phone)){
            return;
        }
        Log.i(TAG, "getHttpVerification:phoneIsA "+phoneIsA);
        if(!phoneIsA){
            return;
        }
        if(!checkName(name)){
            return;
        }
        Log.i(TAG, "getHttpVerification: nameIsA "+nameIsA);
        if(!nameIsA){
            return;
        }
        Log.i(TAG, "getHttpVerification:pwd "+pwd);
        if(TextUtils.isEmpty(etImgcode.getText().toString().trim())){
            ToastUtil.showShortToast(RegistActivity.this,"请输入图片验证码");
            return;
        }
        if(!checkPwd(pwd)){
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("phone", etPhone.getText().toString().trim());
        map.put("validateCode",etImgcode.getText().toString().trim());
        map.put("validateKey",uuid);
        map.put("smsFlag",String.valueOf(0));
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
                    ToastUtil.showShortToast(RegistActivity.this, result.getMessage());
                }
            }

        });
    }
    /**
     * 获取网络请求 验证码校验 返回验证码
     */
    private  void getHttpVerification(){
        if(!checkPhone(phone)){
            return;
        }
        Log.i(TAG, "getHttpVerification:phoneIsA "+phoneIsA);
        if(!phoneIsA){
            return;
        }
        if(!checkName(name)){
            return;
        }
        Log.i(TAG, "getHttpVerification: nameIsA "+nameIsA);
        if(!nameIsA){
            return;
        }
        Log.i(TAG, "getHttpVerification:pwd "+pwd);
        if(!checkPwd(pwd)){
            return;
        }
        //程序到这里 表示 电话 用户名 密码 都可用
        Log.i(TAG, "getHttpVerification: "+"网络获取验证码");
        pairsList=new ArrayList<>();
        String  url= MyConfig.getURl("identifycode/getIdentifycodeByPhone");
        pairsList.add(new Pairs("phone",phone));
        demo.doHttpGet(url,pairsList,1);
        btnYanZhen.setFocusable(true);
        setButtonForTime();//设置获取验证码按钮Button 倒计时
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
        pairsList=new ArrayList<>();
        String url= MyConfig.getURl("user/checkInfo");//验证接口
        pairsList.add(new Pairs("param",msg));
        demo.doHttpPost(url,pairsList,reqquestCode);
    }

    @Override
    public void setMsg(String msg,int requestCode) {
        switch (requestCode){
            case  1://get请求码（验证码校验）
                parseVerification(msg);
                break;
            case  2://post请求码(2表示电话号码校验)
                phoneIsA=parserMessage(msg,2);
                Log.i(TAG, "post请求码(电话号码校验)"+phoneIsA);
                break;
            case  3://post请求码(3表示用户名校验)
                nameIsA=parserMessage(msg,3);
                Log.i(TAG, "setMsg: "+nameIsA);
                break;
            case 4://post请求码(4表示用户注册)
                parserRegister(msg);
                break;
        }
    }

    //解析注册信息
    private void parserRegister(String msg){
        Log.i(TAG, "parserRegister: "+"回调注册"+msg);
        try {
            JSONObject object=new JSONObject(msg);
            if(object.getBoolean("success")){
                Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistActivity.this,LoginActivity.class));
                RegistActivity.this.finish();
            }else{
                Toast.makeText(RegistActivity.this,object.getString("message"),Toast.LENGTH_LONG).show();
                Log.i(TAG, "注册失败:"+object.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析电话号码 用户名是否可用
     * @param msg  json字符串样式   {"state":200,"success":true,"message":"操作成功","data":{}}
     */
    private boolean parserMessage(String msg,int requestCode){
        boolean isAvailable=false;
        if(requestCode==2){//解析数据 查看电话号码是否可用
            try {
                JSONObject object=new JSONObject(msg);
                isAvailable=object.getBoolean("success");
                if(!isAvailable){
                    Toast.makeText(RegistActivity.this,"该手机号已注册",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(requestCode==3){//解析数据 用户名是否可用
            try {
                JSONObject object=new JSONObject(msg);
                isAvailable=object.getBoolean("success");
                if(!isAvailable){
                    Toast.makeText(RegistActivity.this,"该用户名已注册",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isAvailable;
    }

    /**
     * //解析验证码
     * @param msg json字符串样式{"state":200,"success":true,"message":"【短信已发送!】","data":{"identifycode":{"identifyCode":"407063"}}}
     */
    private void parseVerification(String msg){
        try {
            JSONObject object=new JSONObject(msg);
            String message_= (String) object.get("message");
            Log.i(TAG, "进入parseVerification: -----------"+msg);
            if(object.getBoolean("success")){
                Log.i(TAG, "parseVerification: "+"验证码成功");
                verificationIsA=true;
            }else{
                Log.i(TAG, "parseVerification: "+cutString(message_));
                verificationIsA=false;
                Toast.makeText(RegistActivity.this,cutString(message_),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
     *  设置输入框密码明码暗码显示
     */
    private void setPassword(){
        if (isVerification) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isVerification = false;
        } else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isVerification = true;
        }
        etPassword.setSelection(pwd.length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(observer);
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }
}
