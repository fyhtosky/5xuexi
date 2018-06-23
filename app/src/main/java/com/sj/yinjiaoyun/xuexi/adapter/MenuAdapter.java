package com.sj.yinjiaoyun.xuexi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.SoaProductVOs;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/26.
 * popwindow
 */
public class MenuAdapter extends BaseAdapter {

    private Activity activity;//上下文
    private List<SoaProductVOs> list;

    private LayoutInflater inflater=null;//导入布局
    private int temp=-1;

    public MenuAdapter(Activity context, List<SoaProductVOs> list) {
        this.activity = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
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
        // TODO Auto-generated method stub
        return position;
    }

    public void fresh(List<SoaProductVOs> list){
        this.list=list;
        notifyDataSetChanged();
    }


    //listview每显示一行数据,该函数就执行一次
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {//当第一次加载ListView控件时  convertView为空
            convertView=inflater.inflate(R.layout.item_menu, parent,false);//所以当ListView控件没有滑动时都会执行这条语句
            holder=new ViewHolder();
           // holder.tv=(TextView)convertView.findViewById(R.id.item_tv);
            holder.cb=(CheckBox)convertView.findViewById(R.id.item_cb);
            convertView.setTag(holder);//为view设置标签
        }
        else{//取出holder
            holder=(ViewHolder) convertView.getTag();
        }
        //设置list的textview显示
        SoaProductVOs vo=  list.get(position);
        holder.cb.setText(vo.getProductName()+"");
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
        public TextView tv;
        public CheckBox cb;
    }



}
