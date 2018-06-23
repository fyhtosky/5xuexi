package com.sj.yinjiaoyun.xuexi.xmppmanager;

import android.content.Intent;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.db.ChatMsgDao;
import com.sj.yinjiaoyun.xuexi.db.SessionDao;
import com.sj.yinjiaoyun.xuexi.entry.Msg;
import com.sj.yinjiaoyun.xuexi.service.MsfService;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wanzhiying on 2017/3/13.
 */
public class MessListener implements MessageListener {
    private MsfService context;
    private ChatMsgDao msgDao;
    private SessionDao sessionDao;
    private SimpleDateFormat sd;

    public MessListener(MsfService context) {
        this.context = context;
        sessionDao=new SessionDao(context);
        msgDao=new ChatMsgDao(context);
        sd=new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        try {

            String msgBody=message.getBody();
            if(TextUtils.isEmpty(msgBody)){
                return;
            }
            String to=message.getTo().split("@")[0];//接收者
            String from=message.getFrom().split("@")[0];//发送者
//            String type=message.getBody(Const.TYPE);
            Logger.d("message:"+msgBody+"  接收者TO:"+to+"  发送者FROM:"+from);
            //文本类型
//            if(Const.MSG_TYPE_TEXT.equals(type)){
            Msg msg=new Msg();
            msg.setToUser(to);//接收者
            msg.setFromUser(from);//发送者
            msg.setIsComing(0);
            msg.setContent(msgBody);
            msg.setDate(sd.format(new Date()));
            msg.setIsReaded("1");//暂且默认为已读
            msg.setType(Const.MSG_TYPE_TEXT);
//            msgDao.insert(msg);
            //发送广播，通知聊天界面更新
            Intent intent=new Intent(Const.ACTION_UPDATE_CHEAT_LOG);
            intent.putExtra("MSG",msg);
            context.sendBroadcast(intent);
            Logger.d("将消息记录存入数据中");

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
