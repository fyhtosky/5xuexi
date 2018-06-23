package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.ConfirmMicroOrderActivity;
import com.sj.yinjiaoyun.xuexi.activity.LoginActivity;
import com.sj.yinjiaoyun.xuexi.activity.MainActivity;
import com.sj.yinjiaoyun.xuexi.activity.SubscribeActivity;
import com.sj.yinjiaoyun.xuexi.domains.MicroFuse;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.Training;
import com.sj.yinjiaoyun.xuexi.domains.ParseTrainingInfo;
import com.sj.yinjiaoyun.xuexi.domains.ParseTrainingInfoData;
import com.sj.yinjiaoyun.xuexi.domains.TrainingItem;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.utils.MyUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.view.PickerView;
import com.sj.yinjiaoyun.xuexi.domains.Pickers;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.squareup.picasso.Picasso;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/9.
 * 微专业预览页面 简介
 */
public class MicroHintIntroFragment extends Fragment implements HttpDemo.HttpCallBack {

    private String TAG = "microhintintrofragment";
    HttpDemo demo;
    List<Pairs> pairsList;

    ImageView ivImage;
    TextView tvCouse;
    TextView tvCollege;
    TextView tvTutionWay;
    TextView tvPrice;
    TextView tvAttention;
    TextView tvRegisterTime;
    TextView tvIntro;
    TextView microGoTo;
    View timeContainer;//开课方式

    int orderStatus;//定单的状况 101 订单存在已付款等待审核，100订单存在未付款，109订单存在查实取消状态 120 订单不存在   ( 104 订单完成)
   private String endUserId;//
    String trainingId;//微专业id
    String trainingItemId;//分期id
    String price;//分期的价格
    MicroFuse microFuse;//微专业封装类
    String orderCode;//订单号 100订单存在未付款状态 此时才不为空
    Activity activity;

    List<TrainingItem> itemList;//分期变量集合
    Byte tutionWay;//该微专业开课方式
    Training training;//解析出来的微专业信息

    CallBackFromMicroHintIntro callback;
    private boolean loginState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_micro_hint_intro, container,false);
        viewDialog = inflater.inflate(R.layout.dialog_micro_hint, null);
        initView(view);
        initEvent();
        getHttpDate();
        return view;
    }


    /**
     * 从activity传递过来的数据
     *
     * @param callback    接口回调（传递分期id至课程体系）
     * @param orderStatus 订单状况
     * @param microFuse   微专业封装类
     */
    public void setDateForActivity(CallBackFromMicroHintIntro callback, int orderStatus, MicroFuse microFuse, String orderCode) {
        this.callback = callback;
        this.orderStatus = orderStatus;
        this.microFuse = microFuse;
        this.orderCode = orderCode;
        Log.i(TAG, "setDateForActivity: " + microFuse.toString());
    }

    private void initView(View view) {
        demo = new HttpDemo(this);
        trainingId = String.valueOf(microFuse.getMicroId());
        endUserId= PreferencesUtils.getSharePreStr(getActivity(), "username");
        loginState=PreferencesUtils.getSharePreBoolean(getActivity(), Const.LOGIN_STATE);
        ivImage = (ImageView) view.findViewById(R.id.micro_hint_intro_image);
        tvCouse = (TextView) view.findViewById(R.id.micro_hint_intro_course);
        tvCollege = (TextView) view.findViewById(R.id.micro_hint_intro_college);
        tvTutionWay = (TextView) view.findViewById(R.id.micro_hint_intro_tutionWay);
        tvPrice = (TextView) view.findViewById(R.id.micro_hint_intro_price);
        tvAttention = (TextView) view.findViewById(R.id.micro_hint_intro_attention);
        tvRegisterTime = (TextView) view.findViewById(R.id.micro_hint_intro_registerTime);
        tvIntro = (TextView) view.findViewById(R.id.micro_hint_intro_jianjie);
        microGoTo = (TextView) view.findViewById(R.id.micro_hint_goto);
        timeContainer = view.findViewById(R.id.include_micro_timeContainer);
    }

    private void initEvent() {
        microGoTo.setOnClickListener(listener);
        tvRegisterTime.setOnClickListener(listener);
    }

    /**
     * 获取列表数据
     */
    public void getHttpDate() {
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("training/findTrainingInfo");
        pairsList.add(new Pairs("trainingId", trainingId));//微专业id
        demo.doHttpGet(url, pairsList, 1);
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        // Log.i(TAG, "setMsg: " + msg);
        try {
            if (requestCode == 1) {//详情信息
                parseTraningInfo(msg);
            } else if (requestCode == 2) {//免费微专业创建订单后去学习 立即报名 接口
                parseApplyName(msg);
            } else if (requestCode == 3) {  //创建付费订单 后并去支付
                 parseCreateCode(msg);
            }
        } catch (Exception e) {
            Log.e(TAG, "setMsg: " + e.toString());
        }
    }

    /**
     * 解析创建微专业订单号接口  创建成功后确认订单   付费
     */
    private void parseCreateCode(String msg) throws Exception {
        Log.i(TAG, "parserCreateOrderCode: " + msg);
        JSONObject jsonObject = new JSONObject(msg);
        Boolean success = jsonObject.getBoolean("success");
        if (success) {
            JSONObject object = jsonObject.getJSONObject("data");
            String orderCode1 = object.getString("orderCode");
            Log.i(TAG, "parserCreateOrderCode: " + orderCode1);
            if (!TextUtils.isEmpty(orderCode1)) {
                microFuse.setPrice(price);//重置价格为分期的价格
                goToFlag = 2;
                microGoTo.setText("去付款");
                Intent inte = new Intent(activity, ConfirmMicroOrderActivity.class);
                inte.putExtra("orderCode", orderCode1);
                inte.putExtra("endUserId", endUserId);
                inte.putExtra("MicroFuse", microFuse);
                startActivity(inte);
                ActiveActUtil.getInstance().addOrderActivity(activity);
            }
        } else {
            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    //解析免费 立即报名
    private void parseApplyName(String msg) throws Exception {
        Log.i(TAG, "setMsg:立即报名网络接口 " + msg);
        JSONObject jsonObject = new JSONObject(msg);
        if (jsonObject.getBoolean("success")) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("indexType", "2");//设置跳转至课程表下的  微专业
            activity.setResult(1, intent);
            activity.finish();
            dialogHint.dismiss();
        } else {
            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    int trainingCurrentInd;
    //解析详情信息
    private void parseTraningInfo(String msg) throws Exception
    {
        Gson gson = new Gson();
        ParseTrainingInfo parseTrainingInfo = gson.fromJson(msg, ParseTrainingInfo.class);
        ParseTrainingInfoData date = parseTrainingInfo.getData();
        training = date.getTraining();
        itemList = date.getTrainingItems();
        trainingCurrentInd=date.getTrainingCurrentInd();
        setValues(training, itemList,trainingCurrentInd);
    }


    /**
     * 给各控件设置值
     *
     * @param training 微专业
     * @param itemList 微专业分期 的信息集合
     @param trainingCurrentInd 默认显示分期的下标索引
     */
    private void setValues(Training training, List<TrainingItem> itemList ,int trainingCurrentInd) throws Exception
    {
        tutionWay = training.getTutionWay();
        if (TextUtils.isEmpty(training.getTrainingLogo())) {
            Picasso.with(activity).load(R.mipmap.error)
                    .resize(360, 200)
                    .into(ivImage);
        } else {
            Picasso.with(activity).load(training.getTrainingLogo())
                    .resize(360, 200)
                    .into(ivImage);
        }
        tvCouse.setText(training.getTrainingName());
        tvCollege.setText(training.getCollegeName());
        tvTutionWay.setText(MyConfig.tutionWay().get(training.getTutionWay() + "").toString());
        tvAttention.setText(training.getEnterNumber() + "");
        tvIntro.setText(Html.fromHtml(training.getTrainingDesc()));
        TrainingItem trainingItem = itemList.get(trainingCurrentInd);//设置滚动条首次默认在当前时间戳的项
        price = MyUtil.saveTwoScale(trainingItem.getPrice());
        if (training.getIsFree() == 0) {//是否付费  0：收费
            tvPrice.setTextColor(getResources().getColor(R.color.colorRed));
            tvPrice.setText("¥" + price);
        } else if (training.getIsFree() == 1) {//1：免费
            tvPrice.setTextColor(getResources().getColor(R.color.colorGreen));
            tvPrice.setText("免费");
        }
        if (tutionWay == 0) {//随到随学
            timeContainer.setVisibility(View.GONE);
            setValuestoGoTo(0, trainingItem);
        } else if (tutionWay == 1) {//定期开课
            timeContainer.setVisibility(View.VISIBLE);
            String sb = MyUtil.getTime(trainingItem.getEnteredStartTime(), "yyyy-MM-dd") +
                    " ～ " +
                    MyUtil.getTime(trainingItem.getEnteredEndTime(), "yyyy-MM-dd");
            tvRegisterTime.setText(sb);
            setValuestoGoTo(1, trainingItem);
        }
    }


    Long startTime;//选中的开课时间戳
    Long endTime;//选中的开课时间戳

    /**
     * 底部按钮的值判断并设置不同内容
     *
     * @param flag
     */
    private void setValuestoGoTo(int flag, TrainingItem trainingItem) {
        startTime = trainingItem.getEnteredStartTime();
        endTime = trainingItem.getEnteredEndTime();
        trainingItemId = String.valueOf(trainingItem.getId());
        callback.deliveryToCurriclum(trainingItemId);//给课程体系设置
        if(!loginState){
            microGoTo.setText("点击登录");
            microGoTo.setBackgroundResource(R.color.colorGreen);
            return;
        }
        if (orderStatus == 110) {//订单已经完成
            goToFlag = 1;
            microGoTo.setText("去学习");
            microGoTo.setBackgroundResource(R.color.colorGreen);
        } else if (orderStatus == 100) { //100订单存在未付款状态
            goToFlag = 2;
            microGoTo.setText("去付款");
            microGoTo.setBackgroundResource(R.color.colorGreen);
        } else if (orderStatus == 101) {//101 订单存在已付款等待审核状态
            goToFlag = 3;
            microGoTo.setText("审核中");
            microGoTo.setBackgroundResource(R.color.colorGreen);
        } else if (orderStatus == 120 || orderStatus == 109) {//120订单不存在  109订单已取消   （此种情况要冲洗创建订单号）
            if (flag == 0) {//随到随学
                goToFlag = 4;
                microGoTo.setText("立即参加");
                microGoTo.setBackgroundResource(R.color.colorGreen);
            } else if (flag == 1) {//定期开课
                Long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis >= trainingItem.getEnteredStartTime() && currentTimeMillis <= trainingItem.getEnteredEndTime()) {//判断是否在规定开课时间时间内
                    goToFlag = 5;
                    microGoTo.setText("立即参加");
                    microGoTo.setBackgroundResource(R.color.colorGreen);
                } else {
                    goToFlag = 6;
                    microGoTo.setText("预约");
                    microGoTo.setBackgroundResource(R.color.colorOrange);
                }
            }
        }

    }

    int goToFlag;//底部按钮区分值

    /**
     * 最底端按钮点击 根绝底部按钮区分值来判断不同去向的不同操作
     */
    private void clickMicroGoTo() {
        if (goToFlag == 1) {//去学习
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("indexType", "2");//课程表下的  微专业
            activity.setResult(1, intent);
            activity.finish();
        } else if (goToFlag == 2) {//100订单存在未付款状态 显示去支付
            microFuse.setPrice(price);//重置价格为分期的价格
            Intent inte = new Intent(activity, ConfirmMicroOrderActivity.class);
            inte.putExtra("orderCode", orderCode);
            inte.putExtra("MicroFuse", microFuse);
            inte.putExtra("endUserId", endUserId);
            inte.putExtra("startTime", startTime);
            inte.putExtra("endTime", endTime);
            startActivity(inte);
            ActiveActUtil.getInstance().addOrderActivity(activity);
        } else if (goToFlag == 3) {//审核中（不做操作）

        } else if (goToFlag == 4 || goToFlag == 5) {//立即参加
            if (training.getIsFree() == 0) {//付费
                getHttpCreateOrderCode(0, 0, trainingItemId, 3);
            } else if (training.getIsFree() == 1) {//免费公开课，可以直接学习
                setGoToPayDialog();
            }
        } else if (goToFlag == 6) {//预约
            Intent intent = new Intent(activity, SubscribeActivity.class);
            intent.putExtra("endUserId", endUserId);
            intent.putExtra("collegeId", microFuse.getCollegeId());
            intent.putExtra("traningId", String.valueOf(microFuse.getMicroId()));
            startActivity(intent);
        }
    }

    AlertDialog dialogHint;//免费公开课提示

    /**
     * 如果微专业免费 dialog提示免费可以观看   如果公开课付费 分两种情况：
     * 1 没有创建订单      没有付款
     * 2 已创建有订单      没有付款
     */
    public void setGoToPayDialog() {
        Log.i(TAG, "setGoToPayOrBug: ");
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("提示");
        builder.setMessage("该微专业免费")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getHttpCreateOrderCode(1, 1, trainingItemId, 2);//立即报名 接口公用
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogHint.dismiss();
                    }
                });
        dialogHint = builder.create();
        dialogHint.show();
    }


    /**
     * 创建订单接口 （立即报名 ）
     *
     * @param payType        //0:分期 1：全部
     * @param whetherFree    //是否免费  0:不免费   1 免费
     * @param trainingItemId //微专业详情ID
     * @param flag           区分码   2免费公开课创建订单去学习页面 3创建订单后去支付
     */
    private void getHttpCreateOrderCode(int payType, int whetherFree, String trainingItemId, int flag) {
        Log.i(TAG, "getHttpCreateOrderCode: " + " 用户id" + endUserId + " 公开课id:" + trainingId + " 分期:" + trainingItemId);
        if (TextUtils.isEmpty(endUserId) || TextUtils.isEmpty(trainingId) || TextUtils.isEmpty(trainingItemId)) {
            Toast.makeText(activity, "用户id或微专业id或微专业分期id为空", Toast.LENGTH_SHORT).show();
            return;
        }
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("order/createOrder");
        pairsList.add(new Pairs("payType", payType + ""));//0:分期 1：全部
        pairsList.add(new Pairs("whetherFree", whetherFree + ""));//是否免费  0:不免费   1 免费
        pairsList.add(new Pairs("endUserId", endUserId));//用户 id
        pairsList.add(new Pairs("trainingId", trainingId));//微专业ID
        pairsList.add(new Pairs("trainingItemId", trainingItemId));//微专业详情ID
        pairsList.add(new Pairs("orderType", 2 + ""));//订单类型0：专业，1：公开课2:微专业
        pairsList.add(new Pairs("payMethod", 0 + ""));//0:线上,1:线下支付
        pairsList.add(new Pairs("payGatewayId", 1 + ""));//1:支付宝支付
        demo.doHttpPostLoading(activity, url, pairsList, flag);
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.micro_hint_intro_registerTime://报名时间
                    setRegisterCapacity(exangeDateList(itemList));
                    break;
                case R.id.micro_hint_goto://去学习、去购买
                    //未登录则点击去登录否则去弹出对话框
                    if(!loginState){
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }else{
                        clickMicroGoTo();
                    }

                    break;
                case R.id.dialog_micro_hint_positive://弹框里面确定
                    clickDialogPostive(pickerView.getSelected());
                    dialogTime.dismiss();
                    break;
                case R.id.dialog_micro_hint_negative://弹框里面取消
                    dialogTime.dismiss();
                    break;
            }
        }
    };


    /**
     * 把网络数据集合转化指定数据的集合
     *
     * @param itemList
     * @return
     */
    private List<Pickers> exangeDateList(List<TrainingItem> itemList) {
        List<Pickers> list = new ArrayList<>();
        StringBuilder sb;
        Long start;
        Long end;
        for (int i = 0; i < itemList.size(); i++) {
            sb = new StringBuilder();
            TrainingItem item = itemList.get(i);
            start = item.getEnteredStartTime();
            end = item.getEnteredEndTime();
            sb.append(MyUtil.getTime(start, "yyyy-MM-dd"));
            sb.append(" ～ ");
            sb.append(MyUtil.getTime(end, "yyyy-MM-dd"));
            list.add(new Pickers(String.valueOf(item.getId()), sb.toString(), item.getPrice(), start, end));
        }
        return list;
    }

    /**
     * 设置报名时间弹框
     */
    AlertDialog dialogTime;//报名时间弹框
    View viewDialog;
    PickerView pickerView;

    private void setRegisterCapacity(List<Pickers> listPickers) {
        if (listPickers == null || listPickers.size() == 1 || listPickers.size() == 0) {
            return;
        }
        if (dialogTime == null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setIcon(android.R.drawable.ic_dialog_info)
                    .setView(viewDialog);
            pickerView = (PickerView) viewDialog.findViewById(R.id.dialog_micro_hint_pickView);
            TextView tvPositive = (TextView) viewDialog.findViewById(R.id.dialog_micro_hint_positive);
            TextView tvNegative = (TextView) viewDialog.findViewById(R.id.dialog_micro_hint_negative);
            tvPositive.setOnClickListener(listener);
            tvNegative.setOnClickListener(listener);
            pickerView.setData(listPickers);
            dialogTime = builder.show();
        } else {
            dialogTime.show();
        }
    }


    /**
     * 弹框里面时间选择器的确定按钮点击事件  开课方式为定期开课
     */
    private void clickDialogPostive(Pickers pickers) {
        Log.i(TAG, "onClick: " + pickers.toString());
        startTime = pickers.getStartTime();
        endTime = pickers.getEndTime();
        price = MyUtil.saveTwoScale(pickers.getShowPrice());
        trainingItemId = pickers.getTrainingItemId();
        callback.deliveryToCurriclum(trainingItemId);
        tvRegisterTime.setText(pickers.getShowStr());
        tvPrice.setText("¥" + price);
        Long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis >= pickers.getStartTime() && currentTimeMillis <= pickers.getEndTime()) {//判断是否在规定开课时间时间内
            goToFlag = 5;
            microGoTo.setText("立即参加");
        } else {
            goToFlag = 6;
            microGoTo.setText("预约");
        }
    }

    public interface CallBackFromMicroHintIntro {
        void deliveryToCurriclum(String trainingItemId);//传递微专业分期id
    }

}
