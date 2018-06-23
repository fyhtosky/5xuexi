package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domains.MicroFuse;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.pay.SignOrderInfo;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.MyUtil;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/15.
 * 微专业  确认 订单 界面
 */
public class ConfirmMicroOrderActivity extends MyBaseActivity {
    String TAG = "confirmmicro";
    Intent intent;
    String orderCode;//订单号
    String endUserId;
    MicroFuse microFuse;
    Long startTime;
    Long endTime;

    TitleBarView titleBarView;
    ImageView orderImage;
    TextView orderCourseName;
    TextView orderCollegeName;
    TextView orderAttenter;
    TextView orderPrice;
    TextView orderGoodsAmount;
    TextView orderRealPay;
    TextView orderTutionWay;
    Button orderButton;
    View containerTime;

    SignOrderInfo orderInfo;//订单信息，

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_confirmmicroorder);
        init();
        initView();
        initEvent();

    }


    //数据准备
    private void init() {
        intent = getIntent();
        endUserId = intent.getStringExtra("endUserId");
        microFuse = intent.getParcelableExtra("MicroFuse");
        orderCode = intent.getStringExtra("orderCode");
        startTime = intent.getLongExtra("startTime", 0);
        endTime = intent.getLongExtra("endTime", 0);
        Log.i(TAG, "init: " + "用户endUserId" + endUserId + " 订单号：" + orderCode + " startTime：" + startTime + " endTime：" + endTime + " 微专业" + microFuse.toString());
    }

    //初始化控件
    private void initView() {
        orderImage = (ImageView) findViewById(R.id.confirm_micro_image);
        containerTime = findViewById(R.id.confirm_micro_time);
        orderTutionWay= (TextView) findViewById(R.id.confirm_micro_tutionWay);
        orderCourseName = (TextView) findViewById(R.id.confirm_micro_courseName);
        orderCollegeName = (TextView) findViewById(R.id.confirm_micro_college);
        orderAttenter = (TextView) findViewById(R.id.confirm_micro_attention);
        orderPrice = (TextView) findViewById(R.id.confirm_micro_price);
        orderGoodsAmount = (TextView) findViewById(R.id.confirm_micro_goodsAmount);
        orderRealPay = (TextView) findViewById(R.id.confirm_micro_realPay);
        orderButton = (Button) findViewById(R.id.confirm_micro_tijiao);
        titleBarView = (TitleBarView) findViewById(R.id.confirm_micro_titleBarView);
        titleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //初始化时间
    private void initEvent() {
        if (TextUtils.isEmpty(microFuse.getCourseLogo())) {
            Picasso.with(this)
                    .load(R.mipmap.error)
                    .placeholder(R.drawable.progressbar_landing)
                    //加载失败中的图片显示
                    .error(R.mipmap.error).into(orderImage);
        } else {
            Picasso.with(this)
                    .load(microFuse.getCourseLogo())
                    //加载过程中的图片显示
                    .placeholder(R.drawable.progressbar_landing)
                    //加载失败中的图片显示
                    .error(R.mipmap.error)
                    //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                    .centerCrop()
                    .resize(500, 315).into(orderImage);
        }
        orderCourseName.setText(microFuse.getCourseName());
        orderCollegeName.setText(microFuse.getCollegeName());
        orderAttenter.setText(microFuse.getNumber() + "");
        String price = microFuse.getPrice();
        orderPrice.setText("¥" + price);
        if (startTime == 0 || endTime == 0) {
            containerTime.setVisibility(View.GONE);
        } else {
            orderGoodsAmount.setText(MyUtil.getTime(startTime, "yyyy-MM-dd") + " ～ " + MyUtil.getTime(endTime, "yyyy-MM-dd"));
            containerTime.setVisibility(View.VISIBLE);
        }
        String type="";
        switch (microFuse.getTutionWay()){
            case 0:
                type="随到随学";
                break;
            case 1:
                type="定期开课";
                break;
        }
        orderTutionWay.setText(type);
        orderRealPay.setText(MyUtil.setTextFormatColor(this, "实付金额：¥" + price, "¥" + price, R.color.colorRed));
        orderInfo = new SignOrderInfo(orderCode, endUserId, price);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmMicroOrderActivity.this, PayActivity.class);
                intent.putExtra("SignOrderInfo", orderInfo);
                startActivity(intent);
                ActiveActUtil.getInstance().addOrderActivity(ConfirmMicroOrderActivity.this);
            }
        });
    }

}
