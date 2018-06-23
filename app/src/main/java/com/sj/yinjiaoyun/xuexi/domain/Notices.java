package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/18.
 * 消息提醒解 析页面
 */
public class Notices {
    int total;//总共为多少页
    int pageSize;//每一页的条数 每一页数量默认10、
    int pageNo;//当前页面为第几页
    List<NoticesRows> rows;//消息详情

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

    public List<NoticesRows> getRows() {
        return rows;
    }

    public void setRows(List<NoticesRows> rows) {
        this.rows = rows;
    }
}
