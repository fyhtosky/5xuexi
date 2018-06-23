package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.MyOrderChild;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/24.
 * 订单里面 dialog 的学期选择
 */
public class DialogOrderAdapter extends BaseAdapter{

    List<MyOrderChild> list;
    Context context;
    LayoutInflater inflater;
    public DialogOrderAdapter(Context context, List<MyOrderChild> list) {
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    public void refresh(List<MyOrderChild> list){
        this.list=list;
        notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyOrderChild child= list.get(position);
        TextView tv;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_diaolgorder,parent,false);
            tv= (TextView) convertView.findViewById(R.id.item_dialogorder_xueqi);
            convertView.setTag(tv);
        }
        tv= (TextView) convertView.getTag();
        tv.setText("第"+child.getcSchoolYear()+"学期");
        return convertView;
    }
}
