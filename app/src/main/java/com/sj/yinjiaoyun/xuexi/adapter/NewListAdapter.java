package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.ProductDirection;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/3.
 * 课程表  方向选择 导航按钮
 */
public class NewListAdapter extends BaseAdapter {

    String TAG="adapter";
    Context context;
    List<ProductDirection> list;
    LayoutInflater inflater;
    public NewListAdapter(Context context, List<ProductDirection> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    public void refreshListView(List<ProductDirection> list){
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
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_fangx_list,parent,false);
            holder.name= (TextView) convertView.findViewById(R.id.item_fangx_name);
            holder.text= (TextView) convertView.findViewById(R.id.item_fangx_text);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        try{
            ProductDirection product=list.get(position);
            holder.name.setText(product.getProductDirectionName()+"方向:");
            String desc=list.get(position).getProductDirectionDesc();
            holder.text.setText(Html.fromHtml(desc));
        }catch (Exception e){
            Log.e(TAG, "getView: 没有方向简介"+e);
        }
        return convertView;
    }

    class ViewHolder{
        TextView name;
        TextView text;
    }
}
