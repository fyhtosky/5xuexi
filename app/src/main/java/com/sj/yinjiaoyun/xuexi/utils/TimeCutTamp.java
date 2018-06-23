package com.sj.yinjiaoyun.xuexi.utils;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author shenjun  E-mail: 961784535@qq.com
 * @version 创建时间：2016年10月27日 下午3:49:10
 * 类说明： 截取时间段 工具类
 * 记录当前时间位置
 */
public class TimeCutTamp {

	static long sink=0;//记录当前时间节点
	static String TAG="timeTamp";

	/* //把两个时间段的间隔    秒转化为   xx小时xx分钟xx秒 的时间长度
	 * @param time
	 * @return
	 */
	private static String getTimesTamp(Long g1,Long g2) {
		Long time=(g2-g1)/1000;
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

		return timeStr;
	}
	private static  String unitFormat(Long i) {
		String retStr;
		if (i >= 0 && i < 10)
			retStr = "0" + Long.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}


	/**
	 * 把当前毫秒数时间转化为制定指定的时间格式
	 * 15:33:34
	 * @param timeInMillis time
	 * @return String
	 */
	private static  String getTime(long timeInMillis) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * 获取当前时间毫秒数
	 * @return long
	 */
	private static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}


	public static String getTimesTamp(long g2){
		if(sink<=0){
			return "0分0秒";
		}else{
			return getTimesTamp(sink, g2);
		}
	}

	/**
	 * 记录当前时间毫秒数
	 * @return long
	 */
	public static void sinkCurrentTimeInLong() {
		sink = System.currentTimeMillis();
		Log.i("videos", "sinkCurrentTimeInLong:记录当前时间 "+ TimeCutTamp.getTime(sink));
	}

	public static Long getDuration(){
		Log.i("videos", "getDur: "+"视频播放的时间段"+(getCurrentTimeInLong()-sink));
		return (getCurrentTimeInLong()-sink);
	}


	//获取时间   自定义刷新里面用到
	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		return sdf.format(date);
	}
	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}

}
