package com.sj.yinjiaoyun.xuexi.domains;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/21.
 * 判断课程（学历课程 公开课 微专业） 是否购买有
 */
public class CouseIsHave {

    Integer state;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    ParseCouseIsHave data;

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

    public ParseCouseIsHave getData() {
        return data;
    }

    public void setData(ParseCouseIsHave data) {
        this.data = data;
    }
}
