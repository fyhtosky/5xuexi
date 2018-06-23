package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
 * 答题卡中的完型填空、阅读理解的二级适配器
 */
public class AnswerSheetClozeAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<TiMu> tmList;
    private Map<String, Object> answerMap;
    private OnItemClickListener mOnItemClickListener = null;

    public AnswerSheetClozeAdapter(Context context, List<TiMu> tmList, Map<String, Object> answerMap) {
        this.context = context;
        this.tmList = tmList;
        this.answerMap = answerMap;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.answer_sheet_cloze_list, parent, false);
        return new SheetClozeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SheetClozeHolder sheetClozeHolder= (SheetClozeHolder) holder;
        final TiMu tm=tmList.get(position);
        sheetClozeHolder.tvQid.setText((position+1)+":");
        sheetClozeHolder.rvItem.setLayoutManager(new GridLayoutManager(context, 9,LinearLayoutManager.VERTICAL, false));
        SheetClozeItemAdapter itemAdapter = new SheetClozeItemAdapter(context, tm.getChildrenList(),answerMap);
        sheetClozeHolder.rvItem.setAdapter(itemAdapter);
        sheetClozeHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tmList == null ? 0 : tmList.size();
    }

    static class SheetClozeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_qid)
        TextView tvQid;
        @BindView(R.id.rv_item)
        RecyclerView rvItem;
        @BindView(R.id.item)
        RelativeLayout item;

        SheetClozeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
