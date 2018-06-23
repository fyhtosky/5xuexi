package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/17.
 * 课程表 微专业  顶部tab对应的列表数据解析
 */
public class ParseMicroCourse {

    Integer state;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParseMicroCourseData data;

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

    public ParseMicroCourseData getData() {
        return data;
    }

    public void setData(ParseMicroCourseData data) {
        this.data = data;
    }
}
