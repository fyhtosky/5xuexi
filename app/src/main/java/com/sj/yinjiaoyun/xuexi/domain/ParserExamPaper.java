package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/30.
 * 课程详情 作业测试
 */
public class ParserExamPaper {

    Integer status;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParserExamPaperData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public ParserExamPaperData getData() {
        return data;
    }

    public void setData(ParserExamPaperData data) {
        this.data = data;
    }
}
