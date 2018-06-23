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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/10/19.
 * 答题卡中的单选 、多选 、判断、主观题的适配器
 */
public class AnswerReportAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<TiMu> tmList;
    private  int jobState;



    private OnItemClickListener mOnItemClickListener = null;

    public AnswerReportAdapter(Context context, List<TiMu> tmList) {
        this.context = context;
        this.tmList = tmList;
    }

    public void setJobState(int jobState) {
        this.jobState = jobState;
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
        //设置默认背景
        itemHolder.tv.setBackgroundResource(R.drawable.gray);//初始背景
//        -1表示未成功判断   1表示正确 2表示错误 0表示没做
        if(jobState==2){//未批阅
            switch (tm.getAnswerFlag()){
                case 0:
                    itemHolder.tv.setBackgroundResource(R.drawable.gray);
                    break;
                case 1:
                    itemHolder.tv.setBackgroundResource(R.drawable.green);
                    break;
                case 2:
                    itemHolder.tv.setBackgroundResource(R.drawable.green);
                    break;
            }
        }else {//已批阅
            switch (tm.getAnswerFlag()){
                case 0:
                    itemHolder.tv.setBackgroundResource(R.drawable.red);
                    break;
                case 1:
                    itemHolder.tv.setBackgroundResource(R.drawable.green);
                    break;
                case 2:
                    itemHolder.tv.setBackgroundResource(R.drawable.red);
                    break;
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
