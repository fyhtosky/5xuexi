package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.JobActivity;
import com.sj.yinjiaoyun.xuexi.adapter.JobsSingleAdapter;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.ArrayOrJsonUtil;
import com.sj.yinjiaoyun.xuexi.utils.LinkMovementMethodExt;
import com.sj.yinjiaoyun.xuexi.utils.NetworkImageGetter;
import com.sj.yinjiaoyun.xuexi.widget.NewListView;

import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/1.
 * 作业考试题目 单选题目fragment
 */
public class JobsSingleFragment extends Fragment{

    String TAG="SingleJob";
    TiMu tm;//
    int jobState;//此张作业或者考试 完成状态  0：没有测验  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期");
    NewListView listView;//
    TextView tvTitle;//题目
    TextView tvType;
    View headContainer;
    List<String> qList;
    JobsSingleAdapter adapter;
    /**网络图片Getter*/
    private NetworkImageGetter mImageGetter;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView=inflater.inflate(R.layout.fragment_job_choice,container,false);
        Log.i(TAG, "onCreateView: 单选");
        if(tm==null){
            return null;
        }
        headContainer=inflater.inflate(R.layout.head_jobs_title,null);
        footer=inflater.inflate(R.layout.footer_job,null);
        init(convertView,headContainer,footer);
        addAnalysis(tm);
        return convertView;
    }

    private void init(View view,View headContainer,View footer){
        tvTitle= (TextView) headContainer.findViewById(R.id.head_jobs_title);
        tvType= (TextView) headContainer.findViewById(R.id.tv_jobs_type);
        tvCorrect =(TextView) footer.findViewById(R.id.foot_job_Correct);//正确答案
        tvSelected= (TextView) footer.findViewById(R.id.foot_job_choose);//所选
        tvAnalysis= (TextView) footer.findViewById(R.id.foot_job_JieXi);//解析

        listView= (NewListView) view.findViewById(R.id.jobs_singleListView);
        Byte b=tm.getQuestionType();
        context=getActivity();
        mImageGetter = new NetworkImageGetter(context);
        mImageGetter.setTvText(tvTitle);
        tvType.setText(MyConfig.getTimuQid().get(b+"")+"、"+MyConfig.questionType().get(b+"").toString());
        //展示题目
        tvTitle.setText(Html.fromHtml(String.valueOf(tm.getIndex()) + "." + tm.getQuestionTitle() + " (" + tm.getScore() + "分)", mImageGetter, null));
        tvTitle.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));

        Log.i(TAG, "init: "+tm.getQuestionOptions());
        qList= ArrayOrJsonUtil.getList(tm.getQuestionOptions());
        listView.addHeaderView(headContainer);
        adapter=new JobsSingleAdapter(qList,getActivity(),jobState);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setDividerHeight(0);


    }


    View footer;//底
    TextView tvCorrect;//正确答案
    TextView tvSelected;//所选答案
    TextView tvAnalysis;//此题解析
    String[] myLetter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };
    //给尸体添加解析答案等内容
    private void addAnalysis(TiMu tm){
        Log.i(TAG, "addAnalysis: "+tm.toString());
        int jobPaper=tm.getJobState();
        if(jobPaper==3){  //解析

            // Log.i(TAG, "addAnalysis: 已做已批改已状态");
            StringBuilder sb1=new StringBuilder();
            List<String> questionAnswerList=tm.getQuestionAnswerList();//问题正确答案
            for(int i=0;i<questionAnswerList.size();i++){
                String a=questionAnswerList.get(i);
                sb1.append(myLetter[Integer.valueOf(a)]);
            }
            //Log.i(TAG, "addAnalysis: 正确作答的答案"+sb1.toString());
            tvCorrect.setText(sb1.toString());
            if(tm.getStudentAnswer()==null || TextUtils.isEmpty(tm.getStudentAnswer())){//学生未作答
                tvSelected.setText("");
            }else{
                String listAnswer=tm.getStudentAnswer();
                int a=Integer.valueOf(listAnswer);
                Log.i(TAG, "addAnalysis: 学生作答的答案"+a+":"+myLetter[a]);
//                listView.setItemChecked((a+1),true);
                adapter.setPositionCheck(a,true);
                Log.i(TAG, "addAnalysis: 学生作答的答案"+a+":"+myLetter[a]);
                tvSelected.setText(myLetter[a]);
            }
            String analysis=tm.getQuestionAnalysis();//题目解析
            if(analysis==null || analysis.equals("")){
                tvAnalysis.setText("暂无解析");
            }else{
                mImageGetter.setTvText(tvAnalysis);
                tvAnalysis.setText(Html.fromHtml(analysis, mImageGetter, null));
                tvAnalysis.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));

            }
            listView.addFooterView(footer);
            listView.setEnabled(false);
        }else if(jobPaper==2) {//已做未批改只显示自己答案
            Log.i(TAG, "addAnalysis: 已做未批改状态");
            String listAnswer=tm.getStudentAnswer();
            if(!(listAnswer.equals(""))){
                int a=Integer.valueOf(listAnswer);
                Log.i(TAG, "addAnalysis: 学生作答的答案"+a+":"+myLetter[a]);
//                listView.setItemChecked((a+1),true);
                adapter.setPositionCheck(a,true);
                listView.setEnabled(false);
            }
        }else if(jobPaper==1) {  //选择题目时历史记录
            chaLibrary(tm.getId());
        }
    }


    /**
     *
     * @param tiMu  题目
     * @param
     */
    public void setTiMuFromJobs(TiMu tiMu){
        this.tm=tiMu;
        jobState=tiMu.getJobState();
        Log.i(TAG, "setTiMuFromJobs: "+jobState+":"+tm.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: 单选");
        saveLibrary();
    }


    //查库
    public void chaLibrary(String tmID){
        Log.i(TAG, "chaLibrary: ");
        for (Map.Entry<String, Object> entry : JobActivity.getAnswerMap().entrySet()) {
            Log.i(TAG, "saveLibrary: "+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
            if(entry.getKey().equals(tmID)){
                int a= Integer.valueOf(entry.getValue().toString());
                Log.i(TAG, "chaLibrary: 查询该题的答案为为:"+":"+a);
//                listView.setItemChecked(a+1,true);
                adapter.setPositionCheck(a,true);
            }
        }
    }

    //数据存库
    public void saveLibrary(){
        if(jobState==3 || jobState==2){//答案 解析显示时，不需要存储
            return;
        }
        if(adapter==null){
            return;
        }
        int position;
        for (Map.Entry<Integer, Boolean> entry : adapter.getSelectMap().entrySet()) {
            Log.i(TAG, "saveLibrary: "+"Key = " + entry.getKey().toString() + ", Value = " + entry.getValue());
            if(entry.getValue()){
                position=entry.getKey();
                Log.i(TAG, "选中的位子saveLibrary: "+position);
                JobActivity.answerMap.put(tm.getId(),String.valueOf(position));
                Log.i(TAG, "saveLibrary: 保存"+tm.getId()+":"+String.valueOf(position));
            }
        }
    }

}
