package com.sj.yinjiaoyun.xuexi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/1.
 * 主页 -课程表里面  -专业、培训.公开课切换时候的fragment轮播
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    List<Fragment> list;
    public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list=list;
        Log.i("fragmentintro", "FragmentAdapter: "+list.size());
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);//对应一般的操作，直接返回出去
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();//正常返回内容的项数
    }

    //刷新数据
    public void refresh(List<Fragment> list){
        this.list=list;
    }
}
