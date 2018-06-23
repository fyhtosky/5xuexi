package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/30.
 * 作业考试 题目解析 每一个章节对应的题目
 */
public class ExamPaper{

    Byte type;               //	是（1：章节作业，2：章节测试，3：结业考试）
    Integer finishTime;      //	Integer	是	试卷完成时间
    Byte isLimitFinishTime;  //	Byte	是	是否限时完成  0 不限制
    Byte isCorrect;          // 状态   完成作业状态  0：没有作业  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期
    Long startTime;        // 开始考试时间
    Long endTime;          // 结束考试时间
    String score;      //试卷的总分
    ExamPaperVO examPaperVO; //	是	试卷vo

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public Byte getIsLimitFinishTime() {
        return isLimitFinishTime;
    }

    public void setIsLimitFinishTime(Byte isLimitFinishTime) {
        this.isLimitFinishTime = isLimitFinishTime;
    }

    public Byte getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Byte isCorrect) {
        this.isCorrect = isCorrect;
    }

    public ExamPaperVO getExamPaperVO() {
        return examPaperVO;
    }

    public void setExamPaperVO(ExamPaperVO examPaperVO) {
        this.examPaperVO = examPaperVO;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
