package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/14.
 *我的订单 解析封装类 外层
 */
public class ParserOrderDate {

    SoaOrderParameter soaOrderParameter;
    int pageSize; //分页index，默认1
    int pageNo;  //当前页码

    public SoaOrderParameter getSoaOrderParameter() {
        return soaOrderParameter;
    }

    public void setSoaOrderParameter(SoaOrderParameter soaOrderParameter) {
        this.soaOrderParameter = soaOrderParameter;
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
}
