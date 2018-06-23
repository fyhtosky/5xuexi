package com.sj.yinjiaoyun.xuexi.xmppmanager;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.MsgEvent;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;

/**
 * 私聊的消息监听器
 */
public class MyChatManagerListener implements ChatManagerListener {
    @Override
    public void chatCreated(Chat chat, boolean b) {
        chat.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
                Logger.xml(message.toXML());
                try {
                    String msgBody = message.getBody();
                    if (TextUtils.isEmpty(msgBody)) {
                        return;
                    }
                    String to = message.getTo().split("@")[0];//接收者
                    String from = message.getFrom().split("@")[0];//发送者
                    Logger.d("MessageFragment:私聊:" + msgBody + "  接收者TO:" + to + "  发送者FROM:" + from);
                    Logger.d("MessageFragment:私聊:" + msgBody + "  接收者TO:" + message.getTo() + "  发送者FROM:" + message.getFrom());
                    //发送消息通知聊天界面刷新聊天记录
                    EventBus.getDefault().post(new MsgEvent(to,from,msgBody,message.getType()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
