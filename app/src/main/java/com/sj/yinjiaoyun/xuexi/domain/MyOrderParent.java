package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/20.
 * 我的订单里面   adapter信息封装类 父订单
 */
public class MyOrderParent {

    //公共属性
    String orderUrl;//图标..
    String collegeName;//院校名称..
    String courseName;// 课程名称.
    Byte orderType; //订单类型  1公开课  0专业   2微专业..
    String orderCode;//订单号.
    long createTime;//订单创建时间  时间戳.
    Long objectId;//微专业id 公开课id(详情页接口用的)
    String qualificationAuditState;//审核的状态码
    String auditRefuseReason;//未通过审核原因
    int isAudit;//是否需要审核

    //学历课程
    Long productId;//招生专业ID..
    int pXueZhi;//学制...
    int pPayMethod;//支付方式..
    int pPayType;//支付类型..
    Byte productType;//招生专业类型 0：网校 1：成教 2：自考 3：培训 4：考证 5 : 选修',  培训和考证只能全额支付
    List<MyOrderChild> childList;//子订单.


    //公开课、微专业 特性
    Byte orderStatus;//'100：待付款 101：已付款 102:等待系统审核；109:订单超时取消；110：已完成
    Byte isOrderComment;//0:未评价 1：已经评价..
    Double pOrderAmount;//价格  公开课时，取父订单的信息填充所有..
    Byte isFree;//   0收费  1免费
    Byte tutionWay;//微专业开课方式 开课方式（0：随到随学 1:定期开课）
    Long enteredStartTime;//微专业开课时间
    Long enteredEndTime;//微专业开课时间
    int enterNumber;//


    //接口传递需要的数据
    Long orderId;//订单id
    Long courseScheduleId;//课程表id
    Byte courseType;//公开课类型(详情页页接口用的)
    Long courseId;//课程ID(详情页接口用的)

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
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

    public Byte getOrderType() {
        return orderType;
    }

    public void setOrderType(Byte orderType) {
        this.orderType = orderType;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getQualificationAuditState() {
        return qualificationAuditState;
    }

    public void setQualificationAuditState(String qualificationAuditState) {
        this.qualificationAuditState = qualificationAuditState;
    }

    public String getAuditRefuseReason() {
        return auditRefuseReason;
    }

    public void setAuditRefuseReason(String auditRefuseReason) {
        this.auditRefuseReason = auditRefuseReason;
    }

    public int getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(int isAudit) {
        this.isAudit = isAudit;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getpXueZhi() {
        return pXueZhi;
    }

    public void setpXueZhi(int pXueZhi) {
        this.pXueZhi = pXueZhi;
    }

    public int getpPayMethod() {
        return pPayMethod;
    }

    public void setpPayMethod(int pPayMethod) {
        this.pPayMethod = pPayMethod;
    }

    public int getpPayType() {
        return pPayType;
    }

    public void setpPayType(int pPayType) {
        this.pPayType = pPayType;
    }

    public Byte getProductType() {
        return productType;
    }

    public void setProductType(Byte productType) {
        this.productType = productType;
    }

    public List<MyOrderChild> getChildList() {
        return childList;
    }

    public void setChildList(List<MyOrderChild> childList) {
        this.childList = childList;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Byte getIsOrderComment() {
        return isOrderComment;
    }

    public void setIsOrderComment(Byte isOrderComment) {
        this.isOrderComment = isOrderComment;
    }

    public Double getpOrderAmount() {
        return pOrderAmount;
    }

    public void setpOrderAmount(Double pOrderAmount) {
        this.pOrderAmount = pOrderAmount;
    }

    public Byte getIsFree() {
        return isFree;
    }

    public void setIsFree(Byte isFree) {
        this.isFree = isFree;
    }

    public Byte getTutionWay() {
        return tutionWay;
    }

    public void setTutionWay(Byte tutionWay) {
        this.tutionWay = tutionWay;
    }

    public Long getEnteredStartTime() {
        return enteredStartTime;
    }

    public void setEnteredStartTime(Long enteredStartTime) {
        this.enteredStartTime = enteredStartTime;
    }

    public Long getEnteredEndTime() {
        return enteredEndTime;
    }

    public void setEnteredEndTime(Long enteredEndTime) {
        this.enteredEndTime = enteredEndTime;
    }

    public int getEnterNumber() {
        return enterNumber;
    }

    public void setEnterNumber(int enterNumber) {
        this.enterNumber = enterNumber;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(Long courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public Byte getCourseType() {
        return courseType;
    }

    public void setCourseType(Byte courseType) {
        this.courseType = courseType;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "MyOrderParent{" +
                "orderUrl='" + orderUrl + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", orderType=" + orderType +
                ", orderCode='" + orderCode + '\'' +
                ", createTime=" + createTime +
                ", objectId=" + objectId +
                ", qualificationAuditState='" + qualificationAuditState + '\'' +
                ", auditRefuseReason='" + auditRefuseReason + '\'' +
                ", isAudit=" + isAudit +
                ", productId=" + productId +
                ", pXueZhi=" + pXueZhi +
                ", pPayMethod=" + pPayMethod +
                ", pPayType=" + pPayType +
                ", productType=" + productType +
                ", childList=" + childList +
                ", orderStatus=" + orderStatus +
                ", isOrderComment=" + isOrderComment +
                ", pOrderAmount=" + pOrderAmount +
                ", isFree=" + isFree +
                ", tutionWay=" + tutionWay +
                ", enteredStartTime=" + enteredStartTime +
                ", enteredEndTime=" + enteredEndTime +
                ", enterNumber=" + enterNumber +
                ", orderId=" + orderId +
                ", courseScheduleId=" + courseScheduleId +
                ", courseType=" + courseType +
                ", courseId=" + courseId +
                '}';
    }
}
