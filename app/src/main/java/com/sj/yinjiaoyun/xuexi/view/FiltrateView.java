package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/10.
 * 发现页面筛选 自定义控件
 */
public class FiltrateView extends LinearLayout {

    Context context;
    RadioGroup group;
    TextView tvFiltName;//筛选序目名
    RadioButton tvFiltTotal;//筛选全部（不做筛选）
    RadioButton tvFiltOne;//筛选条目一
    RadioButton tvFiltTwo;//筛选条目二

    public FiltrateView(Context context) {
        super(context);
        init(context);
    }

    public FiltrateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        getValues(attrs);
    }

    public FiltrateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        getValues(attrs);
    }

    //初始化控件
    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_filtrate, this);
        group = (RadioGroup) view.findViewById(R.id.view_filtrate_group);
        tvFiltName = (TextView) view.findViewById(R.id.view_filtrate_name);
        tvFiltTotal = (RadioButton) view.findViewById(R.id.view_filtrate_rb0);
        tvFiltOne = (RadioButton) view.findViewById(R.id.view_filtrate_rb1);
        tvFiltTwo = (RadioButton) view.findViewById(R.id.view_filtrate_rb2);
    }

    private void getValues(AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.attrs_filtrate);
        String name = array.getString(R.styleable.attrs_filtrate_attrs_filtrate_name);
        String total = array.getString(R.styleable.attrs_filtrate_attrs_filtrate_total);
        String one = array.getString(R.styleable.attrs_filtrate_attrs_filtrate_one);
        String two = array.getString(R.styleable.attrs_filtrate_attrs_filtrate_two);
        tvFiltName.setText(name);
        tvFiltTotal.setText(total);
        tvFiltOne.setText(one);
        tvFiltTwo.setText(two);
        array.recycle();
    }

    /**
     * 选中的返回出去
     *
     * @return
     */
    public int getSelected() {
        if (tvFiltOne.isChecked()) {
            return 1;//
        } else if (tvFiltTwo.isChecked()) {
            return 2;//最后面一项
        } else {
            return 0;//全部
        }
    }

    /**
     * 设置某些单项的选中
     *
     * @param flag
     */
    public void setSelected(int flag) {
        if (flag == 1) {
            tvFiltOne.setChecked(true);
        } else if (flag == 2) {
            tvFiltTwo.setChecked(true);
        } else {
            tvFiltTotal.setChecked(true);
        }
    }


    public RadioGroup getGroupView(){
        return group;
    }


    /**
     * 设置RadioGroup点击事件有效失效效果
     * @param clickable     true有效   false失效
     */
    public void setGroupClickable(Boolean clickable) {
        if (clickable) {
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.view_filtrate_rb0:
                            tvFiltTotal.setChecked(true);
                            break;
                        case R.id.view_filtrate_rb1:
                            tvFiltOne.setChecked(true);
                            break;
                        case R.id.view_filtrate_rb2:
                            tvFiltTwo.setChecked(true);
                            break;
                    }
                }
            });
        } else {
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    tvFiltTotal.setChecked(true);
                }
            });
        }
    }

}
