package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.db.DbOperatorLogin;
import com.sj.yinjiaoyun.xuexi.domain.ExamPaper;
import com.sj.yinjiaoyun.xuexi.domain.ExamPaperVO;
import com.sj.yinjiaoyun.xuexi.domain.JobsPaper;
import com.sj.yinjiaoyun.xuexi.domain.LoginInfo;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParserExamPaper;
import com.sj.yinjiaoyun.xuexi.domain.ParserExamPaperData;
import com.sj.yinjiaoyun.xuexi.domain.Question;
import com.sj.yinjiaoyun.xuexi.domain.SoaExamPaper;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.ArrayOrJsonUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.TimeUtil;
import com.sj.yinjiaoyun.xuexi.view.CellView;
import com.sj.yinjiaoyun.xuexi.view.NewCheckBox;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/29.
 * 考试 作业考试 提示开始页面提示
 */
public class JobHintActivity extends MyBaseActivity implements HttpDemo.HttpCallBack{
    String TAG="JobHint";

    TitleBarView titleBarView;
    NewCheckBox btn;
    CellView cvCollegeName;
    CellView cvCourseName;
    CellView cvExamTime;
    CellView cvZhangJie;
    CellView cvLeiX;
    CellView cvKeS;
    CellView cvTotal;
    CellView cvTime;//时间段
    TextView tvHint;//提示框
    View scrollView;

    Intent intent;
    int groupPosition;//章节
    int childPosition;//课时
    String examPaperReleaseId;//考试或者测试id
    String courseScheduleId;// 课程id 554
    HttpDemo demo;
    List<Pairs> pairsList;
    List<TiMu> tiMuList;//总的题目集合
    String endUserId;//=String.valueOf(3427);//用户id
    JobsPaper jobsPaper;

    Boolean isOnclick=false;
    //单选题目的集合
    private List<TiMu> singleList = new ArrayList<>();
    //多选题目的集合
    private List<TiMu> moreList = new ArrayList<>();
    //判断题目的集合
    private List<TiMu> giveList = new ArrayList<>();
    //主观题目的集合
    private List<TiMu> subjectiveList = new ArrayList<>();
    //完型填空题目的集合
    private List<TiMu> clozeList = new ArrayList<>();
    //阅读理解的题目集合
    private List<TiMu> partList = new ArrayList<>();
    private List<String> qid= Arrays.asList("一","二","三","四","五","六");
    //考试时间
    private Integer finishTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hint);
        initView();
        initEvent();
        getHttpDate();
    }

    private void initView(){
        endUserId = PreferencesUtils.getSharePreStr(this,"username");
        intent=getIntent();
        groupPosition=intent.getIntExtra("groupPosition",0);
        childPosition=intent.getIntExtra("childPosition",0);
        courseScheduleId=intent.getStringExtra("CourseScheduleId");
        examPaperReleaseId=intent.getStringExtra("ExamPaperReleaseId");
        Log.i(TAG, "onCreate: "+"试卷/作业状态jobState"+"课程表CourseScheduleId"+courseScheduleId+" 试卷/作业examPaperReleaseId "+examPaperReleaseId+" 用户id"+endUserId);
        titleBarView= (TitleBarView) findViewById(R.id.exam_title);
    }

    private void initEvent() {
        cvTime= (CellView) findViewById(R.id.cell_time);
        cvCollegeName= (CellView) findViewById(R.id.cell_colllegeName);
        cvCourseName= (CellView) findViewById(R.id.cell_courseName);
        cvExamTime= (CellView) findViewById(R.id.cell_examTime);
        cvZhangJie= (CellView) findViewById(R.id.cell_zhangjie);
        cvLeiX= (CellView) findViewById(R.id.cell_examLx);
        cvKeS= (CellView) findViewById(R.id.cell_keshi);
        cvTotal= (CellView) findViewById(R.id.cell_grade);
        tvHint= (TextView) findViewById(R.id.cell_hint);
        btn= (NewCheckBox) findViewById(R.id.cell_start);
        titleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setChecked(false);
        tvHint.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnclick){
                    Log.i(TAG, "onClick: "+"点击"+isOnclick);
                    Intent inten=new Intent(JobHintActivity.this,JobActivity.class);
                    inten.putExtra("JobsPaper", ArrayOrJsonUtil.toJson(jobsPaper));
                    inten.putExtra("CourseScheduleId",courseScheduleId);
                    inten.putExtra("ExamPaperReleaseId",examPaperReleaseId);
                    inten.putExtra("EndUserId",endUserId);
                    startActivity(inten);
                    finish();
                }else{
                    //Toast.makeText(JobHintActivity.this,message,Toast.LENGTH_SHORT).show();
                    tvHint.setText(message);
                    tvHint.setVisibility(View.VISIBLE);
                }
            }
        });
    }



    //获取网络数据
    private void getHttpDate(){
        pairsList=new ArrayList<>();
        demo=new HttpDemo(this);
        String url= MyConfig.getURl("learn/test");//作业考试
        pairsList.add(new Pairs("courseScheduleId",String.valueOf(courseScheduleId)));
        pairsList.add(new Pairs("examPaperReleaseId",String.valueOf(examPaperReleaseId)));
        pairsList.add(new Pairs("userId",endUserId));
        demo.doHttpGetLoading(this,url,pairsList,0);
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        try {
            Gson gson = new Gson();
            ParserExamPaper parserExamPaper = gson.fromJson(msg, ParserExamPaper.class);
            ParserExamPaperData data = parserExamPaper.getData();
            ExamPaper examPaper = data.getExamPaper();
            finishTime=examPaper.getFinishTime();
            judgeState(examPaper);
        }catch (Exception e){
            Log.e(TAG, "setMsg: "+e.toString());
        }
    }

    StringBuilder sb;
    String examType="";//考试类型
    //根据状态判断是否进入题目界面
    private void judgeState(ExamPaper examPaper) throws Exception{
        if(examPaper==null){
            return;
        }
        if(examPaper.getType()==1){//作业
            examType="作业";
        }else if(examPaper.getType()==2){//考试
            examType="测试";
        }
        cvTime.setTvText(TimeUtil.getTimeYear(examPaper.getStartTime())+" - "+ TimeUtil.getTimeYear(examPaper.getEndTime()));
        // 完成状态 0：没有测验  1：未做   2：已做未批改  3、已做已批改;   [ 2：已做未批改  3、已做已批改]进入到成绩页面
        switch(examPaper.getIsCorrect()){//状态位判断
            case 1://未做
                Log.i(TAG, "judgeState: "+"未做");
                ExamPaperVO examPaperVO1=examPaper.getExamPaperVO();
                jobsPaper=exangeDate(examPaper);
                Log.i(TAG, "judgeState: "+jobsPaper.toString());
                if(examPaper.getIsLimitFinishTime()==1){ //1限时完成,及有倒计时页面
                    Long count= TimeUtil.getCurrentTimeInLong();
                    if(count>examPaper.getStartTime() && count<examPaper.getEndTime()){
                        setValuesForCell(examPaperVO1,"");
                        isOnclick=true;
                        btn.setChecked(true);
                    }else{
                        setValuesForCell(examPaperVO1,"请在规定的事件里再考试");
                        isOnclick=false;
                        btn.setChecked(false);
                    }
                }else if(examPaper.getIsLimitFinishTime()==0){//0 不限制
                    setValuesForCell(examPaperVO1,"");
                    isOnclick=true;
                    btn.setChecked(true);
                }
                break;
            case 4://尚未开始考试 /////////////////////

                ExamPaperVO examPaperVO4=examPaper.getExamPaperVO();
                sb=new StringBuilder();
                sb.append("该").append(examType).append("尚未开始，不能进入答题");
                setValuesForCell(examPaperVO4,sb.toString());
                isOnclick=false;
                btn.setChecked(false);
                break;
            case 5:// 5：已过期 //////////////////

                ExamPaperVO examPaperVO5=examPaper.getExamPaperVO();
                sb=new StringBuilder();
                sb.append("该").append(examType).append("已过期，不能进入答题");
                setValuesForCell(examPaperVO5,sb.toString());
                isOnclick=false;
                btn.setChecked(false);
                break;

            //////////外部已删选/////////////////////////////////////
            case 2://已做未批改

                Intent intent2=new Intent(this,AnswerReportActivity.class);
                intent2.putExtra("CourseScheduleId",courseScheduleId);
                intent2.putExtra("ExamPaperReleaseId",examPaperReleaseId);
                intent2.putExtra("EndUserId",endUserId);
                startActivity(intent2);
                break;
            case 3://3、已做已批改
                Intent intent3=new Intent(this,AnswerReportActivity.class);
                intent3.putExtra("CourseScheduleId",courseScheduleId);
                intent3.putExtra("ExamPaperReleaseId",examPaperReleaseId);
                intent3.putExtra("EndUserId",endUserId);
                startActivity(intent3);
                break;
        }

    }


    String message;
    //设置内容
    private void setValuesForCell(ExamPaperVO vo, String message) {
        this.message=message;
        titleBarView.getRightTitleView().setText(vo.getExamTitle());
        cvCollegeName.setTvText(""+vo.getCollegeName());//院校
        cvCourseName.setTvText("课程："+vo.getCourseName());
        if(finishTime!=null){
            cvExamTime.setTvText("考试时限："+finishTime+"分钟");
        }else {
            cvExamTime.setTvText("考试时限：无");
        }

        cvZhangJie.setTvText("章节："+vo.getGroupName());
        cvLeiX.setTvText("试卷类型："+examType);
        cvKeS.setTvText("课时："+vo.getCoursewareName());
        cvTotal.setTvText("总分："+vo.getScoreSum()+"分");
    }

    /**
     * 转化数据
     * 题目集合
     */
    private JobsPaper exangeDate(ExamPaper examPaper) {
            ExamPaperVO examPaperVO = examPaper.getExamPaperVO();
            Byte type = examPaper.getType(); //试卷类型
            int jobState = examPaper.getIsCorrect();
            List<SoaExamPaper> soaList = examPaperVO.getExamPaperItemList();
            Log.i(TAG, "setMsg: " + soaList.size());
            JobsPaper jobsPaper = new JobsPaper();
           try{
               jobsPaper.setExamTitle(examPaperVO.getExamTitle());//试卷的名字
               jobsPaper.setIsLimitFinishTime(examPaper.getIsLimitFinishTime());//是否限时完成
               jobsPaper.setStartTime(examPaper.getStartTime());//考试开始时间
               jobsPaper.setEndTime(examPaper.getEndTime());//考试结束时间
               jobsPaper.setIsNeedCorrect(examPaperVO.getIsNeedCorrect());//是否需要手动修改
               jobsPaper.setJobState(jobState);//作业或者考试 完成状态
               jobsPaper.setFinishTime(examPaper.getFinishTime());//试卷完成时间
               jobsPaper.setCourseScheduleId(courseScheduleId);//课程表id
               jobsPaper.setExamPaperId(Long.toString(examPaperVO.getId()));//试卷id
               jobsPaper.setExamPaperReleaseId(examPaperReleaseId);//发布试卷id
               jobsPaper.setExamPaperReleaseType(type);//试卷类型
               jobsPaper.setExamCostTime(examPaperVO.getExamCostTime()); //考试所用时间
               jobsPaper.setEndUserId(endUserId); //用户id
               //存储所有题目的集合
               tiMuList = new ArrayList<>();
               TiMu tm;
            for (int i = 0; i < soaList.size(); i++) {
                SoaExamPaper soaExamPaper = soaList.get(i);
                Question question = soaExamPaper.getQuestion();
                tm = new TiMu();

                      tm.setJobState(jobState);
                      tm.setQid(i + 1);
                      tm.setId(String.valueOf(question.getId()));
                      tm.setQuestionType(question.getQuestionType());
                      //本题的得分
                      tm.setScore(question.getScore());
                      tm.setQuestionTitle(question.getQuestionTitle());
                      tm.setQuestionOptions(question.getQuestionOptions());
                      Log.i(TAG, "exangeDate: "+(question.getQuestionAnswerList()==null));
                      tm.setQuestionAnswerList(question.getQuestionAnswerList());
                      tm.setQuestionAnalysis(question.getQuestionAnalysis());
                      tm.setQuestionAnswer(question.getQuestionAnswer());
                      tm.setStudentAnswer(question.getAnswer());
                       //本题学生得分
                      tm.setStuScore(question.getStuScore());
                     //存储完型填空和阅读理解题型的集合
                      List<TiMu>tiMuList1=new ArrayList<>();
                      tiMuList1.clear();
                        TiMu tm1;
                      Logger.d("完型填空的："+(question.getChildren()==null ?"没有子类":question.getChildren().size()));
                    if(question.getChildren()!=null && question.getChildren().size()>0){
                        for (int j=0;j<question.getChildren().size();j++){
                            Question children=question.getChildren().get(j);
                            tm1=new TiMu();
                            tm1.setJobState(jobState);
                            tm1.setQid(j + 1);
                            tm1.setId(String.valueOf(children.getId()));
                            tm1.setQuestionType(children.getQuestionType());
                            tm1.setScore(children.getScore());
                            tm1.setQuestionTitle(children.getQuestionTitle());
                            tm1.setQuestionOptions(children.getQuestionOptions());
                            tm1.setQuestionAnswerList(children.getQuestionAnswerList());
                            tm1.setQuestionAnalysis(children.getQuestionAnalysis());
                            tm1.setQuestionAnswer(children.getQuestionAnswer());
                            tm1.setStudentAnswer(children.getAnswer());
                            tm1.setStuScore(children.getStuScore());
                            Logger.d("有二级分类的题目："+tm1.toString());
                            tiMuList1.add(tm1);
                        }
                    }
                    tm.setChildrenList(tiMuList1);
                    Logger.d("一级分类的题目："+tm.toString());
                    tiMuList.add(tm);

            }

            //试卷学生的得分
            jobsPaper.setScore(examPaper.getScore());
               //试卷的总分
               jobsPaper.setScoreSum(examPaperVO.getScoreSum());
            jobsPaper.setList(getLits(tiMuList));
            jobsPaper.setTimuSize(tiMuList.size());
           }catch (Exception e){
               e.getLocalizedMessage();
           }
        return jobsPaper;
    }

    /**
     * 题目排序
     * @param tiMuList
     * @return
     */
    private List<TiMu>getLits(List<TiMu> tiMuList){
        //总的题目排序之后的集合
        List<TiMu> list=new ArrayList<>();
        list.clear();
        for ( TiMu tm:tiMuList) {
            switch (tm.getQuestionType()) {
                case 1:
                    singleList.add(tm);
                    break;
                case 2:
                    moreList.add(tm);
                    break;
                case 3:
                    giveList.add(tm);
                    break;
                case 4:
                    subjectiveList.add(tm);
                    break;
                case 5:
                    clozeList.add(tm);
                    break;
                case 6:
                    partList.add(tm);
                    break;
            }
        }
        int a=0;
        if (singleList.size() > 0) {
            //给选择题加下标
            for (int i=0;i<singleList.size();i++) {
                singleList.get(i).setIndex((i+1));
            }
            MyConfig.putTimuQid("1",qid.get(a));
            a=a+1;
        }
        if(moreList.size()>0){
            //给多选题加下标
            for (int i=0;i<moreList.size();i++) {
                moreList.get(i).setIndex((i+1));
            }
            MyConfig.putTimuQid("2",qid.get(a));
            a=a+1;
        }
        if(giveList.size()>0){
            //给判断加下表
            for (int i=0;i<giveList.size();i++) {
                giveList.get(i).setIndex((i+1));
            }
            MyConfig.putTimuQid("3",qid.get(a));
            a=a+1;
        }
        if(clozeList.size()>0){
            //给完型填空加下表
            for (int i=0;i<clozeList.size();i++) {
                clozeList.get(i).setIndex((i+1));
            }
            MyConfig.putTimuQid("5",qid.get(a));
            a=a+1;
        }
        if(partList.size()>0){
            //给阅读理解加下表
            for (int i=0;i<partList.size();i++) {
                partList.get(i).setIndex((i+1));
            }
            MyConfig.putTimuQid("6",qid.get(a));
            a=a+1;
        }
        if(subjectiveList.size()>0){
            //给问答题加下表
            for (int i=0;i<subjectiveList.size();i++) {
                subjectiveList.get(i).setIndex((i+1));
            }
            MyConfig.putTimuQid("4",qid.get(a));
        }
        list.addAll(singleList);//判断题
        list.addAll(moreList);//多选题
        list.addAll(giveList);//判断题
        list.addAll(clozeList);//完型填空
        list.addAll(partList);//阅读理解
        list.addAll(subjectiveList);//问答题
        for (int i=0;i<list.size();i++){
            list.get(i).setQid(i+1);
        }
     return list;
 }
}
