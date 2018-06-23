package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.ChoiceDirectionActivity;
import com.sj.yinjiaoyun.xuexi.activity.CourseListActivity;
import com.sj.yinjiaoyun.xuexi.bean.MajorBean;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/19.
 */
public class MajorAdapter extends BaseAdapter {
    private Context context;
    private List<MajorBean.DataBean.SoaProductVOsBean> list;

    public MajorAdapter(Context context, List<MajorBean.DataBean.SoaProductVOsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.major_item, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MajorBean.DataBean.SoaProductVOsBean soaProductVOsBean = list.get(position);
        //显示默认数据
        holder.tvDirection.setVisibility(View.GONE);
        holder.reportContainerProgress.setVisibility(View.GONE);
        holder.tvChooseOr.setVisibility(View.GONE);
        //显示课程的图片
        if (soaProductVOsBean.getProductLogoUrl().equals("")) {
            Picasso.with(context)
                    .load(R.mipmap.error)
                    .placeholder(R.drawable.progressbar_landing)
                    //加载失败中的图片显示
                    .error(R.mipmap.error).into(holder.ivImage);
        } else {
            Picasso.with(context)
                    .load(soaProductVOsBean.getProductLogoUrl())
                    //加载过程中的图片显示
                    .placeholder(R.drawable.progressbar_landing)
                    //加载失败中的图片显示
                    .error(R.mipmap.error)
                    //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                    .centerCrop()
                    .resize(500, 315).into(holder.ivImage);
        }
        //展示课程名称
        holder.tvName.setText(soaProductVOsBean.getProductName());
        //展示课程专业方向
        if(soaProductVOsBean.getStatusDirection()==2){
            holder.tvDirection.setVisibility(View.VISIBLE);
            holder.tvDirection.setText("(方向:" + soaProductVOsBean.getSoaEndUserDirectionVO().getProductDirectionName() + ")");
        }else {
            holder.tvDirection.setVisibility(View.GONE);
        }

        //展示学校
        holder.tvSchool.setText(soaProductVOsBean.getCollegeName());
               /* '层次 1:高起专   2:专升本  3:高起本  4:专科  5:本科'
                * 1、网教&成教&自考：都分为“高起专、专升本、高起本”3个层次
                * 2、统招：分为“专科”、“本科”两个层次
                * 3、培训&考证：不分层次
                * 层次 1:高起专   2:专升本  3:高起本  4:专科  5:本科',
                */
        switch (soaProductVOsBean.getProductType()) {
            case 0:
                holder.tvEduType.setText("教育类型:网教");
                if (soaProductVOsBean.getProductGradation() == 1) {
                    holder.tvLevel.setText("层次:高起专");
                } else if (soaProductVOsBean.getProductGradation() == 2) {
                    holder.tvLevel.setText("层次:专升本");
                } else if (soaProductVOsBean.getProductGradation() == 3) {
                    holder.tvLevel.setText("层次:高起本");
                }
                break;
            case 1:
                holder.tvEduType.setText("教育类型:成教");
                if (soaProductVOsBean.getProductGradation() == 1) {
                    holder.tvLevel.setText("层次:高起专");
                } else if (soaProductVOsBean.getProductGradation() == 2) {
                    holder.tvLevel.setText("层次:专升本");
                } else if (soaProductVOsBean.getProductGradation() == 3) {
                    holder.tvLevel.setText("层次:高起本");
                }
                break;
            case 2:
                holder.tvEduType.setText("教育类型:自考");
                if (soaProductVOsBean.getProductGradation() == 1) {
                    holder.tvLevel.setText("层次:高起专");
                } else if (soaProductVOsBean.getProductGradation() == 2) {
                    holder.tvLevel.setText("层次:专升本");
                } else if (soaProductVOsBean.getProductGradation() == 3) {
                    holder.tvLevel.setText("层次:高起本");
                }
                break;
            case 3:
                holder.tvEduType.setText("教育类型:培训");
                if (soaProductVOsBean.getProductGradation() == 1) {
                    holder.tvLevel.setText("层次:高起专");
                } else if (soaProductVOsBean.getProductGradation() == 2) {
                    holder.tvLevel.setText("层次:专升本");
                } else if (soaProductVOsBean.getProductGradation() == 3) {
                    holder.tvLevel.setText("层次:高起本");
                }
                break;
            case 4:
                holder.tvEduType.setText("教育类型:考证");
                break;
            case 5:
                holder.tvEduType.setText("教育类型:统招");
                if (soaProductVOsBean.getProductGradation() == 4) {
                    holder.tvLevel.setText("层次:专科");
                } else if (soaProductVOsBean.getProductGradation() == 5) {
                    holder.tvLevel.setText("层次:本科");
                }
                break;
        }
        //显示学习形式
        //学习形式  1: 网络教育 2:业余 3:函授 4：全日制 5:专科段,6:本科段,7:独立本科段
        switch (soaProductVOsBean.getLearningModality()) {
            case 1:
                holder.tvForm.setText("学习形式:网络教育");
                break;
            case 2:
                holder.tvForm.setText("学习形式:业余");
                break;
            case 3:
                holder.tvForm.setText("学习形式:函授");
                break;
            case 4:
                holder.tvForm.setText("学习形式:全日制");
                break;
            case 5:
                holder.tvForm.setText("学习形式:专科段");
                break;
            case 6:
                holder.tvForm.setText("学习形式:本科段");
                break;
            case 7:
                holder.tvForm.setText("学习形式:独立本科段");
                break;
        }
        //显示招生季
        holder.reportZhaosheng.setText("招生季:" + soaProductVOsBean.getEnrollPlanSeason());
        //显示学制
        holder.reportXuezhi.setText("学制:" + soaProductVOsBean.getProductLearningLength() + "学期");

        //// 方向状态  0：没有专业方向；1：有方向且为未选；2：有方向且为已选
        if(soaProductVOsBean.getStatusDirection()==0){
            holder.reportContainerProgress.setVisibility(View.VISIBLE);
            //显示学习进度
            if( soaProductVOsBean.getMajorPercent()!=null|| !(soaProductVOsBean.getMajorPercent().equals("") )){
                int a=transFormation(soaProductVOsBean.getMajorPercent());
                holder.reportMajorPercent.setText(a+"%");
                holder.reportMicroProgressbar.setProgress(a);
            }else{
                holder.reportMajorPercent.setText("0%");
                holder.reportMicroProgressbar.setProgress(0);
            }
        }else if(soaProductVOsBean.getStatusDirection()==1){
            holder.reportContainerProgress.setVisibility(View.GONE);
            holder.tvChooseOr.setVisibility(View.VISIBLE);
        }else if(soaProductVOsBean.getStatusDirection()==2){
            holder.reportContainerProgress.setVisibility(View.VISIBLE);
            holder.tvChooseOr.setVisibility(View.GONE);
            //显示学习进度
            if( soaProductVOsBean.getMajorPercent()!=null|| !(soaProductVOsBean.getMajorPercent().equals("") )){
                int a=transFormation(soaProductVOsBean.getMajorPercent());
                holder.reportMajorPercent.setText(a+"%");
                holder.reportMicroProgressbar.setProgress(a);
            }else{
                holder.reportMajorPercent.setText("0%");
                holder.reportMicroProgressbar.setProgress(0);
            }
        }
        //添加点击时间
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               switch (soaProductVOsBean.getStatusDirection()){
                   case 0:
                       //没有方向
                       CourseListActivity.StartActivity(context,String.valueOf(soaProductVOsBean.getEnrollPlanId()),soaProductVOsBean.getProductLearningLength());
                       break;
                   case 1:
                       //有方向为选择
                       ChoiceDirectionActivity.StartActivity(context,String.valueOf(soaProductVOsBean.getEnrollPlanId()),String.valueOf(soaProductVOsBean.getId()));
                       break;
                   case 2:
                       //有方向已选择
                       CourseListActivity.StartActivity(context,String.valueOf(soaProductVOsBean.getEnrollPlanId()),soaProductVOsBean.getProductLearningLength());
                       break;
               }
            }
        });
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_direction)
        TextView tvDirection;
        @BindView(R.id.tv_school)
        TextView tvSchool;
        @BindView(R.id.tv_edu_type)
        TextView tvEduType;
        @BindView(R.id.tv_level)
        TextView tvLevel;
        @BindView(R.id.tv_form)
        TextView tvForm;
        @BindView(R.id.report_zhaosheng)
        TextView reportZhaosheng;
        @BindView(R.id.report_xuezhi)
        TextView reportXuezhi;
        @BindView(R.id.report_micro_progressbar)
        ProgressBar reportMicroProgressbar;
        @BindView(R.id.report_majorPercent)
        TextView reportMajorPercent;
        @BindView(R.id.report_containerProgress)
        LinearLayout reportContainerProgress;
        @BindView(R.id.tv_choose_or)
        TextView tvChooseOr;
        @BindView(R.id.item)
        LinearLayout item;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 把字符串型百分数改变成into型整数
     * @param str  字符串型百分数
     * @return    整数
     */
    private int  transFormation(String str) {
        int a=0;
        Pattern p=Pattern.compile("(\\d+)");
        Matcher ma=p.matcher(str);
        if(ma.find()){
            a=Integer.valueOf(ma.group(1));
        }
        return  a;
    }

}
