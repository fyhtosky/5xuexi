package com.sj.yinjiaoyun.xuexi.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/20.
 * 工具类
 */
public class MyUtil {


    /**
     * 给某一段话中的某一段文子家特殊颜色的字体
     *
     * @param context   上下文
     * @param text      全文
     * @param textcolor 需要改变颜色的字体
     * @param color     颜色
     * @return
     */
    public static SpannableStringBuilder setTextFormatColor(Context context, String text, String textcolor, int color) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(textcolor)) {
            return null;
        }
        StringFormatUtil spanStr = new StringFormatUtil(context, text,
                textcolor, color).fillColor();
        return spanStr.getResult();
    }


    /**
     * 普通dialoglog提示框
     *
     * @param context
     * @param title
     * @param message
     * @param positive
     * @param negative
     */
    public static void setDailog(Context context, String title, String message, String positive, String negative) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!(title.equals(""))) {
            builder.setTitle(title);
        }
        if (!(message.equals(""))) {
            builder.setMessage(message + positive);
        }
        if (!(positive.equals(""))) {//确定
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        if (!(negative.equals(""))) {//取消
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        builder.create().show();
    }

    /**
     * 圆形缓冲条进行条提示框
     *
     * @param context
     * @param hint
     */
    public static Dialog setCircleDialog(Context context, String hint) {
        final Dialog mDialog = CustomCircleDialog.createLoadingDialog(context, hint);//文字即为显示的内容
        mDialog.setCancelable(true);//允许返回
        mDialog.show();//显示
        return mDialog;
    }


    //自定义dialog
    public static class CustomCircleDialog {
        /**
         * 得到自定义的progressDialog
         *
         * @param context
         * @param msg
         * @return
         */
        public static Dialog createLoadingDialog(Context context, String msg) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
            // main.xml中的ImageView
            ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
            TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
            // 加载动画
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.load_animation);
            // 使用ImageView显示动画
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
            tipTextView.setText(msg);// 设置加载信息

            Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setCancelable(false);// 不可以用“返回键”取消
            loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
            return loadingDialog;
        }
    }



    /**
     * 把当前毫秒数时间转化为制定指定的时间
     * @param timeInMillis
     * @param format          时间格式 yyyy-MM-dd HH:mm
     * @return
     */
    public static String getTime(long timeInMillis,String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(timeInMillis));
    }

    /**
     * 转化格式
     *
     * @param d
     * @return
     */
    public static String saveTwoScale(Double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (d != null && d > 0) {
            return  ""+df.format(d);
        }
        return "免费";
    }


    /**
     * 转化时间格式  个龙类型的转化为 XX分钟XX秒格式
     * @param time
     * @return
     */
    public static String secToTime(Long time) {
        String timeStr ;
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
                timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second)+"秒";
            }
        }
        return timeStr;
    }
    private static String unitFormat(Long i) {
        String retStr ;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    /**
     * 把字符串型百分数改变成into型整数
     * @param str  字符串型百分数
     * @return    整数
     */
    public static  int transFormation(String str) {
        int a=0;
        Pattern p=Pattern.compile("(\\d+)");
        Matcher ma=p.matcher(str);
        if(ma.find()){
            a=Integer.valueOf(ma.group(1));
        }
        return  a;
    }





}
