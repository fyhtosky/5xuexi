package com.sj.yinjiaoyun.xuexi.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/26.
 *  根据招生专业ID+招生计划ID + 用户ID获取课程信息（教学计划信息）
 * 某个专业所有课程课程信息封装类
 * 组员
 */
public class CourseVO implements Parcelable{
    Integer	 totalScore;        //是	课程总成绩
    String	coursePercent;     //是	课程完成百分比
    String  courseName;        //	是	课程名称
    Long	courseId;          //是	课程ID
    Long	courseScheduleId;  //是	课表ID
    String	courseLogoUrl;     //是	课程LOGO
    Byte courseCredit;         //		是	课程学分
    Byte courseAttribute;      //	是	0:公共基础课,1:专业选修课,2:专业基础课,3:公共选修课,

    protected CourseVO(Parcel in) {
        totalScore = in.readInt();
        coursePercent = in.readString();
        courseName = in.readString();
        courseId=in.readLong();
        courseScheduleId=in.readLong();
        courseLogoUrl = in.readString();
        courseCredit=in.readByte();
        courseAttribute=in.readByte();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalScore);
        dest.writeString(coursePercent);
        dest.writeString(courseName);
        dest.writeLong(courseId);
        dest.writeLong(courseScheduleId);
        dest.writeString(courseLogoUrl);
        dest.writeByte(courseCredit);
        try{
            dest.writeByte(courseAttribute);
        }catch(Exception e){
            Byte a=-1;
            dest.writeByte(a);
            Log.i("image", "writeToParcel: "+"序列化出错，培训考证时为空,填充为-1");
        }
    }

    public static final Creator<CourseVO> CREATOR = new Creator<CourseVO>() {
        @Override
        public CourseVO createFromParcel(Parcel in) {
            return new CourseVO(in);
        }

        @Override
        public CourseVO[] newArray(int size) {
            return new CourseVO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }



    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public String getCoursePercent() {
        return coursePercent;
    }

    public void setCoursePercent(String coursePercent) {
        this.coursePercent = coursePercent;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(Long courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public String getCourseLogoUrl() {
        return courseLogoUrl;
    }

    public void setCourseLogoUrl(String courseLogoUrl) {
        this.courseLogoUrl = courseLogoUrl;
    }

    public Byte getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(Byte courseCredit) {
        this.courseCredit = courseCredit;
    }

    public Byte getCourseAttribute() {
        return courseAttribute;
    }

    public void setCourseAttribute(Byte courseAttribute) {
        this.courseAttribute = courseAttribute;
    }



    @Override
    public String toString() {
        return "CourseVO{" +
                "totalScore=" + totalScore +
                ", coursePercent='" + coursePercent + '\'' +
                ", courseName='" + courseName + '\'' +
                ", 课程ID courseId=" + courseId +
                ", 课表ID courseScheduleId=" + courseScheduleId +
                ", courseLogoUrl='" + courseLogoUrl + '\'' +
                ", courseCredit=" + courseCredit +
                ", courseAttribute=" + courseAttribute +
                '}';
    }


}
