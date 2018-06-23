package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domains.Fuse;
import com.sj.yinjiaoyun.xuexi.utils.MyUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/7.
 * 搜索页面adapter 和发现页面公用
 */
public class FindAdapter extends BaseAdapter {

    String TAG = "searchadapter";
    Context context;
    List<Fuse> list;
    LayoutInflater inflater;

    public FindAdapter(List<Fuse> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void onfresh(List<Fuse> list) {
        this.list = list;
        notifyDataSetChanged();
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
        String logo = null;
        String name = null;
        Fuse fuse = list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_find, parent,false);
            holder.tvCouse = (TextView) convertView.findViewById(R.id.item_find_courseName);
            holder.tvCollege = (TextView) convertView.findViewById(R.id.item_find_college);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.item_find_price);
            holder.mIcon = (ImageView) convertView.findViewById(R.id.item_find_image);
            holder.mLabel = (ImageView) convertView.findViewById(R.id.item_find_label);
            holder.tvAttente = (TextView) convertView.findViewById(R.id.item_find_attention);
            holder.tvTutionWay = (TextView) convertView.findViewById(R.id.item_find_tutionWay);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        try {
            int indexType = fuse.getIndexType();
            Byte isFree = fuse.getIsFree();
            if (indexType == 1) {//公开课
                logo = fuse.getCourseLogo();
                name = fuse.getCourseName();
                holder.tvTutionWay.setVisibility(View.GONE);
                holder.mLabel.setImageResource(R.mipmap.tip_publiclass);
                if (isFree == 0) { //付费
                    holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorRed));
                    holder.tvPrice.setText("¥"+MyUtil.saveTwoScale(fuse.getPrice()));
                } else if (isFree == 1) {//免费
                    holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorGreen));
                    holder.tvPrice.setText("免费");
                }
            } else if (indexType == 2) {//微专业
                logo = fuse.getProductLogoUrl();
                name = fuse.getProductName();
                Byte tutionWay=fuse.getTutionWay();
                holder.tvTutionWay.setVisibility(View.VISIBLE);
                holder.mLabel.setImageResource(R.mipmap.tip_mirclass);
                holder.tvTutionWay.setText(MyConfig.tutionWay().get(tutionWay + "").toString());
                if (isFree == 1) { //免费
                    holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorGreen));
                    holder.tvPrice.setText("免费");
                } else if (isFree == 0) {//付费
                    holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorRed));
                    Double start =fuse.getMinPrice();
                    Double end=fuse.getMaxPrice();
                    if(tutionWay==0){//随到随学
                        holder.tvPrice.setText("¥"+MyUtil.saveTwoScale(start));
                    }else{//定期开课
                        if(start<end) {
                            holder.tvPrice.setText("¥"+MyUtil.saveTwoScale(start) + "-" + MyUtil.saveTwoScale(end));
                        }else {
                            holder.tvPrice.setText("¥"+start);
                        }
                    }
                }
            }
            if (TextUtils.isEmpty(logo)) {
                Picasso.with(context)
                        .load(R.mipmap.error)
                        .placeholder(R.drawable.progressbar_landing)
                        //加载失败中的图片显示
                        .error(R.mipmap.error).into(holder.mIcon);
            } else {
                Picasso.with(context)
                        .load(logo)
                        //加载过程中的图片显示
                        .placeholder(R.drawable.progressbar_landing)
                        //加载失败中的图片显示
                        .error(R.mipmap.error)
                        //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                        .centerCrop()
                        .resize(500, 330)
                        .into(holder.mIcon);
            }
            holder.tvAttente.setText(fuse.getNumber() + "");
            holder.tvCouse.setText(name);
            holder.tvCollege.setText(fuse.getCollegeName());
        } catch (Exception e) {
            Log.i(TAG, "getView: " + e.toString());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView mIcon;
        ImageView mLabel;
        TextView tvCouse;
        TextView tvCollege;
        TextView tvPrice;
        TextView tvAttente;
        TextView tvTutionWay;
    }



}
