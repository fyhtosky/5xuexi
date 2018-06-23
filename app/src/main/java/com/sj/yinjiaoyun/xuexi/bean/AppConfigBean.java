package com.sj.yinjiaoyun.xuexi.bean;

/**
 * 作者：VanHua on 2018/5/22 10:27
 * <p>
 * 邮箱：Van_Hua@qq.com
 */
public class AppConfigBean {

    /**
     * state : 30001
     * success : false
     * message : 版本过低，需要升级
     * data : {"properties":{"tigaseUrl":null,"picroomUrl":null,"ixueUrl":null,"xuexiUrl":null,"versionDesc":null,"version":"10","updateUrl":"127.0.0.1:80/android/5xuexi.apk"}}
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
         * properties : {"tigaseUrl":null,"picroomUrl":null,"ixueUrl":null,"xuexiUrl":null,"versionDesc":null,"version":"10","updateUrl":"127.0.0.1:80/android/5xuexi.apk"}
         */

        private PropertiesBean properties;

        public PropertiesBean getProperties() {
            return properties;
        }

        public void setProperties(PropertiesBean properties) {
            this.properties = properties;
        }

        public static class PropertiesBean {
            /**
             * tigaseUrl : null
             * picroomUrl : null
             * ixueUrl : null
             * xuexiUrl : null
             * versionDesc : null
             * version : 10
             * updateUrl : 127.0.0.1:80/android/5xuexi.apk
             */

            private String tigaseUrl;
            private String picroomUrl;
            private String ixueUrl;
            private String xuexiUrl;
            private String versionDesc;
            private String version;
            private String updateUrl;

            public String getTigaseUrl() {
                return tigaseUrl;
            }

            public void setTigaseUrl(String tigaseUrl) {
                this.tigaseUrl = tigaseUrl;
            }

            public String getPicroomUrl() {
                return picroomUrl;
            }

            public void setPicroomUrl(String picroomUrl) {
                this.picroomUrl = picroomUrl;
            }

            public String getIxueUrl() {
                return ixueUrl;
            }

            public void setIxueUrl(String ixueUrl) {
                this.ixueUrl = ixueUrl;
            }

            public String getXuexiUrl() {
                return xuexiUrl;
            }

            public void setXuexiUrl(String xuexiUrl) {
                this.xuexiUrl = xuexiUrl;
            }

            public String getVersionDesc() {
                return versionDesc;
            }

            public void setVersionDesc(String versionDesc) {
                this.versionDesc = versionDesc;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getUpdateUrl() {
                return updateUrl;
            }

            public void setUpdateUrl(String updateUrl) {
                this.updateUrl = updateUrl;
            }
        }
    }
}
