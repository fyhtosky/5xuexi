package com.sj.yinjiaoyun.xuexi.bean;

/**
 * Created by wanzhiying on 2017/3/14.
 */
public class ReturnBean {


    /**
     * state : 200
     * success : true
     * message : null
     * data : {"hasStudentNumber":true,"user":{"id":3477,"endUserId":null,"userName":"Testtliu18","password":"96e79218965eb72c92a549dd5a330112","realName":"胖橙18","sex":2,"email":"1101111@qq.com","phone":"13437177766","idCard":"420111201611157768","middleSchoolCertificate":"114444","collegeSpecializCertificate":"2","collegeSchoolCertificate":"31","bachelorCertificate":"4","address":"辽宁省 沈阳市 和平区七公主","userImg":"http://139.196.255.175/group1/M00/00/0E/i8T_r1jJ7miAPmEzAAG7ah3PKdI776.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""}}
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
         * hasStudentNumber : true
         * user : {"id":3477,"endUserId":null,"userName":"Testtliu18","password":"96e79218965eb72c92a549dd5a330112","realName":"胖橙18","sex":2,"email":"1101111@qq.com","phone":"13437177766","idCard":"420111201611157768","middleSchoolCertificate":"114444","collegeSpecializCertificate":"2","collegeSchoolCertificate":"31","bachelorCertificate":"4","address":"辽宁省 沈阳市 和平区七公主","userImg":"http://139.196.255.175/group1/M00/00/0E/i8T_r1jJ7miAPmEzAAG7ah3PKdI776.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""}
         */

        private boolean hasStudentNumber;
        private UserBean user;

        public boolean isHasStudentNumber() {
            return hasStudentNumber;
        }

        public void setHasStudentNumber(boolean hasStudentNumber) {
            this.hasStudentNumber = hasStudentNumber;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 3477
             * endUserId : null
             * userName : Testtliu18
             * password : 96e79218965eb72c92a549dd5a330112
             * realName : 胖橙18
             * sex : 2
             * email : 1101111@qq.com
             * phone : 13437177766
             * idCard : 420111201611157768
             * middleSchoolCertificate : 114444
             * collegeSpecializCertificate : 2
             * collegeSchoolCertificate : 31
             * bachelorCertificate : 4
             * address : 辽宁省 沈阳市 和平区七公主
             * userImg : http://139.196.255.175/group1/M00/00/0E/i8T_r1jJ7miAPmEzAAG7ah3PKdI776.jpg
             * politicsStatus :
             * nation :
             * fixPhone :
             * postalCode :
             */

            private int id;
            private int endUserId;
            private String userName;
            private String password;
            private String realName;
            private String nickName;
            private String sex;
            private String email;
            private String phone;
            private String idCard;
            private String middleSchoolCertificate;
            private String collegeSpecializCertificate;
            private String collegeSchoolCertificate;
            private String bachelorCertificate;
            private String address;
            private String userImg;
            private String politicsStatus;
            private String nation;
            private String fixPhone;
            private String postalCode;
            private String token;
            private String qqOpenid;
            private String qqUuid;
            private String qqName;
            private String wxOpenid;
            private String wxUuid;
            private String wxName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getEndUserId() {
                return endUserId;
            }

            public void setEndUserId(int endUserId) {
                this.endUserId = endUserId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
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

            public String getMiddleSchoolCertificate() {
                return middleSchoolCertificate;
            }

            public void setMiddleSchoolCertificate(String middleSchoolCertificate) {
                this.middleSchoolCertificate = middleSchoolCertificate;
            }

            public String getCollegeSpecializCertificate() {
                return collegeSpecializCertificate;
            }

            public void setCollegeSpecializCertificate(String collegeSpecializCertificate) {
                this.collegeSpecializCertificate = collegeSpecializCertificate;
            }

            public String getCollegeSchoolCertificate() {
                return collegeSchoolCertificate;
            }

            public void setCollegeSchoolCertificate(String collegeSchoolCertificate) {
                this.collegeSchoolCertificate = collegeSchoolCertificate;
            }

            public String getBachelorCertificate() {
                return bachelorCertificate;
            }

            public void setBachelorCertificate(String bachelorCertificate) {
                this.bachelorCertificate = bachelorCertificate;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getUserImg() {
                return userImg;
            }

            public void setUserImg(String userImg) {
                this.userImg = userImg;
            }

            public String getPoliticsStatus() {
                return politicsStatus;
            }

            public void setPoliticsStatus(String politicsStatus) {
                this.politicsStatus = politicsStatus;
            }

            public String getNation() {
                return nation;
            }

            public void setNation(String nation) {
                this.nation = nation;
            }

            public String getFixPhone() {
                return fixPhone;
            }

            public void setFixPhone(String fixPhone) {
                this.fixPhone = fixPhone;
            }

            public String getPostalCode() {
                return postalCode;
            }

            public void setPostalCode(String postalCode) {
                this.postalCode = postalCode;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getQqOpenid() {
                return qqOpenid;
            }

            public void setQqOpenid(String qqOpenid) {
                this.qqOpenid = qqOpenid;
            }

            public String getQqUuid() {
                return qqUuid;
            }

            public void setQqUuid(String qqUuid) {
                this.qqUuid = qqUuid;
            }

            public String getQqName() {
                return qqName;
            }

            public void setQqName(String qqName) {
                this.qqName = qqName;
            }

            public String getWxOpenid() {
                return wxOpenid;
            }

            public void setWxOpenid(String wxOpenid) {
                this.wxOpenid = wxOpenid;
            }

            public String getWxUuid() {
                return wxUuid;
            }

            public void setWxUuid(String wxUuid) {
                this.wxUuid = wxUuid;
            }

            public String getWxName() {
                return wxName;
            }

            public void setWxName(String wxName) {
                this.wxName = wxName;
            }
        }
    }
}
