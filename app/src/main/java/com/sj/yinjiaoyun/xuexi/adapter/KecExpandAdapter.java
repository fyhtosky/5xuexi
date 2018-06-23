package com.sj.yinjiaoyun.xuexi.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.Kec;
import com.sj.yinjiaoyun.xuexi.domain.KecParent;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.view.MultipleChoiceView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/11.
 * 课程表  课程选择（非培训考证类型的课程选择）
 *
 */
public class KecExpandAdapter extends BaseExpandableListAdapter {

    Context context;
    List<KecParent> date;
    LayoutInflater inflater;
    List<Kec> list;//子

    private Map<Long,Boolean> expandMap;

    public KecExpandAdapter(Context context, List<KecParent> date) {
        this.context = context;
        this.date = date;
        try{
            inflater=LayoutInflater.from(context);
        }catch (Exception e){
            Log.i("Error", "KecExpandAdapter: "+e.toString());
        }
        expandMap=new HashMap<>();
    }

    //数据刷新
    public void refresh(List<KecParent> date){
        this.date = date;
        notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return date==null?0:date.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        list=date.get(groupPosition).getChildList();
        return list==null?0:list.size();
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView;
        KecParent p=date.get(groupPosition);
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_tabulation_group,parent,false);
            textView= (TextView) convertView.findViewById(R.id.expend_parent_parentName);
            convertView.setTag(textView);
        }
        textView= (TextView) convertView.getTag();
        textView.setText(p.getXueQi());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        list=date.get(groupPosition).getChildList();
        final Kec kec=list.get(childPosition);
        String key=String.valueOf(kec.getCourseAttribute())+"";
        MultipleChoiceView multipleChoiceView=new MultipleChoiceView(context);
        //0:公共基础课,1:专业选修课,2:专业基础课,3:公共选修课,
        if(kec.getCourseAttribute()==0 || kec.getCourseAttribute()==2){//公共基础课
            multipleChoiceView.setChecked(true);
            expandMap.put(kec.getId(),true);
            multipleChoiceView.setUnclick(0);//设置其不能点击
            multipleChoiceView.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    expandMap.put(kec.getId(),true);
                }
            });
            multipleChoiceView.getCheckBox().setEnabled(false);
        }else{
            multipleChoiceView.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    expandMap.put(kec.getId(),isChecked);
                }
            });
        }
        multipleChoiceView.setNameAndLeiXing(kec.getCourseName(),(String) MyConfig.couseleiXing().get(key));
        return multipleChoiceView;
    }


    @Override
    public Object getGroup(int groupPosition) {
        return date.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return date.get(groupPosition).getChildList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        //默认这个方法是返回false的, 需要返回true.
        // 这样, 才能使用ListView的CHOICE_MODE_MULTIPLE,
        // CHOICE_MODE_SINGLE, 通过getCheckedItemIds方法才能正常获取用户选中的选项的id, LZ可以试试看
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

     class ViewHolder{
        public CheckBox cb;
        TextView name;
        TextView leiXing;
    }

    public Map<Long, Boolean> getExpandSelectMap() {
        return expandMap;
    }


}
