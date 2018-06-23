package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.AnswerReportAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.ReportSheetClozeAdapter;
import com.sj.yinjiaoyun.xuexi.callback.OnItemClickListener;
import com.sj.yinjiaoyun.xuexi.domain.ExamPaper;
import com.sj.yinjiaoyun.xuexi.domain.ExamPaperVO;
import com.sj.yinjiaoyun.xuexi.domain.JobsPaper;
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
import com.sj.yinjiaoyun.xuexi.utils.RichTextUtil;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/3.
 * 作业考试页面  作答报告
 */
public class AnswerReportActivity extends MyBaseActivity implements HttpDemo.HttpCallBack {

   private String TAG = "AnswerReport";
    //考试或者测试id
    private String examPaperReleaseId;
    // 课程id 554
    private String courseScheduleId;
    /**
     * 作业或者考试 完成状态
     * 0：没有测验  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期");
     */
    private int jobState;
    //用户id
    private String endUserId;



    //试卷转换的实体类
    private JobsPaper jobsPaper;
    @BindView(R.id.answer_report_title)
    TitleBarView answerReportTitle;
    @BindView(R.id.head_answer_paper)
    TextView headAnswerPaper;
    @BindView(R.id.head_answer_time)
    TextView headAnswerTime;
    @BindView(R.id.head_answer_grade)
    CheckBox headAnswerGrade;
    @BindView(R.id.answer_Conatiner)
    RelativeLayout answerConatiner;
    @BindView(R.id.tv_exam_info)
    TextView tvExamInfo;
    @BindView(R.id.answer_datika)
    TextView answerDatika;
    @BindView(R.id.tv_single)
    TextView tvSingle;
    @BindView(R.id.rv_single)
    RecyclerView rvSingle;
    @BindView(R.id.rl_single)
    RelativeLayout rlSingle;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.rv_more)
    RecyclerView rvMore;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.tv_give)
    TextView tvGive;
    @BindView(R.id.rv_give)
    RecyclerView rvGive;
    @BindView(R.id.rl_give)
    RelativeLayout rlGive;
    @BindView(R.id.tv_subjective)
    TextView tvSubjective;
    @BindView(R.id.rv_subjective)
    RecyclerView rvSubjective;
    @BindView(R.id.rl_subjective)
    RelativeLayout rlSubjective;
    @BindView(R.id.tv_cloze)
    TextView tvCloze;
    @BindView(R.id.rv_cloze)
    RecyclerView rvCloze;
    @BindView(R.id.rl_cloze)
    RelativeLayout rlCloze;
    @BindView(R.id.tv_part)
    TextView tvPart;
    @BindView(R.id.rv_part)
    RecyclerView rvPart;
    @BindView(R.id.rl_part)
    RelativeLayout rlPart;
    @BindView(R.id.foot_answer_btn)
    Button footAnswerBtn;
    @BindView(R.id.ll_scroll_View)
    LinearLayout llScrollView;
    @BindView(R.id.tv_single_info)
    TextView tvSingleInfo;
    @BindView(R.id.tv_more_info)
    TextView tvMoreInfo;
    @BindView(R.id.tv_give_info)
    TextView tvGiveInfo;
    @BindView(R.id.tv_subjective_info)
    TextView tvSubjectiveInfo;
    @BindView(R.id.tv_cloze_info)
    TextView tvClozeInfo;
    @BindView(R.id.tv_part_info)
    TextView tvPartInfo;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_yes)
    TextView tvYes;

    //单选题目的集合
    private List<TiMu> singleList = new ArrayList<>();
    private AnswerReportAdapter singleAdapter;
    private int singleDone;//单选题已做的数量
    private int singScore;//单选题的的得分
    private int singTotal;//单选题的总分
    //多选题目的集合
    private List<TiMu> moreList = new ArrayList<>();
    private AnswerReportAdapter moreAdapter;
    private int moreDone;//多选题已做的数量
    private int moreScore;//多选题的的得分
    private int moreTotal;//多选题的总分
    //判断题目的集合
    private List<TiMu> giveList = new ArrayList<>();
    private AnswerReportAdapter giveAdapter;
    private int giveDone;//判断题已做的数量
    private int giveScore;//判断题的的得分
    private int giveTotal;//判断题的总分
    //主观题目的集合
    private List<TiMu> subjectiveList = new ArrayList<>();
    private AnswerReportAdapter subjectiveAdapter;
    private int subjectiveDone;//主观题已做的数量
    private int subjectiveScore;//主观题的的得分
    private int subjectiveTotal;//主观题的总分
    //完型填空题目的集合
    private List<TiMu> clozeList = new ArrayList<>();
    private int clozeDone;//完型填空已做的数量
    private int clozeScore;//完型填空的的得分
    private int clozeSize;//完型填空的总体数量
    private ReportSheetClozeAdapter clozeAdapter;
    private int clozeTotal;//完型填空的总分
    //阅读理解的题目集合
    private List<TiMu> partList = new ArrayList<>();
    private int partDone;//阅读理解已做的数量
    private int partScore;//阅读理解的的得分
    private int partSize;//阅读理解的总题数量
    private ReportSheetClozeAdapter partAdapter;
    private int paartTotal;//阅读理解的总分
    private List<String> qid= Arrays.asList("一","二","三","四","五","六");
    //考试时间
    private Integer finishTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answerreport);
        ButterKnife.bind(this);
        init();
    }

    //数据准备
    private void init() {
        endUserId = PreferencesUtils.getSharePreStr(AnswerReportActivity.this, "username");
        answerReportTitle.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            courseScheduleId = bundle.getString("CourseScheduleId");
            examPaperReleaseId = bundle.getString("ExamPaperReleaseId");
            Log.i(TAG, "onCreate: " + "课程表CourseScheduleId" + courseScheduleId + " 试卷/作业examPaperReleaseId " + examPaperReleaseId + " 用户id" + endUserId);
            getHttpDate();
        }
        //单选题的适配器
        rvSingle.setLayoutManager(new GridLayoutManager(this, 10, LinearLayoutManager.VERTICAL, false));
        singleAdapter = new AnswerReportAdapter(this, singleList);
        rvSingle.setAdapter(singleAdapter);
        //设置点击事件
        singleAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (jobsPaper == null) {
                    return;
                }
                Intent mIntent = new Intent(AnswerReportActivity.this, JobActivity.class);
                mIntent.putExtra("JobsPaper", ArrayOrJsonUtil.toJson(jobsPaper));
                mIntent.putExtra("TiMu", singleList.get(position));
                mIntent.putExtra("CourseScheduleId", courseScheduleId);
                mIntent.putExtra("ExamPaperReleaseId", examPaperReleaseId);
                startActivity(mIntent);
            }
        });
        //多选题的适配器
        rvMore.setLayoutManager(new GridLayoutManager(this, 10, LinearLayoutManager.VERTICAL, false));
        moreAdapter = new AnswerReportAdapter(this, moreList);
        rvMore.setAdapter(moreAdapter);
        //设置点击事件
        moreAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (jobsPaper == null) {
                    return;
                }
                Intent mIntent = new Intent(AnswerReportActivity.this, JobActivity.class);
                mIntent.putExtra("JobsPaper", ArrayOrJsonUtil.toJson(jobsPaper));
                mIntent.putExtra("TiMu", moreList.get(position));
                mIntent.putExtra("CourseScheduleId", courseScheduleId);
                mIntent.putExtra("ExamPaperReleaseId", examPaperReleaseId);
                startActivity(mIntent);
            }
        });
        //判断的适配器
        rvGive.setLayoutManager(new GridLayoutManager(this, 10, LinearLayoutManager.VERTICAL, false));
        giveAdapter = new AnswerReportAdapter(this, giveList);
        rvGive.setAdapter(giveAdapter);
        //设置点击事件
        giveAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (jobsPaper == null) {
                    return;
                }
                Intent mIntent = new Intent(AnswerReportActivity.this, JobActivity.class);
                mIntent.putExtra("JobsPaper", ArrayOrJsonUtil.toJson(jobsPaper));
                mIntent.putExtra("TiMu", giveList.get(position));
                mIntent.putExtra("CourseScheduleId", courseScheduleId);
                mIntent.putExtra("ExamPaperReleaseId", examPaperReleaseId);
                startActivity(mIntent);
            }
        });
        //主观题的适配器
        rvSubjective.setLayoutManager(new GridLayoutManager(this, 10, LinearLayoutManager.VERTICAL, false));
        subjectiveAdapter = new AnswerReportAdapter(this, subjectiveList);
        rvSubjective.setAdapter(subjectiveAdapter);
        //设置点击事件
        subjectiveAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (jobsPaper == null) {
                    return;
                }
                Intent mIntent = new Intent(AnswerReportActivity.this, JobActivity.class);
                mIntent.putExtra("JobsPaper", ArrayOrJsonUtil.toJson(jobsPaper));
                mIntent.putExtra("TiMu", subjectiveList.get(position));
                mIntent.putExtra("CourseScheduleId", courseScheduleId);
                mIntent.putExtra("ExamPaperReleaseId", examPaperReleaseId);
                startActivity(mIntent);
            }
        });
    }

    //获取网络数据
    private void getHttpDate() {
         List<Pairs> pairsList= new ArrayList<>();
         HttpDemo  demo = new HttpDemo(this);
        String url = MyConfig.getURl("learn/test");//作业考试
        pairsList.add(new Pairs("courseScheduleId", String.valueOf(courseScheduleId)));
        pairsList.add(new Pairs("examPaperReleaseId", String.valueOf(examPaperReleaseId)));
        pairsList.add(new Pairs("userId", endUserId));
        demo.doHttpGetLoading(this, url, pairsList, 0);
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: " + msg);
        try {
            Gson gson = new Gson();
            ParserExamPaper parserExamPaper = gson.fromJson(msg, ParserExamPaper.class);
            ParserExamPaperData data = parserExamPaper.getData();
            ExamPaper examPaper = data.getExamPaper();
            finishTime=examPaper.getFinishTime();
            judgeState(examPaper);
        } catch (Exception e) {
            Log.i(TAG, "setMsg: " + e.toString());
        }
    }


    //根据状态判断是否进入题目界面
    private void judgeState(ExamPaper examPaper) {
        // 完成状态 0：没有测验  1：未做   2：已做未批改  3、已做已批改;   [ 2：已做未批改  3、已做已批改]进入到成绩页面
        jobsPaper = exangeDate(examPaper);
        jobState = jobsPaper.getJobState();
        calculateData();
        headAnswerPaper.setText("作答试卷：" + jobsPaper.getExamTitle());
        if(finishTime!=null){
            headAnswerTime.setText("考试时限："+finishTime+"分钟");
        }else {
            headAnswerTime.setText("考试时限：无");
        }
        if (jobsPaper.getJobState() == 2) {//未批改
            footAnswerBtn.setVisibility(View.GONE);
            headAnswerGrade.setChecked(false);
            //显示为批改
            headAnswerGrade.setText("未批改");
            Drawable no =getResources().getDrawable(R.drawable.gray_10);
            if(no!=null){
                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                no.setBounds(0, 0, no.getMinimumWidth(), no.getMinimumHeight());
                tvNo.setText("未做");
                tvNo.setCompoundDrawables(no,null,null,null);
            }
            Drawable yes =getResources().getDrawable(R.drawable.green_10);
            if(yes!=null){
                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                yes.setBounds(0, 0, yes.getMinimumWidth(), yes.getMinimumHeight());
                tvYes.setText("已做");
                tvYes.setCompoundDrawables(yes,null,null,null);
            }
        } else if (jobsPaper.getJobState() == 3) {//已做批改
            footAnswerBtn.setVisibility(View.VISIBLE);
            headAnswerGrade.setChecked(true);
            //显示分数
            headAnswerGrade.setText(jobsPaper.getScore() + "分");
            Drawable no =getResources().getDrawable(R.drawable.red_10);
            if(no!=null){
                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                no.setBounds(0, 0, no.getMinimumWidth(), no.getMinimumHeight());
                tvNo.setText("错误");
                tvNo.setCompoundDrawables(no,null,null,null);
            }
            Drawable yes =getResources().getDrawable(R.drawable.green_10);
            if(yes!=null){
                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                yes.setBounds(0, 0, yes.getMinimumWidth(), yes.getMinimumHeight());
                tvYes.setText("正确");
                tvYes.setCompoundDrawables(yes,null,null,null);
            }
        }
    }

    /**
     * 筛选各种类型的题目
    */
    private void calculateData(){
        Logger.d("单选题目:" + singleList.size() + "多选题目：" + moreList.size() + "判断题目：" + giveList.size()
                + "主观题目：" + subjectiveList.size() + "完型填空题目：" + clozeList.size() + "阅读理解题目：" + partList.size());
        //完型填空的适配器
        rvCloze.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        clozeAdapter = new ReportSheetClozeAdapter(this, clozeList, jobState);
        rvCloze.setAdapter(clozeAdapter);
        //设置点击事件
        clozeAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (jobsPaper == null) {
                    return;
                }
                Intent mIntent = new Intent(AnswerReportActivity.this, JobActivity.class);
                mIntent.putExtra("JobsPaper", ArrayOrJsonUtil.toJson(jobsPaper));
                mIntent.putExtra("TiMu", clozeList.get(position));
                mIntent.putExtra("CourseScheduleId", courseScheduleId);
                mIntent.putExtra("ExamPaperReleaseId", examPaperReleaseId);
                startActivity(mIntent);
            }
        });
        //阅读理解的适配器
        rvPart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        partAdapter = new ReportSheetClozeAdapter(this, partList, jobState);
        rvPart.setAdapter(partAdapter);
        //设置点击事件
        partAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (jobsPaper == null) {
                    return;
                }
                Intent mIntent = new Intent(AnswerReportActivity.this, JobActivity.class);
                mIntent.putExtra("JobsPaper", ArrayOrJsonUtil.toJson(jobsPaper));
                mIntent.putExtra("TiMu", partList.get(position));
                mIntent.putExtra("CourseScheduleId", courseScheduleId);
                mIntent.putExtra("ExamPaperReleaseId", examPaperReleaseId);
                startActivity(mIntent);
            }
        });
        showState();
    }

    /**
     * 根据数据的状态展示相应的题型及题型对应的信息
     * 1表示正确 2表示错误 0表示没做
     */
    private void showState() {
        if (jobState == 2) {
            tvExamInfo.setText(RichTextUtil.fillColor("已做/总题数","已做", Color.parseColor("#01af63")));
        } else {
            tvExamInfo.setText(RichTextUtil.fillColor("得分/总分(共计"+jobsPaper.getScoreSum()+"分)","得分",Color.parseColor("#01af63")));
        }
        //单选题目存在
        if (singleList.size() > 0) {
            //计算单选题总得分和已做数量
            for (TiMu timu : singleList) {
                singTotal = singTotal + timu.getScore();
                singScore = singScore + timu.getStuScore();
                int flag = timu.getAnswerFlag();
                if (flag == 1 || flag == 2) {
                    singleDone++;
                }
            }
            tvSingleInfo.setVisibility(View.VISIBLE);
            if (jobState == 2) {
                tvSingleInfo.setText(RichTextUtil.fillColor("单选题：" + singleDone + "/" + singleList.size(),
                        singleDone+"", Color.parseColor("#01af63")));
            } else {
                tvSingleInfo.setText(RichTextUtil.fillColor("单选题：" + singScore + "/" + singTotal,
                        singScore+"", Color.parseColor("#01af63")));
            }
            tvSingle.setText(MyConfig.getTimuQid().get(1+"")+"、单选题(" + singleList.size() + "题，共" + singTotal + "分)");
            singleAdapter.setJobState(jobState);
            singleAdapter.notifyDataSetChanged();
        } else {
            rlSingle.setVisibility(View.GONE);
            tvSingleInfo.setVisibility(View.GONE);
        }
        //多选题存在
        if (moreList.size() > 0) {
            //计算多选题总得分和已做数量
            for (TiMu timu : moreList) {
                moreTotal = moreTotal + timu.getScore();
                moreScore = moreScore + timu.getStuScore();
                int flag = timu.getAnswerFlag();
                if (flag == 1 || flag == 2) {
                    moreDone++;
                }
            }
            tvMoreInfo.setVisibility(View.VISIBLE);
            if (jobState == 2) {
                tvMoreInfo.setText(RichTextUtil.fillColor("多选题：" + moreDone + "/" + moreList.size(),
                        ""+moreDone,Color.parseColor("#01af63")));
            } else {
                tvMoreInfo.setText(RichTextUtil.fillColor("多选题：" + moreScore + "/" +moreTotal,
                        ""+moreScore,Color.parseColor("#01af63")));
            }
            tvMore.setText(MyConfig.getTimuQid().get(2+"")+"、多选题(" + moreList.size() + "题，共" + moreTotal + "分)");
            moreAdapter.setJobState(jobState);
            moreAdapter.notifyDataSetChanged();
        } else {
            rlMore.setVisibility(View.GONE);
            tvMoreInfo.setVisibility(View.GONE);
        }

        //判断题存在
        if (giveList.size() > 0) {
            //计算判断题总得分和已做数量
            for (TiMu timu : giveList) {
                giveTotal = giveTotal + timu.getScore();
                giveScore = giveScore + timu.getStuScore();
                int flag = timu.getAnswerFlag();
                if (flag == 1 || flag == 2) {
                    giveDone++;
                }
            }
            rlGive.setVisibility(View.VISIBLE);
            tvGiveInfo.setVisibility(View.VISIBLE);
            if (jobState == 2) {
                tvGiveInfo.setText(RichTextUtil.fillColor("判断题：" + giveDone + "/" + giveList.size(),
                        ""+giveDone,Color.parseColor("#01af63")));
            } else {
                tvGiveInfo.setText(RichTextUtil.fillColor("判断题：" + giveScore + "/" + giveTotal,
                        ""+giveScore,Color.parseColor("#01af63")));
            }
            tvGive.setText(MyConfig.getTimuQid().get(3+"")+"、判断题(" + giveList.size() + "题，共" + giveTotal + "分)");
            giveAdapter.setJobState(jobState);
            giveAdapter.notifyDataSetChanged();
        } else {
            tvGiveInfo.setVisibility(View.GONE);
            rlGive.setVisibility(View.GONE);
        }

        //主观题存在
        if (subjectiveList.size() > 0) {
            //计算主观题总得分和已做数量
            for (TiMu timu : subjectiveList) {
                subjectiveTotal = subjectiveTotal + timu.getScore();
                subjectiveScore = subjectiveScore + timu.getStuScore();
                int flag = timu.getAnswerFlag();
                if (flag == 1 || flag == 2) {
                    subjectiveDone++;
                }
            }
            rlSubjective.setVisibility(View.VISIBLE);
            tvSubjectiveInfo.setVisibility(View.VISIBLE);
            if (jobState == 2) {
                tvSubjectiveInfo.setText(RichTextUtil.fillColor("问答题：" + subjectiveDone + "/" + subjectiveList.size(),
                        ""+subjectiveDone,Color.parseColor("#01af63")));
            } else {
                tvSubjectiveInfo.setText(RichTextUtil.fillColor("问答题：" + subjectiveScore + "/" + subjectiveTotal,
                        ""+subjectiveScore,Color.parseColor("#01af63")));
            }
            tvSubjective.setText(MyConfig.getTimuQid().get(4+"")+"、问答题(" + subjectiveList.size() + "题，共" + subjectiveTotal + "分)");
            subjectiveAdapter.setJobState(jobState);
            subjectiveAdapter.notifyDataSetChanged();
        } else {
            rlSubjective.setVisibility(View.GONE);
            tvSubjectiveInfo.setVisibility(View.GONE);
        }

        //完型填空题目存在
        if (clozeList.size() > 0) {
            //计算完型填空总得分和已做数量
            for (TiMu timu : clozeList) {
                clozeSize=clozeSize+timu.getChildrenList().size();
                for (TiMu timu1:timu.getChildrenList()){
                    clozeScore = clozeScore + timu1.getStuScore();
                    clozeTotal = clozeTotal + timu1.getScore();
                    int flag = timu1.getAnswerFlag();
                    if (flag == 1 || flag == 2) {
                        clozeDone++;
                    }
                }
            }
            rlCloze.setVisibility(View.VISIBLE);
            tvClozeInfo.setVisibility(View.VISIBLE);
            if (jobState == 2) {
                tvClozeInfo.setText(RichTextUtil.fillColor("完型填空：" + clozeDone + "/" + clozeSize,
                        ""+clozeDone,Color.parseColor("#01af63")));
            } else {
                tvClozeInfo.setText(RichTextUtil.fillColor("完型填空：" + clozeScore + "/" + clozeTotal,
                        ""+clozeScore,Color.parseColor("#01af63")));
            }
            tvCloze.setText(MyConfig.getTimuQid().get(5+"")+"、完型填空(" + clozeList.size() + "题，共" + clozeTotal + "分)");
            clozeAdapter.notifyDataSetChanged();
        } else {
            rlCloze.setVisibility(View.GONE);
            tvClozeInfo.setVisibility(View.GONE);
        }
        //阅读理解题目存在
        if (partList.size() > 0) {
            //计算阅读理解总得分和已做数量
            for (TiMu timu : partList) {
                partSize=partSize+timu.getChildrenList().size();
                for (TiMu timu1:timu.getChildrenList()){
                    partScore = partScore + timu1.getStuScore();
                    paartTotal = paartTotal + timu1.getScore();
                    int flag = timu1.getAnswerFlag();
                    if (flag == 1 || flag == 2) {
                        partDone++;
                    }
                }

            }
            rlPart.setVisibility(View.VISIBLE);
            tvPartInfo.setVisibility(View.VISIBLE);
            if (jobState == 2) {
                tvPartInfo.setText(RichTextUtil.fillColor("阅读理解：" + partDone + "/" + partSize,
                        ""+partDone,Color.parseColor("#01af63")));
            } else {
                tvPartInfo.setText(RichTextUtil.fillColor("阅读理解：" + partScore + "/" + paartTotal,
                        ""+partScore,Color.parseColor("#01af63")));
            }
            tvPart.setText(MyConfig.getTimuQid().get(6+"")+"、阅读理解(" + partList.size() + "题，共" +paartTotal + "分)");
            partAdapter.notifyDataSetChanged();
        } else {
            rlPart.setVisibility(View.GONE);
            tvPartInfo.setVisibility(View.GONE);
        }
        llScrollView.setVisibility(View.VISIBLE);
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
        try {
            jobsPaper.setJobState(jobState);
            jobsPaper.setStartTime(examPaper.getStartTime());
            jobsPaper.setEndTime(examPaper.getEndTime());
            jobsPaper.setIsLimitFinishTime(examPaper.getIsLimitFinishTime());
            jobsPaper.setCourseScheduleId(courseScheduleId);//课程表id
            jobsPaper.setExamPaperId(Long.toString(examPaperVO.getId()));//试卷id
            jobsPaper.setExamPaperReleaseId(examPaperReleaseId);//发布试卷id
            jobsPaper.setExamPaperReleaseType(type);//试卷类型
            Log.i(TAG, "exangeDate: " + examPaperVO.getExamTitle());
            jobsPaper.setExamTitle(examPaperVO.getExamTitle());
            jobsPaper.setExamCostTime(examPaperVO.getExamCostTime());
            jobsPaper.setEndUserId(endUserId);
            jobsPaper.setIsNeedCorrect(examPaperVO.getIsNeedCorrect());
            jobsPaper.setFinishTime(examPaper.getFinishTime());

        List<TiMu> tiMuList = new ArrayList<>();
        tiMuList.clear();
        TiMu tm;
        Question question;
        SoaExamPaper soaExamPaper;
        for (int i = 0; i < soaList.size(); i++) {
            soaExamPaper = soaList.get(i);
            question = soaExamPaper.getQuestion();
            tm = new TiMu();

                tm.setAnswerFlag(checkAnswer(question.getAnswer(), question.getQuestionAnswer(), question.getQuestionType(), question.getQuestionAnswerList(), question.getStuScore()));
                tm.setJobState(jobState);
                tm.setQid(i + 1);
                tm.setId(String.valueOf(question.getId()));
                tm.setQuestionType(question.getQuestionType());
                //本题的得分
                tm.setScore(question.getScore());
                tm.setQuestionTitle(question.getQuestionTitle());
                tm.setQuestionOptions(question.getQuestionOptions());
                Log.i(TAG, "exangeDate: " + (question.getQuestionAnswerList() == null));
                tm.setQuestionAnswerList(question.getQuestionAnswerList());
                tm.setQuestionAnalysis(question.getQuestionAnalysis());
                tm.setQuestionAnswer(question.getQuestionAnswer());
                tm.setStudentAnswer(question.getAnswer());
                //本题学生得分
                tm.setStuScore(question.getStuScore());
                List<TiMu> tiMuList1 = new ArrayList<>();
                tiMuList1.clear();
                TiMu tm1;
                Logger.d("完型填空的：" + (question.getChildren() == null ? "没有子类" : question.getChildren().size()));
                if (question.getChildren() != null && question.getChildren().size() > 0) {
                    for (int j = 0; j < question.getChildren().size(); j++) {
                        Question children = question.getChildren().get(j);
                        tm1 = new TiMu();
                        tm1.setAnswerFlag(checkAnswer(children.getAnswer(), children.getQuestionAnswer(), children.getQuestionType(), children.getQuestionAnswerList(), children.getStuScore()));
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
                        Logger.d("有二级分类的题目：" + tm1.toString());
                        tiMuList1.add(tm1);
                    }

                }
                tm.setChildrenList(tiMuList1);
                Logger.d("一级分类的题目：" + tm.toString());
                tiMuList.add(tm);

        }
        //试卷的批阅的的分
        jobsPaper.setScore(examPaper.getScore());
            //试卷的总分
            jobsPaper.setScoreSum(examPaperVO.getScoreSum());
            jobsPaper.setList(getLits(tiMuList));
        jobsPaper.setTimuSize(tiMuList.size());
        Logger.d("解析：" + jobsPaper.toString());
        } catch (Exception e) {
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
        singleList.clear();
        moreList.clear();
        giveList.clear();
        subjectiveList.clear();
        clozeList.clear();
        partList.clear();
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
    /**
     * 校对 答案 正确 错误 未做 与否
     *
     * @param stuAnswer   学生答案
     * @param rightAnswer 正确答案
     * @param questerType 问题类型
     * @param list        答案集合
     * @return -1表示未成功判断   1表示正确 2表示错误 0表示没做
     */
    private int checkAnswer(String stuAnswer, String rightAnswer, int questerType, List<String> list, int stuScore) {
        int state = -1;//-1表示未成功判断   1表示正确 2表示错误 0表示没做
        if (rightAnswer == null && stuAnswer == null && list == null) {
            return state;
        }
        if ( "".equals(stuAnswer)) {
            state = 0;
            return state;
        }
        if (questerType == 1 || questerType == 3) {//单选
            for (String str : list) {
                if (str.equals(stuAnswer)) {
                    state = 1;
                } else {
                    state = 2;
                }
            }
        } else if (questerType == 2) {//多选
            Log.i(TAG, "---------checkAnswer: 系统答案" + rightAnswer);
            Log.i(TAG, "---------checkAnswer：学生答案" + stuAnswer);
            List<Integer> stuList = ArrayOrJsonUtil.jsonToList(stuAnswer);
            int len = list.size();
            Log.i(TAG, "checkAnswer:系统答案个数" + len);
            int right = 0;
            if ("[]".equals(stuAnswer)) {
                return 0;
            }
            if (len == stuList.size()) {
                for (int i = 0; i < stuList.size(); i++) {
                    Integer stu = stuList.get(i);
                    String a = String.valueOf(stu);
                    Log.i(TAG, "checkAnswer: 遍历学生答案集合" + stu);
                    if (list.contains(a)) {
                        right++;
                    }
                }
                Log.i(TAG, "checkAnswer: 正确答案个数" + right);
                if (right == len) {
                    state = 1;
                } else {
                    state = 2;
                }
            } else {
                state = 2;
            }
        } else if (questerType == 4) {//主观
            if (stuScore == 0) {
                state = 2;
            } else {
                state = 1;
            }
        }
        Log.i(TAG, "checkAnswer: 返回状态" + state);
        return state;
    }


    /**
     * 查看答案解析
     */
    @OnClick(R.id.foot_answer_btn)
    public void onViewClicked() {
        if (jobsPaper == null) {
            return;
        }
        Intent mIntent = new Intent(AnswerReportActivity.this, JobActivity.class);
        mIntent.putExtra("JobsPaper", ArrayOrJsonUtil.toJson(jobsPaper));
        Log.i(TAG, "onClick: " + ArrayOrJsonUtil.toJson(jobsPaper));
        mIntent.putExtra("CourseScheduleId", courseScheduleId);
        mIntent.putExtra("ExamPaperReleaseId", examPaperReleaseId);
        // 设置结果，并进行传送
        startActivity(mIntent);
    }
}
