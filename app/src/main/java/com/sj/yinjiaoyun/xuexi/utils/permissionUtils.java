package com.sj.yinjiaoyun.xuexi.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by Administrator on 2018/3/8.
 * Android中6.0以上的权限认证
 */

public class permissionUtils {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_PHONE_STATE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    private static String[] PERMISSIONS_PHONE = {
            "android.permission.READ_PHONE_STATE",
            "android.permission.READ_SMS"};

    public static void verifyStoragePermissions(Activity activity) {
        //版本大于6.0的情况
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                //检测是否有写的权限
                int permission = checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // 没有写的权限，去申请写的权限，会弹出对话框
                    ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void verifyPhonePermissions(Activity activity) {
        //版本大于6.0的情况
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                //检测是否获取手机号的权限
                boolean permission =checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&checkSelfPermission(activity, "android.permission.READ_PHONE_STATE")!= PackageManager.PERMISSION_GRANTED && checkSelfPermission(activity, "android.permission.READ_PHONE_NUMBERS")!= PackageManager.PERMISSION_GRANTED ;
                if (!permission) {
                    // 没有的权限，去申请权限，会弹出对话框
                    ActivityCompat.requestPermissions(activity, PERMISSIONS_PHONE, REQUEST_PHONE_STATE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
