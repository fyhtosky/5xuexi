package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.ScheduleItemActivity;
import com.sj.yinjiaoyun.xuexi.bean.CoureseBean;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/20.
 */
public class CourseItemAdapter extends BaseAdapter {
    private Context context;
    private List<CoureseBean.DataBean.CourseSchedulesBean> list;

    public CourseItemAdapter(Context context, List<CoureseBean.DataBean.CourseSchedulesBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.course_item, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CoureseBean.DataBean.CourseSchedulesBean courseSchedulesBean = list.get(position);
        //设置默认状态
        holder.ivCourseComplete.setVisibility(View.GONE);
        //显示课程的图片
        if (courseSchedulesBean.getCourseLogoUrl().equals("")) {
            Picasso.with(context)
                    .load(R.mipmap.error)
                    .placeholder(R.drawable.progressbar_landing)
                    //加载失败中的图片显示
                    .error(R.mipmap.error).into(holder.ivImage);
        } else {
            Picasso.with(context)
                    .load(courseSchedulesBean.getCourseLogoUrl())
                    //加载过程中的图片显示
                    .placeholder(R.drawable.progressbar_landing)
                    //加载失败中的图片显示
                    .error(R.mipmap.error)
                    //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                    .centerCrop()
                    .resize(500, 315).into(holder.ivImage);
        }
        //展示课程名称
        holder.tvName.setText(courseSchedulesBean.getCourseName());
        //展示学分
        holder.tvCourseCredit.setText("学分:" + courseSchedulesBean.getCourseCredit());
        //展示总成绩
        holder.tvTotalScore.setText("总成绩:" + courseSchedulesBean.getTotalScore());
        //显示学习进度
        if (courseSchedulesBean.getCoursePercent() != null || !(courseSchedulesBean.getCoursePercent().equals(""))) {
            int a = transFormation(courseSchedulesBean.getCoursePercent());
            if (a == 100) {
                holder.ivCourseComplete.setVisibility(View.VISIBLE);
            } else {
                holder.ivCourseComplete.setVisibility(View.GONE);
            }
            holder.reportMajorPercent.setText(a + "%");
            holder.reportMicroProgressbar.setProgress(a);
        } else {
            holder.reportMajorPercent.setText("0%");
            holder.reportMicroProgressbar.setProgress(0);
            holder.ivCourseComplete.setVisibility(View.GONE);
        }
        //添加点击事件
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //视频播放界面
                ScheduleItemActivity.StartActivity(context,String.valueOf(courseSchedulesBean.getId()),courseSchedulesBean.getCourseName(),String.valueOf(courseSchedulesBean.getCourseId()),courseSchedulesBean.getCoursePercent());
            }
        });
        return convertView;
    }

    /**
     * 把字符串型百分数改变成into型整数
     *
     * @param str 字符串型百分数
     * @return 整数
     */
    private int transFormation(String str) {
        int a = 0;
        Pattern p = Pattern.compile("(\\d+)");
        Matcher ma = p.matcher(str);
        if (ma.find()) {
            a = Integer.valueOf(ma.group(1));
        }
        return a;
    }



     class ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.iv_course_complete)
        ImageView ivCourseComplete;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_courseCredit)
        TextView tvCourseCredit;
        @BindView(R.id.tv_totalScore)
        TextView tvTotalScore;
        @BindView(R.id.report_micro_progressbar)
        ProgressBar reportMicroProgressbar;
        @BindView(R.id.report_majorPercent)
        TextView reportMajorPercent;
        @BindView(R.id.item)
        RelativeLayout item;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
