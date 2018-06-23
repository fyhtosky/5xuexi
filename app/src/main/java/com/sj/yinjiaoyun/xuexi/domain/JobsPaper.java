package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/1.
 * 填充 谋一份试卷的封装类
 */
public class JobsPaper {

    String examTitle;        //试卷名字
    Byte isLimitFinishTime;  //	Byte	是	1是否限时完成  0 不限制
    Long startTime;        // 开始考试时间
    Long endTime;          // 结束考试时间
    Byte isNeedCorrect;      // 是否需要手动修改 即是否有主观题 0 ：不需要 1：需要
    Integer jobState;        //作业或者考试 完成状态  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期");
    Integer finishTime;      //	Integer	是	试卷完成时间
    String courseScheduleId;       //课程表id
    String examPaperReleaseId;     //发布试卷id
    String endUserId;              //用户id
    Byte examPaperReleaseType;      //试卷类型
    String examPaperId;//试卷id
    Integer examCostTime;           //考试所用时间
    int timuSize;//题目总数
    String score;//试卷做完之后的得分
    String scoreSum;//试卷的总分
    List<TiMu> list;//题目集合

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public Byte getIsLimitFinishTime() {
        return isLimitFinishTime;
    }

    public void setIsLimitFinishTime(Byte isLimitFinishTime) {
        this.isLimitFinishTime = isLimitFinishTime;
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

    public Byte getIsNeedCorrect() {
        return isNeedCorrect;
    }

    public void setIsNeedCorrect(Byte isNeedCorrect) {
        this.isNeedCorrect = isNeedCorrect;
    }

    public Integer getJobState() {
        return jobState;
    }

    public void setJobState(Integer jobState) {
        this.jobState = jobState;
    }

    public Integer getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public String getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(String courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public String getExamPaperReleaseId() {
        return examPaperReleaseId;
    }

    public void setExamPaperReleaseId(String examPaperReleaseId) {
        this.examPaperReleaseId = examPaperReleaseId;
    }

    public String getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    public Byte getExamPaperReleaseType() {
        return examPaperReleaseType;
    }

    public void setExamPaperReleaseType(Byte examPaperReleaseType) {
        this.examPaperReleaseType = examPaperReleaseType;
    }

    public String getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(String examPaperId) {
        this.examPaperId = examPaperId;
    }

    public Integer getExamCostTime() {
        return examCostTime;
    }

    public void setExamCostTime(Integer examCostTime) {
        this.examCostTime = examCostTime;
    }

    public int getTimuSize() {
        return timuSize;
    }

    public void setTimuSize(int timuSize) {
        this.timuSize = timuSize;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoreSum() {
        return scoreSum;
    }

    public void setScoreSum(String scoreSum) {
        this.scoreSum = scoreSum;
    }

    public List<TiMu> getList() {
        return list;
    }

    public void setList(List<TiMu> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "JobsPaper{" +
                "examTitle='" + examTitle + '\'' +
                ", isLimitFinishTime=" + isLimitFinishTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isNeedCorrect=" + isNeedCorrect +
                ", jobState=" + jobState +
                ", finishTime=" + finishTime +
                ", courseScheduleId='" + courseScheduleId + '\'' +
                ", examPaperReleaseId='" + examPaperReleaseId + '\'' +
                ", endUserId='" + endUserId + '\'' +
                ", examPaperReleaseType=" + examPaperReleaseType +
                ", examPaperId='" + examPaperId + '\'' +
                ", examCostTime=" + examCostTime +
                ", timuSize=" + timuSize +
                ", score='" + score + '\'' +
                ", scoreSum='" + scoreSum + '\'' +
                ", list=" + list +
                '}';
    }
}
