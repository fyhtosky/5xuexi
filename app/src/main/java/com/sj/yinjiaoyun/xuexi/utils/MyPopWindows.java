package com.sj.yinjiaoyun.xuexi.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.sj.yinjiaoyun.xuexi.Event.NotifyEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.CollectMessageBean;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/4/25.
 */
public class MyPopWindows {
    private Context context;
    private PopupWindow popupWindow;
    private Activity activity;

    public MyPopWindows(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void pop(final View view, final CollectMessageBean.DataBean.MsgEnshrineBean.RowsBean rowsBean, int gravity) {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
            return;
        }
        popupWindow = new PopupWindow(context);
        final View pView = LayoutInflater.from(context)
                .inflate(R.layout.pop_button, null);
        Button button = (Button) pView.findViewById(R.id.pop_delete_message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage(rowsBean.getId());
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(pView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.my_popwindow);
        popupWindow.showAtLocation(view, gravity, DensityUtils.dp2px(context, 120), 0);
        //设置popupWindow弹出的背景颜色
        closePopupWindow(0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                closePopupWindow(1f);
            }
        });

    }

    /*
   * 删除该条收藏
   * @param id
   */
    private void deleteMessage(int id) {
        String params = "?id=" + String.valueOf(id);
        HttpClient.get(this, Api.DELETE_COLLECT_MESSAGE + params, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                    ToastUtil.showShortToast(context, "删除成功");
                    EventBus.getDefault().post(new NotifyEvent());
                } else {
                    ToastUtil.showShortToast(context, "删除失败");
                }
            }

        });

    }

    private void closePopupWindow(float alpaha) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = alpaha;
        activity.getWindow().setAttributes(params);

    }


}