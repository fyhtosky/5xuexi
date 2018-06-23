package com.sj.yinjiaoyun.xuexi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.GuideFragmentAdapter;
import com.sj.yinjiaoyun.xuexi.fragment.PaperFourFragment;
import com.sj.yinjiaoyun.xuexi.fragment.PaperOneFragment;
import com.sj.yinjiaoyun.xuexi.fragment.PaperThreeFragment;
import com.sj.yinjiaoyun.xuexi.fragment.PaperTwoFragment;
import com.sj.yinjiaoyun.xuexi.manager.UpdateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/24.
 * 引导页面（安装程序时候只进来一次）
 */
public class GuideActivity extends FragmentActivity {

    private ViewPager vp;
    private GuideFragmentAdapter adapter;
    List<Fragment> list;

    // 底部小点图片
    RadioGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guide);

       //group= (RadioGroup) findViewById(R.id.guide_group);
        vp = (ViewPager) findViewById(R.id.viewpager);
        // 初始化页面
        setListData();
        adapter=new GuideFragmentAdapter(getSupportFragmentManager(),list);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // 当当前页面被滑动时调用
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            // 当新的页面被选中时调用
            @Override
            public void onPageSelected(int position) {
              /*  Log.i("guide", "onPageSelected: "+position);
                if(position<3){
                    group.setVisibility(View.VISIBLE);
                    RadioButton rb= (RadioButton) group.getChildAt(position);
                    rb.setChecked(true);
                }else{
                    group.setVisibility(View.GONE);
                }*/
            }

            // 当滑动状态改变时调用
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //给list集合设置数据
    public void setListData(){
        list=new ArrayList<>();//fragment的数据集
        Fragment fragmentOne=new PaperOneFragment();
        Fragment fragmentTwo=new PaperTwoFragment();
        Fragment fragmentThree=new PaperThreeFragment();
        Fragment fragmentFour=new PaperFourFragment();
        list.add(fragmentOne);
        list.add(fragmentTwo);
        list.add(fragmentThree);
        list.add(fragmentFour);
    }

}