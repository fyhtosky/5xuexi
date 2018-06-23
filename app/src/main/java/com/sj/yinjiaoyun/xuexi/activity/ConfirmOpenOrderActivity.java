package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domains.CouseFuse;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.pay.SignOrderInfo;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.MyUtil;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;
import com.squareup.picasso.Picasso;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/1/5.
 * 公开课 确认 订单 界面
 */
public class ConfirmOpenOrderActivity extends MyBaseActivity {

    String TAG="confirmopen";
    Intent intent;
    String orderCode;//订单号
    String endUserId;
    CouseFuse search;

    TitleBarView titleBarView;
    ImageView orderImage;
    TextView orderCourseName;
    TextView orderCollegeName;
    TextView orderAttenter;
    TextView orderPrice;
    TextView orderPriceCenter;
    TextView orderPriceBottom;
    Button orderButton;

    SignOrderInfo orderInfo;//订单信息，

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_confirmopenorder);
        init();
        initView();
        try {
            initEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //数据准备
    private void init() {
        intent=getIntent();
        endUserId=intent.getStringExtra("endUserId");
        search=intent.getParcelableExtra("search");
        orderCode=intent.getStringExtra("orderCode");
      //  Log.i(TAG, "init: "+"用户endUserId"+endUserId+" 订单号："+orderCode+" 课程表id"+courseScheduleId+" 公开课"+search.toString());
    }
    //初始化控件
    private void initView() {
        orderImage= (ImageView) findViewById(R.id.openorder_image);
        orderCourseName= (TextView) findViewById(R.id.openorder_courseName);
        orderCollegeName= (TextView) findViewById(R.id.openorder_collegeName);
        orderAttenter= (TextView) findViewById(R.id.openorder_attention);
        orderPrice= (TextView) findViewById(R.id.openorder_price);
        orderPriceCenter= (TextView) findViewById(R.id.openorder_price_center);
        orderPriceBottom= (TextView) findViewById(R.id.openorder_price_bottom);
        orderButton= (Button) findViewById(R.id.openorder_tijiao);
        titleBarView= (TitleBarView) findViewById(R.id.openorder_titleBarView);
        titleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //初始化时间
    private void initEvent() {
        if(TextUtils.isEmpty(search.getCourseLogo())){
            Picasso.with(this)
                    .load(R.mipmap.error)
                    .placeholder(R.drawable.progressbar_landing)
                    //加载失败中的图片显示
                    .error(R.mipmap.error).into(orderImage);
        }else{
            Picasso.with(this)
                    .load(search.getCourseLogo())
                    //加载过程中的图片显示
                    .placeholder(R.drawable.progressbar_landing)
                    //加载失败中的图片显示
                    .error(R.mipmap.error)
                    //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                    .centerCrop()
                    .resize(500, 315).into(orderImage);
        }
        orderCourseName.setText(search.getCourseName());
        orderCollegeName.setText(search.getCollegeName());
        orderAttenter.setText(search.getNumber()+"");
        String price=search.getPrice();
        orderPrice.setText("¥"+price);
        orderPriceCenter.setText("¥"+price);
        orderPriceBottom.setText(MyUtil.setTextFormatColor(this,"实付金额：¥"+price,"¥"+price,R.color.colorRed));
        orderInfo=new SignOrderInfo(orderCode,endUserId,price);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ConfirmOpenOrderActivity.this,PayActivity.class);
                intent.putExtra("SignOrderInfo",orderInfo);
                startActivity(intent);
                ActiveActUtil.getInstance().addOrderActivity(ConfirmOpenOrderActivity.this);
            }
        });
    }


    
}
