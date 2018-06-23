package com.sj.yinjiaoyun.xuexi.domain;


import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/29.
 * 专业课  公开课 详情 里面的评论接口
 */
public class Comments {

    int total;//总条数
    int pageSize;//每一页的大小
    int pageNo;//当前页数
    List<CoRows> rows;

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

    public List<CoRows> getRows() {
        return rows;
    }

    public void setRows(List<CoRows> rows) {
        this.rows = rows;
    }
}
