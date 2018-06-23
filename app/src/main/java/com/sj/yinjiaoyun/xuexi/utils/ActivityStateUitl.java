package com.sj.yinjiaoyun.xuexi.utils;

import android.app.Activity;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/25.
 * 检测activity 是否初始化前台后台， 程序是否仔仔运行还是已经关闭
 */
public class ActivityStateUitl {

    private final String TAG="abcde";


    private final List<Activity> activityList = new LinkedList<>();
    private static ActivityStateUitl instance;
    private ActivityStateUitl(){

    }

    // 单例模式中获取唯一的MyApplication实例
    public static ActivityStateUitl getInstance() {
        if (null == instance) {
            instance = new ActivityStateUitl();
        }
        return instance;
    }

    // 添加Activity
    public void addActivity(Activity activity) {
        activityList.add(activity);
        Log.i("loginstatus", "addActivity: ");

    }


    // 移除该activity
    public void exitActivity(final Activity activity) {
        activityList.remove(activity);
        Log.i(TAG, "exit:某activity "+activity);
    }

}
