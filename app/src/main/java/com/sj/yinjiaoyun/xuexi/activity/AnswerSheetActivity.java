package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.ChooseTimuEvent;
import com.sj.yinjiaoyun.xuexi.Event.SubmitAnswerEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.AnswerSheetClozeAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.AnswerSheetClozeItemAdapter;
import com.sj.yinjiaoyun.xuexi.callback.OnItemClickListener;
import com.sj.yinjiaoyun.xuexi.domain.JobsPaper;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 答题卡的界面
 */
public class AnswerSheetActivity extends MyBaseActivity {

    @BindView(R.id.title)
    TitleBarView title;
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
    //解析工具
    private Gson gson = new Gson();
    //题目的集合
    private List<TiMu> tiMuList = new ArrayList<>();
    //单选题目的集合
    private List<TiMu> singleList = new ArrayList<>();
    private AnswerSheetClozeItemAdapter singleAdapter;
    private int singTotal;//单选题的总分
    //多选题目的集合
    private List<TiMu> moreList = new ArrayList<>();
    private AnswerSheetClozeItemAdapter moreAdapter;
    private int moreTotal;//多选题的总分
    //判断题目的集合
    private List<TiMu> giveList = new ArrayList<>();
    private AnswerSheetClozeItemAdapter giveAdapter;
    private int giveTotal;//判断题的总分
    //主观题目的集合
    private List<TiMu> subjectiveList = new ArrayList<>();
    private AnswerSheetClozeItemAdapter subjectiveAdapter;
    private int subjectiveTotal;//主观题的总分
    //完型填空题目的集合
    private List<TiMu> clozeList = new ArrayList<>();
    private AnswerSheetClozeAdapter clozeAdapter;
    private int clozeTotal;//完型填空的总分
    //阅读理解的题目集合
    private List<TiMu> partList = new ArrayList<>();
    private AnswerSheetClozeAdapter pratAdapter;
    private int paartTotal;//阅读理解的总分



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_sheet);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //单选题的适配器
        rvSingle.setLayoutManager(new GridLayoutManager(this, 10,LinearLayoutManager.VERTICAL, false));
        singleAdapter = new AnswerSheetClozeItemAdapter(this, singleList, JobActivity.getAnswerMap());
        rvSingle.setAdapter(singleAdapter);
        //设置点击事件
        singleAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventBus.getDefault().post(new ChooseTimuEvent(singleList.get(position)));
                finish();
            }
        });
        //多选题的适配器
        rvMore.setLayoutManager(new GridLayoutManager(this, 10,LinearLayoutManager.VERTICAL, false));
        moreAdapter = new AnswerSheetClozeItemAdapter(this, moreList, JobActivity.getAnswerMap());
        rvMore.setAdapter(moreAdapter);
        //设置点击事件
        moreAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventBus.getDefault().post(new ChooseTimuEvent(moreList.get(position)));
                finish();
            }
        });
        //判断题的适配器
        rvGive.setLayoutManager(new GridLayoutManager(this, 10,LinearLayoutManager.VERTICAL, false));
        giveAdapter = new AnswerSheetClozeItemAdapter(this, giveList, JobActivity.getAnswerMap());
        rvGive.setAdapter(giveAdapter);
        //设置点击事件
        giveAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventBus.getDefault().post(new ChooseTimuEvent(giveList.get(position)));
                finish();
            }
        });
        //主观题的适配器
        rvSubjective.setLayoutManager(new GridLayoutManager(this, 10,LinearLayoutManager.VERTICAL, false));
        subjectiveAdapter = new AnswerSheetClozeItemAdapter(this, subjectiveList, JobActivity.getAnswerMap());
        rvSubjective.setAdapter(subjectiveAdapter);
        //设置点击事件
        subjectiveAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventBus.getDefault().post(new ChooseTimuEvent(subjectiveList.get(position)));
                finish();
            }
        });
        //完型填空的适配器
        rvCloze.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        clozeAdapter = new AnswerSheetClozeAdapter(this, clozeList, JobActivity.getAnswerMap());
        rvCloze.setAdapter(clozeAdapter);
        //设置点击事件
        clozeAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventBus.getDefault().post(new ChooseTimuEvent(clozeList.get(position)));
                finish();
            }
        });
        //阅读理解的适配器
        rvPart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        pratAdapter = new AnswerSheetClozeAdapter(this, partList, JobActivity.getAnswerMap());
        rvPart.setAdapter(pratAdapter);
        //设置点击事件
        pratAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventBus.getDefault().post(new ChooseTimuEvent(partList.get(position)));
                finish();
            }
        });
        title.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //试题的封装类的字符串
             String jobsJson = bundle.getString("JobsPaper");
            //试题的封类
            JobsPaper jobsPaper= gson.fromJson(jobsJson, JobsPaper.class);
            tiMuList.clear();
            tiMuList.addAll(jobsPaper.getList());
            chooseDate(tiMuList);
        }

    }

    //筛选题目
    private void chooseDate(List<TiMu> list) {
        singleList.clear();
        moreList.clear();
        giveList.clear();
        subjectiveList.clear();
        clozeList.clear();
        partList.clear();
        if (list == null || list.size() == 0) {
            return;
        }
        for (TiMu tm : list) {
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
        Logger.d("单选题目:" + singleList.size() + "多选题目：" + moreList.size() + "判断题目：" + giveList.size()
                + "主观题目：" + subjectiveList.size() + "完型填空题目：" + clozeList.size() + "阅读理解题目：" + partList.size());

        showState();

    }

    /**
     * 根据数据的状态展示相应的题型及题型对应的信息
     */
    private void showState() {
        //单选题目存在
        if (singleList.size() > 0) {
            //计算单选题的总分
            for (TiMu timu : singleList) {
                singTotal = singTotal + timu.getScore();
            }
            tvSingle.setText(MyConfig.getTimuQid().get(1+"")+"、单选题(" + singleList.size() + "题，共" + singTotal + "分)");
            singleAdapter.notifyDataSetChanged();
        } else {
            rlSingle.setVisibility(View.GONE);
        }
        //多选题存在
        if (moreList.size() > 0) {
            //计算多选题的总分
            for (TiMu timu : moreList) {
                moreTotal = moreTotal + timu.getScore();
            }
            tvMore.setText(MyConfig.getTimuQid().get(2+"")+"、多选题(" + moreList.size() + "题，共" + moreTotal + "分)");
            moreAdapter.notifyDataSetChanged();
        } else {
            rlMore.setVisibility(View.GONE);
        }
        //判断题存在
        if (giveList.size() > 0) {
            //计算判断题的总分
            for (TiMu timu : giveList) {
                giveTotal = giveTotal + timu.getScore();
            }
            tvGive.setText(MyConfig.getTimuQid().get(3+"")+"、判断题(" + giveList.size() + "题，共" + giveTotal + "分)");
            giveAdapter.notifyDataSetChanged();
        } else {
            rlGive.setVisibility(View.GONE);
        }
        //主观题存在
        if (subjectiveList.size() > 0) {
            //计算主观题题的总分
            for (TiMu timu : subjectiveList) {
                subjectiveTotal = subjectiveTotal + timu.getScore();
            }
            tvSubjective.setText(MyConfig.getTimuQid().get(4+"")+"、问答题(" + subjectiveList.size() + "题，共" + subjectiveTotal + "分)");
            subjectiveAdapter.notifyDataSetChanged();
        } else {
            rlSubjective.setVisibility(View.GONE);
        }
        //完型填空题目存在
        if (clozeList.size() > 0) {
            //计算完型填空题的总分
            for (TiMu timu : clozeList) {
                clozeTotal = clozeTotal + timu.getScore();
            }
            tvCloze.setText(MyConfig.getTimuQid().get(5+"")+"、完型填空(" + clozeList.size() + "题，共" + clozeTotal + "分)");
            clozeAdapter.notifyDataSetChanged();
        } else {
            rlCloze.setVisibility(View.GONE);
        }
        //阅读理解题目存在
        if (partList.size() > 0) {
            //计算阅读理解题的总分
            for (TiMu timu : partList) {
                paartTotal = paartTotal + timu.getScore();
            }
            tvPart.setText(MyConfig.getTimuQid().get(6+"")+"、阅读理解(" + partList.size() + "题，共" + paartTotal + "分)");
            pratAdapter.notifyDataSetChanged();
        } else {
           rlPart.setVisibility(View.GONE);
        }
    }

    /**
     * 启动Activity
     */
    public static void StartActivity(Context context, String jobsPaper) {
        Intent intent = new Intent(context, AnswerSheetActivity.class);
        intent.putExtra("JobsPaper", jobsPaper);
        context.startActivity(intent);

    }

    @OnClick(R.id.submit_exam)
    public void onViewClicked() {
        EventBus.getDefault().post(new SubmitAnswerEvent());
        finish();

    }


}
