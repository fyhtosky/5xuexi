package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;


/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/2.
 * 主页 -我Fragment页面 图标和文字信息
 */
public class MeView extends LinearLayout {
    Context context;
    ImageView etIcon;
    TextView etText;
    TextView etMark;
    public MeView(Context context) {
        super(context);
        init(context);
    }

    public MeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        getValues(attrs);
    }

    public MeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        getValues(attrs);
    }

    //初始化控件
    private void init(Context context){
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.view_me,this);
        etIcon= (ImageView) view.findViewById(R.id.view_me_icon);
        etText= (TextView) view.findViewById(R.id.view_me_text);
        etMark= (TextView) view.findViewById(R.id.view_me_mark);
    }

    public void getValues(AttributeSet attrs){
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.attrs_me);
        Drawable icon=array.getDrawable(R.styleable.attrs_me_attr_image);
        etIcon.setImageDrawable(icon);
        String text=array.getString(R.styleable.attrs_me_attr_txt);
        float f1=array.getFloat(R.styleable.attrs_me_attr_txt_size,16);
        int clolor1=array.getColor(R.styleable.attrs_me_attr_txt_color,getResources().getColor(R.color.colorHomeitem));
        float ff1=array.getFloat(R.styleable.attrs_me_attr_txt_alpha,1f);
        etText.setText(text);
        etText.setTextSize(f1);
        etText.setTextColor(clolor1);
        etText.setAlpha(ff1);
        String mark=array.getString(R.styleable.attrs_me_attr_mark);
        float f2=array.getFloat(R.styleable.attrs_me_attr_mark_size,14);
        int clolor2=array.getColor(R.styleable.attrs_me_attr_mark_color,getResources().getColor(R.color.colorHomeitem));
        float ff2=array.getFloat(R.styleable.attrs_me_attr_mark_alpha,0.6f);
        etMark.setText(mark);
        etMark.setTextSize(f2);
        etMark.setTextColor(clolor2);
        etMark.setAlpha(ff2);
        array.recycle();
    }

    public void setValuesForMark(String mark){
        etMark.setText(mark);
    }

    public void setValuesForText(String mark){
        etText.setText(mark);
    }

    public TextView getEtMark() {
        return etMark;
    }

    public void setEtMark(TextView etMark) {
        this.etMark = etMark;
    }

    public ImageView getEtIcon() {
        return etIcon;
    }


}
