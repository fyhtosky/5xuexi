package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/4.
 * 课程表顶部专业切换 自定义控件
 */
public class ScheduleTitleView extends RelativeLayout {
    TextView textView;
    CheckBox cb;
    Context context;
    View view;
    public ScheduleTitleView(Context context) {
        super(context);
        init(context);
    }

    public ScheduleTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        getValues(attrs);
    }

    public ScheduleTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        getValues(attrs);
    }

    private void init(Context context){
        this.context=context;
        view= LayoutInflater.from(context).inflate(R.layout.view_scheduletitle,this);
        textView= (TextView) view.findViewById(R.id.scheduletitle_name);
        cb= (CheckBox) view.findViewById(R.id.scheduletitle_cb);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    textView.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                }else{
                    textView.setTextColor(getContext().getResources().getColor(R.color.colorHomeitem));
                }
            }
        });
    }

    private void getValues(AttributeSet attrs){
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.attrs_scheduletitle);
        String name=array.getString(R.styleable.attrs_scheduletitle_attr_title);
        textView.setText(name);
        textView.setAlpha(0.7f);
        Boolean ischoice=array.getBoolean(R.styleable.attrs_scheduletitle_attr_checkbox,false);
        String check=array.getString(R.styleable.attrs_scheduletitle_attr_checkbox_text);
        float f1=array.getFloat(R.styleable.attrs_scheduletitle_attr_checkbox_size,14);
        cb.setChecked(ischoice);
        cb.setText(check);
        cb.setTextSize(f1);
        array.recycle();
    }

    public void setTextTitle(String title){
        textView.setText(title);
    }

    public CheckBox isChoice(){
        return cb;
    }

    public View clickView(){
        return  view;
    }

    public void setTextColor(int color){
        textView.setTextColor(color);
    }
}
