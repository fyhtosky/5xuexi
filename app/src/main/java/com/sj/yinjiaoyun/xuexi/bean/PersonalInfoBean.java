package com.sj.yinjiaoyun.xuexi.bean;

/**
 * Created by wanzhiying on 2017/3/16.
 */
public class PersonalInfoBean {


    /**
     * state : 200
     * success : true
     * message : 操作成功
     * data : {"user":{"id":1183,"jid":"5f_3435","userId":3435,"userType":0,"createTime":1489635755000,"msgName":"李小君","msgLogo":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","backendOperatorVO":null,"endUserVO":{"id":3435,"endUserId":null,"userName":"小君001","realName":"李小君","sex":2,"email":"0000@qq.com","phone":"18971552234","idCard":"421088199206064532","middleSchoolCertificate":"765754675","collegeSpecializCertificate":"84675476571","collegeSchoolCertificate":"432421423","bachelorCertificate":"","address":"","userImg":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""}}}
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
         * user : {"id":1183,"jid":"5f_3435","userId":3435,"userType":0,"createTime":1489635755000,"msgName":"李小君","msgLogo":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","backendOperatorVO":null,"endUserVO":{"id":3435,"endUserId":null,"userName":"小君001","realName":"李小君","sex":2,"email":"0000@qq.com","phone":"18971552234","idCard":"421088199206064532","middleSchoolCertificate":"765754675","collegeSpecializCertificate":"84675476571","collegeSchoolCertificate":"432421423","bachelorCertificate":"","address":"","userImg":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""}}
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
             * id : 1183
             * jid : 5f_3435
             * userId : 3435
             * userType : 0
             * createTime : 1489635755000
             * msgName : 李小君
             * msgLogo : http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg
             * backendOperatorVO : null
             * endUserVO : {"id":3435,"endUserId":null,"userName":"小君001","realName":"李小君","sex":2,"email":"0000@qq.com","phone":"18971552234","idCard":"421088199206064532","middleSchoolCertificate":"765754675","collegeSpecializCertificate":"84675476571","collegeSchoolCertificate":"432421423","bachelorCertificate":"","address":"","userImg":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""}
             */

            private int id;
            private String jid;
            private int userId;
            private int userType;
            private long createTime;
            private String msgName;
            private String msgLogo;
            private BackendOperatorVOBean backendOperatorVO;
            private EndUserVOBean endUserVO;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getJid() {
                return jid;
            }

            public void setJid(String jid) {
                this.jid = jid;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getMsgName() {
                return msgName;
            }

            public void setMsgName(String msgName) {
                this.msgName = msgName;
            }

            public String getMsgLogo() {
                return msgLogo;
            }

            public void setMsgLogo(String msgLogo) {
                this.msgLogo = msgLogo;
            }

            public BackendOperatorVOBean getBackendOperatorVO() {
                return backendOperatorVO;
            }

            public void setBackendOperatorVO(BackendOperatorVOBean backendOperatorVO) {
                this.backendOperatorVO = backendOperatorVO;
            }

            public EndUserVOBean getEndUserVO() {
                return endUserVO;
            }

            public void setEndUserVO(EndUserVOBean endUserVO) {
                this.endUserVO = endUserVO;
            }

            public static class BackendOperatorVOBean {
                /**
                 * id : 95
                 * userName : wkd_zp
                 * realName : 张萍
                 * photo : http://139.196.255.175/group1/M00/00/01/i8T_r1eeoh-AbJHNAAATG5BfvUo915.jpg
                 * password :
                 */

                private int id;
                private String userName;
                private String realName;
                private String photo;
                private String password;

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

                public String getRealName() {
                    return realName;
                }

                public void setRealName(String realName) {
                    this.realName = realName;
                }

                public String getPhoto() {
                    return photo;
                }

                public void setPhoto(String photo) {
                    this.photo = photo;
                }

                public String getPassword() {
                    return password;
                }

                public void setPassword(String password) {
                    this.password = password;
                }
            }
            public static class EndUserVOBean {
                /**
                 * id : 3435
                 * endUserId : null
                 * userName : 小君001
                 * realName : 李小君
                 * sex : 2
                 * email : 0000@qq.com
                 * phone : 18971552234
                 * idCard : 421088199206064532
                 * middleSchoolCertificate : 765754675
                 * collegeSpecializCertificate : 84675476571
                 * collegeSchoolCertificate : 432421423
                 * bachelorCertificate :
                 * address :
                 * userImg : http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg
                 * politicsStatus :
                 * nation :
                 * fixPhone :
                 * postalCode :
                 */

                private int id;
                private String endUserId;
                private String userName;
                private String realName;
                private int sex;
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

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getEndUserId() {
                    return endUserId;
                }

                public void setEndUserId(String endUserId) {
                    this.endUserId = endUserId;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }

                public String getRealName() {
                    return realName;
                }

                public void setRealName(String realName) {
                    this.realName = realName;
                }

                public int getSex() {
                    return sex;
                }

                public void setSex(int sex) {
                    this.sex = sex;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getIdCard() {
                    return idCard;
                }

                public void setIdCard(String idCard) {
                    this.idCard = idCard;
                }

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
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
            }
        }
    }
}
