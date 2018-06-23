package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/14.
 * 我的订单 解析封装类 单个订单封装类
 */
public class ItemOrderVO {

    Long id;//订单id
    String orderCode;//订单号
    Double orderAmount;//价钱
    int schoolYear;//学期
    Byte orderStatus;//订单状态
    Byte payMethod;//支付方式
    Byte payType;//支付类型
    long orderCreateTime;//创建时间  时间戳
    String auditRefuseReson;//审核失败原因（订单状态审核不通过，才会有值）
    Byte orderType;//订单类型   0专业 1公开课   2微专业
    //准备接口需要的参数
    Long courseId;//
    Long courseScheduleId;//课程表id
    Long openCourseId;// 公开课id
    Byte openCourseType;//公开课类型
    Byte isOrderComment;//0:未评价 1：已经评价..

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getOpenCourseType() {
        return openCourseType;
    }

    public void setOpenCourseType(Byte openCourseType) {
        this.openCourseType = openCourseType;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(int schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Byte getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Byte payMethod) {
        this.payMethod = payMethod;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public long getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(long orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getAuditRefuseReson() {
        return auditRefuseReson;
    }

    public void setAuditRefuseReson(String auditRefuseReson) {
        this.auditRefuseReson = auditRefuseReson;
    }

    public Byte getOrderType() {
        return orderType;
    }

    public void setOrderType(Byte orderType) {
        this.orderType = orderType;
    }

    public Long getOpenCourseId() {
        return openCourseId;
    }

    public void setOpenCourseId(Long openCourseId) {
        this.openCourseId = openCourseId;
    }

    public Byte getIsOrderComment() {
        return isOrderComment;
    }

    public void setIsOrderComment(Byte isOrderComment) {
        this.isOrderComment = isOrderComment;
    }
}
