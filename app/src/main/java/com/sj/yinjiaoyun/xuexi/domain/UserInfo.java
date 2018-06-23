package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/21.
 * 接口解析类
 * (data登录接口，（成功之后跳转到我的专业）,根据用户ID获取用户个人信息 )
 */
public class UserInfo {
    Long endUserId;	//用户ID
    String	userName;//	用户名
    String	realName;//姓名
    Byte sex;//性别    性别(1:男;2:女)
    String	phone;//	必有	手机号
    String idCard;//	必有	身份证
    String email;//	必有	邮箱
    String middleSchoolCertificate;      //可能	高考准考证号
    String collegeSpecializCertificate;  //可能	大学专科毕业证号
    String collegeSchoolCertificate;     //可能	大学本科毕业证号
    String bachelorCertificate;          //可能	大学学位证证号
    String userImg;                      //可能	头像
    String address;                      //可能	地址
    String politicsStatus;               //可能	政治面貌
    String nation;                       //可能	名族
    String fixPhone;                     //可能	固定电话
    String postalCode;                   //可能	邮政编码

    public Long getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(Long endUserId) {
        this.endUserId = endUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMiddleSchoolCertificate() {
        return middleSchoolCertificate;
    }

    public void setMiddleSchoolCertificate(String middleSchoolCertificate) {
        this.middleSchoolCertificate = middleSchoolCertificate;
    }

    public String getCollegeSpecializCertificate() {
        return collegeSpecializCertificate;
    }

    public void setCollegeSpecializCertificate(String collegeSpecializCertificate) {
        this.collegeSpecializCertificate = collegeSpecializCertificate;
    }

    public String getCollegeSchoolCertificate() {
        return collegeSchoolCertificate;
    }

    public void setCollegeSchoolCertificate(String collegeSchoolCertificate) {
        this.collegeSchoolCertificate = collegeSchoolCertificate;
    }

    public String getBachelorCertificate() {
        return bachelorCertificate;
    }

    public void setBachelorCertificate(String bachelorCertificate) {
        this.bachelorCertificate = bachelorCertificate;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPoliticsStatus() {
        return politicsStatus;
    }

    public void setPoliticsStatus(String politicsStatus) {
        this.politicsStatus = politicsStatus;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getFixPhone() {
        return fixPhone;
    }

    public void setFixPhone(String fixPhone) {
        this.fixPhone = fixPhone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "endUserId=" + endUserId +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", email='" + email + '\'' +
                ", middleSchoolCertificate='" + middleSchoolCertificate + '\'' +
                ", collegeSpecializCertificate='" + collegeSpecializCertificate + '\'' +
                ", collegeSchoolCertificate='" + collegeSchoolCertificate + '\'' +
                ", bachelorCertificate='" + bachelorCertificate + '\'' +
                ", userImg='" + userImg + '\'' +
                ", address='" + address + '\'' +
                ", politicsStatus='" + politicsStatus + '\'' +
                ", nation='" + nation + '\'' +
                ", fixPhone='" + fixPhone + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
