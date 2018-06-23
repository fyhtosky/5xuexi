package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/28.
 * 专业.公开课.微专业 ，详情 目录列表 adapter的item
 */
public class CourseItemView extends LinearLayout{

    ImageView ivIcon;//最左边图标
    TextView tvMark;//左边数字
    ImageView ivJob;//是否有作业
    ImageView ivPull;//弹出下拉框
    TextView tvName;//名字
    Context context;

    public CourseItemView(Context context) {
        super(context);
        init(context);
    }

    public CourseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CourseItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //初始化控件
    private void init(Context context){
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.view_courseitem,this);
        ivIcon= (ImageView) view.findViewById(R.id.view_course_icon);
        ivPull= (ImageView) view.findViewById(R.id.view_course_pull);
        tvMark= (TextView) view.findViewById(R.id.view_course_mark);
        ivJob= (ImageView) view.findViewById(R.id.view_course_job);
        tvName= (TextView) view.findViewById(R.id.view_course_name);
    }

    public void setValueToName(String name){
        tvName.setText(name);
    }



    /**
     *
     * @param flag  1已完成  2正在  0未开始
     * @param text
     */
    public void setValueToIcon(int flag,String text){
        tvMark.setText(text);
        Drawable drawable = null;
        switch(flag){
            case 2://已学完
                drawable=context.getResources().getDrawable(R.mipmap.finished);
                break;
            case 1://正在学
                drawable=context.getResources().getDrawable(R.mipmap.finishing);
                break;
            case 0://未开始学
                drawable=context.getResources().getDrawable(R.mipmap.unfinished);
                break;
        }
        if(drawable!=null)
        ivIcon.setImageDrawable(drawable);
    }

    /**
     *
     * @param homeworkState  完成作业状态  0：没有作业  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期
     * @param testState      完成测验状态  0：没有测验  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期
     */
    public void setValueToJob(Byte homeworkState,Byte testState){
        Drawable drawable;
                if(homeworkState==1 || testState==1){//1：未做
                    drawable=context.getResources().getDrawable(R.mipmap.xiangqing_mulv_kehouzuoye_2);
                    ivJob.setImageDrawable(drawable);
                }else if(homeworkState==2 || testState==2){//2：已做未批改
                   drawable=context.getResources().getDrawable(R.mipmap.xiangqing_mulv_kehouzuoye_1);
                    ivJob.setImageDrawable(drawable);
                }else if(homeworkState==3 || testState==3){//3、已做已批改
                   drawable=context.getResources().getDrawable(R.mipmap.xiangqing_mulv_kehouzuoye_1);
                    ivJob.setImageDrawable(drawable);
                }else if(homeworkState==4 || testState==4){//4：尚未开始考试
                   drawable = context.getResources().getDrawable(R.mipmap.xiangqing_mulv_kehouzuoye_1);
                    ivJob.setImageDrawable(drawable);
                }else if(homeworkState==5 || testState==5){// 5：已过期
                   drawable=context.getResources().getDrawable(R.mipmap.xiangqing_mulv_kehouzuoye_1);
                    ivJob.setImageDrawable(drawable);
                }

    }


    public ImageView getIvPull() {
        return ivPull;
    }



}
