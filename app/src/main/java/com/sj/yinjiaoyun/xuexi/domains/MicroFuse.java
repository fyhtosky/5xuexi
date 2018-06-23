package com.sj.yinjiaoyun.xuexi.domains;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/13.
 * 公开课  传递公共类，支付流程传递会为此类 ，课程表公开课也会转化为此类，订单流程也会转化为此类此类作为一个通用类公共传递类
 */
public class MicroFuse implements Parcelable {

    String TAG="microfuse";
    int indexType;     //课程类型 0：专业 1：公开课 2：微专业
    Long microId;       //微专业id
    String collegeName;//院校名称
    String courseLogo;
    String courseName;//课程名称
    int number;   //学习人数
    Byte isFree;  //0：免费 1不免费
    String price; //价格
    Byte tutionWay;//开课方式
    String collegeId;//院校id



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TAG);
        dest.writeInt(this.indexType);
        dest.writeValue(this.microId);
        dest.writeString(this.collegeName);
        dest.writeString(this.courseLogo);
        dest.writeString(this.courseName);
        dest.writeInt(this.number);
        dest.writeValue(this.isFree);
        dest.writeString(this.price);
        dest.writeValue(this.tutionWay);
        dest.writeString(this.collegeId);
    }

    public MicroFuse() {
    }

    protected MicroFuse(Parcel in) {
        this.TAG = in.readString();
        this.indexType = in.readInt();
        this.microId = (Long) in.readValue(Long.class.getClassLoader());
        this.collegeName = in.readString();
        this.courseLogo = in.readString();
        this.courseName = in.readString();
        this.number = in.readInt();
        this.isFree = (Byte) in.readValue(Byte.class.getClassLoader());
        this.price = in.readString();
        this.tutionWay = (Byte) in.readValue(Byte.class.getClassLoader());
        this.collegeId = in.readString();
    }

    public static final Parcelable.Creator<MicroFuse> CREATOR = new Parcelable.Creator<MicroFuse>() {
        @Override
        public MicroFuse createFromParcel(Parcel source) {
            return new MicroFuse(source);
        }

        @Override
        public MicroFuse[] newArray(int size) {
            return new MicroFuse[size];
        }
    };

    public Byte getTutionWay() {
        return tutionWay;
    }

    public void setTutionWay(Byte tutionWay) {
        this.tutionWay = tutionWay;
    }



    public int getIndexType() {
        return indexType;
    }

    public void setIndexType(int indexType) {
        this.indexType = indexType;
    }

    public Long getMicroId() {
        return microId;
    }

    public void setMicroId(Long microId) {
        this.microId = microId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCourseLogo() {
        return courseLogo;
    }

    public void setCourseLogo(String courseLogo) {
        this.courseLogo = courseLogo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Byte getIsFree() {
        return isFree;
    }

    public void setIsFree(Byte isFree) {
        this.isFree = isFree;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }


    @Override
    public String toString() {
        return "MicroFuse{" +
                "TAG='" + TAG + '\'' +
                ", indexType=" + indexType +
                ", microId=" + microId +
                ", collegeName='" + collegeName + '\'' +
                ", courseLogo='" + courseLogo + '\'' +
                ", courseName='" + courseName + '\'' +
                ", number=" + number +
                ", isFree=" + isFree +
                ", price='" + price + '\'' +
                ", tutionWay=" + tutionWay +
                ", collegeId='" + collegeId + '\'' +
                '}';
    }
}
