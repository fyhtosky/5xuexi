package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/30.
 * 作业考试 题目解析 每一个章节对应的题目
 */
public class ExamPaperVO {

    Long id;//试卷id
    String collegeName;//院校
    String courseName;//课程
    String groupName;//章
    String coursewareName;//节
    Long coursewareId; //
    String tatolCount; //题目个数
    String scoreSum; //试卷的总分数
    String examTitle; //试卷标题
    int examCostTime; //考试限时时间
    Byte isNeedCorrect; // 是否需要手动修改 即是否有主观题 0 ：不需要 1：需要
    List<SoaExamPaper> examPaperItemList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCoursewareName() {
        return coursewareName;
    }

    public void setCoursewareName(String coursewareName) {
        this.coursewareName = coursewareName;
    }

    public Long getCoursewareId() {
        return coursewareId;
    }

    public void setCoursewareId(Long coursewareId) {
        this.coursewareId = coursewareId;
    }

    public String getTatolCount() {
        return tatolCount;
    }

    public void setTatolCount(String tatolCount) {
        this.tatolCount = tatolCount;
    }

    public String getScoreSum() {
        return scoreSum;
    }

    public void setScoreSum(String scoreSum) {
        this.scoreSum = scoreSum;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public int getExamCostTime() {
        return examCostTime;
    }

    public void setExamCostTime(int examCostTime) {
        this.examCostTime = examCostTime;
    }

    public Byte getIsNeedCorrect() {
        return isNeedCorrect;
    }

    public void setIsNeedCorrect(Byte isNeedCorrect) {
        this.isNeedCorrect = isNeedCorrect;
    }

    public List<SoaExamPaper> getExamPaperItemList() {
        return examPaperItemList;
    }

    public void setExamPaperItemList(List<SoaExamPaper> examPaperItemList) {
        this.examPaperItemList = examPaperItemList;
    }
}
