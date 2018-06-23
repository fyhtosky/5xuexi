package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
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
 * Created by ${沈军 961784535@qq.com} on 2016/12/6.
 * 首页
 */
public class HomeAdapter extends BaseAdapter {

    String TAG = "homefragment";
    Context context;
    List<Fuse> list;
    LayoutInflater inflater;

    public HomeAdapter(List<Fuse> list, Context context) {
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
        return 0;
    }


    ViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fuse fuse = list.get(position);
        int type = fuse.getIndexType();
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_home, parent,false);
            holder.tvHint = (TextView) convertView.findViewById(R.id.item_home_desc);
            holder.container = convertView.findViewById(R.id.item_home_container);
            holder.tvCollege = (TextView) convertView.findViewById(R.id.item_home_college);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.item_home_price);
            holder.mIcon = (ImageView) convertView.findViewById(R.id.item_home_image);
            holder.tvCouse = (TextView) convertView.findViewById(R.id.item_home_courseName);
            holder.tvAttente = (TextView) convertView.findViewById(R.id.item_home_attention);
            holder.tvTutionWay = (TextView) convertView.findViewById(R.id.item_home_tutionWay);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            if  (type == 3) {
                holder.container.setVisibility(View.GONE);
                holder.tvHint.setVisibility(View.VISIBLE);
            } else {
                holder.container.setVisibility(View.VISIBLE);
                holder.tvHint.setVisibility(View.GONE);
                if (fuse.getCourseLogo().equals("")) {
                    Picasso.with(context)
                            .load(R.mipmap.error)
                            .placeholder(R.drawable.progressbar_landing)
                            //加载失败中的图片显示
                            .error(R.mipmap.error).into(holder.mIcon);
                } else {
                    Picasso.with(context)
                            .load(fuse.getCourseLogo())
                            //加载过程中的图片显示
                            .placeholder(R.drawable.progressbar_landing)
                            //加载失败中的图片显示
                            .error(R.mipmap.error)
                            //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                            .centerCrop()
                            .resize(500, 315)
                            .into(holder.mIcon);
                }
                holder.tvAttente.setText(fuse.getNumber() + "");
                holder.tvCollege.setText(fuse.getCollegeName());
                holder.tvCouse.setText(fuse.getCourseName());
                if (type == 2) {//微专业
                    holder.tvTutionWay.setVisibility(View.VISIBLE);
                    Byte tutionWay=fuse.getTutionWay();
                    holder.tvTutionWay.setText(MyConfig.tutionWay().get(tutionWay+ "").toString());
                    if (fuse.getIsFree() == 1) {// 【0：收费 1：不收费】
                        holder.tvPrice.setText("免费");
                        holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorGreen));
                    } else {//收费
                        holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorRed));
                        Double start =fuse.getMinPrice();
                        Double end=fuse.getMaxPrice();
                        if(tutionWay==0){// 【开课方式（0：随到随学 1:定期开课）】
                            holder.tvPrice.setText("¥"+MyUtil.saveTwoScale(start));
                        }else{//1:定期开课
                            if(start<end) {
                                holder.tvPrice.setText("¥"+MyUtil.saveTwoScale(start) + "-" + MyUtil.saveTwoScale(end));
                            }else {
                                holder.tvPrice.setText("¥"+start+"");
                            }
                        }

                    }
                } else if (type == 1) {//公开课
                    Log.i(TAG, "getView: "+fuse.toString());
                    holder.tvTutionWay.setVisibility(View.GONE);
                    if(fuse.getIsFree()==0){// 【0：收费 1：不收费】
                        holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorRed));
                        Double a = fuse.getPrice();
                        holder.tvPrice.setText(MyUtil.saveTwoScale(a));
                    }else{//免费
                        holder.tvPrice.setText("免费");
                        holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorGreen));
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "getView: "+e.toString());
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvHint;//推荐公开课
        View container;
        ImageView mIcon;
        TextView tvCollege;
        TextView tvCouse;
        TextView tvPrice;
        TextView tvAttente;
        TextView tvTutionWay;
    }



}
