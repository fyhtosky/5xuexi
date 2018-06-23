package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;


/**
 * Created by Administrator on 2016/8/18.
 * 个人资料自定义控件（左边内别名   右边内容）
 */
public class InfoView extends RelativeLayout{
    Context context;
    TextView etName;
    TextView etText;
    public InfoView(Context context) {
        super(context);
        init(context);
    }

    public InfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        getValues(attrs);
    }

    public InfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        getValues(attrs);
    }
    //初始化控件
    private void init(Context context){
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.view_info,this);
        etName= (TextView) view.findViewById(R.id.view_name);
        etText= (TextView) view.findViewById(R.id.view_text);
    }

    public void getValues(AttributeSet attrs){
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.attrs_info);
        String name=array.getString(R.styleable.attrs_info_attr_name);
        etName.setText(name);
        String text=array.getString(R.styleable.attrs_info_attr_text);
        etText.setText(text);
        array.recycle();
    }

    //get控件设置值
    public void setValues(String name,String text){
        etName.setText(name);
        etText.setText(text);
    }
    //get控件设置值
    public void setValues(String text){
        if(text==null){
            etText.setText("");
        }else{
            etText.setText(text);
        }
    }

}
