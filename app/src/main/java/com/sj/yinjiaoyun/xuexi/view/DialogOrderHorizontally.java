package com.sj.yinjiaoyun.xuexi.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.DialogOrderAdapter;
import com.sj.yinjiaoyun.xuexi.domain.MyOrderChild;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/1/9.
 * 订单列表里面点击切换学期 ，弹出的下拉框
 */
public abstract class DialogOrderHorizontally extends Dialog implements AdapterView.OnItemClickListener,View.OnClickListener{
    private Activity activity;

    public DialogOrderHorizontally(Activity activity, List<MyOrderChild> list) {
        super(activity, R.style.MyDialogOrderTheme);
        this.activity = activity;
        dialogAdapter=new DialogOrderAdapter(activity,list);
    }

    ViewHolder holder;
    DialogOrderAdapter dialogAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_myorder);
        holder=new ViewHolder();
        holder.goLearn = (TextView) findViewById(R.id.dialog_order_goLearn);
        holder.account = (TextView) findViewById(R.id.dialog_order_account);
        holder.goPay = (TextView) findViewById(R.id.dialog_order_goPay);
        holder.gridView= (GridView) findViewById(R.id.dialog_order_gridVIew);
        holder.account.setText("应付款：￥"+"");

        holder.gridView.setAdapter(dialogAdapter);
        holder.gridView.setOnItemClickListener(this);
        holder.goLearn.setOnClickListener(this);
        holder.goPay.setOnClickListener(this);
        holder.account.setOnClickListener(this);
        setViewLocation();
        setCanceledOnTouchOutside(true);//外部点击取消
    }

    /**
     * 设置dialog位于屏幕底部
     */
    private   void setViewLocation(){
        DisplayMetrics dm = new DisplayMetrics();
        Display d1 =activity.getWindowManager().getDefaultDisplay();
        d1.getMetrics(dm);

        int height = dm.heightPixels;

        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = height;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = (int) (d1.getHeight()*0.5);
        // 设置显示位置
        onWindowAttributesChanged(lp);
    }

    class ViewHolder{
        TextView goLearn;//去学习
        TextView account;//应付款
        TextView goPay;//去支付
        GridView gridView;//
    }

}
