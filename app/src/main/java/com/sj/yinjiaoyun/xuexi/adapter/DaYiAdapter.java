package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.DaYi;
import com.sj.yinjiaoyun.xuexi.utils.StringFormatUtil;
import com.sj.yinjiaoyun.xuexi.utils.TimeUtil;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/4.
 * 答疑
 */
public class DaYiAdapter extends BaseAdapter {

    List<DaYi> list;
    Context context;
    LayoutInflater inflater;
    public DaYiAdapter(List<DaYi> list, Context context) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }
    public void  refresh(List<DaYi> list){
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder;
        DaYi dy=list.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_dayi,parent,false);
            holder.tvTime= (TextView) convertView.findViewById(R.id.dayi_item_time);
            holder.tvQuester= (TextView) convertView.findViewById(R.id.dayi_item_question);
            holder.tvAnswer= (TextView) convertView.findViewById(R.id.dayi_item_answer);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        holder.tvTime.setText(TimeUtil.getTime(dy.getCreateTime()));
        holder.tvQuester.setText(dy.getQuestion());
        String answer=dy.getAnswer();
        if(answer!=null){
            answer=" 老师回答："+answer;
            holder.tvAnswer.setText(answer);
            StringFormatUtil spanStr = new StringFormatUtil(context, answer,
                    "老师回答：", R.color.colorGreen).fillColor();
            holder.tvAnswer.setVisibility(View.VISIBLE);
            holder.tvAnswer.setText(spanStr.getResult());
        }else{
            holder.tvAnswer.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        TextView tvTime;
        TextView tvQuester;
        TextView tvAnswer;
    }
}
