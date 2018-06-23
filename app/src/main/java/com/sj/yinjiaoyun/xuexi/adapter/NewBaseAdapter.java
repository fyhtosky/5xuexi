package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/26.
 */
public abstract class NewBaseAdapter<T> extends BaseAdapter {
    Context context;
    List<T> list;
    LayoutInflater inflater;
    public NewBaseAdapter(Context context, List<T> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    //刷新数据
    public void refresh(List<T> list){
        this.list=list;
        notifyDataSetChanged();
    }

    //添加数据集
    public void addAllData(List<T>_list){
        if(list==null){
            list=new ArrayList<>();
        }
        list.addAll(_list);
        notifyDataSetChanged();
    }

    //添加单项数据
    public void addData(T info){
        if (list==null){
            list=new ArrayList<>();
        }
        list.add(info);
        notifyDataSetChanged();
    }

    //删除某个位置的数据
    public void delete(int position){
        if (list==null){
            return;
        }
        int len=list.size();
        if (position<len){
            list.remove(position);
            notifyDataSetChanged();
        }
    }

}
