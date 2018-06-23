package com.sj.yinjiaoyun.xuexi.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sj.yinjiaoyun.xuexi.domain.ProductDirection;
import com.sj.yinjiaoyun.xuexi.view.SingleChoiceView;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/3.
 * 课程表  方向选择 导航按钮
 */
public class NewGridAdapter extends BaseAdapter {

    String TAG="newgrid";

    private List<ProductDirection> list;
    private Activity context;
    LayoutInflater inflater;


    public NewGridAdapter(List<ProductDirection> list, Activity context) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SingleChoiceView singleChoiceView = new SingleChoiceView(context);
        try {
            singleChoiceView.setTitle(list.get(position).getProductDirectionName());
        }catch (Exception e){
            Log.e(TAG, "getView: "+e.toString());
        }
        return singleChoiceView;
    }


}
