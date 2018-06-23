package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.entry.ParseGroupMembers;
import com.sj.yinjiaoyun.xuexi.utils.PicassoUtils;

import java.util.List;

/**
 * Created by wanzhiying on 2017/3/27.
 */
public class ChooseReplyAdapter extends BaseAdapter {
    private List<ParseGroupMembers.DataBean.TigaseGroupUsersBean.RowsBean> list;
    private Context context;

    public ChooseReplyAdapter(List<ParseGroupMembers.DataBean.TigaseGroupUsersBean.RowsBean> list, Context context) {
        this.list = list;
        this.context = context;


    }

    @Override
    public int getCount() {
        return list.size();
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
        final Holder holder;
           if(convertView==null){
               convertView= LayoutInflater.from(context).inflate(R.layout.choose_reply_item,parent,false);
               holder = new Holder();
               holder.item= (RelativeLayout) convertView.findViewById(R.id.item);
               holder.userimg = (ImageView) convertView.findViewById(R.id.user_head);
               holder.username= (TextView) convertView.findViewById(R.id.user_name);
               convertView.setTag(holder);
           }else {
               holder= (Holder) convertView.getTag();
           }
            //加载数据源
        final ParseGroupMembers.DataBean.TigaseGroupUsersBean.RowsBean rowsBean=list.get(position);
        holder.username.setTextColor(Color.BLACK);
        if(rowsBean.getJid().contains("5b_")){
            holder.username.setTextColor(Color.RED);
        }
        //前台
        if(rowsBean.getUserType()==0){
            if(rowsBean.getEndUserVO().getRealName()!=null){
                holder.username.setText(rowsBean.getEndUserVO().getRealName());
            }else{
                holder.username.setText(rowsBean.getEndUserVO().getUserName());
            }

            if(rowsBean.getEndUserVO().getUserImg()!=null){
                if(!"".equals(rowsBean.getEndUserVO().getUserImg())){
                    PicassoUtils.LoadPathCorners(context,rowsBean.getEndUserVO().getUserImg(),holder.userimg);
                }else{
                    PicassoUtils.LoadCorners(context,R.mipmap.default_userhead,holder.userimg);
                }
            }else{
                PicassoUtils.LoadCorners(context,R.mipmap.default_userhead,holder.userimg);
            }
            //后台账号
        }else if(rowsBean.getUserType()==1){
            if(rowsBean.getBackendOperatorVO().getRealName()!=null){
                holder.username.setText(rowsBean.getBackendOperatorVO().getRealName());
            }else{
                holder.username.setText(rowsBean.getBackendOperatorVO().getUserName());
            }
            if(rowsBean.getBackendOperatorVO().getPhoto()!=null){
                if(!"".equals(rowsBean.getBackendOperatorVO().getPhoto())){
                    PicassoUtils.LoadPathCorners(context,rowsBean.getBackendOperatorVO().getPhoto(),holder.userimg);
                }else{
                    PicassoUtils.LoadCorners(context,R.mipmap.default_userhead,holder.userimg);
                }
            }else{
                PicassoUtils.LoadCorners(context,R.mipmap.default_userhead,holder.userimg);
            }
        }

        return convertView;
    }
    class Holder {
        RelativeLayout item;
        ImageView userimg;
        TextView username;

    }
}
