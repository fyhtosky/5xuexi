package com.sj.yinjiaoyun.xuexi.bean;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/11/22.
 */
public class OrderReturnBean {

    /**
     * state : 1
     * success : true
     * message :
     * data : {"orderCode":"201711224334084","courseScheduleId":3170}
     */

    private int state;
    private boolean success;
    private String message;
    private DataBean data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * orderCode : 201711224334084
         * courseScheduleId : 3170
         */

        private String orderCode;
        private int courseScheduleId;

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public int getCourseScheduleId() {
            return courseScheduleId;
        }

        public void setCourseScheduleId(int courseScheduleId) {
            this.courseScheduleId = courseScheduleId;
        }
    }
}
