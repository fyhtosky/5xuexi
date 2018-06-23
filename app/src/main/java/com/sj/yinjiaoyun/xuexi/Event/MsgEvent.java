package com.sj.yinjiaoyun.xuexi.Event;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by wanzhiying on 2017/3/24.
 */
public class MsgEvent {
    private String to;
    private String from;
    private String msgBody;
    private Message.Type type;

    public MsgEvent(String to, String from, String msgBody, Message.Type type) {
        this.to = to;
        this.from = from;
        this.msgBody = msgBody;
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public Message.Type getType() {
        return type;
    }
}
