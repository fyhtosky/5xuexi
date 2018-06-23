package com.sj.yinjiaoyun.xuexi.domains;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/7.
 * 搜索解析
 */
public class ParseSearchData {
    int total;
    int  pageSize;
    int pageNo;
    List<Fuse> rows;

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

    public List<Fuse> getRows() {
        return rows;
    }

    public void setRows(List<Fuse> rows) {
        this.rows = rows;
    }
}
