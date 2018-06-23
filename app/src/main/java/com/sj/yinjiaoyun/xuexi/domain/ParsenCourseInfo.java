package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/30.
 * 课程表 - 专业 -视频详情- 简介接口
 * 接口7
 * http://139.196.255.175:8083/api/v2/course/findCourseInfoByCourseScheduleId.action?id=566
 */
public class ParsenCourseInfo {
    Integer state;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParseCourseInfoDate data;//课程信息

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

    public ParseCourseInfoDate getData() {
        return data;
    }

    public void setData(ParseCourseInfoDate data) {
        this.data = data;
    }
}
