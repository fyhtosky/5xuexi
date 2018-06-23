package com.sj.yinjiaoyun.xuexi.bean;

import java.util.List;

/**
 * Created by wanzhiying on 2017/3/7.
 */
public class RecentChatBean {

    /**
     * state : 200
     * success : true
     * message : 操作成功！
     * data : {"msgPg":{"total":1,"rows":[{"id":null,"sender":"5b_72","receiver":"5f_3376","msg":"你好","system":null,"msgType":0,"createTime":1488856656605,"msgCount":1,"msgName":"赵淑霞","msgLogo":""}],"pageSize":10,"pageNo":1}}
     */

    private int state;
    private boolean success;
    private String message;
    public DataBean data;

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
         * msgPg : {"total":1,"rows":[{"id":null,"sender":"5b_72","receiver":"5f_3376","msg":"你好","system":null,"msgType":0,"createTime":1488856656605,"msgCount":1,"msgName":"赵淑霞","msgLogo":""}],"pageSize":10,"pageNo":1}
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
             * total : 1
             * rows : [{"id":null,"sender":"5b_72","receiver":"5f_3376","msg":"你好","system":null,"msgType":0,"createTime":1488856656605,"msgCount":1,"msgName":"赵淑霞","msgLogo":""}]
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

            public static class RowsBean{
                /**
                 * id : null
                 * sender : 5b_72
                 * receiver : 5f_3376
                 * msg : 你好
                 * system : null
                 * msgType : 0
                 * createTime : 1488856656605
                 * msgCount : 1
                 * msgName : 赵淑霞
                 * msgLogo :
                 */

                private String id;
                private String sender;
                private String receiver;
                private String msg;
                private String system;
                private int msgType;
                private long createTime;
                private int msgCount;
                private String msgName;
                private String msgLogo;
                private String senderName;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
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

                public String getSystem() {
                    return system;
                }

                public void setSystem(String system) {
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
                            "id='" + id + '\'' +
                            ", sender='" + sender + '\'' +
                            ", receiver='" + receiver + '\'' +
                            ", msg='" + msg + '\'' +
                            ", system='" + system + '\'' +
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
