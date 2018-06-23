package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/14.
 * 微专业 简介里面   解析分期列表中 的单项封装类
 */
public class TrainingItem {

    Long id;
    Long trainingId;
    Double price;
    Double originalPrice;
    Long enteredStartTime;
    Long enteredEndTime;
    String trainingPercent;
    int isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
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

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "TrainingItem{" +
                "id=" + id +
                ", trainingId=" + trainingId +
                ", price=" + price +
                ", originalPrice=" + originalPrice +
                ", enteredStartTime=" + enteredStartTime +
                ", enteredEndTime=" + enteredEndTime +
                ", trainingPercent='" + trainingPercent + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
