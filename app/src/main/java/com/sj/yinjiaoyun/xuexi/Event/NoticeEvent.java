package com.sj.yinjiaoyun.xuexi.Event;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/5/9.
 * 最近会话列表的会话的数量
 */
public class NoticeEvent {
    private int messageCount;

    public NoticeEvent(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getMessageCount() {
        return messageCount;
    }

}
