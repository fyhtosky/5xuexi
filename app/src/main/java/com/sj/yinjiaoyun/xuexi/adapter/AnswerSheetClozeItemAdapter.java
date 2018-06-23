package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.callback.OnItemClickListener;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/10/19.
 * 答题卡中的单选 、多选 、判断、主观题的适配器
 */
public class AnswerSheetClozeItemAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<TiMu> tmList;
    private Map<String, Object> answerMap;


    private OnItemClickListener mOnItemClickListener = null;
    public AnswerSheetClozeItemAdapter(Context context, List<TiMu> tmList, Map<String, Object> answerMap) {
        this.context = context;
        this.tmList = tmList;
        this.answerMap = answerMap;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.answer_sheet_colze_item, parent, false);
        return new ClozeItemHolder(view);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
       ClozeItemHolder itemHolder= (ClozeItemHolder) holder;
        final TiMu tm=tmList.get(position);
        String id=tm.getId();
        //设置默认背景
        itemHolder.tv.setBackgroundResource(R.drawable.gray);//初始背景
        for (Map.Entry<String, Object> entry :answerMap.entrySet()) {
            if (id.equals(entry.getKey())) {
                if(tm.getQuestionType()==4){
                    itemHolder.tv.setBackgroundResource(R.drawable.green);
                    if(entry.getValue()==null){
                        itemHolder.tv.setBackgroundResource(R.drawable.gray);
                    }else{
                        String a=entry.getValue().toString();
                        if(a.equals("") ||a.length()==0){
                            itemHolder.tv.setBackgroundResource(R.drawable.gray);//初始背景
                        }
                    }
                }else {
                    if(entry.getValue()==null){
                        itemHolder.tv.setBackgroundResource(R.drawable.gray);
                    }else {
                        itemHolder.tv.setBackgroundResource(R.drawable.green);
                    }
                }

            }
        }
        itemHolder.tv.setText((position+1)+"");
        //点击事件
        itemHolder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取position
                        mOnItemClickListener.onItemClick(v,position);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return tmList == null ? 0 : tmList.size();
    }

    static class ClozeItemHolder  extends RecyclerView.ViewHolder{
        @BindView(R.id.tv)
        TextView tv;

        ClozeItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
