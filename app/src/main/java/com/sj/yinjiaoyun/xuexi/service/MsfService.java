package com.sj.yinjiaoyun.xuexi.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.DataBean;
import com.sj.yinjiaoyun.xuexi.bean.Test;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroups;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.MD5;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.xmppmanager.CheckConnectionListener;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.sj.yinjiaoyun.xuexi.xmppmanager.XmppConnectionManager;
import com.sj.yinjiaoyun.xuexi.xmppmanager.XmppUtil;

import org.jivesoftware.smack.XMPPConnection;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wanzhiying on 2017/3/6.
 * 后台登录服务
 */
public class MsfService extends Service {
    private static MsfService mInstance = null;
    public static DatagramSocket ds = null;

    private NotificationManager mNotificationManager;

    private String mUserName, mPassword;
    private XmppConnectionManager mXmppConnectionManager;
    private XMPPConnection mXMPPConnection;
    private List<TigaseGroups>list=new ArrayList<>();
    private CheckConnectionListener checkConnectionListener;
    private BroadcastReceiver receiver;


    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public MsfService getService() {
            return MsfService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mUserName = "5f_"+PreferencesUtils.getSharePreStr(this, "username");
        mPassword = PreferencesUtils.getSharePreStr(this, "pwd");
        checkConnectionListener=new CheckConnectionListener(this);
        initReceiver();
        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);         // 通知
        mXmppConnectionManager = XmppConnectionManager.getInstance();
        initXMPPTask();

    }

    public static MsfService getInstance() {
        return mInstance;
    }


    /**
     * 初始化xmpp和完成后台登录
     */
    private void initXMPPTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    initXMPP();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 初始化XMPP
     */
    void initXMPP() {
        mXMPPConnection = mXmppConnectionManager.init();						//初始化XMPPConnection
        loginXMPP();															//登录XMPP
    }

    /**
     * 登录XMPP
     */
    public void loginXMPP() {
        try {
            mPassword = PreferencesUtils.getSharePreStr(this, "pwd");
            //IM服务器密码加密
            String MDPwd= MD5.getMessageDigest(mPassword.getBytes());
            Logger.d("用户名："+mUserName+";加密密码："+MDPwd);
            mXMPPConnection.connect();
            mXMPPConnection.login(mUserName, MDPwd);
            /**
             * 如果IM连接成功
             */
            if(mXMPPConnection.isAuthenticated()){                                     //登录成功
                MyApplication.xmppConnection=mXMPPConnection;
                JoinChatRoom();
//                sendLoginBroadcast(true);
                Logger.d("IM登录服务器成功");
            }else{
                sendLoginBroadcast(false);
                //如果登录失败，自动销毁Service
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendLoginBroadcast(false);
            stopSelf();
        }

    }

    /**
     * 发送登录状态广播
     * @param isLoginSuccess
     */
    void sendLoginBroadcast(boolean isLoginSuccess){
        Intent intent =new Intent(Const.ACTION_IS_LOGIN_SUCCESS);
        intent.putExtra("isLoginSuccess", isLoginSuccess);
        sendBroadcast(intent);
    }

    /**
     * 后台登录IM服务器
     */
    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Const.STOP_SELF)){
                    stopSelf();
                }
            }
        };
        //注册广播接收者
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(Const.STOP_SELF);
        registerReceiver(receiver, mFilter);
    }
    /**
     * 获取所有的群加入房间
     */
    private void JoinChatRoom() {

        HashMap<String ,String> map=new HashMap<>();
        map.put("endUserId",PreferencesUtils.getSharePreStr(this, "username"));
        map.put("userType","0");
        HttpClient.postStr(this, Api.GET_GROUP_LIST, map, new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if(result==null){
                    return;
                }
                Gson gson=new Gson();
                Test a=gson.fromJson(result, Test.class);
                DataBean dataBean = a.getData();
                List<TigaseGroups> b= dataBean.getTigaseGroups();
                list.clear();
                Logger.d("群列表的数据源"+dataBean.getTigaseGroups().toString());
                list.addAll(dataBean.getTigaseGroups());
                //赋值给全局变量
                MyApplication.groupsList=list;
                TigaseGroups d=b.get(0);
                Logger.d(""+d.getTigaseGroupVO().toString());
//                获取所有的群id加入群聊天室
                if(list.size()>0) {
                    MyApplication.list= XmppUtil.getMultiChatList(list,"5f_"+PreferencesUtils.getSharePreStr(MsfService.this, "username"));

                }
            }

        });
        sendLoginBroadcast(true);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        try {
            if (mXMPPConnection != null) {
                mXMPPConnection.disconnect();
                mXMPPConnection = null;
            }
            if(mXmppConnectionManager!=null){
                mXmppConnectionManager = null;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        stopSelf();
        super.onDestroy();
    }

}

