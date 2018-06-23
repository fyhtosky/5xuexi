package com.sj.yinjiaoyun.xuexi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.ImgPageActivity;
import com.sj.yinjiaoyun.xuexi.bean.ChatPrivateBean;
import com.sj.yinjiaoyun.xuexi.bean.ImageBean;
import com.sj.yinjiaoyun.xuexi.utils.ExpressionUtil;
import com.sj.yinjiaoyun.xuexi.utils.PopPresenter;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.TimeRender;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by wanzhiying on 2017/3/15.
 */
public class ChatRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<ChatPrivateBean.DataBean.MsgPgBean.RowsBean> list;
    private String sender;
    private PopPresenter popPresenter;
    private ArrayList<ImageBean>imageList=new ArrayList<>();
    private String rgx="*$img_";
    public ChatRecordAdapter(Context mContext, List<ChatPrivateBean.DataBean.MsgPgBean.RowsBean> list,String groupId) {
        this.mContext = mContext;
        this.list = list;
        sender = "5f_"+ PreferencesUtils.getSharePreStr(mContext, "username");
        popPresenter=new PopPresenter(list,groupId,"0", (Activity) mContext);

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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder hodler;
        if (convertView == null) {
            hodler=new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.chat_lv_item, parent,false);
            hodler.rl_chat= (RelativeLayout) convertView.findViewById(R.id.rl_chat);//聊天布局
            hodler.progress_load=(ProgressBar) convertView.findViewById(R.id.progress_load);//ProgressBar
            //接收的消息
            hodler.fromIcon= (ImageView) convertView.findViewById(R.id.chatfrom_icon);//他人头像
            hodler.fromContainer = (LinearLayout) convertView.findViewById(R.id.chart_from_container);
            hodler.fromText = (TextView) convertView.findViewById(R.id.chatfrom_content);//文本
            hodler.fromImg= (ImageView) convertView.findViewById(R.id.chatfrom_img);//图片
            //发送的消息
            hodler.toContainer = (RelativeLayout) convertView.findViewById(R.id.chart_to_container);
            hodler.toIcon= (ImageView) convertView.findViewById(R.id.chatto_icon);//自己头像
            hodler.toText = (TextView) convertView.findViewById(R.id.chatto_content);//文本
            hodler.toImg = (ImageView) convertView.findViewById(R.id.chatto_img);//图片
            //时间
            hodler.time = (TextView) convertView.findViewById(R.id.chat_time);
            convertView.setTag(hodler);
        }else{
            hodler= (ViewHolder) convertView.getTag();
        }
        final ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean=list.get(position);
        /**
         * 加载数据
         */

        //显示聊天创建的时间(隔5秒钟显示一次)
        if (position == 0) {
            hodler.time.setVisibility(View.VISIBLE);
        }else{
           if( bean.getCreateTime()-list.get(position-1).getCreateTime()>=30000){
               hodler.time.setVisibility(View.VISIBLE);
           }else{
               hodler.time.setVisibility(View.GONE);
           }
        }
        notifyDataSetChanged();
        hodler.time.setText(TimeRender.FormatChat(bean.getCreateTime()));
        //默认设置
        hodler.toContainer.setVisibility(View.GONE);//隐藏右侧布局
        hodler.fromContainer.setVisibility(View.VISIBLE);
        //(显示左侧布局)
        if(!sender.equals(bean.getSender())){
            hodler.toContainer.setVisibility(View.GONE);//隐藏右侧布局
            hodler.fromContainer.setVisibility(View.VISIBLE);
            //显示用户的LOGo
            if(bean.getMsgLogo()!=null){
                if(!"".equals(bean.getMsgLogo())&&!"null".equals(bean.getMsgLogo())){
                    Picasso.with(mContext).load(bean.getMsgLogo()).error(R.mipmap.default_userhead).into(hodler.fromIcon);
                }else{
                    Picasso.with(mContext).load(R.mipmap.default_userhead).into(hodler.fromIcon);
                }
            }else{
                Picasso.with(mContext).load(R.mipmap.default_userhead).into(hodler.fromIcon);
            }


            //图片类型
            if(bean.getMsg().startsWith("*$img_") && bean.getMsg().endsWith("_gmi$*")){
                hodler.fromText.setVisibility(View.GONE);//文本
                hodler.fromImg.setVisibility(View.VISIBLE);//图片
                hodler.progress_load.setVisibility(View.GONE);
                Logger.d("MSG的内容："+bean.getMsg()+"   MSG的长度："+bean.getMsg().length());
                final String url=bean.getMsg().
                        substring(rgx.length(), bean.getMsg().length()-rgx.length());
                if(url!=null){
                    Logger.d("内容的URL："+url);
                    Picasso.with(mContext).load(url).placeholder(R.drawable.progressbar_landing).error(R.mipmap.error).into(hodler.fromImg);
                }else{
                    hodler.fromImg.setImageResource(R.mipmap.error);
                }
                //监听器
                hodler.fromImg.setOnClickListener(new ClickListener(list,position));
                hodler.fromImg.setOnLongClickListener(new MyListener(hodler.fromImg,position));
            }else{//文本类型
                hodler.fromText.setVisibility(View.VISIBLE);//文本
                hodler.fromImg.setVisibility(View.GONE);//图片
                hodler.progress_load.setVisibility(View.GONE);
                hodler.fromText.setText(ExpressionUtil.praseSample(mContext,bean.getMsg()));
                //添加监听器
                hodler.fromText.setOnLongClickListener(new MyListener(hodler.fromText,position));
                //增加文本链接类型
                Linkify.addLinks(hodler.fromText,Linkify.ALL);
            }
            //显示右侧布局
        }else{//发送消息
            hodler.toContainer.setVisibility(View.VISIBLE);
            hodler.fromContainer.setVisibility(View.GONE);
            //显示用户的LOGo
            if(bean.getMsgLogo()!=null){
                if(!"".equals(bean.getMsgLogo())&&!"null".equals(bean.getMsgLogo())){
                    Picasso.with(mContext).load(bean.getMsgLogo()).error(R.mipmap.default_userhead).into(hodler.toIcon);
                }else{
                    Picasso.with(mContext).load(R.mipmap.default_userhead).into(hodler.toIcon);
                }
            }else{
                Picasso.with(mContext).load(R.mipmap.default_userhead).into(hodler.toIcon);
            }
            //图片类型
            if(bean.getMsg().startsWith("*$img_") && bean.getMsg().endsWith("_gmi$*")){
                hodler.toText.setVisibility(View.GONE);//文本
                hodler.toImg.setVisibility(View.VISIBLE);//图片
                hodler.progress_load.setVisibility(View.GONE);
                Logger.d("MSG的内容："+bean.getMsg()+"   MSG的长度："+bean.getMsg().length());
                final String url=bean.getMsg().substring(rgx.length(),bean.getMsg().length()-rgx.length());
                Logger.d("内容的URL："+url);
                if(url!=null){
                    Picasso.with(mContext).load(url).placeholder(R.drawable.progressbar_landing).error(R.mipmap.error).into(hodler.toImg);
                }else{
                    hodler.toImg.setImageResource(R.mipmap.error);
                }
                //监听器
                hodler.toImg.setOnClickListener(new ClickListener(list,position));
                hodler.toImg.setOnLongClickListener(new MyListener(hodler.toImg,position));
            }else{//文本类型
                hodler.toText.setVisibility(View.VISIBLE);//文本
                hodler.toImg.setVisibility(View.GONE);//图片
                hodler.progress_load.setVisibility(View.GONE);
                //对内容做处理
                hodler.toText.setText(ExpressionUtil.praseSample(mContext,bean.getMsg()));
                //添加监听器
                hodler.toText.setOnLongClickListener(new MyListener( hodler.toText,position));
                //增加文本链接类型
                Linkify.addLinks(hodler.toText,Linkify.ALL);
            }
        }
        return convertView;
    }

     class ViewHolder {
         RelativeLayout rl_chat;
         ImageView fromIcon, toIcon;
         ImageView fromImg,toImg;
         TextView fromText, toText, time;
         LinearLayout fromContainer;
         RelativeLayout toContainer;
         ProgressBar progress_load;

    }

    /**
     * 屏蔽listitem的所有事件
     * */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
   private class MyListener implements View.OnLongClickListener{
       private View tv;
       private int postion;


       public MyListener(View tv, int postion) {
           this.tv = tv;
           this.postion = postion;
       }

       @Override
       public boolean onLongClick(View v) {
           switch (v.getId()){
               case  R.id.chatfrom_content:
               case R.id.chatto_content:
                   popPresenter.pop(mContext,tv,postion);
                   break;

               case R.id.chatfrom_img:
               case R.id.chatto_img:
                   popPresenter.popImage(mContext,tv,postion);
                   break;
           }
           return true;
       }
   }
    private class ClickListener implements View.OnClickListener{
        private List<ChatPrivateBean.DataBean.MsgPgBean.RowsBean> list;
        private int postion;

        public ClickListener(List<ChatPrivateBean.DataBean.MsgPgBean.RowsBean> list, int postion) {
            this.list = list;
            this.postion = postion;
        }

        @Override
        public void onClick(View v) {
            imageList.clear();
            if(list.size()>0){
                for (int i=0;i<list.size();i++){
                    ChatPrivateBean.DataBean.MsgPgBean.RowsBean rowBean=list.get(i);
                    if(rowBean.getMsg().startsWith("*$img_") && rowBean.getMsg().endsWith("_gmi$*")){
                        String url=rowBean.getMsg().substring(rgx.length(), rowBean.getMsg().length()-rgx.length());
                        ImageBean imageBean=new ImageBean(url,rowBean.getId());
                        imageList.add(imageBean);
                    }
                }
                Logger.d("imageList数量"+imageList.size());
            }
            switch (v.getId()){
                case R.id.chatfrom_img:
                case R.id.chatto_img:
                    if(imageList.size()>0){
                        //启动动画
                        Intent intentImg=new Intent(mContext, ImgPageActivity.class);
                        intentImg.putExtra("id",list.get(postion).getId());
                        intentImg.putExtra("imageList",imageList);
                        mContext.startActivity(intentImg);
                    }
                    break;

            }
        }
    }
}
