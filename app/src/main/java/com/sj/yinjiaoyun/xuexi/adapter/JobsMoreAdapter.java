package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.text.Html;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.sj.yinjiaoyun.xuexi.utils.LinkMovementMethodExt;
import com.sj.yinjiaoyun.xuexi.utils.NetworkImageGetter;
import com.sj.yinjiaoyun.xuexi.view.MultipleChoiceView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/1.
 * 客观题   多选项
 */
public class JobsMoreAdapter extends BaseAdapter{

    String TAG="jobMore";
    List<String> list;
    Context context;
    private Map<Integer,Boolean> selectMap;
    //字母
    public static String[] myLetter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };
    /**网络图片Getter*/
    private NetworkImageGetter mImageGetter;

    public JobsMoreAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        selectMap=new HashMap<>();
        selectMap.clear();
        for (int i = 0; i < list.size(); i++) {
            selectMap.put(i, false);
        }
    }

    public Map<Integer, Boolean> getSelectMap() {
        return selectMap;
    }

    public void onFresh(List<String> list){
        this.list=list;
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

    StringBuilder sb;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        sb=new StringBuilder();
        sb.append(myLetter[position]).append("、").append(list.get(position));
        MultipleChoiceView multipleChoiceView=new MultipleChoiceView(context);
        //加载html文本中的图片标签并显示
        mImageGetter = new NetworkImageGetter(context,multipleChoiceView.getTvName());
        multipleChoiceView.getTvName().setText(Html.fromHtml(sb.toString().replaceAll("\\\\",""), mImageGetter, null));
        multipleChoiceView.getTvName().setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
        if (flag == 1) {//设置器不能点击
//            multipleChoiceView.setNameAndLeiXing(sb.toString(),"");
            multipleChoiceView.getCheckBox().setChecked(selectMap.get(position));
            multipleChoiceView.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectMap.put(position,true);
                }
            });
            multipleChoiceView.setUnclick(0);//设置其不能点击
            multipleChoiceView.getCheckBox().setEnabled(false);
        }else{
//            multipleChoiceView.setNameAndLeiXing(sb.toString(),"");
            multipleChoiceView.getCheckBox().setChecked(selectMap.get(position));
            multipleChoiceView.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectMap.put(position,isChecked);
                }
            });
        }
        return multipleChoiceView;
    }

    int flag=0;
    public void setPositionCheck(int position,boolean a){
        for (Map.Entry<Integer, Boolean> entry : selectMap.entrySet()) {
            Log.i(TAG, "setPositionCheck: "+"Key = " + entry.getKey().toString() + ", Value = " + entry.getValue());
        }
        selectMap.put(position,a);
        notifyDataSetChanged();
    }
    public void setFlag(int flag){
        this.flag=flag;
        notifyDataSetChanged();
    }

}
