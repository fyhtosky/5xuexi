package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/10.
 * 课程表 顶部专业解析
 */
public class SoaProductVOs {

    long id;//招生专业Id
    String productName;//招生专业名称
    long enrollPlanId;//招生计划Id
    String collegeName;//院校名称
    int productType;//教育类别   0：网校 1：成教 2：自考 3：培训 4：考证 5 : 选修
    int productGradation;//层次   1:高起专 2:专升本  3 高起本  4 专科 5 本科....', 0的时候不显示
    long learningModality;
    String productLogoUrl;
    int productLearningLength; //专业学制' (网校   成教   自考   选修)单位：年  ，（培训  考证）单位：周
    String enrollPlanSeason;
    long status;// 1请选方向；2请选择课程；  3完成；
    long startFlow;
    List<ProductDirection> soaEnrollPlanDirectionVO;//方向集合
    SoaEndUserDirectionVO soaEndUserDirectionVO;
    String productIntroduction;//方向简介
    String productTypeText;
    String whetherDirection;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getEnrollPlanId() {
        return enrollPlanId;
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

    public long getLearningModality() {
        return learningModality;
    }

    public void setLearningModality(long learningModality) {
        this.learningModality = learningModality;
    }

    public String getProductLogoUrl() {
        return productLogoUrl;
    }

    public void setProductLogoUrl(String productLogoUrl) {
        this.productLogoUrl = productLogoUrl;
    }

    public int getProductLearningLength() {
        return productLearningLength;
    }

    public void setProductLearningLength(int productLearningLength) {
        this.productLearningLength = productLearningLength;
    }

    public String getEnrollPlanSeason() {
        return enrollPlanSeason;
    }

    public void setEnrollPlanSeason(String enrollPlanSeason) {
        this.enrollPlanSeason = enrollPlanSeason;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getStartFlow() {
        return startFlow;
    }

    public void setStartFlow(long startFlow) {
        this.startFlow = startFlow;
    }

    public List<ProductDirection> getSoaEnrollPlanDirectionVO() {
        return soaEnrollPlanDirectionVO;
    }

    public void setSoaEnrollPlanDirectionVO(List<ProductDirection> soaEnrollPlanDirectionVO) {
        this.soaEnrollPlanDirectionVO = soaEnrollPlanDirectionVO;
    }

    public SoaEndUserDirectionVO getSoaEndUserDirectionVO() {
        return soaEndUserDirectionVO;
    }

    public void setSoaEndUserDirectionVO(SoaEndUserDirectionVO soaEndUserDirectionVO) {
        this.soaEndUserDirectionVO = soaEndUserDirectionVO;
    }

    public String getProductIntroduction() {
        return productIntroduction;
    }

    public void setProductIntroduction(String productIntroduction) {
        this.productIntroduction = productIntroduction;
    }

    public String getProductTypeText() {
        return productTypeText;
    }

    public void setProductTypeText(String productTypeText) {
        this.productTypeText = productTypeText;
    }

    public String getWhetherDirection() {
        return whetherDirection;
    }

    public void setWhetherDirection(String whetherDirection) {
        this.whetherDirection = whetherDirection;
    }

    @Override
    public String toString() {
        return "SoaProductVOs{" +
                "id=" + id +
                ", status=" + status +
                ", startFlow=" + startFlow +
                ", productName='" + productName + '\'' +
                ", enrollPlanId=" + enrollPlanId +
                ", collegeName='" + collegeName + '\'' +
                ", productType=" + productType +
                ", productGradation=" + productGradation +
                ", learningModality=" + learningModality +
                ", productLogoUrl='" + productLogoUrl + '\'' +
                ", productLearningLength=" + productLearningLength +
                ", enrollPlanSeason='" + enrollPlanSeason + '\'' +
                ", soaEnrollPlanDirectionVO=" + soaEnrollPlanDirectionVO +
                ", soaEndUserDirectionVO=" + soaEndUserDirectionVO +
                ", productIntroduction='" + productIntroduction + '\'' +
                ", productTypeText='" + productTypeText + '\'' +
                ", whetherDirection='" + whetherDirection + '\'' +
                '}';
    }
}
