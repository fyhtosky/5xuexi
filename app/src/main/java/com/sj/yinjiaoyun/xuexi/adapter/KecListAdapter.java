package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.Kec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/16.
 * 课程表  课程选择（培训考证类型的课程选择）
 */
public class KecListAdapter extends BaseAdapter {

    Context context;
    List<Kec> list;
    LayoutInflater inflater;
    private Map<Integer,Boolean> selectMap;
    public KecListAdapter(Context context, List<Kec> list) {
        this.list=list;
        this.context=context;
        inflater= LayoutInflater.from(context);
        selectMap=new HashMap<>();
    }



    //数据刷新
    public void refresh(List<Kec> date){
        this.list = date;
        for(int i=0;i<date.size();i++){
            selectMap.put(i,true);
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Kec kec= list.get(position);
//        String key=String.valueOf(kec.getCourseAttribute())+"";
        /*MultipleChoiceView singleViewTest=new MultipleChoiceView(context);
        singleViewTest.setNameAndLeiXing(kec.getCourseName(),"");
        singleViewTest.getCheckBox().setChecked(true);*/
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.view_kec_multiple,parent,false);
            holder.cb= (CheckBox) convertView.findViewById(R.id.item_kec_checkbox);
            holder.name= (TextView) convertView.findViewById(R.id.item_kec_name);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        holder.cb.setChecked(true);
        holder.cb.setEnabled(false);
        holder.name.setText(kec.getCourseName());
        return convertView;
    }

    public Map<Integer, Boolean> getSelectMap(){
        return selectMap;
    }

    class ViewHolder{
        public CheckBox cb;
        TextView name;
        TextView leiXing;
    }
}
