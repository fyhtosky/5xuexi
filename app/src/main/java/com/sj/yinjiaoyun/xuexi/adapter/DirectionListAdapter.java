package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.DirectionBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/20.
 */
public class DirectionListAdapter extends BaseAdapter {
    private Context context;
    private List<DirectionBean.DataBean.EnrollPlanDirectionsBean> list;

    public DirectionListAdapter(Context context, List<DirectionBean.DataBean.EnrollPlanDirectionsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list == null ? 0 : list.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fangx_list, parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        DirectionBean.DataBean.EnrollPlanDirectionsBean enrollPlanDirectionsBean = list.get(position);
        holder.itemName.setText(enrollPlanDirectionsBean.getProductDirectionName()+"方向：");
        holder.itemText.setText(Html.fromHtml(enrollPlanDirectionsBean.getProductDirectionDesc()));
        return convertView;
    }

     class ViewHolder {
        @BindView(R.id.item_fangx_name)
        TextView itemName;
        @BindView(R.id.item_fangx_text)
        TextView itemText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
