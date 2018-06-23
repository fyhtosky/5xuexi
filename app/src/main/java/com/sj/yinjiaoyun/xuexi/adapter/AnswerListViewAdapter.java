package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/5.
 * 作答报告  里面的GridAdapter
 */
public class AnswerListViewAdapter extends BaseAdapter {


    private List<TiMu> tmList;
    private Context context;
    private int jobState=1;
    public AnswerListViewAdapter(List<TiMu> tm, Context context, int jobState) {
        this.tmList = tm;
        this.context = context;
    }

    public void onFresh(List<TiMu> tmList, int jobState){
        this.tmList=tmList;
        notifyDataSetChanged();
        this.jobState=jobState;
    }


    @Override
    public int getCount() {
        return tmList==null?0:tmList.size();
    }

    @Override
    public Object getItem(int position) {
        return tmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TiMu tm=tmList.get(position);
        ViewHolder  holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.item_answerlistview,parent,false);
            holder.tvQid= (TextView) convertView.findViewById(R.id.item_answer_qid);
            holder.tvDafen= (TextView) convertView.findViewById(R.id.item_answer_defen);
            holder.tvTotal= (TextView) convertView.findViewById(R.id.item_answer_total);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        int flag=tm.getAnswerFlag();
        //设置背景颜色
        if(flag==0){//未做
            holder.tvQid.setBackgroundResource(R.drawable.bg_large_gray);
        }else if(flag==1 || flag==2 ){//已做
            holder.tvQid.setBackgroundResource(R.drawable.bg_large_green);
        }
        //设置批改内容
        if(jobState==2){
          //  holder.tvQid.setBackgroundResource(R.drawable.bg_large_skyblue);
            holder.tvDafen.setText("得分："+"未批改");
        }else{
            holder.tvDafen.setText("得分："+tm.getStuScore()+"分");
        }
        holder.tvQid.setText(tm.getQid()+"");
        holder.tvQid.setPadding(10,10,10,10);
        holder.tvTotal.setText("（总分"+tm.getScore()+"分)");
        return convertView;
    }
    class ViewHolder{
        TextView tvQid;
        TextView tvDafen;
        TextView tvTotal;
    }
}
