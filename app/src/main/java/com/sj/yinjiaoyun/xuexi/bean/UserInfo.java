package com.sj.yinjiaoyun.xuexi.bean;

/**
 * Created by Administrator on 2018/2/6.
 */

public class UserInfo {

    /**
     * state : 200
     * success : true
     * message : 操作成功
     * data : {"user":{"id":31950,"endUserId":31950,"userName":"kHDhxe0Z167","password":"96e79218965eb72c92a549dd5a330112","realName":null,"sex":null,"email":null,"phone":"13437199971","idCard":null,"middleSchoolCertificate":null,"collegeSpecializCertificate":null,"collegeSchoolCertificate":null,"bachelorCertificate":null,"address":null,"userImg":"http://139.196.255.175/group1/M00/00/64/i8T_r1plf5CAGdVNAAA1M3Td4xo087.png","politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null,"token":"f8a1e022-17d6-4e0a-bb42-0885e521db43"}}
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
         * user : {"id":31950,"endUserId":31950,"userName":"kHDhxe0Z167","password":"96e79218965eb72c92a549dd5a330112","realName":null,"sex":null,"email":null,"phone":"13437199971","idCard":null,"middleSchoolCertificate":null,"collegeSpecializCertificate":null,"collegeSchoolCertificate":null,"bachelorCertificate":null,"address":null,"userImg":"http://139.196.255.175/group1/M00/00/64/i8T_r1plf5CAGdVNAAA1M3Td4xo087.png","politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null,"token":"f8a1e022-17d6-4e0a-bb42-0885e521db43"}
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
             * id : 31950
             * endUserId : 31950
             * userName : kHDhxe0Z167
             * password : 96e79218965eb72c92a549dd5a330112
             * realName : null
             * sex : null
             * email : null
             * phone : 13437199971
             * idCard : null
             * middleSchoolCertificate : null
             * collegeSpecializCertificate : null
             * collegeSchoolCertificate : null
             * bachelorCertificate : null
             * address : null
             * userImg : http://139.196.255.175/group1/M00/00/64/i8T_r1plf5CAGdVNAAA1M3Td4xo087.png
             * politicsStatus : null
             * nation : null
             * fixPhone : null
             * postalCode : null
             * token : f8a1e022-17d6-4e0a-bb42-0885e521db43
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
