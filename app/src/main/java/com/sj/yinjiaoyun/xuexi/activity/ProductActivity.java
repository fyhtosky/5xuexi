package com.sj.yinjiaoyun.xuexi.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/21.
 * 订单里面 listview 点击事件 专业详情页面
 */
@SuppressLint("Registered")
public class ProductActivity extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product);

    }
}
