package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/18.
 * 消息提醒解 析页面
 */
public class ParserMessageDate {
    Notices notices;

    public ParserMessageDate(Notices notices) {
        this.notices = notices;
    }


    public Notices getNotices() {
        return notices;
    }

    public void setNotices(Notices notices) {
        this.notices = notices;
    }
}
