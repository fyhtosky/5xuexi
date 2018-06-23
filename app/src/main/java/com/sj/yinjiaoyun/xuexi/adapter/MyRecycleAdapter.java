package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.GroupChatActivity;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroupVO;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroups;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanzhiying on 2017/3/7.
 */
public class MyRecycleAdapter extends RecyclerView.Adapter {
    private List<TigaseGroups> list;
    private Context Mcontext;
    private SparseBooleanArray array;
    public MyRecycleAdapter(List<TigaseGroups> list, Context mcontext) {
        this.list = list;
        Mcontext = mcontext;
        array=new SparseBooleanArray();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(Mcontext).inflate(R.layout.recycle_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHodler = (MyViewHolder) holder;
        final TigaseGroups tigaseGroupsBean = list.get(position);
        Logger.d("Position:"+position +"集合："+tigaseGroupsBean.getChildTigaseGroupVOs());
        final Transformation transformation = new RoundedTransformationBuilder()
                //.borderColor(R.color.colorWrite)
                //.borderWidthDp(0.1f)
                .cornerRadiusDp(2)
                .oval(false)
                .build();
       //招生专业类型
        if(0==tigaseGroupsBean.getTigaseGroupVO().getType()){
            Picasso.with(Mcontext).load(R.mipmap.xueli).resize(85,85).centerCrop().transform(transformation).into(viewHodler.ivItemIcon);
          if(0==tigaseGroupsBean.getTigaseGroupVO().getProductType()){
              if(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()!=null){
                  viewHodler.tvItemDescribe.setText(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()+" 网教");
              }else{
                  viewHodler.tvItemDescribe.setText("网教");
              }

          }else if(1==tigaseGroupsBean.getTigaseGroupVO().getProductType()){
              if(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()!=null){
                  viewHodler.tvItemDescribe.setText(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()+" 成教");
              }else{
                  viewHodler.tvItemDescribe.setText("成教");
              }

          }else if(2==tigaseGroupsBean.getTigaseGroupVO().getProductType()){
              if(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()!=null){
                  viewHodler.tvItemDescribe.setText(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()+" 自考");
              }else{
                  viewHodler.tvItemDescribe.setText("自考");
              }

          }else if(3==tigaseGroupsBean.getTigaseGroupVO().getProductType()){
              if(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()!=null){
                  viewHodler.tvItemDescribe.setText(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()+" 培训");
              }else{
                  viewHodler.tvItemDescribe.setText("培训");
              }

          }else if(4==tigaseGroupsBean.getTigaseGroupVO().getProductType()){
              if(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()!=null){
                  viewHodler.tvItemDescribe.setText(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()+" 考证");
              }else{
                  viewHodler.tvItemDescribe.setText("考证");
              }

          }else if(5==tigaseGroupsBean.getTigaseGroupVO().getProductType()){
              if(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()!=null){
                  viewHodler.tvItemDescribe.setText(tigaseGroupsBean.getTigaseGroupVO().getEnrollPlanName()+" 统招");
              }else{
                  viewHodler.tvItemDescribe.setText("统招");
              }

          }
            //公开课类型课程类型
        }else if(1==tigaseGroupsBean.getTigaseGroupVO().getType()){
            Picasso.with(Mcontext).load(R.mipmap.gongkaike).resize(85,85).centerCrop().transform(transformation).into(viewHodler.ivItemIcon);
            if(0==tigaseGroupsBean.getTigaseGroupVO().getOpenCourseType()){
                viewHodler.tvItemDescribe.setText("免费");
            }else if(1==tigaseGroupsBean.getTigaseGroupVO().getOpenCourseType()){
                viewHodler.tvItemDescribe.setText("付费");
            }
            //微专业类型
        }else if(2==tigaseGroupsBean.getTigaseGroupVO().getType()){
            Picasso.with(Mcontext).load(R.mipmap.weizhuanye).resize(85,85).centerCrop().transform(transformation).into(viewHodler.ivItemIcon);
             if(0==tigaseGroupsBean.getTigaseGroupVO().getTrainingType()){
                 if(tigaseGroupsBean.getTigaseGroupVO().getTrainingItemName()!=null){
                     viewHodler.tvItemDescribe.setText("随到随学 "+tigaseGroupsBean.getTigaseGroupVO().getTrainingItemName());
                 }else{
                     viewHodler.tvItemDescribe.setText("随到随学");
                 }

             }else if(1==tigaseGroupsBean.getTigaseGroupVO().getTrainingType()){
                 if(tigaseGroupsBean.getTigaseGroupVO().getTrainingItemName()!=null){
                     viewHodler.tvItemDescribe.setText("定期开课 "+tigaseGroupsBean.getTigaseGroupVO().getTrainingItemName());
                 }else{
                     viewHodler.tvItemDescribe.setText("定期开课");
                 }

             }
        }
        if(tigaseGroupsBean.getTigaseGroupVO().getCollegeName()!=null){
            viewHodler.tvItemMajor.setText(tigaseGroupsBean.getTigaseGroupVO().getBusinessName()+"-"+tigaseGroupsBean.getTigaseGroupVO().getCollegeName());
        }else{
            viewHodler.tvItemMajor.setText(tigaseGroupsBean.getTigaseGroupVO().getBusinessName());
        }



        //设置布局管理器(子集菜单)
        viewHodler.ivItemSwitch.setVisibility(View.INVISIBLE);
        viewHodler.rvItem.setVisibility(View.GONE);
        viewHodler.rvItem.setLayoutManager(new LinearLayoutManager(Mcontext, LinearLayoutManager.VERTICAL, false));
        if(tigaseGroupsBean.getChildTigaseGroupVOs()!=null){
            viewHodler.rvItem.setAdapter(new MyRecycleAdapter2(Mcontext,tigaseGroupsBean.getChildTigaseGroupVOs()));
            viewHodler.ivItemSwitch.setVisibility(View.VISIBLE);
        }else{
            viewHodler.rvItem.setAdapter(new MyRecycleAdapter2(Mcontext,new ArrayList<TigaseGroupVO>()));
            viewHodler.ivItemSwitch.setVisibility(View.INVISIBLE);
        }
        //初始化状态防止复用数据紊乱
        viewHodler.ivItemSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array.put(position,!array.get(position));
                viewHodler.rvItem.setVisibility(viewHodler.rvItem.getVisibility() == View.VISIBLE ?
                        View.VISIBLE : View.GONE);
                switch (viewHodler.rvItem.getVisibility()){
                    case  View.VISIBLE:
                        viewHodler.ivItemSwitch.setImageResource(R.mipmap.dropdown_no);
                        break;
                    case View.GONE:
                        viewHodler.ivItemSwitch.setImageResource(R.mipmap.dropdown_yes);
                        break;

                }
                if(array.get(position)){
                    viewHodler.rvItem.setVisibility(View.GONE);
                    viewHodler.ivItemSwitch.setImageResource(R.mipmap.dropdown_no);
                }else{
                    viewHodler.rvItem.setVisibility(View.VISIBLE);
                    viewHodler.ivItemSwitch.setImageResource(R.mipmap.dropdown_yes);
                }
            }
        });
        //获取用户操作的状态
            if(array.get(position)){
                viewHodler.rvItem.setVisibility(View.GONE);
                viewHodler.ivItemSwitch.setImageResource(R.mipmap.dropdown_no);
            }else{
                viewHodler.rvItem.setVisibility(View.VISIBLE);
                viewHodler.ivItemSwitch.setImageResource(R.mipmap.dropdown_yes);
            }
        viewHodler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Mcontext, GroupChatActivity.class);
                intent.putExtra(Const.GROUP_ID,tigaseGroupsBean.getTigaseGroupVO().getGroupId());
                intent.putExtra(Const.GROUP_NAME,tigaseGroupsBean.getTigaseGroupVO().getBusinessName());
                Mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_icon)
        ImageView ivItemIcon;
        @BindView(R.id.tv_item_major)
        TextView tvItemMajor;
        @BindView(R.id.tv_item_describe)
        TextView tvItemDescribe;
        @BindView(R.id.iv_item_switch)
        ImageView ivItemSwitch;
        @BindView(R.id.rv_item)
        RecyclerView rvItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}
