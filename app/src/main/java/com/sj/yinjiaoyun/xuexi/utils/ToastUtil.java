package com.sj.yinjiaoyun.xuexi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Toast提示的工具类及网络状态提示
 */
public class ToastUtil {
	public static boolean isFrist=false;
	public static void showShortToast(Context context,String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongToast(Context context,String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void getNetworkSatte(Context mContext) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取NetworkInfo对象
		NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
		// 遍历每一个对象
		for (NetworkInfo networkInfo : networkInfos) {
			if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
				if(!isFrist){
					Toast.makeText(mContext, "当前网络类型 " + networkInfo.getTypeName(), Toast.LENGTH_SHORT).show();
					isFrist=true;
				}

				return;
			}
		}
		isFrist=false;
		Toast.makeText(mContext, "当前网络不可用", Toast.LENGTH_SHORT).show();


	}
}
