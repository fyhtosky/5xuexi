package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/30.
 * 作业考试 题目解析 每一个章节对应的题目
 */
public class Question {
    long id;
    int courseCode;//
    String courseName;//
    String groupName;//
    String coursewareName;//
    List<String> questionOptionsList;//
    List<String> questionAnswerList;//答案集合
    Long coursewareId;
    Byte questionType;// 题目类型1单选   2多选  3判断 4主观题
    String questionTitle;// 题目
    String questionOptions;// 内容选项
    String questionAnswer; //问题答案集合的json串
    String questionAnalysis;//
    int score;                      //本题总分
    String answer;//学生答案
    int stuScore;//学生得分
    //多级题目的集合
    List<Question>children;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
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

    public List<String> getQuestionOptionsList() {
        return questionOptionsList;
    }

    public void setQuestionOptionsList(List<String> questionOptionsList) {
        this.questionOptionsList = questionOptionsList;
    }

    public List<String> getQuestionAnswerList() {
        return questionAnswerList;
    }

    public void setQuestionAnswerList(List<String> questionAnswerList) {
        this.questionAnswerList = questionAnswerList;
    }

    public Long getCoursewareId() {
        return coursewareId;
    }

    public void setCoursewareId(Long coursewareId) {
        this.coursewareId = coursewareId;
    }

    public Byte getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Byte questionType) {
        this.questionType = questionType;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(String questionOptions) {
        this.questionOptions = questionOptions;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getQuestionAnalysis() {
        return questionAnalysis;
    }

    public void setQuestionAnalysis(String questionAnalysis) {
        this.questionAnalysis = questionAnalysis;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getStuScore() {
        return stuScore;
    }

    public void setStuScore(int stuScore) {
        this.stuScore = stuScore;
    }

    public List<Question> getChildren() {
        return children;
    }

    public void setChildren(List<Question> children) {
        this.children = children;
    }
}
