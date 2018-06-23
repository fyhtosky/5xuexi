package com.sj.yinjiaoyun.xuexi.bean;

import java.util.List;

/**
 * Created by wanzhiying on 2017/3/15.
 */
public class ChatPrivateBean  {

    /**
     * state : 200
     * success : true
     * message : 操作成功！
     * data : {"msgPg":{"total":22,"rows":[{"id":156,"sender":"5f_3494","receiver":"5f_3435","msg":"爱上的","system":1,"msgType":0,"createTime":1489473692000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":155,"sender":"5f_3494","receiver":"5f_3435","msg":"ok","system":1,"msgType":0,"createTime":1489473402000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":154,"sender":"5f_3494","receiver":"5f_3435","msg":"爱上","system":1,"msgType":0,"createTime":1489473293000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":153,"sender":"5f_3494","receiver":"5f_3435","msg":"撒爱上的","system":1,"msgType":0,"createTime":1489473151000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":152,"sender":"5f_3494","receiver":"5f_3435","msg":"阿达","system":1,"msgType":0,"createTime":1489472902000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":151,"sender":"5f_3494","receiver":"5f_3435","msg":"爱上大厦","system":1,"msgType":0,"createTime":1489472747000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":150,"sender":"5f_3494","receiver":"5f_3435","msg":" 撒大的","system":1,"msgType":0,"createTime":1489472741000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":149,"sender":"5f_3494","receiver":"5f_3435","msg":"俺说的","system":1,"msgType":0,"createTime":1489472618000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":147,"sender":"5f_3494","receiver":"5f_3435","msg":"啊大大厦","system":1,"msgType":0,"createTime":1489472395000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":148,"sender":"5f_3494","receiver":"5f_3435","msg":"啊大大厦","system":1,"msgType":0,"createTime":1489472395000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null}],"pageSize":10,"pageNo":1}}
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
         * msgPg : {"total":22,"rows":[{"id":156,"sender":"5f_3494","receiver":"5f_3435","msg":"爱上的","system":1,"msgType":0,"createTime":1489473692000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":155,"sender":"5f_3494","receiver":"5f_3435","msg":"ok","system":1,"msgType":0,"createTime":1489473402000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":154,"sender":"5f_3494","receiver":"5f_3435","msg":"爱上","system":1,"msgType":0,"createTime":1489473293000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":153,"sender":"5f_3494","receiver":"5f_3435","msg":"撒爱上的","system":1,"msgType":0,"createTime":1489473151000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":152,"sender":"5f_3494","receiver":"5f_3435","msg":"阿达","system":1,"msgType":0,"createTime":1489472902000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":151,"sender":"5f_3494","receiver":"5f_3435","msg":"爱上大厦","system":1,"msgType":0,"createTime":1489472747000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":150,"sender":"5f_3494","receiver":"5f_3435","msg":" 撒大的","system":1,"msgType":0,"createTime":1489472741000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":149,"sender":"5f_3494","receiver":"5f_3435","msg":"俺说的","system":1,"msgType":0,"createTime":1489472618000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":147,"sender":"5f_3494","receiver":"5f_3435","msg":"啊大大厦","system":1,"msgType":0,"createTime":1489472395000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":148,"sender":"5f_3494","receiver":"5f_3435","msg":"啊大大厦","system":1,"msgType":0,"createTime":1489472395000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null}],"pageSize":10,"pageNo":1}
         */

        private MsgPgBean msgPg;

        public MsgPgBean getMsgPg() {
            return msgPg;
        }

        public void setMsgPg(MsgPgBean msgPg) {
            this.msgPg = msgPg;
        }

        public static class MsgPgBean {
            /**
             * total : 22
             * rows : [{"id":156,"sender":"5f_3494","receiver":"5f_3435","msg":"爱上的","system":1,"msgType":0,"createTime":1489473692000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":155,"sender":"5f_3494","receiver":"5f_3435","msg":"ok","system":1,"msgType":0,"createTime":1489473402000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":154,"sender":"5f_3494","receiver":"5f_3435","msg":"爱上","system":1,"msgType":0,"createTime":1489473293000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":153,"sender":"5f_3494","receiver":"5f_3435","msg":"撒爱上的","system":1,"msgType":0,"createTime":1489473151000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":152,"sender":"5f_3494","receiver":"5f_3435","msg":"阿达","system":1,"msgType":0,"createTime":1489472902000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":151,"sender":"5f_3494","receiver":"5f_3435","msg":"爱上大厦","system":1,"msgType":0,"createTime":1489472747000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":150,"sender":"5f_3494","receiver":"5f_3435","msg":" 撒大的","system":1,"msgType":0,"createTime":1489472741000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":149,"sender":"5f_3494","receiver":"5f_3435","msg":"俺说的","system":1,"msgType":0,"createTime":1489472618000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":147,"sender":"5f_3494","receiver":"5f_3435","msg":"啊大大厦","system":1,"msgType":0,"createTime":1489472395000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null},{"id":148,"sender":"5f_3494","receiver":"5f_3435","msg":"啊大大厦","system":1,"msgType":0,"createTime":1489472395000,"msgCount":0,"msgName":"testtliu19","msgLogo":null,"senderName":null}]
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
                 * id : 156
                 * sender : 5f_3494
                 * receiver : 5f_3435
                 * msg : 爱上的
                 * system : 1
                 * msgType : 0
                 * createTime : 1489473692000
                 * msgCount : 0
                 * msgName : testtliu19
                 * msgLogo : null
                 * senderName : null
                 */

                private Long id;
                private String sender;
                private String receiver;
                private String msg;
                private int system;
                private int msgType;
                private long createTime;
                private int msgCount;
                private String msgName;
                private String msgLogo;
                private String senderName;

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public String getSender() {
                    return sender;
                }

                public void setSender(String sender) {
                    this.sender = sender;
                }

                public String getReceiver() {
                    return receiver;
                }

                public void setReceiver(String receiver) {
                    this.receiver = receiver;
                }

                public String getMsg() {
                    return msg;
                }

                public void setMsg(String msg) {
                    this.msg = msg;
                }

                public int getSystem() {
                    return system;
                }

                public void setSystem(int system) {
                    this.system = system;
                }

                public int getMsgType() {
                    return msgType;
                }

                public void setMsgType(int msgType) {
                    this.msgType = msgType;
                }

                public long getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(long createTime) {
                    this.createTime = createTime;
                }

                public int getMsgCount() {
                    return msgCount;
                }

                public void setMsgCount(int msgCount) {
                    this.msgCount = msgCount;
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

                public String getSenderName() {
                    return senderName;
                }

                public void setSenderName(String senderName) {
                    this.senderName = senderName;
                }

                @Override
                public String toString() {
                    return "MessageBean{" +
                            "id=" + id +
                            ", sender='" + sender + '\'' +
                            ", receiver='" + receiver + '\'' +
                            ", msg='" + msg + '\'' +
                            ", system=" + system +
                            ", msgType=" + msgType +
                            ", createTime=" + createTime +
                            ", msgCount=" + msgCount +
                            ", msgName='" + msgName + '\'' +
                            ", msgLogo='" + msgLogo + '\'' +
                            ", senderName='" + senderName + '\'' +
                            '}';
                }
            }
        }
    }
}
