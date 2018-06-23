package com.sj.yinjiaoyun.xuexi.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.DownApkEvent;
import com.sj.yinjiaoyun.xuexi.Event.EmptyEvent;
import com.sj.yinjiaoyun.xuexi.Event.NoticeEvent;
import com.sj.yinjiaoyun.xuexi.Event.SwitchTypeEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.DataBean;
import com.sj.yinjiaoyun.xuexi.bean.Test;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroups;
import com.sj.yinjiaoyun.xuexi.bean.UserInfo;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.fragment.FindFragment;
import com.sj.yinjiaoyun.xuexi.fragment.HomeFragment;
import com.sj.yinjiaoyun.xuexi.fragment.MeFragment;
import com.sj.yinjiaoyun.xuexi.fragment.MessageFragment;
import com.sj.yinjiaoyun.xuexi.fragment.NewMajorFragment;
import com.sj.yinjiaoyun.xuexi.fragment.ScheduleFragment;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.jpush.MyJPushUitl;
import com.sj.yinjiaoyun.xuexi.manager.UpdateManager;
import com.sj.yinjiaoyun.xuexi.utils.DialogUtils;
import com.sj.yinjiaoyun.xuexi.utils.PhoneInfoUtils;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.utils.permissionUtils;
import com.sj.yinjiaoyun.xuexi.xmppmanager.CheckConnectionListener;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.sj.yinjiaoyun.xuexi.xmppmanager.MyChatManagerListener;
import com.sj.yinjiaoyun.xuexi.xmppmanager.MyPacketListener;
import com.sj.yinjiaoyun.xuexi.xmppmanager.XmppConnectionManager;
import com.sj.yinjiaoyun.xuexi.xmppmanager.XmppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.XMPPConnection;
import org.json.JSONObject;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import q.rorbin.badgeview.QBadgeView;


public class MainActivity extends MyBaseActivity implements
         HomeFragment.CallBackFromHome, HttpDemo.HttpCallBack {

    private static final String SELECT_ITEM = "radioGroup";
    private static final String POSITION = "position";
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_FIND = 1;
    private static final int FRAGMENT_SCHEDULE = 2;
    private static final int FRAGMENT_MESSAGE = 3;
    private static final int FRAGMENT_ME = 4;
    //记录选中的下标
    private int position;
    private  RadioGroup radioGroup;
    private  Button tvHint;
    private  Button btCount;

    private HomeFragment homeFragment;
    private FindFragment findFragment;
    private ScheduleFragment scheduleFragment;
    private MessageFragment messageFragment;
    private MeFragment meFragment;
    private String endUserId;
    private UpdateManager updateManager;//是否自动更新
    private  List<Pairs> pairsList;
    private HttpDemo demo;
    private static Boolean isJPushFirst = true;//第一次推送

    private long exitTime ;
    private QBadgeView qBadgemessage;
    private QBadgeView qBadgenotice;
    private  int noticeCount;//用户未读消息数
    //标示用户登录状态
    private boolean loginState=false;
    //涉及登录IM服务器的类
    private XMPPConnection mXMPPConnection;
    private CheckConnectionListener checkConnectionListener;
    private MyPacketListener myPacketListener;
    private MyChatManagerListener myChatManagerListener;
    public static DatagramSocket ds = null;
    private String jid;
    //准备群组的数据源
    private List<TigaseGroups>groupsList=new ArrayList<>();
    //IM服务器密码加密
    private  String MDPwd;
    //1.账号登录2.学号登录3.第三方登录
    private long flag;
    //单线程化的线程池
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    break;
                case 1:
                    ToastUtil.showShortToast(MyApplication.getContext(),"登录服务器失败");
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        permissionUtils.verifyStoragePermissions(this);
        permissionUtils.verifyPhonePermissions(this);
        EventBus.getDefault().register(this);
        updateManager = new UpdateManager(this);
        initView();
        loginIm();
        showDefaultFragment(savedInstanceState);

    }



    /**
     * 展示默认的Fragment
     * @param savedInstanceState
     */
    private void showDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
              homeFragment=HomeFragment.newInstance();
              findFragment=FindFragment.newInstance();
              scheduleFragment=ScheduleFragment.newInstance();
              messageFragment=MessageFragment.newInstance();
              meFragment=MeFragment.newInstance();
              //设置消息
              meFragment.setDateForActivity(noticeCount);
            // 恢复 recreate 前的位置
             displayFragment(savedInstanceState.getInt(POSITION));
             radioGroup.check(savedInstanceState.getInt(SELECT_ITEM));
        }else {
            displayFragment(FRAGMENT_HOME);
        }
    }

    /**
     * 显示Fragment
     * 1.Fragment存在则之间显示
     * 2.不存在的add
     * @param index
     */
    private void displayFragment( int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);
        position = index;
        switch (index){
            case FRAGMENT_HOME:
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance();
                    ft.add(R.id.fragment_Group, homeFragment, HomeFragment.class.getName());
                } else {
                    ft.show(homeFragment);
                }
                break;
            case FRAGMENT_FIND:
                if(findFragment==null){
                    findFragment=FindFragment.newInstance();
                    ft.add(R.id.fragment_Group,findFragment,FindFragment.class.getName());
                }else {
                    ft.show(findFragment);
                }
                break;
            case FRAGMENT_SCHEDULE:
                if(scheduleFragment==null){
                    scheduleFragment=ScheduleFragment.newInstance();
                    ft.add(R.id.fragment_Group,scheduleFragment,ScheduleFragment.class.getName());
                }else {
                    ft.show(scheduleFragment);
                }
                break;
            case FRAGMENT_MESSAGE:
                //保证IM登录成功
                    if( messageFragment==null  ){
                        messageFragment=MessageFragment.newInstance();
                        ft.add(R.id.fragment_Group,messageFragment,MessageFragment.class.getName());
                    }else {
                        ft.show(messageFragment);
                    }

                break;
            case FRAGMENT_ME:
                if(meFragment==null){
                    meFragment=MeFragment.newInstance();
                    meFragment.setDateForActivity(noticeCount);
                    ft.add(R.id.fragment_Group,meFragment,MeFragment.class.getName());
                }else {
                    meFragment.setDateForActivity(noticeCount);
                    ft.show(meFragment);
                }
                break;
        }
        ft.commitAllowingStateLoss();
    }



    /**
     * 判断Fragment是否存在
     * 1.存在则影藏该Fragment
     * @param ft
     */
    private void hideFragment(FragmentTransaction ft) {
        if(homeFragment!=null){
            ft.hide(homeFragment);
        }
        if(findFragment!=null){
            ft.hide(findFragment);
        }
        if(scheduleFragment!=null){
            ft.hide(scheduleFragment);
        }
        if(messageFragment!=null){
            ft.hide(messageFragment);
        }
        if(meFragment!=null){
            ft.hide(meFragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // recreate 时记录当前位置
        outState.putInt(POSITION, position);
        outState.putInt(SELECT_ITEM, radioGroup.getCheckedRadioButtonId());
    }
    //点击时间
    private  View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_message_count:
                    displayFragment(FRAGMENT_MESSAGE);
                    break;
                case R.id.main_hintText:
                    displayFragment(FRAGMENT_ME);
                    break;
            }
        }
    };
    /**
     * 初始化控件
     */
    private void initView() {
        flag=PreferencesUtils.getSharePreLong(this,Const.FLAG);
        endUserId = PreferencesUtils.getSharePreStr(this,"username");
        loginState=PreferencesUtils.getSharePreBoolean(this, Const.LOGIN_STATE);
        qBadgemessage=new QBadgeView(MainActivity.this);
        qBadgenotice=new QBadgeView(MainActivity.this);
        demo = new HttpDemo(this);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        tvHint = (Button) findViewById(R.id.main_hintText);
        btCount= (Button) findViewById(R.id.bt_message_count);
        btCount.setOnClickListener(listener);
        tvHint.setOnClickListener(listener);
        if(loginState){
            if(endUserId!=null && !TextUtils.isEmpty(endUserId)){
                setJPush(endUserId);
                getHttpMyNotices(endUserId);
            }
            if(!MyApplication.isLoginSkip){
                MootLogin();
            }

        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        displayFragment(FRAGMENT_HOME);
                        break;
                    case R.id.rb2:
                        displayFragment(FRAGMENT_FIND);
                        break;
                    case R.id.rb3:
                        if(!loginState){
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            return;
                        }
                        displayFragment(FRAGMENT_SCHEDULE);
                        NewMajorFragment.isScrollViewToTop = true;//此时设置其滑动置顶
                        break;
                    case R.id.rb4:
                        if(!loginState){
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            return;
                        }
                        displayFragment(FRAGMENT_MESSAGE);
                        break;
                    case R.id.rb5:
                        displayFragment(FRAGMENT_ME);
                        break;
                }

            }
        });


    }

    /**
     *
     * 模拟登录
     * 1.判断登录方式
     * 2.若果是账号登录：则需要用户名和密码
     * 3.如果是学号登录：则需要院校id 、学号、密码
     */
    private void MootLogin() {
        String account=PreferencesUtils.getSharePreStr(this,Const.LOGIN_NAME);
        String password=PreferencesUtils.getSharePreStr(this,"pwd");
        HashMap<String,String>map=new HashMap<>();
        if(flag==3){
            Login();
            return;
        }
        if(flag==2){
            //学号登录
            Long school_id=PreferencesUtils.getSharePreLong(this,Const.SCHOOL_ID);
            map.put("collegeId",String.valueOf(school_id));
        }
        map.put("userName",account);
        map.put("password",password);
        map.put("loginSystem","1");
        HttpClient.post(this, MyConfig.getURl("passport/doLogin"), map, new CallBack<UserInfo>() {
            @Override
            public void onSuccess(UserInfo result) {
                if(result==null){
                    return;
                }
                if(result.getState()==200){
                    PreferencesUtils.putSharePre(getApplicationContext(),Const.TOKEN,result.getData().getUser().getToken());
                }else {
                    ToastUtil.showShortToast(getApplicationContext(),"账号或密码错误,即将跳转登录界面");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }

        });
    }

    /**
     * 1.获取I学令牌的Token
     * 2.获取5学习的用户信息
     */
    private void Login() {
        HashMap<String,String>map=new HashMap<>();
        map.put("loginSystem","1");
        map.put("ip", PhoneInfoUtils.getIPAddress(MyApplication.getContext()));
        map.put("token",PreferencesUtils.getSharePreStr(MyApplication.getContext(), Const.TOKEN));
        HttpClient.post(this, Api.DO_LOGIN_BY_TOKEN ,map, new CallBack<UserInfo>() {
            @Override
            public void onSuccess(UserInfo result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
                    PreferencesUtils.putSharePre(getApplicationContext(),Const.TOKEN,result.getData().getUser().getToken());
                }
            }

        });
    }

    /**
     * 登录IM
     */
    private void loginIm() {
        //未登录则不登陆IM服务器
        if(!loginState){
            return;
        }
        //获取userid
        jid="5f_"+endUserId;
        MDPwd=PreferencesUtils.getSharePreStr(MainActivity.this, Const.PASSWORD);
        checkConnectionListener=new CheckConnectionListener(MainActivity.this);
        myPacketListener=new MyPacketListener();
        myChatManagerListener=new MyChatManagerListener();
        try {
            ds = new DatagramSocket();
            singleThreadExecutor.execute(loginRun);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录IM开启线程
     */
   private Runnable loginRun=new Runnable() {
        @Override
        public void run() {
            mXMPPConnection = XmppConnectionManager.getInstance().init();
            loginXMPP();
        }
    };

    /**
     * 登录XMPP
     */
    public void loginXMPP() {
        try {
            //IM服务器密码加密
            Logger.d("用户名："+jid+";加密密码："+MDPwd);
            mXMPPConnection.connect();
            mXMPPConnection.login(jid, MDPwd,"mobile");
            //连接状态监听的监听器
            mXMPPConnection.addConnectionListener(checkConnectionListener);
            //群聊的监听器
            mXMPPConnection.addPacketListener(myPacketListener,null);
            //添加私聊的监听器
            mXMPPConnection.getChatManager().addChatListener(myChatManagerListener);
            /**
             * 如果IM连接成功
             */
            if(mXMPPConnection.isAuthenticated()){
                MyApplication.xmppConnection=mXMPPConnection;
                Logger.d("全局赋值："+MyApplication.xmppConnection +";是否认证："+MyApplication.xmppConnection.isAuthenticated());
                //登录成功
                Logger.d("IM登录服务器成功");
                try {
                    initData();
                }catch (Exception e){
                    e.getLocalizedMessage();
                    handler.sendEmptyMessage(1);
                }
                handler.sendEmptyMessage(0);
            }else{
                //如果登录失败，自动销毁Service
                Logger.d("IM登录服务器失败");
                handler.sendEmptyMessage(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(1);

        }

    }


    Runnable joinGroup=new Runnable() {
        @Override
        public void run() {
            // 获取所有的群id加入群聊天室
            if(groupsList.size()>0) {
                MyApplication.list=null;
                MyApplication.list= XmppUtil.getMultiChatList(groupsList,jid);
            }
        }
    };
    /**
     * 获取群聊的信息
     */
    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("endUserId", endUserId);
        map.put("userType", "0");
        HttpClient.postStr(this, Api.GET_GROUP_LIST, map, new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (result == null) {
                    return;
                }
                Gson gson = new Gson();
                Test a = gson.fromJson(result, Test.class);
                DataBean dataBean = a.getData();
                groupsList.clear();
                Logger.d("群列表的数据源" + dataBean.getTigaseGroups().toString());
                groupsList.addAll(dataBean.getTigaseGroups());
                //赋值给全局变量
                MyApplication.groupsList = groupsList;
                if (mXMPPConnection != null && mXMPPConnection.isAuthenticated()) {
//                    new Thread(joinGroup).start();
                    singleThreadExecutor.execute(joinGroup);
                }

            }
        });
    }


    /**
     * IM服務器状态被挤下线
     * @param emptyEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EmptyEvent emptyEvent){
        if(MyApplication.xmppConnection!=null){
            if(!MyApplication.xmppConnection.isAuthenticated()){
                if(emptyEvent.isLogin){
                    DialogUtils.showNormalDialog(MainActivity.this);
                }
            }
        }

    }

    /**
     * 版本升级
     * @param downApkEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DownApkEvent downApkEvent){
        if(!TextUtils.isEmpty(downApkEvent.getApkUrl())){
            updateManager.showNoticeDialog(downApkEvent.getApkUrl().trim());
        }else {
            updateManager.checkHttpVersion(this);//检查跟新
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.getNetworkSatte(MainActivity.this);
    }



    /**
     * 展示会话列表的消息数量
     * @param noticeEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NoticeEvent noticeEvent){
        Logger.d("MainActivity:getNoticeCount"+noticeEvent.getMessageCount());
        showMessage(btCount,noticeEvent.getMessageCount());
    }



    /**
     * 极光推送总方法
     */
    private void setJPush(String endUserId) {
        //极光推送注册，
        if (isJPushFirst) {//确保每次进来只注册一次
            MyJPushUitl.resumePush(this);
            MyJPushUitl.setAlias(this, endUserId);
            isJPushFirst = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销极光推送
        MyJPushUitl.stopPush(this);
        MyJPushUitl.setClearNotification(this);
        EventBus.getDefault().unregister(this);
        isJPushFirst = true;
        //断开服务器的链接
        if(MyApplication.xmppConnection !=null){
            MyApplication.xmppConnection.disconnect();
            MyApplication.xmppConnection=null;
            MyApplication.groupsList=null;
            MyApplication.list=null;
        }

    }


    /**
     * 获取右下角 用户未读消息数
     * @param endUserId
     */
    private void getHttpMyNotices(String endUserId) {
        if (TextUtils.isEmpty(endUserId)) {
            return;
        }
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("my/myNoticeSum");
        pairsList.add(new Pairs("endUserId", endUserId));
        demo.doHttpGet(url, pairsList, 3);
    }


    //首页轮播下面三个快捷按钮 传递过来的操作(专业 微专业 公开课)
    @Override
    public void handOver(int flag) {
//        if (flag == 0) {//专业
//            radioGroup.check(R.id.rb3);
//            displayFragment(FRAGMENT_SCHEDULE);
//            scheduleFragment.setFlagForActivity(flag);
//        } else if (flag == 1 || flag == 2) {// 1微专业、 2公开课 跳转至发现 搜索
//            radioGroup.check(R.id.rb2);
//            displayFragment(FRAGMENT_FIND);
//            findFragment.setFlagForActivity(flag);
//        }else
           if(flag==4){//我的订单里面 微专业item 去学习 跳转至课程表李微专业
            radioGroup.check(R.id.rb3);
            displayFragment(FRAGMENT_SCHEDULE);
        }
    }

    /**
     * 0切换课程表的界面
     * 1、2切换发现界面公共课和微专业
     * @param switchTypeEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SwitchTypeEvent switchTypeEvent){
        switch (switchTypeEvent.getType()){
            case 0:
                radioGroup.check(R.id.rb3);
                displayFragment(FRAGMENT_SCHEDULE);
                break;
            case 1:
            case 2:
                radioGroup.check(R.id.rb2);
                displayFragment(FRAGMENT_FIND);
                break;
        }


    }





    @Override
    public void setMsg(String msg, int requestCode) {//检查跟新
        Logger.d("setMsg: 未读消息数 " + msg);
        try {
            if (requestCode == 3) {//用户未读消息数
                parseMyNotices(msg);
            } else {
                Log.i("UpDate", "parserUpdate: " + msg);
                JSONObject object = new JSONObject(msg);
                JSONObject data = object.getJSONObject("data");
                String apkUrl = data.getString("url");
                String version = data.getString("version");
                updateManager.checkIsUpdate(Integer.parseInt(version), apkUrl, 2);//检查是否需要更新
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析未读消息接口
     *
     * @param msg
     */
    private void parseMyNotices(String msg) throws Exception {
        if(loginState){
            JSONObject object = new JSONObject(msg);
            JSONObject data = object.getJSONObject("data");
            noticeCount = data.getInt("count");
            //显示消息数量
            showNotice(tvHint,noticeCount);
            if (meFragment != null ) {
                Log.i("mememe", "activity设置: ");//
                meFragment.setDateForActivity( noticeCount);
            }
        }

    }

    /**
     * 显示会话列表消息
     * @param view
     * @param noticeCount
     */
    private void  showMessage(View view,int noticeCount){
        qBadgemessage.bindTarget(view);
        qBadgemessage.setBadgeNumber(noticeCount);
        qBadgemessage.setBadgeNumberSize(12,true);
        qBadgemessage.setBadgeNumberColor(Color.WHITE);
        qBadgemessage.setBadgeBackgroundColor(Color.RED);
        qBadgemessage.setBadgeGravity( Gravity.END | Gravity.TOP );

    }

    /**
     * 显示极光推送的消息
     * @param view
     * @param noticeCount
     */
    private void  showNotice(View view,int noticeCount){
        qBadgenotice.bindTarget(view);
        qBadgenotice.setBadgeNumber(noticeCount);
        qBadgenotice.setBadgeNumberSize(12,true);
        qBadgenotice.setBadgeNumberColor(Color.WHITE);
        qBadgenotice.setBadgeBackgroundColor(Color.RED);
        qBadgenotice.setBadgeGravity( Gravity.END | Gravity.TOP );
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case 1:
                Bundle b = data.getExtras();  //data为B中回传的Intent
                String a = b.getString("indexType");//str即为回传的值"Hello, this is B speaking"
                Log.i("microaaaa", "主页面onActivityResult: " + a);
                if ("2".equals(a)) {
                    handOver(4);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) >= 2000) {
                ToastUtil.showShortToast(MainActivity.this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                if(MyApplication.xmppConnection !=null){
                    MyApplication.xmppConnection.disconnect();
                    MyApplication.xmppConnection=null;
                    MyApplication.groupsList=null;
                    MyApplication.list=null;
                }
                MyApplication.isLoginSkip=false;
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


}
