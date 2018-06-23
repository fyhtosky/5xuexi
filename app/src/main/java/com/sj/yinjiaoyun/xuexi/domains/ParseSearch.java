package com.sj.yinjiaoyun.xuexi.domains;

import com.sj.yinjiaoyun.xuexi.domain.SearchData;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/7.
 * 搜索解析
 */
public class ParseSearch {

    Integer state;//状态码
    Boolean success;//True 可以使用  false 不可以使用
    String message;//操作信息
    SearchData data;

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

    public SearchData getData() {
        return data;
    }

    public void setData(SearchData data) {
        this.data = data;
    }
}
