//package com.sj.yinjiaoyun.xuexi.loginCommon;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.SystemClock;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.sj.yinjiaoyun.xuexi.app.MyApplication;
//import com.tencent.connect.UserInfo;
//import com.tencent.tauth.Tencent;
//
///**
// * 作者：VanHua on 2018/5/7 14:58
// * <p>
// * 邮箱：1373516909@qq.com
// */
//public class LoginManager {
//    private static boolean isServerSideLogin = false;
//    private static LoginManager loginManager = null;
//    private UserInfo mInfo;
//    private BaseUIListener loginListener;
//    public static Tencent mTencent;
//
//    public void setLoginListener(BaseUIListener loginListener) {
//        this.loginListener = loginListener;
//    }
//
//    public BaseUIListener getLoginListener() {
//        return loginListener;
//    }
//
//    public LoginManager() {
//    }
//
//    public static synchronized LoginManager getInstance() {
//        if (loginManager == null) {
//            synchronized (LoginManager.class) {
//                if (loginManager == null) {
//                    loginManager = new LoginManager();
//                }
//            }
//        }
//        return loginManager;
//    }
//
//    /**
//     * 初始化
//     * @param appId
//     * @return
//     */
//    public Tencent init(String appId) {
//        if (mTencent == null) {
//            mTencent = Tencent.createInstance(appId, MyApplication.getContext());
//        }
//        return mTencent;
//    }
//
//    /**
//     * 登录
//     * @param appId
//     * @param activity
//     */
//    public void login(String appId, Activity activity) {
//        init(appId);
//        if (mTencent != null && mTencent.isSessionValid()) {
//            if (!mTencent.isSessionValid()) {
//                if(loginListener!=null){
//                    mTencent.login(activity, "all", loginListener);
//                    isServerSideLogin = false;
//                    Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
//                }else {
//                    Log.d("SDKQQAgentPref", "BaseUIListener is  null");
//                }
//            } else {
//                if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
//                    mTencent.logout(activity);
//                    if(loginListener!=null){
//                        mTencent.login(activity, "all", loginListener);
//                        isServerSideLogin = false;
//                        Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
//                    }else {
//                        Log.d("SDKQQAgentPref", "BaseUIListener is  null");
//                    }
//                    return;
//                }
//                mTencent.logout(activity);
//            }
//            mInfo = new UserInfo(activity, mTencent.getQQToken());
//            mInfo.getUserInfo(loginListener);
//        }
//    }
//
//    /**
//     * 判断是否有SessionId和Token
//     * @param context
//     * @return
//     */
//    public static boolean ready(Context context) {
//        if (mTencent == null) {
//            return false;
//        }
//        boolean ready = mTencent.isSessionValid()
//                && mTencent.getQQToken().getOpenId() != null;
//        if (!ready) {
//            Toast.makeText(context, "login and get openId first, please!",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        return ready;
//    }
//
//    /**
//     * 获取用户登陆之后的回调信息
//     */
//    private  void getUserInfo() {
//        if (ready(MyApplication.getContext())) {
//            mInfo.getUserInfo(new BaseUIListener(MyApplication.getContext(), "get_simple_userinfo") {
//                @Override
//                protected void showResult(String onCancel, String s) {
//
//                }
//            });
//
//        }
//    }
//
//}