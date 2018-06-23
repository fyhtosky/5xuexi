package com.sj.yinjiaoyun.xuexi.bean;




/**
 * Created by wanzhiying on 2017/3/8.
 * 公开课的解析的实体类
 */
public class TigaseGroupVO {
    private Long id;
    private Long parentId;
    private long isNotDisturb;
    private String groupId;
    private Long  isLef;
    private Long businessId;
    private Long type;
    private Long createTime;
    private String businessName;
    private String businessImg;
    private String productDirectionName;
    private String trainingItemName;
    private Long trainingType;
    private String enrollPlanName;
    private Long productType;
    private Long openCourseType;
    private String collegeName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public long getIsNotDisturb() {
        return isNotDisturb;
    }

    public void setIsNotDisturb(long isNotDisturb) {
        this.isNotDisturb = isNotDisturb;
    }

    public Long getIsLef() {
        return isLef;
    }

    public void setIsLef(Long isLef) {
        this.isLef = isLef;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessImg() {
        return businessImg;
    }

    public void setBusinessImg(String businessImg) {
        this.businessImg = businessImg;
    }

    public String getProductDirectionName() {
        return productDirectionName;
    }

    public void setProductDirectionName(String productDirectionName) {
        this.productDirectionName = productDirectionName;
    }

    public String getTrainingItemName() {
        return trainingItemName;
    }

    public void setTrainingItemName(String trainingItemName) {
        this.trainingItemName = trainingItemName;
    }

    public Long getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(Long trainingType) {
        this.trainingType = trainingType;
    }

    public String getEnrollPlanName() {
        return enrollPlanName;
    }

    public void setEnrollPlanName(String enrollPlanName) {
        this.enrollPlanName = enrollPlanName;
    }

    public Long getProductType() {
        return productType;
    }

    public void setProductType(Long productType) {
        this.productType = productType;
    }

    public Long getOpenCourseType() {
        return openCourseType;
    }

    public void setOpenCourseType(Long openCourseType) {
        this.openCourseType = openCourseType;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    @Override
    public String toString() {
        return "TigaseGroupVO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", isNotDisturb=" + isNotDisturb +
                ", groupId='" + groupId + '\'' +
                ", isLef=" + isLef +
                ", businessId=" + businessId +
                ", type=" + type +
                ", createTime=" + createTime +
                ", businessName='" + businessName + '\'' +
                ", businessImg='" + businessImg + '\'' +
                ", productDirectionName='" + productDirectionName + '\'' +
                ", trainingItemName='" + trainingItemName + '\'' +
                ", trainingType=" + trainingType +
                ", enrollPlanName='" + enrollPlanName + '\'' +
                ", productType=" + productType +
                ", openCourseType=" + openCourseType +
                ", collegeName='" + collegeName + '\'' +
                '}';
    }
}
