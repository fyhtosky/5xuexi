package com.sj.yinjiaoyun.xuexi.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.ChatActivity;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.ChatPrivateBean;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.view.GrayDecoration;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/4/18.
 */
public class PopPresenter {
    private PopupWindow popupWindow;
    private List<ChatPrivateBean.DataBean.MsgPgBean.RowsBean> rowsBeanList;
    private String groupId;
    private String msgType;
    private String sender;
    private Activity activity;

    public PopPresenter(List<ChatPrivateBean.DataBean.MsgPgBean.RowsBean> rowsBeanList, String groupId, String msgType, Activity activity) {
        this.rowsBeanList = rowsBeanList;
        this.groupId = groupId;
        this.msgType = msgType;
        sender = "5f_"+ PreferencesUtils.getSharePreStr(MyApplication.getContext(), "username");
        this.activity=activity;
    }

    public void pop(final Context context, final View view, final int posit){
        if (popupWindow != null){
            popupWindow.dismiss();
            popupWindow = null;
            return;
        }
        popupWindow = new PopupWindow(context);
        final View pView = LayoutInflater.from(context)
                .inflate(R.layout.pop_message,null);
        final List<String> list = Arrays.asList("复制","收藏");
        RecyclerView rv = (RecyclerView) pView.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        rv.addItemDecoration(new GrayDecoration());
        rv.setAdapter(new BaseQuickAdapter<String>(R.layout.item_pop_message,list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String str) {
                baseViewHolder.setText(R.id.tv,str);
                final ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean=rowsBeanList.get(posit);
                final int position = list.indexOf(str);
                baseViewHolder.setOnClickListener(R.id.tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        switch (position){
                            case 0:
                                ClipboardManager cmb = (ClipboardManager) context.getSystemService(ChatActivity.CLIPBOARD_SERVICE);
                                cmb.setText(bean.getMsg());
                                ToastUtil.showShortToast(context, "已复制到剪切板");
                                break;
                            case 1:
                                HashMap<String,String>hashMap=new HashMap<>();
                                hashMap.put("jid",sender);
                                hashMap.put("senderJid",bean.getSender());
                                hashMap.put("receiverJid",bean.getReceiver());
                                hashMap.put("msgType",msgType);
                                hashMap.put("msgContent",bean.getMsg());
                                hashMap.put("msgCreateTime",""+bean.getCreateTime());
                                HttpClient.post(this, Api.COLLECT_MESSAGE, hashMap, new CallBack<ReturnBean>() {
                                    @Override
                                    public void onSuccess(ReturnBean result) {
                                        if(result==null){
                                            return;
                                        }
                                        if(result.isSuccess()){
                                            ToastUtil.showShortToast(context,"收藏成功");
                                        }
                                    }
                                });

                                break;
                        }

                    }
                });
            }
        });
        popupWindow.setContentView(pView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.my_popwindow);
//        popupWindow.showAsDropDown(view,0, DensityUtils.dp2px(context,-3));
        closePopupWindow(0.6f);
        popupWindow.showAtLocation(view,Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                closePopupWindow(1f);
            }
        });
    }
    public void popImage(final Context context, final View view, final int posit){
        if (popupWindow != null){
            popupWindow.dismiss();
            popupWindow = null;
            return;
        }
        popupWindow = new PopupWindow(context);
        final View pView = LayoutInflater.from(context)
                .inflate(R.layout.pop_message,null);
        final List<String> list = Arrays.asList("保存本地","收藏");
        RecyclerView rv = (RecyclerView) pView.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        rv.addItemDecoration(new GrayDecoration());
        rv.setAdapter(new BaseQuickAdapter<String>(R.layout.item_pop_message,list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String str) {
                baseViewHolder.setText(R.id.tv,str);
                final int position = list.indexOf(str);
                baseViewHolder.setOnClickListener(R.id.tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        final ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean=rowsBeanList.get(posit);
                        String rgx="*$img_";
                        switch (position){
                            //保存到本地
                            case 0:
                                final String url=bean.getMsg().substring(rgx.length(),bean.getMsg().length()-rgx.length());
                                Logger.d("内容的URL："+url);
                                PhotoUtils.decodeUriAsBitmapFromNet(url, new CallBack<Bitmap>() {
                                                @Override
                                                public void onSuccess(Bitmap result) {
                                                    try {
                                                    PhotoUtils.saveFile(mContext,result);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                break;
                            //收藏
                            case 1:
                                HashMap<String,String>hashMap=new HashMap<>();
                                hashMap.put("jid",sender);
                                hashMap.put("senderJid",bean.getSender());
                                hashMap.put("receiverJid",bean.getReceiver());
                                hashMap.put("msgType",msgType);
                                hashMap.put("msgContent",bean.getMsg());
                                hashMap.put("msgCreateTime",""+bean.getCreateTime());
                                HttpClient.post(this, Api.COLLECT_MESSAGE, hashMap, new CallBack<ReturnBean>() {
                                    @Override
                                    public void onSuccess(ReturnBean result) {
                                        if(result==null){
                                            return;
                                        }
                                        if(result.isSuccess()){
                                            ToastUtil.showShortToast(context,"收藏成功");
                                        }
                                    }
                                });

                                break;
                        }

                    }
                });
            }
        });
        popupWindow.setContentView(pView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.my_popwindow);
//        popupWindow.showAsDropDown(view,0, DensityUtils.dp2px(context,-3));
        popupWindow.showAtLocation(view,Gravity.CENTER,0,0);
        closePopupWindow(0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                closePopupWindow(1f);
            }
        });
    }
    private void closePopupWindow(float alpaha) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = alpaha;
        activity.getWindow().setAttributes(params);

    }
}
