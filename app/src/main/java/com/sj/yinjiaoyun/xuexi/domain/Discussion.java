package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/4.
 * 答疑 解析
 *  http://139.196.255.175:8083/api/v2/learn/findDiscussionOnline.action?coursewareId=0&teachingPlanId=0&page=1&rows=20
 */
public class Discussion {

    int total;//总共多少条
    int pageSize;//当前每页显示多少条
    int pageNo;//当前页面下表
    List<DaYi> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<DaYi> getRows() {
        return rows;
    }

    public void setRows(List<DaYi> rows) {
        this.rows = rows;
    }
}
