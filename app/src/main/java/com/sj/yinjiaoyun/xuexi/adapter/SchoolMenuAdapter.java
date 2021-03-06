package com.sj.yinjiaoyun.xuexi.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.StudentNumbers;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/8.
 */
public class SchoolMenuAdapter extends BaseAdapter {

    String TAG="school";
    private int temp=-1;
    private Activity activity;//上下文
    private List<StudentNumbers> list;

    private LayoutInflater inflater=null;//导入布局

    public SchoolMenuAdapter(Activity context, List<StudentNumbers> list) {
        Log.i(TAG, "SchoolMenuAdapter: "+list.size());
        this.activity=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }



    public void refresh(List<StudentNumbers> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null ? 0:list.size();
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
        Log.i(TAG, "getView: ");
        ViewHolder holder;
        if (convertView==null) {//当第一次加载ListView控件时  convertView为空
            convertView=inflater.inflate(R.layout.item_menu, parent,false);//所以当ListView控件没有滑动时都会执行这条语句
            holder=new ViewHolder();
            holder.cb=(CheckBox)convertView.findViewById(R.id.item_cb);
            convertView.setTag(holder);//为view设置标签
        } else{//取出holder
            holder=(ViewHolder) convertView.getTag();//the Object stored in this view as a tag
        }
        //设置list的textview显示
        StudentNumbers studentNumbers=list.get(position);
        Log.i(TAG, "getView: "+studentNumbers.getCollegeName());
        holder.cb.setText(studentNumbers.getCollegeName()+"-"+studentNumbers.getProductName());
        // 根据isSelected来设置checkbox的选中状况

        holder.cb.setId(position);//对checkbox的id进行重新设置为当前的position
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //把上次被选中的checkbox设为false
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){//实现checkbox的单选功能,同样适用于radiobutton
                    if(temp!=-1){
                        //找到上次点击的checkbox,并把它设置为false,对重新选择时可以将以前的关掉
                        CheckBox tempCheckBox=(CheckBox)activity.findViewById(temp);
                        if(tempCheckBox!=null)
                            tempCheckBox.setChecked(false);
                    }
                    temp=buttonView.getId();//保存当前选中的checkbox的id值
                }
            }
        });
        if(position==temp)//比对position和当前的temp是否一致
            holder.cb.setChecked(true);
        else
            holder.cb.setChecked(false);
        return convertView;
    }

    public static class ViewHolder {
        public CheckBox cb;
    }
}
