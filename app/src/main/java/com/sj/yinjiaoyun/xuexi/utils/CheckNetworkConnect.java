package com.sj.yinjiaoyun.xuexi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/9/6.
 * 检查网络连接类型 （移动网络 和wifi网络）
 * int net= CheckInternet.checkNetworkAvailable(mcontext);
 * 返回0则为无网络，返回1则为wifi，返回2则为移动网络
 */
public class CheckNetworkConnect {

    String TAG="check";
    public static int checkNetworkAvailable(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo wifi =connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile =connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.isAvailable()){
            Log.i("check", "checkNetworkAvailable: "+1);
            return 1;//返回1则为wifi，
        }else if(mobile.isAvailable()){
            Log.i("check", "checkNetworkAvailable: "+2);
            return 2;//2则为移动网络
        }else{
            Log.i("check", "checkNetworkAvailable: "+3);
            return 0;//返回0则为无网络
        }
    }


    /**
     * 判断当前网络是否是3G网络
     * @param context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 判断当前网络是否是wifi网络
     * if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) { //判断3G网
     * @param context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

}
