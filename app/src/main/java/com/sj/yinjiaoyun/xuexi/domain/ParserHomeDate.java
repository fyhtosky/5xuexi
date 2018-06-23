package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/6.
 * 首页轮播图
 */
public class ParserHomeDate {

    Map<String,String> carouselPhoto;
    List<OpenCourse> openCourse;
    List<Training>  training;

    public Map<String, String> getCarouselPhoto() {
        return carouselPhoto;
    }

    public void setCarouselPhoto(Map<String, String> carouselPhoto) {
        this.carouselPhoto = carouselPhoto;
    }

    public List<OpenCourse> getOpenCourse() {
        return openCourse;
    }

    public void setOpenCourse(List<OpenCourse> openCourse) {
        this.openCourse = openCourse;
    }

    public List<Training> getTraining() {
        return training;
    }

    public void setTraining(List<Training> training) {
        this.training = training;
    }
}
