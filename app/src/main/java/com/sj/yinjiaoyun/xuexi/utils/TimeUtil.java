package com.sj.yinjiaoyun.xuexi.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/6.
 * 实践操作工具类
 */
public class TimeUtil {
    /**
     * 获取当前时间毫秒数
     * @return long
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }


    /**
     * 把当前毫秒数时间转化为制定指定的时间格式
     * 15:33:34
     * @param timeInMillis time
     * @return String
     */
    public static  String getTime(long timeInMillis) {
           /*时间戳转换成字符窜*/
            Date d = new Date(timeInMillis);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
            return sf.format(d);
    }

    /**
     * 把当前毫秒数时间转化为制定指定的时间格式
     * 15:33:34
     * @param timeInMillis time
     * @return String
     */
    public static  String getTimeYear(long timeInMillis) {
        Date d = new Date(timeInMillis);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.getDefault());
        return sf.format(d);
    }

    /**
     * 获取当前日期时间  并返回指定格式的
     * @param format  "yyyy年MM月dd日 HH:mm"
     * @return
     */
    public  static String getCurrentFormatTime(String format){
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat sf = new SimpleDateFormat(format,Locale.getDefault());
        return sf.format(d);
    }

    /**
     * 比较两个相同格式日期的前后大小（微专业也预约用到）
     * @param date1
     * @param date2
     * @return   1 表示date1在date2前   -1 表示date1在date2后   0 表示date1和date2在同一天
     */
    public static int DateCompare(String date1,String date2)  {
        try {
            //设定时间的模板
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日",Locale.getDefault());
            //得到指定模范的时间
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            //比较
            if ( d1.getTime() >d2.getTime() ) {
                //  d1 在d2前
                return -1;
            } else if (d1.getTime() < d2.getTime()) {
                // d2在d1后
                return  1;
            } else {
                return 0;
            }
        }catch (Exception e){
            Log.i("subscribe", "DateCompare: "+e.toString());
        }
        return 0;
    }

}
