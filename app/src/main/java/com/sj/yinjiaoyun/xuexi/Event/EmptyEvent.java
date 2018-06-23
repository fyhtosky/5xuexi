package com.sj.yinjiaoyun.xuexi.Event;

/**
 * Created by wanzhiying on 2017/4/10.
 * 判断用户是否被踢下线
 */
public class EmptyEvent {
    public boolean isLogin;

    public EmptyEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
