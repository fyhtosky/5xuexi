package com.sj.yinjiaoyun.xuexi.xmppmanager;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.GroEvent;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * Created by wanzhiying on 2017/3/29.
 * 群聊消息的监听器
 */
public class MyPacketListener implements PacketListener {
    @Override
    public void processPacket(Packet packet) {
        Message message = (Message) packet;
        Logger.xml(message.toXML());
        String msgBody = message.getBody();
        if (TextUtils.isEmpty(msgBody)) {
            return;
        }
        String to = message.getTo().split("@")[0];//接收者
//        String from = message.getFrom().split("@")[0];//发送者
        Logger.d("PacketListener message:" + msgBody + "  接收者TO:" + message.getTo() + "  发送者FROM:" + message.getFrom()+"TYPE:"+message.getType());
        //发送消息通知群聊的信息实时更新
        GroEvent groEvent=new GroEvent(to,message.getFrom(),msgBody, message.getType());
        EventBus.getDefault().post(groEvent);
    }
}
