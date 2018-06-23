package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/16.
 * 微专业 预览页面的课程体系
 */
public class TrainingCourse {

    String courseName;//课程名称
    String teacherName;//授课老师名称
    String courseCredit;
    String productDirectionName;
    String courseAttribute;
    Byte trainingCourseOrder;//微专业课程组课顺序

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(String courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getProductDirectionName() {
        return productDirectionName;
    }

    public void setProductDirectionName(String productDirectionName) {
        this.productDirectionName = productDirectionName;
    }

    public String getCourseAttribute() {
        return courseAttribute;
    }

    public void setCourseAttribute(String courseAttribute) {
        this.courseAttribute = courseAttribute;
    }

    public Byte getTrainingCourseOrder() {
        return trainingCourseOrder;
    }

    public void setTrainingCourseOrder(Byte trainingCourseOrder) {
        this.trainingCourseOrder = trainingCourseOrder;
    }

    @Override
    public String toString() {
        return "TrainingCourse{" +
                "courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", courseCredit='" + courseCredit + '\'' +
                ", productDirectionName='" + productDirectionName + '\'' +
                ", courseAttribute='" + courseAttribute + '\'' +
                ", trainingCourseOrder=" + trainingCourseOrder +
                '}';
    }
}
