package com.sj.yinjiaoyun.xuexi.jpush;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/1/23.
 * 极光推送操作工具类 
 */
public class MyJPushUitl {
    
    static  String TAG="jpushuitl";
    static Context context;

    private static  MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static String JPUSH_ALIAS="5xuexi_st";//别名前置字段
    private static final int MSG_SET_ALIAS = 1001;//别名识别码

    /**
     * 给推送设置别名字段
     * @param endUserId  用户id
     */
    public static void setAlias(Context contxet, String endUserId) {
        Log.i(TAG, "setAlias: "+endUserId);
        context=contxet;
        if (TextUtils.isEmpty(JPUSH_ALIAS)) {
            Log.e(TAG, "setAlias: "+ "别名为空");
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(JPUSH_ALIAS)) {
            Log.e(TAG, "setAlias: "+"格式不对");
            return;
        }
        if(TextUtils.isEmpty(endUserId)){//别名设置为空，即取消别名
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));
        }else {
            StringBuilder sb = new StringBuilder();
            sb.append(JPUSH_ALIAS).append("_").append(endUserId).toString();
            Log.e(TAG, "setAlias: "+sb.toString());
            //调用JPush API设置Alias
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, sb.toString()));
        }
    }

    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS://设置别名
                    Log.d(TAG, "Set alias in handler."+ msg.obj);
                   JPushInterface.setAliasAndTags(context, (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {//别名回调函数
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0://0为成功，其他返回码请参考错误码定义。
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    try {
                        registerMessageReceiver();//极光推送注册
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String rid = JPushInterface.getRegistrationID(context);
                    if (!rid.isEmpty()) {
                        Log.i(TAG, "setJPush:rid "+rid);
                        // Toast.makeText(this, rid, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e(TAG, "setJPush: "+"Get registration fail, JPush init failed!");
                        // Toast.makeText(this, "Get registration fail, JPush init failed!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(context)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
            Log.e(TAG, "gotResult: "+logs);
        }
    };

    //极光推送注册
    private static void registerMessageReceiver() {
        Log.i(TAG, "registerMessageReceiver: ");
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        context.registerReceiver(mMessageReceiver, filter);
    }
    //极光推送注销
    public static void  unregisterReceiver(){
        try {
            if(mMessageReceiver!=null){
                context.unregisterReceiver(mMessageReceiver);
            }
        }catch (  Exception e ){
            e.getLocalizedMessage();
        }



    }
    private static class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : ").append(messge).append("\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : ").append(extras).append("\n");
                }
                Toast.makeText(context,showMsg.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * //恢复接受通知
     * @param context
     */
    public static void resumePush(Context context){
        Log.i(TAG, "resumePush: "+"恢复接受通知");
        JPushInterface.resumePush(context);//恢复接受通知
    }

    /**
     * //停止接受该用户别名下接收的通知
     * @param context
     */
    public static void stopPush(Context context){
        Log.i(TAG, "stopPush: "+"停止接受该用户别名下接收的通知");
        JPushInterface.stopPush(context);//停止接受该用户别名下接收的通知
    }

    /**
     * 设置保存消息条数 默认为5条
     * @param context
     * @param maxNum
     */
    public static void setMaxNum(Context context, int maxNum){
        JPushInterface.setLatestNotificationNumber(context,maxNum);
    }

    /**
     * 清除消息通知栏
     * @param context
     */
    public static  void setClearNotification(Context context){
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

}
