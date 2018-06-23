package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/28.
 * 课程表 - 专业 -视频详情- 简介接口
 * 接口7
 */
public class MajorCourse {
    Long id;                //  是	课程
    Long productId;         //
    Long collegeId;         //
    Long teachingGroupId;         //
    Long enrollPlanDirectionId;         //

    Long productDirectionId;         //
    Long enrolmentId;         //
    Long courseId;           //
    Byte courseCredit;       //是	课程学分
    String suggestTime;

    String courseStartTime; //
    Byte  courseAttribute;  //	是	课程性质  0:公共基础课    1:专业选修课  2:专业基础课  3:公共选修课
    Byte  theFewYear;       //	是	学年 1:第一学年 2:第二学年 ....', 0的时候不显示
    Long operatorRoleId;    //
    int isDeleted;

    String createTime;
    int createBy;
    String updateTime;
    int updateBy;
    String courseName;      //	是	课程名称

    String courseTotalTime;
    String enrollmentPlanName;
    String enrollPlanVO;
    String productDirectionName;
    String productName;

    String	teacherName;   //	是	教师姓名
    String collegeName;
    String organizationList;
    String	courseLogoUrl;  //	是	课程logo

    String totalScore;
    Long courseScheduleId;
    String	courseDesc;     //	是	课程简介
    String coursePercent;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Long collegeId) {
        this.collegeId = collegeId;
    }

    public Long getTeachingGroupId() {
        return teachingGroupId;
    }

    public void setTeachingGroupId(Long teachingGroupId) {
        this.teachingGroupId = teachingGroupId;
    }

    public Long getEnrollPlanDirectionId() {
        return enrollPlanDirectionId;
    }

    public void setEnrollPlanDirectionId(Long enrollPlanDirectionId) {
        this.enrollPlanDirectionId = enrollPlanDirectionId;
    }

    public Long getProductDirectionId() {
        return productDirectionId;
    }

    public void setProductDirectionId(Long productDirectionId) {
        this.productDirectionId = productDirectionId;
    }

    public Long getEnrolmentId() {
        return enrolmentId;
    }

    public void setEnrolmentId(Long enrolmentId) {
        this.enrolmentId = enrolmentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Byte getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(Byte courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getSuggestTime() {
        return suggestTime;
    }

    public void setSuggestTime(String suggestTime) {
        this.suggestTime = suggestTime;
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

    public Long getOperatorRoleId() {
        return operatorRoleId;
    }

    public void setOperatorRoleId(Long operatorRoleId) {
        this.operatorRoleId = operatorRoleId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTotalTime() {
        return courseTotalTime;
    }

    public void setCourseTotalTime(String courseTotalTime) {
        this.courseTotalTime = courseTotalTime;
    }

    public String getEnrollmentPlanName() {
        return enrollmentPlanName;
    }

    public void setEnrollmentPlanName(String enrollmentPlanName) {
        this.enrollmentPlanName = enrollmentPlanName;
    }

    public String getEnrollPlanVO() {
        return enrollPlanVO;
    }

    public void setEnrollPlanVO(String enrollPlanVO) {
        this.enrollPlanVO = enrollPlanVO;
    }

    public String getProductDirectionName() {
        return productDirectionName;
    }

    public void setProductDirectionName(String productDirectionName) {
        this.productDirectionName = productDirectionName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(String organizationList) {
        this.organizationList = organizationList;
    }

    public String getCourseLogoUrl() {
        return courseLogoUrl;
    }

    public void setCourseLogoUrl(String courseLogoUrl) {
        this.courseLogoUrl = courseLogoUrl;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public Long getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(Long courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getCoursePercent() {
        return coursePercent;
    }

    public void setCoursePercent(String coursePercent) {
        this.coursePercent = coursePercent;
    }

    @Override
    public String toString() {
        return "MajorCourse{" +
                "id=" + id +
                ", productId=" + productId +
                ", collegeId=" + collegeId +
                ", teachingGroupId=" + teachingGroupId +
                ", enrollPlanDirectionId=" + enrollPlanDirectionId +
                ", productDirectionId=" + productDirectionId +
                ", enrolmentId=" + enrolmentId +
                ", courseId=" + courseId +
                ", courseCredit=" + courseCredit +
                ", suggestTime='" + suggestTime + '\'' +
                ", courseStartTime='" + courseStartTime + '\'' +
                ", courseAttribute=" + courseAttribute +
                ", theFewYear=" + theFewYear +
                ", operatorRoleId=" + operatorRoleId +
                ", isDeleted=" + isDeleted +
                ", createTime='" + createTime + '\'' +
                ", createBy=" + createBy +
                ", updateTime='" + updateTime + '\'' +
                ", updateBy=" + updateBy +
                ", courseName='" + courseName + '\'' +
                ", courseTotalTime='" + courseTotalTime + '\'' +
                ", enrollmentPlanName='" + enrollmentPlanName + '\'' +
                ", enrollPlanVO='" + enrollPlanVO + '\'' +
                ", productDirectionName='" + productDirectionName + '\'' +
                ", productName='" + productName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", organizationList='" + organizationList + '\'' +
                ", courseLogoUrl='" + courseLogoUrl + '\'' +
                ", totalScore='" + totalScore + '\'' +
                ", courseScheduleId=" + courseScheduleId +
                ", courseDesc='" + courseDesc + '\'' +
                ", coursePercent='" + coursePercent + '\'' +
                '}';
    }
}
