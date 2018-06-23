package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.RichTextActivity;
import com.sj.yinjiaoyun.xuexi.bean.QusetionBean;
import com.sj.yinjiaoyun.xuexi.utils.ArrayOrJsonUtil;
import com.sj.yinjiaoyun.xuexi.utils.LinkMovementMethodExt;
import com.sj.yinjiaoyun.xuexi.utils.NetworkImageGetter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/10/11.
 * 完型填空题的适配器
 */
public class JobClozeRecycleAdapter extends RecyclerView.Adapter {
    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        OPTION,
        SUBJECT
    }

    private Context context;
    private NetworkImageGetter mImageGetter;
    //表示试卷是否阅卷
    private int jobState;
    private List<QusetionBean> list;
    //字母
    public static String[] myLetter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};

    public JobClozeRecycleAdapter(Context context, int jobState, List<QusetionBean> list) {
        this.context = context;
        this.jobState = jobState;
        this.list = list;
        mImageGetter = new NetworkImageGetter(context);
        for (QusetionBean bean:list){
            Logger.d("数据源:"+bean.toString());
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == ITEM_TYPE.OPTION.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.job_cloze_list, parent, false);
            holder=new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.job_cloze_subject_item, parent, false);
            holder= new SubjectHolder(view);
        }
        return holder;
    }

    StringBuilder sb;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final QusetionBean bean = list.get(position);
        //单选和多选的
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
        mImageGetter.setTvText(viewHolder.questionTitle);
        viewHolder.questionTitle.setText(Html.fromHtml(bean.getQid() + "." + bean.getQuestionTitle() + " (" + bean.getScore() + "分)".replaceAll("\\\\", ""), mImageGetter, null));
        viewHolder.questionTitle.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
        viewHolder.llAnalysis.setVisibility(View.GONE);
        //显示题目和选项
        if (bean.getOptionBeanList() != null && bean.getOptionBeanList().size()>0) {
            //选项的列表
            viewHolder.rvItem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            jobClozeTabRecycleAdapter jobClozeTabRecycleAdapter = new jobClozeTabRecycleAdapter(context, bean.getOptionBeanList(), jobState);
            viewHolder.rvItem.setAdapter(jobClozeTabRecycleAdapter);
        }
        if (jobState == 3) {
            viewHolder.llAnalysis.setVisibility(View.VISIBLE);
            //正确答案
            sb = new StringBuilder();
            if (bean.getOptionBeanList() != null) {
                List<String> questionAnswerList = bean.getQuestionAnswerList();
                for (int i = 0; i < questionAnswerList.size(); i++) {
                    String a = questionAnswerList.get(i);
                    sb.append(myLetter[Integer.valueOf(a)]);
                }
                viewHolder.footJobCorrect.setText(sb.toString());
            }
            //学生选择答案
                if (bean.getStudentAnswer() != null && !TextUtils.isEmpty(bean.getStudentAnswer())) {
                    if (bean.getQuestionType() == 2) {
                        //多选
                        sb = new StringBuilder();
                        List<Integer> listAnswer = ArrayOrJsonUtil.jsonToList(bean.getStudentAnswer());
                        for (int i = 0; i < listAnswer.size(); i++) {
                            sb.append(myLetter[i]);
                        }
                        viewHolder.footJobChoose.setText(sb.toString());
                    } else {
                        //单选
                        int a = Integer.valueOf(bean.getStudentAnswer());
                        viewHolder.footJobChoose.setText(myLetter[a]);
                    }

                } else {
                    viewHolder.footJobChoose.setText("");
                }

            //解析
            viewHolder.footJobJieXi.setText((bean.getQuestionAnalysis() == null ? "暂无解析" : bean.getQuestionAnalysis()));
        } else {
            viewHolder.llAnalysis.setVisibility(View.GONE);
        }
            //主观题的
        }else {
            SubjectHolder subjectHolder = (SubjectHolder) holder;
            mImageGetter.setTvText(subjectHolder.questionTitle);
            subjectHolder.questionTitle.setText(Html.fromHtml(bean.getQid() + "." + bean.getQuestionTitle() + " (" + bean.getScore() + "分)".replaceAll("\\\\", ""), mImageGetter, null));
            subjectHolder.questionTitle.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
            mImageGetter.setTvText(subjectHolder.jobsSubjuectiveEditView);
            subjectHolder.jobsSubjuectiveEditView.setText(Html.fromHtml((bean.getValue() == null ? "" : bean.getValue()), mImageGetter, null));
            subjectHolder.jobsSubjuectiveEditView.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
            if(jobState==1){
                mImageGetter.setTvText(subjectHolder.jobsSubjuectiveEditView);
                subjectHolder.jobsSubjuectiveEditView.setText(Html.fromHtml((bean.getValue() == null ? "点击跳转编辑界面" : bean.getValue()), mImageGetter, null));
                subjectHolder.jobsSubjuectiveEditView.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
                subjectHolder.jobsSubjuectiveEditView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //富文本编辑界面
                       RichTextActivity.StartActivity(context,bean.getId());
                    }
                });
            }
            if (jobState == 3) {
                subjectHolder.llAnalysis.setVisibility(View.VISIBLE);
                //正确答案
                mImageGetter.setTvText(subjectHolder.footJobCorrect);
                subjectHolder.footJobCorrect.setText(Html.fromHtml(( bean.getQuestionAnalysis()==null ? "": bean.getQuestionAnalysis()), mImageGetter, null));
                subjectHolder.footJobCorrect.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
                //解析
                subjectHolder.footJobJieXi.setText((bean.getQuestionAnalysis() == null ? "暂无解析" : bean.getQuestionAnalysis()));
            }

        }

        Logger.d("适配器的数据状态：" + list.size() + "实体类的_id:" + bean.getId() + ", 位置：" + position);
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getQuestionType() == 4 ? ITEM_TYPE.SUBJECT.ordinal() : ITEM_TYPE.OPTION.ordinal();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.question_title)
        TextView questionTitle;
        @BindView(R.id.rv_item)
        RecyclerView rvItem;
        @BindView(R.id.foot_job_Correct)
        TextView footJobCorrect;
        @BindView(R.id.foot_job_choose)
        TextView footJobChoose;
        @BindView(R.id.foot_job_JieXi)
        TextView footJobJieXi;
        @BindView(R.id.ll_analysis)
        LinearLayout llAnalysis;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class SubjectHolder  extends RecyclerView.ViewHolder{
        @BindView(R.id.question_title)
        TextView questionTitle;
        @BindView(R.id.jobs_subjuective_editView)
        TextView jobsSubjuectiveEditView;
        @BindView(R.id.foot_job_Correct)
        TextView footJobCorrect;
        @BindView(R.id.foot_job_JieXi)
        TextView footJobJieXi;
        @BindView(R.id.ll_analysis)
        LinearLayout llAnalysis;

        SubjectHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
