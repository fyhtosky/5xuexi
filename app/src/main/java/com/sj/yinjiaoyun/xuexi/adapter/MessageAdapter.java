package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.NoticesRows;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/18.
 */
public class MessageAdapter extends BaseAdapter {

    List<NoticesRows> rowsList;//消息列表
    Context context;
    LayoutInflater inflater;
    NoticesRows rows;

    public MessageAdapter(List<NoticesRows> rowsList, Context context) {
        this.rowsList = rowsList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void refresh(List<NoticesRows> rowsList){
        this.rowsList=rowsList;
        notifyDataSetChanged();
    }

    Boolean isNull;
    @Override
    public int getCount() {
        isNull = rowsList == null;
        return rowsList==null?1:rowsList.size();
    }

    @Override
    public Object getItem(int position) {
        return rowsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder;
        rows=rowsList.get(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_message,parent,false);
            holder.continer=convertView.findViewById(R.id.item_containerMessage);
            holder.text= (TextView) convertView.findViewById(R.id.item_message_text);
            holder.time= (TextView) convertView.findViewById(R.id.item_message_time);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        holder.text.setText(rows.getNoticeContent());
        holder.time.setText(getTime(rows.getCreateTime()));
        if(rows.getIsRead()==1){//是否已读 0:未读 1：已读
            holder.continer.setAlpha(0.7f);
           //  holder.text.setTextColor(context.getResources().getColor(R.color.colorMoreGray));
        }else{
            holder.continer.setAlpha(1f);
           // holder.text.setTextColor(context.getResources().getColor(R.color.colorHomeitem));
        }
        return convertView;
    }

    class ViewHolder{
        View continer;
        TextView text;
        TextView time;
    }

    /**
     * 把当前毫秒数时间转化为制定指定的时间
     * 15:33:34
     * @param timeInMillis time
     * @return String
     */
    public String getTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date(timeInMillis));
    }

}
