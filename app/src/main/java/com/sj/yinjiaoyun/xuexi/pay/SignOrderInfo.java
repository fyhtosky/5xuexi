package com.sj.yinjiaoyun.xuexi.pay;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/1/6.
 * 去支付包支付前 加签前 封装类
 */
public class SignOrderInfo implements Parcelable{

    String orderCode;
    String endUserId;//用户id
    String amount;//订单金额

    /**
     *
     * @param orderCode  订单号
     * @param endUserId   用户id
     * @param amount     订单金额
     */
    public SignOrderInfo(String orderCode, String endUserId, String amount) {
        this.orderCode = orderCode;
        this.endUserId = endUserId;
        this.amount = amount;
    }

    protected SignOrderInfo(Parcel in) {
        orderCode = in.readString();
        endUserId = in.readString();
        amount = in.readString();
    }

    public static final Creator<SignOrderInfo> CREATOR = new Creator<SignOrderInfo>() {
        @Override
        public SignOrderInfo createFromParcel(Parcel in) {
            return new SignOrderInfo(in);
        }

        @Override
        public SignOrderInfo[] newArray(int size) {
            return new SignOrderInfo[size];
        }
    };

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderCode);
        dest.writeString(endUserId);
        dest.writeString(amount);
    }

    @Override
    public String toString() {
        return " 加签前订单信息封装类OrderInfo{" +
                "orderCode='" + orderCode + '\'' +
                ", endUserId='" + endUserId + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
