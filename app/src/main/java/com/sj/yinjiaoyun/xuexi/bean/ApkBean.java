package com.sj.yinjiaoyun.xuexi.bean;

/**
 * 作者：VanHua on 2018/5/22 15:15
 * <p>
 * 邮箱：Van_Hua@qq.com
 */
public class ApkBean {

    /**
     * state : null
     * success : true
     * message : null
     * data : {"url":"http://139.196.255.175:8082/statics/android_app/5xuexi.apk","version":"10"}
     */

    private String state;
    private boolean success;
    private String message;
    private DataBean data;

    public String getState() {
        return state;
    }

    public void setState(String state) {
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
         * url : http://139.196.255.175:8082/statics/android_app/5xuexi.apk
         * version : 10
         */

        private String url;
        private String version;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
