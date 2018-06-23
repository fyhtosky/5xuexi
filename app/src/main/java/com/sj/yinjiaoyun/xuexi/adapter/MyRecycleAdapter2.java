package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.GroupChatActivity;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroupVO;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanzhiying on 2017/3/8.
 */
public class MyRecycleAdapter2 extends RecyclerView.Adapter {
    public static final int TYPETWO = 2;
    public static final int TYPEONE = 1;
    private Context mContext;
    private List<TigaseGroupVO> list;

    public MyRecycleAdapter2(Context mContext, List<TigaseGroupVO> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPEONE){
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.recycle_view_item3, parent, false);
            return  new ViewHolder(view1);
        }else{
            View view2 = LayoutInflater.from(mContext).inflate(R.layout.recycle_view_item2, parent, false);
            return new MyViewHolder2(view2);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getProductDirectionName() != null) {
            return TYPETWO;
        } else {
            return TYPEONE;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TigaseGroupVO childTigaseGroupVOsBean = list.get(position);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(2)
                .oval(false)
                .build();
        switch (getItemViewType(position)){
            case TYPEONE:
                ViewHolder viewHolder= (ViewHolder )holder;
                Picasso.with(mContext).load(R.mipmap.kechen).resize(75, 75).centerCrop().transform(transformation).into(viewHolder.ivChildItemIcon);
                viewHolder.tvChildItemCousename.setText(childTigaseGroupVOsBean.getBusinessName());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GroupChatActivity.class);
                        intent.putExtra(Const.GROUP_ID, childTigaseGroupVOsBean.getGroupId());
                        intent.putExtra(Const.GROUP_NAME, childTigaseGroupVOsBean.getBusinessName());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case TYPETWO:
                MyViewHolder2 viewHolder2 = (MyViewHolder2) holder;
                Picasso.with(mContext).load(R.mipmap.kechen).resize(75, 75).centerCrop().transform(transformation).into(viewHolder2.ivChildItemIcon);
                viewHolder2.tvChildItemCousename.setText(childTigaseGroupVOsBean.getBusinessName());
                viewHolder2.tvChildItemFangxiang.setText("方向：" + childTigaseGroupVOsBean.getProductDirectionName());
                viewHolder2.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GroupChatActivity.class);
                        intent.putExtra(Const.GROUP_ID, childTigaseGroupVOsBean.getGroupId());
                        intent.putExtra(Const.GROUP_NAME, childTigaseGroupVOsBean.getBusinessName());
                        mContext.startActivity(intent);
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;

    }

    class MyViewHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_child_item_icon)
        ImageView ivChildItemIcon;
        @BindView(R.id.tv_child_item_cousename)
        TextView tvChildItemCousename;
        @BindView(R.id.tv_child_item_fangxiang)
        TextView tvChildItemFangxiang;

        public MyViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_child_item_icon)
        ImageView ivChildItemIcon;
        @BindView(R.id.ll)
        LinearLayout ll;
        @BindView(R.id.tv_child_item_cousename)
        TextView tvChildItemCousename;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
