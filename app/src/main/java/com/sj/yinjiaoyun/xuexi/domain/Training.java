package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/8.
 * 首页推荐 微专业信息
 */
public class Training {

    Long id;
    Long collegeId;
    String trainingName;
    String trainingLogo;
    String trainingIntro;//简介
    String trainingDesc;//简介
    Byte trainingStatus;//1,【微专业状态（0::下架 1：上架）】
    Byte isFree;// 1, 【0：收费 1：不收费】//是否付费（0：收费  1：免费）
    Byte tutionWay; //0, 【开课方式（0：随到随学 1:定期开课）】

    String createTime;

    String collegeName;
    String collegeDesc;
    Double  minPrice;//价格区间之底价（微专业）
    Double maxPrice;// 价格区间值顶价（微专业）
    int enterNumber;//【学习人数】



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Long collegeId) {
        this.collegeId = collegeId;
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

    public String getTrainingIntro() {
        return trainingIntro;
    }

    public void setTrainingIntro(String trainingIntro) {
        this.trainingIntro = trainingIntro;
    }

    public Byte getTrainingStatus() {
        return trainingStatus;
    }

    public void setTrainingStatus(Byte trainingStatus) {
        this.trainingStatus = trainingStatus;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCollegeDesc() {
        return collegeDesc;
    }

    public void setCollegeDesc(String collegeDesc) {
        this.collegeDesc = collegeDesc;
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

    public int getEnterNumber() {
        return enterNumber;
    }

    public void setEnterNumber(int enterNumber) {
        this.enterNumber = enterNumber;
    }

    public String getTrainingDesc() {
        return trainingDesc;
    }

    public void setTrainingDesc(String trainingDesc) {
        this.trainingDesc = trainingDesc;
    }
}
