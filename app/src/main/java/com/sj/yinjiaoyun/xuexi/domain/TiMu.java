package com.sj.yinjiaoyun.xuexi.domain;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/1.
 * 填充 题目的封装类
 */
public class TiMu  implements Parcelable{

    int index;  //题目下标的索引
    Integer jobState;               //作业或者考试 完成状态 0：没有测验  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期");
    int qid;                        //题目序号
    String id;                      //题目id
    Byte questionType;              // 题目类型  1单选   2多选  3判断  4主观题
    int score;                      //本题总分
    String questionTitle;           //题目
    String questionOptions;         //题目答案选项
    List<String> questionAnswerList; //问题正确答案集合
    String questionAnswer;          //问题正确答案json串
    String studentAnswer;           //学生的所选答案的作答答案json串
    String questionAnalysis;        //答案解析
    int stuScore;                   //此题学生得分
    int answerFlag;                 // 1表示正确 2 表示错误，0 表示未做
    List<TiMu> childrenList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeValue(this.jobState);
        dest.writeInt(this.qid);
        dest.writeString(this.id);
        dest.writeValue(this.questionType);
        dest.writeInt(this.score);
        dest.writeString(this.questionTitle);
        dest.writeString(this.questionOptions);
        dest.writeStringList(this.questionAnswerList);
        dest.writeString(this.questionAnswer);
        dest.writeString(this.studentAnswer);
        dest.writeString(this.questionAnalysis);
        dest.writeInt(this.stuScore);
        dest.writeInt(this.answerFlag);
        dest.writeTypedList(this.childrenList);
    }

    public TiMu() {
    }

    protected TiMu(Parcel in) {
        this.index = in.readInt();
        this.jobState = (Integer) in.readValue(Integer.class.getClassLoader());
        this.qid = in.readInt();
        this.id = in.readString();
        this.questionType = (Byte) in.readValue(Byte.class.getClassLoader());
        this.score = in.readInt();
        this.questionTitle = in.readString();
        this.questionOptions = in.readString();
        this.questionAnswerList = in.createStringArrayList();
        this.questionAnswer = in.readString();
        this.studentAnswer = in.readString();
        this.questionAnalysis = in.readString();
        this.stuScore = in.readInt();
        this.answerFlag = in.readInt();
        this.childrenList = in.createTypedArrayList(TiMu.CREATOR);
    }

    public static final Creator<TiMu> CREATOR = new Creator<TiMu>() {
        @Override
        public TiMu createFromParcel(Parcel source) {
            return new TiMu(source);
        }

        @Override
        public TiMu[] newArray(int size) {
            return new TiMu[size];
        }
    };

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Integer getJobState() {
        return jobState;
    }

    public void setJobState(Integer jobState) {
        this.jobState = jobState;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Byte getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Byte questionType) {
        this.questionType = questionType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public List<String> getQuestionAnswerList() {
        return questionAnswerList;
    }

    public void setQuestionAnswerList(List<String> questionAnswerList) {
        this.questionAnswerList = questionAnswerList;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
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

    public int getStuScore() {
        return stuScore;
    }

    public void setStuScore(int stuScore) {
        this.stuScore = stuScore;
    }

    public int getAnswerFlag() {
        return answerFlag;
    }

    public void setAnswerFlag(int answerFlag) {
        this.answerFlag = answerFlag;
    }

    public List<TiMu> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<TiMu> childrenList) {
        this.childrenList = childrenList;
    }

    @Override
    public String toString() {
        return "TiMu{" +
                "index=" + index +
                ", jobState=" + jobState +
                ", qid=" + qid +
                ", id='" + id + '\'' +
                ", questionType=" + questionType +
                ", score=" + score +
                ", questionTitle='" + questionTitle + '\'' +
                ", questionOptions='" + questionOptions + '\'' +
                ", questionAnswerList=" + questionAnswerList +
                ", questionAnswer='" + questionAnswer + '\'' +
                ", studentAnswer='" + studentAnswer + '\'' +
                ", questionAnalysis='" + questionAnalysis + '\'' +
                ", stuScore=" + stuScore +
                ", answerFlag=" + answerFlag +
                ", childrenList=" + childrenList +
                '}';
    }
}
