package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/10/13.
 * 接口15    获取所有院校名称和id
 */
public class ParserYuanXiao {
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParserYuanXiaoData data;

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

    public ParserYuanXiaoData getData() {
        return data;
    }

    public void setData(ParserYuanXiaoData data) {
        this.data = data;
    }
}
