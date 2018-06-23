package com.sj.yinjiaoyun.xuexi.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/11/17.
 */
public class DebugUtil {
    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

        } catch (Exception e) {
            return false;
        }
    }
}
