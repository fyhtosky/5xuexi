package com.sj.yinjiaoyun.xuexi.bean;

/**
 * Created by wanzhiying on 2017/3/27.
 */
public class GroupinfoBean {

    /**
     * state : 200
     * success : true
     * message : 操作成功
     * data : {"groupChat":{"id":1058,"parentId":1048,"isNotDisturb":0,"groupId":"5p_304_1580","isLef":1,"businessId":1580,"type":0,"createTime":1489635855000,"businessName":"小学美术","businessImg":"http://139.196.255.175/group1/M00/00/0B/i8T_r1hXrw-AMsyMAACV-1L-h60611.jpg","trainingItemName":null,"trainingType":null,"enrollPlanName":"IM简单的开发-南京大学招生66666","productType":null,"openCourseType":null,"collegeName":"南京大学"}}
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
         * groupChat : {"id":1058,"parentId":1048,"isNotDisturb":0,"groupId":"5p_304_1580","isLef":1,"businessId":1580,"type":0,"createTime":1489635855000,"businessName":"小学美术","businessImg":"http://139.196.255.175/group1/M00/00/0B/i8T_r1hXrw-AMsyMAACV-1L-h60611.jpg","trainingItemName":null,"trainingType":null,"enrollPlanName":"IM简单的开发-南京大学招生66666","productType":null,"openCourseType":null,"collegeName":"南京大学"}
         */

        private GroupChatBean groupChat;

        public GroupChatBean getGroupChat() {
            return groupChat;
        }

        public void setGroupChat(GroupChatBean groupChat) {
            this.groupChat = groupChat;
        }

        public static class GroupChatBean {
            /**
             * id : 1058
             * parentId : 1048
             * isNotDisturb : 0
             * groupId : 5p_304_1580
             * isLef : 1
             * businessId : 1580
             * type : 0
             * createTime : 1489635855000
             * businessName : 小学美术
             * businessImg : http://139.196.255.175/group1/M00/00/0B/i8T_r1hXrw-AMsyMAACV-1L-h60611.jpg
             * trainingItemName : null
             * trainingType : null
             * enrollPlanName : IM简单的开发-南京大学招生66666
             * productType : null
             * openCourseType : null
             * collegeName : 南京大学
             */

            private int id;
            private int parentId;
            private int isNotDisturb;
            private String groupId;
            private int isLef;
            private int businessId;
            private int type;
            private long createTime;
            private String businessName;
            private String businessImg;
            private String trainingItemName;
            private String trainingType;
            private String enrollPlanName;
            private String productType;
            private String openCourseType;
            private String collegeName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getIsNotDisturb() {
                return isNotDisturb;
            }

            public void setIsNotDisturb(int isNotDisturb) {
                this.isNotDisturb = isNotDisturb;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }

            public int getIsLef() {
                return isLef;
            }

            public void setIsLef(int isLef) {
                this.isLef = isLef;
            }

            public int getBusinessId() {
                return businessId;
            }

            public void setBusinessId(int businessId) {
                this.businessId = businessId;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getBusinessName() {
                return businessName;
            }

            public void setBusinessName(String businessName) {
                this.businessName = businessName;
            }

            public String getBusinessImg() {
                return businessImg;
            }

            public void setBusinessImg(String businessImg) {
                this.businessImg = businessImg;
            }

            public String getTrainingItemName() {
                return trainingItemName;
            }

            public void setTrainingItemName(String trainingItemName) {
                this.trainingItemName = trainingItemName;
            }

            public String getTrainingType() {
                return trainingType;
            }

            public void setTrainingType(String trainingType) {
                this.trainingType = trainingType;
            }

            public String getEnrollPlanName() {
                return enrollPlanName;
            }

            public void setEnrollPlanName(String enrollPlanName) {
                this.enrollPlanName = enrollPlanName;
            }

            public String getProductType() {
                return productType;
            }

            public void setProductType(String productType) {
                this.productType = productType;
            }

            public String getOpenCourseType() {
                return openCourseType;
            }

            public void setOpenCourseType(String openCourseType) {
                this.openCourseType = openCourseType;
            }

            public String getCollegeName() {
                return collegeName;
            }

            public void setCollegeName(String collegeName) {
                this.collegeName = collegeName;
            }

            @Override
            public String toString() {
                return "GroupChatBean{" +
                        "id=" + id +
                        ", parentId=" + parentId +
                        ", isNotDisturb=" + isNotDisturb +
                        ", groupId='" + groupId + '\'' +
                        ", isLef=" + isLef +
                        ", businessId=" + businessId +
                        ", type=" + type +
                        ", createTime=" + createTime +
                        ", businessName='" + businessName + '\'' +
                        ", businessImg='" + businessImg + '\'' +
                        ", trainingItemName='" + trainingItemName + '\'' +
                        ", trainingType='" + trainingType + '\'' +
                        ", enrollPlanName='" + enrollPlanName + '\'' +
                        ", productType='" + productType + '\'' +
                        ", openCourseType='" + openCourseType + '\'' +
                        ", collegeName='" + collegeName + '\'' +
                        '}';
            }
        }
    }
}
