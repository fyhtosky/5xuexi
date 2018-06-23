package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/23.
 * 标题栏 （类似状态栏）
 */
public class TitleBarView extends RelativeLayout{

    ImageView ivLeft;
    TextView tvCenter;
    TextView tvRight;
    ImageView ivRight;
    Context context;
    View view;
    public TitleBarView(Context context) {
        super(context);
        init(context);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        getValues(attrs);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        getValues(attrs);
    }

    private void init(final Context context){
        this.context=context;
        view= LayoutInflater.from(context).inflate(R.layout.view_titlebar,this);
        ivLeft= (ImageView) view.findViewById(R.id.view_titlebar_left);
        ivRight= (ImageView) view.findViewById(R.id.view_titlebar_rightImg);
        tvCenter= (TextView) view.findViewById(R.id.view_titlebar_center);
        tvRight= (TextView) view.findViewById(R.id.view_titlebar_rightText);
    }

    private void getValues(AttributeSet attrs){
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.attrs_titlebar);
        Drawable backImage=array.getDrawable(R.styleable.attrs_titlebar_attr_titlebar_back);
        Drawable img=array.getDrawable(R.styleable.attrs_titlebar_attr_titlebar_img);
        ivLeft.setImageDrawable(backImage);
        ivRight.setImageDrawable(img);
        String title=array.getString(R.styleable.attrs_titlebar_attr_titlebar_title);
        String mark=array.getString(R.styleable.attrs_titlebar_attr_titlebar_mark);
        tvCenter.setText(title);
        tvRight.setText(mark);
        array.recycle();
    }

    public ImageView getBackImageView(){
        return ivLeft;
    }

    public ImageView getRightImageView(){
        return ivRight;
    }

    public TextView getRightTextView(){
        return tvRight;
    }

    public TextView getRightTitleView(){
        return tvCenter;
    }


}
