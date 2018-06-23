package com.sj.yinjiaoyun.xuexi.bean;

import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Created by wanzhiying on 2017/3/29.
 */
public class MultiChatBean {
    private MultiUserChat multiUserChat;
    private String groupId;

    public MultiChatBean(MultiUserChat multiUserChat, String groupId) {
        this.multiUserChat = multiUserChat;
        this.groupId = groupId;
    }

    public MultiUserChat getMultiUserChat() {
        return multiUserChat;
    }

    public void setMultiUserChat(MultiUserChat multiUserChat) {
        this.multiUserChat = multiUserChat;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
