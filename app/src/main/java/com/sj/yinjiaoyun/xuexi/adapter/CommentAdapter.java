package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.CoRows;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/29.
 * 公开课里面 评论 adapter
 */
public class CommentAdapter extends BaseAdapter{

    String TAG="evaluate";
    Context context;
    List<CoRows> list;
    LayoutInflater inflater;
    public CommentAdapter(Context context, List<CoRows> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    public void refresh(List<CoRows> list){
        this.list = list;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        CoRows coRows=list.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_comment,parent,false);
            holder.mHead= (ImageView) convertView.findViewById(R.id.comment_icon);
            holder.mName= (TextView) convertView.findViewById(R.id.comment_name);
            holder.mTime= (TextView) convertView.findViewById(R.id.comment_time);
            holder.mRatingBar= (RatingBar) convertView.findViewById(R.id.comment_ratingBar);
            holder.mText= (TextView) convertView.findViewById(R.id.comment_text);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
         try {
             holder.mText.setText(coRows.getContent());
             holder.mRatingBar.setProgress(coRows.getStars());
             holder.mTime.setText(coRows.getCreateTime());
             Transformation transformation = new RoundedTransformationBuilder()
                     .cornerRadiusDp(30)
                     .oval(false)
                     .build();
             if (TextUtils.isEmpty(coRows.getEndUserAvatar())) {
                 Picasso.with(context).load(coRows.getEndUserAvatar())
                         .placeholder(R.mipmap.default_userhead)////默认显示
                         .transform(transformation)
                         .centerCrop()
                         .fit().error(R.mipmap.default_userhead)//加载错误显示
                         .resize(80, 80)
                         .into(holder.mHead);
             } else {
                 Picasso.with(context)
                         .load(coRows.getEndUserAvatar())
                         .transform(transformation)
                         .error(R.mipmap.default_userhead)
                         .centerCrop()
                         .resize(60, 60)
                         .into(holder.mHead);
             }
             if (coRows.getAnonymous()) {//
                 holder.mName.setText("****");
             } else {
                 holder.mName.setText(coRows.getEndUserName());
             }
         }catch (Exception e){
             Log.e(TAG, "getView: "+e.toString());
         }
        return convertView;
    }

    class ViewHolder{
        ImageView mHead;
        TextView  mName;
        TextView mTime;
        RatingBar mRatingBar;
        TextView mText;
    }

}
