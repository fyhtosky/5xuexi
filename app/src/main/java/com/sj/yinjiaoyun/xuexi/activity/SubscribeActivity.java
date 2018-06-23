package com.sj.yinjiaoyun.xuexi.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/15.
 * 微专业预约页面
 */
public class SubscribeActivity extends MyBaseActivity implements HttpDemo.HttpCallBack {

    private final String TAG = "subscribe";
    HttpDemo demo;
    List<Pairs> pairsList;

    String traningId;//微专业id
    String mobile;
    String userRemark;
    String fullName;
    String time;
    String qqNum;
    String email;

    ImageView ivBack;
    EditText etMobile;
    TextView tvTime;
    EditText etUserRemark;
    EditText etFullName;
    EditText etQqNum;
    EditText etEmail;
    TextView tvTiJiao;

    Intent intent;
    String collegeId;//院校id
    String endUserId;//用户id

    Boolean qqIsLegal = false;//qq是否合法
    Boolean emailIsLegal = false;//邮箱是否合法
    Boolean phoneIsLegal = false;//电话号是否合法
    Boolean userMarkIsLegal = false;//留言是否合法
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_subscribe);
        init();
    }


    private void init() {
        intent = getIntent();
        demo=new HttpDemo(this);
        endUserId = intent.getStringExtra("endUserId");
        collegeId = intent.getStringExtra("collegeId");
        traningId=intent.getStringExtra("traningId");
        Log.i(TAG, "init: " + endUserId + ":" + collegeId+":"+traningId);
        demo = new HttpDemo(SubscribeActivity.this);
        ivBack = (ImageView) findViewById(R.id.subscribe_back);
        etMobile = (EditText) findViewById(R.id.subscribe_phone);
        tvTime = (TextView) findViewById(R.id.subscribe_time);
        etFullName = (EditText) findViewById(R.id.subscribe_name);
        etQqNum = (EditText) findViewById(R.id.subscribe_qq);
        etEmail = (EditText) findViewById(R.id.subscribe_email);
        etUserRemark = (EditText) findViewById(R.id.subscribe_userRemark);
        tvTiJiao = (TextView) findViewById(R.id.subscribe_tijiao);
        etQqNum.setOnFocusChangeListener(focusListener);
        etEmail.setOnFocusChangeListener(focusListener);
        etMobile.setOnFocusChangeListener(focusListener);
        etUserRemark.setOnFocusChangeListener(focusListener);
    }

    View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.subscribe_qq:
                    if (!hasFocus) {
                        qqIsLegal = checkQQ1(etQqNum.getText().toString());
                        if (!qqIsLegal) {
                            Toast.makeText(SubscribeActivity.this, "请输入合法QQ号码", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case R.id.subscribe_email:
                    if (!hasFocus) {
                        emailIsLegal = checkEmail(etEmail.getText().toString());
                        if (!emailIsLegal) {
                            Toast.makeText(SubscribeActivity.this, "请输入合法邮箱地址", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case R.id.subscribe_phone:
                    if (!hasFocus) {
                        phoneIsLegal = checkPhone(etMobile.getText().toString());
                        if (!phoneIsLegal) {
                            Toast.makeText(SubscribeActivity.this, "请输入合法手机号", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case R.id.subscribe_userRemark:
                    if (!hasFocus) {
                        userMarkIsLegal = checkuserRemark(etUserRemark.getText().toString());
                        if (!phoneIsLegal) {
                            Toast.makeText(SubscribeActivity.this, "请输入留言（不超过300字）", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
            }
        }
    };


    public void onclick(View view) {
        Log.i(TAG, "onclick: ");
        date1 = TimeUtil.getCurrentFormatTime("yyyy年MM月dd日");
        mobile = etMobile.getText().toString();
        userRemark = etUserRemark.getText().toString();
        fullName = etFullName.getText().toString();
        time = tvTime.getText().toString();
        qqNum = etQqNum.getText().toString();
        email = etEmail.getText().toString();
        switch (view.getId()) {
            case R.id.subscribe_back://返回
                finish();
                break;
            case R.id.subscribe_tijiao://提交
                Log.i(TAG, "onclick: " + "提交");
                getHttpProductOrderEnter(mobile, userRemark);
                break;
            case R.id.subscribe_time://预约时间 弹出日历
                subscribeTime();
                break;
        }
    }

    /**
     * 校验qq
     *
     * @param qqNum
     * @return
     */
    private Boolean checkQQ1(String qqNum) {
        Boolean isCounted;
        if (qqNum.length() == 0) {
            return true;//不输入内容时候不提示
        }
        char[] chArr;
        boolean flag = true;
        if (qqNum.length() >= 6 && qqNum.length() <= 10) {
            chArr = qqNum.toCharArray();
            for (char ch : chArr) {
                //如果发现非法字符，则将flag置为flase，退出循环
                //if(!(ch>=0&&ch<=9)){//!!!Error,这句话是错误的！！！
                if (!(ch >= '0' && ch <= '9')) {//疑问：用不用加括号呢？!运算符的优先级如何？？加括号会不会影响效率呢？？
                    flag = false;
                    break;
                }
            }
            if (flag) {
                isCounted = !qqNum.startsWith("0");
            } else {
                isCounted = false;
            }
        } else {
            isCounted = false;
        }
        return isCounted;
    }

    /**
     * 校验邮箱地址
     *
     * @param strEmail
     * @return
     */
    private Boolean checkEmail(String strEmail) {
        if (TextUtils.isEmpty(strEmail)) {
            return true;
        }
        String strPattern = "^([a-zA-Z0-9_ \\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    Calendar calendar;
    Dialog dialog;
    String date1;//当前日期
    String date2;//用户选择的日期

    //预约时间
    private void subscribeTime() {
        Log.i(TAG, "selectedTime: date1 " + date1);
        calendar = Calendar.getInstance();
        if (dialog == null) {
            dialog = new DatePickerDialog(SubscribeActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            date2 = year + "年" + (monthOfYear + 1) + "月"
                                    + dayOfMonth + "日";
                            if (TimeUtil.DateCompare(date1, date2) != -1) {
                                tvTime.setText(date2);
                            } else {
                                Toast.makeText(SubscribeActivity.this, "请选择大于当前日期日", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, calendar.get(Calendar.YEAR), calendar
                    .get(Calendar.MONTH), calendar
                    .get(Calendar.DAY_OF_MONTH));
            dialog.show();
        } else {
            dialog.show();
        }
    }


    /**
     * 校验留言
     *
     * @param userRemark
     * @return
     */
    private Boolean checkuserRemark(String userRemark) {
        return !TextUtils.isEmpty(userRemark) && userRemark.length() < 300;
    }

    /**
     * 校验电话
     *
     * @param mobile
     * @return
     */
    private Boolean checkPhone(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }
        String strPattern = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }


    //提交预约信息
    private void getHttpProductOrderEnter(String mobile, String userRemark) {
        try {
        if (!checkPhone(mobile)) {
            Toast.makeText(SubscribeActivity.this, "请输入合法手机号", Toast.LENGTH_LONG).show();
            return;
        }
        if (!checkuserRemark(userRemark)) {
            Toast.makeText(SubscribeActivity.this, "请输入留言（不超过300字）", Toast.LENGTH_LONG).show();
            return;
        }
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("product/orderEnter");
//            String url = "http://192.168.100.175:8083/api/v2/product/orderEnter.action";
        pairsList.add(new Pairs("endUserId", endUserId));
        pairsList.add(new Pairs("collegeId", collegeId));
        pairsList.add(new Pairs("enterType", "2"));//0：专业，1：公开课，2：微专业（默认是专业）
        pairsList.add(new Pairs("productId", traningId));//专业id（enterType是0时对应的是专业id，1时是公开课id；2时是微专业id）
        pairsList.add(new Pairs("mobile", mobile));//手机（*手机，留言，qq有一项填写即可）
        pairsList.add(new Pairs("userRemark", userRemark));//留言
        if (!TextUtils.isEmpty(fullName)) {
            pairsList.add(new Pairs("fullName", fullName));//姓名
        }
        if (!TextUtils.isEmpty(time)) {
                pairsList.add(new Pairs("enterTime", String.valueOf(sdf.parse(time).getTime())));//姓名
        }
        if (!TextUtils.isEmpty(qqNum)) {
            if (checkQQ1(qqNum)) {
                pairsList.add(new Pairs("qqNum", qqNum));//QQ号
            } else {
                Toast.makeText(SubscribeActivity.this, "请输入合法QQ号码", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (!TextUtils.isEmpty(email)) {
            if (checkEmail(email)) {
                pairsList.add(new Pairs("email", email));//邮箱
            } else {
                Toast.makeText(SubscribeActivity.this, "请输入合法邮箱地址", Toast.LENGTH_LONG).show();
                return;
            }
        }

        demo.doHttpPostLoading(this,url, pairsList,1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        Logger.d("回来的数据："+msg);
        try {
            JSONObject obj=new JSONObject(msg);
            if(obj.getBoolean("success")){
                Toast.makeText(this,"预约成功",Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
