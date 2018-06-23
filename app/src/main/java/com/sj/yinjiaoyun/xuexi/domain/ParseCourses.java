package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/26.
 * 根据招生专业ID+招生计划ID + 用户ID获取课程信息（教学计划信息）
 * 外层
 */
public class ParseCourses {
    Integer state;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParseCoursesDate data;//课程信息

    public ParseCoursesDate getData() {
        return data;
    }

    public void setData(ParseCoursesDate data) {
        this.data = data;
    }

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

}
