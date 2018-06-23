package com.sj.yinjiaoyun.xuexi.Event;

/**
 * 作者：VanHua on 2018/5/22 17:27
 * <p>
 * 邮箱：Van_Hua@qq.com
 */
public class DownApkEvent {
    private String apkUrl;

    public DownApkEvent(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getApkUrl() {
        return apkUrl;
    }
}
