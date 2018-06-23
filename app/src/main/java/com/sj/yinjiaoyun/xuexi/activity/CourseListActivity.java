package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.MyViewPagerAdapter;
import com.sj.yinjiaoyun.xuexi.fragment.CourseItemFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseListActivity extends AppCompatActivity {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.rl_image)
    RelativeLayout rlImage;
    //标识的用户id
//    private String endUserId = "";
    //招生计划的id
//    private String enrollPlanId = "";
    //学期数
//    private int productLearningLength;
    private List<String> list = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
//    private MyViewPagerAdapter myViewPagerAdapter;
    private boolean iscut=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
//        endUserId = PreferencesUtils.getSharePreStr(this, "username");
        list.clear();
        fragmentList.clear();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String enrollPlanId = bundle.getString("enrollPlanId");
            int productLearningLength = bundle.getInt("productLearningLength", 0);
            if (productLearningLength >5) {
                rlImage.setVisibility(View.VISIBLE);
            }else {
                rlImage.setVisibility(View.GONE);
            }
            for (int i = 0; i <=productLearningLength-1; i++) {
                list.add("学期" + (i+1));
                fragmentList.add(CourseItemFragment.newInstance((i+1),enrollPlanId));
            }
            tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            MyViewPagerAdapter myViewPagerAdapter=new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList, list);
            vp.setAdapter(myViewPagerAdapter);
            vp.setOffscreenPageLimit(fragmentList.size());
            tablayout.setupWithViewPager(vp);
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_image:
                iscut=!iscut;
                if(iscut){
                    ivNext.setImageResource(R.mipmap.left_arrow);
                    tablayout.setScrollPosition(0,5,false);
                    if(tablayout.getTabAt(5)!=null){
                        tablayout.getTabAt(5).select();
                    }

                }else {
                    ivNext.setImageResource(R.mipmap.right_arrow);
                    tablayout.setScrollPosition(5,-5,true);
                    if(tablayout.getTabAt(0)!=null){
                        tablayout.getTabAt(0).select();
                    }

                }
                break;
        }
    }

    /**
     * 启动Activity
     */
    public static void StartActivity(Context context, String enrollPlanId, int productLearningLength) {
        Intent intent = new Intent(context, CourseListActivity.class);
        intent.putExtra("enrollPlanId", enrollPlanId);
        intent.putExtra("productLearningLength", productLearningLength);
        context.startActivity(intent);

    }
}
