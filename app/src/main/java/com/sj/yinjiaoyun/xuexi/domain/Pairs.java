package com.sj.yinjiaoyun.xuexi.domain;

import java.io.File;

/**
 * Created by Administrator on 2016/8/21.
 * 网络请求封装类 键值对
 */
public class Pairs {
    String key;
    String value;
    Long endUserId;
    File myfile;

    public Pairs(String key, Long endUserId) {
        this.key = key;
        this.endUserId = endUserId;
    }

    public Pairs(String key, File myfile) {
        this.key = key;
        this.myfile = myfile;
    }

    public Pairs(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(Long endUserId) {
        this.endUserId = endUserId;
    }

    @Override
    public String toString() {
        return "Pairs{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", endUserId=" + endUserId +
                ", myfile=" + myfile +
                '}';
    }
}
