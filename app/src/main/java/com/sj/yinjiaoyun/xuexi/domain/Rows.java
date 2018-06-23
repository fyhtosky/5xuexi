package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/14.
 * 我的订单 解析封装类 内层
 * 某个专业
 */
public class Rows {

    ItemOrderVO orderVO;//父订单
    List<ItemOrderVO> itemOrderVOs;//子订单集合
    Long productId;//招生专业ID
    int productLength;//学制
    String productName;//招生专业名称
    String productUrl;//招生专业图片
    Byte productType;//教育类型0：网校 1：成教 2：自考 3：培训 4：考证 5 : 选修',
    String collegeName;//院校名称
    String courseName;//课程名称
    String courseLogo;//课程图标
    Byte orderType;//订单类型  1公开课  0专业   2微专业
    Long openCourseId;// 公开课id

    //微专业
    Long trainingId;//微专业id
    Long trainingItemId;//微专业分期id
    String trainingName;//
    String trainingLogo;//
    Byte isFree;//
    Byte tutionWay;//
    Long enteredStartTime;//
    Long enteredEndTime;//
    String qualificationAuditState;
    String auditRefuseReason;
    int isAudit;

    public ItemOrderVO getOrderVO() {
        return orderVO;
    }

    public void setOrderVO(ItemOrderVO orderVO) {
        this.orderVO = orderVO;
    }

    public List<ItemOrderVO> getItemOrderVOs() {
        return itemOrderVOs;
    }

    public void setItemOrderVOs(List<ItemOrderVO> itemOrderVOs) {
        this.itemOrderVOs = itemOrderVOs;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getProductLength() {
        return productLength;
    }

    public void setProductLength(int productLength) {
        this.productLength = productLength;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public Byte getProductType() {
        return productType;
    }

    public void setProductType(Byte productType) {
        this.productType = productType;
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

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public Long getTrainingItemId() {
        return trainingItemId;
    }

    public void setTrainingItemId(Long trainingItemId) {
        this.trainingItemId = trainingItemId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingLogo() {
        return trainingLogo;
    }

    public void setTrainingLogo(String trainingLogo) {
        this.trainingLogo = trainingLogo;
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
}
