package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/17.
 * 根据用户名获取我的微专业   36.2
 */
public class ParseMicroTraning {

    Integer state;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParseTraningData data;

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

    public ParseTraningData getData() {
        return data;
    }

    public void setData(ParseTraningData data) {
        this.data = data;
    }
}
