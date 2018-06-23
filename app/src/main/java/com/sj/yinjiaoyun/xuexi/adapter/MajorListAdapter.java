package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.CourseVO;
import com.sj.yinjiaoyun.xuexi.view.ProgressBarView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/9/7.
 * 首页 当专业类型 培训 考证时候的 listView
 */
public class MajorListAdapter extends BaseAdapter {

    Context context;
    List<CourseVO> list;
    LayoutInflater inflater;
    public MajorListAdapter(Context context, List<CourseVO> list) {
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    //刷新数据
    public void refresh(List<CourseVO> list){
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
        CourseVO courseVO =  list.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_tabulation_child, parent,false);
            holder.ivCouseState= (ImageView) convertView.findViewById(R.id.expand_child_courseState);
            holder.ivCourseImage= (ImageView) convertView.findViewById(R.id.expend_child_image);
            holder.tvCourseName= (TextView) convertView.findViewById(R.id.expand_child_courseName);
            holder.progressBarView= (ProgressBarView) convertView.findViewById(R.id.expand_child_progress);
            holder.tvTotalScore= (TextView) convertView.findViewById(R.id.expand_child_totalScore);
            holder.tvCourseCredit= (TextView) convertView.findViewById(R.id.expand_child_courseCredit);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
      //  Picasso.with(context).load(courseVO.getCourseLogoUrl()).centerCrop().resize(500,315).into(holder.ivCourseImage);
        if(courseVO.getCourseLogoUrl().equals("")){
            Picasso.with(context)
                    .load(R.mipmap.error)
                    .placeholder(R.drawable.progressbar_landing)
                    //加载失败中的图片显示
                    .error(R.mipmap.error).into(holder.ivCourseImage);
        }else{
            Picasso.with(context)
                    .load(courseVO.getCourseLogoUrl())
                    //加载过程中的图片显示
                    .placeholder(R.drawable.progressbar_landing)
                    //加载失败中的图片显示
                    .error(R.mipmap.error)
                    //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                    .centerCrop()
                    .resize(500, 315).into(holder.ivCourseImage);
        }
        holder.tvCourseName.setText(courseVO.getCourseName());//课程名字
        holder.progressBarView.setProgressAndMark(courseVO.getCoursePercent());
        holder.tvTotalScore.setText("总成绩："+ courseVO.getTotalScore());
        holder.tvCourseCredit.setText("学分："+ courseVO.getCourseCredit());
        /*Log.i("home", "getChildView: "+courseVO.getCourseLogoUrl()+":"+courseVO.getCourseName()+"进度："+courseVO.getCoursePercent()
                +"总成绩："+courseVO.getTotalScore()+"学分："+courseVO.getCourseCredit());*/
        return convertView;
    }
    class ViewHolder{
        ImageView ivCouseState;//是否已付款标记
        ImageView ivCourseImage;
        TextView tvCourseName;
        ProgressBarView progressBarView;
        TextView tvTotalScore;
        TextView tvCourseCredit;
    }
}
