package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.JobActivity;
import com.sj.yinjiaoyun.xuexi.adapter.JobsMoreAdapter;
import com.sj.yinjiaoyun.xuexi.db.DbOperatorJob;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.ArrayOrJsonUtil;
import com.sj.yinjiaoyun.xuexi.utils.LinkMovementMethodExt;
import com.sj.yinjiaoyun.xuexi.utils.NetworkImageGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/1.
 * 作业考试题目 多选题目fragment
 */
public class JobsMoreFragment extends Fragment{

    String TAG="jobMore";
    TiMu tm;
    int tmCode;
    int jobState;//此张作业或者考试 完成状态 0：没有测验  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期");
    ListView listView;//
    TextView tvTitle;//题目
    TextView tvType;
    View headContainer;
    List<String> qList;
    JobsMoreAdapter adapter;
    DbOperatorJob dbJob;
    /**网络图片Getter*/
    private NetworkImageGetter mImageGetter;
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbJob=new DbOperatorJob(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView=inflater.inflate(R.layout.fragment_job_choice,container,false);
        headContainer=inflater.inflate(R.layout.head_jobs_title,null);
        footer=inflater.inflate(R.layout.footer_job,null);
        init(convertView,headContainer,footer);
        addAnalysis(tm);
        return convertView;
    }

    StringBuilder sb;
    private void init(View view,View headContainer,View footer){
        tvTitle= (TextView) headContainer.findViewById(R.id.head_jobs_title);
        tvType= (TextView) headContainer.findViewById(R.id.tv_jobs_type);
        tvCorrect =(TextView) footer.findViewById(R.id.foot_job_Correct);//正确答案
        tvSelected= (TextView) footer.findViewById(R.id.foot_job_choose);//所选
        tvAnalysis= (TextView) footer.findViewById(R.id.foot_job_JieXi);//解析

        listView= (ListView) view.findViewById(R.id.jobs_singleListView);
        Byte b=tm.getQuestionType();
        sb=new StringBuilder();
        sb.append(tm.getIndex()).append(".").append(tm.getQuestionTitle()).append(" (").append(tm.getScore()).append("分)");
        context=getActivity();
        //展示题型
        tvType.setText(MyConfig.getTimuQid().get(b+"")+"、"+MyConfig.questionType().get(b+"").toString());
        //展示题目
        //加载题干中带有html中<img>图片
        mImageGetter = new NetworkImageGetter(context);
        mImageGetter.setTvText(tvTitle);
        tvTitle.setText(Html.fromHtml(sb.toString(), mImageGetter, null));
        tvTitle.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
        Log.i(TAG, "init: "+tm.getQuestionOptions());
        qList= ArrayOrJsonUtil.getList(tm.getQuestionOptions());
        listView.addHeaderView(headContainer);
        adapter=new JobsMoreAdapter(qList,getActivity());
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);
    }

    /**
     * @param tiMu  题目
     * @param
     */
    public void setTiMuFromJobs(TiMu tiMu){
        this.tm=tiMu;
        this.jobState=tiMu.getJobState();
        Log.i(TAG, "setTiMuFromJobs: "+jobState+tiMu.toString());
    }


    View footer;//底
    TextView tvCorrect;//正确答案
    TextView tvSelected;//所选答案
    TextView tvAnalysis;//此题解析
    String[] myLetter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };
    //给题目添加解析答案等内容
    private void addAnalysis(TiMu tm){
        Log.i(TAG, "addAnalysis: "+tm.toString());
        int jobPaper=tm.getJobState();
        if(jobPaper==3){  //解析
                Log.i(TAG, "addAnalysis: 已做已批改状态");
                adapter.setFlag(1);//设置不能点击
                StringBuilder sb1 = new StringBuilder();
                List<String> questionAnswerList = tm.getQuestionAnswerList();//问题正确答案
                for (int i = 0; i < questionAnswerList.size(); i++) {
                    String a = questionAnswerList.get(i);
                    Log.i(TAG, "addAnalysis:正确作答遍历"+a);
                    sb1.append(myLetter[Integer.valueOf(a)]);
                }
                Log.i(TAG, "addAnalysis: 正确作答的答案" + sb1.toString());
                tvCorrect.setText(sb1.toString());

                if (tm.getStudentAnswer() == null  ||tm.getStudentAnswer().equals("") ) {//学生未作答
                    tvSelected.setText("");
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    Log.i(TAG, "--addAnalysis: "+tm.getStudentAnswer());
                    List<Integer> listAnswer = ArrayOrJsonUtil.jsonToList(tm.getStudentAnswer());
                    for (int a = 0; a < listAnswer.size(); a++) {
                        Log.i(TAG, "addAnalysis: 学生作答的答案" + a + ":" + myLetter[a]);
                        try{
                            int b=listAnswer.get(a);
                            sb2.append(myLetter[b]);
                            adapter.setPositionCheck(b, true);
                        }catch (Exception e){
                            e.getLocalizedMessage();
                        }
                    }
                    tvSelected.setText(sb2.toString());
                }

                String analysis = tm.getQuestionAnalysis();//题目解析
                if (analysis == null || analysis.equals("")) {
                    tvAnalysis.setText("暂无解析");
                } else {
                    mImageGetter.setTvText(tvAnalysis);
                    tvAnalysis.setText(Html.fromHtml(analysis, mImageGetter, null));
                    tvAnalysis.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
                }
                listView.addFooterView(footer);
                listView.setEnabled(false);
        }else if(jobPaper==2) {//已做为批改只显示自己答案
            Log.i(TAG, "addAnalysis: 已做未批改状态");
            String answer=tm.getStudentAnswer();
            adapter.setFlag(1);
            if(!(answer.equals(""))) {
                List<Integer> listAnswer = ArrayOrJsonUtil.jsonToList(answer);
                for (int a : listAnswer) {
                    Log.i(TAG, "addAnalysis: 学生作答的答案" + a + ":" + myLetter[a]);
                    adapter.setPositionCheck(a, true);
                }
                listView.setEnabled(false);
            }
        }else  {  //选择题目时历史记录
            chaLibrary(tm.getId());
        }
    }



    //查库
    public void chaLibrary(String tmID){
        if(jobState==3 || jobState==4){//答案解析显示时，不需要存储
            return;
        }
        adapter.setFlag(0);
        for (Map.Entry<String, Object> entry : JobActivity.getAnswerMap().entrySet()) {
          //  Log.i(TAG, "saveLibrary: "+"Key = " + entry.getKey().toString() + ", Value = " + entry.getValue());
            if(entry.getKey().equals(tmID)){
                if(entry.getValue()!=null) {
                    List<String> list = (List<String>) entry.getValue();
                    for (String s : list) {
                        int a = Integer.valueOf(s);
                        Log.i(TAG, "chaLibrary: 查询该题的答案为为:" + ":" + s);
                        adapter.setPositionCheck(a, true);
                    }
                }
            }
        }
    }



    //数据存库
    public void saveLibrary(){
        if(jobState==3 || jobState==2){//答案解析显示时，不需要存储
            return;
        }
        int position;
        List<String> listString=new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : adapter.getSelectMap().entrySet()) {
            Log.i(TAG, "saveLibrary: "+"Key = " + entry.getKey().toString() + ", Value = " + entry.getValue());
            if(entry.getValue()){
                position=entry.getKey();
                listString.add(position+"");
            }
        }
        if(listString.size()>0){
            Log.i(TAG, "saveLibrary: 保存答案的个数--"+tm.getId()+":"+listString.size());
            JobActivity.answerMap.put(tm.getId(),listString);
        }else{
            JobActivity.answerMap.put(tm.getId(),null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveLibrary();
    }
}


