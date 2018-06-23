package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/18.
 * 消息提醒解 析页面
 *
 */
public class ParserMessage {
    Integer status;//状态码
    boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParserMessageDate data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ParserMessageDate getData() {
        return data;
    }

    public void setData(ParserMessageDate data) {
        this.data = data;
    }
}
