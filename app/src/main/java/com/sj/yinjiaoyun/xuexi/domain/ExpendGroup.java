package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/30.
 * 课程表  点击视屏item 详情页的信息
 */
public class ExpendGroup {
    String groupName;
    List<Coursewares> expendChild;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Coursewares> getExpendChild() {
        return expendChild;
    }

    public void setExpendChild(List<Coursewares> expendChild) {
        this.expendChild = expendChild;
    }
}
