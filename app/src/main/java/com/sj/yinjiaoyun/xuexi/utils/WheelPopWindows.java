package com.sj.yinjiaoyun.xuexi.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.callback.WheelViewCallBack;
import com.sj.yinjiaoyun.xuexi.widget.WheelView;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/5/12.
 */
public class WheelPopWindows {
    private Context context;
    private PopupWindow popupWindow;
    private Activity activity;
    private String chooseContent;

    public WheelPopWindows(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void pop(final View view, final List<String>list, String title, final WheelViewCallBack callBack) {
        if (popupWindow != null) {
            chooseContent=null;
            popupWindow.dismiss();
            popupWindow = null;
            return;
        }
        popupWindow = new PopupWindow(context);
        final View pView = LayoutInflater.from(context)
                .inflate(R.layout.wheelview_item, null);
        WheelView wheelView = (WheelView) pView.findViewById(R.id.wheelview);
        wheelView.setItems(list);
        wheelView.setSeletion(0);
        Logger.d( "wheelPopWindows==selectedIndex: " + chooseContent);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                Logger.d( "wheelPopWindows==selectedIndex: " + selectedIndex + ", item: " + item);
                chooseContent=item;

            }
        });

        //标题
        TextView tvTitle= (TextView) pView.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        /**
         * 取消
         */
        TextView tvCancel= (TextView) pView.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        /**
         * 确定
         */
        TextView tvaffirm= (TextView) pView.findViewById(R.id.tv_affirm);
        tvaffirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chooseContent==null){
                    chooseContent=list.get(0);
                }
                Logger.d( "wheelPopWindows==selectedIndex: " + chooseContent);
                callBack.getSelectItem(chooseContent);
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(pView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        //设置popupWindow弹出的背景颜色
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
