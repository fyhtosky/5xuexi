package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.OpenCourseVO;
import com.sj.yinjiaoyun.xuexi.view.ProgressBarView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/24.
 * 课程表 公开课 里面的adapter
 */
public class OpenCouseAdapter extends BaseAdapter {

    String TAG="openfragment";
    Context context;
    List<OpenCourseVO> list;
    LayoutInflater inflater;
    public OpenCouseAdapter(Context context, List<OpenCourseVO> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    public void onFresh(List<OpenCourseVO> list){
        this.list=list;
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

    ViewHolder holder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OpenCourseVO couses=list.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_openclass,parent,false);
            holder.mIcon= (ImageView) convertView.findViewById(R.id.openclass_image);
            holder.mCollogeName= (TextView) convertView.findViewById(R.id.openclass_collegeName);
            holder.mAttention= (TextView) convertView.findViewById(R.id.openclass_attention);
            holder.mbarView= (ProgressBarView) convertView.findViewById(R.id.openclass_progress);
            holder.tvName= (TextView) convertView.findViewById(R.id.openclass_courseName);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        try {
            if (couses.getCourseLogo().equals("")) {
                Picasso.with(context)
                        .load(R.mipmap.error)
                        .placeholder(R.drawable.progressbar_landing)
                        //加载失败中的图片显示
                        .error(R.mipmap.error).into(holder.mIcon);
            } else {
                Picasso.with(context).load(couses.getCourseLogo()).placeholder(R.drawable.progressbar_landing).error(R.mipmap.error).centerCrop().resize(500, 315).into(holder.mIcon);
            }
            holder.tvName.setText(couses.getCourseName());
            holder.mCollogeName.setText(couses.getCollegeName());
            holder.mAttention.setText("" + couses.getNumber());//"人数："
            if (couses.getOpenCoursePercent() == null) {
                holder.mbarView.setProgressAndMark("");
            } else {
                holder.mbarView.setProgressAndMark(couses.getOpenCoursePercent());
            }
        }catch (Exception e){
          e.getLocalizedMessage();
        }
        return convertView;
    }

    class ViewHolder{
        ImageView mIcon;
        TextView tvName;
        TextView mCollogeName;
        TextView mAttention;
        ProgressBarView mbarView;
    }

}
