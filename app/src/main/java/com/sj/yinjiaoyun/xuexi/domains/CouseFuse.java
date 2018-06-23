package com.sj.yinjiaoyun.xuexi.domains;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/7.
 * 公开课  传递公共类，支付流程传递会为此类 ，课程表公开课也会转化为此类，订单流程也会转化为此类此类作为一个通用类公共传递类
 */
public class CouseFuse implements Parcelable{

    String TAG="";
    int indexType;  //课程类型 0：专业 1：公开课 2：微专业
    String collegeName;//院校名称
    String courseLogo;
    String courseName;//课程名称
    int number;//学习人数
    Long courseId; //课程id
    Long opencourseId; //公开课id
    Byte isFree;  //0：免费 1不免费
    Byte courseType;//公开课类型
    String price;//价格

    public CouseFuse() {
    }

    protected CouseFuse(Parcel in) {
        try {
            indexType = in.readInt();
            collegeName = in.readString();
            courseLogo = in.readString();
            courseName = in.readString();
            number = in.readInt();
            courseId = in.readLong();
            opencourseId = in.readLong();
            isFree = in.readByte();
            courseType = in.readByte();
            price = in.readString();
        }catch (Exception e){
            Log.i(TAG, "CouseFuse: ");
        }
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeInt(indexType);
            dest.writeString(collegeName);
            dest.writeString(courseLogo);
            dest.writeString(courseName);
            dest.writeInt(number);
            dest.writeLong(courseId);
            dest.writeLong(opencourseId);
            dest.writeByte(isFree);
            dest.writeByte(courseType);
            dest.writeString(price);
        }catch (Exception w){
            Log.i(TAG, "writeToParcel: ");
        }
    }

    public static final Creator<CouseFuse> CREATOR = new Creator<CouseFuse>() {
        @Override
        public CouseFuse createFromParcel(Parcel in) {
            return new CouseFuse(in);
        }

        @Override
        public CouseFuse[] newArray(int size) {
            return new CouseFuse[size];
        }
    };

    public int getIndexType() {
        return indexType;
    }

    public void setIndexType(int indexType) {
        this.indexType = indexType;
    }

    public Byte getCourseType() {
        return courseType;
    }

    public void setCourseType(Byte courseType) {
        this.courseType = courseType;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getOpencourseId() {
        return opencourseId;
    }

    public void setOpencourseId(Long opencourseId) {
        this.opencourseId = opencourseId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Byte getIsFree() {
        return isFree;
    }

    public void setIsFree(Byte isFree) {
        this.isFree = isFree;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "CouseFuse{" +
                "indexType=" + indexType +
                ", collegeName='" + collegeName + '\'' +
                ", courseLogo='" + courseLogo + '\'' +
                ", price='" + price + '\'' +
                ", courseName='" + courseName + '\'' +
                ", number=" + number +
                ", courseId=" + courseId +
                ", opencourseId=" + opencourseId +
                ", isFree=" + isFree +
                ", courseType=" + courseType +
                '}';
    }
}
