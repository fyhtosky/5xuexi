package com.sj.yinjiaoyun.xuexi.entry;

/**
 * Created by wanzhiying on 2017/3/14.
 * 聊天成员 封装类
 */

public class GroupUserInfo {
    String img;
    String name;
    String jid;

    public GroupUserInfo() {
    }

    public GroupUserInfo(String img, String name, String jid) {
        this.img = img;
        this.name = name;
        this.jid = jid;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }



    @Override
    public String toString() {
        return "GroupUserInfo{" +
                "img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", jid='" + jid + '\'' +
                '}';
    }
}
