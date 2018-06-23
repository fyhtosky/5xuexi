package com.sj.yinjiaoyun.xuexi.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/26.
 * 根据招生专业ID + 招生计划ID + 用户ID获取课程信息（教学计划信息）
 * 某个专业信息封装类
 */
public class SoaProductVO implements Parcelable{

    String collegeName;     //	是	院校名称
    String majorName;       //	是	专业名称
    String	majorPercent;   //  是	专业进度
    String	productLogoUrl; //	是	专业图片
    String enrollSeason;    //  是     招生季
    Byte productType;       // 是	专业类型
    Byte productLearningLength;//	是	专业学制' (网校   成教   自考   选修)单位：年  ，（培训  考证）单位：周
    Long productDirectionId;   //    可能没有	学生选择方向ID
    String	productDirectionName;//可能没有	学生选择方向名称
    Map<String,List<CourseVO>> teachingPlanMap;       //	是	该学生  该专业的所有课程 Map<key:学年,value:课程集合>

    Map<String,Boolean>	stateMap;              //是	该学生的缴费情况Map<KEY:学年，value:KEY学年true已缴费/false:未缴费>


    protected SoaProductVO(Parcel in) {
        collegeName = in.readString();
        majorName = in.readString();
        majorPercent = in.readString();
        productLogoUrl = in.readString();
        enrollSeason=in.readString();
        productDirectionName = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(collegeName);
        dest.writeString(majorName);
        dest.writeString(majorPercent);
        dest.writeString(productLogoUrl);
        dest.writeString(enrollSeason);
        dest.writeString(productDirectionName);
    }
    public static final Creator<SoaProductVO> CREATOR = new Creator<SoaProductVO>() {
        @Override
        public SoaProductVO createFromParcel(Parcel in) {
            return new SoaProductVO(in);
        }

        @Override
        public SoaProductVO[] newArray(int size) {
            return new SoaProductVO[size];
        }
    };

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getMajorPercent() {
        return majorPercent;
    }

    public void setMajorPercent(String majorPercent) {
        this.majorPercent = majorPercent;
    }

    public String getProductLogoUrl() {
        return productLogoUrl;
    }

    public void setProductLogoUrl(String productLogoUrl) {
        this.productLogoUrl = productLogoUrl;
    }

    public Byte getProductType() {
        return productType;
    }

    public void setProductType(Byte productType) {
        this.productType = productType;
    }

    public Byte getProductLearningLength() {
        return productLearningLength;
    }

    public void setProductLearningLength(Byte productLearningLength) {
        this.productLearningLength = productLearningLength;
    }

    public Long getProductDirectionId() {
        return productDirectionId;
    }

    public void setProductDirectionId(Long productDirectionId) {
        this.productDirectionId = productDirectionId;
    }

    public String getProductDirectionName() {
        return productDirectionName;
    }

    public void setProductDirectionName(String productDirectionName) {
        this.productDirectionName = productDirectionName;
    }

    public Map<String, Boolean> getStateMap() {
        return stateMap;
    }

    public void setStateMap(Map<String, Boolean> stateMap) {
        this.stateMap = stateMap;
    }

    public Map<String, List<CourseVO>> getTeachingPlanMap() {
        return teachingPlanMap;
    }

    public void setTeachingPlanMap(Map<String, List<CourseVO>> teachingPlanMap) {
        this.teachingPlanMap = teachingPlanMap;
    }

    public String getEnrollSeason() {
        return enrollSeason;
    }

    public void setEnrollSeason(String enrollSeason) {
        this.enrollSeason = enrollSeason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return " SoaProductVO{" +
                "collegeName='" + collegeName + '\'' +
                ", majorName='" + majorName + '\'' +
                ", majorPercent='" + majorPercent + '\'' +
                ", productLogoUrl='" + productLogoUrl + '\'' +
                ", productType=" + productType +
                ", productLearningLength=" + productLearningLength +
                ", productDirectionId=" + productDirectionId +
                ", productDirectionName='" + productDirectionName + '\'' +
                ", teachingPlanMap=" + teachingPlanMap +
                ", stateMap=" + stateMap +
                '}';
    }
}
