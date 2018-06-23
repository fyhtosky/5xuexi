package com.sj.yinjiaoyun.xuexi.bean;

/**
 * Created by wanzhiying on 2017/3/8.
 */
public class Test {


   private Integer state;//状态码
   private Boolean success;//True 可以使用  false 不可以使用
   private String message;//操作信息
   private DataBean data;//课程信息

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

}
