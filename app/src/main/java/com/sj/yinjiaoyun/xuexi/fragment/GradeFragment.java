package com.sj.yinjiaoyun.xuexi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.CourseVO;
import com.sj.yinjiaoyun.xuexi.domain.ParseScore;
import com.sj.yinjiaoyun.xuexi.domain.Score;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.view.InfoView;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/29.
 * 视屏详情页  下面fragment对应  --成绩
 * 获取学生课程成绩 接口10 /api/v1/course/findScoreByCourseScheduleId.action
 */
public class GradeFragment extends Fragment {
    String TAG="fragmentgrade";
    ViewHolder holder;
    View convertView;
    CourseVO courseVO;//activity里面的课程信息


    long totalTime=0;//item项里面成绩的中总时长
    //课程表id
    private String courseScheduleId;
    //课程进度
    private  String coursePercent;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView=inflater.inflate(R.layout.fragment_grade,container,false);
        Log.i("fragment", "onCreateView: "+"GradeFragment");
        initView();
        return convertView;
    }

    /**
     * 从activity传递过来的数据
     * @param course 课程
     */
    public void setDateFromActivity(CourseVO course){
        this.courseVO =course;
      //  this.totalTime=totalTime;
        Log.i(TAG, "setDateFromActivity: 总的时长"+totalTime);
    }
    /**
     * 从activity传递过来的数据
     * @param coursePercent
     * @param courseScheduleId
     */
    public void setDateFromActivity(String coursePercent,String courseScheduleId ){
        this.coursePercent =coursePercent;
        this.courseScheduleId=courseScheduleId;
        //  this.totalTime=totalTime;
        Log.i(TAG, "setDateFromActivity: 总的时长"+totalTime);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(courseScheduleId!=null){
            getHttpDate();
        }
    }

    /**
     * 从activity传递过来的数据
     * @param totalTime 学习行为，看了多长时间
     */
    public void setTotalTimeFromActivity(long totalTime){
        this.totalTime=totalTime;
        Log.i(TAG, "setDateFromActivity: 总的时长"+totalTime);
        if(holder.cjShiChang!=null){
            holder.cjShiChang.setValues("学习时长",secToTime(totalTime));//课程时长
        }
        Log.i("cccc", "changeDate: "+totalTime);
    }




    //给各控件设置值
    private  void setValues(Score score){
        Log.i("dbintro", "setValues: "+score.toString());
        //赋值操作 学习进度
        coursePercent=score.getCoursePercent();
        if(coursePercent==null ||coursePercent.equals("")){
            holder.cjJinDu.setValues("学习进度","");//学习进度
        }else{
            holder.cjJinDu.setValues("学习进度", coursePercent);//学习进度
        }
         //赋值操作学习时长
        totalTime=score.getTotalPlayTime();
        if(totalTime==0){
            holder.cjShiChang.setValues("学习时长","0分0秒");
        }else{
            Log.i(TAG, "init: "+secToTime(totalTime));
            holder.cjShiChang.setValues("学习时长",secToTime(totalTime));//课程时长
        }
        holder.cjXingWei.setValues("学习行为成绩",score.getLearnBehaviorScore()+"");//学习行为成绩：
        holder.cjPingShi.setValues("平时成绩",score.getUsualScore()+"");//平时：
        holder.cjJieYe.setValues("结业成绩",score.getFinalScore()+"");//结业成绩：
        holder.cjZongChengJi.setValues("总成绩",score.getTotalScore()+"");//总成绩：
    }

    private void initView(){
        holder=new ViewHolder();
        //成绩
        holder.cjJinDu= (InfoView) convertView.findViewById(R.id.homeitem_chengJi_jindu);
        holder.cjShiChang= (InfoView)convertView. findViewById(R.id.homeitem_chengJi_shichang);
        holder.cjXingWei= (InfoView)convertView. findViewById(R.id.homeitem_chengJi_xingwei);
        holder.cjPingShi= (InfoView)convertView. findViewById(R.id.homeitem_chengJi_pingshi);
        holder.cjJieYe= (InfoView) convertView.findViewById(R.id.homeitem_chengJi_jieye);
        holder.cjZongChengJi= (InfoView)convertView. findViewById(R.id.homeitem_chengJi_zongchengji);
//        if(totalTime==0){
//            holder.cjShiChang.setValues("学习时长","0分0秒");
//        }else{
//            Log.i(TAG, "init: "+secToTime(totalTime));
//            holder.cjShiChang.setValues("学习时长",secToTime(totalTime));//课程时长
//        }
//        Log.i("cccc", "Grade分数:布局初始化 "+totalTime);
//        if(coursePercent==null ||coursePercent.equals("")){
//            holder.cjJinDu.setValues("学习进度","");//学习进度
//        }else{
//            holder.cjJinDu.setValues("学习进度", coursePercent);//学习进度
//        }
    }
    /**
     * 获取课程信息
     * courseScheduleId 课程
     */
    public void getHttpDate(){
        String url= MyConfig.getURl("course/findScoreByCourseScheduleId")+"?id="+courseScheduleId;
        HttpClient.get(this, url, new CallBack<ParseScore>() {
            @Override
            public void onSuccess(ParseScore result) {
                  if(result==null){
                      return;
                  }
                if(result.getSuccess()){
                    if(result.getData().getScore()!=null){
                        setValues(result.getData().getScore());
                    }
                }
            }
        });
    }

    class ViewHolder{
        InfoView cjJinDu;//学习进度
        InfoView cjShiChang;//学习时长
        InfoView cjXingWei;//学习行为成绩
        InfoView cjPingShi;//平时成绩
        InfoView cjJieYe;//结业成绩
        InfoView cjZongChengJi;//总成绩
    }

    //把秒转化为分钟
    public  String secToTime(Long time) {
        String timeStr;
        Long hour ;
        Long minute ;
        Long second ;
        if (time <= 0)
            return "0";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second)+"秒";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second)+"秒";
            }
        }
        Log.i(TAG, "secToTime: "+timeStr);
        return timeStr;
    }
    private String unitFormat(Long i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
