package com.sj.yinjiaoyun.xuexi.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.CouseInfo;
import com.sj.yinjiaoyun.xuexi.domain.MajorCourse;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParseCourseInfoDate;
import com.sj.yinjiaoyun.xuexi.domain.ParsenCourseInfo;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/9.
 * 微专业学习页面   简介
 */
public class MicroIntroFragment extends Fragment implements HttpDemo.HttpCallBack{

    private  final String TAG = "microintrofragment";
    String id;
    String collegeName;

    TextView tvCouseName;
    TextView tvCollege;
    TextView tvIntro;
    TextView tvTeacher;

    List<Pairs> pairsList;
    HttpDemo demo;
    Activity activity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microintro, container,false);
        initView(view);
        getHttpDate();
        return view;
    }

    private void initView(View view) {
        demo=new HttpDemo(this);
        tvCouseName = (TextView) view.findViewById(R.id.micro_intro_name);
        tvCollege = (TextView) view.findViewById(R.id.micro_intro_college);
        tvIntro = (TextView) view.findViewById(R.id.micro_intro_intro);
        tvTeacher= (TextView) view.findViewById(R.id.micro_intro_teacher);
    }

    /**
     * 从activity传递过来的数据
     *
     * @param id     课程表id
     * @param collegeName     学校名称
     */
    public void setDateFromActivity(String id,String collegeName){
        this.id=id;
        this.collegeName=collegeName;
    }

    private void getHttpDate(){
        pairsList=new ArrayList<>();
        Log.i(TAG, "接口7-获取课程信息: "+id);
        String url= MyConfig.getURl("course/findCourseInfoByCourseScheduleId");
        pairsList.add(new Pairs("id",id));
        demo.doHttpGet(url,pairsList, 0);
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        try{
            Gson gson=new Gson();
            ParsenCourseInfo parsenCourseInfo =gson.fromJson(msg, ParsenCourseInfo.class);
            ParseCourseInfoDate parseDateCourseInfo = parsenCourseInfo.getData();
            CouseInfo couseInfo=parseDateCourseInfo.getCourseInfo();
            MajorCourse majorCourse=couseInfo.getMajorCourse();
            tvCouseName.setText(majorCourse.getCourseName());
            if(!TextUtils.isEmpty(majorCourse.getTeacherName())){
                tvTeacher.setText("授课老师："+majorCourse.getTeacherName());
            }
            tvCollege.setText(collegeName);
            tvIntro.setText(Html.fromHtml(majorCourse.getCourseDesc()));
        }catch (Exception e){
            Log.i(TAG, "setMsg: "+e.toString());
        }
    }
}
