package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/14.
 * 自定义控件的封装类
 */
public class Pickers {

    String trainingItemId;//分期id
    String showStr;
    Double showPrice;
    Long startTime;
    Long endTime;


    public Pickers(String trainingItemId, String showStr, Double showPrice, Long startTime, Long endTime) {
        this.trainingItemId = trainingItemId;
        this.showStr = showStr;
        this.showPrice = showPrice;
        this.startTime = startTime;
        this.endTime = endTime;
    }



    public Double getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(Double showPrice) {
        this.showPrice = showPrice;
    }

    public String getShowStr() {
        return showStr;
    }

    public void setShowStr(String showStr) {
        this.showStr = showStr;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getTrainingItemId() {
        return trainingItemId;
    }

    public void setTrainingItemId(String trainingItemId) {
        this.trainingItemId = trainingItemId;
    }


    @Override
    public String toString() {
        return "Pickers{" +
                "trainingItemId='" + trainingItemId + '\'' +
                ", showStr='" + showStr + '\'' +
                ", showPrice=" + showPrice +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
