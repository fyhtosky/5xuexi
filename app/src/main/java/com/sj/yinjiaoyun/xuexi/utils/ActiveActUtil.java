package com.sj.yinjiaoyun.xuexi.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/9/28.
 * 退出账号时关闭所有的没有被杀死的Activity，为下次重启另外一个账户时做准备，避免干扰
 * (主要解决切换账号登录成功后首页界面，点击返回键，界面跳转到了上一个账号的首页的问题)
 */
@SuppressLint("Registered")
public class ActiveActUtil extends Application {

    private final List<Activity> activityList = new LinkedList<>();
    private static ActiveActUtil instance;

    private ActiveActUtil() {
    }

    // 单例模式中获取唯一的MyApplication实例
    public static ActiveActUtil getInstance() {
        if (null == instance) {
            instance = new ActiveActUtil();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        instance = null;
    }



    //订单流程里面的activity,订单支付成功后，关闭所有的流程中的activity
    private final List<Activity> activityOrderList = new LinkedList<>();
    //添加订单流程里面的activity
    public void addOrderActivity(Activity activity) {
        activityOrderList.add(activity);
    }
    // 遍历所有订单流程Activity并finish
    public void exitOrder() {
        for (Activity activity : activityOrderList) {
            activity.finish();
        }
        instance = null;
    }
}
