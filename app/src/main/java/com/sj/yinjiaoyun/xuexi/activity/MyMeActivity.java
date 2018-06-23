package com.sj.yinjiaoyun.xuexi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.callback.WheelViewCallBack;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.utils.WheelPopWindows;
import com.sj.yinjiaoyun.xuexi.view.MeView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/23.
 * 主页- 我fragment- 个人资料 页面
 */
public class MyMeActivity extends MyBaseActivity implements View.OnClickListener {
    String TAG = "meActivity";
    @BindView(R.id.me_back)
    ImageView meBack;
    @BindView(R.id.myme_touxinag)
    TextView mymeTouxinag;
    @BindView(R.id.me_icon)
    ImageView meIcon;
    @BindView(R.id.me_container_icon)
    RelativeLayout meContainerIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.me_realName)
    RelativeLayout meRealName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.me_sex)
    RelativeLayout meSex;
    @BindView(R.id.tv_idCard)
    TextView tvIdCard;
    @BindView(R.id.me_idCard)
    RelativeLayout meIdCard;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.me_userName)
    RelativeLayout meUserName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.me_phone)
    RelativeLayout mePhone;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.me_email)
    RelativeLayout meEmail;
    @BindView(R.id.me_nation)
    MeView meNation;
    @BindView(R.id.me_politicsStatus)
    MeView mePoliticsStatus;
    @BindView(R.id.me_fixPhone)
    MeView meFixPhone;
    @BindView(R.id.me_postalCode)
    MeView mePostalCode;
    @BindView(R.id.me_address)
    MeView meAddress;
    @BindView(R.id.me_head_jiaoyubeijing)
    TextView meHeadJiaoyubeijing;
    @BindView(R.id.me_middleSchool)
    MeView meMiddleSchool;
    @BindView(R.id.me_collegeSpecializ)
    MeView meCollegeSpecializ;
    @BindView(R.id.me_collegeSchool)
    MeView meCollegeSchool;
    @BindView(R.id.me_bachelor)
    MeView meBachelor;
    @BindView(R.id.layout_me)
    LinearLayout layoutMe;


    private String senderImg;
    //标示性别的
    private int sex = 0;
    //标示用户选去性别的下标
    private int index = 0;
    private String endUserId;
    //传递性别的标示
    private int id = 1;
    //标示是否有学籍
    private boolean hasStudentNumber=false;
    //对话框
    private WheelPopWindows wheelNation;
    private WheelPopWindows wheelpolitics;
    //省份证
    private String idCard;
    //固定电话
    private String fixPhone;
    //邮编
    private String postCode;
    //名族列表
    private List<String> nationList =
            Arrays.asList("汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族", "布衣族",
                    "朝鲜族", "满族", "侗族", "瑶族", "白族", "土家族", "哈尼族", "哈萨克族", "傣族", "黎族", "傈傈族",
                    "佤族", "畲族", "高山族", "拉枯族", "水族", "东乡族", "纳西族", "景颇族", "柯尔克孜族", "土族",
                    "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族", "毛南族", "仡佬族", "锡伯族", "阿昌族", "普米族",
                    "塔吉克族", "怒族", "乌兹别克族", "俄罗斯族", "鄂温克族", "德昂族", "保安族", "裕固族", "京族", "塔塔尔族",
                    "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族", "基诺族");
    //政治面貌
    private List<String> poliList = Arrays.asList("中共党员", "中共预备党员", "共青团员", "民主党派", "无党派人士", "群众");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_me);
        ButterKnife.bind(this);
        init();


    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.getNetworkSatte(MyMeActivity.this);
        setDateRequest();

    }

    //发送数据请求
    private void setDateRequest() {
        senderImg = PreferencesUtils.getSharePreStr(this, "userImg");
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(R.color.colorWrite)
                .borderWidthDp(0.5f)
                .cornerRadiusDp(2)
                .oval(false)
                .build();
        if (!TextUtils.isEmpty(senderImg)) {
            Picasso.with(this)
                    .load(senderImg.trim())
                    .resize(60, 60)
                    .error(R.mipmap.default_userhead).centerCrop().transform(transformation).into(meIcon);
        } else {
            Picasso.with(this)
                    .load(R.mipmap.default_userhead)
                    .resize(60, 60).into(meIcon);
        }
        String params = "?id=" + endUserId;
        HttpClient.get(this, Api.GET_USER_INFO + params, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
                hasStudentNumber=result.getData().isHasStudentNumber();
                idCard=result.getData().getUser().getIdCard();
                fixPhone=result.getData().getUser().getFixPhone();
                postCode=result.getData().getUser().getPostalCode();
                fillData(result.getData().getUser());
            }


        });

    }

    //初始化控件
    private void init() {
        wheelNation = new WheelPopWindows(this, MyMeActivity.this);
        wheelpolitics=new WheelPopWindows(this,MyMeActivity.this);
        endUserId = PreferencesUtils.getSharePreStr(this, "username");
        Logger.d("MyMeActivity" + senderImg + endUserId);
        //个人资料

        //添加监听器
        meContainerIcon.setOnClickListener(this);
        meSex.setOnClickListener(this);
        meAddress.setOnClickListener(this);
        meNation.setOnClickListener(this);
        mePoliticsStatus.setOnClickListener(this);
        meRealName.setOnClickListener(this);
        meIdCard.setOnClickListener(this);
        meFixPhone.setOnClickListener(this);
        mePostalCode.setOnClickListener(this);
    }


    //填充数据到相应的控件
    private void fillData(ReturnBean.DataBean.UserBean userInfo) {
        Log.i(TAG, "fillData: ");
        if(userInfo.getRealName()!=null){
            PreferencesUtils.putSharePre(getApplicationContext(), "realName", userInfo.getRealName());
        }else {
            PreferencesUtils.putSharePre(getApplicationContext(), "realName", userInfo.getNickName());
        }
        if(userInfo.getUserName()!=null){
            PreferencesUtils.putSharePre(getApplicationContext(), "Name", userInfo.getUserName());
        }else {
            PreferencesUtils.putSharePre(getApplicationContext(), "Name", userInfo.getNickName());
        }

        /**
         * 姓名
         */
        if(userInfo.getRealName()!=null){
            tvName.setText(userInfo.getRealName());
        } else if(userInfo.getNickName()!=null){
            tvName.setText(userInfo.getNickName());
        } else if(userInfo.getUserName()!=null){
            tvName.setText(userInfo.getUserName());
        }else {
            tvName.setText("");
        }
        /**
         * 性别
         */
        if ("1".equals(userInfo.getSex())) {
            tvSex.setText("男");
        } else {
            tvSex.setText("女");
        }
        /**
         *生份证号
         */
        if (userInfo.getIdCard() == null) {//生份证号
            //tvIdCard.setValues("身份证号","");
        } else {
            tvIdCard.setText(userInfo.getIdCard());
        }
        /**
         * 用户名
         */
            if(userInfo.getUserName()!=null){
                 tvUserName.setText(userInfo.getUserName());
             }else {
                 tvUserName.setText("");
             }

        /**
         * 手机号码
         */
        if (userInfo.getPhone() == null) {//电话
            //tvPhone.setValues("手机号","");
        } else {
            tvPhone.setText(userInfo.getPhone());
        }
        /**
         * 邮箱
         */
        if (userInfo.getEmail() == null) {//邮件
            // tvEmail.setValues("邮箱","");
        } else {
            tvEmail.setText(userInfo.getEmail());
        }
        /**
         * 地址
         */
        if (userInfo.getAddress() != null) {//
            meAddress.setValuesForMark(userInfo.getAddress());
        }
        if (userInfo.getNation() == null) {//民族
            meNation.setValuesForMark("");
        } else {
            meNation.setValuesForMark(userInfo.getNation());
        }
        if (userInfo.getPoliticsStatus() == null) {//政治面貌
            mePoliticsStatus.setValuesForMark("");
        } else {
            mePoliticsStatus.setValuesForMark(userInfo.getPoliticsStatus());
        }
        if (userInfo.getFixPhone() == null) {//固话
            meFixPhone.setValuesForMark("");
        } else {
            meFixPhone.setValuesForMark(userInfo.getFixPhone());
        }
        if (userInfo.getPostalCode() == null) {//邮政编码
            mePostalCode.setValuesForMark("");
        } else {
            mePostalCode.setValuesForMark(userInfo.getPostalCode());
        }


        //教育背景
        if (userInfo.getMiddleSchoolCertificate() == null || userInfo.getMiddleSchoolCertificate().equals("")) {//高考
            meMiddleSchool.setVisibility(View.GONE);
        } else {
            meMiddleSchool.setVisibility(View.VISIBLE);
            meMiddleSchool.setValuesForMark(userInfo.getMiddleSchoolCertificate());
        }
        if (userInfo.getCollegeSpecializCertificate() == null || (userInfo.getCollegeSpecializCertificate().equals(""))) {//专科
            meCollegeSchool.setVisibility(View.GONE);
        } else {
            meCollegeSchool.setVisibility(View.VISIBLE);
            meCollegeSchool.setValuesForMark(userInfo.getCollegeSpecializCertificate());
        }
        if (userInfo.getCollegeSchoolCertificate() == null || userInfo.getCollegeSchoolCertificate().equals("")) {//本科
            meCollegeSpecializ.setVisibility(View.GONE);
        } else {
            meCollegeSpecializ.setVisibility(View.VISIBLE);
            meCollegeSpecializ.setValuesForMark(userInfo.getCollegeSchoolCertificate());
        }
        if (userInfo.getBachelorCertificate() == null || userInfo.getBachelorCertificate().equals("")) {//学士
            meBachelor.setVisibility(View.GONE);
        } else {
            meBachelor.setVisibility(View.VISIBLE);
            meBachelor.setValuesForMark(userInfo.getBachelorCertificate());
        }
        if ((userInfo.getBachelorCertificate() == null || userInfo.getMiddleSchoolCertificate().equals(""))
                && (userInfo.getCollegeSpecializCertificate() == null || (userInfo.getCollegeSpecializCertificate().equals("")))
                && (userInfo.getCollegeSchoolCertificate() == null || userInfo.getCollegeSchoolCertificate().equals(""))
                && (userInfo.getBachelorCertificate() == null || userInfo.getBachelorCertificate().equals(""))) {
            meHeadJiaoyubeijing.setVisibility(View.GONE);//教育背景
        } else {
            meHeadJiaoyubeijing.setVisibility(View.VISIBLE);//教育背景
        }
    }


    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.me_back://返回
                finish();
                break;
        }
    }

    /**
     * 点击的监听器
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
             //修改图像
            case R.id.me_container_icon:
                startActivity(new Intent(MyMeActivity.this,ChangeHeadPictureActivity.class));
                break;
            //修改姓名
            case R.id.me_realName:
                //有学籍的不可编辑
                if(!hasStudentNumber){
                    ChangeRealNameActivity.StartActivity(MyMeActivity.this);
                }

                break;
            //修改性别
            case R.id.me_sex:
                //有学籍的不可编辑
                if(!hasStudentNumber){
                    updateSex();
                }

                break;
            //修改身份证号码
            case R.id.me_idCard:
                //有学籍的不可编辑
                if(!hasStudentNumber){
//                    ChangeIdCardActivity.StartActivity(MyMeActivity.this,idCard);
                }
                break;
            //修改地址
            case R.id.me_address:
                Intent intent = new Intent(this, ChangeAddressActivity.class);
                startActivity(intent);
                break;
            //修改名族
            case R.id.me_nation:
                wheelNation.pop(findViewById(R.id.layout_me), nationList, "选择民族", new WheelViewCallBack() {
                    @Override
                    public void getSelectItem(String content) {
                        if(content==null){
                            return;
                        }
                        Logger.d("wheelNation.pop:"+content);
                        initData(6, content);
                    }
                });
                break;
            //修改政治面貌
            case R.id.me_politicsStatus:
                wheelpolitics.pop(findViewById(R.id.layout_me), poliList, "选择政治面貌", new WheelViewCallBack() {
                    @Override
                    public void getSelectItem(String content) {
                        if(content==null){
                            return;
                        }
                        initData(7, content);
                    }
                });
                break;
            //修改固定电话
            case R.id.me_fixPhone:
                ChangeFixPhoneActivity.StartActivity(MyMeActivity.this,fixPhone);
                break;
            //修改邮编
            case R.id.me_postalCode:
                ChangePostcodeActivity.StartActivity(MyMeActivity.this,postCode);
                break;
        }
    }

    /**
     * 发送请求修改家庭地址
     */
    private void initData(final int araType, final String content) {
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
        HashMap<String, String> map = new HashMap<>();
        map.put("id", endUserId);
        map.put("type", String.valueOf(araType));
        map.put("content", content);
        HttpClient.post(this, Api.MODIFY_USER_INFO, map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                    switch (araType) {
                        case 3:
                            if (id == 1) {
                                tvSex.setText("男");
                            } else if (id == 2) {
                                tvSex.setText("女");
                            } else {
                                tvSex.setText("保密");

                            }
                            break;

                        case 6:
                            //显示修改之后的民族
                            meNation.getEtMark().setText(content);
                            break;
                        case 7:
                            //显示修改之后的政治面貌
                            mePoliticsStatus.getEtMark().setText(content);
                            break;
                    }

                } else {
                    ToastUtil.showShortToast(MyMeActivity.this, result.getMessage());
                }
            }

        });
    }

    /**
     * 修改性别
     */
    private void updateSex() {
        if ("男".equals(tvSex.getText().toString())) {
            sex = 0;
        } else if ("女".equals(tvSex.getText().toString())) {
            sex = 1;
        } else {
//            sex = 2;
        }
        new AlertDialog.Builder(this).setTitle("请选择性别")
                .setSingleChoiceItems(
                        new String[]{"男", "女"}, sex, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                index = which;
                            }
                        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (index == 0) {
                    id = 1;
                } else  {
                    id = 2;
                }
                initData(3, String.valueOf(id));
                dialog.dismiss();
            }
        }).setNegativeButton("取消", null).show();
    }
}
