package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/30.
 * 接口9.课程详情-章节课时列表及学习情况
 */
public class ParseCouseWares {
    Integer state;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParseCourseWaresDate data;//课程信息

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

    public ParseCourseWaresDate getData() {
        return data;
    }

    public void setData(ParseCourseWaresDate data) {
        this.data = data;
    }
}
