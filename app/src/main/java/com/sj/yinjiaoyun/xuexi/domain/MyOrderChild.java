package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/24.
 * 我的订单里面   adapter信息封装类 子订单
 */
public class MyOrderChild {

    String cOrderCode;//订单号
    Double cAmount;//价钱
    int cSchoolYear;//学期

    public MyOrderChild(String cOrderCode, Double cAmount, int cSchoolYear) {
        this.cOrderCode = cOrderCode;
        this.cAmount = cAmount;
        this.cSchoolYear = cSchoolYear;
    }

    public int getcSchoolYear() {
        return cSchoolYear;
    }

    public void setcSchoolYear(int cSchoolYear) {
        this.cSchoolYear = cSchoolYear;
    }

    public String getcOrderCode() {
        return cOrderCode;
    }

    public void setcOrderCode(String cOrderCode) {
        this.cOrderCode = cOrderCode;
    }

    public Double getcAmount() {
        return cAmount;
    }

    public void setcAmount(Double cAmount) {
        this.cAmount = cAmount;
    }

    @Override
    public String toString() {
        return "MyOrderChild{" +
                "cOrderCode='" + cOrderCode + '\'' +
                ", cAmount='" + cAmount + '\'' +
                ", cSchoolYear=" + cSchoolYear +
                '}';
    }
}
