package com.sj.yinjiaoyun.xuexi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.CouseInfo;
import com.sj.yinjiaoyun.xuexi.domain.MajorCourse;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParseCourseInfoDate;
import com.sj.yinjiaoyun.xuexi.domain.ParsenCourseInfo;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/29.
 * 视屏详情页  下面fragment对应  --简介
 * 接口7   /api/v1//course/findCourseInfoByCourseScheduleId.action
 */
public class IntroFragment extends Fragment implements HttpDemo.HttpCallBack{

    String TAG="fragmentintro";
    ViewHolder holder;
    View convertView;
    HttpDemo demo;
    String id;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: "+"IntroFragment");
        convertView=inflater.inflate(R.layout.fragment_intro,container,false);
        initView();
        getHttpDate();
        return convertView;
    }

    //从activity传递过来的数据
    public void setDateFromActivity(String id){
        this.id=id;
    }



    /**
     * 获取课程信息
     * @param  //选课表ID
     */
    private void getHttpDate(){
        demo=new HttpDemo(this);
        Log.i(TAG, "接口7-获取课程信息: "+id);
        String url= MyConfig.getURl("course/findCourseInfoByCourseScheduleId");
        List<Pairs> pairsList=new ArrayList<>();
        pairsList.add(new Pairs("id",id));
        demo.doHttpGet(url,pairsList, MyConfig.CODE_GET_COURSEINFO);
    }


    //网络请求返回数据
    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        try{
            Gson gson=new Gson();
            ParsenCourseInfo parsenCourseInfo =gson.fromJson(msg, ParsenCourseInfo.class);
            ParseCourseInfoDate parseDateCourseInfo = parsenCourseInfo.getData();
            CouseInfo couseInfo=parseDateCourseInfo.getCourseInfo();
            MajorCourse majorCourse=couseInfo.getMajorCourse();
            if(couseInfo !=null){
                holder.jjName.setText(majorCourse.getCourseName());
                holder.jjXueFen.setText("学分：" + majorCourse.getCourseCredit());
                holder.jjJiangShi.setText("讲师：" + majorCourse.getTeacherName());
                holder.jjText.setText(Html.fromHtml(majorCourse.getCourseDesc()));
                try {
                    Log.i(TAG, "setMsg: 1111" + majorCourse.getCourseDesc());
                    holder.jjText.setText(Html.fromHtml(majorCourse.getCourseDesc()));
                    if (majorCourse.getCourseLogoUrl().equals("")) {
                        Picasso.with(context).load(R.mipmap.error).into(holder.jjImage);
                    } else {
                        Picasso.with(context)
                                .load(majorCourse.getCourseLogoUrl())
                                //加载过程中的图片显示
                                .placeholder(R.drawable.progressbar_landing)
                                //加载失败中的图片显示
                                .error(R.mipmap.error)
                                //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                                .centerCrop()
                                .resize(500, 315).into(holder.jjImage);
                    }
                } catch (Exception e) {
                    Log.i(TAG, "setMsg:---- " + e.toString());
                }
                try {
                    if (majorCourse.getTheFewYear() == null) {
                        holder.jjXueNian.setText("");
                    } else {
                        holder.jjXueNian.setText("第" + majorCourse.getTheFewYear() + "学期");
                    }
                    if (majorCourse.getCourseAttribute() == null) {
                        holder.jjLeiXing.setText("");
                    } else {
                        Map<String, String> map = MyConfig.couseleiXing();
                        Log.i(TAG, "setMsg: " + majorCourse.getCourseAttribute());
                        holder.jjLeiXing.setText(map.get(String.valueOf(majorCourse.getCourseAttribute())));
                    }
                } catch (Exception e) {
                    Log.i(TAG, "用户信息异常: ");
                }
            }
        }catch (Exception e){
              e.getLocalizedMessage();
        }
    }


    private void initView(){
        holder=new ViewHolder();
        //简介
        holder.jjImage= (ImageView) convertView.findViewById(R.id.intro_JianJie_image);
        holder.jjName= (TextView) convertView.findViewById(R.id.intro_JianJie_name);
        holder.jjXueFen= (TextView) convertView.findViewById(R.id.intro_JianJie_xuefen);
        holder.jjXueNian= (TextView) convertView.findViewById(R.id.intro_JianJie_xuenian);
        holder.jjJiangShi= (TextView) convertView.findViewById(R.id.intro_JianJie_Jiangshi);
        holder.jjLeiXing= (TextView) convertView.findViewById(R.id.intro_JianJie_leixing);
        holder.jjText= (TextView) convertView.findViewById(R.id.intro_JianJie_text);
    }

    class ViewHolder{
        ImageView jjImage;//简介图片
        TextView jjName;//课程名字
        TextView jjXueFen;//课程学分
        TextView jjXueNian;//课程学年
        TextView jjJiangShi;//课程讲师
        TextView jjLeiXing;//课程类型
        TextView jjText;//课程名字
    }

}