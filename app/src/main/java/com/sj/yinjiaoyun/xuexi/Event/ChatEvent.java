package com.sj.yinjiaoyun.xuexi.Event;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/4/24.
 */
public class ChatEvent {
    private  org.jivesoftware.smack.packet.Message message;

    public ChatEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
