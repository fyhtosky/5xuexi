package com.sj.yinjiaoyun.xuexi.bean;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/10/13.
 * 完型填空和阅读理解 题干的实体类
 */

public class QusetionBean {
    private int id;//题目的id
    private int qid;  //题目序号
    private int questionType;//题目类型
    private String questionTitle;//题干
    private int score;//该题目的分数
    private String value;//多选题的答案
    List<String> questionAnswerList;        //问题正确答案json串
    private String studentAnswer;           //学生的所选答案的作答答案json串
    private String questionAnalysis;        //答案解析
    private List<QusetionOptionBean>optionBeanList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getQuestionAnswerList() {
        return questionAnswerList;
    }

    public void setQuestionAnswerList(List<String> questionAnswerList) {
        this.questionAnswerList = questionAnswerList;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getQuestionAnalysis() {
        return questionAnalysis;
    }

    public void setQuestionAnalysis(String questionAnalysis) {
        this.questionAnalysis = questionAnalysis;
    }

    public List<QusetionOptionBean> getOptionBeanList() {
        return optionBeanList;
    }

    public void setOptionBeanList(List<QusetionOptionBean> optionBeanList) {
        this.optionBeanList = optionBeanList;
    }

    @Override
    public String toString() {
        return "QusetionBean{" +
                "id=" + id +
                ", qid=" + qid +
                ", questionType=" + questionType +
                ", questionTitle='" + questionTitle + '\'' +
                ", score=" + score +
                ", value='" + value + '\'' +
                ", questionAnswerList=" + questionAnswerList +
                ", studentAnswer='" + studentAnswer + '\'' +
                ", questionAnalysis='" + questionAnalysis + '\'' +
                ", optionBeanList=" + optionBeanList +
                '}';
    }
}
