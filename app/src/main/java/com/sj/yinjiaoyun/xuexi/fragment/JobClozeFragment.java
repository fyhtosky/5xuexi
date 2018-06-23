package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.RichTextEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.JobActivity;
import com.sj.yinjiaoyun.xuexi.adapter.JobClozeRecycleAdapter;
import com.sj.yinjiaoyun.xuexi.bean.QusetionBean;
import com.sj.yinjiaoyun.xuexi.bean.QusetionOptionBean;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.ArrayOrJsonUtil;
import com.sj.yinjiaoyun.xuexi.utils.LinkMovementMethodExt;
import com.sj.yinjiaoyun.xuexi.utils.NetworkImageGetter;
import com.sj.yinjiaoyun.xuexi.view.FullyLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/10/11.
 * 完型填空
 */
public class JobClozeFragment extends Fragment {
    public static final String BASE_TI_MU= "baseTimu";
    @BindView(R.id.tv_jobs_type)
    TextView tvJobsType;
    @BindView(R.id.head_jobs_title)
    TextView headJobsTitle;
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.ll_analysis)
    LinearLayout llAnalysis;
    @BindView(R.id.foot_job_JieXi)
    TextView footJobJieXi;
    Unbinder unbinder;
    private FullyLinearLayoutManager mLayoutManager ;

    private TiMu tm;
    private int jobState;//此张作业或者考试 完成状态 0：没有测验  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期");
    /**网络图片Getter*/
    private NetworkImageGetter mImageGetter;
    private Context context;
    //适配器
    private JobClozeRecycleAdapter jobClozeRecycleAdapter;
    //数据源
    private List<QusetionBean>list=new ArrayList<>();

    public static JobClozeFragment newInstance(TiMu tiMu){
        JobClozeFragment fragment = new JobClozeFragment();
        Bundle args = new Bundle();
        args.putParcelable(BASE_TI_MU,tiMu);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.job_cloze_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        //注册成为订阅者
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        if (bundle!=null) {
            tm=bundle.getParcelable(BASE_TI_MU);
            if(tm!=null){
                parseTm();
                init();
                addAnalysis();
            }
        }
        return view;

    }

    private void parseTm() {
        this.jobState = tm.getJobState();
        list.clear();
        for(TiMu tiMu1:tm.getChildrenList()){
            QusetionBean bean=new QusetionBean();
            List<QusetionOptionBean>optionBeanList=new ArrayList<>();
            bean.setQuestionTitle(tiMu1.getQuestionTitle());
            bean.setQid(tiMu1.getQid());
            bean.setId(Integer.parseInt(tiMu1.getId()));
            bean.setQuestionType(tiMu1.getQuestionType());
            bean.setQuestionAnswerList(tiMu1.getQuestionAnswerList());
            bean.setStudentAnswer(tiMu1.getStudentAnswer());
            bean.setQuestionAnalysis(tiMu1.getQuestionAnalysis());
            bean.setScore(tiMu1.getScore());
            bean.setValue(tiMu1.getStudentAnswer());
            if(tiMu1.getQuestionType()!=4){
                if(tiMu1.getQuestionOptions()!=null && !TextUtils.isEmpty(tiMu1.getQuestionOptions())){
                    List<String> stringList=ArrayOrJsonUtil.getList(tiMu1.getQuestionOptions());
                    for (int j=0;j<stringList.size();j++){
                        QusetionOptionBean optionBean=new QusetionOptionBean();
                        optionBean.setId(Integer.parseInt(tiMu1.getId()));
                        optionBean.setOptionTitle(stringList.get(j));
                        optionBean.setQuestionType(tiMu1.getQuestionType());
                        optionBean.setSelected(false);
                        optionBeanList.add(optionBean);
                    }
                }
            }
            bean.setOptionBeanList(optionBeanList);
            list.add(bean);
            Logger.d("封装的题目："+list.toString());
        }
    }


    private void init() {
        context=getActivity();
        //获取题型
        Byte b=tm.getQuestionType();
        tvJobsType.setText(MyConfig.getTimuQid().get(b+"")+"、"+MyConfig.questionType().get(b+"").toString());
        //展示题目
        mImageGetter = new NetworkImageGetter(context);
        mImageGetter.setTvText(headJobsTitle);
        headJobsTitle.setText(Html.fromHtml(String.valueOf(tm.getIndex()) + "." + tm.getQuestionTitle() + " (" + tm.getScore() + "分)", mImageGetter, null));
        headJobsTitle.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));

        //装在数据
        mLayoutManager = new FullyLinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(mLayoutManager);
        jobClozeRecycleAdapter=new JobClozeRecycleAdapter(context,jobState,list);
        recyclerView.setAdapter(jobClozeRecycleAdapter);
        jobClozeRecycleAdapter.notifyDataSetChanged();



    }
    //订阅方法，当富文本界面编辑，会调用该方法刷新数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RichTextEvent richTextEvent){
        int id=richTextEvent.getId();
       String content=richTextEvent.getContent();
        for (QusetionBean bean:list){
            if(bean.getId()==id){
                bean.setValue(content);
            }
        }
        jobClozeRecycleAdapter.notifyDataSetChanged();
    }



    public void setTiMuFromJobs(TiMu tiMu) {
        this.tm = tiMu;
        this.jobState = tiMu.getJobState();
        list.clear();
        for(TiMu tiMu1:tiMu.getChildrenList()){
            QusetionBean bean=new QusetionBean();
            List<QusetionOptionBean>optionBeanList=new ArrayList<>();
            bean.setQuestionTitle(tiMu1.getQuestionTitle());
            bean.setQid(tiMu1.getQid());
            bean.setId(Integer.parseInt(tiMu1.getId()));
            bean.setQuestionType(tiMu1.getQuestionType());
            bean.setQuestionAnswerList(tiMu1.getQuestionAnswerList());
            bean.setStudentAnswer(tiMu1.getStudentAnswer());
            bean.setQuestionAnalysis(tiMu1.getQuestionAnalysis());
            bean.setScore(tiMu1.getScore());
            bean.setValue(tiMu1.getStudentAnswer());
            if(tiMu1.getQuestionType()!=4){
                if(tiMu1.getQuestionOptions()!=null && !TextUtils.isEmpty(tiMu1.getQuestionOptions())){
                    List<String> stringList=ArrayOrJsonUtil.getList(tiMu1.getQuestionOptions());
                    for (int j=0;j<stringList.size();j++){
                        QusetionOptionBean optionBean=new QusetionOptionBean();
                        optionBean.setId(Integer.parseInt(tiMu1.getId()));
                        optionBean.setOptionTitle(stringList.get(j));
                        optionBean.setQuestionType(tiMu1.getQuestionType());
                        optionBean.setSelected(false);
                        optionBeanList.add(optionBean);
                    }
                }
            }
            bean.setOptionBeanList(optionBeanList);
            list.add(bean);
        }
        Logger.d("封装的题目："+list.toString());
    }

    /**
     * 根据试卷的状态添加相应的状态
     */
    private void addAnalysis() {
        if(jobState==3){//已做已批改
            llAnalysis.setVisibility(View.VISIBLE);
            mImageGetter.setTvText(footJobJieXi);
            footJobJieXi.setText(Html.fromHtml(tm.getQuestionAnalysis(), mImageGetter, null));
            footJobJieXi.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
            selfChoose();
        }else if(jobState==2){//已做未批改只显示自己答案
            llAnalysis.setVisibility(View.GONE);
            selfChoose();
        }else if(jobState==1){//选择题目时历史记录
            chaLibrary();
            llAnalysis.setVisibility(View.GONE);
        }else {
            llAnalysis.setVisibility(View.GONE);
        }
    }

    /**
     * 显示试卷学生原始选择的记录
     */
    private void selfChoose() {
        for (QusetionBean bean:list){
            if(bean.getQuestionType()!=4){
            for (int i=0;i<bean.getOptionBeanList().size();i++) {
                QusetionOptionBean optionBean = bean.getOptionBeanList().get(i);
                for (TiMu timu:tm.getChildrenList()){
                    if(optionBean.getId()==Integer.parseInt(timu.getId())){
                        if(timu.getStudentAnswer()!=null && !TextUtils.isEmpty(timu.getStudentAnswer())){
                            if(optionBean.getQuestionType()==2){
                                List<Integer> listAnswer = ArrayOrJsonUtil.jsonToList(timu.getStudentAnswer());
                                Logger.d("多选的选项："+listAnswer.toString());
                                for (int j=0;j<listAnswer.size();j++){
                                    if(i==listAnswer.get(j)){
                                        optionBean.setSelected(true);
                                    }
                                }
                            }else {
                                if(i==(Integer.parseInt(timu.getStudentAnswer()))){
                                    optionBean.setSelected(true);
                                }else {
                                    optionBean.setSelected(false);
                                }
                            }
                        }
                    }
                }
            }
            }
        }
        jobClozeRecycleAdapter.notifyDataSetChanged();
    }

    //数据查询
    public void  chaLibrary(){
        if(jobState==3 || jobState==4){//答案解析显示时，不需要存储
            return;
        }
        for (QusetionBean bean:list){
            if(bean.getQuestionType()!=4){
            for (int i=0;i<bean.getOptionBeanList().size();i++) {
                    QusetionOptionBean optionBean = bean.getOptionBeanList().get(i);
                    for (Map.Entry<String, Object> entry : JobActivity.getAnswerMap().entrySet()) {
                        if(entry.getKey().equals(String.valueOf(optionBean.getId()))){
                            if(entry.getValue()!=null ) {
                                //多选
                                if(optionBean.getQuestionType()==2){
                                    List<String> list = (List<String>) entry.getValue();
                                    for (int m=0;m<list.size();m++){
                                        int n= Integer.valueOf(list.get(m));
                                        if(n==i){
                                            optionBean.setSelected(true);
                                        }
                                    }
                                } else {
                                    //单选 判断题
                                    int a= Integer.valueOf(entry.getValue().toString());
                                    if(a==i){
                                        optionBean.setSelected(true);
                                    }else {
                                        optionBean.setSelected(false);
                                    }
                                }
                            }
                        }
                    }
                }
            }else {
                //主观题赋值答案
                for (Map.Entry<String, Object> entry : JobActivity.getAnswerMap().entrySet()) {
                    if (entry.getKey().equals(String.valueOf(bean.getId()))) {
                        if (entry.getValue() != null) {
                                bean.setValue(entry.getValue().toString());
                            }
                        }
                    }
                }
            }
        jobClozeRecycleAdapter.notifyDataSetChanged();
    }

    //数据存库
    public void saveLibrary(){
        if(jobState==3 || jobState==2){//答案 解析显示时，不需要存储
            return;
        }
        if(jobClozeRecycleAdapter==null){
            return;
        }
        for (QusetionBean bean:list) {
            List<QusetionOptionBean> optionBeanList = bean.getOptionBeanList();
            List<String> listString = new ArrayList<>();
            if (bean.getQuestionType() != 4) {
            for (int j = 0; j < optionBeanList.size(); j++) {
                QusetionOptionBean optionBean = optionBeanList.get(j);
                if (optionBean.getQuestionType() == 2) {
                    //多选
                    if (optionBean.isSelected()) {
                        listString.add(j + "");
                    }
                    if (listString.size() > 0) {
                        JobActivity.answerMap.put(String.valueOf(optionBean.getId()), listString);
                    } else {
                        JobActivity.answerMap.put(String.valueOf(optionBean.getId()), null);
                    }
                } else {
                    //单选
                    if (optionBean.isSelected()) {
                        JobActivity.answerMap.put(String.valueOf(optionBean.getId()), j + "");
                    }
                }
            }
        }else {
                //存储主观题的答案
                JobActivity.answerMap.put(String.valueOf(bean.getId()), bean.getValue());
            }
        }
        String strMap= ArrayOrJsonUtil.mapToJson(JobActivity.answerMap);
        Logger.d("选项的选择结果："+strMap);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除注册
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        saveLibrary();
    }
}
