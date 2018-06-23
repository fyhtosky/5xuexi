package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/25.
 * 课程表 公开课列表数据 解析
 *        发现公开课  共用
 */
public class OpenCourseVO {

    String collegeName;//院校名称
    Long  collegeId;//院校id
    Long id;          //公开课id
    String courseName;//课程名称
    String courseLogo;//课程图片
    Long courseScheduleId;//
    Long courseId;      //课程id
    Byte openCourseType;//课程类型   0随到随学（免费）  1随到随学（付费） 2定期开课
    String courseDesc;  //新增 发现 公开课详情 简介里面数据解析
    Double price;       //新增  发现 公开课详情 简介里面数据解析
    int number;         //报名人数
    String goodRate;    //好评率
    int isAudit;   //是否需要審核表示
    String openCoursePercent;//公开课学习进度

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Long getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Long collegeId) {
        this.collegeId = collegeId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseLogo() {
        return courseLogo;
    }

    public void setCourseLogo(String courseLogo) {
        this.courseLogo = courseLogo;
    }

    public int getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(int isAudit) {
        this.isAudit = isAudit;
    }

    public Long getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(Long courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Byte getOpenCourseType() {
        return openCourseType;
    }

    public void setOpenCourseType(Byte openCourseType) {
        this.openCourseType = openCourseType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getGoodRate() {
        return goodRate;
    }

    public void setGoodRate(String goodRate) {
        this.goodRate = goodRate;
    }

    public String getOpenCoursePercent() {
        return openCoursePercent;
    }

    public void setOpenCoursePercent(String openCoursePercent) {
        this.openCoursePercent = openCoursePercent;
    }

    @Override
    public String toString() {
        return "公开课OpenCouses{" +
                "collegeName='" + collegeName + '\'' +
                ", collegeId=" + collegeId +
                ", courseName='" + courseName + '\'' +
                ", courseLogo='" + courseLogo + '\'' +
                ", courseScheduleId=" + courseScheduleId +
                ", courseId=" + courseId +
                ", openCourseType=" + openCourseType +
                ", number=" + number +
                ", goodRate='" + goodRate + '\'' +
                ", openCoursePercent='" + openCoursePercent + '\'' +
                '}';
    }
}
