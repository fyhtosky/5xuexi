package com.sj.yinjiaoyun.xuexi.entry;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.ActivityStateUitl;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/25.
 * 重写一个Activity 的基类 ，记录其子Activity的状态
 */
@SuppressLint("Registered")
public class MyBaseActivity extends AppCompatActivity {

    String TAG = getClass().getName();
//    String TAG="abcde";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveActUtil.getInstance().addActivity(this);//退出登陆时关闭所有的Activity
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        JPushInterface.setLatestNotificationNumber(getApplicationContext(), 5); //最多显示的条数
        ActivityStateUitl.getInstance().addActivity(this);//记录avtivity的状态，最后记录程序是否在运行
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        ActivityStateUitl.getInstance().exitActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "current Activity is :" + TAG);
    }


    //改变状态栏为自己颜色颜色代码,(还需在不觉最外层中添加android:fitsSystemWindows="true")
  /*  @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorGreen);//通知栏所需颜色
        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }*/


}
