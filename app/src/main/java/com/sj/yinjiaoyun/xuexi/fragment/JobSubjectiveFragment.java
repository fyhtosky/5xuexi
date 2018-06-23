package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.JobActivity;
import com.sj.yinjiaoyun.xuexi.activity.RichTextActivity;
import com.sj.yinjiaoyun.xuexi.adapter.JobsSingleAdapter;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.LinkMovementMethodExt;
import com.sj.yinjiaoyun.xuexi.utils.NetworkImageGetter;
import com.sj.yinjiaoyun.xuexi.utils.NetworkOriginalImageGetter;

import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/1.
 * 作业考试题目 主观题题目fragment
 */
public class JobSubjectiveFragment extends Fragment{
    private static final int START_RICH_TEXT_ACTIVITY =1 ;
    String TAG="jobpSubject";
    TiMu tm;
    int tmCode;
    int jobState;//此张作业或者考试 完成状态 0：没有测验  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期");
    TextView tvTitle;//题目
    TextView tvType;
    JobsSingleAdapter adapter;
//    EditText editText;
    TextView tvContent;
    TextView tvAnswer;//系统正确答案
    /**网络图片Getter*/
    private NetworkImageGetter mImageGetter;
    private NetworkOriginalImageGetter originalImageGetter;
    private Context context;
    private String identify="x_u_e_i_x_@#$_x_u_e_i_x";
    //富文本编辑的文本
    private  String result;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView=inflater.inflate(R.layout.fragment_job_subjective,container,false);
        init(convertView);
        addAnalysis(jobState);
        return convertView;
    }

    StringBuilder sb;
    private void init(View view){
        context=getActivity();
        originalImageGetter=new NetworkOriginalImageGetter(context);
        footer=view.findViewById(R.id.bottom_jobs_container);
        tvAnalysis= (TextView) view.findViewById(R.id.jobs_subjuective_JieXi);//解析
        tvAnswer= (TextView) view.findViewById(R.id.jobs_subjuective_answer);
        tvContent= (TextView) view.findViewById(R.id.jobs_subjuective_editView);
        tvTitle= (TextView) view.findViewById(R.id.head_jobs_subTitle);
        tvType= (TextView) view.findViewById(R.id.tv_jobs_type);
        Byte b=tm.getQuestionType();
        sb=new StringBuilder();
        sb.append(tm.getIndex()).append(".").append(tm.getQuestionTitle()).append(" (").append(tm.getScore()).append("分)");
        //展示题型
        tvType.setText(MyConfig.getTimuQid().get(b+"")+"、"+MyConfig.questionType().get(b+"").toString());
        //展示题目
        mImageGetter = new NetworkImageGetter(context);
        mImageGetter.setTvText(tvTitle);
        tvTitle.setText(Html.fromHtml(sb.toString(), mImageGetter, null));
        tvTitle.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));

    }

    public void setTiMuFromJobs(TiMu tiMu){
        this.tm=tiMu;
        this.jobState=tiMu.getJobState();
        Log.i(TAG, "setTiMuFromJobs: "+tiMu.toString());
    }



    View footer;//底
    TextView tvAnalysis;//此题解析
    //给实体添加解析内容
    private void addAnalysis(int jobState){
        if(jobState==3){
            Log.i(TAG, "addAnalysis:已做已批改状态 "+tm.getStudentAnswer()+":"+tm.getQuestionAnalysis());
            footer.setVisibility(View.VISIBLE);
            tvAnalysis.setVisibility(View.VISIBLE);
            //显示正确答案
            mImageGetter.setTvText(tvAnswer);
            tvAnswer.setText(Html.fromHtml((tm.getQuestionAnswer()==null ? "":tm.getQuestionAnswer()), mImageGetter, null));
            tvAnswer.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
            //显示学生自己作答
            originalImageGetter.setTvText(tvContent);
            tvContent.setText(Html.fromHtml((tm.getStudentAnswer()==null ? "":tm.getStudentAnswer()), originalImageGetter, null));
            tvContent.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
            //显示解析
            mImageGetter.setTvText(tvAnalysis);
            tvAnalysis.setText(Html.fromHtml(tm.getQuestionAnalysis(), mImageGetter, null));
            tvAnalysis.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
//            tvContent.setEnabled(false);
        }else if(jobState ==2){
            Log.i(TAG, "addAnalysis: 已做未批改"+tm.getStudentAnswer());
            //显示学生自己作答
            originalImageGetter.setTvText(tvContent);
            tvContent.setText(Html.fromHtml((tm.getStudentAnswer()==null ? "":tm.getStudentAnswer()), originalImageGetter, null));
            tvContent.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
            tvAnalysis.setVisibility(View.GONE);
            footer.setVisibility(View.GONE);
//            tvContent.setEnabled(false);
        }else if(jobState==1){//未做作业显示作答历史
            footer.setVisibility(View.GONE);
            tvAnalysis.setVisibility(View.GONE);
            chaLibrary(tm.getId());
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   startActivityForResult(new Intent(context,RichTextActivity.class),START_RICH_TEXT_ACTIVITY);
                }
            });
        }
    }

    //查库
    public void chaLibrary(String tmID){
        for (Map.Entry<String, Object> entry : JobActivity.getAnswerMap().entrySet()) {
          //  Log.i(TAG, "saveLibrary: "+"Key = " + entry.getKey().toString() + ", Value = " + entry.getValue());
            if(entry.getKey().equals(tmID)){
                if(entry.getValue()!=null){
                    if(!TextUtils.isEmpty(entry.getValue()+"")){
                        result=entry.getValue().toString();
                    }
                    originalImageGetter.setTvText(tvContent);
                    tvContent.setText(Html.fromHtml((entry.getValue()+""), originalImageGetter, null));
                    tvContent.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
                }
            }
        }
    }

    //数据存库
    public void saveLibrary(){
        if(jobState==3 || jobState==2){//答案解析显示时，不需要存储
            return;
        }
        Log.i(TAG, "saveLibrary: 保存"+tm.getId()+":"+result);
        if(result==null ||result.equals("")){
            JobActivity.answerMap.put(tm.getId(),null);
        }else{
            JobActivity.answerMap.put(tm.getId(),result);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveLibrary();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_RICH_TEXT_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
               if(data!=null){
                   result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                   if(result!=null && !TextUtils.isEmpty(result)){

                   }
                   originalImageGetter.setTvText(tvContent);
                   tvContent.setText(Html.fromHtml(result, originalImageGetter, null));
                   tvContent.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
               }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
