package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/24.
 * 个人信息接口（根据用户ID获取用户个人信息）
 * 里层数据
 */
public class ParseDataUser {
    UserInfo user;
    boolean hasStudentNumber;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public boolean isHasStudentNumber() {
        return hasStudentNumber;
    }

    public void setHasStudentNumber(boolean hasStudentNumber) {
        this.hasStudentNumber = hasStudentNumber;
    }
}
