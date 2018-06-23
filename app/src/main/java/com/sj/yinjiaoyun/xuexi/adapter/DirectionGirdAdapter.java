package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.DirectionBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/20.
 */
public class DirectionGirdAdapter extends BaseAdapter {
    private Context context;
    private List<DirectionBean.DataBean.EnrollPlanDirectionsBean> list;
    private int selectorPosition=Integer.MAX_VALUE;

    public DirectionGirdAdapter(Context context, List<DirectionBean.DataBean.EnrollPlanDirectionsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dirction_gird_item, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DirectionBean.DataBean.EnrollPlanDirectionsBean enrollPlanDirectionsBean = list.get(position);
        holder.textview.setText(enrollPlanDirectionsBean.getProductDirectionName());
        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == position) {
            holder.checkbox.setChecked(true);
        } else {
            //其他的恢复原来的状态
            holder.checkbox.setChecked(false);
        }
        return convertView;
    }

    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();

    }
    static class ViewHolder {
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.textview)
        TextView textview;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
