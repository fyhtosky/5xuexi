package com.sj.yinjiaoyun.xuexi.bean;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/10/13.
 * 完型填空和阅读理解的选项封装的实体类
 */
public class QusetionOptionBean {
    private int id;
    private String optionTitle;
    private boolean isSelected;
    private int questionType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    @Override
    public String toString() {
        return "QusetionOptionBean{" +
                "id=" + id +
                ", optionTitle='" + optionTitle + '\'' +
                ", isSelected=" + isSelected +
                ", questionType=" + questionType +
                '}';
    }
}
