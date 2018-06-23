package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/21.
 * 个人信息接口（根据用户ID获取用户个人信息）
 * 外层数据
 */
public class ParseUser {
    Integer status;//状态码
    boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParseDataUser data;

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

    public ParseDataUser getData() {
        return data;
    }

    public void setData(ParseDataUser data) {
        this.data = data;
    }
}
