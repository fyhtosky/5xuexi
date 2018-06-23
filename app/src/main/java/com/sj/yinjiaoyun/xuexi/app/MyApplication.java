package com.sj.yinjiaoyun.xuexi.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.bean.MultiChatBean;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroups;
import com.sj.yinjiaoyun.xuexi.bean.User;
import com.sj.yinjiaoyun.xuexi.greedao.gen.DaoMaster;
import com.sj.yinjiaoyun.xuexi.greedao.gen.DaoSession;
import com.sj.yinjiaoyun.xuexi.utils.DebugUtil;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.SocializeConstants;

import org.jivesoftware.smack.XMPPConnection;

import java.util.List;

/**
 * Created by wanzhiying on 2017/3/6.
 */
public class MyApplication extends MultiDexApplication {
    //表示是否是手动登录
    public static boolean isLoginSkip=false;
    private static Context context;
    public static XMPPConnection xmppConnection;
    public static List<MultiChatBean>list;
    public static List<TigaseGroups>groupsList;
    private DaoMaster.DevOpenHelper mHelper;
    private  SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static MyApplication Instances;
    public static  final String AppId="24809973-1";
    public static  final String AppSecret="c0552312382842b88cae34c29936a17b";
    public static  final String RSA="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDPh25wxly61qinFM7okLgQGITm+D5tVSgjv0kvW+PioyQqx7ntwjebeEwRACa3/B7wFkTuUuKezEG6NPB1rf4KAP2AiHBBoPyS8lhsKbxFpEJ2fphILczhvBOO8YHEtD8O4rCcWslH1Uqv5hYdm0K2kEmxBgkKkInJXe/9cNyQaeQ9CaUMppgwF5LR5u2ZyonOrS9x3GBVWiMs/GorFug8CgajHzmExFC9wtw865yRqeXwEzcw9N6PwPviiBTIGN6lseWIRJl/WNIApBNX1IdJhR9+7OqZ7kg/HKptiaKMcAc/s5zybRoDPB5NP/1StQ8RjHu7lCqLw6IWWIiglM+jAgMBAAECggEBALBBeQYlIuq4l6LYJGcXSoy5Fl9IZVTcZ2v88X5cj+6T2orO+NW7BxlvEUO7GidW5TvJ3h9/m+N83TH19mgsLz912dgJiTXkMb/oPjsXM0T+nkgfetyKsaDi6MWVzj4vTAMrew5AeQUeXQGJ7CyXChEd+Mh1tMYZPj0dIE/jRCENltt1CfbOJTmi91zoK63s5mBBUuVNMcKLO15iCUOJJ35Fmp/YjVtetcmuNXNefS+/bapYvqqoZ42Pdc3FufxDySu4PvignDQPahTtMEW6MLKmpyZwa1bdIi5fHC9TmATVCee5aavpIfx9skWu4dQZ/fpDJz+PNxn3vv3ORFm6p/ECgYEA7Dnxiibw8wXBVa1P51qhaOqYMcQYyKFNsqD2XLD+LSfhqsrXrKyZh8eyMYiezmZZDiXm+UaWFQ0Rptaezv1eWF+Xb9oiqnep6mO7sHM9KXvItnfLbFuVdx52U8tvgqxfYJqmhRV3n1avENreZc0vFCUN0iQdH3EU3Xp9OwMDILkCgYEA4OaJqxb595qMityrL1YOyX2D2d7djPOQhsgXxUnsAw4sbfpKUDshG392tv7YZAH74iAV1rhGUWx3O7VEiX0Vs/qkJkPjAK77wzLs7x7X8C90fzplcskTwQl2p7wX8o1VRfW/nLFzYU5h47O7RnpX5lO9FNfnPCU5lCqwvtOZ7TsCgYAutsatK0Y6yMiSyNMkTSfeN4YhUUYVZtCWon4VlmqtQ1W9EJ/V5cv2rebvJLVC27qAsb4bTXogb5UuCQMeq7wcfcrUs65nR4Kyd0aGfkN6EKCAdPAlXXcRN1Tu/SvwC2ZRFEQZzUT6nYnBK4cPWqQUt78j70BTF2mo4rAhG1djAQKBgGQqqaQ27ow2HGR4lhHWLW3WZlQyK9DsZjFxiwzGuMQ/mtA8yr5jJ4K8yZ4FcVFRhOMY0UE5/6+iNJS2TllCCdmIR3X3hHI4fU9aLibuQNTr/eHwm2B9aDvwwBVC9BOGmTU4l+ftLOto4rZsxXtFYELohu1yn72tArEYNnI3gnYjAoGBAJ6bMLhp8SUZVWZu6Vs7bffAICzucwd/9xM8pQfJLUPR668dV+09knzHGGFpBD9I4rkgV+eXUPDE7e7wdXYCAG03bactWvjfAk0wW3dvXj3bef65sUA6mi4uvYE/DbVQK80NZlz4JjlBxkqv+fSAwOLnm+S5Z2cFQ3KwUlF5+6An";
    static {
        //各个平台的配置，建议放在全局Application或者程序入口
        PlatformConfig.setQQZone("1105831237","wVbTC4rLgWD8plEV");
        PlatformConfig.setWeixin("wx4bd312eb2e623eb0","7eaa83b050011a17eb3cc3681a59608f");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Instances=this;
        //初始化热跟新
//        initSophix();
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
        //开启Debug模式，方便调错
        Config.DEBUG=true;
        //注册友盟及初始化
        UMShareAPI.get(this);
        //日志管理初始化
        initLogger(context);
        Logger.d("友盟的版本："+SocializeConstants.SDK_VERSION);
       //初始化数据库
        setDatabase();


    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
    /**
     *
     * 初始化日志库管理
     * @param context
     */
    private static void initLogger(@NonNull Context context) {
        // 在 debug 模式输出日志， release 模式自动移除
        if (DebugUtil.isInDebug(context)) {
            Logger.init("iiiiii").logLevel(LogLevel.FULL)//  显示全部日志，LogLevel.NONE不显示日志，默认是Full
                    .methodCount(5)//  方法栈打印的个数，默认是2
                    .methodOffset(0)//  设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
                    .hideThreadInfo(); //  隐藏线程信息
        } else {
            Logger.init("iiiiii").logLevel(LogLevel.NONE)//  显示全部日志，LogLevel.NONE不显示日志，默认是Full
                    .methodCount(5)//  方法栈打印的个数，默认是2
                    .methodOffset(0)//  设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
                    .hideThreadInfo(); //  隐藏线程信息
        }
    }

    /**
     * 设置Greendao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper=new DaoMaster.DevOpenHelper(context,"5xuexi_db",null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        //  删除所有的记录
        mDaoSession.deleteAll(User.class);
    }

    /**
     * 初始化Sophix
     */
    private void initSophix() {
        String appVersion ;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion="1.0.0";
        }
        // initialize最好放在attachBaseContext最前面，初始化直接在Application类里面，切勿封装到其他类
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData(AppId,AppSecret,RSA)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();

        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
    }



    public static MyApplication getInstances() {
        return Instances;
    }

    public static Context getContext(){
        return context;
    }

    public static XMPPConnection getXmppConnection() {
        return xmppConnection;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }
}
