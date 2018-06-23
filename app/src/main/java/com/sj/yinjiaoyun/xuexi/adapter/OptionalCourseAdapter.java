package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.OptionalCourseBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/21.
 * 选选课的适配器
 */
public class OptionalCourseAdapter extends BaseAdapter {
    private Context context;
    private List<OptionalCourseBean.DataBean.NeedSelectCoursesBean> list;
    //存储点击状态
    private SparseBooleanArray array = new SparseBooleanArray();
    public OptionalCourseAdapter(Context context, List<OptionalCourseBean.DataBean.NeedSelectCoursesBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.optional_course_item, parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
           holder= (ViewHolder) convertView.getTag();
        }
       OptionalCourseBean.DataBean.NeedSelectCoursesBean needSelectCoursesBean=list.get(position);
        holder.tvCourseName.setText(needSelectCoursesBean.getCourseName());
        //添加点击事件
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array.put(position, !array.get(position));
                if (array.get(position)) {
                    holder.ivSelect.setImageResource(R.mipmap.select_active);
                } else {
                    holder.ivSelect.setImageResource(R.mipmap.select);
                }
                notifyDataSetChanged();
            }
        });
        switch (needSelectCoursesBean.getCourseAttribute()){
            case 0:
                holder.tvType.setText("公共基础课");
                break;
            case 1:
                holder.tvType.setText("专业选修课");
                break;
            case 2:
                holder.tvType.setText("专业基础课");
                break;
            case 3:
                holder.tvType.setText("公共选修课");
                break;
        }
        return convertView;
    }
    /**
     * 获取选中的
     */
    public List< OptionalCourseBean.DataBean.NeedSelectCoursesBean> getCheckedData() {
        //准备集合存放选中的商品集合
        List< OptionalCourseBean.DataBean.NeedSelectCoursesBean> checkList = new ArrayList<>();
        checkList.clear();
        //遍历集合，将选中的商品添加集合中
        for (int i = 0; i < list.size(); i++) {
            array.put(i, array.get(i));
            if (array.get(i)) {
                checkList.add(list.get(i));
            }
        }
        return checkList;
    }
     class ViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.tv_course_name)
        TextView tvCourseName;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.item)
        RelativeLayout item;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
