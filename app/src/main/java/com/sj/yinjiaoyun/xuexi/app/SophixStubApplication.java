package com.sj.yinjiaoyun.xuexi.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Keep;
import android.support.multidex.MultiDex;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * Created by Administrator on 2018/3/8.
 */

public class SophixStubApplication  extends SophixApplication {
    public static  final String AppId="24809973-1";
    public static final String AppSecret="c0552312382842b88cae34c29936a17b";
    private  static  final String RSA="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDPh25wxly61qinFM7okLgQGITm+D5tVSgjv0kvW+PioyQqx7ntwjebeEwRACa3/B7wFkTuUuKezEG6NPB1rf4KAP2AiHBBoPyS8lhsKbxFpEJ2fphILczhvBOO8YHEtD8O4rCcWslH1Uqv5hYdm0K2kEmxBgkKkInJXe/9cNyQaeQ9CaUMppgwF5LR5u2ZyonOrS9x3GBVWiMs/GorFug8CgajHzmExFC9wtw865yRqeXwEzcw9N6PwPviiBTIGN6lseWIRJl/WNIApBNX1IdJhR9+7OqZ7kg/HKptiaKMcAc/s5zybRoDPB5NP/1StQ8RjHu7lCqLw6IWWIiglM+jAgMBAAECggEBALBBeQYlIuq4l6LYJGcXSoy5Fl9IZVTcZ2v88X5cj+6T2orO+NW7BxlvEUO7GidW5TvJ3h9/m+N83TH19mgsLz912dgJiTXkMb/oPjsXM0T+nkgfetyKsaDi6MWVzj4vTAMrew5AeQUeXQGJ7CyXChEd+Mh1tMYZPj0dIE/jRCENltt1CfbOJTmi91zoK63s5mBBUuVNMcKLO15iCUOJJ35Fmp/YjVtetcmuNXNefS+/bapYvqqoZ42Pdc3FufxDySu4PvignDQPahTtMEW6MLKmpyZwa1bdIi5fHC9TmATVCee5aavpIfx9skWu4dQZ/fpDJz+PNxn3vv3ORFm6p/ECgYEA7Dnxiibw8wXBVa1P51qhaOqYMcQYyKFNsqD2XLD+LSfhqsrXrKyZh8eyMYiezmZZDiXm+UaWFQ0Rptaezv1eWF+Xb9oiqnep6mO7sHM9KXvItnfLbFuVdx52U8tvgqxfYJqmhRV3n1avENreZc0vFCUN0iQdH3EU3Xp9OwMDILkCgYEA4OaJqxb595qMityrL1YOyX2D2d7djPOQhsgXxUnsAw4sbfpKUDshG392tv7YZAH74iAV1rhGUWx3O7VEiX0Vs/qkJkPjAK77wzLs7x7X8C90fzplcskTwQl2p7wX8o1VRfW/nLFzYU5h47O7RnpX5lO9FNfnPCU5lCqwvtOZ7TsCgYAutsatK0Y6yMiSyNMkTSfeN4YhUUYVZtCWon4VlmqtQ1W9EJ/V5cv2rebvJLVC27qAsb4bTXogb5UuCQMeq7wcfcrUs65nR4Kyd0aGfkN6EKCAdPAlXXcRN1Tu/SvwC2ZRFEQZzUT6nYnBK4cPWqQUt78j70BTF2mo4rAhG1djAQKBgGQqqaQ27ow2HGR4lhHWLW3WZlQyK9DsZjFxiwzGuMQ/mtA8yr5jJ4K8yZ4FcVFRhOMY0UE5/6+iNJS2TllCCdmIR3X3hHI4fU9aLibuQNTr/eHwm2B9aDvwwBVC9BOGmTU4l+ftLOto4rZsxXtFYELohu1yn72tArEYNnI3gnYjAoGBAJ6bMLhp8SUZVWZu6Vs7bffAICzucwd/9xM8pQfJLUPR668dV+09knzHGGFpBD9I4rkgV+eXUPDE7e7wdXYCAG03bactWvjfAk0wW3dvXj3bef65sUA6mi4uvYE/DbVQK80NZlz4JjlBxkqv+fSAwOLnm+S5Z2cFQ3KwUlF5+6An";
    private final String TAG = "SophixStubApplication";
    private Handler handler=new Handler(Looper.getMainLooper());
    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(MyApplication.class)
    static class RealApplicationStub {}

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        //初始化热跟新
        initSophix();
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
                              handler.postDelayed(new Runnable() {
                                  @Override
                                  public void run() {
                                      Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getPackageName());
                                     PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    manager.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, restartIntent);//200ms后重启应用
                                  }
                              },5000);
                            SophixManager.getInstance().killProcessSafely();
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
    }

}
