package com.sj.yinjiaoyun.xuexi.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.ChooseTimuEvent;
import com.sj.yinjiaoyun.xuexi.Event.SubmitAnswerEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.callback.OnTimeCountFinishListener;
import com.sj.yinjiaoyun.xuexi.domain.JobsPaper;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.fragment.JobClozeFragment;
import com.sj.yinjiaoyun.xuexi.fragment.JobSubjectiveFragment;
import com.sj.yinjiaoyun.xuexi.fragment.JobsMoreFragment;
import com.sj.yinjiaoyun.xuexi.fragment.JobsSingleFragment;
import com.sj.yinjiaoyun.xuexi.fragment.WaitLeadingFragment;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.ArrayOrJsonUtil;
import com.sj.yinjiaoyun.xuexi.utils.TimeCount;
import com.sj.yinjiaoyun.xuexi.utils.TimeCutTamp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/29.
 * 作业 测试 作答页面
 */
public class JobActivity extends MyBaseActivity implements HttpDemo.HttpCallBack {

    String TAG="jobpaper";
    TextView tvTime;//倒计时
    TextView tvdatika;//答题卡
    TextView tvTiJiao;//提交
    TextView tvZuoda;//做答报告
    TextView tvPage;//页数

    JobsPaper jobsPaper;//封装类
    String jobsJson;
    List<TiMu> tiMuList;//题目集合
    int totalPager;//总题目数
    int countPager=1;//当前题目
    HttpDemo demo;

    Intent intent;//m
    String examPaperReleaseId;//考试或者测试id
    String courseScheduleId;// 课程id 554
    TiMu tm;

    FragmentManager manager;
    FragmentTransaction tran;
    JobsSingleFragment jobsSingleFragment;//单选
    JobsMoreFragment jobsMoreFragment;//多选
    JobSubjectiveFragment jobSubjectiveFragment;//主观题
    JobClozeFragment jobClozeFragment;//完型填空
    String startTime;//开始考试的时间
    public static Map<String,Object> answerMap;//存储答案的集合
    public static Map<String, Object> getAnswerMap() {
        return answerMap;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jobs);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //注册EventBus
        EventBus.getDefault().register(this);
        try {
            init();
            initView();
            initEvent(jobsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化控件
    private void initView() {
        tvdatika= (TextView) findViewById(R.id.jobs_datika);
        tvTiJiao= (TextView) findViewById(R.id.jobs_tijiao);
        tvZuoda= (TextView) findViewById(R.id.jobs_zuodabaogaoo);
        tvTime= (TextView) findViewById(R.id.jobs_shijian);
        tvPage= (TextView) findViewById(R.id.jobs_page);
        manager=getFragmentManager();
        WaitLeadingFragment waitLeadingFragment=new WaitLeadingFragment();
        replaceFragment(waitLeadingFragment);

    }

    private void initEvent(String jobsJson) {
        Log.i(TAG, "initEvent: "+jobsJson);
        Gson gson=new Gson();
        jobsPaper=gson.fromJson(jobsJson,JobsPaper.class);
        tiMuList=jobsPaper.getList();
        for (int i=0;i<tiMuList.size();i++){
            Logger.d("所有题目的数据,题目的下标:"+tiMuList.get(i).getIndex()+"题目切换的序号："+tiMuList.get(i).getQid());
        }
        totalPager=jobsPaper.getTimuSize();
        Log.i(TAG, "initEvent: 题目数"+totalPager);
        if(tiMuList==null){
            return;
        }
        tvPage.setText("1/"+totalPager);
        Log.i(TAG, "init: 封装好的"+jobsPaper.toString());
        if(tm==null){
            tm=tiMuList.get(countPager-1);
        }
        if(jobsPaper.getJobState()==1){
            tvTiJiao.setVisibility(View.VISIBLE);
            tvdatika.setVisibility(View.VISIBLE);
            tvZuoda.setVisibility(View.GONE);
            if(jobsPaper.getIsLimitFinishTime()==1){
                tvTime.setVisibility(View.VISIBLE);
                if(jobsPaper.getFinishTime()!=null){
                    setCheckBoxIsTrue(jobsPaper.getFinishTime()*60);
                }
            }
        }else{
            tvTiJiao.setVisibility(View.GONE);
            tvdatika.setVisibility(View.GONE);
            tvZuoda.setVisibility(View.VISIBLE);
        }
        flip(tm);
    }

    //数据前准备
    private void init() {
        answerMap=new HashMap<>();
        startTime= TimeCutTamp.getCurrentTime();
        intent=getIntent();
        jobsJson=intent.getStringExtra("JobsPaper");
        courseScheduleId=intent.getStringExtra("CourseScheduleId");
        examPaperReleaseId=intent.getStringExtra("ExamPaperReleaseId");
        tm=intent.getParcelableExtra("TiMu");
        Log.i(TAG, "init: "+"CourseScheduleId:"+courseScheduleId+" ExamPaperReleaseId:"+examPaperReleaseId);
        Log.i(TAG, "init: "+jobsJson);

    }
    public void onclick(View v){
        switch(v.getId()){
            case R.id.jobs_back://返回
                finish();
                break;
            case R.id.jobs_shijian://倒计时(无点击时间)
                break;
            case R.id.jobs_tijiao://提交
                initAnswerTiJiao();
                break;
            case R.id.jobs_zuodabaogaoo://做答报告
                initAnswerReport();
                break;
            case R.id.jobs_datika://答题卡
                daTiKaDialog();
                break;
            case R.id.jobs_bottom_left://向左翻页
                if(countPager>1){// =1时为第一题
                    countPager=countPager-1;
                    tvPage.setText(countPager+"/"+totalPager);
                    tm=tiMuList.get(countPager-1);
                    Log.i(TAG, "onclick: "+"左划 "+"划后当前页面"+countPager);
                    flip(tm);
                }
                break;
            case R.id.jobs_bottom_right://向右翻页
                if(countPager<totalPager){
                    countPager=countPager+1;
                    tm=tiMuList.get(countPager-1);
                    Log.i(TAG, "onclick: "+"右划 "+"划后当前页面"+countPager);
                    flip(tm);
                }
                break;
        }
    }


    /**
     * 翻页操作
     * @param tm
     */
    private void flip(TiMu tm){
        Log.i(TAG, "flip: "+tm.toString());
        countPager=tm.getQid();
        tvPage.setText(countPager+"/"+totalPager);
        switch (tm.getQuestionType()){
            case 1://单选
                Log.i(TAG, "flip: "+"单选");
                jobsSingleFragment=new JobsSingleFragment();
                jobsSingleFragment.setTiMuFromJobs(tm);
                replaceFragment(jobsSingleFragment);
                break;
            case 2://多选
                Log.i(TAG, "flip: "+"多选");
                jobsMoreFragment=new JobsMoreFragment();
                jobsMoreFragment.setTiMuFromJobs(tm);
                replaceFragment(jobsMoreFragment);
                break;
            case 3://判断题
                Log.i(TAG, "flip: "+"判断");
                jobsSingleFragment=new JobsSingleFragment();
                jobsSingleFragment.setTiMuFromJobs(tm);
                replaceFragment(jobsSingleFragment);
                break;
            case 4://主观题
                Log.i(TAG, "flip: "+"主观题");
                jobSubjectiveFragment=new JobSubjectiveFragment();
                jobSubjectiveFragment.setTiMuFromJobs(tm);
                replaceFragment(jobSubjectiveFragment);
                break;
            case 5://完型填空
                Log.i(TAG, "flip: "+"完型填空");
//                jobClozeFragment=new JobClozeFragment();
//                jobClozeFragment.setTiMuFromJobs(tm);
                jobClozeFragment=JobClozeFragment.newInstance(tm);
                replaceFragment(jobClozeFragment);
                break;
            case 6://阅读理解
                Log.i(TAG, "flip: "+"阅读理解");
//                jobClozeFragment=new JobClozeFragment();
//                jobClozeFragment.setTiMuFromJobs(tm);
                jobClozeFragment=JobClozeFragment.newInstance(tm);
                replaceFragment(jobClozeFragment);
                break;
        }
    }


    /**
     * 答题卡界面选择状态显示对应的界面
     * @param timuEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChooseTimuEvent timuEvent){
        TiMu tiMu=timuEvent.getTimu();
        Logger.d("接受到选择的状态:"+tiMu.toString());
        flip(tiMu);

    }

    /**
     * 答题卡界面中点击提交试卷
     * @param answerEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SubmitAnswerEvent answerEvent){
        initAnswerTiJiao();
    }
    /**
     * 设置答题卡弹框  及其事件
     */
    private void daTiKaDialog(){
        //提交前要先提交当担题目的答案
        if(jobsSingleFragment!=null && jobsSingleFragment.isVisible()){//正在显示单选
            jobsSingleFragment.saveLibrary();
        }else if(jobsMoreFragment!=null && jobsMoreFragment.isVisible()){//正在显示多选
            jobsMoreFragment.saveLibrary();
        }else if(jobSubjectiveFragment!=null && jobSubjectiveFragment.isVisible()){//正在显示主观题
            Log.i(TAG, "initAnswerTiJiao: "+"主观");
            jobSubjectiveFragment.saveLibrary();
        }else if(jobClozeFragment!=null && jobClozeFragment.isVisible()){//正在显示阅读理解和完型填空
            jobClozeFragment.saveLibrary();
        }
        AnswerSheetActivity.StartActivity(JobActivity.this,jobsJson);
    }


    //做答报告
    private void initAnswerReport(){
        Log.i(TAG, "answerTiJiao: "+"直接跳转");
        Intent intent=new Intent(this,AnswerReportActivity.class);
        intent.putExtra("CourseScheduleId",courseScheduleId);
        intent.putExtra("ExamPaperReleaseId",examPaperReleaseId);
        Log.i(TAG, "answerTiJiao: "+jobsPaper.getJobState()+":"+":"+courseScheduleId+"："+examPaperReleaseId);
        startActivity(intent);
        finish();
    }

    /**
     * 作业 答案 提交前数据准备
     */
    private void initAnswerTiJiao(){
        //提交前要先提交当担题目的答案
        if(jobsSingleFragment!=null && jobsSingleFragment.isVisible()){//正在显示单选
            jobsSingleFragment.saveLibrary();
        }else if(jobsMoreFragment!=null && jobsMoreFragment.isVisible()){//正在显示多选
            jobsMoreFragment.saveLibrary();
        }else if(jobSubjectiveFragment!=null && jobSubjectiveFragment.isVisible()){//正在显示主观题
            Log.i(TAG, "initAnswerTiJiao: "+"主观");
            jobSubjectiveFragment.saveLibrary();
        }else if(jobClozeFragment!=null && jobClozeFragment.isVisible()){//正在显示阅读理解和完型填空
            jobClozeFragment.saveLibrary();
        }
        if(jobsPaper.getJobState()!=1){//非等于1的时候不可提交
            Log.i(TAG, "answerTiJiao: "+"直接跳转");
            Intent intent=new Intent(this,AnswerReportActivity.class);
            intent.putExtra("CourseScheduleId",courseScheduleId);
            intent.putExtra("ExamPaperReleaseId",examPaperReleaseId);
            Log.i(TAG, "answerTiJiao: "+jobsPaper.getJobState()+":"+":"+courseScheduleId+"："+examPaperReleaseId);
            startActivity(intent);
        }else{//学生作答的时候，可提交
            Log.i(TAG, "answerTiJiao: 状态为正确学生正常提交："+"提交数"+answerMap.size()+" 总题数:"+totalPager);
            String hint="请确认是否提交！";
            if(answerMap.size()<totalPager){//
                hint="您还有未作答的题目，请确认后提交";
            }else{
                for (Map.Entry<String, Object> entry : answerMap.entrySet()) {
                   Log.i(TAG, "initAnswerTiJiao: "+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    if(entry.getValue()==null){
                        hint="您还有未作答的题目，请确认后提交";
                    }
                }
            }
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage(hint);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                 sendAnswerToHttp();
                }
            });
            builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            builder.create().show();
        }
    }

    /**
     * 答案正式提交到网络
     */
    private void sendAnswerToHttp(){
        demo=new HttpDemo(this);
        List<Pairs> pairsList=new ArrayList<>();
        String url= MyConfig.getURl("learn/grade");
        pairsList.add(new Pairs("courseScheduleId",jobsPaper.getCourseScheduleId()));//课程表id
        pairsList.add(new Pairs("examPaperReleaseId",jobsPaper.getExamPaperReleaseId()));//发布试卷id
        pairsList.add(new Pairs("endUserId",jobsPaper.getEndUserId()));//用户id
        pairsList.add(new Pairs("examPaperReleaseType",jobsPaper.getExamPaperReleaseType()+""));//试卷类型
        pairsList.add(new Pairs("examPaperId",jobsPaper.getExamPaperId()));//试卷ID
        pairsList.add(new Pairs("startTime",startTime));//开始考试时间
        String strMap= ArrayOrJsonUtil.mapToJson(answerMap);
        Log.i(TAG, "sendAnswerToHttp: "+strMap);
        pairsList.add(new Pairs("answers",strMap));//学员答案 Map<ExamPaperItemId,答案>
        demo.doHttpPost(url,pairsList,100);
    }


    //切换题目信息fragment
    private void replaceFragment(Fragment fragment){
        tran=manager.beginTransaction();
        tran.replace(R.id.jobs_fragment,fragment);
        tran.commitAllowingStateLoss();
    }


    /**
     * 设置获取验证码按钮TextView 倒计时
     * @param second 秒
     */
    private void setCheckBoxIsTrue(long second){
        if(second!=0){
            TimeCount time=new TimeCount(this,second*1000,1000, tvTime);
            time.setOnTimeCountFinishListener(new OnTimeCountFinishListener() {
                @Override
                public void onfinish() {
                    AlertDialog.Builder builder=new AlertDialog.Builder(JobActivity.this);
                    builder.setTitle("提示");
                    builder.setCancelable(false);
                    builder.setMessage("作答时间已到，即将提交试卷");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //提交前要先提交当担题目的答案
                            if(jobsSingleFragment!=null && jobsSingleFragment.isVisible()){//正在显示单选
                                jobsSingleFragment.saveLibrary();
                            }else if(jobsMoreFragment!=null && jobsMoreFragment.isVisible()){//正在显示多选
                                jobsMoreFragment.saveLibrary();
                            }else if(jobSubjectiveFragment!=null && jobSubjectiveFragment.isVisible()){//正在显示主观题
                                jobSubjectiveFragment.saveLibrary();
                            }else if(jobClozeFragment!=null && jobClozeFragment.isVisible()){//正在显示阅读理解和完型填空
                                jobClozeFragment.saveLibrary();
                            }
                            sendAnswerToHttp();
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
            time.start();
        }else{
            tvTime.setText("不限时");
        }
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: 发送答案成功"+msg);
        try {
            JSONObject object=new JSONObject(msg);
            if(object.getBoolean("success")){
                Intent intent=new Intent(this,AnswerReportActivity.class);
                intent.putExtra("CourseScheduleId",courseScheduleId);
                intent.putExtra("ExamPaperReleaseId",examPaperReleaseId);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }
}
