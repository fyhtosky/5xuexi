package com.sj.yinjiaoyun.xuexi.bean;

import java.util.List;

/**
 * Created by wanzhiying on 2017/3/8.
 */
public class DataBean {

    List<TigaseGroups> tigaseGroups;

    public List<TigaseGroups> getTigaseGroups() {
        return tigaseGroups;
    }

    public void setTigaseGroups(List<TigaseGroups> tigaseGroups) {
        this.tigaseGroups = tigaseGroups;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "tigaseGroups=" + tigaseGroups +
                '}';
    }
}
