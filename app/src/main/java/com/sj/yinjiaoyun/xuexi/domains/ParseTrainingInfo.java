package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/14.
 * 解析 微专业预览简介
 */
public class ParseTrainingInfo {

    Integer state;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParseTrainingInfoData data;

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

    public ParseTrainingInfoData getData() {
        return data;
    }

    public void setData(ParseTrainingInfoData data) {
        this.data = data;
    }
}
