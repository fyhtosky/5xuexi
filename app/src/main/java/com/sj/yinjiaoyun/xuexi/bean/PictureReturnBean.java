package com.sj.yinjiaoyun.xuexi.bean;

/**
 * Created by wanzhiying on 2017/3/21.
 * 上传图片回调的实体类封装
 */
public class PictureReturnBean {

    /**
     * state : 1
     * success : true
     * message :
     * data : {"url":"http://d1.5xuexi.com/group1/M00/00/0E/i8T_r1jQmKyAJsbDAAAc-4Hc9c8822.png"}
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
         * url : http://d1.5xuexi.com/group1/M00/00/0E/i8T_r1jQmKyAJsbDAAAc-4Hc9c8822.png
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

//    @Override
//    public String toString() {
//        return "PictureReturnBean{" +
//                "state=" + state +
//                ", success=" + success +
//                ", message='" + message + '\'' +
//                ", data=" + data +
//                '}';
//    }
}
