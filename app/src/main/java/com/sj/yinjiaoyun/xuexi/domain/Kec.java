package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/16.
 *  课程表 选完方向 选课页面
 */
public class Kec {

    long id;	//是	教学计划id
    String productId;	//tring	是	专业id
    String collegeId;	//是	院校id
    String collegeName;	//是	院校名称
    Byte enrollPlanDirectionId;	//	是	招生计划方向ID
    Byte productDirectionId;	//是	专业方向id
    String enrolmentId;	//是	招生计划id
    long courseId;	//	是	课程id
    long  courseScheduleId;	//	是	课表ID
    String ProductName;	//	是	专业名称
    String courseStartTime;	//	是	课程开始时间
    Byte courseAttribute;	//Byte	是	课程性质 0:公共基础课 3:公共选修课 2:专业基础课 1:专业选修课
    Byte theFewYear;	//学年
    long operatorRoleId;	//		教师ID
    String courseName;	//		课程名称
    String enrollmentPlanName;	//		招生计划名称
    String courseLogoUrl;	//		课程logo

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Byte getEnrollPlanDirectionId() {
        return enrollPlanDirectionId;
    }

    public void setEnrollPlanDirectionId(Byte enrollPlanDirectionId) {
        this.enrollPlanDirectionId = enrollPlanDirectionId;
    }

    public Byte getProductDirectionId() {
        return productDirectionId;
    }

    public void setProductDirectionId(Byte productDirectionId) {
        this.productDirectionId = productDirectionId;
    }

    public String getEnrolmentId() {
        return enrolmentId;
    }

    public void setEnrolmentId(String enrolmentId) {
        this.enrolmentId = enrolmentId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(long courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(String courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public Byte getCourseAttribute() {
        return courseAttribute;
    }

    public void setCourseAttribute(Byte courseAttribute) {
        this.courseAttribute = courseAttribute;
    }

    public Byte getTheFewYear() {
        return theFewYear;
    }

    public void setTheFewYear(Byte theFewYear) {
        this.theFewYear = theFewYear;
    }

    public long getOperatorRoleId() {
        return operatorRoleId;
    }

    public void setOperatorRoleId(long operatorRoleId) {
        this.operatorRoleId = operatorRoleId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEnrollmentPlanName() {
        return enrollmentPlanName;
    }

    public void setEnrollmentPlanName(String enrollmentPlanName) {
        this.enrollmentPlanName = enrollmentPlanName;
    }

    public String getCourseLogoUrl() {
        return courseLogoUrl;
    }

    public void setCourseLogoUrl(String courseLogoUrl) {
        this.courseLogoUrl = courseLogoUrl;
    }
}
