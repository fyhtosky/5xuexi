package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.QusetionOptionBean;
import com.sj.yinjiaoyun.xuexi.utils.LinkMovementMethodExt;
import com.sj.yinjiaoyun.xuexi.utils.NetworkImageGetter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/10/12.
 */
public class jobClozeTabRecycleAdapter extends RecyclerView.Adapter {
    private Context context;
    private NetworkImageGetter mImageGetter;
    private List<QusetionOptionBean>optionBeanList;
    //表示试卷是否阅卷
    private int jobState;


    public jobClozeTabRecycleAdapter(Context context, List<QusetionOptionBean> optionBeanList, int jobState) {
        this.context = context;
        this.optionBeanList = optionBeanList;
        this.jobState = jobState;
        mImageGetter = new NetworkImageGetter(context);
    }

    //字母
    public static String[] myLetter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.job_cloze_list_item, parent, false);
        return new TabViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final QusetionOptionBean optionBean=optionBeanList.get(position);
        final TabViewHolder tabViewHolder= (TabViewHolder) holder;
        mImageGetter.setTvText(tabViewHolder.textview);
        tabViewHolder.textview.setText(Html.fromHtml(myLetter[position]+"、"+optionBean.getOptionTitle().replaceAll("\\\\", ""), mImageGetter, null));
        tabViewHolder.textview.setMovementMethod(LinkMovementMethodExt.getInstance(context, ImageSpan.class));
        if(optionBean.getQuestionType()==2){
            //多选
            if(optionBean.isSelected()){
                tabViewHolder.ivSelect.setImageResource(R.mipmap.list_chioce_2);
            }else {
                tabViewHolder.ivSelect.setImageResource(R.mipmap.list_chioce_1);
            }
        }else {
            //单选
            if(optionBean.isSelected()){
                tabViewHolder.ivSelect.setImageResource(R.mipmap.select_active);
            }else {
                tabViewHolder.ivSelect.setImageResource(R.mipmap.select_disable);
            }
        }
        if(jobState==2 || jobState==3){

        }else {
            //添加点击效果
            tabViewHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(optionBean.getQuestionType()==2){//多选
                        optionBean.setSelected(!optionBean.isSelected());
                        if(optionBean.isSelected()){
                            tabViewHolder.ivSelect.setImageResource(R.mipmap.list_chioce_2);
                        }else {
                            tabViewHolder.ivSelect.setImageResource(R.mipmap.list_chioce_1);
                        }

                    }else {
                        //单选
                        for (int i = 0; i < optionBeanList.size(); i++) {
                            optionBeanList.get(i).setSelected(false);
                        }
                        optionBean.setSelected(true);
                        tabViewHolder.ivSelect.setImageResource(R.mipmap.select_active);
                    }
                    notifyItemRangeChanged(0,optionBeanList.size());
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        if(optionBeanList==null){
            return 0;
        }
        return optionBeanList.size();
    }

     class TabViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.textview)
        TextView textview;
         @BindView(R.id.item)
         LinearLayout item;

         TabViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
