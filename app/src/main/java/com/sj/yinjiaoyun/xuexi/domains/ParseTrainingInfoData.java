package com.sj.yinjiaoyun.xuexi.domains;

import com.sj.yinjiaoyun.xuexi.domain.Training;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/14.
 * 解析 微专业预览简介
 */
public class ParseTrainingInfoData {
    Training training;
    List<TrainingItem> trainingItems;
    int trainingCurrentInd;//默认显示时间段在List中的下标所有

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public List<TrainingItem> getTrainingItems() {
        return trainingItems;
    }

    public void setTrainingItems(List<TrainingItem> trainingItems) {
        this.trainingItems = trainingItems;
    }

    public int getTrainingCurrentInd() {
        return trainingCurrentInd;
    }

    public void setTrainingCurrentInd(int trainingCurrentInd) {
        this.trainingCurrentInd = trainingCurrentInd;
    }
}
