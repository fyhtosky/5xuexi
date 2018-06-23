package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/25.
 * 进度条
 */
public class ProgressBarView extends LinearLayout {

    Context context;
    ProgressBar bar;
    TextView barMark;
    public ProgressBarView(Context context) {
        super(context);
        init(context);
    }


    public ProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.context=context;
        View convertView= LayoutInflater.from(context).inflate(R.layout.view_progressbar,this);
        bar= (ProgressBar) convertView.findViewById(R.id.progressbar_bar);
        barMark= (TextView) convertView.findViewById(R.id.progressbar_mark);
    }


    public void setProgressAndMark(String progress){
            if( progress!=null|| !("".equals(progress) )){
                int a=transFormation(progress);
                barMark.setText(a+"%");
                bar.setProgress(a);
            }else{
                barMark.setText("0%");
                bar.setProgress(0);
            }
    }

    /**
     * 把字符串型百分数改变成into型整数
     * @param str  字符串型百分数
     * @return    整数
     */
    private int  transFormation(String str) {
        int a=0;
        Pattern p=Pattern.compile("(\\d+)");
        Matcher ma=p.matcher(str);
        if(ma.find()){
            a=Integer.valueOf(ma.group(1));
        }
        return  a;
    }

}
