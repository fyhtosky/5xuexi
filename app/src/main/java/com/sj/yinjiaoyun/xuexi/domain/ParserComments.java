package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/29.
 * 专业课  公开课 详情 里面的评论接口
 */
public class ParserComments {

    Integer state;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParserCommentData data;

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

    public ParserCommentData getData() {
        return data;
    }

    public void setData(ParserCommentData data) {
        this.data = data;
    }
}
