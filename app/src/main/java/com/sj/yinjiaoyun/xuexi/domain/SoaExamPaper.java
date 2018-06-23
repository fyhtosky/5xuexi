package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/30.
 * 作业考试 题目解析 每一个章节对应的题目
 */
public class SoaExamPaper {
    Long id;//题目id
    Long examPaperId;//   试卷id
    long questionType;//题目类型
    Long questionId;//
    Long sortId;//
    Question question;//题目


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(Long examPaperId) {
        this.examPaperId = examPaperId;
    }

    public long getQuestionType() {
        return questionType;
    }

    public void setQuestionType(long questionType) {
        this.questionType = questionType;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getSortId() {
        return sortId;
    }

    public void setSortId(Long sortId) {
        this.sortId = sortId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
