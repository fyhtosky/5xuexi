package com.sj.yinjiaoyun.xuexi.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.callback.OnTimeCountFinishListener;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/30.
 * TextView  作业考试 倒计时显示
 */
public class TimeCount  extends CountDownTimer {

    private Activity mActivity;
    private TextView btn;//按钮
    //倒计时完成的回调
    private OnTimeCountFinishListener onTimeCountFinishListener;

    // 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个事，就把这个按钮传过来就可以了
    public TimeCount( Activity mActivity,long millisInFuture, long countDownInterval,TextView btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn =btn;
    }

    public void setOnTimeCountFinishListener(OnTimeCountFinishListener onTimeCountFinishListener) {
        this.onTimeCountFinishListener = onTimeCountFinishListener;
    }

    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {
        Long m=millisUntilFinished / 1000;
        btn.setClickable(false);//设置不能点击
        btn.setText(secToTime(m) +"");//设置倒计时时间
      //  btn.setPadding(5,5,5,5);//设置内间距
        btn.setTextColor(mActivity.getResources().getColor(R.color.colorWrite));//设置字体颜色为白色

        //设置按钮为灰色，这时是不能点击的
        //btn.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_duck_gray));//设置背景图片
        //btn.setBackgroundColor(mActivity.getResources().getColor(R.color.colorGray));
        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
        span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 2,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
        btn.setText(span);
    }


    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        btn.setText("0分0秒");
       // btn.setPadding(5,5,5,5);//设置内间距
        btn.setAlpha(0.5f);
        btn.setFocusable(false);
       // btn.setClickable(true);//重新获得点击
       // btn.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_drug_green));//还原背景色
        // btn.setBackgroundColor(mActivity.getResources().getColor(R.color.colorGreen));
        if(onTimeCountFinishListener!=null){
            onTimeCountFinishListener.onfinish();
        }
    }



    //把秒转化为分钟
    public  String secToTime(Long time) {
        String timeStr;
        Long hour ;
        Long minute ;
        Long second ;
        if (time <= 0)
            return "0";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second)+"秒";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second);
            }
        }
        //Log.i(TAG, "secToTime: "+timeStr);
        return timeStr;
    }
    private String unitFormat(Long i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
