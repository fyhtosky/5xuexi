package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/30.
 * 接口10-获取学生课程成绩
 */
public class Score {
    Integer learnBehaviorScore;//	是	学习行为成绩
    Integer	usualScore;        //   是	平时成绩
    Integer	finalScore;       //	是	结业成绩
    Integer	totalScore;       //	是	总成绩
    String coursePercent;
    Long totalPlayTime;

    public Score(Integer learnBehaviorScore, Integer usualScore, Integer finalScore, Integer totalScore, String coursePercent, Long totalPlayTime) {
        this.learnBehaviorScore = learnBehaviorScore;
        this.usualScore = usualScore;
        this.finalScore = finalScore;
        this.totalScore = totalScore;
        this.coursePercent = coursePercent;
        this.totalPlayTime = totalPlayTime;
    }

    public Score(Integer learnBehaviorScore, Integer usualScore, Integer finalScore, Integer totalScore) {
        this.learnBehaviorScore = learnBehaviorScore;
        this.usualScore = usualScore;
        this.finalScore = finalScore;
        this.totalScore = totalScore;
    }

    public Integer getLearnBehaviorScore() {
        return learnBehaviorScore;
    }

    public void setLearnBehaviorScore(Integer learnBehaviorScore) {
        this.learnBehaviorScore = learnBehaviorScore;
    }

    public Integer getUsualScore() {
        return usualScore;
    }

    public void setUsualScore(Integer usualScore) {
        this.usualScore = usualScore;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public String getCoursePercent() {
        return coursePercent;
    }

    public void setCoursePercent(String coursePercent) {
        this.coursePercent = coursePercent;
    }

    public Long getTotalPlayTime() {
        return totalPlayTime;
    }

    public void setTotalPlayTime(Long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    @Override
    public String toString() {
        return "Score{" +
                "learnBehaviorScore=" + learnBehaviorScore +
                ", usualScore=" + usualScore +
                ", finalScore=" + finalScore +
                ", totalScore=" + totalScore +
                ", coursePercent='" + coursePercent + '\'' +
                ", totalPlayTime=" + totalPlayTime +
                '}';
    }
}
