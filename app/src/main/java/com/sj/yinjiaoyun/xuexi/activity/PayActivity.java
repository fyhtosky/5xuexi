package com.sj.yinjiaoyun.xuexi.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.pay.SignOrderInfo;
import com.sj.yinjiaoyun.xuexi.pay.PayResult;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.MyUtil;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/1/6.
 * 支付页面
 */
public class PayActivity extends MyBaseActivity implements HttpDemo.HttpCallBack{

    String TAG="alipay";
    TitleBarView titleBarView;
    TextView tvPrice;
    Button btnGoPay;

    Intent intent;
    SignOrderInfo signOrderInfo;
    String endUserId;
    HttpDemo demo;
    List<Pairs> pairsList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pay);
        init();
        initView();
    }

    //初始化时间
    private void init() {
        demo=new HttpDemo(this);
        intent=getIntent();
        signOrderInfo=intent.getParcelableExtra("SignOrderInfo");
        endUserId=signOrderInfo.getEndUserId();
        Log.i(TAG, "init: "+signOrderInfo.toString());
    }

    private void initView() {
        titleBarView= (TitleBarView) findViewById(R.id.pay_titleBarView);
        btnGoPay= (Button) findViewById(R.id.pay_goPay);
        tvPrice= (TextView) findViewById(R.id.pay_price);
        String amount=signOrderInfo.getAmount();
        tvPrice.setText(MyUtil.setTextFormatColor(this,"实付金额："+amount,""+amount,R.color.colorRed));
        titleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnGoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoPayFromAlipay();
            }
        });
    }

    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    Log.i(TAG, "handleMessage:1支付情况结果反馈: " + payResult.toString());
                    // 判断resultStatus 为9000则代表支付成功
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        goPayVerifySign(payResult.getResult());
                    } else if(TextUtils.equals(resultStatus,"8000")){//正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "正在处理中，请查询“我的订单”", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.equals(resultStatus,"4000")){//订单支付失败
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.equals(resultStatus,"6001")){//用户中途取消
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "您已取消了支付", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.equals(resultStatus,"6002")){//网络连接出错
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败，网络连接出错啦", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.equals(resultStatus,"6004")){//网络连接出错
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "正在处理中，请查询“我的订单”", Toast.LENGTH_SHORT).show();
                    }else {//其它支付错误
                        Toast.makeText(PayActivity.this, "支付错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    /**
     * 进入支付宝进行 加签服务请求
     */
    public void GoPayFromAlipay() {
        Log.i(TAG, "GoPayFromAlipay: ");
        String url= MyConfig.getURl("order/signPayParams");
        pairsList=new ArrayList<>();
        pairsList.add(new Pairs("orderCode",signOrderInfo.getOrderCode()));
        pairsList.add(new Pairs("endUserId",signOrderInfo.getEndUserId()));
        pairsList.add(new Pairs("amount",signOrderInfo.getAmount()));
        demo.doHttpPost(url,pairsList,1);
    }

    /**
     * 对于支付结果，与服务端进行验签
     */
    private void goPayVerifySign( String resultInfo){
        Log.i(TAG, "handleMessage: 需要验签的信息"+resultInfo);
        //开始验签
        String url=MyConfig.getURl("order/syncAppBackInfo");
        pairsList=new ArrayList<>();
        pairsList.add(new Pairs("returnStr",resultInfo));
        demo.doHttpPost(url,pairsList,2);
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        try {
            if(requestCode==1) {
                parserGetSign(msg);
            }else if(requestCode==2){
                parserVerifySign(msg);
            }
        } catch (Exception e) {
            Log.e(TAG, "setMsg: "+e.toString());
        }
    }


    /**
     * 解析加签信息 ，如果加签成功了，就访问支付宝
     * @param msg
     */
    private void parserVerifySign(String msg) throws Exception {
        Log.i(TAG, "parserVerifySign: 支付成功后验签"+msg);
        JSONObject jsonObject=new JSONObject(msg);
        if(jsonObject.getBoolean("success")){
            Log.e(TAG, "parserVerifySign: "+"验签成功");
            Intent intent=new Intent(PayActivity.this,MyOrderActivity.class);
            intent.putExtra("endUserId",endUserId);
            startActivity(intent);
            ActiveActUtil.getInstance().addOrderActivity(PayActivity.this);
            ActiveActUtil.getInstance().exitOrder();//把支付流程里面设计的所有activity关闭掉
        }else{
            Toast.makeText(this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
            Log.i(TAG, "parserVerifySign: 验签失败"+jsonObject.getString("message"));
        }
    }


    /**
     * 解析加签信息 ，如果加签成功了，就访问支付宝
     * @param msg
     */
    private void parserGetSign(String msg) throws Exception {
        Log.i(TAG, "parserGetSign: 解析加签"+msg);
        JSONObject jsonObject=new JSONObject(msg);
        Boolean success=jsonObject.getBoolean("success");
        if(success){
            JSONObject object=jsonObject.getJSONObject("data");
            final String orderSign = object.getString("orderSign");
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask alipay = new PayTask(PayActivity.this);
                    Map<String, String> result = alipay.payV2(orderSign, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
    }


}
