package com.sj.yinjiaoyun.xuexi.bean;

import java.io.Serializable;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/4/19.
 */
public class ImageBean implements Serializable{
    private String url;
    private Long id;

    public ImageBean() {
    }

    public ImageBean(String url, Long id) {
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "url='" + url + '\'' +
                ", id=" + id +
                '}';
    }
}
