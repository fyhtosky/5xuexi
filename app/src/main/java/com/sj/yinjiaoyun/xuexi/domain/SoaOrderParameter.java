package com.sj.yinjiaoyun.xuexi.domain;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/14.
 * 我的订单 解析封装类 中层
 */
public class SoaOrderParameter {

    int total;//订单总条数（该用户所有的订单数，并不是显示了多少条）
    List<Rows> rows;//订单信息

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }

}
