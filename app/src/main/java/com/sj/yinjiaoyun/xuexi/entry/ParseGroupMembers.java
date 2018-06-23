package com.sj.yinjiaoyun.xuexi.entry;

import java.util.List;

/**
 * Created by wanzhiying on 2017/3/14.
 */
public class ParseGroupMembers {
    /**
     * state : 200
     * success : true
     * message : 操作成功
     * data : {"tigaseGroupUsers":{"total":8,"rows":[{"id":1876,"groupId":"5t_16_1264","jid":"5f_3476","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3476,"endUserId":null,"userName":"小君015","realName":"吴俊","sex":1,"email":"6667777@qq.com","phone":"13099897651","idCard":"256322199211023567","middleSchoolCertificate":"","collegeSpecializCertificate":"31243124321412414342","collegeSchoolCertificate":"","bachelorCertificate":"","address":"","userImg":"http://139.196.255.175/group1/M00/00/00/i8T_r1eav4-ABkqXAAC9C16mbx4944.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""},"userType":0},{"id":1877,"groupId":"5t_16_1264","jid":"5f_3501","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3501,"endUserId":null,"userName":"小君022","realName":null,"sex":null,"email":"1111111111111111@qq.com","phone":"19878788888","idCard":null,"middleSchoolCertificate":null,"collegeSpecializCertificate":null,"collegeSchoolCertificate":null,"bachelorCertificate":null,"address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1878,"groupId":"5t_16_1264","jid":"5f_3435","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3435,"endUserId":null,"userName":"小君001","realName":"李小君","sex":1,"email":"0000@qq.com","phone":"18971552234","idCard":"421088199206064532","middleSchoolCertificate":"765754675","collegeSpecializCertificate":"84675476571","collegeSchoolCertificate":"432421423","bachelorCertificate":"","address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1879,"groupId":"5t_16_1264","jid":"5f_3026","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3026,"endUserId":null,"userName":"fengq","realName":"fffeee","sex":2,"email":"","phone":"13309893029","idCard":"420106198610112250","middleSchoolCertificate":"asdfa","collegeSpecializCertificate":"","collegeSchoolCertificate":"","bachelorCertificate":"","address":"是的aa去111","userImg":"http://139.196.255.175/group1/M00/00/07/i8T_r1fsvmGAN5KZAAl5WLU-YRY995.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""},"userType":0},{"id":1880,"groupId":"5t_16_1264","jid":"5f_3495","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3495,"endUserId":null,"userName":null,"realName":"胖橙20","sex":2,"email":null,"phone":"","idCard":"420111201611157763","middleSchoolCertificate":"12","collegeSpecializCertificate":"23","collegeSchoolCertificate":"34","bachelorCertificate":"45","address":"","userImg":null,"politicsStatus":"","nation":"","fixPhone":"","postalCode":""},"userType":0},{"id":1881,"groupId":"5t_16_1264","jid":"5f_3450","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3450,"endUserId":null,"userName":"小君004","realName":"李小军","sex":1,"email":"2222222222222@qq.com","phone":"13268900000","idCard":"771213199412149900","middleSchoolCertificate":"5945803955","collegeSpecializCertificate":"54325432534254","collegeSchoolCertificate":"532412334214214524","bachelorCertificate":"","address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1882,"groupId":"5t_16_1264","jid":"5f_3438","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3438,"endUserId":null,"userName":"小君003","realName":"李君","sex":1,"email":"6656565@qq.com","phone":"13476768899","idCard":"421111197612013749","middleSchoolCertificate":"75464365636","collegeSpecializCertificate":"865746547567","collegeSchoolCertificate":"5432353246523454325","bachelorCertificate":"","address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1883,"groupId":"5t_16_1264","jid":"5b_106","isRecive":0,"backendOperatorVO":{"id":106,"userName":"wkd_xj","realName":"夏杰","photo":"http://d1.5xuexi.com/group1/M00/00/0E/i-AgdVfXwX6AcEGiAAGDBtyTrbQ539.jpg"},"endUserVO":null,"userType":1}],"pageSize":10,"pageNo":1}}
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
         * tigaseGroupUsers : {"total":8,"rows":[{"id":1876,"groupId":"5t_16_1264","jid":"5f_3476","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3476,"endUserId":null,"userName":"小君015","realName":"吴俊","sex":1,"email":"6667777@qq.com","phone":"13099897651","idCard":"256322199211023567","middleSchoolCertificate":"","collegeSpecializCertificate":"31243124321412414342","collegeSchoolCertificate":"","bachelorCertificate":"","address":"","userImg":"http://139.196.255.175/group1/M00/00/00/i8T_r1eav4-ABkqXAAC9C16mbx4944.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""},"userType":0},{"id":1877,"groupId":"5t_16_1264","jid":"5f_3501","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3501,"endUserId":null,"userName":"小君022","realName":null,"sex":null,"email":"1111111111111111@qq.com","phone":"19878788888","idCard":null,"middleSchoolCertificate":null,"collegeSpecializCertificate":null,"collegeSchoolCertificate":null,"bachelorCertificate":null,"address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1878,"groupId":"5t_16_1264","jid":"5f_3435","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3435,"endUserId":null,"userName":"小君001","realName":"李小君","sex":1,"email":"0000@qq.com","phone":"18971552234","idCard":"421088199206064532","middleSchoolCertificate":"765754675","collegeSpecializCertificate":"84675476571","collegeSchoolCertificate":"432421423","bachelorCertificate":"","address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1879,"groupId":"5t_16_1264","jid":"5f_3026","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3026,"endUserId":null,"userName":"fengq","realName":"fffeee","sex":2,"email":"","phone":"13309893029","idCard":"420106198610112250","middleSchoolCertificate":"asdfa","collegeSpecializCertificate":"","collegeSchoolCertificate":"","bachelorCertificate":"","address":"是的aa去111","userImg":"http://139.196.255.175/group1/M00/00/07/i8T_r1fsvmGAN5KZAAl5WLU-YRY995.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""},"userType":0},{"id":1880,"groupId":"5t_16_1264","jid":"5f_3495","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3495,"endUserId":null,"userName":null,"realName":"胖橙20","sex":2,"email":null,"phone":"","idCard":"420111201611157763","middleSchoolCertificate":"12","collegeSpecializCertificate":"23","collegeSchoolCertificate":"34","bachelorCertificate":"45","address":"","userImg":null,"politicsStatus":"","nation":"","fixPhone":"","postalCode":""},"userType":0},{"id":1881,"groupId":"5t_16_1264","jid":"5f_3450","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3450,"endUserId":null,"userName":"小君004","realName":"李小军","sex":1,"email":"2222222222222@qq.com","phone":"13268900000","idCard":"771213199412149900","middleSchoolCertificate":"5945803955","collegeSpecializCertificate":"54325432534254","collegeSchoolCertificate":"532412334214214524","bachelorCertificate":"","address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1882,"groupId":"5t_16_1264","jid":"5f_3438","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3438,"endUserId":null,"userName":"小君003","realName":"李君","sex":1,"email":"6656565@qq.com","phone":"13476768899","idCard":"421111197612013749","middleSchoolCertificate":"75464365636","collegeSpecializCertificate":"865746547567","collegeSchoolCertificate":"5432353246523454325","bachelorCertificate":"","address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1883,"groupId":"5t_16_1264","jid":"5b_106","isRecive":0,"backendOperatorVO":{"id":106,"userName":"wkd_xj","realName":"夏杰","photo":"http://d1.5xuexi.com/group1/M00/00/0E/i-AgdVfXwX6AcEGiAAGDBtyTrbQ539.jpg"},"endUserVO":null,"userType":1}],"pageSize":10,"pageNo":1}
         */

        private TigaseGroupUsersBean tigaseGroupUsers;

        public TigaseGroupUsersBean getTigaseGroupUsers() {
            return tigaseGroupUsers;
        }

        public void setTigaseGroupUsers(TigaseGroupUsersBean tigaseGroupUsers) {
            this.tigaseGroupUsers = tigaseGroupUsers;
        }

        public static class TigaseGroupUsersBean {
            /**
             * total : 8
             * rows : [{"id":1876,"groupId":"5t_16_1264","jid":"5f_3476","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3476,"endUserId":null,"userName":"小君015","realName":"吴俊","sex":1,"email":"6667777@qq.com","phone":"13099897651","idCard":"256322199211023567","middleSchoolCertificate":"","collegeSpecializCertificate":"31243124321412414342","collegeSchoolCertificate":"","bachelorCertificate":"","address":"","userImg":"http://139.196.255.175/group1/M00/00/00/i8T_r1eav4-ABkqXAAC9C16mbx4944.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""},"userType":0},{"id":1877,"groupId":"5t_16_1264","jid":"5f_3501","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3501,"endUserId":null,"userName":"小君022","realName":null,"sex":null,"email":"1111111111111111@qq.com","phone":"19878788888","idCard":null,"middleSchoolCertificate":null,"collegeSpecializCertificate":null,"collegeSchoolCertificate":null,"bachelorCertificate":null,"address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1878,"groupId":"5t_16_1264","jid":"5f_3435","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3435,"endUserId":null,"userName":"小君001","realName":"李小君","sex":1,"email":"0000@qq.com","phone":"18971552234","idCard":"421088199206064532","middleSchoolCertificate":"765754675","collegeSpecializCertificate":"84675476571","collegeSchoolCertificate":"432421423","bachelorCertificate":"","address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1879,"groupId":"5t_16_1264","jid":"5f_3026","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3026,"endUserId":null,"userName":"fengq","realName":"fffeee","sex":2,"email":"","phone":"13309893029","idCard":"420106198610112250","middleSchoolCertificate":"asdfa","collegeSpecializCertificate":"","collegeSchoolCertificate":"","bachelorCertificate":"","address":"是的aa去111","userImg":"http://139.196.255.175/group1/M00/00/07/i8T_r1fsvmGAN5KZAAl5WLU-YRY995.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""},"userType":0},{"id":1880,"groupId":"5t_16_1264","jid":"5f_3495","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3495,"endUserId":null,"userName":null,"realName":"胖橙20","sex":2,"email":null,"phone":"","idCard":"420111201611157763","middleSchoolCertificate":"12","collegeSpecializCertificate":"23","collegeSchoolCertificate":"34","bachelorCertificate":"45","address":"","userImg":null,"politicsStatus":"","nation":"","fixPhone":"","postalCode":""},"userType":0},{"id":1881,"groupId":"5t_16_1264","jid":"5f_3450","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3450,"endUserId":null,"userName":"小君004","realName":"李小军","sex":1,"email":"2222222222222@qq.com","phone":"13268900000","idCard":"771213199412149900","middleSchoolCertificate":"5945803955","collegeSpecializCertificate":"54325432534254","collegeSchoolCertificate":"532412334214214524","bachelorCertificate":"","address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1882,"groupId":"5t_16_1264","jid":"5f_3438","isRecive":0,"backendOperatorVO":null,"endUserVO":{"id":3438,"endUserId":null,"userName":"小君003","realName":"李君","sex":1,"email":"6656565@qq.com","phone":"13476768899","idCard":"421111197612013749","middleSchoolCertificate":"75464365636","collegeSpecializCertificate":"865746547567","collegeSchoolCertificate":"5432353246523454325","bachelorCertificate":"","address":null,"userImg":null,"politicsStatus":null,"nation":null,"fixPhone":null,"postalCode":null},"userType":0},{"id":1883,"groupId":"5t_16_1264","jid":"5b_106","isRecive":0,"backendOperatorVO":{"id":106,"userName":"wkd_xj","realName":"夏杰","photo":"http://d1.5xuexi.com/group1/M00/00/0E/i-AgdVfXwX6AcEGiAAGDBtyTrbQ539.jpg"},"endUserVO":null,"userType":1}]
             * pageSize : 10
             * pageNo : 1
             */

            private int total;
            private int pageSize;
            private int pageNo;
            private List<RowsBean> rows;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getPageNo() {
                return pageNo;
            }

            public void setPageNo(int pageNo) {
                this.pageNo = pageNo;
            }

            public List<RowsBean> getRows() {
                return rows;
            }

            public void setRows(List<RowsBean> rows) {
                this.rows = rows;
            }

            public static class RowsBean {
                /**
                 * id : 1876
                 * groupId : 5t_16_1264
                 * jid : 5f_3476
                 * isRecive : 0
                 * backendOperatorVO : null
                 * endUserVO : {"id":3476,"endUserId":null,"userName":"小君015","realName":"吴俊","sex":1,"email":"6667777@qq.com","phone":"13099897651","idCard":"256322199211023567","middleSchoolCertificate":"","collegeSpecializCertificate":"31243124321412414342","collegeSchoolCertificate":"","bachelorCertificate":"","address":"","userImg":"http://139.196.255.175/group1/M00/00/00/i8T_r1eav4-ABkqXAAC9C16mbx4944.jpg","politicsStatus":"","nation":"","fixPhone":"","postalCode":""}
                 * userType : 0
                 */

                private String groupId;
                private String jid;
                private EndUserVOBean endUserVO;
                private BackendOperatorVOBean backendOperatorVO;
                private Byte userType;

                public String getGroupId() {
                    return groupId;
                }

                public void setGroupId(String groupId) {
                    this.groupId = groupId;
                }

                public String getJid() {
                    return jid;
                }

                public void setJid(String jid) {
                    this.jid = jid;
                }

                public EndUserVOBean getEndUserVO() {
                    return endUserVO;
                }

                public void setEndUserVO(EndUserVOBean endUserVO) {
                    this.endUserVO = endUserVO;
                }

                public Byte getUserType() {
                    return userType;
                }

                public BackendOperatorVOBean getBackendOperatorVO() {
                    return backendOperatorVO;
                }

                public void setBackendOperatorVO(BackendOperatorVOBean backendOperatorVO) {
                    this.backendOperatorVO = backendOperatorVO;
                }

                public void setUserType(Byte userType) {
                    this.userType = userType;
                }

                public static class EndUserVOBean {
                    /**
                     * id : 3476
                     * endUserId : null
                     * userName : 小君015
                     * realName : 吴俊
                     * sex : 1
                     * email : 6667777@qq.com
                     * phone : 13099897651
                     * idCard : 256322199211023567
                     * middleSchoolCertificate :
                     * collegeSpecializCertificate : 31243124321412414342
                     * collegeSchoolCertificate :
                     * bachelorCertificate :
                     * address :
                     * userImg : http://139.196.255.175/group1/M00/00/00/i8T_r1eav4-ABkqXAAC9C16mbx4944.jpg
                     * politicsStatus :
                     * nation :
                     * fixPhone :
                     * postalCode :
                     */

                    private Long id;
                    private Long endUserId;
                    private String userName;
                    private String realName;
                    private String userImg;

                    public Long getId() {
                        return id;
                    }

                    public void setId(Long id) {
                        this.id = id;
                    }

                    public Long getEndUserId() {
                        return endUserId;
                    }

                    public void setEndUserId(Long endUserId) {
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

                    public String getUserImg() {
                        return userImg;
                    }

                    public void setUserImg(String userImg) {
                        this.userImg = userImg;
                    }

                    @Override
                    public String toString() {
                        return "EndUserVOBean{" +
                                "id=" + id +
                                ", endUserId=" + endUserId +
                                ", userName='" + userName + '\'' +
                                ", realName='" + realName + '\'' +
                                ", userImg='" + userImg + '\'' +
                                '}';
                    }
                }

                public static class BackendOperatorVOBean{

                    /**
                     * id : 106
                     * userName : wkd_xj
                     * realName : 夏杰
                     * photo : http://d1.5xuexi.com/group1/M00/00/0E/i-AgdVfXwX6AcEGiAAGDBtyTrbQ539.jpg
                     */

                    private Long id;
                    private String userName;
                    private String realName;
                    private String photo;

                    public Long getId() {
                        return id;
                    }

                    public void setId(Long id) {
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

                    @Override
                    public String toString() {
                        return "BackendOperatorVOBean{" +
                                "id=" + id +
                                ", userName='" + userName + '\'' +
                                ", realName='" + realName + '\'' +
                                ", photo='" + photo + '\'' +
                                '}';
                    }
                }
            }
        }
    }
}
