package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/30.
 * 作业导向页面 -  单元格 +边框
 */
public class CellView  extends RelativeLayout{

    Context context;
    ImageView ivLeft;
    ImageView ivRight;
    ImageView ivTop;
    ImageView ivBottom;
    TextView tvText;
    public CellView(Context context) {
        super(context);
        init(context);
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        getValues(attrs);
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        getValues(attrs);
    }

    //初始化控件
    private void init(Context context){
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.view_cell,this);
        ivTop= (ImageView) view.findViewById(R.id.view_cell_top);
        ivBottom= (ImageView) view.findViewById(R.id.view_cell_bottom);
        ivLeft= (ImageView) view.findViewById(R.id.view_cell_left);
        ivRight= (ImageView) view.findViewById(R.id.view_cell_right);
        tvText= (TextView) view.findViewById(R.id.view_cell_text);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tvText.setFocusable(true);
            }
        });
    }


    public void getValues(AttributeSet attrs){
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.attrs_cell);
        Boolean top=array.getBoolean(R.styleable.attrs_cell_attr_cell_top,false);
        if(top){//顶线是否隐藏
            ivTop.setVisibility(GONE);
        }
        Boolean bottom=array.getBoolean(R.styleable.attrs_cell_attr_cell_bottom,false);
        if(bottom){
            ivBottom.setVisibility(GONE);
        }else{
            ivBottom.setVisibility(VISIBLE);
        }
        Boolean left=array.getBoolean(R.styleable.attrs_cell_attr_cell_left,false);
        if(left){
            ivLeft.setVisibility(GONE);
        }
        Boolean right=array.getBoolean(R.styleable.attrs_cell_attr_cell_right,false);
        if(right){
            ivRight.setVisibility(GONE);
        }
        Boolean gravity=array.getBoolean(R.styleable.attrs_cell_attr_cell_gravity,false);
        if(gravity){
            tvText.setGravity(Gravity.CENTER);
        }else{
            tvText.setGravity(Gravity.CENTER_VERTICAL);
        }
        Boolean isMoreLines=array.getBoolean(R.styleable.attrs_cell_attr_cell_singleLines,false);
        if (!isMoreLines){
            tvText.setSingleLine();
        }
        float size=array.getDimension(R.styleable.attrs_cell_attr_cell_size,14f);
        tvText.setTextSize(size);
        String text=array.getString(R.styleable.attrs_cell_attr_cell_text);
        tvText.setText(text);
        int color=array.getColor(R.styleable.attrs_cell_attr_cell_color,getResources().getColor(R.color.colorHomeitem));
        tvText.setTextColor(color);
        array.recycle();
    }

    public void setTvText(String text) {
        tvText.setText(text);
    }

}
