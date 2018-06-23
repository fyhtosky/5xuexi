package com.sj.yinjiaoyun.xuexi.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/10/13.
 * 接口15   某个学校的详细信息
 */
public class College implements Parcelable{
    Long id;//学院id
    String organizationName;//学院名字
    String firstSpell;//学院名字的第一个字母
    String collegeLogo; //图标

    protected College(Parcel in) {
        id=in.readLong();
        organizationName = in.readString();
        firstSpell=in.readString();
        collegeLogo=in.readString();
    }

    public College() {
    }

    public static final Creator<College> CREATOR = new Creator<College>() {
        @Override
        public College createFromParcel(Parcel in) {
            return new College(in);
        }

        @Override
        public College[] newArray(int size) {
            return new College[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getFirstSpell() {
        return firstSpell;
    }

    public void setFirstSpell(String firstSpell) {
        this.firstSpell = firstSpell;
    }


    @Override
    public String toString() {
        return "College{" +
                "id=" + id +
                ", organizationName='" + organizationName + '\'' +
                ", firstSpell='" + firstSpell + '\'' +
                ", collegeLogo='" + collegeLogo + '\'' +
                '}';
    }

    public String getCollegeLogo() {
        return collegeLogo;
    }

    public void setCollegeLogo(String collegeLogo) {
        this.collegeLogo = collegeLogo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(organizationName);
        dest.writeString(firstSpell);
        dest.writeString(collegeLogo);
    }
}
