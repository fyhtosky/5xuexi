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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/10/18.
 * 答题卡的适配器
 */
public class AnswerSheetAdapter extends BaseAdapter {
    private Context context;
    private List<TiMu> tmList;
    private Map<String, Object> answerMap;

    public AnswerSheetAdapter(Context context, List<TiMu> tmList, Map<String, Object> answerMap) {
        this.context = context;
        this.tmList = tmList;
        this.answerMap = answerMap;
    }

    @Override
    public int getCount() {
        return tmList == null ? 0 : tmList.size();
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
        SheetHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.answer_sheet_item, parent,false);
            holder=new SheetHolder(convertView);
            convertView.setTag(holder);
        } else {
           holder= (SheetHolder) convertView.getTag();
        }
        //展示数据
        TiMu tm=tmList.get(position);
        String id=tm.getId();
        //设置默认背景
        holder.tv.setBackgroundResource(R.drawable.gray);//初始背景
        for (Map.Entry<String, Object> entry :answerMap.entrySet()) {
            if (id.equals(entry.getKey())) {
                if(tm.getQuestionType()==4){
                    holder.tv.setBackgroundResource(R.drawable.green);
                    if(entry.getValue()==null){
                        holder.tv.setBackgroundResource(R.drawable.gray);
                    }else{
                        String a=entry.getValue().toString();
                        if(a.equals("") ||a.length()==0){
                            holder.tv.setBackgroundResource(R.drawable.gray);//初始背景
                        }
                    }
                }else {
                    if(entry.getValue()==null){
                        holder.tv.setBackgroundResource(R.drawable.gray);
                    }else {
                        holder.tv.setBackgroundResource(R.drawable.green);
                    }
                }


            }
        }
        holder.tv.setText(tm.getQid()+"");
        return convertView;
    }

     class SheetHolder {
        @BindView(R.id.tv)
        TextView tv;

         SheetHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
