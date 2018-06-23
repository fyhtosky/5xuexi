package com.sj.yinjiaoyun.xuexi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by Administrator on 2016/5/30 0030.
 * 引导页,图片轮播时候的
 */
public class GuideFragmentAdapter extends FragmentStatePagerAdapter {

    List<Fragment> list;
    FragmentManager fragmentManager;
    public GuideFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list=list;
    }

    //返回指定位子的item内容
    @Override
    public Fragment getItem(int position) {
        //return list.get(position);//对应一般的操作，直接返回出去
       //如果要求滑动无限循环，则做如下操作
        Fragment fragment=list.get(position%4);
        if(fragment.isAdded()){
            FragmentTransaction tran=fragmentManager.beginTransaction();
            //tran.commit();//异步操作，有可能还没有提交，就返回出去了
            tran.commitAllowingStateLoss();//确保同步操作，提交完成在执行其他代码
            fragmentManager.executePendingTransactions();//辅助同步
        }
        return fragment;//对应重复循环显示的操作
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();//正常返回内容的项数
       // return 100;//对应返回无数项，即重复循环显示
    }

    //刷新数据
    public void refresh(List<Fragment> list){
        this.list=list;

    }
}
