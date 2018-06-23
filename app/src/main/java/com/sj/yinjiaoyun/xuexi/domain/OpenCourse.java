package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/6.
 * 首页轮播图   公开课
 */
public class OpenCourse {

    Long id;
    Long courseId;
    String collegeName;
    String courseName;
    String courseLogo;
    String goodRate;
    Double price;
    int number;
    Byte openCourseType;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
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

    public String getGoodRate() {
        return goodRate;
    }

    public void setGoodRate(String goodRate) {
        this.goodRate = goodRate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Byte getOpenCourseType() {
        return openCourseType;
    }

    public void setOpenCourseType(Byte openCourseType) {
        this.openCourseType = openCourseType;
    }

    @Override
    public String toString() {
        return "OpenCourse{" +
                "id=" + id +
                ", collegeName='" + collegeName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseLogo='" + courseLogo + '\'' +
                ", goodRate='" + goodRate + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
