package com.sj.yinjiaoyun.xuexi.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.FragmentAdapter;
import com.sj.yinjiaoyun.xuexi.domains.MicroFuse;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.fragment.MicroHintCurriculumFragment;
import com.sj.yinjiaoyun.xuexi.fragment.MicroHintIntroFragment;
import com.sj.yinjiaoyun.xuexi.fragment.EvaluateFragment;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/9.
 * 微专业预览页面(即详情页面)   简介 、教学计划 、评论
 */
public class MicroHintActivity extends MyBaseActivity implements MicroHintIntroFragment.CallBackFromMicroHintIntro {

    String TAG = "microhintactivity";
    Intent intent;
    MicroFuse microFuse;
   private String endUserId;
    int orderStatus;//定单的状况 101 订单存在已付款等待审核，100订单存在未付款，109订单存在查实取消状态 120 订单不存在   ( 104 订单完成)
    String orderCode;//101状态订单未完成的订单号

    FragmentManager manager;
    RadioGroup group;
    ImageView ivBack;

    FragmentAdapter fragmentAdapter;
    List<android.support.v4.app.Fragment> listFragment;
    ViewPager viewPager;

    MicroHintIntroFragment microHintIntroFragment;
    MicroHintCurriculumFragment microHintCurriculumFragment;
    EvaluateFragment microHintEvaluateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_microhint);
        init();
        initView();
    }


    private void init() {
        manager = getFragmentManager();
        intent = getIntent();
        endUserId= PreferencesUtils.getSharePreStr(MicroHintActivity.this, "username");
        microFuse = intent.getParcelableExtra("MicroFuse");
        orderStatus = intent.getIntExtra("orderStatus", 0);
        Log.i(TAG, "init 微专业预览页面：");
        Log.i(TAG, "init:用户id： " + endUserId + "订单状况" + orderStatus);
        Log.i(TAG, "init:专业信息： " + microFuse.toString());
        if (orderStatus == 101 || orderStatus == 100 ) {//101 订单存在已付款等待审核，100订单存在未付款，109订单存在查实取消状态
            orderCode = intent.getStringExtra("orderCode");
            Log.i(TAG, "setGoToPayOrBug: 订单号" + orderCode);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.micro_hint_viewpager);
        ivBack = (ImageView) findViewById(R.id.micro_hint_back);
        group = (RadioGroup) findViewById(R.id.micro_hint_radioGroup);
        group.setOnCheckedChangeListener(changeListener);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createFragment();
        fragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),listFragment);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                RadioButton button= (RadioButton) group.getChildAt(position%4);
                button.setChecked(true);//
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 创建fragment
     */
    private void createFragment() {
        listFragment=new ArrayList<>();
        microHintIntroFragment = new MicroHintIntroFragment();//简介
        Logger.d("MicroHintCurriculumFragment:"+microFuse.toString());
        microHintIntroFragment.setDateForActivity(this,orderStatus,microFuse,orderCode);

        microHintCurriculumFragment = new MicroHintCurriculumFragment();//课程体系
        Logger.d("MicroHintCurriculumFragment:"+microFuse.getMicroId());
        microHintCurriculumFragment.setTrainingIdForActivity(microFuse.getMicroId());


        microHintEvaluateFragment=new EvaluateFragment();
        microHintEvaluateFragment.setDateFromActivity(microFuse.getMicroId(),2);

        listFragment.add(microHintIntroFragment);
        listFragment.add(microHintCurriculumFragment);
        listFragment.add(microHintEvaluateFragment);
    }



    RadioGroup.OnCheckedChangeListener changeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.micro_hint_rb1://简介
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.micro_hint_rb2://课程体系
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.micro_hint_rb3://评价
                    viewPager.setCurrentItem(2);
                    break;
            }
        }
    };

    String trainingItemId;
    @Override
    public void deliveryToCurriclum(String trainingItemId) {
        Log.i(TAG, "deliveryToCurriclum: "+(microHintCurriculumFragment==null) +trainingItemId);
        if(microHintCurriculumFragment!=null){
            microHintCurriculumFragment.setTrainingItemIdForActivity(trainingItemId);
        }else{
            this.trainingItemId=trainingItemId;
        }
    }


}
