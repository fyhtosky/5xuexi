package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;

import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/2.
 *  答题卡adapter
 */
public class DaTiKaAdapter extends BaseAdapter {

    List<TiMu> tmList;
    Context context;
    Map<String,Object> answerMap;
    LayoutInflater inflater;
    String TAG="bbbb";

    public DaTiKaAdapter(List<TiMu> tm, Map<String,Object> answerMap, Context context) {
        this.tmList = tm;
        this.context = context;
        this.answerMap=answerMap;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tmList==null?0:tmList.size();
    }

    @Override
    public Object getItem(int position) {
        return tmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv=new TextView(context);
        TiMu tm=tmList.get(position);
        String id=tm.getId();
        //设置字体背景
        tv.setBackgroundColor(context.getResources().getColor(R.color.colorDrayGray));//初始背景
        Log.i(TAG, "--------getView:已做题号 "+tm.getQid()+"答案集合:"+answerMap.size());
        for (Map.Entry<String, Object> entry :answerMap.entrySet()) {
            if (id.equals(entry.getKey())) {
                tv.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                if(tm.getQuestionType()==4){
                    if(entry.getValue()==null){
                        tv.setBackgroundColor(context.getResources().getColor(R.color.colorDrayGray));
                    }else{
                        String a=entry.getValue().toString();
                        if(a.equals("") ||a.length()==0){
                            tv.setBackgroundColor(context.getResources().getColor(R.color.colorDrayGray));//初始背景
                        }
                    }
                }else{
                    if(entry.getValue()==null){
                        tv.setBackgroundColor(context.getResources().getColor(R.color.colorDrayGray));
                    }else {
                        tv.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                    }
                }
            }
        }
        tv.setPadding(10,10,10,10);
        tv.setGravity(Gravity.CENTER);
        tv.setText(tm.getQid()+"");
        return tv;
    }

}
