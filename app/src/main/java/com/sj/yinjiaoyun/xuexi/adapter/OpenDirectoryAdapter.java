package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.JobDialogActivity;
import com.sj.yinjiaoyun.xuexi.domain.Coursewares;
import com.sj.yinjiaoyun.xuexi.domain.ExpendGroup;
import com.sj.yinjiaoyun.xuexi.view.CourseItemView;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/19.
 * 公开课详情 目录
 */
public class OpenDirectoryAdapter extends BaseExpandableListAdapter {
    String TAG="dialog";
    List<ExpendGroup> data;//一级菜单，组的集合数据
    Context context;
    LayoutInflater inflater;
    int flag;//0表示 预览页面  2表示学习页面
    String courseScheduleId;//设置课程表id

    public OpenDirectoryAdapter(Context context,String courseScheduleId, List<ExpendGroup> data,int flag){
        this.context=context;
        this.courseScheduleId=courseScheduleId;
        this.data=data;
        this.flag=flag;
        inflater=LayoutInflater.from(context);
    }


    //返回父的个数
    @Override
    public int getGroupCount() {
        return data==null?0:data.size();
    }

    //返回子的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        ExpendGroup expendGroup=data.get(groupPosition);
        if(expendGroup==null){//确保父不为空，避免出现空指针
            return 0;
        }
        List<Coursewares> child=expendGroup.getExpendChild();//获得子的对象
        return child==null?0:child.size();
    }
    //获得组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView;
        ExpendGroup expendGroup=data.get(groupPosition);
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_expend_expendgroup,parent,false);
            textView= (TextView) convertView.findViewById(R.id.item_expendgroup);
            convertView.setTag(textView);
        }
        textView= (TextView) convertView.getTag();
        textView.setText(expendGroup.getGroupName());
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

    CourseItemView courseItemView;
    //获得子的视图
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final List<Coursewares> childList=data.get(groupPosition).getExpendChild();
        final Coursewares expendChild=childList.get(childPosition);
        Log.i(TAG, "getChildView: "+expendChild.toString());
        courseItemView=new CourseItemView(context);
            try {
            Log.i(TAG, "getChildView: "+expendChild.getCoursewareName());
            courseItemView.setValueToName(expendChild.getCoursewareName() + " ");
            courseItemView.setValueToJob(expendChild.getHomeworkState()==null ? 0:expendChild.getHomeworkState(),expendChild.getExamState()==null ? 0:expendChild.getExamState());
            courseItemView.getIvPull().setVisibility(View.VISIBLE);
                //跳转答疑测试
                courseItemView.getIvPull().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, JobDialogActivity.class);
                        Log.i(TAG, "onClick: 传数据之前" + expendChild.toString());
                        intent.putExtra("Coursewares", expendChild);
                        intent.putExtra("TestExamPaperReleaseId", String.valueOf(expendChild.getTestExamPaperReleaseId()));//考试id
                        intent.putExtra("ExamPaperReleaseId", String.valueOf(expendChild.getHomeworkExamPaperReleaseId()));//作业id
                        intent.putExtra("groupPosition", groupPosition + 1);//章节
                        intent.putExtra("childPosition", childPosition + 1);//课时
                        intent.putExtra("CourseScheduleId", courseScheduleId);//课表id
                        Log.i(TAG, "onClick:传数据之前" + courseScheduleId);
                        context.startActivity(intent);
                    }
                });
            if (flag == 0) {//预览页面
                courseItemView.setValueToIcon(0, (childPosition + 1) + "");
            } else {
                courseItemView.setValueToIcon(expendChild.getVideoState(), (childPosition + 1) + "");
            }
        }catch (Exception e){
            Log.e(TAG, "getChildView: "+e.toString());
        }
        return courseItemView;
    }

}
