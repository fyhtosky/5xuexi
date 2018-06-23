package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;


/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/16.
 *
 * listVIew expandListView GridView   的item项   单选自定义控件
 */
public class SingleChoiceView extends LinearLayout implements Checkable {

    private TextView mText;
    private CheckBox mCheckBox;

    public SingleChoiceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public SingleChoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SingleChoiceView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context){
        // 填充布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.view_singlechoice,this);
        mText = (TextView) v.findViewById(R.id.SingleChoice_textview);
        mCheckBox = (CheckBox) v.findViewById(R.id.SingleChoice_checkbox);
    }



    public TextView getmText() {
        return mText;
    }

    @Override
    public void setChecked(boolean checked) {
        mCheckBox.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return mCheckBox.isChecked();
    }

    @Override
    public void toggle() {
        mCheckBox.toggle();
    }

    public void setTitle(String text){
        mText.setText(text);
    }
    public void setTitle(CharSequence text){
        mText.setText(text);
    }

    public CheckBox getCheckBox(){
        return mCheckBox;
    }
    

}
