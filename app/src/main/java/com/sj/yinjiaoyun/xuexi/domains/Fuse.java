package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/8.
 * 首页、发现（接受类）  公开课 微专业 转化成公共的封装类（给adapter使用）
 * 发现页面 搜索解析用到
 */
public class Fuse {

    int indexType;//业务类型 0： 专业，1：公开课   2 微专业

    String courseLogo;//课程图片
    String courseName;//课程名称
    String collegeName;//院校名称
    int number;//学习人数

    Long courseId; //课程id
    Long productId; //公开课、微专业id
    Long collegeId;//院校id
    Byte isFree;  //0：免费 1不免费

    Double price;//  价格   （公开课独有）
    Double  minPrice;// 价格区间之底价（微专业）
    Double maxPrice;//  价格区间值顶价（微专业）
    String productName;   // 课程名称（微专业）
    String productLogoUrl;//课程图标（微专业）

    Byte tutionWay; // 开课方式 0：随到随学 1:定期开课（微专业）
    Byte courseType;// 公开课  课程类型

    int isAudit;//表示是否需要審核

    public Fuse() {
    }


    public int getIndexType() {
        return indexType;
    }

    public void setIndexType(int indexType) {
        this.indexType = indexType;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCourseLogo() {
        return courseLogo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCourseLogo(String courseLogo) {
        this.courseLogo = courseLogo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Byte getIsFree() {
        return isFree;
    }

    public void setIsFree(Byte isFree) {
        this.isFree = isFree;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Byte getTutionWay() {
        return tutionWay;
    }

    public void setTutionWay(Byte tutionWay) {
        this.tutionWay = tutionWay;
    }

    public Byte getCourseType() {
        return courseType;
    }

    public void setCourseType(Byte courseType) {
        this.courseType = courseType;
    }

    public String getProductLogoUrl() {
        return productLogoUrl;
    }

    public void setProductLogoUrl(String productLogoUrl) {
        this.productLogoUrl = productLogoUrl;
    }

    public Long getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Long collegeId) {
        this.collegeId = collegeId;
    }

    public int getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(int isAudit) {
        this.isAudit = isAudit;
    }

    @Override
    public String toString() {
        return "Fuse{" +
                "indexType=" + indexType +
                ", courseLogo='" + courseLogo + '\'' +
                ", courseName='" + courseName + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", number=" + number +
                ", courseId=" + courseId +
                ", productId=" + productId +
                ", collegeId=" + collegeId +
                ", isFree=" + isFree +
                ", price=" + price +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", productName='" + productName + '\'' +
                ", productLogoUrl='" + productLogoUrl + '\'' +
                ", tutionWay=" + tutionWay +
                ", courseType=" + courseType +
                '}';
    }
}
