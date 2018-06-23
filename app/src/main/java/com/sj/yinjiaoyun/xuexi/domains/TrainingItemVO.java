package com.sj.yinjiaoyun.xuexi.domains;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/17.
 * 课程表 微专业  顶部tab对应的列表数据解析
 */
public class TrainingItemVO {

    String id;
    Double price;
    Long trainingId;
    Long enteredStartTime;
    Long enteredEndTime;//学习进度
    String trainingPercent;

    List<CourseScheduleVO> courseScheduleVOs;

    Byte  isDeleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
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

    public String getTrainingPercent() {
        return trainingPercent;
    }

    public void setTrainingPercent(String trainingPercent) {
        this.trainingPercent = trainingPercent;
    }

    public List<CourseScheduleVO> getCourseScheduleVOs() {
        return courseScheduleVOs;
    }

    public void setCourseScheduleVOs(List<CourseScheduleVO> courseScheduleVOs) {
        this.courseScheduleVOs = courseScheduleVOs;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }
}
