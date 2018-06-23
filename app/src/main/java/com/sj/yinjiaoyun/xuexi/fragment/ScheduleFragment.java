package com.sj.yinjiaoyun.xuexi.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.FragmentAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/1.
 * 主Activity - 课程表Fragment
 */
public class ScheduleFragment extends Fragment {


    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.item_head_group)
    RadioGroup itemHeadGroup;
    @BindView(R.id.schedule_underLineC)
    RelativeLayout scheduleUnderLineC;

    private MajorFragment majorFragment;//专业
    private MicroFragment microFragment;//微专业
    private OpenCouseFragment openFragment;//公开课

    private List<Fragment> fragmentList = new ArrayList<>();

    public static ScheduleFragment newInstance() {
        Bundle args = new Bundle();
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, view);
        initEvent();
        initTabLayout();
        return view;
    }

    private void initTabLayout() {
        FragmentAdapter  fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), fragmentList);
        vp.setAdapter(fragmentAdapter);
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton button = (RadioButton)itemHeadGroup .getChildAt(position % 4);
                button.setChecked(true);//
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        itemHeadGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.item_head_rb1://简介
                        changeAnimation(0);
                        vp.setCurrentItem(0);
                        break;
                    case R.id.item_head_rb2://目录
                        changeAnimation(1);
                        vp.setCurrentItem(1);
                        break;
                    case R.id.item_head_rb4://成绩
                        changeAnimation(2);
                        vp.setCurrentItem(2);
                        break;
                }
            }
        });
    }


    /**
     * 对初始化的控件做事件
     */
    private void initEvent() {
        fragmentList.clear();
        majorFragment = new MajorFragment();//专业
        microFragment = new MicroFragment();//微专业
        openFragment = new OpenCouseFragment();//公开课
        fragmentList.add(majorFragment);
        fragmentList.add(microFragment);
        fragmentList.add(openFragment);
        setUnderLine();
    }

    /**
     * 给下划线设置宽
     */
    private void setUnderLine() {
        // 获取屏幕密度（方法2）
        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        int screenWidth2 = dm2.widthPixels;      // 屏幕宽（像素，如：480px）
        //设置动画图片的图片宽度
        RelativeLayout.LayoutParams laParams = (RelativeLayout.LayoutParams) scheduleUnderLineC.getLayoutParams();
        laParams.width = screenWidth2 / 3;
        scheduleUnderLineC.setLayoutParams(laParams);
        changeAnimation(0);
    }

    //下划线切换时候的动画效果
    private void changeAnimation(int arg0) {
        switch (arg0) {
            case 0:
                Animation animationL = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_three_o);
                scheduleUnderLineC.startAnimation(animationL);//开始动画
                animationL.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
            case 1://表示动画从左到右
                Animation animationR = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_three_t);
                scheduleUnderLineC.startAnimation(animationR);//开始动画
                animationR.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
            case 2://表示动画初始化（登录进来时候）
                Animation animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_three_th);
                scheduleUnderLineC.startAnimation(animation3);//开始动画
                animation3.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
        }
    }
}
