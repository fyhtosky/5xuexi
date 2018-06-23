package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domains.CourseScheduleVO;
import com.sj.yinjiaoyun.xuexi.utils.MyUtil;
import com.sj.yinjiaoyun.xuexi.view.ProgressBarView;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/16.
 * 课程表  我的微专业  列表
 */
public class MicroAdapter extends BaseAdapter {

    List<CourseScheduleVO> list;
    Context context;
    LayoutInflater inflater;

    public MicroAdapter(List<CourseScheduleVO> list, Context context) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    public void onfresh(List<CourseScheduleVO> list){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        CourseScheduleVO vo= list.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_microclass,parent,false);
            holder.imageView= (ImageView) convertView.findViewById(R.id.item_micro_image);
            holder.tvCouseName= (TextView) convertView.findViewById(R.id.item_micro_courseName);
            holder.tvLearnTime= (TextView) convertView.findViewById(R.id.item_micro_learnTime);
            holder.tvTeacher= (TextView) convertView.findViewById(R.id.item_micro_teacher);
            holder.progressBarView= (ProgressBarView) convertView.findViewById(R.id.item_micro_progress);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        try {
            if (TextUtils.isEmpty(vo.getCourseLogoUrl())) {
                Picasso.with(context)
                        .load(R.mipmap.error)
                        .placeholder(R.drawable.progressbar_landing)
                        //加载失败中的图片显示
                        .error(R.mipmap.error).into(holder.imageView);
            } else {
                Picasso.with(context)
                        .load(vo.getCourseLogoUrl())
                        //加载过程中的图片显示
                        .placeholder(R.drawable.progressbar_landing)
                        //加载失败中的图片显示
                        .error(R.mipmap.error)
                        //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                        .centerCrop()
                        .resize(500, 315).into(holder.imageView);
            }
            holder.tvCouseName.setText(vo.getCourseName());
            holder.tvLearnTime.setText("学习时长:" + MyUtil.secToTime(vo.getTotalLearnTime()));
            String teachName = vo.getTeacherName();
            if (TextUtils.isEmpty(teachName)){
                holder.tvTeacher.setVisibility(View.GONE);
            } else {
                holder.tvTeacher.setVisibility(View.VISIBLE);
                holder.tvTeacher.setText("授课教师:" + vo.getTeacherName());
            }
            holder.progressBarView.setProgressAndMark(vo.getCoursePercent());
        }catch (Exception e){
          e.getLocalizedMessage();
        }
        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        TextView tvCouseName;
        TextView tvLearnTime;
        TextView tvTeacher;
        ProgressBarView progressBarView;
    }

}
