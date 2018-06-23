package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/7.
 * 解析学籍信息  外围
 */
public class ParserStudentNumbers {
    Integer state;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParseStudentNumbersDate data;//课程信息

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ParseStudentNumbersDate getData() {
        return data;
    }

    public void setData(ParseStudentNumbersDate data) {
        this.data = data;
    }
}
