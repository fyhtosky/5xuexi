package com.sj.yinjiaoyun.xuexi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/10/25.
 * 登录页面  账户图标等自定义控件
 */
public class NewEditView extends RelativeLayout{
    Context context;
    ImageView img;
    TextView tvName;
    EditText editText;
    public NewEditView(Context context) {
        super(context);
    }

    public NewEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        getValues(attrs);
    }

    public NewEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        getValues(attrs);
    }

    //初始化控件
    private void init(Context context){
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.view_edit,this);
        tvName= (TextView) view.findViewById(R.id.edit_name);
        img= (ImageView) view.findViewById(R.id.edit_icon);
        editText= (EditText) view.findViewById(R.id.edit_edit);
    }

    public void getValues(AttributeSet attrs){
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.attrs_edit);
        Drawable image=array.getDrawable(R.styleable.attrs_edit_attr_edit_icon);
        img.setImageDrawable(image);
        String hint=array.getString(R.styleable.attrs_edit_attr_edit_hint);
        editText.setHint(hint);
        String name=array.getString(R.styleable.attrs_edit_attr_edit_name);
        tvName.setText(name);
        array.recycle();
    }

    public String getEditText(){
        return editText.getText().toString();
    }

    public void setEditText(String text){
        editText.setText(text);
    }

    public void setHintText(String text){
        editText.setHint(text);
    }

    public void setSelection(int a){
        editText.setSelection(a);
    }

}
