package com.sj.yinjiaoyun.xuexi.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/5.
 * 作答报告里面的 自定义控件
 */
public class AnswerView extends LinearLayout{

   private Context context;
   private TextView tvQid;
   private TextView tvName;
   private TextView tvMark;
    public AnswerView(Context context) {
        super(context);
        init(context);
    }

    public AnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        getValues(attrs);
    }

    public AnswerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        getValues(attrs);
    }

    //初始化控件
    private void init(Context context){
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.view_answer,this);
        tvQid= (TextView) view.findViewById(R.id.view_answer_qid);
        tvName= (TextView) view.findViewById(R.id.view_answer_name);
        tvMark= (TextView) view.findViewById(R.id.view_answer_mark);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getValues(AttributeSet attrs){
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.attrs_answer);
        String t1=array.getString(R.styleable.attrs_answer_attrs_answer_qid);
        tvQid.setText(t1);
        Drawable d1 = array.getDrawable(R.styleable.attrs_answer_attrs_answer_qidBg);
        tvQid.setBackground(d1);

        String t2=array.getString(R.styleable.attrs_answer_attr_answer_name);
        tvName.setText(t2);
        int f2=array.getColor(R.styleable.attrs_answer_attr_answer_nameColor,getResources().getColor(R.color.colorHomeitem));
        tvName.setTextColor(f2);

        String t3=array.getString(R.styleable.attrs_answer_attr_answer_mark);
        tvMark.setText(t3);
        int f3=array.getColor(R.styleable.attrs_answer_attr_answer_markColor,getResources().getColor(R.color.colorHomeitem));
        tvMark.setTextColor(f3);
        array.recycle();
    }

    public void setTvQid(String qid){
        tvQid.setText(qid);
    }

    public void setTvName(String name){
        tvName.setText(name);
    }

    public void setTvMark(String mark) {
        tvMark.setText(mark);
    }
}
