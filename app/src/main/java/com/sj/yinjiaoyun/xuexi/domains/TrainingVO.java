package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/16.
 * 课程表  我的微专业  顶部部封装类
 */
public class TrainingVO {

    Long id;//微专业id
    Long collegeId;//
    Long trainingItemId;//微专业分期id
    String collegeName;
    String trainingName;//微专业名称
    String trainingLogo;
    String trainingIntro;//简介
    String trainingDesc;//简介
    Byte trainingStatus;//1,【微专业状态（0::下架 1：上架）】
    Byte isFree;// 1, 【0：收费 1：不收费】
    Byte tutionWay; //0, 【开课方式（0：随到随学 1:定期开课）】

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

    public String getTrainingDesc() {
        return trainingDesc;
    }

    public void setTrainingDesc(String trainingDesc) {
        this.trainingDesc = trainingDesc;
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

    public Long getTrainingItemId() {
        return trainingItemId;
    }

    public void setTrainingItemId(Long trainingItemId) {
        this.trainingItemId = trainingItemId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
}
