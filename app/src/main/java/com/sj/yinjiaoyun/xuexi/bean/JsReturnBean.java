package com.sj.yinjiaoyun.xuexi.bean;

/**
 * Created by Administrator on 2018/1/11.
 * js交互回调的结果
 */

public class JsReturnBean {

    /**
     * status : 200
     * data :
     */

    private int status;
    private String data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
