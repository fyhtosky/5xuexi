package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/30.
 * 课程表 - 专业 -视频详情- 简介接口
 * 接口7
 */
public class CouseInfo {
    MajorCourse majorCourse;
    int  courseType;//0专业课程详情  1公开课成详情

    public MajorCourse getMajorCourse() {
        return majorCourse;
    }

    public void setMajorCourse(MajorCourse majorCourse) {
        this.majorCourse = majorCourse;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }
}
