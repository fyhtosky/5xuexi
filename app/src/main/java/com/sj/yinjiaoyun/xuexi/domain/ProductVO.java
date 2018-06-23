package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/26.
 * 我的专业界面   productVO集合里面子项
 */
public class ProductVO {
    long id;//招生专业Id
    String productName;//招生专业名称
    long enrollPlanId;//招生计划Id
    String collegeName;//院校名称
    int productType;//教育类别   学年 1:第一学年 2:第二学年 ....', 0的时候不显示
    int productGradation;//层次   1:高起专 2:专升本  3 高起本  4 专科 5 本科....', 0的时候不显示
    int productLearningLength;//专业学制' (网校   成教   自考   选修)单位：年  ，（培训  考证）单位：周
    long status;//是否已选专业方向
    String productIntroduction;//方向简介

    public ProductVO(long id, String productName, long enrollPlanId) {
        this.id = id;
        this.productName = productName;
        this.enrollPlanId = enrollPlanId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getEnrollPlanId() {
        return enrollPlanId;
    }

    public void setEnrollPlanId(Long enrollPlanId) {
        this.enrollPlanId = enrollPlanId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEnrollPlanId(long enrollPlanId) {
        this.enrollPlanId = enrollPlanId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getProductGradation() {
        return productGradation;
    }

    public void setProductGradation(int productGradation) {
        this.productGradation = productGradation;
    }

    public int getProductLearningLength() {
        return productLearningLength;
    }

    public void setProductLearningLength(int productLearningLength) {
        this.productLearningLength = productLearningLength;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getProductIntroduction() {
        return productIntroduction;
    }

    public void setProductIntroduction(String productIntroduction) {
        this.productIntroduction = productIntroduction;
    }

    @Override
    public String toString() {
        return "ProductVO{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", enrollPlanId=" + enrollPlanId +
                ", collegeName='" + collegeName + '\'' +
                ", productType=" + productType +
                ", productGradation=" + productGradation +
                ", productLearningLength=" + productLearningLength +
                ", status=" + status +
                ", productIntroduction='" + productIntroduction + '\'' +
                '}';
    }
}
