package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/17.
 * 课程表 微专业  顶部tab对应的列表数据解析
 */
public class CourseScheduleVO {

    String courseCode;
    Long id;//课程表ID
    String courseName;
    String courseLogoUrl;
    String coursePercent;
    Long totalLearnTime;
    String teacherName;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseLogoUrl() {
        return courseLogoUrl;
    }

    public void setCourseLogoUrl(String courseLogoUrl) {
        this.courseLogoUrl = courseLogoUrl;
    }

    public String getCoursePercent() {
        return coursePercent;
    }

    public void setCoursePercent(String coursePercent) {
        this.coursePercent = coursePercent;
    }

    public Long getTotalLearnTime() {
        return totalLearnTime;
    }

    public void setTotalLearnTime(Long totalLearnTime) {
        this.totalLearnTime = totalLearnTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }


    @Override
    public String toString() {
        return "CourseScheduleVO{" +
                "courseCode='" + courseCode + '\'' +
                ", id=" + id +
                ", courseName='" + courseName + '\'' +
                ", courseLogoUrl='" + courseLogoUrl + '\'' +
                ", coursePercent='" + coursePercent + '\'' +
                ", totalLearnTime=" + totalLearnTime +
                ", teacherName='" + teacherName + '\'' +
                '}';
    }
}
