package com.sj.yinjiaoyun.xuexi.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/5/23.
 */
@Entity
public class User {
    private String userId;
    private String msgName;
    private String msgLogo;
    public String getMsgLogo() {
        return this.msgLogo;
    }
    public void setMsgLogo(String msgLogo) {
        this.msgLogo = msgLogo;
    }
    public String getMsgName() {
        return this.msgName;
    }
    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Generated(hash = 1324985469)
    public User(String userId, String msgName, String msgLogo) {
        this.userId = userId;
        this.msgName = msgName;
        this.msgLogo = msgLogo;
    }
    @Generated(hash = 586692638)
    public User() {
    }
   


}
