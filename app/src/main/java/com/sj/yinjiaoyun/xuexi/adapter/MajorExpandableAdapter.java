package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.CourseVO;
import com.sj.yinjiaoyun.xuexi.domain.Parent;
import com.sj.yinjiaoyun.xuexi.view.ProgressBarView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/28.
 * 课程表  的二级菜单(此时已选完方向，课程 )
 */
public class MajorExpandableAdapter extends BaseExpandableListAdapter {
    String TAG="fragment";
    List<Parent> data;//一级菜单，组的集合数据
    Context context;
    LayoutInflater inflater;
    public MajorExpandableAdapter(Context context, List<Parent> data){
        this.context=context;
        this.data=data;
        inflater=LayoutInflater.from(context);
    }

    //刷新数据
    public void refresh(List<Parent> list){
        this.data=list;
        notifyDataSetChanged();
    }

    //返回父的个数
    @Override
    public int getGroupCount() {
        return data==null?0:data.size();
    }

    //返回子的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        Parent parent=data.get(groupPosition);
        if(parent==null){//确保父不为空，避免出现空指针
            return 0;
        }
        List<CourseVO> child=parent.getChild();//获得子的对象
        return child==null ? 0:child.size();
    }
    //获得组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView;
        Parent p=data.get(groupPosition);
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_tabulation_group,parent,false);
            textView= (TextView) convertView.findViewById(R.id.expend_parent_parentName);
            convertView.setTag(textView);
        }
        textView= (TextView) convertView.getTag();
        textView.setText(p.getParentName());
        return convertView;
    }
    //获得子的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        List<CourseVO> childList=data.get(groupPosition).getChild();
        CourseVO courseVO =childList.get(childPosition);
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
        try {
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
        }catch (Exception e){
            Log.i(TAG, "getView: "+e.toString());
        }
        holder.tvCourseName.setText(courseVO.getCourseName());//课程名字
        holder.progressBarView.setProgressAndMark(courseVO.getCoursePercent());
        holder.tvTotalScore.setText("总成绩："+ courseVO.getTotalScore());
        holder.tvCourseCredit.setText("学分："+ courseVO.getCourseCredit());
        Log.i(TAG, "getChildView: "+ courseVO.getCourseLogoUrl()+":"+ courseVO.getCourseName()+"进度："+ courseVO.getCoursePercent()
        +"总成绩："+ courseVO.getTotalScore()+"学分："+ courseVO.getCourseCredit());
       // if(data.get(groupPosition).getState()){
            holder.ivCouseState.setVisibility(View.GONE);
       //}
        return convertView;
    }

    //获得组的对象
    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }
    //获得子的对象
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }
    //获得组的id
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }
    //获得子的id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //设置子项点击事件
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder{
        ImageView ivCouseState;
        ImageView ivCourseImage;
        TextView tvCourseName;
        ProgressBarView progressBarView;
        TextView tvTotalScore;
        TextView tvCourseCredit;
    }
}
