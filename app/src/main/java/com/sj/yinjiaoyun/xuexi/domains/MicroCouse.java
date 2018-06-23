package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/17.
 * 课程表 微专业  顶部tab对应的列表数据解析
 */
public class MicroCouse {

    Long id;
    Long collegeId;
    String collegeName;
    String trainingName;
    Byte tutionWay;//开课方式0：随到随学 1:定期开课
    TrainingItemVO trainingItemVO;

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

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Byte getTutionWay() {
        return tutionWay;
    }

    public void setTutionWay(Byte tutionWay) {
        this.tutionWay = tutionWay;
    }

    public TrainingItemVO getTrainingItemVO() {
        return trainingItemVO;
    }

    public void setTrainingItemVO(TrainingItemVO trainingItemVO) {
        this.trainingItemVO = trainingItemVO;
    }
}
