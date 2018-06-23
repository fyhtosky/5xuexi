package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/11.
 * 课程表  课程选择  expandableListView封装类
 */
public class KecParent {
    String xueQi;
    List<Kec> childList;

    public KecParent(String xueQi, List<Kec> childList) {
        this.xueQi = xueQi;
        this.childList = childList;
    }

    public String getXueQi() {
        return xueQi;
    }

    public void setXueQi(String xueQi) {
        this.xueQi = xueQi;
    }

    public List<Kec> getChildList() {
        return childList;
    }

    public void setChildList(List<Kec> childList) {
        this.childList = childList;
    }
}
