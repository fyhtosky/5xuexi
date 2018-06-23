package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domains.TrainingCourse;
import com.sj.yinjiaoyun.xuexi.view.CellView;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/9.
 * 微专业预览页面  课程体系 中的adapter
 */
public class CurriculumAdapter extends BaseAdapter {

    private  final String TAG ="microhintcurriculum" ;
    List<TrainingCourse> list;
    Context context;
    LayoutInflater inflater;

    public CurriculumAdapter(List<TrainingCourse> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void onRefreshs(List<TrainingCourse> list){
        this.list=list;
        notifyDataSetChanged();
        for(int i=0;i<list.size();i++){
            Log.i(TAG, "onRefreshs: "+list.get(i).toString());
        }
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
        TrainingCourse trainingCourse=list.get(position);
        Log.i(TAG, "getView: "+trainingCourse.toString());
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_curriculum, parent,false);
            holder.cvSerial= (CellView) convertView.findViewById(R.id.item_curriculum_serial);
            holder.cvCourseName= (CellView) convertView.findViewById(R.id.item_curriculum_courseName);
            holder.cvTeacher= (CellView) convertView.findViewById(R.id.item_curriculum_teacher);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.cvCourseName.setTvText(trainingCourse.getCourseName());
        holder.cvTeacher.setTvText(trainingCourse.getTeacherName());
        holder.cvSerial.setTvText("课程 "+(trainingCourse.getTrainingCourseOrder()+1));
        return convertView;
    }

    class ViewHolder {
        CellView cvSerial;
        CellView cvCourseName;
        CellView cvTeacher;
    }

}
