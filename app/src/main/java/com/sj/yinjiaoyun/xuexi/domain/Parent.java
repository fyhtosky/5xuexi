package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/28.
 * 课程表 列表 ExpandableListView 填充的父控件封装类  组
 */
public class Parent {
    String parentName;//父控件的名字
    Boolean state;//该课程是否付费
    List<CourseVO> child;//父控件对应的子控件的课程

    public Parent(String parentName, Boolean state, List<CourseVO> child) {
        this.parentName = parentName;
        this.state = state;
        this.child = child;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public List<CourseVO> getChild() {
        return child;
    }

    public void setChild(List<CourseVO> child) {
        this.child = child;
    }
}
