package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/7.
 * 解析学籍信息  外围
 */
public class StudentNumbers {
    String userHead;//录取图片

    String applicationNumber;//成人高考报名号
    String studentCode;//学号
    String collegeCode;//院校代码
    String collegeName;//院校名称
    String productCode;//专业代码

    String productName;//专业名称
    byte productType;//教育类别
    byte productGradation;//层次
    String learningModality;//学习形式
    String productLearningLength;//学制
    String learningLength;//学制

    int theYear;//年级
    byte theSeason;//入学季
    String enrollDate;//入学日期
    String teachingCenterName;//教学站点
    String graduateTime;//预计毕业时间
    String remark;//备注

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getLearningLength() {
        return learningLength;
    }

    public void setLearningLength(String learningLength) {
        this.learningLength = learningLength;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public byte getProductType() {
        return productType;
    }

    public void setProductType(byte productType) {
        this.productType = productType;
    }

    public byte getProductGradation() {
        return productGradation;
    }

    public void setProductGradation(byte productGradation) {
        this.productGradation = productGradation;
    }

    public String getLearningModality() {
        return learningModality;
    }

    public void setLearningModality(String learningModality) {
        this.learningModality = learningModality;
    }

    public String getProductLearningLength() {
        return productLearningLength;
    }

    public void setProductLearningLength(String productLearningLength) {
        this.productLearningLength = productLearningLength;
    }

    public int getTheYear() {
        return theYear;
    }

    public void setTheYear(int theYear) {
        this.theYear = theYear;
    }

    public byte getTheSeason() {
        return theSeason;
    }

    public void setTheSeason(byte theSeason) {
        this.theSeason = theSeason;
    }

    public String getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(String enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getTeachingCenterName() {
        return teachingCenterName;
    }

    public void setTeachingCenterName(String teachingCenterName) {
        this.teachingCenterName = teachingCenterName;
    }

    public String getGraduateTime() {
        return graduateTime;
    }

    public void setGraduateTime(String graduateTime) {
        this.graduateTime = graduateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Override
    public String toString() {
        return "StudentNumbers{" +
                "userHead='" + userHead + '\'' +
                ", applicationNumber='" + applicationNumber + '\'' +
                ", studentCode='" + studentCode + '\'' +
                ", collegeCode='" + collegeCode + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productType=" + productType +
                ", productGradation=" + productGradation +
                ", learningModality='" + learningModality + '\'' +
                ", productLearningLength='" + productLearningLength + '\'' +
                ", theYear=" + theYear +
                ", theSeason=" + theSeason +
                ", enrollDate='" + enrollDate + '\'' +
                ", teachingCenterName='" + teachingCenterName + '\'' +
                ", graduateTime='" + graduateTime + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
