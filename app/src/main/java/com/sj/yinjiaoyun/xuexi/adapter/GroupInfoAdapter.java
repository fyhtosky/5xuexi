package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.ChatActivity;
import com.sj.yinjiaoyun.xuexi.entry.GroupUserInfo;
import com.sj.yinjiaoyun.xuexi.utils.DensityUtils;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wanzhiying on 2017/3/14.
 */
public class GroupInfoAdapter extends BaseAdapter {

    private Context context;
    private List<GroupUserInfo> mDatas;
    LayoutInflater inflater;
    private String receiver;
    private int width;

    public GroupInfoAdapter(Context context, List<GroupUserInfo> mDatas,int width) {
        this.context = context;
        this.mDatas = mDatas;
        inflater=LayoutInflater.from(context);
        receiver = "5f_"+ PreferencesUtils.getSharePreStr(context, "username");
        this.width=width;

    }

    public void freshData(List<GroupUserInfo> mDatas) {
        this.mDatas=mDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas==null?0:mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GroupUserInfo a=mDatas.get(position);
        int itemWidth=(width- 6*DensityUtils.dp2px(context,2))/5;
        MyViewHolder holder;
        if(convertView==null){
            holder=new MyViewHolder();
            convertView=inflater.inflate(R.layout.item_group_info, parent, false);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(param);
            holder.item= (RelativeLayout) convertView.findViewById(R.id.group_info_item);
            holder.tv= (TextView) convertView.findViewById(R.id.item_group_info_Name);
            holder.iv= (ImageView) convertView.findViewById(R.id.item_group_info_Image);
            convertView.setTag(holder);
        }else{
            holder= (MyViewHolder) convertView.getTag();
        }
        holder.tv.setTextColor(Color.BLACK);
       if(a.getJid().contains("5b_")){
           holder.tv.setTextColor(Color.RED);
       }
        holder.tv.setText(a.getName());
        if(a.getImg()!=null){
            if(a.getImg().equals("")){
                Picasso.with(context).load(R.mipmap.default_userhead).resize(60,60).centerCrop().into(holder.iv);
            }else {
                Picasso.with(context).load(a.getImg()).resize(60,60).centerCrop().into(holder.iv);
            }
        }else{
            Picasso.with(context).load(R.mipmap.default_userhead).resize(60,60).centerCrop().into(holder.iv);
        }
         holder.item.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(!receiver.equals(a.getJid())){
                     //跳转操作
                     Intent intent=new Intent(context, ChatActivity.class);
                     intent.putExtra(Const.CHAT_NAME,a.getName());
                     intent.putExtra(Const.CHAT_ID,a.getJid());
                     intent.putExtra(Const.CHAT_LOGO,a.getImg());
                     context.startActivity(intent);
                 }else{
                     ToastUtil.showShortToast(context,"不能和自己聊天");
                 }

             }
         });
        return convertView;
    }

    class MyViewHolder{
        RelativeLayout item;
        ImageView iv;
        TextView tv;
    }
}
