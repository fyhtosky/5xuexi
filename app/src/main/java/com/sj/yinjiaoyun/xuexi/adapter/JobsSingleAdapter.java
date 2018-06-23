package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.text.Html;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.utils.LinkMovementMethodExt;
import com.sj.yinjiaoyun.xuexi.utils.NetworkImageGetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/1.
 * 客观题   单选选项
 */
public class JobsSingleAdapter extends BaseAdapter {

    String TAG = "SingleJob";
    List<String> list;
    Context context;
    LayoutInflater inflater;
    private  int jobState;
    /**
     * 网络图片Getter
     */
    private NetworkImageGetter mImageGetter;
    //字母
    public static String[] myLetter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    private Map<Integer, Boolean> selectMap;

//    public JobsSingleAdapter(List<String> list, Context context) {
//        this.list = list;
//        this.context = context;
//        mImageGetter = new NetworkImageGetter(context);
//        inflater = LayoutInflater.from(context);
//        selectMap = new HashMap<Integer, Boolean>();
//        selectMap.clear();
//        for (int i = 0; i < list.size(); i++) {
//            selectMap.put(i, false);
//        }
//    }

    public JobsSingleAdapter(List<String> list, Context context, int jobState) {
        this.list = list;
        this.context = context;
        this.jobState = jobState;
        mImageGetter = new NetworkImageGetter(context);
        inflater = LayoutInflater.from(context);
        selectMap = new HashMap<>();
        selectMap.clear();
        for (int i = 0; i < list.size(); i++) {
            selectMap.put(i, false);
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

    public Map<Integer, Boolean> getSelectMap() {
        return selectMap;
    }

    public void setPositionCheck(int position, boolean a) {
        for (Map.Entry<Integer, Boolean> entry : selectMap.entrySet()) {
            Log.i(TAG, "setPositionCheck: " + "Key = " + entry.getKey().toString() + ", Value = " + entry.getValue());
        }
        for (int i = 0; i < list.size(); i++) {
            selectMap.put(i, false);
        }
        selectMap.put(position, a);
        notifyDataSetChanged();
    }


    StringBuilder sb;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        sb = new StringBuilder();
        sb.append(myLetter[position]).append("、").append(list.get(position));
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.job_single_choice_item, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        mImageGetter.setTvText(holder.textview);
        holder.textview.setText(Html.fromHtml(sb.toString().replaceAll("\\\\", ""), mImageGetter, null));
        holder.textview.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
        if(selectMap.get(position)){
            holder.ivSelect.setImageResource(R.mipmap.select_active);
        }else {
            holder.ivSelect.setImageResource(R.mipmap.select_disable);
        }
        Logger.d("试卷的状态："+jobState);
        if(jobState==2 || jobState==3){
           return convertView;
        }else {
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < list.size(); i++) {
                        selectMap.put(i, false);
                    }
                    selectMap.put(position, true);
                    holder.ivSelect.setImageResource(R.mipmap.select_active);
                    notifyDataSetChanged();
                }
            });
        }


        return convertView;
    }




    static class ViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.textview)
        TextView textview;
        @BindView(R.id.item)
        LinearLayout item;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
