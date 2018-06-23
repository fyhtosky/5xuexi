package com.sj.yinjiaoyun.xuexi.bean;

/**
 * 作者：VanHua on 2018/5/8 10:26
 * <p>
 * 邮箱：1373516909@qq.com
 * 判断第三方账号是否有账号返回的数据
 */
public class CheckInfoBean {


    /**
     * state : 1
     * success : true
     * message :
     * data : {"user":{"id":1975,"userName":"ycTs3uuB258","phone":"","idCard":"","password":"96e79218965eb72c92a549dd5a330112","status":0,"createTime":1525855913000,"updateTime":1525855913000,"registerFrom":1,"qqUuid":"UID_A88498A4B94134C4E58A263F9288E041","qqOpenid":"","qqName":"上海银教云","qqFigureurl":" http://thirdqq.qlogo.cn/qqapp/1105831237/C6DDC0954F53348F34425100819E7993/100","wxUuid":null,"wxOpenid":"","wxName":null,"wxFigureurl":null,"token":"4ebd8491-dfc2-406b-8e68-1158b1431ee2","invalidTime":null,"fromSite":1,"platform":2}}
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
         * user : {"id":1975,"userName":"ycTs3uuB258","phone":"","idCard":"","password":"96e79218965eb72c92a549dd5a330112","status":0,"createTime":1525855913000,"updateTime":1525855913000,"registerFrom":1,"qqUuid":"UID_A88498A4B94134C4E58A263F9288E041","qqOpenid":"","qqName":"上海银教云","qqFigureurl":" http://thirdqq.qlogo.cn/qqapp/1105831237/C6DDC0954F53348F34425100819E7993/100","wxUuid":null,"wxOpenid":"","wxName":null,"wxFigureurl":null,"token":"4ebd8491-dfc2-406b-8e68-1158b1431ee2","invalidTime":null,"fromSite":1,"platform":2}
         */

        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 1975
             * userName : ycTs3uuB258
             * phone :
             * idCard :
             * password : 96e79218965eb72c92a549dd5a330112
             * status : 0
             * createTime : 1525855913000
             * updateTime : 1525855913000
             * registerFrom : 1
             * qqUuid : UID_A88498A4B94134C4E58A263F9288E041
             * qqOpenid :
             * qqName : 上海银教云
             * qqFigureurl :  http://thirdqq.qlogo.cn/qqapp/1105831237/C6DDC0954F53348F34425100819E7993/100
             * wxUuid : null
             * wxOpenid :
             * wxName : null
             * wxFigureurl : null
             * token : 4ebd8491-dfc2-406b-8e68-1158b1431ee2
             * invalidTime : null
             * fromSite : 1
             * platform : 2
             */

            private int id;
            private String userName;
            private String phone;
            private String idCard;
            private String password;
            private int status;
            private long createTime;
            private long updateTime;
            private int registerFrom;
            private String qqUuid;
            private String qqOpenid;
            private String qqName;
            private String qqFigureurl;
            private String wxUuid;
            private String wxOpenid;
            private String wxName;
            private String wxFigureurl;
            private String token;
            private long invalidTime;
            private int fromSite;
            private int platform;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getIdCard() {
                return idCard;
            }

            public void setIdCard(String idCard) {
                this.idCard = idCard;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getRegisterFrom() {
                return registerFrom;
            }

            public void setRegisterFrom(int registerFrom) {
                this.registerFrom = registerFrom;
            }

            public String getQqUuid() {
                return qqUuid;
            }

            public void setQqUuid(String qqUuid) {
                this.qqUuid = qqUuid;
            }

            public String getQqOpenid() {
                return qqOpenid;
            }

            public void setQqOpenid(String qqOpenid) {
                this.qqOpenid = qqOpenid;
            }

            public String getQqName() {
                return qqName;
            }

            public void setQqName(String qqName) {
                this.qqName = qqName;
            }

            public String getQqFigureurl() {
                return qqFigureurl;
            }

            public void setQqFigureurl(String qqFigureurl) {
                this.qqFigureurl = qqFigureurl;
            }

            public String getWxUuid() {
                return wxUuid;
            }

            public void setWxUuid(String wxUuid) {
                this.wxUuid = wxUuid;
            }

            public String getWxOpenid() {
                return wxOpenid;
            }

            public void setWxOpenid(String wxOpenid) {
                this.wxOpenid = wxOpenid;
            }

            public String getWxName() {
                return wxName;
            }

            public void setWxName(String wxName) {
                this.wxName = wxName;
            }

            public String getWxFigureurl() {
                return wxFigureurl;
            }

            public void setWxFigureurl(String wxFigureurl) {
                this.wxFigureurl = wxFigureurl;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public long getInvalidTime() {
                return invalidTime;
            }

            public void setInvalidTime(long invalidTime) {
                this.invalidTime = invalidTime;
            }

            public int getFromSite() {
                return fromSite;
            }

            public void setFromSite(int fromSite) {
                this.fromSite = fromSite;
            }

            public int getPlatform() {
                return platform;
            }

            public void setPlatform(int platform) {
                this.platform = platform;
            }
        }
    }
}
