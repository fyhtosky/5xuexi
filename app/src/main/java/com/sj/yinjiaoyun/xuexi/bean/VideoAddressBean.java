package com.sj.yinjiaoyun.xuexi.bean;

/**
 * 作者：Administrator on 2018/4/26 09:07
 * <p>
 * 邮箱：xjs250@163.com
 */
public class VideoAddressBean {

    /**
     * state : 200
     * success : true
     * message : null
     * data : {"courseware":{"id":6,"courseId":1,"groupId":0,"isGroup":1,"coursewareName":"第一章","coursewareTime":0,"coursewareOrder":1,"coursewareVideoUrl":""}}
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
         * courseware : {"id":6,"courseId":1,"groupId":0,"isGroup":1,"coursewareName":"第一章","coursewareTime":0,"coursewareOrder":1,"coursewareVideoUrl":""}
         */

        private CoursewareBean courseware;

        public CoursewareBean getCourseware() {
            return courseware;
        }

        public void setCourseware(CoursewareBean courseware) {
            this.courseware = courseware;
        }

        public static class CoursewareBean {
            /**
             * id : 6
             * courseId : 1
             * groupId : 0
             * isGroup : 1
             * coursewareName : 第一章
             * coursewareTime : 0
             * coursewareOrder : 1
             * coursewareVideoUrl :
             */

            private int id;
            private int courseId;
            private int groupId;
            private int isGroup;
            private String coursewareName;
            private int coursewareTime;
            private int coursewareOrder;
            private String coursewareVideoUrl;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
            }

            public int getGroupId() {
                return groupId;
            }

            public void setGroupId(int groupId) {
                this.groupId = groupId;
            }

            public int getIsGroup() {
                return isGroup;
            }

            public void setIsGroup(int isGroup) {
                this.isGroup = isGroup;
            }

            public String getCoursewareName() {
                return coursewareName;
            }

            public void setCoursewareName(String coursewareName) {
                this.coursewareName = coursewareName;
            }

            public int getCoursewareTime() {
                return coursewareTime;
            }

            public void setCoursewareTime(int coursewareTime) {
                this.coursewareTime = coursewareTime;
            }

            public int getCoursewareOrder() {
                return coursewareOrder;
            }

            public void setCoursewareOrder(int coursewareOrder) {
                this.coursewareOrder = coursewareOrder;
            }

            public String getCoursewareVideoUrl() {
                return coursewareVideoUrl;
            }

            public void setCoursewareVideoUrl(String coursewareVideoUrl) {
                this.coursewareVideoUrl = coursewareVideoUrl;
            }
        }
    }
}
