package com.sj.yinjiaoyun.xuexi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/4/18.
 */
public class CollectMessageBean {

    /**
     * state : 200
     * success : true
     * message : 操作成功
     * data : {"msgEnshrine":{"total":4,"rows":[{"id":231,"jid":"5f_3435","senderJid":"5f_3435","receiverJid":"5b_95","msgContent":"2","msgType":0,"msgCreateTime":1491368981000,"md5":"d3da2d0886648b9e03509d1a48efc902","createTime":1491371911000,"updateTime":1491371911000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","senderName":"李小君","businessName":null,"parentName":null,"trainingItemName":null,"trainingType":null,"enrollPlanName":null,"productType":null,"openCourseType":null,"type":null,"isLef":null},{"id":230,"jid":"5f_3435","senderJid":"5f_3452","receiverJid":"5t_33","msgContent":"*$img_http://d1.5xuexi.com/group1/M00/00/17/i8T_r1jkXsSAaGLnAACJ_82p5IA933.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/17/i8T_r1jkXsqAAfqEAACPIlThCNM940.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXs-ARtQ-AABPOUMIFUU292.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXtaAW50DAACGzxMsepc391.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXtqAPMmDAADL-Cko5pg151.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXt-AcFbfAABrMyzkBRY160.jpg_gmi$*","msgType":1,"msgCreateTime":1491361537000,"md5":"010f4a2f0139ccf12bb7494cbce2c55f","createTime":1491371749000,"updateTime":1491371749000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0D/i8T_r1jI8z6AJ_8IAAAe9bl78HI140.jpg","senderName":"王小二","businessName":"书法欣赏","parentName":null,"trainingItemName":null,"trainingType":0,"enrollPlanName":null,"productType":null,"openCourseType":null,"type":2,"isLef":0},{"id":229,"jid":"5f_3435","senderJid":"5f_3452","receiverJid":"5o_118_1493","msgContent":"*$img_http://d1.5xuexi.com/group1/M00/00/17/i8T_r1jkVyuAGMbPAAAac0GgSlo949.png_gmi$*","msgType":1,"msgCreateTime":1491359559000,"md5":"fde12b1e6bf4a6ecea694b72160a5efb","createTime":1491371562000,"updateTime":1491371562000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0D/i8T_r1jI8z6AJ_8IAAAe9bl78HI140.jpg","senderName":"王小二","businessName":"公开课数学","parentName":null,"trainingItemName":null,"trainingType":null,"enrollPlanName":null,"productType":null,"openCourseType":0,"type":1,"isLef":0},{"id":228,"jid":"5f_3435","senderJid":"5f_3435","receiverJid":"5b_95","msgContent":"大厦大厦的","msgType":0,"msgCreateTime":1490321041000,"md5":"23a16ae1d0235008a76066e53ee7bfd8","createTime":1491371552000,"updateTime":1491371552000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","senderName":"李小君","businessName":null,"parentName":null,"trainingItemName":null,"trainingType":null,"enrollPlanName":null,"productType":null,"openCourseType":null,"type":null,"isLef":null}],"pageSize":10,"pageNo":1}}
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
         * msgEnshrine : {"total":4,"rows":[{"id":231,"jid":"5f_3435","senderJid":"5f_3435","receiverJid":"5b_95","msgContent":"2","msgType":0,"msgCreateTime":1491368981000,"md5":"d3da2d0886648b9e03509d1a48efc902","createTime":1491371911000,"updateTime":1491371911000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","senderName":"李小君","businessName":null,"parentName":null,"trainingItemName":null,"trainingType":null,"enrollPlanName":null,"productType":null,"openCourseType":null,"type":null,"isLef":null},{"id":230,"jid":"5f_3435","senderJid":"5f_3452","receiverJid":"5t_33","msgContent":"*$img_http://d1.5xuexi.com/group1/M00/00/17/i8T_r1jkXsSAaGLnAACJ_82p5IA933.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/17/i8T_r1jkXsqAAfqEAACPIlThCNM940.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXs-ARtQ-AABPOUMIFUU292.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXtaAW50DAACGzxMsepc391.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXtqAPMmDAADL-Cko5pg151.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXt-AcFbfAABrMyzkBRY160.jpg_gmi$*","msgType":1,"msgCreateTime":1491361537000,"md5":"010f4a2f0139ccf12bb7494cbce2c55f","createTime":1491371749000,"updateTime":1491371749000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0D/i8T_r1jI8z6AJ_8IAAAe9bl78HI140.jpg","senderName":"王小二","businessName":"书法欣赏","parentName":null,"trainingItemName":null,"trainingType":0,"enrollPlanName":null,"productType":null,"openCourseType":null,"type":2,"isLef":0},{"id":229,"jid":"5f_3435","senderJid":"5f_3452","receiverJid":"5o_118_1493","msgContent":"*$img_http://d1.5xuexi.com/group1/M00/00/17/i8T_r1jkVyuAGMbPAAAac0GgSlo949.png_gmi$*","msgType":1,"msgCreateTime":1491359559000,"md5":"fde12b1e6bf4a6ecea694b72160a5efb","createTime":1491371562000,"updateTime":1491371562000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0D/i8T_r1jI8z6AJ_8IAAAe9bl78HI140.jpg","senderName":"王小二","businessName":"公开课数学","parentName":null,"trainingItemName":null,"trainingType":null,"enrollPlanName":null,"productType":null,"openCourseType":0,"type":1,"isLef":0},{"id":228,"jid":"5f_3435","senderJid":"5f_3435","receiverJid":"5b_95","msgContent":"大厦大厦的","msgType":0,"msgCreateTime":1490321041000,"md5":"23a16ae1d0235008a76066e53ee7bfd8","createTime":1491371552000,"updateTime":1491371552000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","senderName":"李小君","businessName":null,"parentName":null,"trainingItemName":null,"trainingType":null,"enrollPlanName":null,"productType":null,"openCourseType":null,"type":null,"isLef":null}],"pageSize":10,"pageNo":1}
         */

        private MsgEnshrineBean msgEnshrine;

        public MsgEnshrineBean getMsgEnshrine() {
            return msgEnshrine;
        }

        public void setMsgEnshrine(MsgEnshrineBean msgEnshrine) {
            this.msgEnshrine = msgEnshrine;
        }

        public static class MsgEnshrineBean {
            /**
             * total : 4
             * rows : [{"id":231,"jid":"5f_3435","senderJid":"5f_3435","receiverJid":"5b_95","msgContent":"2","msgType":0,"msgCreateTime":1491368981000,"md5":"d3da2d0886648b9e03509d1a48efc902","createTime":1491371911000,"updateTime":1491371911000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","senderName":"李小君","businessName":null,"parentName":null,"trainingItemName":null,"trainingType":null,"enrollPlanName":null,"productType":null,"openCourseType":null,"type":null,"isLef":null},{"id":230,"jid":"5f_3435","senderJid":"5f_3452","receiverJid":"5t_33","msgContent":"*$img_http://d1.5xuexi.com/group1/M00/00/17/i8T_r1jkXsSAaGLnAACJ_82p5IA933.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/17/i8T_r1jkXsqAAfqEAACPIlThCNM940.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXs-ARtQ-AABPOUMIFUU292.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXtaAW50DAACGzxMsepc391.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXtqAPMmDAADL-Cko5pg151.jpg_gmi$**$img_http://d1.5xuexi.com/group1/M00/00/18/i8T_r1jkXt-AcFbfAABrMyzkBRY160.jpg_gmi$*","msgType":1,"msgCreateTime":1491361537000,"md5":"010f4a2f0139ccf12bb7494cbce2c55f","createTime":1491371749000,"updateTime":1491371749000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0D/i8T_r1jI8z6AJ_8IAAAe9bl78HI140.jpg","senderName":"王小二","businessName":"书法欣赏","parentName":null,"trainingItemName":null,"trainingType":0,"enrollPlanName":null,"productType":null,"openCourseType":null,"type":2,"isLef":0},{"id":229,"jid":"5f_3435","senderJid":"5f_3452","receiverJid":"5o_118_1493","msgContent":"*$img_http://d1.5xuexi.com/group1/M00/00/17/i8T_r1jkVyuAGMbPAAAac0GgSlo949.png_gmi$*","msgType":1,"msgCreateTime":1491359559000,"md5":"fde12b1e6bf4a6ecea694b72160a5efb","createTime":1491371562000,"updateTime":1491371562000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0D/i8T_r1jI8z6AJ_8IAAAe9bl78HI140.jpg","senderName":"王小二","businessName":"公开课数学","parentName":null,"trainingItemName":null,"trainingType":null,"enrollPlanName":null,"productType":null,"openCourseType":0,"type":1,"isLef":0},{"id":228,"jid":"5f_3435","senderJid":"5f_3435","receiverJid":"5b_95","msgContent":"大厦大厦的","msgType":0,"msgCreateTime":1490321041000,"md5":"23a16ae1d0235008a76066e53ee7bfd8","createTime":1491371552000,"updateTime":1491371552000,"isDeleted":0,"senderLogo":"http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg","senderName":"李小君","businessName":null,"parentName":null,"trainingItemName":null,"trainingType":null,"enrollPlanName":null,"productType":null,"openCourseType":null,"type":null,"isLef":null}]
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

            public static class RowsBean implements Serializable{
                /**
                 * id : 231
                 * jid : 5f_3435
                 * senderJid : 5f_3435
                 * receiverJid : 5b_95
                 * msgContent : 2
                 * msgType : 0
                 * msgCreateTime : 1491368981000
                 * md5 : d3da2d0886648b9e03509d1a48efc902
                 * createTime : 1491371911000
                 * updateTime : 1491371911000
                 * isDeleted : 0
                 * senderLogo : http://139.196.255.175/group1/M00/00/0A/i8T_r1hJDcCAVUBiAABqX5F3CpE442.jpg
                 * senderName : 李小君
                 * businessName : null
                 * parentName : null
                 * trainingItemName : null
                 * trainingType : null
                 * enrollPlanName : null
                 * productType : null
                 * openCourseType : null
                 * type : null
                 * isLef : null
                 */

                private int id;
                private String jid;
                private String senderJid;
                private String receiverJid;
                private String msgContent;
                private int msgType;
                private long msgCreateTime;
                private String md5;
                private long createTime;
                private long updateTime;
                private int isDeleted;
                private String senderLogo;
                private String senderName;
                private String businessName;
                private String parentName;
                private String trainingItemName;
                private Long trainingType;
                private String enrollPlanName;
                private Long productType;
                private Long openCourseType;
                private Long type;
                private Long isLef;


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

                public String getSenderJid() {
                    return senderJid;
                }

                public void setSenderJid(String senderJid) {
                    this.senderJid = senderJid;
                }

                public String getReceiverJid() {
                    return receiverJid;
                }

                public void setReceiverJid(String receiverJid) {
                    this.receiverJid = receiverJid;
                }

                public String getMsgContent() {
                    return msgContent;
                }

                public void setMsgContent(String msgContent) {
                    this.msgContent = msgContent;
                }

                public int getMsgType() {
                    return msgType;
                }

                public void setMsgType(int msgType) {
                    this.msgType = msgType;
                }

                public long getMsgCreateTime() {
                    return msgCreateTime;
                }

                public void setMsgCreateTime(long msgCreateTime) {
                    this.msgCreateTime = msgCreateTime;
                }

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
                }

                public long getUpdateTime() {
                    return updateTime;
                }

                public void setUpdateTime(long updateTime) {
                    this.updateTime = updateTime;
                }

                public long getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(long createTime) {
                    this.createTime = createTime;
                }

                public int getIsDeleted() {
                    return isDeleted;
                }

                public void setIsDeleted(int isDeleted) {
                    this.isDeleted = isDeleted;
                }

                public String getSenderName() {
                    return senderName;
                }

                public void setSenderName(String senderName) {
                    this.senderName = senderName;
                }

                public String getSenderLogo() {
                    return senderLogo;
                }

                public void setSenderLogo(String senderLogo) {
                    this.senderLogo = senderLogo;
                }

                public String getBusinessName() {
                    return businessName;
                }

                public void setBusinessName(String businessName) {
                    this.businessName = businessName;
                }

                public String getParentName() {
                    return parentName;
                }

                public void setParentName(String parentName) {
                    this.parentName = parentName;
                }

                public String getTrainingItemName() {
                    return trainingItemName;
                }

                public void setTrainingItemName(String trainingItemName) {
                    this.trainingItemName = trainingItemName;
                }

                public Long getTrainingType() {
                    return trainingType;
                }

                public void setTrainingType(Long trainingType) {
                    this.trainingType = trainingType;
                }

                public String getEnrollPlanName() {
                    return enrollPlanName;
                }

                public void setEnrollPlanName(String enrollPlanName) {
                    this.enrollPlanName = enrollPlanName;
                }

                public Long getProductType() {
                    return productType;
                }

                public void setProductType(Long productType) {
                    this.productType = productType;
                }

                public Long getOpenCourseType() {
                    return openCourseType;
                }

                public void setOpenCourseType(Long openCourseType) {
                    this.openCourseType = openCourseType;
                }

                public Long getType() {
                    return type;
                }

                public void setType(Long type) {
                    this.type = type;
                }

                public Long getIsLef() {
                    return isLef;
                }

                public void setIsLef(Long isLef) {
                    this.isLef = isLef;
                }

                @Override
                public String toString() {
                    return "RowsBean{" +
                            "id=" + id +
                            ", jid='" + jid + '\'' +
                            ", senderJid='" + senderJid + '\'' +
                            ", receiverJid='" + receiverJid + '\'' +
                            ", msgContent='" + msgContent + '\'' +
                            ", msgType=" + msgType +
                            ", msgCreateTime=" + msgCreateTime +
                            ", md5='" + md5 + '\'' +
                            ", createTime=" + createTime +
                            ", updateTime=" + updateTime +
                            ", isDeleted=" + isDeleted +
                            ", senderLogo='" + senderLogo + '\'' +
                            ", senderName='" + senderName + '\'' +
                            ", businessName='" + businessName + '\'' +
                            ", parentName='" + parentName + '\'' +
                            ", trainingItemName='" + trainingItemName + '\'' +
                            ", trainingType=" + trainingType +
                            ", enrollPlanName='" + enrollPlanName + '\'' +
                            ", productType=" + productType +
                            ", openCourseType=" + openCourseType +
                            ", type=" + type +
                            ", isLef=" + isLef +
                            '}';
                }
            }
        }
    }
}
