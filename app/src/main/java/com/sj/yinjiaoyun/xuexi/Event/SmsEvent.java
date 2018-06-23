package com.sj.yinjiaoyun.xuexi.Event;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/5/22.
 *监听短信获取验证码
 */
public class SmsEvent {
    private String SmsContent;

    public SmsEvent(String smsContent) {
        SmsContent = smsContent;
    }

    public String getSmsContent() {
        return SmsContent;
    }
}
