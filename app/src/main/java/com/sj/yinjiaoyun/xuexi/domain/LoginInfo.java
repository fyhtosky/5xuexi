package com.sj.yinjiaoyun.xuexi.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/23.
 * 对接口处返回用户信息删选，封装需要的用户登录信息（仅方便数据库使用）
 */
public class LoginInfo implements Parcelable{
    String state;//是否登录状态  1表示登录 2表示未登录
    String flag;//表示通过哪种方式登录  1表示普通方式  2表示学号登录
    String endUserId;//用户id
    String param;//用户名或手机号
    String realName;//用户真名
    String pwd;//用户密码
    String image;//用户头像

    public LoginInfo(String state, String flag, String endUserId, String param, String pwd, String image,String realName) {
        this.state = state;
        this.flag = flag;
        this.endUserId = endUserId;
        this.param = param;
        this.pwd = pwd;
        this.image = image;
        this.realName=realName;
    }


    protected LoginInfo(Parcel in) {
        state = in.readString();
        flag = in.readString();
        endUserId = in.readString();
        param = in.readString();
        pwd = in.readString();
        image = in.readString();
        realName=in.readString();
    }

    public static final Creator<LoginInfo> CREATOR = new Creator<LoginInfo>() {
        @Override
        public LoginInfo createFromParcel(Parcel in) {
            return new LoginInfo(in);
        }

        @Override
        public LoginInfo[] newArray(int size) {
            return new LoginInfo[size];
        }
    };

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(state);
        dest.writeString(flag);
        dest.writeString(endUserId);
        dest.writeString(param);
        dest.writeString(pwd);
        dest.writeString(image);
        dest.writeString(realName);
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "state='" + state + '\'' +
                ", flag='" + flag + '\'' +
                ", endUserId='" + endUserId + '\'' +
                ", param='" + param + '\'' +
                ", pwd='" + pwd + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
