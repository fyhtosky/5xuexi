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
 * Created by ${沈军 961784535@qq.com} on 2016/11/17.
 * listVIew expandListView GridView   的item项 多选自定义控件
 *
 */
public class MultipleChoiceView extends LinearLayout implements Checkable {

    CheckBox cb;
    TextView tvName;
    TextView tvXing;

    public MultipleChoiceView(Context context) {
        super(context);
        init(context);
    }

    public MultipleChoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultipleChoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //初始化控件
    private void init(Context context){
        // 填充布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.view_kec_multiple,this);
        cb= (CheckBox) view.findViewById(R.id.item_kec_checkbox);
        tvName= (TextView) view.findViewById(R.id.item_kec_name);
        tvXing= (TextView) view.findViewById(R.id.item_kec_leiXing);
        tvName.setOnClickListener(listener);
        tvXing.setOnClickListener(listener);
    }

    OnClickListener listener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(flag==1){
                cb.toggle();
            }
        }
    };
    int flag=1;
    public void setUnclick(int flag){
        this.flag=flag;
    }

    @Override
    public void setChecked(boolean checked) {
        cb.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return cb.isChecked();
    }

    @Override
    public void toggle() {
        if(flag==1){
            cb.toggle();
        }
    }

    public void  setNameAndLeiXing(String name,String leixing){
//        tvName.setText(name);
        tvXing.setText(leixing);
    }

    public TextView getTvName() {
        return tvName;
    }

    public CheckBox getCheckBox(){
        return cb;
    }

}
