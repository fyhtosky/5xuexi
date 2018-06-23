package com.sj.yinjiaoyun.xuexi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.CollectMessageDetailActivity;
import com.sj.yinjiaoyun.xuexi.bean.CollectMessageBean;
import com.sj.yinjiaoyun.xuexi.utils.ExpressionUtil;
import com.sj.yinjiaoyun.xuexi.utils.MyPopWindows;
import com.sj.yinjiaoyun.xuexi.utils.TimeRender;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/4/18.
 */
public class CollectMessageAdapter extends BaseAdapter {
    private List<CollectMessageBean.DataBean.MsgEnshrineBean.RowsBean> list;
    private Context Mcontext;
    private MyPopWindows popupWindow;

    public CollectMessageAdapter(List<CollectMessageBean.DataBean.MsgEnshrineBean.RowsBean> list, Context mcontext,Activity activity) {
        this.list = list;
        Mcontext = mcontext;
        popupWindow = new MyPopWindows(activity,mcontext);
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder hodler;
        if (convertView == null) {
            convertView = LayoutInflater.from(Mcontext).inflate(R.layout.collect_message_item, parent,false);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }
        final CollectMessageBean.DataBean.MsgEnshrineBean.RowsBean rowsBean = list.get(position);
        String rgx = "*$img_";
        //加载图片
        if (rowsBean.getSenderLogo() != null&& !TextUtils.isEmpty(rowsBean.getSenderLogo())) {
            Picasso.with(Mcontext).load(rowsBean.getSenderLogo()).into(hodler.ivLogo);
        } else {
            hodler.ivLogo.setImageResource(R.mipmap.default_userhead);
        }

        if (rowsBean.getBusinessName() != null) {
            hodler.tvName.setText(rowsBean.getBusinessName());
        } else {
            hodler.tvName.setText(rowsBean.getSenderName());
        }

        hodler.tvTime.setText(TimeRender.getFormat(rowsBean.getUpdateTime()));
        if (rowsBean.getMsgContent() != null) {
            if (rowsBean.getMsgContent().startsWith("*$img_")  && rowsBean.getMsgContent().endsWith("_gmi$*")) {
                hodler.ivPicture.setVisibility(View.VISIBLE);
                hodler.tvContent.setVisibility(View.GONE);
                final String url = rowsBean.getMsgContent().
                        substring(rgx.length(), rowsBean.getMsgContent().length() - rgx.length());
                if (url != null) {
                    Picasso.with(Mcontext).load(url).error(R.mipmap.ic_default_adimage).into(hodler.ivPicture);
                } else {
                    hodler.ivPicture.setImageResource(R.mipmap.logo);
                }
            } else {
                hodler.ivPicture.setVisibility(View.GONE);
                hodler.tvContent.setVisibility(View.VISIBLE);
                //@功能
                if(rowsBean.getMsgContent().contains("_!@_")){
                    String content=ExpressionUtil.RecursiveQuery(Mcontext,rowsBean.getMsgContent(),0);
                    hodler.tvContent.setText(content);
                }else{
                    //对内容做处理
                    hodler.tvContent.setText(ExpressionUtil.praseSample(Mcontext,rowsBean.getMsgContent()));
                }
            }
        } else {
            hodler.ivPicture.setVisibility(View.GONE);
            hodler.tvContent.setVisibility(View.VISIBLE);
            hodler.tvContent.setText("");
        }
        //点击跳转详情
         hodler.llItem.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(Mcontext, CollectMessageDetailActivity.class);
                 intent.putExtra("rowbean",rowsBean);
                 Mcontext.startActivity(intent);
             }
         });
        //长按删除该收藏
        hodler.llItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               popupWindow.pop(hodler.llItem,rowsBean,Gravity.CENTER);
                return true;
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.iv_picture)
        ImageView ivPicture;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

